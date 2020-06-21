package provider

import com.google.common.collect.ImmutableBiMap
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.opencsv.CSVReader
import net.techcable.srglib.FieldData
import net.techcable.srglib.JavaType
import net.techcable.srglib.MethodData
import net.techcable.srglib.format.MappingsFormat
import net.techcable.srglib.mappings.ImmutableMappings
import net.techcable.srglib.mappings.Mappings
import java.io.File
import java.io.Reader
import java.net.URL
import java.util.*

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
