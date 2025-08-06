package j4ui.dev.splittingSouls.component;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class ShardProgressComponentImpl implements ShardProgressComponent, AutoSyncedComponent {
    private float progress = 0f;
    private final PlayerEntity player;

    public ShardProgressComponentImpl(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public float getProgress() {
        return progress;
    }

    @Override
    public void addProgress(float amount) {
        this.progress += amount;
        ShardComponentInitializer.SHARD_PROGRESS.sync(player);
    }

    @Override
    public void setProgress(float amount) {
        this.progress = amount;
        ShardComponentInitializer.SHARD_PROGRESS.sync(player);
    }

    @Override
    public boolean canSplit() {
        return progress >= 1.0f;
    }

    // ✅ Optional — no @Override required in CCA 7
    public void readFromNbt(NbtCompound tag) {
        this.progress = tag.getFloat("progress").orElse(0f);
    }

    public void writeToNbt(NbtCompound tag) {
        tag.putFloat("progress", this.progress);
    }

    @Override
    public void readData(ReadView readView) {

    }

    @Override
    public void writeData(WriteView writeView) {

    }
}
