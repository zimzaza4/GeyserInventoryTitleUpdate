package re.imc.geyserinventorytitleupdate;

import com.google.inject.Inject;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.simplix.protocolize.api.Protocolize;
import lombok.Getter;
import org.slf4j.Logger;
import re.imc.geyserinventorytitleupdate.listeners.WindowOpenListener;
import re.imc.geyserinventorytitleupdate.listeners.SetSlotListener;
import re.imc.geyserinventorytitleupdate.listeners.WindowCloseListener;
import re.imc.geyserinventorytitleupdate.listeners.WindowItemsListener;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Plugin(
        id = "geyserinventorytitleupdate",
        name = "GeyserInventoryTitleUpdate",
        version = "1.0-SNAPSHOT",
        dependencies = {@Dependency(id = "protocolize")}
)
public class GeyserInventoryTitleUpdate {

    public static Set<UUID> hasOpenWindow = new HashSet<>();
    public static Map<UUID, Integer> windowIds = new ConcurrentHashMap<>();
    public static Set<UUID> windowSetup = new ConcurrentSkipListSet<>();


    @Getter
    private static GeyserInventoryTitleUpdate instance;

    @Getter
    private static ProxyServer server;

    @Inject
    public GeyserInventoryTitleUpdate(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        GeyserInventoryTitleUpdate.server = server;
        instance = this;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        Protocolize.listenerProvider().registerListener(new WindowOpenListener());
        Protocolize.listenerProvider().registerListener(new WindowCloseListener());
        Protocolize.listenerProvider().registerListener(new WindowItemsListener());
        Protocolize.listenerProvider().registerListener(new SetSlotListener());
    }
    @Subscribe
    public void onQuit(DisconnectEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        hasOpenWindow.remove(uuid);
        windowSetup.remove(uuid);
        windowIds.remove(uuid);
    }
}
