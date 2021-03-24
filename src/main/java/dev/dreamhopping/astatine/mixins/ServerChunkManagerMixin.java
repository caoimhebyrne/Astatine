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

import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.WorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.function.Consumer;

/**
 * Fix ArrayList#forEach usage impacting how long it takes to run a chunk tick
 *
 * @author LlamaLad7
 */
@Mixin(ServerChunkManager.class)
public class ServerChunkManagerMixin {
    @Shadow
    private void method_20801(long l, boolean b, SpawnHelper.Info info, boolean b1, int i, ChunkHolder chunkHolder) {
    }

    /**
     * Iterate over all ChunkHolder instances in the list using a for loop and not the ArrayList#forEach
     *
     * @author LlamaLad7
     */
    @Inject(method = "tickChunks", at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void storeLocals(CallbackInfo ci, long l, long m, WorldProperties worldProperties, boolean bl, boolean bl2, int i, boolean bl3, int j, SpawnHelper.Info info, List<ChunkHolder> list) {
        for (ChunkHolder chunkHolder : list) {
            method_20801(m, bl2, info, bl3, i, chunkHolder);
        }
    }

    /**
     * Stop the ArrayList#forEach method from firing
     *
     * @author LlamaLad7
     */
    @Redirect(method = "tickChunks", at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V"))
    private void removeForEachCall(List<ChunkHolder> list, Consumer<ChunkHolder> action) {
    }
}
