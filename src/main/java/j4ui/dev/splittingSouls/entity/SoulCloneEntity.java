package j4ui.dev.splittingSouls.entity;

import j4ui.dev.splittingSouls.SplittingSouls;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.world.World;

import java.util.UUID;

public class SoulCloneEntity extends LivingEntity {

    private static final TrackedData<String> OWNER_UUID =
            DataTracker.registerData(SoulCloneEntity.class, TrackedDataHandlerRegistry.STRING);

    public SoulCloneEntity(EntityType<? extends SoulCloneEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(OWNER_UUID, "");
    }

    public UUID getOwnerUUID() {
        String raw = this.dataTracker.get(OWNER_UUID);
        return raw.isEmpty() ? null : UUID.fromString(raw);
    }

    public void setOwnerUUID(UUID uuid) {
        this.dataTracker.set(OWNER_UUID, uuid.toString());
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (!this.getWorld().isClient) {
            UUID ownerUUID = getOwnerUUID();
            if (ownerUUID != null && SplittingSouls.cloneDataManager != null) {
                SplittingSouls.cloneDataManager.clearClone(ownerUUID);
                ServerPlayerEntity owner = ((ServerWorld) this.getWorld())
                        .getServer().getPlayerManager().getPlayer(ownerUUID);
                if (owner != null) {
                    owner.sendMessage(Text.literal("§cYour soul clone has perished!"), true);
                }
            }
        }
    }

    @Override
    protected void dropInventory(ServerWorld world) {}

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }

    @Override
    public boolean isCustomNameVisible() {
        return false;
    }

    @Override
    public boolean canHit() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }
}
