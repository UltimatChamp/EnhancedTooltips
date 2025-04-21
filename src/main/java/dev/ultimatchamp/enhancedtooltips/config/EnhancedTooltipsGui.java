package dev.ultimatchamp.enhancedtooltips.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.gui.controllers.cycling.EnumController;
import dev.isxander.yacl3.gui.controllers.slider.FloatSliderController;
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController;
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
                                                () -> config.general.rarityTooltip,
                                                (value) -> config.general.rarityTooltip = value
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
                                                () -> config.general.itemBadges,
                                                (value) -> config.general.itemBadges = value
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
                                                () -> config.general.scaleFactor,
                                                (value) -> config.general.scaleFactor = value
                                        )
                                        .customController(opt -> new FloatSliderController(opt, 0.25f, 2f, 0.05f, value -> Text.literal(String.format("%." + 0 /* decimal places */ + "f%%", value * 100.0F))))
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("enhancedtooltips.config.popUpAnimation"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("addServer.resourcePack.enabled"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.popUpAnimation.desc"))
                                                .build())
                                        .binding(
                                                true,
                                                () -> config.popUpAnimation.enabled,
                                                (value) -> config.popUpAnimation.enabled = value
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.translatable("stat.minecraft.play_time"))
                                        .binding(
                                                1.5f,
                                                () -> config.popUpAnimation.time,
                                                (value) -> config.popUpAnimation.time = value
                                        )
                                        .customController(opt -> new FloatSliderController(opt, 1f, 5f, 0.05f, value -> Text.literal(String.format("%." + 2 /* decimal places */ + "f seconds", value))))
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.popUpAnimation.magnitude"))
                                        .binding(
                                                1.0f,
                                                () -> config.popUpAnimation.magnitude,
                                                (value) -> config.popUpAnimation.magnitude = value
                                        )
                                        .customController(opt -> new FloatSliderController(opt, 0.25f, 2f, 0.05f, value -> Text.literal(String.format("%." + 0 /* decimal places */ + "f%%", value * 100.0F))))
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("enhancedtooltips.config.itemPreviewAnimation"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("addServer.resourcePack.enabled"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.itemPreviewAnimation.desc"))
                                                .build())
                                        .binding(
                                                true,
                                                () -> config.itemPreviewAnimation.enabled,
                                                (value) -> config.itemPreviewAnimation.enabled = value
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.translatable("stat.minecraft.play_time"))
                                        .binding(
                                                1f,
                                                () -> config.itemPreviewAnimation.time,
                                                (value) -> config.itemPreviewAnimation.time = value
                                        )
                                        .customController(opt -> new FloatSliderController(opt, 0.25f, 2f, 0.05f, value -> Text.literal(String.format("%." + 2 /* decimal places */ + "f seconds", value))))
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.popUpAnimation.magnitude"))
                                        .binding(
                                                2f,
                                                () -> config.itemPreviewAnimation.magnitude,
                                                (value) -> config.itemPreviewAnimation.magnitude = value
                                        )
                                        .customController(opt -> new FloatSliderController(opt, 0.5f, 4f, 0.05f, value -> Text.literal(String.format("%." + 0 /* decimal places */ + "f%%", value * 100.0F))))
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
                                                () -> config.border.borderColor,
                                                (value) -> config.border.borderColor = value
                                        )
                                        .customController(opt -> new EnumController<>(opt, EnhancedTooltipsConfig.BorderColorMode.class))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.rarity.common"))
                                        .binding(
                                                EnhancedTooltipsConfig.BorderColor.COMMON.getColor(),
                                                () -> config.border.customBorderColors.common,
                                                (value) -> config.border.customBorderColors.common = value
                                        )
                                        .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .available(config.border.borderColor == EnhancedTooltipsConfig.BorderColorMode.CUSTOM)
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.rarity.uncommon"))
                                        .binding(
                                                EnhancedTooltipsConfig.BorderColor.UNCOMMON.getColor(),
                                                () -> config.border.customBorderColors.uncommon,
                                                (value) -> config.border.customBorderColors.uncommon = value
                                        )
                                        .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .available(config.border.borderColor == EnhancedTooltipsConfig.BorderColorMode.CUSTOM)
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.rarity.rare"))
                                        .binding(
                                                EnhancedTooltipsConfig.BorderColor.RARE.getColor(),
                                                () -> config.border.customBorderColors.rare,
                                                (value) -> config.border.customBorderColors.rare = value
                                        )
                                        .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .available(config.border.borderColor == EnhancedTooltipsConfig.BorderColorMode.CUSTOM)
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.rarity.epic"))
                                        .binding(
                                                EnhancedTooltipsConfig.BorderColor.EPIC.getColor(),
                                                () -> config.border.customBorderColors.epic,
                                                (value) -> config.border.customBorderColors.epic = value
                                        )
                                        .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .available(config.border.borderColor == EnhancedTooltipsConfig.BorderColorMode.CUSTOM)
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.customBorderColors.endColor"))
                                        .binding(
                                                EnhancedTooltipsConfig.BorderColor.END_COLOR.getColor(),
                                                () -> config.border.customBorderColors.endColor,
                                                (value) -> config.border.customBorderColors.endColor = value
                                        )
                                        .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .available(config.border.borderColor == EnhancedTooltipsConfig.BorderColorMode.CUSTOM)
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("enhancedtooltips.config.group.background"))
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.backgroundColor"))
                                        .binding(
                                                new Color(0xF0100010, true),
                                                () -> config.background.backgroundColor,
                                                (value) -> config.background.backgroundColor = value
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
                                                () -> config.foodAndDrinks.hungerTooltip,
                                                (value) -> config.foodAndDrinks.hungerTooltip = value
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
                                                () -> config.foodAndDrinks.saturationTooltip,
                                                (value) -> config.foodAndDrinks.saturationTooltip = value
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
                                                () -> config.foodAndDrinks.effectsTooltip,
                                                (value) -> config.foodAndDrinks.effectsTooltip = value
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
                                                () -> config.mobs.rotationSpeed,
                                                (value) -> config.mobs.rotationSpeed = value
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
                                                () -> config.mobs.armorTooltip,
                                                (value) -> config.mobs.armorTooltip = value
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.horseArmorTooltip"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.horseArmorTooltip.desc"))
                                                .build())
                                        .binding(
                                                true,
                                                () -> config.mobs.horseArmorTooltip,
                                                (value) -> config.mobs.horseArmorTooltip = value
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.wolfArmorTooltip"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.wolfArmorTooltip.desc"))
                                                .build())
                                        .binding(
                                                true,
                                                () -> config.mobs.wolfArmorTooltip,
                                                (value) -> config.mobs.wolfArmorTooltip = value
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
                                                () -> config.mobs.bucketTooltip,
                                                (value) -> config.mobs.bucketTooltip = value
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
                                                () -> config.mobs.spawnEggTooltip,
                                                (value) -> config.mobs.spawnEggTooltip = value
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
                                                () -> config.mapTooltip.enabled,
                                                (value) -> config.mapTooltip.enabled = value
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
                                                () -> config.paintingTooltip.enabled,
                                                (value) -> config.paintingTooltip.enabled = value
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
                                                () -> config.durability.durabilityTooltip,
                                                (value) -> config.durability.durabilityTooltip = value
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
                                                () -> config.durability.durabilityBar,
                                                (value) -> config.durability.durabilityBar = value
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("enhancedtooltips.config.group.heldItemTooltip"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("addServer.resourcePack.enabled"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.heldItemTooltip.desc"))
                                                .build())
                                        .binding(
                                                true,
                                                () -> config.heldItemTooltip.enabled,
                                                (value) -> config.heldItemTooltip.enabled = value
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.showHeldItemTooltipBackground"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.showHeldItemTooltipBackground.desc"))
                                                .build())
                                        .binding(
                                                true,
                                                () -> config.heldItemTooltip.showBackground,
                                                (value) -> config.heldItemTooltip.showBackground = value
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.heldItemTooltipTiltAnimation"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.heldItemTooltipTiltAnimation.desc"))
                                                .build())
                                        .binding(
                                                true,
                                                () -> config.heldItemTooltip.tiltAnimation,
                                                (value) -> config.heldItemTooltip.tiltAnimation = value
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.translatable("enhancedtooltips.config.heldItemTooltipMaxLines"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("enhancedtooltips.config.heldItemTooltipMaxLines.desc"))
                                                .build())
                                        .binding(
                                                10,
                                                () -> config.heldItemTooltip.maxLines,
                                                (value) -> config.heldItemTooltip.maxLines = value
                                        )
                                        .customController(opt -> new IntegerSliderController(opt, 5, 25, 1))
                                        .build())
                                .build())
                        .build())
                .save(() -> EnhancedTooltipsConfig.save(config))
                .build()
                .generateScreen(parent);
    }
}
