package com.cmii.cmiicore.exnihilo.client.renderers;

import com.cmii.cmiicore.exnihilo.blocks.BlockCrucibleBase;
import com.cmii.cmiicore.exnihilo.client.models.ModelVertex;
import com.cmii.cmiicore.exnihilo.config.ModConfig;
import com.cmii.cmiicore.exnihilo.texturing.Color;
import com.cmii.cmiicore.exnihilo.texturing.SpriteColor;
import com.cmii.cmiicore.exnihilo.tiles.TileCrucibleBase;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.animation.FastTESR;

import javax.annotation.Nullable;

public class RenderCrucible extends FastTESR<TileCrucibleBase> {
    private static final ModelVertex[] model_thick = new ModelVertex[4];
    private static final ModelVertex[] model_thin = new ModelVertex[4];

    static {
        model_thick[0] = new ModelVertex(EnumFacing.UP, 0.125, 0.6875, 0.125, 0, 0);
        model_thick[1] = new ModelVertex(EnumFacing.UP, 0.875, 0.6875, 0.125, 1, 0);
        model_thick[2] = new ModelVertex(EnumFacing.UP, 0.875, 0.6875, 0.875, 1, 1);
        model_thick[3] = new ModelVertex(EnumFacing.UP, 0.125, 0.6875, 0.875, 0, 1);
        model_thin[0] = new ModelVertex(EnumFacing.UP, 0.0625, 0.6875, 0.0625, 0, 0);
        model_thin[1] = new ModelVertex(EnumFacing.UP, 0.9375, 0.6875, 0.0625, 1, 0);
        model_thin[2] = new ModelVertex(EnumFacing.UP, 0.9375, 0.6875, 0.9375, 1, 1);
        model_thin[3] = new ModelVertex(EnumFacing.UP, 0.0625, 0.6875, 0.9375, 0, 1);
    }

    @Override
    public void renderTileEntityFast(@Nullable TileCrucibleBase te, double x, double y, double z, float partialTicks, int destroyStage, float partial, @Nullable BufferBuilder buffer) {
        if (te == null || buffer == null) return;
        final BlockPos pos = te.getPos();
        final Block block = getWorld().getBlockState(pos).getBlock();

        // Check because sometimes the renderer gets called while the block is breaking/decaying
        if (!(block instanceof BlockCrucibleBase)) return;

        // Draw
        SpriteColor[] sprites = te.getSpriteAndColor();

        buffer.setTranslation(x, y, z);
        addSpriteColor(te, sprites[0], buffer, te.getSolidProportion());
        addSpriteColor(te, sprites[1], buffer, te.getFluidProportion());
        buffer.setTranslation(0, 0, 0);
    }

    private void addSpriteColor(TileCrucibleBase te, SpriteColor sprite, BufferBuilder buffer, float fill) {
        if (sprite == null || fill <= 0f) return;

        final BlockPos pos = te.getPos();
        // Light levels
        final int mixedBrightness = te.getWorld().getBlockState(pos).getPackedLightmapCoords(te.getWorld(), te.getPos());
        final int skyLight = mixedBrightness >> 16 & 0xFFFF;
        final int blockLight = mixedBrightness & 0xFFFF;
        // Texturing
        TextureAtlasSprite icon = sprite.getSprite();
        Color color = sprite.getColor();

        if(icon == null)
            return;

        // Draw
        final ModelVertex[] model = ModConfig.client.thinCrucibleModel ? model_thin : model_thick;
        for (final ModelVertex vert : model) {
            for (final VertexFormatElement e : buffer.getVertexFormat().getElements()) {
                switch (e.getUsage()) {
                    case COLOR:
                        buffer.color(color.r, color.g, color.b, color.a);
                        break;

                    case NORMAL:
                        buffer.normal(vert.face.getXOffset(), vert.face.getYOffset(), vert.face.getZOffset());
                        break;

                    case POSITION:
                        final double vertX = vert.x;
                        final double vertY = vert.y * fill + 0.25;
                        final double vertZ = vert.z;

                        buffer.pos(vertX, vertY, vertZ);
                        break;

                    case UV:
                        if (e.getIndex() == 1) {
                            buffer.lightmap(skyLight, blockLight);
                        } else {
                            buffer.tex(icon.getInterpolatedU(vert.u), icon.getInterpolatedV(16.0 - vert.v));
                        }
                        break;

                    default:
                        break;
                }
            }
            buffer.endVertex();
        }
    }

}