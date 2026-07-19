package dev.ultimatchamp.enhancedtooltips.util;

import net.minecraft.client.Minecraft;

public class PositionUtil {
    public enum Side {
        LEFT, RIGHT, ABOVE, BELOW;

        private boolean isHorizontal() {
            return this == LEFT || this == RIGHT;
        }

        private Side[] order() {
            return switch (this) {
                case LEFT -> new Side[]{LEFT, RIGHT, ABOVE, BELOW};
                case RIGHT -> new Side[]{RIGHT, LEFT, ABOVE, BELOW};
                case ABOVE -> new Side[]{ABOVE, BELOW, LEFT, RIGHT};
                case BELOW -> new Side[]{BELOW, ABOVE, LEFT, RIGHT};
            };
        }
    }

    public record Bounds(int x, int y, int width, int height) {
        public Bounds union(Bounds other) {
            int minX = Math.min(this.x, other.x);
            int minY = Math.min(this.y, other.y);
            int maxX = Math.max(this.x + this.width, other.x + other.width);
            int maxY = Math.max(this.y + this.height, other.y + other.height);

            return new Bounds(minX, minY, maxX - minX, maxY - minY);
        }
    }

    public static Bounds clampToScreen(Bounds anchor, int width, int height, int gap, Side preferredSide) {
        var window = Minecraft.getInstance().getWindow();
        int screenWidth = window.getGuiScaledWidth();
        int screenHeight = window.getGuiScaledHeight();

        Side side = null;

        for (Side candidate : preferredSide.order()) {
            if (space(candidate, anchor, screenWidth, screenHeight) >= (candidate.isHorizontal() ? width : height) + gap) {
                side = candidate;
                break;
            }
        }

        if (side == null) {
            for (Side candidate : preferredSide.order()) {
                if (side == null || space(candidate, anchor, screenWidth, screenHeight) > space(side, anchor, screenWidth, screenHeight))
                    side = candidate;
            }
        }

        return switch (side) {
            case LEFT -> new Bounds(anchor.x - width - gap, anchor.y + clampPerpendicular(anchor.y, height, screenHeight), width, height);
            case RIGHT -> new Bounds(anchor.x + anchor.width + gap, anchor.y + clampPerpendicular(anchor.y, height, screenHeight), width, height);
            case ABOVE -> new Bounds(anchor.x + clampPerpendicular(anchor.x, width, screenWidth), anchor.y - height - gap, width, height);
            case BELOW -> new Bounds(anchor.x + clampPerpendicular(anchor.x, width, screenWidth), anchor.y + anchor.height + gap, width, height);
        };
    }

    private static int space(Side side, Bounds anchor, int screenWidth, int screenHeight) {
        return switch (side) {
            case LEFT -> anchor.x;
            case RIGHT -> screenWidth - (anchor.x + anchor.width);
            case ABOVE -> anchor.y;
            case BELOW -> screenHeight - (anchor.y + anchor.height);
        };
    }

    private static int clampPerpendicular(int anchor, int size, int screenSize) {
        return Math.max(0, Math.min(anchor, screenSize - size)) - anchor;
    }
}
