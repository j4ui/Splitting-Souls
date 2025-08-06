package j4ui.dev.splittingSouls;
import j4ui.dev.splittingSouls.component.ShardComponentInitializer;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SplittingSouls implements ModInitializer {
    public static final String MOD_ID = "splitting-souls";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    @Override
    public void onInitialize() {
        ModItems.initialize();
        LOGGER.info("Items Initialized");
        SplittingSoulsItemGroups.initialize();
        LOGGER.info("Item Group Initialized");
        ModLootTableModifiers.modifyLootTables();
        LOGGER.info("Loot tables modified");


    }
}
