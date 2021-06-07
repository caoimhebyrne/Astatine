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

package dev.cbyrne.astatine.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.stat.Stat;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Fix MC-169386: High numbers in the "Statistics" screen are overlapping with other columns
 *
 * @author Conor Byrne (dreamhoping)
 */
@Mixin(targets = "net.minecraft.client.gui.screen.StatsScreen$ItemStatsListWidget$Entry")
public class ItemStatsListWidgetEntryMixin {
    /**
     * The formatters used to convert an int (f.ex. 2234214) to a readable string that doesnt overlap other text (f.ex. 2.22M)
     */
    private static final DecimalFormat millionFormat = Util.make(new DecimalFormat("#.#M"), (format) -> format.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH)));
    private static final DecimalFormat billionFormat = Util.make(new DecimalFormat("#.#B"), (format) -> format.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH)));

    /**
     * Solves the overflowing problem of text when the numbers start reaching the millions
     * If the statistic is over 1 million, we must tidy up the string (f.ex: 1,000,000 -> 1M)
     *
     * @return the newly-formatted string if above 1 million, otherwise the original
     */
    private static String customFormat(Stat<?> stat, int i) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return "";

        double value = player.getStatHandler().getStat(stat);
        if (value > 1_000_000_000) {
            return billionFormat.format(value / 1_000_000_000);
        } else if (value > 1_000_000) {
            return millionFormat.format(value / 1_000_000);
        } else {
            return stat.format((int) value);
        }
    }

    /**
     * Redirects all Stat#format calls to our customFormat method
     */
    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/stat/Stat;IIZ)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/Stat;format(I)Ljava/lang/String;")
    )
    private String format(Stat<?> stat, int i) {
        return customFormat(stat, i);
    }

    /**
     * Renders tooltips above the formatted statistics to show their full values
     */
    @Inject(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/stat/Stat;IIZ)V",
        at = @At(value = "RETURN")
    )
    private void render(MatrixStack matrices, Stat<?> stat, int x, int y, boolean bl, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.currentScreen == null || stat == null) return;

        int statInt = mc.player.getStatHandler().getStat(stat);
        String statText = customFormat(stat, statInt);

        if (statText == null) statText = "-";
        int textWidth = mc.textRenderer.getWidth(statText);
        int textHeight = mc.textRenderer.fontHeight + 1;
        int offsetX = x - textWidth;
        int textEnd = offsetX + textWidth + 3;

        double scaleFactor = mc.getWindow().getScaleFactor();
        double scaledMouseX = mc.mouse.getX() / scaleFactor;
        double scaledMouseY = mc.mouse.getY() / scaleFactor;

        if ((scaledMouseX < textEnd && scaledMouseX >= (offsetX)) && (scaledMouseY <= y + textHeight && scaledMouseY > y - 1)) {
            mc.currentScreen.renderTooltip(matrices, Text.of(stat.format(statInt)), (int) scaledMouseX, (int) scaledMouseY);
        }
    }
}
