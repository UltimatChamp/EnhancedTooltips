package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.mixin.accessors.BucketItemEntityTypeAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.item.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.joml.*;

import java.lang.Math;
import java.util.List;

/*? if <1.21.9 {*//*import dev.ultimatchamp.enhancedtooltips.mixin.accessors.SpawnEggItemEntityTypeAccessor;*//*?}*/
//? if >1.21.4 {
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
//?} else {
/*import net.minecraft.nbt.NbtCompound;
*///?}
//? if >1.21.1 {
import net.minecraft.item.equipment.trim.ArmorTrim;
//?} else {
/*import net.minecraft.item.trim.ArmorTrim;
*///?}
//? if <1.21.6 {
/*import net.minecraft.client.render.DiffuseLighting;
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
        var equippable = itemStack.get(DataComponentTypes.EQUIPPABLE);
        return equippable != null ? equippable.slot() : EquipmentSlot.MAINHAND;
    }
    //?}

    @Override
    public void render(DrawContext context, int x, int y, int width, int height, int z, int page) throws Exception {
        super.render(context, x, y, width, height, z, page);

        if (page != 0) return;

        currentRotation = (currentRotation + ROTATION_INCREMENT) % 360;

        //? if >1.21.1 {
        if (getEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR ||
        //?} else {
        /*if (EntityType.ARMOR_STAND.create(MinecraftClient.getInstance().world).getPreferredEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR ||
        *///?}
            stack.isOf(Items.ELYTRA)) {
            if (!config.mobs.armorTooltip) return;
            renderArmorStand(context, x, y, z);
        } else if (stack.getItem() instanceof SmithingTemplateItem) {
            if (!config.mobs.armorTooltip) return;
            renderArmorTrim(context, x, y, z);
        } else if (stack.getItem().toString().contains("horse_armor")) {
            if (!config.mobs.horseArmorTooltip) return;
            renderHorseArmor(context, x, y, z);
        } else if (stack.isOf(Items.WOLF_ARMOR)) {
            if (!config.mobs.wolfArmorTooltip) return;
            renderWolfArmor(context, x, y, z);
        } else if (stack.getItem() instanceof EntityBucketItem bucketItem) {
            if (!config.mobs.bucketTooltip) return;
            renderBucketEntity(context, x, y, z, bucketItem);
        } else if (stack.getItem() instanceof SpawnEggItem spawnEggItem) {
            if (!config.mobs.spawnEggTooltip) return;
            renderSpawnEggEntity(context, x, y, z, spawnEggItem);
        }
    }

    private void renderArmorStand(DrawContext context, int x, int y, int z) throws Exception {
        var armorStand = new ArmorStandEntity(EntityType.ARMOR_STAND, MinecraftClient.getInstance().world);
        //? if >1.21.1 {
        armorStand.equipStack(getEquipmentSlot(stack), stack);
        //?} else {
        /*armorStand.equipStack(armorStand.getPreferredEquipmentSlot(stack), stack);
        *///?}

        super.render(context, x - 65, y, 40, 70, z, -1);
        drawEntity(context, x - 30 / 2 - SPACING - 10, y + 65, 30, currentRotation, armorStand);
    }

    private void renderArmorTrim(DrawContext context, int x, int y, int z) throws Exception {
        var armorStand = new ArmorStandEntity(EntityType.ARMOR_STAND, MinecraftClient.getInstance().world);
        List<ItemStack> armorPieces = List.of(
                Items.NETHERITE_HELMET.getDefaultStack(),
                Items.NETHERITE_CHESTPLATE.getDefaultStack(),
                Items.NETHERITE_LEGGINGS.getDefaultStack(),
                Items.NETHERITE_BOOTS.getDefaultStack()
        );

        for (ItemStack armor : armorPieces) {
            Identifier id = Identifier.of(StringUtils.substringBefore(stack.getItem().toString(), "_"));

            ClientWorld world = MinecraftClient.getInstance().world;
            if (world == null) return;

            DynamicRegistryManager registryManager = world.getRegistryManager();
            var mat = registryManager.getOptional(RegistryKeys.TRIM_MATERIAL);
            var pat = registryManager.getOptional(RegistryKeys.TRIM_PATTERN);
            if (mat.isEmpty() || pat.isEmpty()) return;

            var material = mat.get().getEntry(Identifier.ofVanilla("diamond")).orElseThrow();
            var pattern = pat.get().getEntry(id).orElseThrow();

            armor.set(DataComponentTypes.TRIM, new ArmorTrim(material, pattern));
            //? if >1.21.1 {
            armorStand.equipStack(getEquipmentSlot(armor), armor);
            //?} else {
            /*armorStand.equipStack(armorStand.getPreferredEquipmentSlot(armor), armor);
            *///?}
        }

        super.render(context, x - 65, y, 40, 70, z, -1);
        drawEntity(context, x - 30 / 2 - SPACING - 10, y + 65, 30, currentRotation, armorStand);
    }

    private void renderHorseArmor(DrawContext context, int x, int y, int z) throws Exception {
        var entityType = EntityType.HORSE;
        //? if >1.21.1 {
        var horse = entityType.create(MinecraftClient.getInstance().world, SpawnReason.SPAWN_ITEM_USE);
        //?} else {
        /*var horse = entityType.create(MinecraftClient.getInstance().world);
        *///?}

        if (horse == null) return;
        horse.equipStack(EquipmentSlot.BODY, stack);

        float entityWidth = horse.getWidth();
        float entityHeight = horse.getHeight();
        float entityScale = calculateScale(entityWidth, entityHeight);
        int scaledWidth = (int) (entityWidth * entityScale);
        int scaledHeight = (int) (entityHeight * entityScale);
        int entityOffset = scaledWidth + SPACING - 10;

        super.render(context, x - entityOffset - 70, y, scaledWidth + 60, scaledHeight + 20, z, -1);
        drawEntity(context, x - scaledWidth / 2 - SPACING - 30, y + scaledHeight + SPACING, entityScale, currentRotation, horse);
    }

    private void renderWolfArmor(DrawContext context, int x, int y, int z) throws Exception {
        var entityType = EntityType.WOLF;
        //? if >1.21.1 {
        var wolf = entityType.create(MinecraftClient.getInstance().world, SpawnReason.SPAWN_ITEM_USE);
        //?} else {
        /*var wolf = entityType.create(MinecraftClient.getInstance().world);
        *///?}

        if (wolf == null) return;
        wolf.equipStack(EquipmentSlot.BODY, stack);

        float entityWidth = wolf.getWidth();
        float entityHeight = wolf.getHeight();
        float entityScale = calculateScale(entityWidth, entityHeight);
        int scaledWidth = (int) (entityWidth * entityScale);
        int scaledHeight = (int) (entityHeight * entityScale);
        int entityOffset = scaledWidth + SPACING - 10;

        super.render(context, x - entityOffset - 70, y, scaledWidth + 50, scaledHeight + 10, z, -1);
        drawEntity(context, x - scaledWidth / 2 - SPACING - 35, y + scaledHeight + 10, entityScale, currentRotation, wolf);
    }

    private void renderBucketEntity(DrawContext context, int x, int y, int z, EntityBucketItem bucketItem) throws Exception {
        var entityType = ((BucketItemEntityTypeAccessor) bucketItem).get();
        //? if >1.21.1 {
        var entity = entityType.create(MinecraftClient.getInstance().world, SpawnReason.BUCKET);
        //?} else {
        /*var entity = entityType.create(MinecraftClient.getInstance().world);
        *///?}

        if (entity instanceof Bucketable bucketable) {
            var nbtComponent = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
            bucketable.copyDataFromNbt(nbtComponent.copyNbt());

            if (entity instanceof SchoolingFishEntity) return;
            if (bucketable instanceof PufferfishEntity pufferfishEntity) pufferfishEntity.setPuffState(2);

            float entityWidth = entity.getWidth();
            float entityHeight = entity.getHeight();
            float entityScale = calculateScale(entityWidth, entityHeight);
            int scaledWidth = (int) (entityWidth * entityScale);
            int scaledHeight = (int) (entityHeight * entityScale);
            int entityOffset = scaledWidth + SPACING - 10;

            super.render(context, x - entityOffset - 70, y, scaledWidth + 50, scaledHeight + 20, z, -1);
            drawEntity(context, x - scaledWidth / 2 - SPACING - 35, y + scaledHeight + SPACING, entityScale, currentRotation, (LivingEntity) bucketable);
        }
    }

    private void renderSpawnEggEntity(DrawContext context, int x, int y, int z, SpawnEggItem spawnEggItem) throws Exception {
        //? if >1.21.8 {
        var entityType = spawnEggItem.getEntityType(stack);
        if (entityType == null) return;
        //?} else {
        /*var entityType = ((SpawnEggItemEntityTypeAccessor) spawnEggItem).get();
        *///?}
        //? if >1.21.1 {
        var entity = entityType.create(MinecraftClient.getInstance().world, SpawnReason.BUCKET);
        //?} else {
        /*var entity = entityType.create(MinecraftClient.getInstance().world);
        *///?}

        if (entityType == EntityType.VILLAGER || entityType == EntityType.ZOMBIE_VILLAGER) {
            var world = MinecraftClient.getInstance().world;
            if (entity != null && world != null) {
                //? if >1.21.4 {
                ((VillagerDataContainer) entity).setVillagerData(VillagerEntity.createVillagerData()
                        .withLevel(1)
                        .withProfession(world.getRegistryManager(), VillagerProfession.FARMER)
                        .withType(world.getRegistryManager(), VillagerType.PLAINS));
                //?} else {
                /*var villagerData = new NbtCompound();
                villagerData.putString("profession", "minecraft:none");
                villagerData.putString("type", "minecraft:plains");

                var nbtComponent = stack.getOrDefault(DataComponentTypes.ENTITY_DATA, NbtComponent.DEFAULT);
                var nbt = nbtComponent.copyNbt();

                nbt.put("VillagerData", villagerData);
                entity.readNbt(nbt);
                *///?}
            }
        }

        if (entity instanceof SchoolingFishEntity) return;
        if (entity instanceof PufferfishEntity pufferfishEntity) pufferfishEntity.setPuffState(2);
        if (entity instanceof SnowGolemEntity snowGolemEntity) snowGolemEntity.setHasPumpkin(false);

        if (entity instanceof LivingEntity livingEntity) {
            float entityWidth = entity.getWidth();
            float entityHeight = entity.getHeight();
            float entityScale = calculateScale(entityWidth, entityHeight);
            int scaledWidth = (int) (entityWidth * entityScale);
            int scaledHeight = (int) (entityHeight * entityScale);
            int entityOffset = scaledWidth + SPACING - 10;

            super.render(context, x - entityOffset - 70, y, scaledWidth + 50, scaledHeight + 20, z, -1);
            drawEntity(context, x - scaledWidth / 2 - SPACING - 35, y + scaledHeight + SPACING, entityScale, currentRotation, livingEntity);
        }
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

    public static void drawEntity(DrawContext context, int x, int y, float scale, float rotationYaw, LivingEntity entity) {
        entity.bodyYaw = rotationYaw;
        entity.setYaw(rotationYaw);
        entity.headYaw = rotationYaw;

        var modelRotation = new Quaternionf()
                .rotateY((float) Math.toRadians(rotationYaw))
                .rotateX((float) Math.toRadians(180));

        //? if >1.21.5 {
        float entityWidth = entity.getWidth();
        float entityHeight = entity.getHeight();

        int x1 = (int) (x - entityWidth * scale - SPACING + 5);
        int y1 = (int) (y - entityHeight * scale - SPACING);
        int x2 = (int) (x + entityWidth * scale + SPACING - 5);
        int y2 = (int) (y + entityHeight * scale + SPACING);

        var renderState = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(entity)
                .getAndUpdateRenderState(entity, 1);

        //? if >1.21.8 {
        renderState.light = 15728880;
        renderState.shadowPieces.clear();
        //?}

        context.addEntity(renderState, scale, new Vector3f(), modelRotation, null, x1, y1, x2, y2);
        //?} else {
        /*drawEntity(context, x, y, scale, modelRotation, entity);
        *///?}
    }

    //? if <1.21.6 {
    /*public static void drawEntity(DrawContext context, int x, int y, float scale, Quaternionf rotation, Entity entity) {
        DiffuseLighting.disableGuiDepthLighting();
        context.getMatrices().push();

        context.getMatrices().translate(x, y, 450);
        context.getMatrices().multiplyPositionMatrix(new Matrix4f().scaling(scale, scale, scale));
        context.getMatrices().multiply(rotation);

        var dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false);
        dispatcher.render(entity, 0.0, 0.0, 0.0,/^? if 1.21.1 {^/ /^0,^//^?}^/ 0, context.getMatrices(), MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers(), 15728880);
        dispatcher.setRenderShadows(true);

        context.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
    }
    *///?}
}
