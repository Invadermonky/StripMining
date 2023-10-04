package com.invadermonky.stripmining.item.tools;

import com.google.common.collect.ImmutableList;
import com.invadermonky.stripmining.handlers.BreakHandler;
import com.invadermonky.stripmining.item.IAreaBrealToolSM;
import com.invadermonky.stripmining.item.IItemToolSM;
import com.invadermonky.stripmining.item.stats.HammerStats;
import com.invadermonky.stripmining.item.stats.ToolStatsBase;
import com.invadermonky.stripmining.util.ItemHelper;
import com.invadermonky.stripmining.util.StringHelper;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TLinkedHashSet;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ItemHammer extends ItemPickaxe implements IItemToolSM, IAreaBrealToolSM {
    protected final TLinkedHashSet<String> toolClasses = new TLinkedHashSet<>();
    protected final Set<String> immutableClasses = Collections.unmodifiableSet(toolClasses);

    protected HammerStats stats;
    protected ItemStack craftingStack;

    protected final int height;
    protected final int width;

    public ItemHammer(HammerStats stats) {
        super(stats.getToolMaterial());

        setRegistryName(stats.unlocName);
        setCreativeTab(CreativeTabs.TOOLS);
        setTranslationKey(StringHelper.getItemID(stats.unlocName));

        addToolClass("pickaxe");
        addToolClass("hammer");

        setMaxDamage(stats.durability);
        attackDamage = stats.damage - 1.0F;
        attackSpeed = stats.attackspeed;
        efficiency = stats.efficiency;
        canRepair = stats.isRepairable;

        this.stats = stats;
        this.craftingStack = ItemHelper.getStackFromString(stats.craftingMaterial);
        this.height = stats.breakareaHeight;
        this.width = stats.breakareaWidth;
    }

    private ItemHammer addToolClass(String str) {
        toolClasses.add(str);
        return this;
    }

    /*
        ItemPickaxe override methods
    */

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if(stats.displayNameRaw == null)
            return super.getItemStackDisplayName(stack);
        else
            return new String(stats.displayNameRaw, StandardCharsets.UTF_8);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        return this.stats.harvestLevel >= state.getBlock().getHarvestLevel(state) && getDestroySpeed(stack, state) > 1.0F;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return isRepairable() && (ItemHelper.isOreNameEqual(repair, getCraftingMaterial()) || ItemHelper.isRepairItem(this, repair));
    }

    @Override
    public boolean isRepairable() {
        return this.stats.isRepairable;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return this.stats.isEnchantable;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn) {
        if(GuiScreen.isShiftKeyDown()) {
            tooltip.add(I18n.format("tooltip.stripmining:breakarea_hammer", ""));
            tooltip.add(" " + I18n.format("tooltip.stripmining:breakarea_height", this.stats.breakareaHeight));
            tooltip.add(" " + I18n.format("tooltip.stripmining:breakarea_width", this.stats.breakareaWidth));
        } else {
            tooltip.add(I18n.format("tooltip.stripmining:breakarea_hammer", I18n.format("tooltip.stripmining:breakarea", this.height, this.width)));
        }
    }

    /*
        IItemToolSM Override Methods
    */

    @Override
    public TLinkedHashSet<String> getToolClasses() {
        return this.toolClasses;
    }

    @Override
    public String getCraftingMaterial() {
        return this.stats.craftingMaterial;
    }

    @Override
    public ItemStack getCraftingStack() {
        return this.craftingStack;
    }

    @Override
    public ToolStatsBase getToolBaseStats() { return this.stats; }

    @Override
    public Color getToolBaseColor() {
        return this.stats.color;
    }

    @Override
    public float getEfficiency() {
        return efficiency;
    }

    @Override
    public THashSet<Block> getEffectiveBlocks() {
        return this.stats.effectiveBlocks;
    }

    @Override
    public THashSet<Material> getEffectiveMaterials() {
        return this.stats.effectiveMaterials;
    }

    /*
        Multi-block break methods
    */

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        BreakHandler.instantHarvestBlocks(stack, pos, player);
        return false;
    }

    @Override
    public ImmutableList<BlockPos> getAreaBreakBlocks(ItemStack stack, BlockPos origin, EntityPlayer player) {
        return BreakHandler.getAoEBlocks(stack, height, width, origin, player);
    }
}
