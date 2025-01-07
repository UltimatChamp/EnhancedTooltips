package dev.ultimatchamp.enhancedtooltips.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.gui.controllers.cycling.EnumController;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class EnhancedTooltipsGui {
    public static Screen createConfigScreen(Screen parent) {
        var config = EnhancedTooltipsConfig.load();

        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("enhancedtooltips.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("stat.generalButton"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("enhancedtooltips.config.rarityTooltip"))
                                .description(OptionDescription.createBuilder()
                                        .text(Text.translatable("enhancedtooltips.config.rarityTooltip.desc"))
                                        .build())
                                .binding(
                                        true,
                                        () -> config.rarityTooltip,
                                        (value) -> config.rarityTooltip = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("enhancedtooltips.config.effectTooltip"))
                                .description(OptionDescription.createBuilder()
                                        .text(Text.translatable("enhancedtooltips.config.effectTooltip.desc"))
                                        .build())
                                .binding(
                                        true,
                                        () -> config.effectTooltip,
                                        (value) -> config.effectTooltip = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("enhancedtooltips.config.armorTooltip"))
                                .description(OptionDescription.createBuilder()
                                        .text(Text.translatable("enhancedtooltips.config.armorTooltip.desc"))
                                        .build())
                                .binding(
                                        true,
                                        () -> config.armorTooltip,
                                        (value) -> config.armorTooltip = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("enhancedtooltips.config.bucketTooltip"))
                                .description(OptionDescription.createBuilder()
                                        .text(Text.translatable("enhancedtooltips.config.bucketTooltip.desc"))
                                        .build())
                                .binding(
                                        true,
                                        () -> config.bucketTooltip,
                                        (value) -> config.bucketTooltip = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("enhancedtooltips.config.spawnEggTooltip"))
                                .description(OptionDescription.createBuilder()
                                        .text(Text.translatable("enhancedtooltips.config.spawnEggTooltip.desc"))
                                        .build())
                                .binding(
                                        true,
                                        () -> config.spawnEggTooltip,
                                        (value) -> config.spawnEggTooltip = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<EnhancedTooltipsConfig.DurabilityTooltipMode>createBuilder()
                                .name(Text.translatable("enhancedtooltips.config.durabilityTooltip"))
                                .description(OptionDescription.createBuilder()
                                        .text(Text.translatable("enhancedtooltips.config.durabilityTooltip.desc"))
                                        .build())
                                .binding(
                                        EnhancedTooltipsConfig.DurabilityTooltipMode.VALUE,
                                        () -> config.durabilityTooltip,
                                        (value) -> config.durabilityTooltip = value
                                )
                                .customController(opt -> new EnumController<>(opt, EnhancedTooltipsConfig.DurabilityTooltipMode.class))
                                .build())
                        .build())
                .save(() -> EnhancedTooltipsConfig.save(config))
                .build()
                .generateScreen(parent);
    }
}
