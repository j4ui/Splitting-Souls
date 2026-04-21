package j4ui.dev.splittingSouls;
import j4ui.dev.splittingSouls.commands.CloneCommand;
import j4ui.dev.splittingSouls.data.CloneDataManager;
import j4ui.dev.splittingSouls.data.ShardProgressManager;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
