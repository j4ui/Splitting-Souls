package j4ui.dev.splittingSouls;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

public class ModLootTableModifiers {

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registry) -> {
            Identifier id = key.getValue();

            if (id.equals(Identifier.of("minecraft", "chests/ancient_city"))) {
                addShard(tableBuilder, ModItems.SMALL_PURITY_SHARD, 0.5f, 1f, 2f);
                addShard(tableBuilder, ModItems.PURITY_SHARD, 0.35f, 1f, 1f);
                addShard(tableBuilder, ModItems.SMALL_PURITY_CRYSTAL, 0.125f, 1f, 2f);
                addShard(tableBuilder, ModItems.PURITY_CRYSTAL, 0.01f, 1f, 1f);
            }

            if (id.equals(Identifier.of("minecraft", "chests/shipwreck_treasure"))) {
                addShard(tableBuilder, ModItems.PURITY_SHARD, 0.5f, 2f, 3f);
            }

            if (id.equals(Identifier.of("minecraft", "chests/jungle_temple"))) {
                addShard(tableBuilder, ModItems.SMALL_PURITY_SHARD, 0.5f, 1f, 2f);
                addShard(tableBuilder, ModItems.PURITY_SHARD, 0.35f, 1f, 1f);
                addShard(tableBuilder, ModItems.SMALL_PURITY_CRYSTAL, 0.125f, 1f, 1f);
            }

            if (id.equals(Identifier.of("minecraft", "chests/pillager_outpost"))) {
                addShard(tableBuilder, ModItems.SMALL_PURITY_SHARD, 0.5f, 1f, 2f);
                addShard(tableBuilder, ModItems.PURITY_SHARD, 0.35f, 1f, 1f);
                addShard(tableBuilder, ModItems.SMALL_PURITY_CRYSTAL, 0.125f, 1f, 1f);
            }

            if (id.equals(Identifier.of("minecraft", "chests/desert_pyramid"))) {
                addShard(tableBuilder, ModItems.SMALL_PURITY_SHARD, 0.5f, 2f, 3f);
                addShard(tableBuilder, ModItems.PURITY_SHARD, 0.35f, 1f, 1f);
            }

            if (id.equals(Identifier.of("minecraft", "chests/woodland_mansion"))) {
                addShard(tableBuilder, ModItems.SMALL_PURITY_SHARD, 0.5f, 1f, 1f);
                addShard(tableBuilder, ModItems.PURITY_SHARD, 0.35f, 1f, 1f);
                addShard(tableBuilder, ModItems.SMALL_PURITY_CRYSTAL, 0.125f, 1f, 2f);
                addShard(tableBuilder, ModItems.PURITY_CRYSTAL, 0.01f, 1f, 1f);
            }

            if (id.equals(Identifier.of("minecraft", "chests/stronghold_library"))) {
                addShard(tableBuilder, ModItems.PURITY_SHARD, 0.35f, 1f, 1f);
                addShard(tableBuilder, ModItems.SMALL_PURITY_CRYSTAL, 0.125f, 2f, 3f);
                addShard(tableBuilder, ModItems.PURITY_CRYSTAL, 0.01f, 1f, 1f);
            }

            if (id.equals(Identifier.of("minecraft", "chests/bastion_treasure"))) {
                addShard(tableBuilder, ModItems.PURITY_SHARD, 0.35f, 1f, 1f);
                addShard(tableBuilder, ModItems.SMALL_PURITY_CRYSTAL, 0.125f, 2f, 2f);
                addShard(tableBuilder, ModItems.PURITY_CRYSTAL, 0.01f, 1f, 1f);
            }

            if (id.equals(Identifier.of("minecraft", "chests/end_city_treasure"))) {
                addShard(tableBuilder, ModItems.SMALL_PURITY_CRYSTAL, 0.125f, 1f, 2f);
                addShard(tableBuilder, ModItems.PURITY_CRYSTAL, 0.01f, 1f, 1f);
            }
        });
    }

    private static void addShard(net.minecraft.loot.LootTable.Builder tableBuilder, Item item, float chance, float minCount, float maxCount) {
        LootPool.Builder poolBuilder = LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(RandomChanceLootCondition.builder(chance))
                .with(ItemEntry.builder(item))
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(minCount, maxCount)).build());
        tableBuilder.pool(poolBuilder.build());
    }
}
