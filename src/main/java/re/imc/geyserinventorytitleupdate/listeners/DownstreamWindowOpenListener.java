package re.imc.geyserinventorytitleupdate.listeners;

import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.listener.AbstractPacketListener;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;
import dev.simplix.protocolize.data.packets.CloseWindow;
import dev.simplix.protocolize.data.packets.OpenWindow;
import org.geysermc.floodgate.api.FloodgateApi;
import re.imc.geyserinventorytitleupdate.GeyserInventoryTitleUpdate;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class DownstreamWindowOpenListener extends AbstractPacketListener<OpenWindow>{


    public DownstreamWindowOpenListener() {
        super(OpenWindow.class, Direction.DOWNSTREAM, 0);
    }

    @Override
    public void packetReceive(PacketReceiveEvent<OpenWindow> event) {

        if (event.player() == null) {
            return;
        }

        System.out.println("received");

        if (FloodgateApi.getInstance().isFloodgatePlayer(event.player().uniqueId())
                && GeyserInventoryTitleUpdate.hasOpenWindow.contains(event.player().uniqueId())) {


            if (GeyserInventoryTitleUpdate.windowIds.containsKey(event.player().uniqueId())) {

                event.cancelled(true);

                event.player().sendPacket(new CloseWindow(GeyserInventoryTitleUpdate.windowIds.get(event.player().uniqueId())));

                GeyserInventoryTitleUpdate.getServer()
                        .getScheduler()
                        .buildTask(GeyserInventoryTitleUpdate.getInstance(), () -> {
                            System.out.println("send");
                            event.player().sendPacket(event.packet());
                        })
                        .delay(10, TimeUnit.MILLISECONDS)
                        .schedule();
            }
            GeyserInventoryTitleUpdate.windowIds.put(event.player().uniqueId(), event.packet().windowId());

        } else {
            System.out.println("first");
            GeyserInventoryTitleUpdate.hasOpenWindow.add(event.player().uniqueId());
            GeyserInventoryTitleUpdate.windowIds.put(event.player().uniqueId(), event.packet().windowId());

        }

    }

    public void packetSend(PacketSendEvent<OpenWindow> event) {

    }
}
