package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class ModelViewerTooltipComponent extends TooltipBorderColorComponent {
    private static float currentRotation = 0f;
    private static final int SPACING = 20;
    private static final int SHADOW_LIGHT_COLOR = 15728880;
    private static final int MAX_TOOLTIP_SIZE = 80;
    private static final float REFERENCE_SIZE = 3.5f;

    private final ItemStack stack;
    private final EnhancedTooltipsConfig config;
    private final float ROTATION_INCREMENT;

    public ModelViewerTooltipComponent(ItemStack stack) {
        super(stack);
        this.stack = stack;
        this.config = EnhancedTooltipsConfig.load();
        this.ROTATION_INCREMENT = config.rotationSpeed;
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
        if (getEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
        //?} else {
        /*if (EntityType.ARMOR_STAND.create(MinecraftClient.getInstance().world).getPreferredEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
        *///?}
            if (!config.armorTooltip) return;
            renderArmorStand(context, x, y, z);
        } else if (stack.getItem().toString().contains("horse_armor")) {
            if (!config.horseArmorTooltip) return;
            renderHorseArmor(context, x, y, z);
        } else if (stack.getItem().toString().contains("wolf_armor")) {
            if (!config.wolfArmorTooltip) return;
            renderWolfArmor(context, x, y, z);
        } else if (stack.getItem() instanceof EntityBucketItem bucketItem) {
            if (!config.bucketTooltip) return;
            renderBucketEntity(context, x, y, z, bucketItem);
        } else if (stack.getItem() instanceof SpawnEggItem spawnEggItem) {
            if (!config.spawnEggTooltip) return;
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
        var entityType = bucketItem.entityType;
        //? if >1.21.1 {
        var entity = entityType.create(MinecraftClient.getInstance().world, SpawnReason.BUCKET);
        //?} else {
        /*var entity = entityType.create(MinecraftClient.getInstance().world);
        *///?}

        if (entity instanceof Bucketable bucketable) {
            var nbtComponent = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
            bucketable.copyDataFromNbt(nbtComponent.copyNbt());

            if (entityType == EntityType.TROPICAL_FISH) return;
            if (bucketable instanceof PufferfishEntity pufferfishEntity) pufferfishEntity.setPuffState(2);

            float entityWidth = entity.getWidth();
            float entityHeight = entity.getHeight();
            float entityScale = calculateScale(entityWidth, entityHeight);
            int scaledWidth = (int) (entityWidth * entityScale);
            int scaledHeight = (int) (entityHeight * entityScale);
            int entityOffset = scaledWidth + SPACING - 10;

            super.render(context, x - entityOffset - 70, y, scaledWidth + 50, scaledHeight + 10, z, -1);
            drawEntity(context, x - scaledWidth / 2 - SPACING - 35, y + scaledHeight, entityScale, currentRotation, (LivingEntity) bucketable);
        }
    }

    private void renderSpawnEggEntity(DrawContext context, int x, int y, int z, SpawnEggItem spawnEggItem) throws Exception {
        var entityType = spawnEggItem.type;
        //? if >1.21.1 {
        var entity = entityType.create(MinecraftClient.getInstance().world, SpawnReason.BUCKET);
        //?} else {
        /*var entity = entityType.create(MinecraftClient.getInstance().world);
        *///?}

        if (entityType == EntityType.VILLAGER || entityType == EntityType.ZOMBIE_VILLAGER) {
            var villagerData = new NbtCompound();
            villagerData.putString("profession", "minecraft:none");
            villagerData.putString("type", "minecraft:plains");

            var nbtComponent = stack.getOrDefault(DataComponentTypes.ENTITY_DATA, NbtComponent.DEFAULT);
            var nbt = nbtComponent.copyNbt();

            nbt.put("VillagerData", villagerData);
            if (entity != null) entity.readNbt(nbt);
        }

        if (entityType == EntityType.TROPICAL_FISH) return;
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

        var yawRotation = new Quaternionf().rotateY((float) Math.toRadians(rotationYaw));

        Quaternionf correctionRotation;
        if (entity instanceof CodEntity || entity instanceof SalmonEntity) {
            correctionRotation = new Quaternionf().rotateZ((float) Math.toRadians(-90));
        } else {
            correctionRotation = new Quaternionf().rotateX((float) Math.toRadians(180));
        }

        var combinedRotation = yawRotation.mul(correctionRotation);

        drawEntity(context, x, y, scale, combinedRotation, entity);
    }

    public static void drawEntity(DrawContext context, int x, int y, float scale, Quaternionf rotation, Entity entity) {
        DiffuseLighting.disableGuiDepthLighting();
        context.getMatrices().push();

        context.getMatrices().translate(x, y, 450);
        context.getMatrices().multiplyPositionMatrix(new Matrix4f().scaling(scale, scale, scale));
        context.getMatrices().multiply(rotation);

        var dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false);
        dispatcher.render(entity, 0.0, 0.0, 0.0,/*? if 1.21.1 {*/ /*0.0F,*//*?}*/ 0.0F, context.getMatrices(), context.vertexConsumers, SHADOW_LIGHT_COLOR);
        dispatcher.setRenderShadows(true);

        context.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
    }
}
