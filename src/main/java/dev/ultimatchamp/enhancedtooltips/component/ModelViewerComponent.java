package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

//? if >1.20.4 {
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
//?}

public class ModelViewerComponent extends ColorBorderComponent {
    private static final float ROTATION_INCREMENT = 0.2f;
    private static float currentRotation = 0f;

    private static final int ENTITY_SIZE = 30;
    private static final int SPACING = 20;
    private static final int ENTITY_OFFSET = ENTITY_SIZE + SPACING - 10;
    private static final int SHADOW_LIGHT_COLOR = 15728880;

    private final ItemStack stack;
    private final EnhancedTooltipsConfig config;

    public ModelViewerComponent(ItemStack stack, int color) {
        super(color);
        this.stack = stack;
        this.config = EnhancedTooltipsConfig.load();
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

        if (stack.getItem() instanceof ArmorItem) {
            if (!config.armorTooltip) return;
            renderArmorStand(context, x, y, z);
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
        //?} else if >1.20.6 {
        /*armorStand.equipStack(armorStand.getPreferredEquipmentSlot(stack), stack);
        *///?} else {
        /*armorStand.equipStack(LivingEntity.getPreferredEquipmentSlot(stack), stack);
        *///?}

        super.render(context, x - ENTITY_OFFSET - 25, y, ENTITY_SIZE + 10, ENTITY_SIZE + 30 + 10, z, -1);
        drawEntity(context, x - ENTITY_SIZE / 2 - SPACING - 10, y + ENTITY_SIZE + 30 + 5, ENTITY_SIZE, currentRotation, armorStand);
    }

    private void renderBucketEntity(DrawContext context, int x, int y, int z, EntityBucketItem bucketItem) throws Exception {
        var entityType = bucketItem.entityType;
        //? if >1.21.1 {
        var entity = entityType.create(MinecraftClient.getInstance().world, SpawnReason.BUCKET);
        //?} else {
        /*var entity = entityType.create(MinecraftClient.getInstance().world);
        *///?}

        if (entity instanceof Bucketable bucketable) {
            //? if >1.20.4 {
            var nbtComponent = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
            bucketable.copyDataFromNbt(nbtComponent.copyNbt());
            //?} else {
            /*var nbtComponent = stack.getOrCreateNbt();
            bucketable.copyDataFromNbt(nbtComponent);
            *///?}

            if (entityType == EntityType.TROPICAL_FISH) return;
            if (bucketable instanceof PufferfishEntity pufferfishEntity) pufferfishEntity.setPuffState(2);

            super.render(context, x - ENTITY_OFFSET - 70, y, ENTITY_SIZE + 50, ENTITY_SIZE + 10, z, -1);
            drawEntity(context, x - ENTITY_SIZE / 2 - SPACING - 35, y + ENTITY_SIZE, ENTITY_SIZE, currentRotation, (LivingEntity) bucketable);
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

            //? if >1.20.4 {
            var nbtComponent = stack.getOrDefault(DataComponentTypes.ENTITY_DATA, NbtComponent.DEFAULT);
            var nbt = nbtComponent.copyNbt();
            //?} else {
            /*var nbt = stack.getOrCreateNbt();
            *///?}

            nbt.put("VillagerData", villagerData);
            entity.readNbt(nbt);
        }

        if (entityType == EntityType.TROPICAL_FISH) return;
        if (entity instanceof PufferfishEntity pufferfishEntity) pufferfishEntity.setPuffState(2);
        if (entity instanceof SnowGolemEntity snowGolemEntity) snowGolemEntity.setHasPumpkin(false);

        if (entity instanceof LivingEntity livingEntity) {
            super.render(context, x - ENTITY_OFFSET - 70, y, ENTITY_SIZE + 50, ENTITY_SIZE + 50, z, -1);

            var size = ENTITY_SIZE;
            if (entity instanceof GhastEntity) size = 10;
            if (entity instanceof CamelEntity) size = 20;

            drawEntity(context, x - size / 2 - SPACING - 35, y + size * 2 + SPACING, size, currentRotation, livingEntity);
        }
    }

    public static void drawEntity(DrawContext context, int x, int y, int size, float rotationYaw, LivingEntity entity) {
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

        drawEntity(context, x, y, size, combinedRotation, entity);
    }

    public static void drawEntity(DrawContext context, int x, int y, int size, Quaternionf rotation, Entity entity) {
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 450);
        context.getMatrices().multiplyPositionMatrix(new Matrix4f().scaling(size, size, size));
        context.getMatrices().multiply(rotation);

        DiffuseLighting.method_34742();

        var dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false);
        //? if >1.21.1 {
        dispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, context.getMatrices(), context.vertexConsumers, SHADOW_LIGHT_COLOR);
        //?} else {
        /*dispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, context.getMatrices(), context.vertexConsumers, SHADOW_LIGHT_COLOR);
        *///?}
        dispatcher.setRenderShadows(true);

        context.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
    }
}
