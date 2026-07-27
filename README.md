English|[简体中文](./README_CN.md)
# Chang'e Client
It's a Minecraft client in alpha stage.
It changes some features in the client and makes it better.
Thanks for your star!

Lastest release: *none*

Lastest sanpshot: `alpha2`

Support version: 26.2

Only the version for the lastest Minecraft release would keep being supported and devloped.
And those for the earlier releases would not receive updates besides safety updates. 

Index:
+ [Features](./README.md#features)
+ [Commands](./README.md#commands)
+ [GUI](./README.md#gui)

Links: 
+ [Bilibili](https://space.bilibili.com/627871340)

## Features
### Boat Fly `boat_fly`
Type: Boolean

Normal: OFF(false)

When this feature is ON(true), you can fly when you're rowing a boat.
You can change `boat_speed` and `boat_vertical_speed` to make the boat faster or slower.

Usage: Sit on a boat. Press Jump Key to rise and Descline Key(Down Key is normal. You can change it in the Key Bind Settings). Press WASD to control.

### Boat Speed `boat_speed`
Type: Float

Normal: 14.0

Unit: m/s(0~1024)

It affects the speed of the boat.

### Boat Vertical Speed `boat_vertical_speed`
Type: Float

Normal: 10.4

Unit: m/s(0~45)

It affects the vertical speed of the boat
(the rising spped when the JumpKey is Down and the descline speed when the descline key is DOWN).

### Air Fly `air_fly`
Type: Boolean

Normal: OFF(false)

When this feature is ON(true), you can fly as in the creative mode.

### Disable Pumpkin Blur `disable_pumpkin_blur`
Type: Boolean

Normal: OFF(false)

Disables the pumpkin blur when it's ON.

## Commands
Main command: `\change`

Usage: `\change <id> [value]`
+ `<id>`: **Required**. The id of features.
+ `[value]`: **Optional**. If this argument is empty, the command will send the value of the feature config. Or it will set the config to the value you input.

Extra commands:
+ `/getservertps`: Get the tps of the server.

## GUI
Press `Backspace` to open the GUI(Changeable in the settings).
Configure feature settings in the GUI.
