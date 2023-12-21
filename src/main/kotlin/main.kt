import java.io.File

val GLOBAL_FOLDER = File("mappings")

fun main(args: Array<String>) {
    val time = System.currentTimeMillis()
    GLOBAL_FOLDER.mkdirs()

	if (args.isEmpty()) {
		println("Usage: java -jar mappings-generator.jar <minecraftversion>")
		println("Possible values for <minecraftversion> :")
		MinecraftVersion.values().forEach {
			println("\t" + it.mcVersion);
		}
	} else {
	    MinecraftVersion.findByMCVersion(args[0])!!.write(GLOBAL_FOLDER)
        val elapsed = (System.currentTimeMillis() - time) / 1000.0
        println("Done. Took ${elapsed / 60}m (${elapsed}s)")
	}
}
