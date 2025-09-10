package j4ui.dev.splittingSouls;

import j4ui.dev.splittingSouls.item.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;


public class ModItems{
    private ModItems() {

    }

    // public static final Item PURITY_SHARD = register("purity_shard", Item::new, new Item.Settings());

    public static final Item SMALL_PURITY_SHARD = register("small_purity_shard", SmallPurityShardItem::new, new Item.Settings().maxCount(64));
    public static final Item PURITY_SHARD = register("purity_shard", PurityShardItem::new, new Item.Settings().maxCount(64));
    public static final Item SMALL_PURITY_CRYSTAL = register("small_purity_crystal", SmallPurityCrystalItem::new, new Item.Settings().maxCount(64));
    public static final Item PURITY_CRYSTAL = register("purity_crystal", PurityCrystalItem::new, new Item.Settings().maxCount(64));


    public static Item register(String path, Function<Item.Settings, Item> factory, Item.Settings settings) {
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(SplittingSouls.MOD_ID, path));
        return Items.register(registryKey, factory, settings);
    }

    public static void initialize() {

    }
}
