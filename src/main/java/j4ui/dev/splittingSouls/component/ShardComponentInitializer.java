package j4ui.dev.splittingSouls.component;

import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class ShardComponentInitializer implements EntityComponentInitializer {
    public static ComponentKey<ShardProgressComponent> SHARD_PROGRESS = null;

    public static ComponentKey<ShardProgressComponent> getShardProgressKey() {
        if (SHARD_PROGRESS == null) {
            SHARD_PROGRESS = ComponentRegistry.getOrCreate(
                    Identifier.of("splitting-souls", "shard_progress"),
                    ShardProgressComponent.class
            );
        }
        return SHARD_PROGRESS;
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        ComponentKey<ShardProgressComponent> key = ComponentRegistry.getOrCreate(
                Identifier.of("splitting-souls", "shard_progress"),
                ShardProgressComponent.class
        );
        registry.registerForPlayers(key, ShardProgressComponentImpl::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}