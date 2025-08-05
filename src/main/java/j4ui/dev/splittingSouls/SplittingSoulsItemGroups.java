package j4ui.dev.splittingSouls;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class SplittingSoulsItemGroups {
    public static final ItemGroup SPLITTING_SOULS = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.PURITY_SHARD))
            .displayName(Text.translatable("itemGroup.splittingsouls.splittingsouls_group"))
            .entries((context, entries) -> {
                entries.add(ModItems.PURITY_SHARD);
                entries.add(ModItems.TINY_PURITY_SHARD);
                entries.add(ModItems.SMALL_PURITY_SHARD);
                entries.add(ModItems.MEDIUM_PURITY_SHARD);
                entries.add(ModItems.LARGE_PURITY_SHARD);

            })
            .build();

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, Identifier.of("splittingsouls", "splittingsouls_group"), SPLITTING_SOULS);
    }
}
