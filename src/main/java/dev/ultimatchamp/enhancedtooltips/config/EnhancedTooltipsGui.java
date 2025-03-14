package dev.ultimatchamp.enhancedtooltips.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.gui.controllers.cycling.EnumController;
import dev.isxander.yacl3.gui.controllers.slider.FloatSliderController;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;

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
                                .option(Option.<Float>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.scaleFactor"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.scaleFactor.desc"))
                                                .build())
                                        .binding(
                                                1f,
                                                () -> config.scaleFactor,
                                                (value) -> config.scaleFactor = value
                                        )
                                        .customController(opt -> new FloatSliderController(opt, 0.25f, 2f, 0.05f, value -> Text.literal(String.format("%." + 0 /* decimal places */ + "f%%", value * 100.0F))))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.popUpAnimation"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.popUpAnimation.desc"))
                                                .build())
                                        .binding(
                                                true,
                                                () -> config.popUpAnimation,
                                                (value) -> config.popUpAnimation = value
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.itemPreviewAnimation"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.itemPreviewAnimation.desc"))
                                                .build())
                                        .binding(
                                                true,
                                                () -> config.itemPreviewAnimation,
                                                (value) -> config.itemPreviewAnimation = value
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
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
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("enhancedtooltips.config.group.border"))
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
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.rarity.common"))
                                        .binding(
                                                EnhancedTooltipsConfig.BorderColor.COMMON.getColor(),
                                                () -> config.customBorderColors.common,
                                                (value) -> config.customBorderColors.common = value
                                        )
                                        .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .available(config.borderColor == EnhancedTooltipsConfig.BorderColorMode.CUSTOM)
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.rarity.uncommon"))
                                        .binding(
                                                EnhancedTooltipsConfig.BorderColor.UNCOMMON.getColor(),
                                                () -> config.customBorderColors.uncommon,
                                                (value) -> config.customBorderColors.uncommon = value
                                        )
                                        .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .available(config.borderColor == EnhancedTooltipsConfig.BorderColorMode.CUSTOM)
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.rarity.rare"))
                                        .binding(
                                                EnhancedTooltipsConfig.BorderColor.RARE.getColor(),
                                                () -> config.customBorderColors.rare,
                                                (value) -> config.customBorderColors.rare = value
                                        )
                                        .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .available(config.borderColor == EnhancedTooltipsConfig.BorderColorMode.CUSTOM)
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.rarity.epic"))
                                        .binding(
                                                EnhancedTooltipsConfig.BorderColor.EPIC.getColor(),
                                                () -> config.customBorderColors.epic,
                                                (value) -> config.customBorderColors.epic = value
                                        )
                                        .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .available(config.borderColor == EnhancedTooltipsConfig.BorderColorMode.CUSTOM)
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.customBorderColors.endColor"))
                                        .binding(
                                                EnhancedTooltipsConfig.BorderColor.END_COLOR.getColor(),
                                                () -> config.customBorderColors.endColor,
                                                (value) -> config.customBorderColors.endColor = value
                                        )
                                        .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .available(config.borderColor == EnhancedTooltipsConfig.BorderColorMode.CUSTOM)
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("enhancedtooltips.config.group.background"))
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.backgroundColor"))
                                        .binding(
                                                new Color(0xF0100010, true),
                                                () -> config.backgroundColor,
                                                (value) -> config.backgroundColor = value
                                        )
                                        .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
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
                                .option(Option.<Float>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.rotationSpeed"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.rotationSpeed.desc"))
                                                .build())
                                        .binding(
                                                0.2f,
                                                () -> config.rotationSpeed,
                                                (value) -> config.rotationSpeed = value
                                        )
                                        .customController(opt -> new FloatSliderController(opt, 0, 1, 0.05f, value -> Text.literal(String.format("%." + 0 /* decimal places */ + "f%%", value * 100.0F))))
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
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("item.minecraft.filled_map"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.mapTooltip"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.mapTooltip.desc"))
                                                .build())
                                        .binding(
                                                true,
                                                () -> config.mapTooltip,
                                                (value) -> config.mapTooltip = value
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("item.minecraft.painting"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.paintingTooltip"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.paintingTooltip.desc"))
                                                .build())
                                        .binding(
                                                true,
                                                () -> config.paintingTooltip,
                                                (value) -> config.paintingTooltip = value
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
