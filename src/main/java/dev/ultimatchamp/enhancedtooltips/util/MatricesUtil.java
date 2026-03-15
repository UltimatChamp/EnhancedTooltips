package dev.ultimatchamp.enhancedtooltips.util;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix3x2fStack;

public record MatricesUtil(Object matrixStack) {
    public void pushMatrix() {
        if (this.matrixStack instanceof Matrix3x2fStack m) {
            m.pushMatrix();
        } else if (this.matrixStack instanceof PoseStack m) {
            m.pushPose();
        }
    }

    public void popMatrix() {
        if (this.matrixStack instanceof Matrix3x2fStack m) {
            m.popMatrix();
        } else if (this.matrixStack instanceof PoseStack m) {
            m.popPose();
        }
    }

    public void trans(float x, float y, float z) {
        if (this.matrixStack instanceof Matrix3x2fStack m) {
            m.translate(x, y);
        } else if (this.matrixStack instanceof PoseStack m) {
            m.translate(x, y, z);
        }
    }

    public void scal(float x, float y, float z) {
        if (this.matrixStack instanceof Matrix3x2fStack m) {
            m.scale(x, y);
        } else if (this.matrixStack instanceof PoseStack m) {
            m.scale(x, y, z);
        }
    }
}
