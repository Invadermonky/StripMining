package com.invadermonky.stripmining.item.stats;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

import java.awt.*;

public abstract class ToolStatsBase {

    /** The unique tool name. Used internally to generate the tool registry name. */
    public String name;

    /** The unlocalized registry name of the item. */
    public String unlocName;

    /** The raw display name byte array, formatted as UTF-8. */
    public byte[] displayNameRaw;

    /**
     * The tool crafting material. Used for repairing the tool and generating recipes.
     * Accepted values be "oreDictName", "modid:item_id", or "modid:item_id:meta".
     */
    public String craftingMaterial;

    /** The base color of the tool. Used to generate colors using the template. */
    public Color color;

    /** The tool tier. Determines the appearance of the tool. Valid values are 1, 2, or 3. */
    public int tier;

    /** The tool harvest level. Used to create the custom tool material. */
    public int harvestLevel;

    /**
     * The tool durability. Used to create the custom tool material and determines the number
     * of uses before the tool breaks.
     */
    public int durability;
    /** The effective break speed of the tool. Used to create the custom tool material. */
    public float efficiency;

    /**
     * The attack damage of the tool. Used to create the custom tool material and determines the
     * attack damage of the tool.
     */
    public float damage;

    /** The attack speed of the tool. Attack speeds of -3.6F will attack every second. */
    public float attackspeed;

    /** The enchantability of the tool. Used to create the custom tool material. */
    public int enchantability;

    /** Determines whether the tool can be enchanted. */
    public boolean isEnchantable;

    /** Determines whether the tool can be repaired. */
    public boolean isRepairable;

    //Template Color controls
    /** Used to control the dark border on the template. (RED template pixels) */
    public Color borderDark;
    /** Used to control the light border on the template. (GREEN template pixels) */
    public Color borderLight;
    /** Used to control the dark shading on the template. (BLUE template pixels) */
    public Color shadingDark;
    /** Used to control the light shading on the template. (YELLOW template pixels) */
    public Color shadingLight;
    /** Used to control the dark reflection on the template. (MAGENTA template pixels) */
    public Color reflectDark;
    /** Used to control the light reflection on the template. (CYAN template pixels) */
    public Color reflectLight;

    /**
     * Creates a custom tool material using the base tool stats.
     *
     * @return A custom tool material used for tool creation.
     */
    public Item.ToolMaterial getToolMaterial() {
        return EnumHelper.addToolMaterial(
                this.name,
                this.harvestLevel,
                this.durability,
                this.efficiency,
                this.damage,
                this.enchantability);
    }
}
