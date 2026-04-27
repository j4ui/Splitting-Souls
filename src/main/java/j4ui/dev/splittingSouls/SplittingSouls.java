package j4ui.dev.splittingSouls;

import j4ui.dev.splittingSouls.commands.CloneCommand;
import j4ui.dev.splittingSouls.data.CloneDataManager;
import j4ui.dev.splittingSouls.data.PlayerCloneData;
import j4ui.dev.splittingSouls.data.ShardProgressManager;
import j4ui.dev.splittingSouls.entity.SoulCloneEntity;
import j4ui.dev.splittingSouls.network.SoulChoicePayload;
import j4ui.dev.splittingSouls.network.SplitResultPayload;
import j4ui.dev.splittingSouls.network.SplitSoulPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class SplittingSouls implements ModInitializer {
    public static final String MOD_ID = "splitting-souls";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static CloneDataManager cloneDataManager;
    public static ShardProgressManager shardProgressManager;

    public static CloneDataManager getManager() {
        return cloneDataManager;
    }

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(SplitSoulPayload.ID, SplitSoulPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SoulChoicePayload.ID, SoulChoicePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SplitResultPayload.ID, SplitResultPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SplitSoulPayload.ID, (payload, context) ->
                context.server().execute(() -> handleSplit(payload, context.player())));

        ServerPlayNetworking.registerGlobalReceiver(SoulChoicePayload.ID, (payload, context) ->
                context.server().execute(() -> handleSoulChoice(payload, context.player())));

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            cloneDataManager = new CloneDataManager(server);
            shardProgressManager = new ShardProgressManager(server);
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            if (cloneDataManager != null) {
                cloneDataManager.saveData();
                cloneDataManager = null;
            }
            if (shardProgressManager != null) {
                shardProgressManager.saveData();
                shardProgressManager = null;
            }
        });

        ModEntities.initialize();
        LOGGER.info("Entities Initialized");
        ModItems.initialize();
        LOGGER.info("Items Initialized");
        SplittingSoulsItemGroups.initialize();
        LOGGER.info("Item Group Initialized");
        ModLootTableModifiers.modifyLootTables();
        LOGGER.info("Loot tables modified");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CloneCommand.register(dispatcher);
            LOGGER.info("Soul splitting commands registered!");
        });
    }

    private static void handleSplit(SplitSoulPayload payload, ServerPlayerEntity player) {
        UUID uuid = player.getUuid();

        if (!shardProgressManager.canSplit(uuid)) {
            player.sendMessage(Text.literal("§cYour soul is not ready to split!"), true);
            return;
        }
        if (cloneDataManager.getOrCreate(uuid).exists()) {
            player.sendMessage(Text.literal("§cYou already have an active soul clone!"), true);
            return;
        }

        float yawRad = (float) Math.toRadians(player.getYaw());
        double offsetX, offsetZ;
        if (payload.direction() == 0) {
            offsetX = -Math.sin(yawRad) * 2.5;
            offsetZ =  Math.cos(yawRad) * 2.5;
        } else {
            offsetX =  Math.cos(yawRad) * 2.5;
            offsetZ =  Math.sin(yawRad) * 2.5;
        }

        Vec3d clonePos = player.getPos().add(offsetX, 0, offsetZ);
        ServerWorld world = player.getWorld();

        PlayerCloneData cloneData = new PlayerCloneData(uuid.toString());
        cloneData.setPosition(clonePos);
        cloneData.setYaw(player.getYaw());
        cloneData.setPitch(player.getPitch());
        cloneData.setWorld(world);
        cloneData.setExists(true);
        SoulCloneEntity cloneEntity = new SoulCloneEntity(ModEntities.SOUL_CLONE, world);
        cloneEntity.setOwnerUUID(uuid);
        cloneEntity.refreshPositionAndAngles(clonePos.x, clonePos.y, clonePos.z, player.getYaw(), player.getPitch());
        world.spawnEntity(cloneEntity);
        cloneData.setEntityUUID(cloneEntity.getUuid());

        cloneDataManager.updateCloneData(uuid, cloneData);

        shardProgressManager.setProgress(uuid, shardProgressManager.getProgress(uuid) - 1.0f);

        ServerPlayNetworking.send(player, new SplitResultPayload());
    }

    private static void handleSoulChoice(SoulChoicePayload payload, ServerPlayerEntity player) {
        if (!payload.controlOther()) {
            player.sendMessage(Text.literal("§bYou remain. Your other soul waits."), true);
            return;
        }
        PlayerCloneData cloneData = cloneDataManager.getOrCreate(player.getUuid());
        if (!cloneData.exists()) {
            player.sendMessage(Text.literal("§cNo active soul clone!"), true);
            return;
        }
        CloneCommand.performTeleport(player, cloneData);
    }
}
