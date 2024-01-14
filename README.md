# Minecraft Mappings

**This repository is a fork.**

I have disabled the SRG/TSRG generating codes because I don't need them.

So transitive mappings to **Mojmap/MCP/Searge** are **NOT** producible by this fork,
although they are supported by original code.

This tool creates `obf <-> spigot <-> intermediary <-> yarn` mappings for various Minecraft versions.

~~All the mappings can be translated to one another for a given version: `spigot2mcp.srg`, `srg2obf.mcp`, etc.~~
* This feature is disabled.

Supported versions:

|        | Spigot   | MCP      | Searge   | Yarn     | Intermediary | Mojang   |
|--------|----------|----------|----------|----------|--------------|----------|
| 1.20.1 | &#x2713; |          |          | &#x2713; | &#x2713;     | &#x2713; |
| 1.19.2 | &#x2713; |          |          | &#x2713; | &#x2713;     | &#x2713; |
| 1.18.2 | &#x2713; |          |          | &#x2713; | &#x2713;     | &#x2713; |
| 1.17.1 | &#x2713; |          |          | &#x2713; | &#x2713;     | &#x2713; |
| 1.16.5 | &#x2713; |          |          | &#x2713; | &#x2713;     | &#x2713; |
| 1.16.2 | &#x2713; |          |          | &#x2713; | &#x2713;     | &#x2713; |
| 1.16.1 | &#x2713; |          |          | &#x2713; | &#x2713;     | &#x2713; |
| 1.15.2 | &#x2713; | &#x2713; | &#x2713; | &#x2713; | &#x2713;     | &#x2713; |
| 1.15.1 | &#x2713; | &#x2713; | &#x2713; | &#x2713; | &#x2713;     | &#x2713; |
| 1.15   | &#x2713; | &#x2713; | &#x2713; | &#x2713; | &#x2713;     | &#x2713; |
| 1.14.4 | &#x2713; | &#x2713; | &#x2713; | &#x2713; | &#x2713;     | &#x2713; |
| 1.14.3 | &#x2713; | &#x2713; | &#x2713; | &#x2713; | &#x2713;     |          |
| 1.14.2 | &#x2713; | &#x2713; | &#x2713; | &#x2713; | &#x2713;     |          |
| 1.14.1 | &#x2713; | &#x2713; | &#x2713; | &#x2713; | &#x2713;     |          |
| 1.14   | &#x2713; | &#x2713; | &#x2713; | &#x2713; | &#x2713;     |          |
| 1.13.2 | &#x2713; | &#x2713; | &#x2713; |          | &#x2713;     |          |
| 1.13.1 | &#x2713; | &#x2713; | &#x2713; |          | &#x2713;     |          |
| 1.13   | &#x2713; | &#x2713; | &#x2713; |          | &#x2713;     |          |
| 1.12.2 | &#x2713; | &#x2713; | &#x2713; |          | &#x2713;     |          |
| 1.12   | &#x2713; | &#x2713; | &#x2713; |          | &#x2713;     |          |
| 1.11.2 | &#x2713; | &#x2713; | &#x2713; |          | &#x2713;     |          |
| 1.10.2 | &#x2713; | &#x2713; | &#x2713; |          |              |          |
| 1.9.4  | &#x2713; | &#x2713; | &#x2713; |          |              |          |
| 1.9    | &#x2713; | &#x2713; | &#x2713; |          |              |          |
| 1.8.9  |          | &#x2713; | &#x2713; |          | &#x2713;     |          |
| 1.8.8  | &#x2713; | &#x2713; | &#x2713; |          | &#x2713;     |          |
| 1.8    | &#x2713; | &#x2713; | &#x2713; |          | &#x2713;     |          |
| 1.7.10 | &#x2713; | &#x2713; | &#x2713; |          |              |          |
| 1.7.2  | &#x2713; |          |          |          |              |          |
| 1.6.4  | &#x2713; |          |          |          |              |          |
| 1.5.2  | &#x2713; |          |          |          |              |          |
| 1.4.7  | &#x2713; |          |          |          |              |          |
| 1.3.2  | &#x2713; |          |          |          |              |          |
| 1.2.5  | &#x2713; |          |          |          |              |          |

Supported formats:

- ~~SRG~~ (Disabled)
- ~~CSRG~~ (Disabled)
- ~~TSRG~~ (Disabled)
- Tiny
- ~~JSON~~ (Disabled)

These mappings were made possible by @Techcable, the MCP team, Bukkit, SpigotMC, FabricMC, Mojang, and various other people.

## TODO

- [ ] [older versions](https://github.com/agaricusb/MinecraftRemapping)
- [x] < 1.8 CraftBukkit mappings? (see /storage)
- [ ] snapshots?

## License

* All Kotlin scripts are MIT Licensed.

* The MCP mappings are the property of the MCP Team and are released under the MCP License.

* The Spigot mappings are copyright SpigotMC Pty. Ltd.

* The Yarn mappings are licensed under the Creative Commons Zero license.

* The Mojang mappings are copyright Microsoft.
