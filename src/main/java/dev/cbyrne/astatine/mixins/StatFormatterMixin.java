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

import net.minecraft.stat.StatFormatter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * Fix MC-72494: In Statistics screen 'm' is the same unit for both minutes and meters
 *
 * @author UserTeemu with help of LlamaLad7
 */
@Mixin(StatFormatter.class)
public interface StatFormatterMixin {
    /**
     * @author UserTeemu with help of LlamaLad7
     */
    @Overwrite
    @SuppressWarnings("all")
    static String method_16819(int i) {
        double d = (double) i / 20.0D;
        double e = d / 60.0D;
        double f = e / 60.0D;
        double g = f / 24.0D;
        double h = g / 365.0D;
        if (h > 0.5D) {
            return StatFormatter.DECIMAL_FORMAT.format(h) + " y";
        } else if (g > 0.5D) {
            return StatFormatter.DECIMAL_FORMAT.format(g) + " d";
        } else if (f > 0.5D) {
            return StatFormatter.DECIMAL_FORMAT.format(f) + " h";
        } else {
            return e > 0.5D ? StatFormatter.DECIMAL_FORMAT.format(e) + " min" : d + " s";
        }
    }
}