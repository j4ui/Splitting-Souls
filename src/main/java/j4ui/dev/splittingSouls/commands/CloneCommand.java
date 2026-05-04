package j4ui.dev.splittingSouls.commands;

import net.minecraft.particle.ParticleTypes;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import j4ui.dev.splittingSouls.SplittingSouls;
import j4ui.dev.splittingSouls.data.PlayerCloneData;
import j4ui.dev.splittingSouls.entity.SoulCloneEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Set;

import static net.minecraft.server.command.CommandManager.literal;

public class CloneCommand {
    private static final int COUNTDOWN_SECONDS = 1;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("SplittingSouls")
                        .then(literal("create").executes(CloneCommand::createClone))
                        .then(literal("switch").executes(CloneCommand::startSwitchCountdown))
        );
    }
    private static int startSwitchCountdown(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (player == null) return 0;

//---------------------------------------------------------------------------
        PlayerCloneData cloneData = SplittingSouls.getManager().getOrCreate(player.getUuid());
        if (!cloneData.exists()) {
            player.sendMessage(Text.literal("§cNo active soul clone!"), true);
            return 0;
        }
        World cloneWorld = cloneData.getWorld();
        if (cloneWorld == null) {
            player.sendMessage(Text.literal("§cYour soul clone's location is invalid!"), true);
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

    public static void performTeleport(ServerPlayerEntity player, PlayerCloneData cloneData) {
        Vec3d playerPos = player.getPos();
        float playerYaw = player.getYaw();
        float playerPitch = player.getPitch();
        ServerWorld playerWorld = player.getWorld();

        ServerWorld cloneWorld = (ServerWorld) cloneData.getWorld();
        Vec3d clonePos = cloneData.getPosition();
        float cloneYaw = cloneData.getYaw();
        float clonePitch = cloneData.getPitch();

        // Use entity's actual position (may have shifted from knockback)
        java.util.UUID entityUUID = cloneData.getEntityUUID();
        SoulCloneEntity cloneEntity = null;
        if (entityUUID != null && cloneWorld != null) {
            net.minecraft.entity.Entity found = cloneWorld.getEntity(entityUUID);
            if (found instanceof SoulCloneEntity sce) {
                cloneEntity = sce;
                clonePos = sce.getPos();
                cloneYaw = sce.getYaw();
                clonePitch = sce.getPitch();
            }
        }

        player.teleport(cloneWorld, clonePos.x, clonePos.y, clonePos.z, Set.of(), cloneYaw, clonePitch, false);
        player.setYaw(cloneYaw);
        player.setPitch(clonePitch);

        // Move entity to player's old position
        if (cloneEntity != null) {
            cloneEntity.refreshPositionAndAngles(playerPos.x, playerPos.y, playerPos.z, playerYaw, playerPitch);
        }

        cloneData.setPosition(playerPos);
        cloneData.setYaw(playerYaw);
        cloneData.setPitch(playerPitch);
        cloneData.setWorld(playerWorld);

        player.sendMessage(Text.literal("§aSwitched places with your soul clone!"), true);
        player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);

        SplittingSouls.getManager().saveData();
    }

    private static int createClone(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) return 0;

        ServerWorld world = context.getSource().getWorld();

        PlayerCloneData cloneData = new PlayerCloneData(player.getUuid().toString());
        cloneData.setPosition(player.getPos());
        cloneData.setYaw(player.getYaw());
        cloneData.setPitch(player.getPitch());
        cloneData.setWorld(world);
        cloneData.setExists(true);



        SplittingSouls.getManager().updateCloneData(player.getUuid(), cloneData);
        player.sendMessage(Text.of("Created a soul clone at your current position!"), true);
        return Command.SINGLE_SUCCESS;
    }
}