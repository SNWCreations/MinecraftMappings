package io.jadon.mapping_generator.enums

import cn.hutool.core.util.ZipUtil
import com.google.common.base.Preconditions
import io.jadon.mapping_generator.SpigotMappingType
import com.google.common.collect.ImmutableList
import com.google.common.io.Files
import net.techcable.srglib.mappings.Mappings
import io.jadon.mapping_generator.provider.*
import java.io.File
import io.jadon.mapping_generator.SpigotMappingType.*
import net.fabricmc.mappingio.MappingReader
import net.fabricmc.mappingio.MappingWriter
import net.fabricmc.mappingio.format.MappingFormat
import net.fabricmc.mappingio.tree.MemoryMappingTree
import java.util.zip.GZIPInputStream

enum class MinecraftVersion(
    val mcVersion: String,
    val mcpVersion: String? = null,
    val mcpConfig: Boolean = false,
    val spigot: SpigotMappingType = SpigotMappingType.NO_SPIGOT,
    val yarn: Boolean = false,
    val mojang: Boolean = false,
    val legacyIntermediary: Boolean = false
) {
    V1_20_1("1.20.1", null, true, MODERN_SPIGOT, true, true, false),
	V1_19_4("1.19.4", null, true, MODERN_SPIGOT, true, true, false),
	V1_19_2("1.19.2", null, true, MODERN_SPIGOT, true, true, false),
	V1_18_2("1.18.2", null, true, MODERN_SPIGOT, true, true, false),
	V1_17_1("1.17.1", null, true, SPIGOT, true, true, false),
	V1_16_5("1.16.5", null, true, SPIGOT, true, true, false),
	V1_16_2("1.16.2", null, true, SPIGOT, true, true, false),
	V1_16_1("1.16.1", null, true, SPIGOT, true, true, false),
    V1_15_2("1.15.2", "snapshot_20200515", true, SPIGOT, true, true, false),
    V1_15_1("1.15.1", "snapshot_20191217", true, SPIGOT, true, true, false),
    V1_15("1.15", "snapshot_nodoc_20191212", true, SPIGOT, true, true, false),
    V1_14_4("1.14.4", "snapshot_nodoc_20190729", true, SPIGOT, true, true, false),
    V1_14_3("1.14.3", "snapshot_nodoc_20190729", true, SPIGOT, true, false, false),
    V1_14_2("1.14.2", "stable_nodoc_53", true, SPIGOT, true, false, false),
    V1_14_1("1.14.1", "stable_nodoc_51", true, SPIGOT, true, false, false),
    V1_14("1.14", "stable_nodoc_49", true, SPIGOT, true, false, false),
    V1_13_2("1.13.2", "stable_nodoc_47", true, SPIGOT, false, false, true),
    V1_13_1("1.13.1", "stable_nodoc_45", true, SPIGOT, false, false, true),
    V1_13("1.13", "stable_nodoc_43", true, SPIGOT, false, false, true),
    V1_12_2("1.12.2", "snapshot_nodoc_20180129", false, SPIGOT, false, false, true),
    V1_12("1.12", "snapshot_nodoc_20180814", false, SPIGOT, false, false, true),
    V1_11_2("1.11.2", "snapshot_nodoc_20170612", false, SPIGOT, false, false, true),
    V1_10_2("1.10.2", "snapshot_nodoc_20160703", false, SPIGOT, false, false, false),
    V1_9_4("1.9.4", "snapshot_nodoc_20160604", false, SPIGOT, false, false, false),
    V1_9("1.9", "snapshot_nodoc_20160516", false, SPIGOT, false, false, false),
    V1_8_9("1.8.9", "snapshot_nodoc_20160301", false, SPIGOT, false, false, true),
    V1_8_8("1.8.8", "snapshot_nodoc_20151216", false, SPIGOT, false, false, true),
    V1_8("1.8", "snapshot_nodoc_20141130", false, SPIGOT, false, false, true),
    V1_7_10("1.7.10", "snapshot_nodoc_20140925", false, LEGACY_MCDEV, false, false, false),
    V1_7_2("1.7.2", null, false, LEGACY_MCDEV, false, false, false),
    V1_6_4("1.6.4", null, false, LEGACY_MCDEV, false, false, false),
    V1_5_2("1.5.2", null, false, LEGACY_MCDEV, false, false, false),
    V1_4_7("1.4.7", null, false, LEGACY_MCDEV, false, false, false),
    V1_3_2("1.3.2", null, false, LEGACY_MCDEV, false, false, false),
    V1_2_5("1.2.5", null, false, LEGACY_MCDEV, false, false, false),
	;

    fun generateMappings(
        outputDir: File,
        spigotClsIn: File,
        spigotMemberIn: File?,
        mojIn: File?,
        yarnIn: File
    ): List<Pair<String, Mappings>> {
        // Mappings, fromObf
        val mappings = mutableListOf<Pair<Mappings, String>>()

        if (mcpVersion != null) {
            val obf2srgMappings = if (mcpConfig) {
                getMCPConfigMappings(mcVersion)
            } else {
                downloadSrgMappings(mcVersion)
            }
            val srg2mcpMappings = downloadMcpMappings(obf2srgMappings, mcpVersion)
            val obf2mcp = Mappings.chain(ImmutableList.of(obf2srgMappings, srg2mcpMappings))
            mappings.add(Pair(obf2srgMappings, "srg"))
            mappings.add(Pair(obf2mcp, "mcp"))
        }
        if (spigot == SPIGOT || spigot == MODERN_SPIGOT) {
//            val buildDataCommit = getBuildDataCommit(outputDir, mcVersion)
//            val obf2spigotMappings = downloadSpigotMappings(mcVersion, outputDir, buildDataCommit, spigot == MODERN_SPIGOT)
            val obf2spigotMappings = loadSpigotMap(mcVersion, spigotClsIn, spigotMemberIn, mojIn, outputDir)
            mappings.add(Pair(obf2spigotMappings, "spigot"))
        } else if (spigot == LEGACY_MCDEV) {
			val obf2mcdevMappings = readMcDevMappings(mcVersion)
			mappings.add(Pair(obf2mcdevMappings, "spigot"))
		}
        if (yarn) {
            val format = MappingReader.detectFormat(yarnIn.toPath())
            Preconditions.checkArgument(
                format == MappingFormat.TINY_FILE,
                "Provided yarn mappings must use TINY v1 format, but we got $format"
            )
            val obf2yarnMappingsSet = loadYarnMap(yarnIn)
//            val obf2yarnMappingsSet = getYarnMappings(outputDir, mcVersion)
            obf2yarnMappingsSet.forEach { id, m -> mappings.add(Pair(m, id)) }
        }
		if (legacyIntermediary) {
            val obf2legacyIntermediaryMappingsSet = getLegacyIntermediaryMappings(mcVersion)
            obf2legacyIntermediaryMappingsSet.forEach { id, m -> mappings.add(Pair(m, id)) }
        }
//        if (mojang) {
//            val obf2mojangMappingSet = MojangMappings.getMappings(mcVersion)
//            mappings.add(Pair(obf2mojangMappingSet, "mojang"))
//        }

        val completeMappings = mutableListOf<Pair<String, Mappings>>()
        for (a in mappings) {
            val obf2aMappings = a.first
//            val a2obfMappings = obf2aMappings.inverted()

            completeMappings.add(Pair("obf2${a.second}", obf2aMappings))
//            completeMappings.add(Pair("${a.second}2obf", a2obfMappings))
//            for (b in mappings) {
//                if (a != b) {
//					try {
//						// some code
//						val a2bMappings = Mappings.chain(a2obfMappings, b.first)
//						completeMappings.add(Pair("${a.second}2${b.second}", a2bMappings))
//					} catch (e: IllegalArgumentException) {
//						// handler
//						println("Failed: ${a.second}2${b.second}")
//					}
//                }
//            }
        }
        return completeMappings
    }

    fun write(yarnFile: File, spigotClassIn: File, spigotMemberIn: File?, mojIn: File, mappingsFolder: File): File {
        val outputFolder = File(mappingsFolder, mcVersion)
        outputFolder.mkdirs()
        val finalYarnFile: File
        if (yarnFile.extension == "gz") {
            println("Extracting yarn mapping from the GZIP archive provided by user")
            finalYarnFile = File(outputFolder, "yarn.tiny")
            Files.write(ZipUtil.unGzip(GZIPInputStream(yarnFile.inputStream())), finalYarnFile)
        } else {
            finalYarnFile = yarnFile
        }
//        fun Mappings.writeTo(fileName: String) {
//            println("$mcVersion: writing mappings to $fileName.srg")
//            val strippedMappings = stripDuplicates(this)
//            val srgLines = MappingsFormat.SEARGE_FORMAT.toLines(strippedMappings)
//            srgLines.sort()
//            val file = File(outputFolder, "$fileName.srg")
//			file.createNewFile()
//            file.bufferedWriter().use {
//                for (line in srgLines) {
//                    it.write(line)
//                    it.write("\n")
//                }
//            }
//
//            println("$mcVersion: writing mappings to $fileName.csrg")
//            val csrgLines = MappingsFormat.COMPACT_SEARGE_FORMAT.toLines(strippedMappings)
//            csrgLines.sort()
//            File(outputFolder, "$fileName.csrg").bufferedWriter().use {
//                for (line in csrgLines) {
//                    it.write(line)
//                    it.write("\n")
//                }
//            }
//
//            println("$mcVersion: writing mappings to $fileName.tsrg")
//            TSrgUtil.fromSrg(file, File(outputFolder, "$fileName.tsrg"))
//        }

        // srg & tsrg
        val generatedMappings = generateMappings(outputFolder, spigotClassIn, spigotMemberIn, mojIn, finalYarnFile)
//        generatedMappings.forEach { pair ->
//            val fileName = pair.first
//            val mappings = pair.second
//            mappings.writeTo(fileName)
//        }

        // tiny
        println("$mcVersion: merging mapping")
        val tinyMappings = io.jadon.mapping_generator.tiny.Mappings()
        generatedMappings.filter { it.first.startsWith("obf2") }.forEach { pair ->
            val name = pair.first.split("2")[1]

            tinyMappings.addMappings(name, pair.second)
        }
        val tinyFile = File(outputFolder, "$mcVersion.tiny")
        val clientSpecific = listOf(
            "com/mojang/blaze3d/",
            "net/minecraft/client/"
        )
        val notClientSide = mutableListOf<String>()
        tinyFile.bufferedWriter().use {
            for (line in tinyMappings.toStrings()) {
                var clientSide = false
                if (clientSpecific.any { n -> line.contains(n) }) {
                    val split = line.split("\t")
                    when (split[0]) {
                        "CLASS" -> {
                            clientSide = split[1] == split[2]
                            if (!clientSide) {
                                notClientSide.add(split[1])
                            }
                        }

                        "FIELD", "METHOD" -> {
                            clientSide = !notClientSide.contains(split[1])
                        }
                    }
                }
                if (clientSide) {
                    println("$mcVersion: ignoring client-side content: $line")
                    continue
                }
                it.write(line)
                it.write("\n")
            }
        }

        // fix field descriptor
        println("$mcVersion: fixing field descriptors")
        val yarn = MemoryMappingTree()
        MappingReader.read(finalYarnFile.toPath(), yarn)
        val tree = MemoryMappingTree()
        MappingReader.read(tinyFile.toPath(), tree)
        tinyFile.delete()
        val spigotNamespaceId = tree.getNamespaceId("spigot")
        val srcNameField = Class.forName("net.fabricmc.mappingio.tree.MemoryMappingTree\$Entry")
            .getDeclaredField("srcName")
        srcNameField.isAccessible = true
        for (c in tree.classes) {
            val className = c.srcName
            var yarnCM = yarn.getClass(className)
            if (yarnCM == null) {
                println("Attempting to fix parent name for $className")

                val split = className.split("$").toMutableList()
                val thing = split.removeAt(0)
                var theThing = tree.getClass(thing, spigotNamespaceId)
                var fail = false
                if (theThing != null) {
                    while (split.isNotEmpty()) {
                        val child = split.removeAt(0);
                        val parentSrcName = theThing!!.srcName
                        var concatName = "$parentSrcName$$child"
                        var lookup = tree.getClass(concatName) // we think it is obf name
                        if (lookup == null) { // no?
                            println("Could not find the class mapping object of $concatName in obfuscated namespace, trying Spigot name")
                            // maybe Spigot name?
                            concatName = theThing.getDstName(spigotNamespaceId)!! + "$" + child
                            lookup = tree.getClass(concatName, spigotNamespaceId)
                            if (lookup == null) {
                                fail = true
                                break
                            } else { // good
                                println("Found good parent: $lookup")
                                theThing = lookup
                            }
                        } else { // good
                            println("Found good parent: $lookup")
                            theThing = lookup
                        }
                    }
                } else {
                    fail = true
                }
                if (fail) {
                    println("WARN: class mapping not found for ${className}")
                    continue
                } else {
                    yarnCM = yarn.getClass(theThing!!.srcName)
                    if (yarnCM == null) {
                        println("WARN: class mapping not found for ${className}")
                        continue
                    } else {
                        val srcName = yarnCM.srcName
                        println("Fixed $className -> $srcName")
                        srcNameField.set(c, srcName)
                        c.setDstName(className, spigotNamespaceId)
                    }
                }
            }
            for (f in ArrayList(c.fields)) {
                val fieldName = f.srcName
                println("Fixing $fieldName in $c")
                var fieldNotFound = true
                for (yf in yarnCM.fields) {
                    if (yf.srcName.equals(fieldName)) {
                        f.srcDesc = yf.srcDesc
                        fieldNotFound = false
                        println("Fixed $fieldName's descriptor, new descriptor: ${yf.srcDesc}")
                        break
                    }
                }
                if (fieldNotFound) {
                    println("WARN: could not fix field descriptor for field $fieldName in $c: field not found")
                }
            }
        }



        println("$mcVersion: writing final tiny mappings to $mcVersion.tiny")
        val writer = MappingWriter.create(tinyFile.toPath(), MappingFormat.TINY_2_FILE)
        tree.accept(writer)

        return tinyFile
        // json
//        val classMappings =
//            MultimapBuilder.hashKeys(1000).arrayListValues().build<JavaType, Pair<String, JavaType>>()
//        val fieldMappings =
//            MultimapBuilder.hashKeys(1000).arrayListValues().build<FieldData, Pair<String, FieldData>>()
//        val methodMappings =
//            MultimapBuilder.hashKeys(1000).arrayListValues().build<MethodData, Pair<String, MethodData>>()
//        generatedMappings.filter { it.first.startsWith("obf2") }.forEach { pair ->
//            val name = pair.first.split("2")[1]
//            val mappings = pair.second
//            mappings.forEachClass { obf, mapped -> classMappings.put(obf, Pair(name, mapped)) }
//            mappings.forEachField { obf, mapped -> fieldMappings.put(obf, Pair(name, mapped)) }
//            mappings.forEachMethod { obf, mapped -> methodMappings.put(obf, Pair(name, mapped)) }
//            println("$mcVersion: generating json for $name")
//        }
//
//        fun String.lp(): String = split(".").last()
//
//        val classArray = JsonArray()
//        val fieldArray = JsonArray()
//        val methodArray = JsonArray()
//        for (obf in classMappings.keySet()) {
//            val mappedObj = JsonObject()
//            mappedObj.addProperty("obf", obf.name.lp())
//            classMappings.get(obf).forEach {
//                mappedObj.addProperty(it.first, it.second.name.lp())
//            }
//            classArray.add(mappedObj)
//        }
//        for (obf in fieldMappings.keySet()) {
//            val mappedObj = JsonObject()
//            mappedObj.addProperty("obf", obf.declaringType.name.lp() + "." + obf.name.lp())
//            fieldMappings.get(obf).forEach {
//                mappedObj.addProperty(it.first, it.second.declaringType.name.lp() + "." + it.second.name)
//            }
//            fieldArray.add(mappedObj)
//        }
//        for (obf in methodMappings.keySet()) {
//            val mappedObj = JsonObject()
//            mappedObj.addProperty("obf", obf.declaringType.name.lp() + "." + obf.name.lp())
//            methodMappings.get(obf).forEach {
//                mappedObj.addProperty(it.first, it.second.declaringType.name.lp() + "." + it.second.name)
//            }
//            methodArray.add(mappedObj)
//        }
//
//        val bigJson = JsonObject()
//        bigJson.addProperty("minecraftVersion", mcVersion)
//        bigJson.add("classes", classArray)
//        bigJson.add("fields", fieldArray)
//        bigJson.add("methods", methodArray)
//        File(outputFolder, "$mcVersion.json").writeText(Gson().toJson(bigJson))
    }

    companion object {
        @JvmStatic
        private val mcVersionToEnumValue: Map<String, MinecraftVersion>

        init {
            val map = mutableMapOf<String, MinecraftVersion>()
            for (minecraftVersion in values()) {
                map[minecraftVersion.mcVersion] = minecraftVersion
            }
            mcVersionToEnumValue = map.toMap()
        }

        @JvmStatic
        fun findByMCVersion(
            mcVersion: String
        ) = mcVersionToEnumValue[mcVersion]
    }
}
