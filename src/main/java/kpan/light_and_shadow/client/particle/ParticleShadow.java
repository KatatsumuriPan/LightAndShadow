package kpan.light_and_shadow.client.particle;

import kpan.light_and_shadow.ModTagsGenerated;
import kpan.light_and_shadow.block.BlockInit;
import kpan.light_and_shadow.item.ItemInit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

@SideOnly(Side.CLIENT)
public class ParticleShadow extends ParticleBlockConditional {

    public ParticleShadow(World world, double x, double y, double z) {
        super(world, x, y, z, ItemInit.SHADOW);
    }

    @Override
    protected @Nullable ResourceLocation getTexture() {
        if (world == null)
            return null;
        IBlockState state = world.getBlockState(new BlockPos(posX, posY, posZ));
        if (state.getBlock() != BlockInit.SHADOW_BLOCK)
            return null;
        return new ResourceLocation(ModTagsGenerated.MODID,
                "textures/items/shadow_" + String.format("%02d", BlockInit.SHADOW_BLOCK.getLightOpacity(state)) + ".png");
    }
    @Override
    protected boolean isValidBlock() {
        if (world == null)
            return false;
        IBlockState state = world.getBlockState(new BlockPos(posX, posY, posZ));
        if (state.getBlock() != BlockInit.SHADOW_BLOCK)
            return false;
        return true;
    }
}
