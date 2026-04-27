package j4ui.dev.splittingSouls;

import j4ui.dev.splittingSouls.entity.SoulCloneEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<SoulCloneEntity> SOUL_CLONE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(SplittingSouls.MOD_ID, "soul_clone"),
            EntityType.Builder.<SoulCloneEntity>create(SoulCloneEntity::new, SpawnGroup.MISC)
                    .dimensions(0.6f, 1.8f)
                    .maxTrackingRange(10)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SplittingSouls.MOD_ID, "soul_clone")))
    );

    public static void initialize() {
        FabricDefaultAttributeRegistry.register(SOUL_CLONE, createAttributes());
    }

    private static DefaultAttributeContainer.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.MAX_HEALTH, 20.0);
    }
}
