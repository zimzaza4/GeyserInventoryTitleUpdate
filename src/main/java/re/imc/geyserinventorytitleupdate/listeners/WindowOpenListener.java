package re.imc.geyserinventorytitleupdate.listeners;

import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.listener.AbstractPacketListener;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;
import dev.simplix.protocolize.data.inventory.InventoryType;
import dev.simplix.protocolize.data.packets.CloseWindow;
import dev.simplix.protocolize.data.packets.OpenWindow;
import dev.simplix.protocolize.data.packets.WindowItems;
import org.geysermc.floodgate.api.FloodgateApi;
import re.imc.geyserinventorytitleupdate.GeyserInventoryTitleUpdate;

import java.util.concurrent.TimeUnit;

public class WindowOpenListener extends AbstractPacketListener<OpenWindow> {


    public WindowOpenListener() {
        super(OpenWindow.class, Direction.DOWNSTREAM, 0);
    }

    @Override
    public void packetReceive(PacketReceiveEvent<OpenWindow> event) {

        if (event.player() == null) {
            return;
        }

        if (!FloodgateApi.getInstance().isFloodgatePlayer(event.player().uniqueId())) {
            return;
        }

        InventoryType originalType = GeyserInventoryTitleUpdate.windowTypes.get(event.player().uniqueId());
        String originalTitle = GeyserInventoryTitleUpdate.windowTitles.get(event.player().uniqueId());
        GeyserInventoryTitleUpdate.windowTypes.put(event.player().uniqueId(), event.packet().inventoryType());
        GeyserInventoryTitleUpdate.windowTitles.put(event.player().uniqueId(), event.packet().titleJson());

        if (originalType != null) {
            if (originalType != event.packet().inventoryType()) {
                return;
            }
        }

        if (originalTitle != null) {
            if (originalTitle.equals(event.packet().titleJson())) {
                return;
            }
        }

        if (GeyserInventoryTitleUpdate.hasOpenWindow.contains(event.player().uniqueId())) {


            if (GeyserInventoryTitleUpdate.windowIds.containsKey(event.player().uniqueId())) {

                if (GeyserInventoryTitleUpdate.windowIds.get(event.player().uniqueId()) == event.packet().windowId()) {
                    return;
                }

                event.cancelled(true);


                
                
                GeyserInventoryTitleUpdate.windowSetup.add(event.player().uniqueId());

                event.player().sendPacket(new CloseWindow(GeyserInventoryTitleUpdate.windowIds.get(event.player().uniqueId())));



                /*
                GeyserInventoryTitleUpdate.getServer()
                        .getScheduler()
                        .buildTask(GeyserInventoryTitleUpdate.getInstance(), () -> {


                 */

                            GeyserInventoryTitleUpdate.hasOpenWindow.add(event.player().uniqueId());
                            
                            event.player().sendPacket(event.packet());
                            GeyserInventoryTitleUpdate.getServer()
                                    .getScheduler()
                                    .buildTask(GeyserInventoryTitleUpdate.getInstance(), () -> {
                                        
                                        GeyserInventoryTitleUpdate.windowSetup.remove(event.player().uniqueId());
                                    })
                                    .delay(80, TimeUnit.MILLISECONDS)
                                    .schedule();
                        /*})
                        .delay(0, TimeUnit.MILLISECONDS)
                        .schedule();

                         */
            }
            GeyserInventoryTitleUpdate.windowIds.put(event.player().uniqueId(), event.packet().windowId());

        } else {

            GeyserInventoryTitleUpdate.hasOpenWindow.add(event.player().uniqueId());
            GeyserInventoryTitleUpdate.windowIds.put(event.player().uniqueId(), event.packet().windowId());

        }

    }

    public void packetSend(PacketSendEvent<OpenWindow> event) {

    }
}
