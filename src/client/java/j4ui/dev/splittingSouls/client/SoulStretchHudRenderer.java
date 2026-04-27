package j4ui.dev.splittingSouls.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class SoulStretchHudRenderer {

    public static void register() {
        HudRenderCallback.EVENT.register(SoulStretchHudRenderer::render);
    }

    private static void render(DrawContext context, RenderTickCounter tickCounter) {
        SoulStretchHandler handler = SoulStretchHandler.INSTANCE;
        if (handler == null) return;

        int ticks = handler.getStretchTicks();
        int dir = handler.getLastDirection();
        if (dir == -1 || ticks == 0) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        int sw = mc.getWindow().getScaledWidth();
        int sh = mc.getWindow().getScaledHeight();
        float progress = (float) ticks / SoulStretchHandler.REQUIRED_TICKS;

        int alpha = (int) (progress * 200);
        int barColor = (alpha << 24) | 0x6A3FD2;
        int lineColor = 0xFFFFFFFF;

        if (dir == 0) {
            int barH = (int) (sh * 0.06f * progress);
            context.fill(0, 0, sw, barH, barColor);
            context.fill(0, sh - barH, sw, sh, barColor);
            context.fill(0, barH, (int) (sw * progress), barH + 2, lineColor);
            context.fill(0, sh - barH - 2, (int) (sw * progress), sh - barH, lineColor);
        } else {
            int barW = (int) (sw * 0.04f * progress);
            context.fill(0, 0, barW, sh, barColor);
            context.fill(sw - barW, 0, sw, sh, barColor);
            context.fill(barW, 0, barW + 2, (int) (sh * progress), lineColor);
            context.fill(sw - barW - 2, 0, sw - barW, (int) (sh * progress), lineColor);
        }
    }
}
