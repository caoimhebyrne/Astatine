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
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.dreamhopping.astatine.mixins;

import dev.dreamhopping.astatine.mixins.accessor.WorldListWidgetAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Date;

/**
 * Fix MC-383: World names/versions/timestamps can overflow the list to the right
 *
 * @author Conor Byrne (dreamhopping)
 */
@Mixin(WorldListWidget.Entry.class)
public class WorldListWidgetEntryMixin {
    private final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    private final int truncatedDotsWidth = textRenderer.getWidth("...");

    /**
     * Redirect the LevelSummary#getName call to trim the string to the required amount of characters
     * For example: "My world aaaaaaaaaaaaaaaaaaaaaaaaaaaaa (04/20/21 04:20)"
     * Changes to: "My World aaaaaaaaaaaaaaa... (04/20/21 04:20)"
     *
     * @return the trimmed level name if it is too long, otherwise the original level name
     */
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/LevelSummary;getName()Ljava/lang/String;"))
    private String getName(LevelSummary levelSummary, MatrixStack matrixStack, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        String levelName = levelSummary.getName();
        String dateString = " (" + WorldListWidgetAccessor.getDateFormatter().format(new Date(levelSummary.getLastPlayed())) + ")";

        int dateStringWidth = textRenderer.getWidth(dateString);
        int levelSummaryWidth = textRenderer.getWidth(levelName);

        // If the level name and the date string is larger than the width allowed, truncate the level name with "..."
        if (textRenderer.getWidth(levelName + dateString) >= entryWidth) {
            int textWidth = levelSummaryWidth - (dateStringWidth / 2) - truncatedDotsWidth;
            return textRenderer.trimToWidth(levelName, textWidth) + "...";
        } else {
            return levelName;
        }
    }
}
