package dev.ultimatchamp.enhancedtooltips.util;

import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix3x2fStack;

public class MatricesUtil {
    private final Object matrixStack;

    public MatricesUtil(Object matrixStack) {
        this.matrixStack = matrixStack;
    }
    public void pushMatrix() {
        if (this.matrixStack instanceof Matrix3x2fStack m) {
            m.pushMatrix();
        } else if (this.matrixStack instanceof MatrixStack m) {
            m.push();
        }
    }

    public void popMatrix() {
        if (this.matrixStack instanceof Matrix3x2fStack m) {
            m.popMatrix();
        } else if (this.matrixStack instanceof MatrixStack m) {
            m.pop();
        }
    }

    public void trans(float x, float y, float z) {
        if (this.matrixStack instanceof Matrix3x2fStack m) {
            m.translate(x, y);
        } else if (this.matrixStack instanceof MatrixStack m) {
            m.translate(x, y, z);
        }
    }

    public void scal(float x, float y, float z) {
        if (this.matrixStack instanceof Matrix3x2fStack m) {
            m.scale(x, y);
        } else if (this.matrixStack instanceof MatrixStack m) {
            m.scale(x, y, z);
        }
    }
}
