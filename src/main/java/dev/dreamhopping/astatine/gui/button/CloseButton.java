/*
 *     Astatine is a mod for Minecraft which fixes many common bugs in the game
 *     Copyright (C) 2021 Conor Byrne
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.dreamhopping.astatine.gui.button;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

/**
 * A bedrock-style close button to be used in GUIs which don't have a close button already
 * See [AdvancementsScreenMixin]
 *
 * @author Conor Byrne (dreamhopping)
 */
public class CloseButton extends ButtonWidget {
    /**
     * The default text colour for a close button
     */
    private final Color textColour = new Color(64, 64, 64);

    /**
     * A constructor for the CloseButton
     *
     * @param x       The x position of the button
     * @param y       The y position of the button
     * @param onPress The action to run when the button is pressed
     */
    public CloseButton(int x, int y, PressAction onPress) {
        super(x, y, 20, 20, Text.of("x"), onPress, null);
    }

    /**
     * Renders a button to the screen
     *
     * @param matrices The matrix stack of the current screen
     * @param mouseX   The x position of the mouse
     * @param mouseY   The y position of the mouse
     */
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // Render text, we don't use drawCenteredText because that automatically calls drawWithShadow, which we do not want
        int color = this.isHovered() ? textColour.brighter().brighter().getRGB() : textColour.getRGB();
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        textRenderer.draw(matrices, this.getMessage(), (float) (this.x + this.width / 2 - textRenderer.getWidth(this.getMessage()) / 2), y, color | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }
}
