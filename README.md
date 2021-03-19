<div align="center">

# Astatine

Astatine is a mod for Minecraft which fixes common bugs in the game

*Astatine uses the [Fabric Mod Loader](https://fabricmc.net), Forge will not be supported.*

</div>

### About Astatine

The Minecraft JIRA page has a lot of bugs that have been reported, some of which have been open for years with an easy
fix!

Astatine aims to fix those bugs in the most efficient ways, without compromising the core game mechanics.

### Features
- Switch between audio devices inside of Minecraft (based on my 1.8.9 mod, [AudioSwitcher](https://github.com/dreamhopping/AudioSwitcher))

### Fixed bugs

- ``MC-132820``: Spawner isn't in the creative inventory
- ``MC-2474``: Transparent blocks (ie: snow) placed between bookshelves and enchantment tables negate bonuses received
  from bookshelves.
- ``MC-148998``: Items (specifically BlockItems) are offset in wandering trader's trading menu
- ``MC-127995``: You can use bone meal on sea pickles in situations where no sea pickles will grow
- ``MC-115092``: Squid named "Dinnerbone" or "Grumm" is not
  upside-down [(#3)](https://github.com/dreamhopping/Astatine/issues/3)
- ``MC-117977``: Advancement GUI doesn't have a close button
- ``MC-75721``: Arrow buttons in book UI are rendered in front of tooltips
- ``MC-131562``: Pressing "Done" in an empty command block minecart returns "Command set:"
- ``MC-72687``: Action Bar Messages do not have the small shadow under the text
- ``MC-200000``: Merchant trade select packet (C2S) does not check for negative indices
- ``MC-72494``: In Statistics screen 'm' is the same unit for both minutes and meters
- ``MC-142555``: Cats and Ocelots wont eat tropical fish
- ``MC-117800``: Half bed can be placed outside the world's border
- ``MC-153010``: "doMobLoot" gamerule doesn't prevent foxes from dropping their items
- ``MC-201723``: Statistics sprites don't look pressed when clicked
- ``MC-169386``: High numbers in the "Statistics" screen are overlapping with other columns

### Bugs to be fixed

- ``MC-149058``: Using a 1-9 hotkey to move items into the anvil appends the number to the new item name
- ``MC-219537``: Long villager names cause an overlay

### Credits

- [LlamaLad7](https://github.com/LlamaLad7): Assisting with the improvement of the fixes for ``MC-2474``
  and ``MC-72687``
- [UserTeemu](https://github.com/UserTeemu): Providing the fix for ``MC-72494``
- [DJtheRedstoner](https://github.com/DJtheRedstoner): Providing the fix for ``MC-142555``, ``MC-153010`` and others
