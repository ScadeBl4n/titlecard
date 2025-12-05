package net.scade.titlecard;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.Window;
import net.scade.titlecard.config.TitleConfig;

import java.util.Collection;



public class TitleCardClient implements ClientModInitializer {
    private static boolean enabled = true;
    private static long lastUpdateTime = 0;
    private static final long UPDATE_INTERVAL = 1000;

    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(TitleCardClient::updateWindowTitle);

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            updateWindowTitle(client);
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            updateWindowTitle(client);
        });
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdateTime > UPDATE_INTERVAL) {
                MinecraftClient client = MinecraftClient.getInstance();
                updateWindowTitle(client);
                lastUpdateTime = currentTime;
            }
        });
    }

    public static void updateWindowTitle(MinecraftClient client) {
        if (!TitleCardClient.enabled || client == null) return;

        Window window = client.getWindow();
        String formatted = formatTitle(client);
        window.setTitle(formatted);
    }

    private static String formatTitle(MinecraftClient client) {
        String username = client.getSession().getUsername();
        ServerInfo serverEntry = MinecraftClient.getInstance().getCurrentServerEntry();
        String mcName = "Minecraft";
        String version = SharedConstants.getGameVersion().getName();
        String state = "";
        String stateWithDash = "";
        String fps = String.valueOf(client.getCurrentFps());
        String ipn = "";
        String ipa = "";
        if (serverEntry != null) {
            ipn = serverEntry.name;
            ipa = serverEntry.address;
        }
        String modCount;


        Collection<ModContainer> allMods = FabricLoader.getInstance().getAllMods();
        modCount = String.valueOf(allMods.size());


        ClientPlayNetworkHandler clientPlayNetworkHandler = client.getNetworkHandler();
        if (clientPlayNetworkHandler != null && clientPlayNetworkHandler.getConnection().isOpen()) {
            if (client.isInSingleplayer()) {
                state = I18n.translate("title.singleplayer");
            } else if (client.isConnectedToRealms()) {
                state = I18n.translate("title.multiplayer.realms");
            } else if (client.getCurrentServerEntry() == null || !client.getCurrentServerEntry().isLocal()) {
                state = I18n.translate("title.multiplayer.other");
            } else {
                state = I18n.translate("title.multiplayer.lan");
            }
            //noinspection StringTemplateMigration
            stateWithDash = "- " + state;
        }

        return TitleConfig.config.formatPattern
                .replace("%M", mcName)
                .replace("%V", version)
                .replace("%S", username)
                .replace("%I", ipn)
                .replace("%i", ipa)
                .replace("%l", modCount)
                .replace("%F", fps)
                .replace("%s", stateWithDash)
                .replace("%d", state);
    }
}