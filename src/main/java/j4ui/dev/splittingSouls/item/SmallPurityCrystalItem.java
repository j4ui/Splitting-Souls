package j4ui.dev.splittingSouls.item;

import j4ui.dev.splittingSouls.SplittingSouls;
import j4ui.dev.splittingSouls.data.ShardProgressManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SmallPurityCrystalItem extends Item {
    public SmallPurityCrystalItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            ItemStack stack = player.getStackInHand(hand);

            if (!player.isCreative()) {
                stack.decrement(1);
            }

            ShardProgressManager manager = SplittingSouls.shardProgressManager;
            manager.addProgress(player.getUuid(), 1.0f / 4);
            System.out.println("Shard progress: " + manager.getProgress(player.getUuid()));

            if (manager.canSplit(player.getUuid())) {
                System.out.println("Soul splitting unlocked!");
            }
        }

        return ActionResult.SUCCESS;
    }
}
