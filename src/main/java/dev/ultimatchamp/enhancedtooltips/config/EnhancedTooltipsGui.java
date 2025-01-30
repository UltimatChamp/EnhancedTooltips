package dev.ultimatchamp.enhancedtooltips.config;

import dev.isxander.yacl3.api.*;
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
                        .name(Text.translatable("enhancedtooltips.title"))
                        .group(OptionGroup.createBuilder()
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
                                //? if >1.20.6 {
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.itemBadges"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.itemBadges.desc"))
                                                .build())
                                        .binding(
                                                true,
                                                () -> config.itemBadges,
                                                (value) -> config.itemBadges = value
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                //?}
                                .option(Option.<EnhancedTooltipsConfig.BorderColorMode>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.borderColor"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.borderColor.desc"))
                                                .build())
                                        .binding(
                                                EnhancedTooltipsConfig.BorderColorMode.RARITY,
                                                () -> config.borderColor,
                                                (value) -> config.borderColor = value
                                        )
                                        .customController(opt -> new EnumController<>(opt, EnhancedTooltipsConfig.BorderColorMode.class))
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("itemGroup.foodAndDrink"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.hungerTooltip"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.hungerTooltip.desc"))
                                                .build())
                                        .binding(
                                                true,
                                                () -> config.hungerTooltip,
                                                (value) -> config.hungerTooltip = value
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.saturationTooltip"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.saturationTooltip.desc"))
                                                .build())
                                        .binding(
                                                true,
                                                () -> config.saturationTooltip,
                                                (value) -> config.saturationTooltip = value
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<EnhancedTooltipsConfig.EffectsTooltipMode>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.effectsTooltip"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.effectsTooltip.desc"))
                                                .build())
                                        .binding(
                                                EnhancedTooltipsConfig.EffectsTooltipMode.WITH_ICONS,
                                                () -> config.effectsTooltip,
                                                (value) -> config.effectsTooltip = value
                                        )
                                        .customController(opt -> new EnumController<>(opt, EnhancedTooltipsConfig.EffectsTooltipMode.class))
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("gamerule.category.mobs"))
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
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("enhancedtooltips.tooltip.durability"))
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
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.durabilityBar"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.durabilityBar.desc"))
                                                .build())
                                        .binding(
                                                false,
                                                () -> config.durabilityBar,
                                                (value) -> config.durabilityBar = value
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .save(() -> EnhancedTooltipsConfig.save(config))
                .build()
                .generateScreen(parent);
    }
}
