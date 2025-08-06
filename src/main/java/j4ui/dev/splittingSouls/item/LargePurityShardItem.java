package j4ui.dev.splittingSouls.item;

import j4ui.dev.splittingSouls.component.ShardComponentInitializer;
import j4ui.dev.splittingSouls.component.ShardProgressComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class LargePurityShardItem extends Item {
    public LargePurityShardItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            ItemStack stack = player.getStackInHand(hand);

            if (!player.isCreative()) {
                stack.decrement(1);
            }

            // 🔧 Add progress via component
            ShardProgressComponent component = ShardComponentInitializer.SHARD_PROGRESS.get(player);
            component.addProgress(1.0f ); // large shard
            System.out.println("Shard progress: " + component.getProgress());

            // You can optionally check for unlock here:
            if (component.canSplit()) {
                System.out.println("Soul splitting unlocked!");
            }
        }

        return ActionResult.SUCCESS;
    }
}
