package io.jadon.mapping_generator

import io.jadon.mapping_generator.enums.MinecraftVersion
import joptsimple.OptionParser
import java.io.File

val GLOBAL_FOLDER = File("mappings")

fun main(args: Array<String>) {
    val time = System.currentTimeMillis()
    GLOBAL_FOLDER.mkdirs()
	val parser = OptionParser()
	val mcOpt = parser.accepts("mc", "The Minecraft version").withRequiredArg().ofType(String::class.java)
	val yarnOpt = parser.accepts("yarn", "Yarn mapping file path").withRequiredArg().ofType(File::class.java)
	val spigotClsOpt = parser.accepts("spigotCls", "Spigot class mapping file path").withRequiredArg().ofType(File::class.java)
	val spigotMemberIn = parser.accepts("spigotMember",
		"Spigot member mapping file path " +
				"(not necessary if spigot mapping type of provided Minecraft version is MODERN)")
		.withOptionalArg().ofType(File::class.java)
	if (args.isEmpty()) {
		parser.printHelpOn(System.out)
		println("Possible values for <mc> :")
		MinecraftVersion.values().forEach {
			println("\t" + it.mcVersion);
		}
	} else {
		val optSet = parser.parse(*args)

	    MinecraftVersion.findByMCVersion(optSet.valueOf(mcOpt))!!.write(optSet.valueOf(yarnOpt), optSet.valueOf(spigotClsOpt), optSet.valueOf(spigotMemberIn), GLOBAL_FOLDER)
        val elapsed = (System.currentTimeMillis() - time) / 1000.0
        println("Done. Took ${elapsed / 60}m (${elapsed}s)")
	}
}
