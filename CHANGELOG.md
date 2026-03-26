🗃️**Detailed Changelog: [1.8.0 --> 1.9.0](https://github.com/UltimatChamp/EnhancedTooltips/compare/1.8.0+fabric.1.21.11...1.9.0+fabric.26.1)**

---

- [**YetAnotherConfigLib**](https://modrinth.com/mod/yacl) is now a required dependency.

---

### 💡Added

- Support for **Minecraft** `26.1`.
- Added **Nautilus Armor Preview**.
- Added an option to render the **Player model** _(default)_ besides Armor Stand to **Armor (Trim) Preview**. [**[#40]**](https://github.com/UltimatChamp/EnhancedTooltips/issues/40)
- Added an option to **hide** _Item Names_ in the **Held Item Tooltip**. [**[#44]**](https://github.com/UltimatChamp/EnhancedTooltips/issues/44)
- Resource-pack makers, rejoice!<br />
  Added **resource-pack support** to change the tooltip background and frames. [**[#54]**](https://github.com/UltimatChamp/EnhancedTooltips/issues/54)
  - Same as the vanilla format.
  - These are the paths:
    - **Background**: `assets/minecraft/textures/gui/sprites/background.png`
    - **Frame**: `assets/minecraft/textures/gui/sprites/frame.png` - Common 
      - `assets/enhancedtooltips/textures/gui/sprites/frame/uncommon.png` - Uncommon
      - `assets/enhancedtooltips/textures/gui/sprites/frame/rare.png` - Rare
      - `assets/enhancedtooltips/textures/gui/sprites/frame/epic.png` - Epic
- Added an option to **remove all spacing** from tooltips. [**[#67]**](https://github.com/UltimatChamp/EnhancedTooltips/issues/67)

---

### 🛠️Changes

- **Potion effects** in tooltips are now colored, just like food effects.
- **Badges** now display **mod names** for modded items if they do not belong to a custom item group.
  - _(Previously, it was only doing so if they were not registered in any item group at all.)_
- Fixed incompatibility with **Fresh Animations** and other such CEM resource-packs, by reverting to the vanilla model. [**[#2]**](https://github.com/UltimatChamp/EnhancedTooltips/issues/2)
- Fixed rendering of some modded armors. [**[#47]**](https://github.com/UltimatChamp/EnhancedTooltips/issues/47)
- Fixed **Armor Trim Preview** for _armor trims_ added by some mods as they were not being correctly handled. [**[#58]**](https://github.com/UltimatChamp/EnhancedTooltips/issues/58)
- Fixed compatibility with **Sophisticated Backpacks/Storage**. [**[#64]**](https://github.com/UltimatChamp/EnhancedTooltips/issues/64)
- Fixed **Food Effect Tooltip**s not showing the effect levels. [**[#74]**](https://github.com/UltimatChamp/EnhancedTooltips/issues/74)

---

### 📘Translation Changes

- Added **Simplified Chinese** translation. [**[#71]**](https://github.com/UltimatChamp/EnhancedTooltips/pull/71)
