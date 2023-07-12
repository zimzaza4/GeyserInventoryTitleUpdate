package re.imc.geyserinventorytitleupdate.listeners;

import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.listener.AbstractPacketListener;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;
import dev.simplix.protocolize.data.packets.CloseWindow;
import dev.simplix.protocolize.data.packets.OpenWindow;
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

        if (FloodgateApi.getInstance().isFloodgatePlayer(event.player().uniqueId())
                && GeyserInventoryTitleUpdate.hasOpenWindow.contains(event.player().uniqueId())) {


            if (GeyserInventoryTitleUpdate.windowIds.containsKey(event.player().uniqueId())) {

                event.cancelled(true);

                event.player().sendPacket(new CloseWindow(GeyserInventoryTitleUpdate.windowIds.get(event.player().uniqueId())));

                
                GeyserInventoryTitleUpdate.windowSetup.add(event.player().uniqueId());
                GeyserInventoryTitleUpdate.getServer()
                        .getScheduler()
                        .buildTask(GeyserInventoryTitleUpdate.getInstance(), () -> {

                            GeyserInventoryTitleUpdate.hasOpenWindow.add(event.player().uniqueId());
                            
                            event.player().sendPacket(event.packet());
                            GeyserInventoryTitleUpdate.getServer()
                                    .getScheduler()
                                    .buildTask(GeyserInventoryTitleUpdate.getInstance(), () -> {
                                        
                                        GeyserInventoryTitleUpdate.windowSetup.remove(event.player().uniqueId());
                                    })
                                    .delay(20, TimeUnit.MILLISECONDS)
                                    .schedule();
                        })
                        .delay(5, TimeUnit.MILLISECONDS)
                        .schedule();
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
