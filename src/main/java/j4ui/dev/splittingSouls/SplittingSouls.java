package j4ui.dev.splittingSouls;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;

import net.minecraft.loot.LootTables;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SplittingSouls implements ModInitializer {
    public static final String MOD_ID = "splittingsouls";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    @Override
    public void onInitialize() {
        ModItems.initialize();
        LOGGER.info("Items Initialized!");
        SplittingSoulsItemGroups.initialize();
        LOGGER.info("Item Group Initialized");


    }
}
