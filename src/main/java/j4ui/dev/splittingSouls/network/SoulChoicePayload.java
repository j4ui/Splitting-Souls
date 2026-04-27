package j4ui.dev.splittingSouls.network;

import j4ui.dev.splittingSouls.SplittingSouls;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SoulChoicePayload(boolean controlOther) implements CustomPayload {
    public static final Id<SoulChoicePayload> ID =
            new Id<>(Identifier.of(SplittingSouls.MOD_ID, "soul_choice"));
    public static final PacketCodec<ByteBuf, SoulChoicePayload> CODEC =
            PacketCodec.of(
                    (value, buf) -> buf.writeBoolean(value.controlOther()),
                    buf -> new SoulChoicePayload(buf.readBoolean())
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
