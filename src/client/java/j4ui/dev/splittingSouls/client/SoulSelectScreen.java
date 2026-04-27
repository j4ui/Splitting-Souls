package j4ui.dev.splittingSouls.client;

import j4ui.dev.splittingSouls.network.SoulChoicePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class SoulSelectScreen extends Screen {

    private boolean choiceSent = false;

    public SoulSelectScreen() {
        super(Text.literal("Your Soul Has Split"));
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int cy = this.height / 2;

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Remain Here"),
                btn -> sendChoice(false)
        ).dimensions(cx - 105, cy + 10, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Control Other Soul"),
                btn -> sendChoice(true)
        ).dimensions(cx + 5, cy + 10, 100, 20).build());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        int cx = this.width / 2;
        int cy = this.height / 2;
        context.fill(cx - 115, cy - 35, cx + 115, cy + 40, 0xBB000022);
        context.fill(cx - 115, cy - 35, cx + 115, cy - 34, 0xFF6A3FD2);
        context.fill(cx - 115, cy + 39, cx + 115, cy + 40, 0xFF6A3FD2);
        context.fill(cx - 115, cy - 35, cx - 114, cy + 40, 0xFF6A3FD2);
        context.fill(cx + 114, cy - 35, cx + 115, cy + 40, 0xFF6A3FD2);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(
                this.textRenderer, Text.literal("Your Soul Has Split"),
                this.width / 2, this.height / 2 - 20, 0xBB88FF);
        context.drawCenteredTextWithShadow(
                this.textRenderer, Text.literal("Which soul do you inhabit?"),
                this.width / 2, this.height / 2 - 8, 0xCCCCCC);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        sendChoice(false);
    }

    private void sendChoice(boolean controlOther) {
        if (choiceSent) return;
        choiceSent = true;
        ClientPlayNetworking.send(new SoulChoicePayload(controlOther));
        this.client.setScreen(null);
    }
}
