package de.md5lukas.kotlin

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.IOException
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.logging.Level

class UpdateChecker(private val main: Spigot) {

    private companion object {
        const val USER = "Sytm"
        const val REPOSITORY = "spigot-kotlin"
        const val PATH = "https://api.github.com/repos/$USER/$REPOSITORY/releases/latest"
    }

    private val currentVersion = main.description.version
    private var latestVersion: String? = null

    fun fetchLatest() {
        try {
            val jsonString = URL(PATH).openStream().bufferedReader(StandardCharsets.UTF_8).let {
                val text = it.readText()
                it.close()
                text
            }
            val parsed: JSONObject = JSONParser().parse(jsonString) as JSONObject
            latestVersion = (parsed["tag_name"] as String?)?.let {
                if (it.startsWith("v")) {
                    it.substring(1)
                } else {
                    it
                }
            }
        } catch (ioe: IOException) {
            main.logger.log(Level.WARNING, "Could not fetch latest version of this plugin", ioe)
        } catch (cce: ClassCastException) {
            main.logger.log(Level.WARNING, "Could not parse returned JSON data")
        }
    }

    fun isLatest(): UpdateResult {
        val latest: List<Int>
        val curr: List<Int>

        try {
            latest = latestVersion?.split(".", "-")?.map(String::toInt) ?: return createUpdateResult(VersionStatus.UNKNOWN)
            curr = currentVersion.split(".", "-").map(String::toInt)
        } catch (nfe: NumberFormatException) {
            main.logger.log(Level.WARNING, "Could not parse current or latest version needed for update checking")
            return createUpdateResult(VersionStatus.UNKNOWN)
        }

        when {
            latest[0] > curr[0] -> {
                return createUpdateResult(VersionStatus.OUT_OF_DATE)
            }
            latest[1] > curr[1] -> {
                return createUpdateResult(VersionStatus.OUT_OF_DATE)
            }
            latest[2] > curr[2] -> {
                return createUpdateResult(VersionStatus.OUT_OF_DATE)
            }
            else -> {
                val latestBuild = if (latest.size == 4) {
                    latest[3]
                } else {
                    0
                }
                val currentBuild = if (curr.size == 4) {
                    curr[3]
                } else {
                    0
                }
                if (latestBuild > currentBuild) {
                    return createUpdateResult(VersionStatus.OUT_OF_DATE)
                }
                return createUpdateResult(VersionStatus.RECENT)
            }
        }
    }

    private fun createUpdateResult(versionStatus: VersionStatus): UpdateResult {
        return UpdateResult(
            versionStatus, currentVersion, latestVersion
        )
    }

    enum class VersionStatus {
        UNKNOWN, OUT_OF_DATE, RECENT;
    }

    class UpdateResult(val versionStatus: VersionStatus, val currentVersion: String, val latestVersion: String?)
}