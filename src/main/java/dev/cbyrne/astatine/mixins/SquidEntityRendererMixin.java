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

import net.minecraft.client.render.entity.SquidEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fix MC-115092: Squid named "Dinnerbone" or "Grumm" is not upside-down
 * SquidEntityRenderer#setupTransforms does not call the super method, meaning that the transformations for upside down are not applied
 *
 * @author Conor Byrne
 */
@Mixin(SquidEntityRenderer.class)
public class SquidEntityRendererMixin {
    /**
     * Inserts code at the start of SquidEntityRenderer#setupTransforms to fix transformations to support "upside down squids"
     */
    @Inject(method = "setupTransforms", at = @At("HEAD"), cancellable = true)
    private void setupTransforms(SquidEntity squidEntity, MatrixStack matrixStack, float f, float g, float h, CallbackInfo ci) {
        // Remove any formatting & check if the name is equal to Dinnerbone or Grumm
        String customName = Formatting.strip(squidEntity.getName().getString());
        if (("Dinnerbone".equals(customName) || "Grumm".equals(customName))) {
            // Rotate the entity by 180 degrees
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F));

            // Bounding box transformation
            matrixStack.translate(0.0D, -0.2D, 0.0D);

            // Apply the roll angle
            float rollAngle = MathHelper.lerp(h, squidEntity.prevRollAngle, squidEntity.rollAngle);
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rollAngle));

            // Apply an unknown transformation & cancel any other transformations
            matrixStack.translate(0.0D, -1.2000000476837158D, 0.0D);
            ci.cancel();
        }
    }
}
