package kpan.light_and_shadow.client.particle;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

@SideOnly(Side.CLIENT)
public abstract class ParticleBlockConditional extends Particle {
    private static final Map<ParticleBlockConditional, ParticleBlockConditional> ADDED_PARTICLES = new HashMap<>();
    public static boolean spawnParticle(ParticleBlockConditional particle) {
        ParticleBlockConditional p = ADDED_PARTICLES.get(particle);
        if (p == null) {
            ADDED_PARTICLES.put(particle, particle);
            Minecraft.getMinecraft().effectRenderer.addEffect(particle);
            return true;
        } else {
            p.resetAge();
            return false;
        }
    }

    public static void clear() {
        ADDED_PARTICLES.clear();
    }

    private static final VertexFormat VERTEX_FORMAT = new VertexFormat().addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB)
            .addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);

    protected final Item handHeld;

    public ParticleBlockConditional(World world, double x, double y, double z, Item handHeld) {
        super(world, x, y, z);
        this.handHeld = handHeld;
        particleScale = 5;
        particleMaxAge = 400;// 20秒
    }

    @Nullable
    protected abstract ResourceLocation getTexture();
    protected abstract boolean isValidBlock();

    public void resetAge() {
        particleAge = 0;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!isValidBlock()) {
            setExpired();
            return;
        }
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player == null) {
            setExpired();
            return;
        }
        if (player.getHeldItemMainhand().getItem() != handHeld && player.getHeldItemOffhand().getItem() != handHeld) {
            setExpired();
            return;
        }
    }

    @Override
    public void setExpired() {
        super.setExpired();
        ADDED_PARTICLES.remove(this);
    }
    @Override
    public int getFXLayer() { return 3; }

    @Override
    public void renderParticle(
            BufferBuilder bufferIn,
            Entity entityIn,
            float partialTicks,
            float rotationX, float rotationZ,
            float rotationYZ, float rotationXY, float rotationXZ) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(516, 0.003921569F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, particleAlpha);

        ResourceLocation texture = getTexture();
        if (texture == null)
            return;
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

        float uMin = 0;
        float uMax = 1;
        float vMin = 0;
        float vMax = 1;
        float scale = 0.1F * particleScale;
        float xInterp = (float) (prevPosX + (posX - prevPosX) * partialTicks - interpPosX);
        float yInterp = (float) (prevPosY + (posY - prevPosY) * partialTicks - interpPosY);
        float zInterp = (float) (prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ);

        Vec3d[] avec3d = new Vec3d[]{new Vec3d(-rotationX * scale - rotationXY * scale, -rotationZ * scale, -rotationYZ * scale - rotationXZ * scale),
                new Vec3d(-rotationX * scale + rotationXY * scale, rotationZ * scale, -rotationYZ * scale + rotationXZ * scale),
                new Vec3d(rotationX * scale + rotationXY * scale, rotationZ * scale, rotationYZ * scale + rotationXZ * scale),
                new Vec3d(rotationX * scale - rotationXY * scale, -rotationZ * scale, rotationYZ * scale - rotationXZ * scale)};

        if (particleAngle != 0.0F) {
            float angleInterp = particleAngle + (particleAngle - prevParticleAngle) * partialTicks;
            float f9 = MathHelper.cos(angleInterp * 0.5F);
            float xComponent = MathHelper.sin(angleInterp * 0.5F) * (float) cameraViewDir.x;
            float yComponent = MathHelper.sin(angleInterp * 0.5F) * (float) cameraViewDir.y;
            float zComponent = MathHelper.sin(angleInterp * 0.5F) * (float) cameraViewDir.z;
            Vec3d vec3d = new Vec3d(xComponent, yComponent, zComponent);

            for (int l = 0; l < 4; ++l) {
                avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale(f9 * f9 - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale(2.0F * f9));
            }
        }

        int brightness = 15 << 20 | 15 << 4;
        int skyLight = brightness >> 16 & 65535;
        int blockLight = brightness & 65535;

        bufferIn.begin(7, VERTEX_FORMAT);
        bufferIn.pos(xInterp + avec3d[0].x, yInterp + avec3d[0].y, zInterp + avec3d[0].z)
                .tex(uMax, vMax)
                .color(particleRed, particleGreen, particleBlue, particleAlpha)
                .lightmap(skyLight, blockLight)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
        bufferIn.pos(xInterp + avec3d[1].x, yInterp + avec3d[1].y, zInterp + avec3d[1].z)
                .tex(uMax, vMin)
                .color(particleRed, particleGreen, particleBlue, particleAlpha)
                .lightmap(skyLight, blockLight)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
        bufferIn.pos(xInterp + avec3d[2].x, yInterp + avec3d[2].y, zInterp + avec3d[2].z)
                .tex(uMin, vMin)
                .color(particleRed, particleGreen, particleBlue, particleAlpha)
                .lightmap(skyLight, blockLight)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
        bufferIn.pos(xInterp + avec3d[3].x, yInterp + avec3d[3].y, zInterp + avec3d[3].z)
                .tex(uMin, vMax)
                .color(particleRed, particleGreen, particleBlue, particleAlpha)
                .lightmap(skyLight, blockLight)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();

        Tessellator.getInstance().draw();
        GlStateManager.popMatrix();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ParticleBlockConditional that = (ParticleBlockConditional) o;
        if (!Objects.equals(posX, that.posX))
            return false;
        if (!Objects.equals(posY, that.posY))
            return false;
        if (!Objects.equals(posZ, that.posZ))
            return false;
        return true;
    }
    @Override
    public int hashCode() {
        return Objects.hash(posX, posY, posZ);
    }
}
