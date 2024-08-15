package com.invadermonky.stripmining.event;

import com.google.common.collect.ImmutableList;
import com.invadermonky.stripmining.item.IAreaBrealToolSM;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class RenderEvent {
    public static final RenderEvent INSTANCE = new RenderEvent();
    private final TextureAtlasSprite[] blockDamageIcons = new TextureAtlasSprite[10];

    @SubscribeEvent (priority = EventPriority.LOW)
    public void renderExtraBlockBreak(RenderWorldLastEvent event) {
        PlayerControllerMP controllerMP = Minecraft.getMinecraft().playerController;

        if (controllerMP == null) {
            return;
        }
        EntityPlayer player = Minecraft.getMinecraft().player;
        ItemStack stack = player.getHeldItemMainhand();

        if (!stack.isEmpty() && stack.getItem() instanceof IAreaBrealToolSM) {
            Entity renderEntity = Minecraft.getMinecraft().getRenderViewEntity();
            if (renderEntity == null)
                return;

            IAreaBrealToolSM aoeTool = (IAreaBrealToolSM) stack.getItem();

            double distance = Math.max(controllerMP.getBlockReachDistance(), aoeTool.getReachDistance(stack));
            RayTraceResult traceResult = renderEntity.rayTrace(distance, event.getPartialTicks());

            if (traceResult != null) {
                ImmutableList<BlockPos> extraBlocks = aoeTool.getAreaBreakBlocks(stack, traceResult.getBlockPos(), player);
                for (BlockPos pos : extraBlocks) {
                    event.getContext().drawSelectionBox(player, new RayTraceResult(new Vec3d(0, 0, 0), null, pos), 0, event.getPartialTicks());
                }
            }
        }

        if (controllerMP.getIsHittingBlock()) {
            if (!stack.isEmpty() && stack.getItem() instanceof IAreaBrealToolSM) {
                BlockPos pos = controllerMP.currentBlock;
                IAreaBrealToolSM aoeTool = (IAreaBrealToolSM) stack.getItem();
                drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), player, event.getPartialTicks(), player.getEntityWorld(), aoeTool.getAreaBreakBlocks(stack, pos, player));
            }
        }
    }

    public void drawBlockDamageTexture(Tessellator tessellatorIn, BufferBuilder bufferIn, Entity entityIn, float partialTicks, World world, List<BlockPos> blocks) {
        double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double) partialTicks;
        double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double) partialTicks;
        double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double) partialTicks;

        TextureManager renderEngine = Minecraft.getMinecraft().renderEngine;

        PlayerControllerMP controllerMP = Minecraft.getMinecraft().playerController;
        int progress = (int) (controllerMP.curBlockDamageMP * 10.0F) - 1;

        if (progress < 0)
            return;

        renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        preRenderDamagedBlocks();

        bufferIn.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        bufferIn.setTranslation(-d0, -d1, -d2);
        bufferIn.noColor();

        for (BlockPos blockPos : blocks) {
            TileEntity tile = world.getTileEntity(blockPos);

            if (!(tile != null && tile.canRenderBreaking())) {
                IBlockState state = world.getBlockState(blockPos);
                if (state.getMaterial() != Material.AIR) {
                    if(blockDamageIcons[0] == null)
                        getBlockDamageIcons();
                    Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockDamage(state, blockPos, blockDamageIcons[progress], world);
                }
            }
        }
        tessellatorIn.draw();
        bufferIn.setTranslation(0.0D, 0.0D, 0.0D);
        postRenderDamagedBlocks();
    }

    /**
     * Copy from RenderGlobal preRenderDamagedBlocks
     */
    private void preRenderDamagedBlocks() {
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
        GlStateManager.doPolygonOffset(-3.0F, -3.0F);
        GlStateManager.enablePolygonOffset();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
    }

    /**
     * Copy from RenderGlobal postRenderDamagedBlocks
     */
    private void postRenderDamagedBlocks() {
        GlStateManager.disableAlpha();
        GlStateManager.doPolygonOffset(0.0F, 0.0F);
        GlStateManager.disablePolygonOffset();
        GlStateManager.enableAlpha();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }

    public void getBlockDamageIcons() {
        TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
        for (int i = 0; i < this.blockDamageIcons.length; ++i) {
            this.blockDamageIcons[i] = texturemap.getAtlasSprite("minecraft:blocks/destroy_stage_" + i);
        }
    }
}