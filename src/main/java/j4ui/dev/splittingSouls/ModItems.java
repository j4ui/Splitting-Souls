package j4ui.dev.splittingSouls;

import j4ui.dev.splittingSouls.item.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import net.minecraft.registry.Registry;

import java.util.function.Function;


public class ModItems{
    private ModItems() {

    }

    // public static final Item PURITY_SHARD = register("purity_shard", Item::new, new Item.Settings());
    public static final Item PURITY_SHARD = register(
            "purity_shard",
            PurityShardItem::new,
            new Item.Settings().maxCount(16)
    );
    public static final Item TINY_PURITY_SHARD = register("tiny_purity_shard", TinyPurityShardItem::new, new Item.Settings().maxCount(64));
    public static final Item SMALL_PURITY_SHARD = register("small_purity_shard", SmallPurityShardItem::new, new Item.Settings().maxCount(16));
    public static final Item MEDIUM_PURITY_SHARD = register("medium_purity_shard", MediumPurityShardItem::new, new Item.Settings().maxCount(16));
    public static final Item LARGE_PURITY_SHARD = register("large_purity_shard", LargePurityShardItem::new, new Item.Settings().maxCount(16));


    public static Item register(String path, Function<Item.Settings, Item> factory, Item.Settings settings) {
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(SplittingSouls.MOD_ID, path));
        return Items.register(registryKey, factory, settings);
    }

    public static void initialize() {

    }
}
