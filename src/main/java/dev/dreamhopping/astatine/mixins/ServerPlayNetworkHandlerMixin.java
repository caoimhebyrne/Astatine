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

import net.minecraft.network.packet.c2s.play.UpdateCommandBlockMinecartC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.ChatUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fix MC-131562: Pressing "Done" in an empty command block minecart returns "Command set:"
 *
 * @author Conor Byrne (dreamhopping)
 */
@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    /**
     * Adds a ChatUtil#isEmpty check before running ServerPlayerEntity#sendSystemMessage
     * Originally this check wasn't there, which meant pressing "Done" in an empty command block minecart returns "Command set:" when it shouldn't do anything, like a regular command block
     *
     * @see <a href="https://bugs.mojang.com/browse/MC-131562?focusedCommentId=936217&page=com.atlassian.jira.plugin.system.issuetabpanels%3Acomment-tabpanel#comment-"936217">Mojang JIRA Page</a>
     */
    @Inject(method = "onUpdateCommandBlockMinecart", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;sendSystemMessage(Lnet/minecraft/text/Text;Ljava/util/UUID;)V"), cancellable = true)
    private void onUpdateCommandBlockMinecart(UpdateCommandBlockMinecartC2SPacket packet, CallbackInfo ci) {
        // Check if the command is empty before sending the message
        String command = packet.getCommand();
        if (ChatUtil.isEmpty(command)) {
            // The command string is empty, return here
            ci.cancel();
        }
    }
}
