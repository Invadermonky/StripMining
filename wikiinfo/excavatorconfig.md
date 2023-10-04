![Excavators](https://raw.githubusercontent.com/Invadermonky/StripMining/master/icons/Excavator%20Sprites%20v2.png)
# Required Properties
## name
```json
"name": "test"
```
This is the unique identifier of the tool used to generate the tool RegistryName. This value **MUST**
be unique from other excavators.

This value does not have to be unique to other tool types as it will be modified internally. The
example will generate a RegistryName equal to `stripmining:excavator_test`.
## displayName
```json
"displayName": "Test Excavator"
```
This is the display name of the tool itself. This value does not have to be unique and supports any
UTF-8 encoded character. That means that the display name will support any letter supported by
vanilla Minecraft.
## craftingMaterial
```json
"craftingMaterial": "ingotTest"
```
```json
"craftingMaterial": "minecraft:test_ingot"
```
```json
"craftingMaterial": "minecraft:test_ingot:3"
```
This is the crafting material used to generate the tool. This setting will determine the tool repair
material a well as the crafting recipe if `generateRecipes` is enabled in`stripmining.cfg`.

Accepted values include OreDictionary strings, Items, and Items with metadata. Items with NBT data are
not supported.
## breakarea
```json
"breakarea": {
"height": 3,
"width": 3
}
```
This controls the break area of the tool. Tools have been tested and are capable of supporting break
areas up to `"height": 63` and `"width": 127` with only a small amount of lag.
### height
This controls the height of the break area. Value must be greater than or equal to 1.
### width
This controls the width of the break area. Value must be greater than or equal to 1.



### Break Pattern
#### Horizontal Mining
When mining North, South, East, or West, the tool `width` will build right then left, and the `height`
will build down one, with the remaining height above the initial block.

This break pattern means that even `width` settings will always break one additional block to the right
of the initial block.

Below is an example of the N/S/E/W break area pattern of `"width": 5` and `"height": 5`.
```
    5
    4
    3
5 3 1 2 4
    2
```
#### Vertical Mining
When mining up or down, the tool `width` will build right then left and the `height` will build forward
then backwards.

The intent of this break pattern is to allow break areas to mine the largest area possible before mining
the block under the player.

Below is an example of the UP/DOWN break area pattern of `"width": 5` and `"height": 5`.
```
    4
    2
5 3 1 2 4
    3
    5
```
## color
```json
"color": {
"red": 255,
"green": 255,
"blue": 255
}
```
This is the base color of the tool. The tool sprite texture will be generated based on this color. The
values for `red`, `green`, and `blue` correspond to RBG color values and must be between 0 and 255.

For more precise control over the tool coloration, check out the
[Template Color Customization](https://github.com/Invadermonky/StripMining/wiki/Template-Color-Customization)
section of this wiki.
## tier
```json
"tier": 2
```
This controls the appearance of the tool. StripMining excavators come with three different tool designs.
These designs are based off of a template where the coloration of the tool is generated from the base
tool color set with the  `color` property.

Accepted Values are 1, 2, or 3.
# *Optional Properties*
## harvestLevel *(default = 2)*
```json
"harvestLevel": 2
```
This controls the harvest level of the tool.

| Harvest Level |   Material   |    Equivalent     |
|:-------------:|:------------:|:-----------------:|
|       0       | Stone / Coal |   Wood Pickaxe    |
|       1       |     Iron     |   Stone Pickaxe   |
|       2       |   Diamond    |   Iron Pickaxe    |
|       3       |   Obsidian   |  Diamond Pickaxe  |
|       4       |     N/A      | (Tinker's) Ardite |

## durability *(default = 500)*
```json
"durability": 500
```
This controls the maximum durability of the tool or how many blocks it can harvest before breaking.
The standard calculation for this value is `shovelDurability x 2 = excavatorDurability`.

For example, an Iron Shovel has 250 durability, so the Iron Excavator would have `250 x 2 = 500`
durability.
## efficiency *(default = 6.0)*
```json
"efficiency": 5.4
```
This controls how fast the tool can break a block. The standard calculation for this value is
`shovelEfficiency x 0.90 = excavatorEfficiency`. This will make excavators slightly slower than shovels,
but still close enough to resemble the original material.

|      Tool      | Efficiency |
|:--------------:|:----------:|
|  Wood Shovel   |    2.0     |
|  Stone Shovel  |    4.0     |
|  Iron Shovel   |    6.0     |
|  Gold Shovel   |    12.0    |
| Diamond Shovel |    8.0     |
## damage *(default = 5.0)*
```json
"damage": 5.0
```
This controls how much damage the tool deals per swing. There is no standard calculation for this
value.

|     Weapon     | Damage |
|:--------------:|:------:|
| Wooden Shovel  |  4.0   |
|  Stone Shovel  |  5.0   |
|  Iron Shovel   |  6.0   |
| Golden Shovel  |  4.0   |
| Diamond Shovel |  7.0   |
## attackspeed *(default = -3.2)*
```json
"attackspeed": -3.1
```
This controls the how fast the tool can attack. An attack speed of -3.0 will allow the tool to
attack once every second. There is no standard calculation for this value.
## enchantability *(default = 14)*
```json
"enchantability": 14
```
This controls the enchantability of the tool, or how readily the tool accepts

| Material | Enchantability |
|:--------:|:--------------:|
|   Wood   |       15       |
|  Stone   |       5        |
|   Iron   |       14       |
|   Gold   |       22       |
| Diamond  |       10       |
## enchantable *(default = true)*
```json
"enchantable": true
```
This controls whether the tool can be enchanted, or if enchants can be applied at an anvil.
## repairable *(default = true)*
```json
"repairable": true
```
This controls whether the tool can be repaired. Setting this to `false` will disable anvil repairing.
# Example Excavator Json
```json
{
  "name": "test",
  "displayName": "Test Excavator",
  "craftingMaterial": "ingotTest",
  "breakarea": {
    "height": 3,
    "width": 3
  },
  "color": {
    "red": 255,
    "green": 255,
    "blue": 255
  },
  "tier": 2,
  "harvestLevel": 2,
  "durability": 500,
  "efficiency": 5.4,
  "damage": 5.0,
  "attackspeed": -3.1,
  "enchantability": 14,
  "enchantable": true,
  "repairable": true
}
```
