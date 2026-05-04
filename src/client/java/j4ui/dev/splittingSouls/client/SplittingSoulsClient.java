package j4ui.dev.splittingSouls.client;

import j4ui.dev.splittingSouls.ModEntities;
import j4ui.dev.splittingSouls.client.entity.SoulCloneEntityRenderer;
import j4ui.dev.splittingSouls.network.SplitResultPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class SplittingSoulsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SoulStretchHandler handler = new SoulStretchHandler();
        SoulStretchHandler.INSTANCE = handler;
        ClientTickEvents.END_CLIENT_TICK.register(handler::tick);

        SoulStretchHudRenderer.register();
        EntityRendererRegistry.register(ModEntities.SOUL_CLONE, SoulCloneEntityRenderer::new);

        ClientPlayNetworking.registerGlobalReceiver(SplitResultPayload.ID, (payload, context) ->
                context.client().execute(() -> {
                    if (context.client().currentScreen == null) {
                        context.client().setScreen(new SoulSelectScreen());
                    }
                }));
    }
}
