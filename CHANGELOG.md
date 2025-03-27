🗃️**Detailed Changelog: [1.5.2 --> 1.6.0](https://github.com/UltimatChamp/EnhancedTooltips/compare/1.5.2+fabric.1.21.4...1.6.0+fabric.1.21.5)**

---

### 🛠️Changes

- Updated to `1.21.5`.
- Changed the _config file structure_.
  - The options are grouped now.
  - Added detailed descriptions to each option.
- Fixed _wolf_ and _horse armor preview_ not working in <1.21.5.
- Adjusted _food effect icons_ position to align properly with the effect text and the saturation icon.
- Fixed various sizing issues with _food-related tooltips_. [**[#23]**](https://github.com/UltimatChamp/EnhancedTooltips/issues/23)
- Centered the item name with the item preview if both the **Item Badges** and **Rarity Tooltip** options are disabled. [**[#24]**](https://github.com/UltimatChamp/EnhancedTooltips/issues/24)
  - If only the **Rarity Tooltip** is disabled, the **Item Badge** will take its place.
- The durability label color has been changed to "gray" from "white", matching with vanilla. [**[#25]**](https://github.com/UltimatChamp/EnhancedTooltips/issues/25)
  - There is now about a one-line padding between the durability bar/text and the rest of the text.
- Disabled the vanilla _creative tab_ tooltip if **Item Badges** is enabled, to avoid duplicate text. [**[#26]**](https://github.com/UltimatChamp/EnhancedTooltips/issues/26)
