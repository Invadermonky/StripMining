package com.invadermonky.stripmining.util;

import com.invadermonky.stripmining.item.tools.ItemProspectingPick;
import com.invadermonky.stripmining.item.tools.ItemProspectingPick.ProspectedOre;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.itemstages.ConfigurationHandler;
import net.darkhax.itemstages.ItemStages;
import net.darkhax.orestages.api.OreTiersAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Loader;

public class StageHelper {
    public static final boolean GAMESTAGES_LOADED;
    public static final boolean ITEMSTAGES_LOADED;
    public static final boolean ORESTAGES_LOADED;

    public static ProspectedOre getProspectedOre(ItemProspectingPick pick, EntityPlayer player, IBlockState state, BlockPos pos) {
        ProspectedOre prospectedOre = new ProspectedOre(player.world, state, pos);
        if(ORESTAGES_LOADED) {
            prospectedOre.setDisplayName(getOreStageDisplayName(pick, player, state, pos));
        }
        if(ITEMSTAGES_LOADED) {
            prospectedOre.isUnknown = isItemUnknown(player, prospectedOre.state, prospectedOre.pos);
        }
        return prospectedOre;
    }

    public static boolean isItemUnknown(EntityPlayer player, IBlockState state, BlockPos pos) {
        String stage = ItemStages.getStage(state.getBlock().getPickBlock(state, null, player.world, pos, null));
        if(stage != null && !GameStageHelper.hasStage(player, stage)) {
            return ConfigurationHandler.changeRestrictionTooltip;
        }
        return false;
    }

    public static String getOreStageDisplayName(ItemProspectingPick pick, EntityPlayer player, IBlockState state, BlockPos pos) {
        String displayName = ItemHelper.getBlockDisplayName(player.world, state, pos);
        Tuple<String, IBlockState> check = OreTiersAPI.getStageInfo(state);
        while(check != null) {
            if(GameStageHelper.hasStage(player, check.getFirst())) {
                return displayName;
            } else {
                displayName = ItemHelper.getBlockDisplayName(player.world, check.getSecond(), pos);
                if(!pick.stats.isWhitelisted(player.world, check.getSecond(), pos)) {
                    return null;
                }
                check = OreTiersAPI.getStageInfo(check.getSecond());
            }
        }
        return displayName;
    }

    static {
        GAMESTAGES_LOADED = Loader.isModLoaded("gamestages");
        ITEMSTAGES_LOADED = Loader.isModLoaded("itemstages");
        ORESTAGES_LOADED = Loader.isModLoaded("orestages");
    }
}
