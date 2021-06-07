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

import net.minecraft.client.Mouse;
import net.minecraft.client.gui.Element;
import net.minecraft.item.Item;
import net.minecraft.stat.StatType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

/**
 * Fix MC-201723: Statistics sprites don't look pressed when clicked
 *
 * @author Conor Byrne
 */
@Mixin(targets = "net.minecraft.client.gui.screen.StatsScreen$ItemStatsListWidget")
public class ItemStatsListWidgetMixin implements Element {
    @Shadow
    protected int selectedHeaderColumn;

    @Shadow
    @Final
    protected List<StatType<Item>> itemStatTypes;

    /**
     * Stops the render method from setting the selected to -1 all the time
     */
    @Redirect(method = "renderHeader", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Mouse;wasLeftButtonClicked()Z"))
    private boolean wasLeftButtonClicked(Mouse mouse) {
        return true;
    }

    /**
     * Sets the selected header to -1 if the left mouse button was released and the item list is not null
     */
    public boolean mouseReleased(double mouseX, double mouseY, int clickedMouseButton) {
        if (this.itemStatTypes != null && clickedMouseButton == 0) this.selectedHeaderColumn = -1;
        return true;
    }
}
