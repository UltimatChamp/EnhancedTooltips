package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.compat.EMFCompat;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.mixin.accessors.MobBucketItemTypeAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.GameType;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.joml.*;

import java.lang.Math;
import java.util.List;

/*? if <1.21.9 {*//*import dev.ultimatchamp.enhancedtooltips.mixin.accessors.SpawnEggItemEntityTypeAccessor;*//*?}*/
//? if >1.21.10 {
import net.minecraft.world.entity.npc.villager.VillagerDataHolder;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.entity.npc.villager.Villager;
//?} else if >1.21.4 {
/*import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.npc.Villager;
*///?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}

//? if >1.21.1 {
import net.minecraft.world.item.equipment.trim.ArmorTrim;
//?} else {
/*import net.minecraft.world.item.armortrim.ArmorTrim;
*///?}

//? if <1.21.6 {
/*import com.mojang.blaze3d.platform.Lighting;
*///?}

//? if >1.21.10 {
import net.minecraft.world.entity.animal.fish.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.fish.Pufferfish;
import net.minecraft.world.entity.animal.golem.SnowGolem;
//?} else {
/*import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.animal.SnowGolem;
*///?}

public class ModelViewerTooltipComponent extends TooltipBorderColorComponent {
    private static float currentRotation = 0f;
    private static final int SPACING = 20;
    private static final int MAX_TOOLTIP_SIZE = 80;
    private static final float REFERENCE_SIZE = 3.5f;

    private final ItemStack stack;
    private final EnhancedTooltipsConfig config;
    private final float ROTATION_INCREMENT;

    public ModelViewerTooltipComponent(ItemStack stack) {
        super(stack);
        this.stack = stack;
        this.config = EnhancedTooltipsConfig.load();
        this.ROTATION_INCREMENT = config.mobs.rotationSpeed;
    }

    //? if >1.21.1 {
    public static EquipmentSlot getEquipmentSlot(ItemStack itemStack) {
        var equippable = itemStack.get(DataComponents.EQUIPPABLE);
        return equippable != null ? equippable.slot() : EquipmentSlot.MAINHAND;
    }
    //?}

    @Override
    public void render(GuiGraphicsExtractor context, int x, int y, int width, int height, int z, int page) throws Exception {
        super.render(context, x, y, width, height, z, page);

        if (page != 0) return;

        currentRotation = (currentRotation + ROTATION_INCREMENT) % 360;

        //? if >1.21.1 {
        if (getEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR ||
        //?} else {
        /*if (EntityType.ARMOR_STAND.create(Minecraft.getInstance().level).getEquipmentSlotForItem(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR ||
        *///?}
            stack.is(Items.ELYTRA)) {
            if (config.mobs.armorTooltip == EnhancedTooltipsConfig.ArmorTooltipMode.OFF) return;
            renderArmor(context, x, y, z);
        } else if (stack.getItem() instanceof SmithingTemplateItem) {
            if (config.mobs.armorTooltip == EnhancedTooltipsConfig.ArmorTooltipMode.OFF) return;
            renderArmorTrim(context, x, y, z);
        } else if (stack.getItem().toString().contains("horse_armor")) {
            if (!config.mobs.horseArmorTooltip) return;
            renderHorseArmor(context, x, y, z);
        //? if >1.21.10 {
        } else if (stack.getItem().toString().contains("nautilus_armor")) {
            if (!config.mobs.nautilusArmorTooltip) return;
            renderNautilusArmor(context, x, y, z);
        //?}
        } else if (stack.is(Items.WOLF_ARMOR)) {
            if (!config.mobs.wolfArmorTooltip) return;
            renderWolfArmor(context, x, y, z);
        } else if (stack.getItem() instanceof MobBucketItem bucketItem) {
            if (!config.mobs.bucketTooltip) return;
            renderBucketEntity(context, x, y, z, bucketItem);
        } else if (stack.getItem() instanceof SpawnEggItem spawnEggItem) {
            if (!config.mobs.spawnEggTooltip) return;
            renderSpawnEggEntity(context, x, y, z, spawnEggItem);
        }
    }

    private void renderArmor(GuiGraphicsExtractor context, int x, int y, int z) throws Exception {
        LivingEntity entity;
        if (config.mobs.armorTooltip == EnhancedTooltipsConfig.ArmorTooltipMode.PLAYER) {
            entity = createFakePlayer();
        } else if (config.mobs.armorTooltip == EnhancedTooltipsConfig.ArmorTooltipMode.ARMOR_STAND) {
            entity = createArmorStand();
        } else return;

        if (entity == null) return;

        //? if >1.21.1 {
        entity.setItemSlot(getEquipmentSlot(stack), stack.copy());
        //?} else {
        /*entity.setItemSlot(entity.getEquipmentSlotForItem(stack), stack.copy());
        *///?}

        super.render(context, x - 65, y, 40, 70, z, -1);
        drawEntity(context, x - 30 / 2 - SPACING - 10, y + 65, 30, currentRotation, entity);
    }

    private void renderArmorTrim(GuiGraphicsExtractor context, int x, int y, int z) throws Exception {
        LivingEntity entity;

        if (config.mobs.armorTooltip == EnhancedTooltipsConfig.ArmorTooltipMode.PLAYER) {
            entity = createFakePlayer();
        } else if (config.mobs.armorTooltip == EnhancedTooltipsConfig.ArmorTooltipMode.ARMOR_STAND) {
            entity = createArmorStand();
        } else return;

        if (entity == null) return;

        List<ItemStack> armorPieces = List.of(
                Items.NETHERITE_HELMET.getDefaultInstance(),
                Items.NETHERITE_CHESTPLATE.getDefaultInstance(),
                Items.NETHERITE_LEGGINGS.getDefaultInstance(),
                Items.NETHERITE_BOOTS.getDefaultInstance()
        );

        for (ItemStack armor : armorPieces) {
            Identifier id = Identifier.parse(StringUtils.substringBefore(stack.getItem().toString(), "_armor_trim_smithing_template"));

            ClientLevel world = Minecraft.getInstance().level;
            if (world == null) return;

            RegistryAccess registryManager = world.registryAccess();

            //? if >1.21.1 {
            var mat = registryManager.lookup(Registries.TRIM_MATERIAL);
            var pat = registryManager.lookup(Registries.TRIM_PATTERN);
            //?} else {
            /*var mat = registryManager.registry(Registries.TRIM_MATERIAL);
            var pat = registryManager.registry(Registries.TRIM_PATTERN);
            *///?}

            if (mat.isEmpty() || pat.isEmpty()) return;

            //? if >1.21.1 {
            var material = mat.get().get(Identifier.withDefaultNamespace("diamond")).orElseThrow();
            var pattern = pat.get().get(id).orElseThrow();
            //?} else {
            /*var material = mat.get().getHolder(Identifier.withDefaultNamespace("diamond")).orElseThrow();
            var pattern = pat.get().getHolder(id).orElseThrow();
            *///?}

            armor.set(DataComponents.TRIM, new ArmorTrim(material, pattern));

            //? if >1.21.1 {
            entity.setItemSlot(getEquipmentSlot(armor), armor.copy());
            //?} else {
            /*entity.setItemSlot(entity.getEquipmentSlotForItem(armor), armor.copy());
            *///?}
        }

        super.render(context, x - 65, y, 40, 70, z, -1);
        drawEntity(context, x - 30 / 2 - SPACING - 10, y + 65, 30, currentRotation, entity);
    }

    private AbstractClientPlayer createFakePlayer() {
        var player = Minecraft.getInstance().player;
        var world = Minecraft.getInstance().level;

        if (player == null || world == null) return null;

        var fakePlayer = new AbstractClientPlayer(world, Minecraft.getInstance().getGameProfile()) {
            //? if >1.21.8 {
            @Override
            public @NotNull GameType gameMode() {
                return GameType.ADVENTURE;
            }
            //?}

            @Override
            public boolean isModelPartShown(PlayerModelPart part) {
                return true;
            }
        };
        fakePlayer.setUUID(player.getUUID());

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack equipped = player.getItemBySlot(slot);
            fakePlayer.setItemSlot(slot, equipped.copy());
        }

        //? if >1.21.1 {
        Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(fakePlayer).createRenderState().nameTag = Component.empty();
        //?}

        return fakePlayer;
    }

    private ArmorStand createArmorStand() {
        return new ArmorStand(EntityType.ARMOR_STAND, Minecraft.getInstance().level);
    }

    private void renderHorseArmor(GuiGraphicsExtractor context, int x, int y, int z) throws Exception {
        var entityType = EntityType.HORSE;
        //? if >1.21.1 {
        var horse = entityType.create(Minecraft.getInstance().level, EntitySpawnReason.SPAWN_ITEM_USE);
        //?} else {
        /*var horse = entityType.create(Minecraft.getInstance().level);
        *///?}

        if (horse == null) return;
        horse.setItemSlot(EquipmentSlot.BODY, stack);

        float entityWidth = horse.getBbWidth();
        float entityHeight = horse.getBbHeight();
        float entityScale = calculateScale(entityWidth, entityHeight);
        int scaledWidth = (int) (entityWidth * entityScale);
        int scaledHeight = (int) (entityHeight * entityScale);
        int entityOffset = scaledWidth + SPACING - 10;

        super.render(context, x - entityOffset - 70, y, scaledWidth + 60, scaledHeight + 20, z, -1);
        drawEntity(context, x - scaledWidth / 2 - SPACING - 30, y + scaledHeight + SPACING, entityScale, currentRotation, horse);
    }

    //? if >1.21.10 {
    private void renderNautilusArmor(GuiGraphicsExtractor context, int x, int y, int z) throws Exception {
        var nautilus = EntityType.NAUTILUS.create(Minecraft.getInstance().level, EntitySpawnReason.SPAWN_ITEM_USE);

        if (nautilus == null) return;
        nautilus.setItemSlot(EquipmentSlot.BODY, stack);

        float entityWidth = nautilus.getBbWidth();
        float entityHeight = nautilus.getBbHeight();
        float entityScale = calculateScale(entityWidth, entityHeight);
        int scaledWidth = (int) (entityWidth * entityScale);
        int scaledHeight = (int) (entityHeight * entityScale);
        int entityOffset = scaledWidth + SPACING - 10;

        super.render(context, x - entityOffset - 70, y, scaledWidth + 50, scaledHeight + 20, z, -1);
        drawEntity(context, x - scaledWidth / 2 - SPACING - 35, y + scaledHeight + SPACING, entityScale, currentRotation, nautilus);
    }
    //?}

    private void renderWolfArmor(GuiGraphicsExtractor context, int x, int y, int z) throws Exception {
        var entityType = EntityType.WOLF;
        //? if >1.21.1 {
        var wolf = entityType.create(Minecraft.getInstance().level, EntitySpawnReason.SPAWN_ITEM_USE);
        //?} else {
        /*var wolf = entityType.create(Minecraft.getInstance().level);
        *///?}

        if (wolf == null) return;
        wolf.setItemSlot(EquipmentSlot.BODY, stack);

        float entityWidth = wolf.getBbWidth();
        float entityHeight = wolf.getBbHeight();
        float entityScale = calculateScale(entityWidth, entityHeight);
        int scaledWidth = (int) (entityWidth * entityScale);
        int scaledHeight = (int) (entityHeight * entityScale);
        int entityOffset = scaledWidth + SPACING - 10;

        super.render(context, x - entityOffset - 70, y, scaledWidth + 50, scaledHeight + 10, z, -1);
        drawEntity(context, x - scaledWidth / 2 - SPACING - 35, y + scaledHeight + 10, entityScale, currentRotation, wolf);
    }

    private void renderBucketEntity(GuiGraphicsExtractor context, int x, int y, int z, MobBucketItem bucketItem) throws Exception {
        var entityType = ((MobBucketItemTypeAccessor) bucketItem).get();
        //? if >1.21.1 {
        LivingEntity entity = entityType.create(Minecraft.getInstance().level, EntitySpawnReason.BUCKET);
        //?} else {
        /*var entity = entityType.create(Minecraft.getInstance().level);
        *///?}

        if (entity instanceof Bucketable bucketable) {
            var nbtComponent = stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);
            bucketable.loadFromBucketTag(nbtComponent.copyTag());

            if (entity instanceof AbstractSchoolingFish) return;
            if (bucketable instanceof Pufferfish pufferfishEntity) pufferfishEntity.setPuffState(2);

            float entityWidth = entity.getBbWidth();
            float entityHeight = entity.getBbHeight();
            float entityScale = calculateScale(entityWidth, entityHeight);
            int scaledWidth = (int) (entityWidth * entityScale);
            int scaledHeight = (int) (entityHeight * entityScale);
            int entityOffset = scaledWidth + SPACING - 10;

            super.render(context, x - entityOffset - 70, y, scaledWidth + 50, scaledHeight + 20, z, -1);
            drawEntity(context, x - scaledWidth / 2 - SPACING - 35, y + scaledHeight + SPACING, entityScale, currentRotation, (LivingEntity) bucketable);
        }
    }

    private void renderSpawnEggEntity(GuiGraphicsExtractor context, int x, int y, int z, SpawnEggItem spawnEggItem) throws Exception {
        //? if >1.21.8 {
        var entityType = spawnEggItem.getType(stack);
        if (entityType == null) return;
        //?} else {
        /*var entityType = ((SpawnEggItemEntityTypeAccessor) spawnEggItem).get();
        *///?}
        //? if >1.21.1 {
        var entity = entityType.create(Minecraft.getInstance().level, EntitySpawnReason.COMMAND);
        //?} else {
        /*var entity = entityType.create(Minecraft.getInstance().level);
        *///?}

        if (entityType == EntityType.VILLAGER || entityType == EntityType.ZOMBIE_VILLAGER) {
            var world = Minecraft.getInstance().level;
            if (entity != null && world != null) {
                //? if >1.21.4 {
                ((VillagerDataHolder) entity).setVillagerData(Villager.createDefaultVillagerData()
                        .withLevel(1)
                        .withProfession(world.registryAccess(), VillagerProfession.FARMER)
                        .withType(world.registryAccess(), VillagerType.PLAINS));
                //?} else {
                /*var villagerData = new CompoundTag();
                villagerData.putString("profession", "minecraft:none");
                villagerData.putString("type", "minecraft:plains");

                var nbtComponent = stack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY);
                var nbt = nbtComponent.copyTag();

                nbt.put("VillagerData", villagerData);
                entity.load(nbt);
                *///?}
            }
        }

        if (entity instanceof AbstractSchoolingFish) return;
        if (entity instanceof Pufferfish pufferfishEntity) pufferfishEntity.setPuffState(2);
        if (entity instanceof SnowGolem snowGolemEntity) snowGolemEntity.setPumpkin(false);

        float entityWidth = entity.getBbWidth();
        float entityHeight = entity.getBbHeight();
        float entityScale = calculateScale(entityWidth, entityHeight);
        int scaledWidth = (int) (entityWidth * entityScale);
        int scaledHeight = (int) (entityHeight * entityScale);
        int entityOffset = scaledWidth + SPACING - 10;

        super.render(context, x - entityOffset - 70, y, scaledWidth + 50, scaledHeight + 20, z, -1);
        drawEntity(context, x - scaledWidth / 2 - SPACING - 35, y + scaledHeight + SPACING, entityScale, currentRotation, entity);
    }

    private float calculateScale(float width, float height) {
        float longerDimension = Math.max(width, height);
        float scale = (MAX_TOOLTIP_SIZE / REFERENCE_SIZE) * longerDimension;

        if (scale > MAX_TOOLTIP_SIZE) {
            return MAX_TOOLTIP_SIZE / longerDimension;
        } else if (scale < 30.0f && longerDimension < 1.0f) {
            return 30.0f / longerDimension;
        }

        return scale / longerDimension;
    }

    public static void drawEntity(GuiGraphicsExtractor context, int x, int y, float scale, float rotationYaw, Entity entity) {
        entity.setYBodyRot(rotationYaw);
        entity.setYRot(rotationYaw);
        entity.setYHeadRot(rotationYaw);

        var modelRotation = new Quaternionf()
                .rotateY((float) Math.toRadians(rotationYaw))
                .rotateX((float) Math.toRadians(180));

        entity.setCustomName(Component.literal(EMFCompat.KEY));
        entity.setCustomNameVisible(false);

        //? if >1.21.5 {
        float entityWidth = entity.getBbWidth();
        float entityHeight = entity.getBbHeight();

        int x1 = (int) (x - entityWidth * scale - SPACING + 5);
        int y1 = (int) (y - entityHeight * scale - SPACING);
        int x2 = (int) (x + entityWidth * scale + SPACING - 5);
        int y2 = (int) (y + entityHeight * scale + SPACING);

        var renderState = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity)
                .createRenderState(entity, 1);

        //? if >1.21.8 {
        renderState.lightCoords = 15728880;
        renderState.shadowPieces.clear();
        //?}

        //? if >1.21.11 {
        context.entity(renderState, scale, new Vector3f(), modelRotation, null, x1, y1, x2, y2);
        //?} else {
        /*context.submitEntityRenderState(renderState, scale, new Vector3f(), modelRotation, null, x1, y1, x2, y2);
        *///?}

        //?} else {
        /*drawEntity(context, x, y, scale, modelRotation, entity);
        *///?}
    }

    //? if <1.21.6 {
    /*public static void drawEntity(GuiGraphicsExtractor context, int x, int y, float scale, Quaternionf rotation, Entity entity) {
        Lighting.setupForEntityInInventory();
        context.pose().pushPose();

        context.pose().translate(x, y, 450);
        context.pose().mulPose(new Matrix4f().scaling(scale, scale, scale));
        context.pose().mulPose(rotation);

        entity.setCustomName(EMFCompat.KEY);
        entity.setCustomNameVisible(false);

        var dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        dispatcher.setRenderShadow(false);
        dispatcher.render(entity, 0.0, 0.0, 0.0,/^? if 1.21.1 {^/ /^0,^//^?}^/ 0, context.pose(), Minecraft.getInstance().renderBuffers().bufferSource(), 15728880);
        dispatcher.setRenderShadow(true);

        context.pose().popPose();
        Lighting.setupForFlatItems();
    }
    *///?}
}
