package kpan.light_and_shadow.item;

import kpan.light_and_shadow.block.BlockInit;
import kpan.light_and_shadow.block.BlockShadow;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemShadow extends ItemMultiModels {

    public ItemShadow(String name) {
        super(name, CreativeTabs.DECORATIONS);
    }

    @Override
    protected String getItemFileName(int i) {
        return "shadow" + Integer.toString(i);
    }

    @Override
    public String getSpecialName(ItemStack stack) {
        return getTranslationKey();
    }

    @Override
    public int metaMax() {
        return 15;
    }

    // ItemBlock
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (block == BlockInit.SHADOW_BLOCK) {
            if (player.isSneaking()) {
                pos = pos.offset(facing);
                return place(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
            } else {
                int new_light = BlockInit.SHADOW_BLOCK.getMetaFromState(iblockstate) + 1;
                new_light = new_light & 15;
                worldIn.setBlockState(pos, iblockstate.withProperty(BlockShadow.SHADOW, new_light));
                BlockShadow.spawnParticle(worldIn, pos);
                return EnumActionResult.SUCCESS;
            }
        } else {
            if (!block.isReplaceable(worldIn, pos))
                pos = pos.offset(facing);

            return place(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }
    }

    private EnumActionResult place(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = player.getHeldItem(hand);

        if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && worldIn.mayPlace(BlockInit.SHADOW_BLOCK, pos, false, facing, player)) {
            int i = getMetadata(itemstack.getMetadata());
            IBlockState iblockstate1 = BlockInit.SHADOW_BLOCK.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand);

            if (placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1)) {
                iblockstate1 = worldIn.getBlockState(pos);
                SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, worldIn, pos, player);
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                itemstack.shrink(1);
            }

            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }

    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (!world.setBlockState(pos, newState, 11))
            return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == BlockInit.SHADOW_BLOCK) {
            BlockInit.SHADOW_BLOCK.onBlockPlacedBy(world, pos, state, player, stack);

            if (player instanceof EntityPlayerMP)
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
        }

        return true;
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

}
