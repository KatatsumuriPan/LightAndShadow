package kpan.light_and_shadow.block;

import java.util.ArrayList;
import kpan.light_and_shadow.client.particle.ParticleBlockConditional;
import kpan.light_and_shadow.client.particle.ParticleShadow;
import kpan.light_and_shadow.item.ItemInit;
import kpan.light_and_shadow.util.interfaces.block.IHasMultiModels;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockShadow extends BlockBase implements IHasMultiModels, IBlockUncollidable {

    public static final PropertyInteger SHADOW = PropertyInteger.create("block_shadow_level", 0, 15);

    public BlockShadow(String name) {
        super(name, Material.BARRIER);
        setCreativeTab(CreativeTabs.DECORATIONS);
        setBlockUnbreakable();
        setResistance(6000001.0F);
        disableStats();
        translucent = true;
    }

    @Override
    public void registerAsItem() { }

    @Override
    public int getLightOpacity(IBlockState state) {
        return state.getValue(SHADOW);
    }

    @Override
    public Item getItem(IBlockState state) {
        return ItemInit.SHADOW;
    }

    @Override
    public boolean isTouchingItem(ItemStack stack) {
        return stack.getItem() == ItemInit.SHADOW;
    }

    @Override
    public RayTraceResult rayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        return rayTrace(pos, start, end, blockState.getBoundingBox(worldIn, pos));
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public String getSpecialName(ItemStack stack) {
        return getTranslationKey();
    }

    @Override
    public int metaMax() {
        return 15;
    }

    @Override
    public String getInventoryItemStateName(int itemMeta) {
        return "inventory_" + Integer.toString(itemMeta);
    }

    @Override
    protected ArrayList<IProperty<?>> getProperties() {
        ArrayList<IProperty<?>> properties = super.getProperties();
        properties.add(SHADOW);
        return properties;
    }

    @Override
    public int getMetaFromState(IBlockState state) { return state.getValue(SHADOW); }
    @Override
    public IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(SHADOW, meta); }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
        return false;
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isFullCube(IBlockState state) { return false; }

    @Override
    public boolean isOpaqueCube(IBlockState state) { return false; }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getAmbientOcclusionLightValue(IBlockState state) { return 1.0F; }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) { }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        spawnParticle(worldIn, pos);
    }

    public static void spawnParticle(World worldIn, BlockPos pos) {
        if (worldIn.isRemote) {
            Client.renderParticle(worldIn, pos);
        }
    }

    private static class Client {
        @SideOnly(Side.CLIENT)
        public static void renderParticle(World world, BlockPos pos) {
            if (Minecraft.getMinecraft().playerController.getCurrentGameType() != GameType.CREATIVE)
                return;
            ParticleBlockConditional particle = new ParticleShadow(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            ParticleBlockConditional.spawnParticle(particle);
        }
    }
}
