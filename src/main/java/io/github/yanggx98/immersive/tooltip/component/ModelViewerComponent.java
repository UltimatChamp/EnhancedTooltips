package io.github.yanggx98.immersive.tooltip.component;

import io.github.yanggx98.immersive.tooltip.ImmersiveTooltip;
import io.github.yanggx98.immersive.tooltip.mixin.EntityBucketItemMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class ModelViewerComponent extends ColorBorderComponent {
    private static final float ROTATION_INCREMENT = 0.4f;
    private static float currentRotation = 0f;

    private static final int ENTITY_SIZE = 30;
    private static final int SPACING = 20;
    private static final int ENTITY_OFFSET = ENTITY_SIZE + SPACING - 10;
    private static final int SHADOW_LIGHT_COLOR = 15728880;

    private final ItemStack stack;

    public ModelViewerComponent(ItemStack stack, int color) {
        super(color);
        this.stack = stack;
    }

    public static EquipmentSlot getEquipmentSlot(ItemStack itemStack) {
        EquippableComponent equippable = itemStack.get(DataComponentTypes.EQUIPPABLE);
        return equippable != null ? equippable.slot() : EquipmentSlot.MAINHAND;
    }

    @Override
    public void render(DrawContext context, int x, int y, int width, int height, int z, int page) throws Exception {
        super.render(context, x, y, width, height, z, page);

        if (page != 0 || !ImmersiveTooltip.isRenderingArmorModel) return;

        currentRotation = (currentRotation + ROTATION_INCREMENT) % 360;

        if (stack.getItem() instanceof ArmorItem) {
            renderArmorStand(context, x, y, z);
        } else if (stack.getItem() instanceof EntityBucketItem bucketItem) {
            renderBucketEntity(context, x, y, z, bucketItem);
        }
    }

    private void renderArmorStand(DrawContext context, int x, int y, int z) throws Exception {
        ArmorStandEntity armorStand = new ArmorStandEntity(EntityType.ARMOR_STAND, MinecraftClient.getInstance().world);
        armorStand.equipStack(getEquipmentSlot(stack), stack);

        super.render(context, x - ENTITY_OFFSET - 25, y, ENTITY_SIZE + 10, ENTITY_SIZE + 30 + 10, z, -1);
        drawEntity(context, x - ENTITY_SIZE / 2 - SPACING - 10, y + ENTITY_SIZE + 30 + 5, ENTITY_SIZE, currentRotation, armorStand);
    }

    private void renderBucketEntity(DrawContext context, int x, int y, int z, EntityBucketItem bucketItem) throws Exception {
        EntityType<?> entityType = ((EntityBucketItemMixin) bucketItem).getEntityType();
        Entity entity = entityType.create(MinecraftClient.getInstance().world, SpawnReason.MOB_SUMMONED);

        if (entity instanceof AxolotlEntity axolotl) {
            NbtCompound nbt = stack.get(DataComponentTypes.BUCKET_ENTITY_DATA).getNbt();
            axolotl.copyDataFromNbt(nbt != null ? nbt : new NbtCompound());
            axolotl.setFromBucket(false);

            super.render(context, x - ENTITY_OFFSET - 70, y, ENTITY_SIZE + 50, ENTITY_SIZE + 10, z, -1);
            drawEntity(context, x - ENTITY_SIZE / 2 - SPACING - 35, y + ENTITY_SIZE, ENTITY_SIZE, currentRotation, axolotl);
        }
    }

    public static void drawEntity(DrawContext context, int x, int y, int size, float rotationYaw, LivingEntity entity) throws Exception {
        entity.bodyYaw = rotationYaw;
        entity.setYaw(rotationYaw);
        entity.headYaw = rotationYaw;

        Quaternionf yawRotation = new Quaternionf().rotateY((float) Math.toRadians(-rotationYaw));
        Quaternionf correctionRotation = new Quaternionf().rotateX((float) Math.toRadians(180)); // fix inverted model
        Quaternionf combinedRotation = correctionRotation.mul(yawRotation);

        drawEntity(context, x, y, size, combinedRotation, entity);
    }

    public static void drawEntity(DrawContext context, int x, int y, int size, Quaternionf rotation, Entity entity) throws Exception {
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 450);
        context.getMatrices().multiplyPositionMatrix(new Matrix4f().scaling(size, size, size));
        context.getMatrices().multiply(rotation);

        DiffuseLighting.method_34742();

        EntityRenderDispatcher dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false);
        dispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, context.getMatrices(), context.vertexConsumers, SHADOW_LIGHT_COLOR);
        dispatcher.setRenderShadows(true);

        context.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
    }
}
