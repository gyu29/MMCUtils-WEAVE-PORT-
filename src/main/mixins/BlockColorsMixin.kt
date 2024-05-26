package me.redth.mmcutils.mixins

import me.redth.mmcutils.core.Core
import net.minecraft.block.BlockColored
import net.minecraft.block.state.IBlockState
import net.minecraft.util.BlockPos
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(BlockColored::class)
abstract class BlockColorsMixin {
    @Inject(method = "getRGBColorValue", at = At("RETURN"), cancellable = true)
    private fun modifyRGBColorValue(
        stateIn: IBlockState,
        blockPosIn: BlockPos,
        cir: CallbackInfoReturnable<Int>
    ) {
        Core.modifyArgs(cir.args, stateIn, blockPosIn)
    }
}
