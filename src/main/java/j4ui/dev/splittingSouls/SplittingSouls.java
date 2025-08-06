package j4ui.dev.splittingSouls;
import j4ui.dev.splittingSouls.commands.CloneCommand;
import j4ui.dev.splittingSouls.component.ShardComponentInitializer;
import j4ui.dev.splittingSouls.data.CloneDataManager;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SplittingSouls implements ModInitializer {
    public static final String MOD_ID = "splitting-souls";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static CloneDataManager cloneDataManager;


    @Override
    public void onInitialize() {

        cloneDataManager = new CloneDataManager();

        ModItems.initialize();
        LOGGER.info("Items Initialized");
        SplittingSoulsItemGroups.initialize();
        LOGGER.info("Item Group Initialized");
        ModLootTableModifiers.modifyLootTables();
        LOGGER.info("Loot tables modified");
        ShardComponentInitializer.getShardProgressKey();
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            cloneDataManager.saveData();
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CloneCommand.register(dispatcher);
            LOGGER.info("Soul splitting commands registered!");
        });


    }
}
