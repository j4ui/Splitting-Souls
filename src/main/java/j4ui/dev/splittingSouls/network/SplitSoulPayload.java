package j4ui.dev.splittingSouls.network;

import j4ui.dev.splittingSouls.SplittingSouls;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SplitSoulPayload(int direction) implements CustomPayload {
    // 0 = W+S (forward/backward axis), 1 = A+D (left/right axis)
    public static final Id<SplitSoulPayload> ID =
            new Id<>(Identifier.of(SplittingSouls.MOD_ID, "split_soul"));
    public static final PacketCodec<ByteBuf, SplitSoulPayload> CODEC =
            PacketCodecs.VAR_INT.xmap(SplitSoulPayload::new, SplitSoulPayload::direction);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
