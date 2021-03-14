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

package dev.dreamhopping.astatine.mixins;

import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fix MC-142555: Cats and Ocelots wont eat tropical fish
 *
 * @author Conor Byrne (dreamhopping) and DJtheRedstoner
 */
@Mixin({CatEntity.class, OcelotEntity.class})
public class CatEntityMixin {
    /**
     * Modifies the list of items passed to the Ingredient.ofItems call to add TROPICAL_FISH to the supported ingredients
     */
    @ModifyArg(method = "<clinit>", at = @At(
            value = "INVOKE", target = "Lnet/minecraft/recipe/Ingredient;ofItems([Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/recipe/Ingredient;"))
    private static ItemConvertible[] modifyItems(ItemConvertible[] items) {
        List<ItemConvertible> newItems = new ArrayList<>();

        // Add the existing items, and TROPICAL_FISH to the item array
        Collections.addAll(newItems, items);
        newItems.add(Items.TROPICAL_FISH);

        return newItems.toArray(new ItemConvertible[0]);
    }
}
