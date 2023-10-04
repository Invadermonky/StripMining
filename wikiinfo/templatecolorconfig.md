# Template Colors
![Template Sprites](https://raw.githubusercontent.com/Invadermonky/StripMining/master/icons/template_sprites.png)

StripMining features a template system used to generate tool sprites using a base color and a tool tier. The template works by taking the base color and darkening or lightening the color by a percentage, then substituting the new colors into the template using a color map. The following is a table listing the color adjustments and mapping.

|   Property   |  Color  |   RGB Value   |  Type   | Adjust % | Description                        |
|:------------:|:-------:|:-------------:|:-------:|:--------:|:-----------------------------------|
|  borderDark  |   RED   |   255, 0, 0   | Darken  |   -20%   | Used for dark borders              | 
| borderLight  |  GREEN  |   0, 255, 0   | Darken  |   -40%   | Used for light borders             |
| shadingDark  |  BLUE   |   0, 0, 255   | Darken  |   -60%   | Used for dark shading textures     |
| shadingLIght | YELLOW  |  255, 255, 0  | Darken  |   -80%   | Used for light shading textures    |
| reflectDark  | MAGENTA |  255, 0, 255  | Lighten |  ~+40%   | Used for dark reflection textures  |
| reflectLight |  CYAN   |  0, 255, 255  | Lighten |  ~+60%   | Used for light reflection textures |
|     N/A      |  WHITE  | 255, 255, 255 | Default |   N/A    | Equivalent to base color           |

While the template generation is effective enough in most cases, occasionally a color can appear too bright or too dark. In these cases, StripMining allows precise control over all template colors.
```json
  "templateColors": {
    "borderDark" : {
      "red": 255,
      "green": 255,
      "blue": 255
    },
    "borderLight" : {
      "red": 255,
      "green": 255,
      "blue": 255
    },
    "shadingDark" : {
      "red": 255,
      "green": 255,
      "blue": 255
    },
    "shadingLight" : {
      "red": 255,
      "green": 255,
      "blue": 255
    },
    "reflectDark" : {
      "red": 255,
      "green": 255,
      "blue": 255
    },
    "reflectLight" : {
      "red": 255,
      "green": 255,
      "blue": 255
    }
  }
```
# Getting Started
The first step to begin modifying a sprite's color is to enable `B:robustLogging=true` in `stripmining.cfg` and add `"templateColors": {}` to the bottom of the specific tool Json.

This will print a large amount of information to the `latest.log` found in the `logs` folder. You can search for the name of the json file you are attempting to modify, and the logger will read something like, `Parsing File: hammer_test.json`.

With the addition of the `"templateColors": {}` property, the logger will print all currently used template color values. Once you have the color you wish to modify, simply add it to the `"templateColors": {}` object as shown above.

## borderDark
Template Color: RED {255, 0, 0}\
This is the darker border color. Used along the back face of tools.

## borderLight
Template Color: GREEN {0, 255, 0}\
This is the lighter border color. Used along the front face of tools.

## shadingDark
Template Color: BLUE {0, 0, 255}\
This is darker shading color. Used along the back and bottom of tools to provide depth.

## shadingLight
Template Color: YELLOW {255, 255, 0}\
This is the lighter shading color. Used along the top of tools and along the border of any `shadingDark`.

## reflectDark
Template Color: MAGENTA {255, 0, 255}\
This is a darker brightened color. Used along the front face of tools to give it the appearance of reflecting light.

## reflectLight
Template Color: CYAN {0, 255, 255}\
This is a very bright color. Used along the front edge of tools to give them the impression of sharpness.

\
<ins>**Note:**</ins> All template color properties are optional. If you only want to change one or two colors, you can simply exclude the unchanged properties.
```json
  "templateColors": {
    "borderLight" : {
      "red": 230,
      "green": 225,
      "blue": 0
    },
    "reflectLight" : {
      "red": 0,
      "green": 0,
      "blue": 120
    }
  }
```
# Completed Example Script
```json
{
  "name": "test",
  "displayName": "Test Hammer",
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
  "damage": 8.0,
  "attackspeed": -3.3,
  "enchantability": 14,
  "enchantable": true,
  "repairable": true,
  "templateColors": {
    "borderDark" : {
      "red": 255,
      "green": 255,
      "blue": 255
    },
    "borderLight" : {
      "red": 255,
      "green": 255,
      "blue": 255
    },
    "shadingDark" : {
      "red": 255,
      "green": 255,
      "blue": 255
    },
    "shadingLight" : {
      "red": 255,
      "green": 255,
      "blue": 255
    },
    "reflectDark" : {
      "red": 255,
      "green": 255,
      "blue": 255
    },
    "reflectLight" : {
      "red": 255,
      "green": 255,
      "blue": 255
    }
  }
}
```
