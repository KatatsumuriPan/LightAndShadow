package kpan.light_and_shadow.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface IBlockUncollidable {
    boolean isTouchingItem(ItemStack stack);
    RayTraceResult rayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end);
}
