package kpan.light_and_shadow.util.handlers;

import java.util.Random;
import kpan.light_and_shadow.block.BlockInit;
import kpan.light_and_shadow.client.particle.ParticleBlockConditional;
import kpan.light_and_shadow.client.particle.ParticleLight;
import kpan.light_and_shadow.client.particle.ParticleShadow;
import kpan.light_and_shadow.item.ItemInit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

@EventBusSubscriber
public class TickingClientHandler {

    private static int lightHeldTick = 0;
    private static int shadowHeldTick = 0;

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == Phase.END) {
            Minecraft minecraft = Minecraft.getMinecraft();
            EntityPlayerSP player = minecraft.player;
            World world = minecraft.world;
            if (player == null || world == null) {
                lightHeldTick = 0;
                shadowHeldTick = 0;
                ParticleBlockConditional.clear();
                return;
            }
            ItemStack itemstack = player.getHeldItemMainhand();
            boolean flag = minecraft.playerController.getCurrentGameType() == GameType.CREATIVE && !itemstack.isEmpty();
            if (!flag) {
                lightHeldTick = 0;
                shadowHeldTick = 0;
                return;
            }
            Random random = new Random();
            Item item = itemstack.getItem();
            if (item == ItemInit.LIGHT) {
                showLightParticles(random, player, world);
                lightHeldTick++;
                shadowHeldTick = 0;
            } else if (item == ItemInit.SHADOW) {
                showShadowParticles(random, player, world);
                shadowHeldTick++;
                lightHeldTick = 0;
            } else {
                lightHeldTick = 0;
                shadowHeldTick = 0;
            }
        }
    }

    private static void showLightParticles(Random random, EntityPlayerSP player, World world) {
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();

        int x = MathHelper.floor(player.posX);
        int y = MathHelper.floor(player.posY);
        int z = MathHelper.floor(player.posZ);
        showLightParticle(world, x, y, z, 1, random, mpos);
        for (int i = 0; i < 400; ++i) {
            showLightParticle(world, x, y, z, 16, random, mpos);
        }
        for (int i = 0; i < 200; ++i) {
            showLightParticle(world, x, y, z, 32, random, mpos);
        }
    }

    private static void showShadowParticles(Random random, EntityPlayerSP player, World world) {
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();

        int x = MathHelper.floor(player.posX);
        int y = MathHelper.floor(player.posY);
        int z = MathHelper.floor(player.posZ);
        showShadowParticle(world, x, y, z, 1, random, mpos);
        for (int i = 0; i < 400; ++i) {
            showShadowParticle(world, x, y, z, 16, random, mpos);
        }
        for (int i = 0; i < 200; ++i) {
            showShadowParticle(world, x, y, z, 32, random, mpos);
        }
    }

    public static void showLightParticle(World world, int x, int y, int z, int range, Random random, BlockPos.MutableBlockPos pos) {
        int x1 = x + random.nextInt(range) - random.nextInt(range);
        int y1 = y + random.nextInt(range) - random.nextInt(range);
        int z1 = z + random.nextInt(range) - random.nextInt(range);
        pos.setPos(x1, y1, z1);
        IBlockState iblockstate = world.getBlockState(pos);

        if (iblockstate.getBlock() == BlockInit.LIGHT_BLOCK) {
            ParticleBlockConditional particle = new ParticleLight(world, x1 + 0.5, y1 + 0.5, z1 + 0.5);
            ParticleBlockConditional.spawnParticle(particle);
        }
    }

    public static void showShadowParticle(World world, int x, int y, int z, int range, Random random, BlockPos.MutableBlockPos pos) {
        int x1 = x + random.nextInt(range) - random.nextInt(range);
        int y1 = y + random.nextInt(range) - random.nextInt(range);
        int z1 = z + random.nextInt(range) - random.nextInt(range);
        pos.setPos(x1, y1, z1);
        IBlockState iblockstate = world.getBlockState(pos);

        if (iblockstate.getBlock() == BlockInit.SHADOW_BLOCK) {
            ParticleBlockConditional particle = new ParticleShadow(world, x1 + 0.5, y1 + 0.5, z1 + 0.5);
            ParticleBlockConditional.spawnParticle(particle);
        }
    }

}
