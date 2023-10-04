package com.invadermonky.stripmining.handlers;

import com.google.common.collect.ImmutableList;
import com.invadermonky.stripmining.item.IAreaBrealToolSM;
import com.invadermonky.stripmining.item.IItemToolSM;
import com.invadermonky.stripmining.util.RayTraceHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;
import java.util.List;

public class BreakHandler {

    /**
     * Attempts to harvest a block at a passed BlockPos using the passed tool.
     *
     * @param tool The tool used to harvest the block. Must be an instance of IItemToolSM.
     * @param world The world where the block exists.
     * @param pos The position of the block to harvest.
     * @param player The player attempting to harvest the block.
     * @return If true, the block can and will be harvested with appropriate XP.
     */
    public static boolean harvestBlock(ItemTool tool, World world, BlockPos pos, EntityPlayer player) {
        if(world.isAirBlock(pos)) {
            return false;
        }

        //Check to ensure the tool is a Strip Mining tool.
        if(!(tool instanceof IItemToolSM))
            return false;

        IItemToolSM toolSM = (IItemToolSM) tool;
        EntityPlayerMP playerMP = (player instanceof EntityPlayerMP) ? (EntityPlayerMP) player : null;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if(!(toolSM.getToolClasses().contains(block.getHarvestTool(state)) || tool.canHarvestBlock(state, player.getHeldItemMainhand()))) {
            return false;
        }

        if(!ForgeHooks.canHarvestBlock(block, player, world, pos)) {
            return false;
        }

        int xpToDrop = 0;
        if(playerMP != null) {
            xpToDrop = ForgeHooks.onBlockBreakEvent(world, playerMP.interactionManager.getGameType(), playerMP, pos);
            if(xpToDrop == -1) {
                return false;
            }
        }

        if(!world.isRemote) {
            if(block.removedByPlayer(state, world, pos, player, !player.capabilities.isCreativeMode)) {
                block.onPlayerDestroy(world, pos, state);
                if(!player.capabilities.isCreativeMode) {
                    block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), player.getHeldItemMainhand());
                    if(xpToDrop > 0)
                        block.dropXpOnBlockBreak(world, pos, xpToDrop);
                }
            }
            playerMP.connection.sendPacket(new SPacketBlockChange(world, pos));
        } else {
            if(block.removedByPlayer(state, world, pos, player, !player.capabilities.isCreativeMode)) {
                block.onPlayerDestroy(world, pos, state);
            }
            Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, Minecraft.getMinecraft().objectMouseOver.sideHit));
        }
        return true;
    }

    /**
     * Generates a list of all blocks to be broken in an area based tool mining height and width.
     *
     * @param stack The item being used to harvest the blocks.
     * @param height The tool break area height.
     * @param width The tool break area width.
     * @param origin The position of the origin block.
     * @param player The player attempting to harvest the blocks.
     * @return A list of the BlockPos of all blocks to be broken.
     */
    public static ImmutableList<BlockPos> getAoEBlocks(ItemStack stack, int height, int width, BlockPos origin, EntityPlayer player) {
        ArrayList<BlockPos> breakarea = new ArrayList<>();
        World world = player.world;
        Item tool = stack.getItem();
        RayTraceResult traceResult = RayTraceHelper.retrace(player);

        if(traceResult == null || traceResult.sideHit == null || !tool.canHarvestBlock(world.getBlockState(origin), stack) || player.isSneaking())
            return ImmutableList.copyOf(breakarea);

        BlockPos harvestPos;
        Vec3d look;

        int posX = origin.getX();
        int posY = origin.getY();
        int posZ = origin.getZ();

        int north, south, east, west, up, down;

        switch (traceResult.sideHit) {
            case UP:
            case DOWN:
                look = player.getLookVec();

                if(Double.compare(Math.abs(look.x), Math.abs(look.z)) >= 0) {
                    if(Double.compare(look.x, 0.0D) >= 0) {
                        north = (int) Math.ceil((double) (width - 1) / 2);;
                        south = (int) Math.floor((double) (width - 1) / 2);;
                        east = (int) Math.ceil((double) (height - 1) / 2);
                        west = (int) Math.floor((double) (height - 1) / 2);
                    } else {
                        north = (int) Math.floor((double) (width - 1) / 2);;
                        south = (int) Math.ceil((double) (width - 1) / 2);;
                        east = (int) Math.floor((double) (height - 1) / 2);;
                        west = (int) Math.ceil((double) (height - 1) / 2);;
                    }
                } else {
                    if(Double.compare(look.z, 0.0D) >= 0) {
                        north = (int) Math.ceil((double) (height - 1) / 2);;
                        south = (int) Math.floor((double) (height - 1) / 2);;
                        east = (int) Math.floor((double) (width - 1) / 2);;
                        west = (int) Math.ceil((double) (width - 1) / 2);;
                    } else {
                        north = (int) Math.floor((double) (height - 1) / 2);;
                        south = (int) Math.ceil((double) (height - 1) / 2);;
                        east = (int) Math.ceil((double) (width - 1) / 2);;
                        west = (int) Math.floor((double) (width - 1) / 2);;
                    }
                }

                for(int x = posX - west; x <= posX + east; x++) {
                    for(int z = posZ - south; z <= posZ + north; z++) {
                        if(x == posX && z == posZ)
                            continue;

                        harvestPos = new BlockPos(x, posY, z);
                        if(tool.canHarvestBlock(world.getBlockState(harvestPos), stack)) {
                            breakarea.add(harvestPos);
                        }
                    }
                }
                break;
            case NORTH:
                //Extends east > west (+x > -x)
                east = (int) Math.floor((double) (width - 1) / 2);
                west = (int) Math.ceil((double) (width - 1) / 2);
                up = height == 1 ? 0 : height - 2;
                down = height == 1 ? 0 : 1;

                for(int x = posX - west; x <= posX + east; x++) {
                    for (int y = posY - down; y <= posY + up; y++) {
                        if(x == posX && y == posY)
                            continue;

                        harvestPos = new BlockPos(x, y, posZ);
                        if(tool.canHarvestBlock(world.getBlockState(harvestPos), stack)) {
                            breakarea.add(harvestPos);
                        }
                    }
                }
                break;
            case SOUTH:
                //Extends west > east (-x > +x)
                east = (int) Math.ceil((double) (width - 1) / 2);
                west = (int) Math.floor((double) (width - 1) / 2);
                up = height == 1 ? 0 : height - 2;
                down = height == 1 ? 0 : 1;

                for(int x = posX - west; x <= posX + east; x++) {
                    for(int y = posY - down; y <= posY + up; y++) {
                        if(x == posX && y == posY)
                            continue;

                        harvestPos = new BlockPos(x, y, posZ);
                        if(tool.canHarvestBlock(world.getBlockState(harvestPos), stack)) {
                            breakarea.add(harvestPos);
                        }
                    }
                }
                break;
            case EAST:
                //Extends south > north (+z > -z)
                north = (int) Math.floor((double) (width - 1) / 2);
                south = (int) Math.ceil((double) (width - 1) / 2);
                up = height == 1 ? 0 : height - 2;
                down = height == 1 ? 0 : 1;

                for(int z = posZ - south; z <= posZ + north; z++) {
                    for (int y = posY - down; y <= posY + up; y++) {
                        if(y == posY && z == posZ)
                            continue;

                        harvestPos = new BlockPos(posX, y, z);
                        if(tool.canHarvestBlock(world.getBlockState(harvestPos), stack)) {
                            breakarea.add(harvestPos);
                        }
                    }
                }
                break;
            case WEST:
                //Extends north > south (-z > +z)
                north = (int) Math.ceil((double) (width - 1) / 2);
                south = (int) Math.floor((double) (width - 1) / 2);
                up = height == 1 ? 0 : height - 2;
                down = height == 1 ? 0 : 1;

                for(int z = posZ - south; z <= posZ + north; z++) {
                    for(int y = posY - down; y <= posY + up; y++) {
                        if(y == posY && z == posZ)
                            continue;

                        harvestPos = new BlockPos(posX, y, z);
                        if(tool.canHarvestBlock(world.getBlockState(harvestPos), stack)) {
                            breakarea.add(harvestPos);
                        }
                    }
                }
                break;
        }
        return ImmutableList.copyOf(breakarea);
    }

    /**
     * Attempts to harvest all blocks in an area defined by the tool mining area.
     * All blocks will be harvested at the same time.
     *
     * @param stack The item being used to harvest the blocks.
     * @param origin The position of the origin block.
     * @param player The player attempting to harvest the blocks.
     */
    public static void instantHarvestBlocks(ItemStack stack, BlockPos origin, EntityPlayer player) {
        World world = player.world;
        IBlockState state = world.getBlockState(origin);
        Item tool = stack.getItem();

        if(!(tool instanceof IAreaBrealToolSM) || !(tool instanceof ItemTool))
            return;

        IAreaBrealToolSM aoeToolSM = (IAreaBrealToolSM) tool;

        if(state.getBlockHardness(world, origin) == 0.0F)
            return;

        if(!tool.canHarvestBlock(state, stack) || player.isSneaking()) {
            if(!player.capabilities.isCreativeMode)
                stack.damageItem(1, player);
            return;
        }

        float refHardness = state.getPlayerRelativeBlockHardness(player, world, origin);
        float maxHardness = refHardness / 8;

        if(refHardness != 0.0F) {
            RayTraceResult traceResult = RayTraceHelper.retrace(player);

            if(traceResult == null || traceResult.sideHit == null) {
                return;
            }

            ImmutableList<BlockPos> aoeBlockPos = aoeToolSM.getAreaBreakBlocks(stack, origin, player);
            IBlockState adjState;
            float hardness;
            int count = 0;

            for(BlockPos adjPos : aoeBlockPos) {
                adjState = world.getBlockState(adjPos);
                hardness = adjState.getPlayerRelativeBlockHardness(player, world, adjPos);

                if(hardness > 0.0F && hardness >= maxHardness) {
                    if(harvestBlock((ItemTool) tool, world, adjPos, player))
                        count++;
                }
            }

            if(count >= 0 && !player.capabilities.isCreativeMode) {
                stack.damageItem(count, player);
            }
        }
    }

    /**
     * Gets a list of all blocks surrounding (including diagonally) the origin block.
     *
     * @param center The center block.
     * @return All blocks surrounding the center block.
     */
    public static List<BlockPos> surroundingBlocks(BlockPos center) {
        List<BlockPos> surrounding = new ArrayList<>();
        final int radius = 1;
        for(int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if(x == 0 && y == 0 && z == 0)
                        continue;
                    surrounding.add(center.add(x, y, z));
                }
            }
        }
        return surrounding;
    }
}
