package j4ui.dev.splittingSouls.item;

import j4ui.dev.splittingSouls.SplittingSouls;
import j4ui.dev.splittingSouls.data.ShardProgressManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SmallPurityShardItem extends Item {
    public SmallPurityShardItem(Settings settings) {
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
            boolean wasSplit = manager.canSplit(player.getUuid());
            manager.addProgress(player.getUuid(), 1.0f / 64);
            if (!wasSplit && manager.canSplit(player.getUuid())) {
                player.sendMessage(Text.literal("§bYour soul is ready to split!"), true);
            }
        }

        return ActionResult.SUCCESS;
    }
}
