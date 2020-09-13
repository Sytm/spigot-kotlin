package de.md5lukas.kotlin

import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

class Spigot : JavaPlugin() {

    override fun onEnable() {
        val updateChecker = UpdateChecker(this)
        server.scheduler.runTaskAsynchronously(this, Runnable {
            updateChecker.fetchLatest()
            val result = updateChecker.isLatest()
            server.scheduler.runTask(this, Runnable {
                if (result.versionStatus == Helpers.VersionStatus.OUT_OF_DATE) {
                    logger.log(
                        Level.INFO,
                        "The current version of this plugin is outdated (${result.latestVersion} > ${result.currentVersion})." +
                                "Consider downloading a newer version from ${description.website}"
                    )
                }
            })
        })
    }
}