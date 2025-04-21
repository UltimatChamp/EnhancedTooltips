package dev.ultimatchamp.enhancedtooltips.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class NoConfigScreenWarning extends Screen {
    private final Screen parent;

    public NoConfigScreenWarning(Screen parent) {
        super(Text.empty());
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        var fileBtn = ButtonWidget.builder(Text.translatable("enhancedtooltips.config.screen.open"), button -> Util.getOperatingSystem().open(EnhancedTooltipsConfig.CONFIG_PATH)).dimensions(this.width / 2 - 100, this.height / 2 + 50, 200, 20).build();
        this.addDrawableChild(fileBtn);

        var backBtn = ButtonWidget.builder(Text.translatable("dataPack.validation.back"), button -> this.client.setScreen(parent)).dimensions(this.width / 2 - 100, this.height / 2 + 75, 200, 20).build();
        this.addDrawableChild(backBtn);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        Text warning = Text.translatable("enhancedtooltips.config.screen.warn");
        //? if >1.21.3 {
        context.drawWrappedText(this.textRenderer, warning, this.width / 2 - 100, this.height / 2 - 50, 200, 0xFFFFFFFF, true);
        //?} else {
        /*context.drawTextWrapped(this.textRenderer, warning, this.width / 2 - 100, this.height / 2 - 50, 200, 0xFFFFFFFF);
        *///?}
    }
}
