package io.jadon.mapping_generator.provider

import net.techcable.srglib.format.MappingsFormat
import net.techcable.srglib.mappings.Mappings
import java.io.File

/**
 * Pre-1.8 Spigot Mappings
 */

fun readMcDevMappings(mcver: String): Mappings {
    val baseUrl = "https://hub.spigotmc.org/stash/projects/SPIGOT/repos/builddata/browse/"
    val storageDir = File("storage")
    val mappingsFile = File(storageDir, "mcdev-" + mcver + ".csrg")

    val mapp = MappingsFormat.COMPACT_SEARGE_FORMAT.parseLines(stripBrokenLines(mappingsFile.readLines()))

    return mapp
}
