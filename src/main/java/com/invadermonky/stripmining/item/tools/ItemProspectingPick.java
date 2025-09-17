package com.invadermonky.stripmining.item.tools;

import com.invadermonky.stripmining.handlers.ConfigHandler;
import com.invadermonky.stripmining.init.SoundRegistrySM;
import com.invadermonky.stripmining.item.IItemToolSM;
import com.invadermonky.stripmining.item.stats.ProspectingPickStats;
import com.invadermonky.stripmining.item.stats.ToolStatsBase;
import com.invadermonky.stripmining.util.ChatHelper;
import com.invadermonky.stripmining.util.ItemHelper;
import com.invadermonky.stripmining.util.StageHelper;
import com.invadermonky.stripmining.util.StringHelper;
import gnu.trove.map.hash.THashMap;
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
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;

public class ItemProspectingPick extends ItemPickaxe implements IItemToolSM {
    protected TLinkedHashSet<String> toolClasses = new TLinkedHashSet<>();
    protected final Set<String> immutableClasses = Collections.unmodifiableSet(toolClasses);

    public final ProspectingPickStats stats;
    public final ItemStack craftingStack;

    public ItemProspectingPick(ProspectingPickStats stats) {
        super(stats.getToolMaterial());

        setRegistryName(stats.unlocName);
        setCreativeTab(CreativeTabs.TOOLS);
        setTranslationKey(StringHelper.getItemID(stats.unlocName));

        addToolClass("prospetingpick");

        setMaxDamage(stats.durability);
        attackDamage = stats.damage - 1.0F;
        attackSpeed = stats.attackspeed;
        efficiency = stats.efficiency;
        canRepair = stats.isRepairable;

        this.stats = stats;
        this.craftingStack = ItemHelper.getStackFromString(stats.craftingMaterial);
    }

    private ItemProspectingPick addToolClass(String str) {
        toolClasses.add(str);
        return this;
    }

    /*
        ItemPickaxe Override Methods
    */

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return immutableClasses;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stats.displayNameRaw == null) {
            return super.getItemStackDisplayName(stack);
        } else {
            return new String(stats.displayNameRaw, StandardCharsets.UTF_8);
        }
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
    public int getItemEnchantability() {
        return this.stats.enchantability;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(ConfigHandler.enableProspectingSound)
            world.playSound(player, pos.getX(), pos.getY(), pos.getZ(), SoundRegistrySM.PROSPECTING, SoundCategory.PLAYERS, 0.8f, 1.0f);

        player.swingArm(hand);

        if (world.isRemote)
            return EnumActionResult.PASS;

        player.getHeldItem(hand).damageItem(1, player);

        THashMap<ProspectedOre, Integer> blockCounts = new THashMap<>();

        //Facing Directions:
        //  Up: scans down in all directions
        //  Down: scans up in all directions
        //  North:  Scans East/West/South
        //  South:  Scans East/West/North
        //  East:   Scans North/South/West
        //  West:   Scans North/South/East
        int x1 = facing == EnumFacing.WEST && stats.directionalScan ? -1 : -stats.scanRadius;
        int y1 = facing == EnumFacing.DOWN && stats.directionalScan ? -1 : -stats.scanRadius;
        int z1 = facing == EnumFacing.NORTH && stats.directionalScan ? -1 : -stats.scanRadius;
        int x2 = facing == EnumFacing.EAST && stats.directionalScan ? 1 : stats.scanRadius;
        int y2 = facing == EnumFacing.UP && stats.directionalScan ? 1 : stats.scanRadius;
        int z2 = facing == EnumFacing.SOUTH && stats.directionalScan ? 1 : stats.scanRadius;

        int blockCount = 0;
        BlockPos start = pos.add(x1, y1, z1);
        BlockPos stop = pos.add(x2, y2, z2);
        for (BlockPos checkPos : BlockPos.getAllInBox(start, stop)) {

            //Skips air blocks
            if (world.isAirBlock(checkPos))
                continue;

            blockCount++;
            //Skipping block if accuracy ignores it.
            if (stats.accuracy < 1.0f && world.rand.nextFloat() > stats.accuracy)
                continue;

            IBlockState checkState = world.getBlockState(checkPos);
            if (stats.isWhitelisted(world, checkState, checkPos)) {
                ProspectedOre prospectedOre = getProspectedOre(player, checkState, checkPos);
                if (!blockCounts.containsKey(prospectedOre)) {
                    blockCounts.put(prospectedOre, 0);
                }
                blockCounts.put(prospectedOre, blockCounts.get(prospectedOre) + 1);
            }
        }

        List<ITextComponent> messages = new ArrayList<>();
        for (Map.Entry<ProspectedOre, Integer> entry : blockCounts.entrySet()) {
            String blockName = entry.getKey().getDisplayName();

            if (blockName == null)
                continue;

            int count = entry.getValue();
            String m = "chat.stripmining:";
            if (count <= ConfigHandler.veinTrace) {
                m += "traces";
            } else if (count <= ConfigHandler.veinSmall) {
                m += "small_sample";
            } else if (count <= ConfigHandler.veinMedium) {
                m += "medium_sample";
            } else if (count <= ConfigHandler.veinLarge) {
                m += "large_sample";
            } else {
                m += "motherload";
            }
            messages.add(new TextComponentTranslation("  ").appendSibling(new TextComponentTranslation(m, blockName)));
        }
        messages.add(0, (messages.isEmpty() ? new TextComponentTranslation("chat.stripmining:found_nothing") : new TextComponentTranslation("chat.stripmining:found")));

        ChatHelper.sendNoSpam(player, messages.toArray(new ITextComponent[0]));
        return EnumActionResult.PASS;
    }

    public ProspectedOre getProspectedOre(EntityPlayer player, IBlockState state, BlockPos pos) {
        if(StageHelper.GAMESTAGES_LOADED) {
            return StageHelper.getProspectedOre(this, player, state, pos);
        } else {
            return new ProspectedOre(player.world, state, pos);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltip.stripmining:scanradius", this.stats.scanRadius));
        if(GuiScreen.isShiftKeyDown()) {
            tooltip.add("  " + I18n.format("tooltip.stripmining:accuracy", (int) (this.stats.accuracy * 100)));
            if(this.stats.directionalScan)
                tooltip.add("  " + I18n.format("tooltip.stripmining:scan_directional"));
            else
                tooltip.add("  " + I18n.format("tooltip.stripmining:scan_full"));
        }
    }


    /*
        IItemToolSM Override Methods
    */

    @Override
    public TLinkedHashSet<String> getToolClasses() {
        return toolClasses;
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
    public ToolStatsBase getToolBaseStats() {
        return this.stats;
    }

    @Override
    public Color getToolBaseColor() {
        return this.stats.color;
    }

    @Override
    public float getEfficiency() {
        return this.efficiency;
    }

    @Override
    public THashSet<Block> getEffectiveBlocks() {
        return new THashSet<>(0);
    }

    @Override
    public THashSet<Material> getEffectiveMaterials() {
        return this.stats.effectiveMaterials;
    }

    public static class ProspectedOre {
        public final IBlockState state;
        public final BlockPos pos;
        public boolean isUnknown;
        private String displayName;

        public ProspectedOre(World world, IBlockState state, BlockPos pos) {
            this.state = state;
            this.pos = pos;
            this.isUnknown = false;
            this.displayName = ItemHelper.getBlockDisplayName(world, state, pos);
        }

        public String getDisplayName() {
            return isUnknown ? new TextComponentTranslation("chat.stripmining:staged_ore").getUnformattedComponentText() : this.displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProspectedOre that = (ProspectedOre) o;
            return Objects.equals(displayName, that.displayName);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(displayName);
        }
    }
}
