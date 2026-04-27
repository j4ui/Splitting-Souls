package j4ui.dev.splittingSouls.client;

import j4ui.dev.splittingSouls.network.SplitSoulPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.sound.SoundEvents;

public class SoulStretchHandler {
    public static final int REQUIRED_TICKS = 30;
    public static SoulStretchHandler INSTANCE;

    private int stretchTicks = 0;
    private int lastDirection = -1; // -1 = idle, 0 = W+S, 1 = A+D

    public int getStretchTicks() { return stretchTicks; }
    public int getLastDirection() { return lastDirection; }

    public void tick(MinecraftClient client) {
        if (client.player == null || client.currentScreen != null) {
            stretchTicks = 0;
            lastDirection = -1;
            return;
        }

        GameOptions opts = client.options;
        boolean w = opts.forwardKey.isPressed();
        boolean s = opts.backKey.isPressed();
        boolean a = opts.leftKey.isPressed();
        boolean d = opts.rightKey.isPressed();

        int dir = -1;
        if (w && s && !a && !d) dir = 0;
        else if (a && d && !w && !s) dir = 1;

        if (dir == -1 || dir != lastDirection) {
            stretchTicks = 0;
            lastDirection = dir;
            return;
        }

        lastDirection = dir;
        stretchTicks++;

        if (stretchTicks == 10) {
            client.player.playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 0.4f, 0.8f);
        } else if (stretchTicks == 20) {
            client.player.playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 0.6f, 1.1f);
        } else if (stretchTicks == REQUIRED_TICKS) {
            client.player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
            ClientPlayNetworking.send(new SplitSoulPayload(dir));
            stretchTicks = 0;
            lastDirection = -1;
        }
    }
}
