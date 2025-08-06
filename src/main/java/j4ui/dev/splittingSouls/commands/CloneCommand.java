package j4ui.dev.splittingSouls.commands;

import net.minecraft.particle.ParticleTypes;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import j4ui.dev.splittingSouls.SplittingSouls;
import j4ui.dev.splittingSouls.data.PlayerCloneData;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static net.minecraft.server.command.CommandManager.literal;

public class CloneCommand {
    private static final int COUNTDOWN_SECONDS = 3;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("soulsplit")
                        .then(literal("create").executes(CloneCommand::createClone))
                        .then(literal("switch").executes(CloneCommand::startSwitchCountdown))
        );
    }
    private static int startSwitchCountdown(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) return 0;

        PlayerCloneData cloneData = SplittingSouls.cloneDataManager.getOrCreate(player.getUuid());
        if (!cloneData.exists()) {
            player.sendMessage(Text.literal("§cNo active soul clone!"), false);
            return 0;
        }

        // Start countdown
        scheduleTeleport(player, cloneData, COUNTDOWN_SECONDS);
        return Command.SINGLE_SUCCESS;
    }

    private static void scheduleTeleport(ServerPlayerEntity player, PlayerCloneData cloneData, int secondsLeft) {
        if (secondsLeft > 0) {
            // Show countdown message
            player.sendMessage(Text.literal("§eTeleporting in §6" + secondsLeft + "§e..."), true);

            player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                    0.5f,  // Volume
                    0.5f + (COUNTDOWN_SECONDS - secondsLeft) * 0.2f  // Pitch increases each second
            );
            // Spawn particles around player
            ServerWorld world = player.getWorld();
            world.spawnParticles(
                    ParticleTypes.PORTAL,        // Particle type
                    player.getX(),              // X position
                    player.getY() + 1.0,        // Y position (above head)
                    player.getZ(),              // Z position
                    10,                         // Count
                    0.5,                        // X spread
                    0.5,                        // Y spread
                    0.5,                        // Z spread
                    0.1
            );

            // Schedule next countdown tick
            player.getServer().getOverworld().getServer().submitAndJoin(() -> {
                try {
                    Thread.sleep(1000); // 1000ms = 1 second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                scheduleTeleport(player, cloneData, secondsLeft - 1);
            });
        } else {
            performTeleport(player, cloneData);
        }

    }

    private static void performTeleport(ServerPlayerEntity player, PlayerCloneData cloneData) {
        // Store current player data
        Vec3d playerPos = player.getPos();
        float playerYaw = player.getYaw();
        float playerPitch = player.getPitch();
        ServerWorld playerWorld = (ServerWorld)player.getWorld();

        // Teleport player
        player.requestTeleport(
                cloneData.getPosition().x,
                cloneData.getPosition().y,
                cloneData.getPosition().z
        );
        player.setYaw(cloneData.getYaw());
        player.setPitch(cloneData.getPitch());

        // Update clone with player's old position
        cloneData.setPosition(playerPos);
        cloneData.setYaw(playerYaw);
        cloneData.setPitch(playerPitch);
        cloneData.setWorld(playerWorld);

        player.sendMessage(Text.literal("§aSwitched places with your soul clone!"), true);
        player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
    }

    private static int createClone(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) return 0;

        PlayerCloneData cloneData = SplittingSouls.cloneDataManager.getOrCreate(player.getUuid());
        cloneData.setPosition(player.getPos());
        cloneData.setYaw(player.getYaw());
        cloneData.setPitch(player.getPitch());
        cloneData.setWorld(player.getWorld());
        cloneData.setExists(true);

        player.sendMessage(Text.of("Created a soul clone at your current position!"), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int switchWithClone(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) return 0;

        PlayerCloneData cloneData = SplittingSouls.cloneDataManager.getOrCreate(player.getUuid());

        // Check if clone exists and is in overworld
        if (!cloneData.exists() || !cloneData.getWorld().getRegistryKey().equals(World.OVERWORLD)) {
            player.sendMessage(Text.literal("§cYour soul clone must be in the Overworld!"), false);
            return 0;
        }

        // Check player is in overworld too
        if (!player.getWorld().getRegistryKey().equals(World.OVERWORLD)) {
            player.sendMessage(Text.literal("§cYou must be in the Overworld to switch!"), false);
            return 0;
        }

        // Store current player data
        Vec3d playerPos = player.getPos();
        float playerYaw = player.getYaw();
        float playerPitch = player.getPitch();

        // Simple same-dimension teleport
        player.requestTeleport(
                cloneData.getPosition().x,
                cloneData.getPosition().y,
                cloneData.getPosition().z
        );

        // Set rotation
        player.setYaw(cloneData.getYaw());
        player.setPitch(cloneData.getPitch());

        // Update clone with player's old position
        cloneData.setPosition(playerPos);
        cloneData.setYaw(playerYaw);
        cloneData.setPitch(playerPitch);

        player.sendMessage(Text.literal("§aSwitched places with your soul clone!"), false);
        return Command.SINGLE_SUCCESS;
    }
}