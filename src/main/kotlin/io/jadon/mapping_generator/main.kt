package io.jadon.mapping_generator

import io.jadon.mapping_generator.enums.MinecraftVersion
import joptsimple.OptionParser
import java.io.File

val GLOBAL_FOLDER = File("mappings")

fun main(args: Array<String>) {
    val time = System.currentTimeMillis()
    GLOBAL_FOLDER.mkdirs()

	if (args.isEmpty()) {
		println("Usage: java -jar mappings-generator.jar --mc <minecraftversion> --yarn <file> --spigotCls <file> [--spigotMember file]")
		println("Possible values for <minecraftversion> :")
		MinecraftVersion.values().forEach {
			println("\t" + it.mcVersion);
		}
	} else {
		val parser = OptionParser()
		val mcOpt = parser.accepts("mc").withRequiredArg().ofType(String::class.java)
		val yarnOpt = parser.accepts("yarn").withRequiredArg().ofType(String::class.java)
//		val yInOpt = parser.accepts("yarn").withRequiredArg().ofType(File::class.java)
		val spigotClsOpt = parser.accepts("spigotCls").withRequiredArg().ofType(File::class.java)
		val spigotMemberIn = parser.accepts("spigotMember").withOptionalArg().ofType(File::class.java)
		val optSet = parser.parse(*args)

	    MinecraftVersion.findByMCVersion(optSet.valueOf(mcOpt))!!.write(optSet.valueOf(yarnOpt), optSet.valueOf(spigotClsOpt), optSet.valueOf(spigotMemberIn), GLOBAL_FOLDER)
        val elapsed = (System.currentTimeMillis() - time) / 1000.0
        println("Done. Took ${elapsed / 60}m (${elapsed}s)")
	}
}
