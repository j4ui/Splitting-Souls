package j4ui.dev.splittingSouls.client.entity;

import j4ui.dev.splittingSouls.entity.SoulCloneEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

import java.util.UUID;

public class SoulCloneEntityRenderer extends LivingEntityRenderer<SoulCloneEntity, SoulCloneEntityRenderState, PlayerEntityModel> {

    public SoulCloneEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new PlayerEntityModel(ctx.getPart(EntityModelLayers.PLAYER), false), 0.5f);
    }

    @Override
    public SoulCloneEntityRenderState createRenderState() {
        return new SoulCloneEntityRenderState();
    }

    @Override
    public void updateRenderState(SoulCloneEntity entity, SoulCloneEntityRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.displayName = null;

        UUID ownerUUID = entity.getOwnerUUID();
        if (ownerUUID != null) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.getNetworkHandler() != null) {
                PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(ownerUUID);
                if (entry != null) {
                    state.skinTextures = entry.getSkinTextures();
                    return;
                }
            }
            state.skinTextures = net.minecraft.client.util.DefaultSkinHelper.getSkinTextures(ownerUUID);
        } else {
            state.skinTextures = net.minecraft.client.util.DefaultSkinHelper.getSteve();
        }
    }

    @Override
    public Identifier getTexture(SoulCloneEntityRenderState state) {
        return state.skinTextures.texture();
    }

    @Override
    protected RenderLayer getRenderLayer(SoulCloneEntityRenderState state, boolean showBody, boolean translucent, boolean showOutline) {
        Identifier texture = getTexture(state);
        return RenderLayer.getEntityTranslucent(texture);
    }

    @Override
    protected int getMixColor(SoulCloneEntityRenderState state) {
        // ~55% alpha (0x8C), white — blends multiplicatively with the render layer
        return ColorHelper.withAlpha(0x8C, 0xFFFFFF);
    }
}
