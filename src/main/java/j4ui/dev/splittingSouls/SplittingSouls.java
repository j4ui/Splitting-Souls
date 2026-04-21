package j4ui.dev.splittingSouls;
import j4ui.dev.splittingSouls.commands.CloneCommand;
import j4ui.dev.splittingSouls.data.CloneDataManager;
import j4ui.dev.splittingSouls.data.ShardProgressManager;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SplittingSouls implements ModInitializer {
    public static final String MOD_ID = "splitting-souls";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Map<RegistryKey<World>, CloneDataManager> worldManagers = new HashMap<>();
    public static ShardProgressManager shardProgressManager;


    public static CloneDataManager getManager(ServerWorld world) {
        return worldManagers.computeIfAbsent(
                world.getRegistryKey(),
                k -> new CloneDataManager(world)
        );
    }

    @Override
    public void onInitialize() {

//        cloneDataManager = new CloneDataManager();

        ServerWorldEvents.LOAD.register((server, world) -> {
            worldManagers.put(world.getRegistryKey(), new CloneDataManager(world));
            if (world.getRegistryKey().equals(World.OVERWORLD)) {
                shardProgressManager = new ShardProgressManager(server);
            }
        });

        ServerWorldEvents.UNLOAD.register((server, world) -> {
            CloneDataManager manager = worldManagers.remove(world.getRegistryKey());
            if (manager != null) {
                manager.saveData();
            }
            if (world.getRegistryKey().equals(World.OVERWORLD) && shardProgressManager != null) {
                shardProgressManager.saveData();
                shardProgressManager = null;
            }
        });


        ModItems.initialize();
        LOGGER.info("Items Initialized");
        SplittingSoulsItemGroups.initialize();
        LOGGER.info("Item Group Initialized");
        ModLootTableModifiers.modifyLootTables();
        LOGGER.info("Loot tables modified");
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
//            cloneDataManager.saveData();
        });




        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CloneCommand.register(dispatcher);
            LOGGER.info("Soul splitting commands registered!");
        });


    }
}
