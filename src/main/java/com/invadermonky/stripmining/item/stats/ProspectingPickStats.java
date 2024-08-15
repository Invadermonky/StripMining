package com.invadermonky.stripmining.item.stats;

import com.google.common.collect.Sets;
import com.invadermonky.stripmining.handlers.ConfigHandler;
import com.invadermonky.stripmining.util.LogHelper;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ProspectingPickStats extends ToolStatsBase {
    public int scanRadius;
    public boolean directionalScan;
    public float accuracy;
    private String[] rawBlacklist;
    private String[] rawWhitelist;
    public THashSet<String> blacklistedBlocks = new THashSet<>();
    public THashSet<String> whitelistedBlocks = new THashSet<>();
    public TIntHashSet blacklistedOres = new TIntHashSet();
    public TIntHashSet whitelistedOres = new TIntHashSet();

    private boolean areListsInitialized = false;

    public THashSet<Material> effectiveMaterials = new THashSet<>(Sets.newHashSet(
            Material.GROUND,
            Material.ROCK
    ));

    /**
     * Sets the raw ore/block blacklist from the tool json.
     */
    public void setRawBlacklist(String... rawBlacklist) {
        this.rawBlacklist = rawBlacklist;
    }

    /**
     * Sets the raw ore/block whitelist from the tool json.
     */
    public void setRawWhitelist(String... rawWhitelist) {
        this.rawWhitelist = rawWhitelist;
    }

    public boolean isWhitelisted(World world, IBlockState state, BlockPos pos) {
        if(!areListsInitialized) {
            initLists();
        }

        if(state.getMaterial() instanceof MaterialLiquid)
            return false;

        String blockName = state.getBlock().getRegistryName().toString();
        int meta = state.getBlock().getMetaFromState(state);

        if(this.whitelistedBlocks.contains(blockName) || this.whitelistedBlocks.contains(blockName + ":" + meta)) {
            return true;
        } else if(this.blacklistedBlocks.contains(blockName) || this.blacklistedBlocks.contains(blockName + ":" + meta)) {
            return false;
        } else {
            ItemStack stack = state.getBlock().getPickBlock(state, null, world, pos, null);
            for(int oreId : OreDictionary.getOreIDs(stack)) {
                if(this.whitelistedOres.contains(oreId)) {
                    return true;
                } else if(this.blacklistedOres.contains(oreId)) {
                    return false;
                }
            }
        }
        return false;
    }

    private void initLists() {
        //Global config
        addToWhitelist(ConfigHandler.oreWhitelist);
        addToBlacklist(ConfigHandler.oreBlacklist);
        //Tool specific config
        addToBlacklist(this.rawBlacklist);
        addToWhitelist(this.rawWhitelist);

        this.areListsInitialized = true;
    }

    protected void addToBlacklist(String... rawStrings) {
        populateSets(this.blacklistedBlocks, this.blacklistedOres, rawStrings);
        whitelistedBlocks.removeAll(blacklistedBlocks);
        whitelistedOres.removeAll(blacklistedOres);
    }

    protected void addToWhitelist(String... rawStrings) {
        populateSets(this.whitelistedBlocks, this.whitelistedOres, rawStrings);
    }

    protected static void populateSets(THashSet<String> blockSet, TIntHashSet oreSet, String... rawStrings) {
        final String BLOCK = "^block=";
        final String ORE = "^ore=";
        final String TYPE = "^type=";

        for(String s : rawStrings) {
            if(s.matches(TYPE + ".+")) {
                for(String oreName : OreDictionary.getOreNames()) {
                    if(oreName.contains(s.replaceFirst(TYPE, ""))) {
                        oreSet.add(OreDictionary.getOreID(oreName));
                    }
                }
            } else if(s.matches(ORE + ".+")) {
                oreSet.add(OreDictionary.getOreID(s.replaceFirst(ORE, "")));
            } else if(s.matches(BLOCK + ".+")) {
                blockSet.add(s.replaceFirst(BLOCK, ""));
            } else {
                LogHelper.error("Could not match syntax for Prospecting Pick value: " + s);
            }
        }
    }
}
