package re.imc.geyserinventorytitleupdate.listeners;

import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.listener.AbstractPacketListener;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;
import dev.simplix.protocolize.data.packets.CloseWindow;
import re.imc.geyserinventorytitleupdate.GeyserInventoryTitleUpdate;

public class WindowCloseListener extends AbstractPacketListener<CloseWindow> {
    public WindowCloseListener() {
        super(CloseWindow.class, Direction.DOWNSTREAM,0);
    }

    @Override
    public void packetReceive(PacketReceiveEvent<CloseWindow> packetReceiveEvent) {
        if (packetReceiveEvent.player() != null) {
            GeyserInventoryTitleUpdate.hasOpenWindow.add(packetReceiveEvent.player().uniqueId());
        }
    }

    @Override
    public void packetSend(PacketSendEvent<CloseWindow> packetSendEvent) {
        if (packetSendEvent.player() != null) {

            GeyserInventoryTitleUpdate.hasOpenWindow.add(packetSendEvent.player().uniqueId());
        }
    }
}
