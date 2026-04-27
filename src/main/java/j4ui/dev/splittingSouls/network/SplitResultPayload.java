package j4ui.dev.splittingSouls.network;

import j4ui.dev.splittingSouls.SplittingSouls;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SplitResultPayload() implements CustomPayload {
    public static final Id<SplitResultPayload> ID =
            new Id<>(Identifier.of(SplittingSouls.MOD_ID, "split_result"));
    public static final PacketCodec<ByteBuf, SplitResultPayload> CODEC =
            PacketCodec.unit(new SplitResultPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
