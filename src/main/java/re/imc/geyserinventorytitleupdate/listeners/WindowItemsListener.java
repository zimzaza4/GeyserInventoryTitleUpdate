package re.imc.geyserinventorytitleupdate.listeners;

import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.listener.AbstractPacketListener;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;
import dev.simplix.protocolize.api.player.ProtocolizePlayer;
import dev.simplix.protocolize.data.packets.CloseWindow;
import dev.simplix.protocolize.data.packets.WindowItems;
import org.geysermc.floodgate.api.FloodgateApi;
import re.imc.geyserinventorytitleupdate.GeyserInventoryTitleUpdate;

import java.util.concurrent.TimeUnit;

public class WindowItemsListener extends AbstractPacketListener<WindowItems> {
        public WindowItemsListener() {
                super(WindowItems.class, Direction.DOWNSTREAM, 60);
        }

        @Override
        public void packetReceive(PacketReceiveEvent<WindowItems> event) {
                ProtocolizePlayer player = event.player();
                if (player == null) {
                        return;
                }

                if (FloodgateApi.getInstance().isFloodgatePlayer(event.player().uniqueId())
                        && GeyserInventoryTitleUpdate.hasOpenWindow.contains(event.player().uniqueId())) {


                        if (GeyserInventoryTitleUpdate.windowIds.containsKey(event.player().uniqueId())) {
                                event.cancelled(true);

                                GeyserInventoryTitleUpdate.getServer()
                                        .getScheduler()
                                        .buildTask(GeyserInventoryTitleUpdate.getInstance(), () -> {
                                                event.player().sendPacket(event.packet());
                                        })
                                        .delay(20, TimeUnit.MILLISECONDS)
                                        .schedule();
                        }


                }
        }

        @Override
        public void packetSend(PacketSendEvent<WindowItems> packetSendEvent) {

        }


}
