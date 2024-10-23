# Bacillus 1.21.x

<b>[IOU for 1x mod information]</b>

## 1.21.2 fixes

- [ ] Rewrite the shader code and mixin (mixin and render type code are commented out to stop the compiler from erroring)
- [ ] uncomment BER code after fixing said shader code
- [ ] fix save/load additional from throwing a network error (No Value Present)
- [ ] make a cleaner implementation of registering block and item ids (they're just glued onto the current entries and it's bad.)
- [ ] uncomment mods in the build.gradle files after they've updated to 1.21.2

## Todos

- [ ] Slime mold
- [ ] Mod configuration
- [ ] Localized jammer item and jammer block
- [X] Destroyer bacteria as an in-inventory trash slot
- [ ] Inventory for the bacteria blocks
    - Inventory is only kept once for a "line" of bacteria
        - all replicated bacteria point to the original block's inventory
    - <b>Replacer Inventory</b>
        - Storing the blocks to replace with
        - Storing the replaced blocks
        - Creative filter
    - <b>Destroyer Inventory</b>
        - Storing blocks that were consumed
        - Trash button for voiding consumed blocks
- [ ] Convert the everything block to an item, work on textures for said item
- [ ] Bacteria fading to gray during it's lifetime
- [ ] Make the patchouli book work
- [X] Fix lighting not updating while using destroyer bacteria

## Credits

* Textures used are from the Bacterium mod by InEqualMeasure: https://www.curseforge.com/minecraft/mc-mods/bacterium
