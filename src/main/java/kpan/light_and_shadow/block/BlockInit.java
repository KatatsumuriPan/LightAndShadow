package kpan.light_and_shadow.block;

import java.util.ArrayList;

public class BlockInit {

    public static final ArrayList<BlockBase> BLOCKS = new ArrayList<>();

    public static final BlockLight LIGHT_BLOCK = new BlockLight("light_block");
    public static final BlockShadow SHADOW_BLOCK = new BlockShadow("shadow_block");

}
