/*
 * This file is licensed under the MIT License, part of ToolTipFix.
 * Copyright (c) 2019 kyrptonaught
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.ultimatchamp.enhancedtooltips.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;
import org.jetbrains.annotations.NotNull;

public class EnhancedTooltipsTextVisitor implements FormattedCharSink {
    private final MutableComponent text = Component.empty();

    @Override
    public boolean accept(int index, @NotNull Style style, int codePoint) {
        String car = new String(Character.toChars(codePoint));
        text.append(Component.literal(car).setStyle(style));
        return true;
    }

    public Component getText() {
        return text;
    }

    public static Component get(FormattedCharSequence text) {
        EnhancedTooltipsTextVisitor visitor = new EnhancedTooltipsTextVisitor();
        text.accept(visitor);
        return visitor.getText();
    }
}
