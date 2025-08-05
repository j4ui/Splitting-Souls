package j4ui.dev.splittingSouls.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class PurityShardItem extends Item {
    public PurityShardItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            ItemStack stack = player.getStackInHand(hand);

            // TODO: Set split-enabled component/NBT
            System.out.println("[PurityShard] Player " + player.getName() + "used a shard!");

            if(player.isCreative()) {
                stack.decrement(1);
            }
        }
        return ActionResult.SUCCESS;
    }
}
