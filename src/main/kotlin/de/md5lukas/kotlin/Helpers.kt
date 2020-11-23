package de.md5lukas.kotlin

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.IOException
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.math.max

object Helpers {
    fun compareVersions(
        installedString: String,
        latestString: String?,
        logger: Logger = Logger.getGlobal()
    ): VersionStatus {
        val installedParts: List<Int>
        val latestParts: List<Int>

        try {
            installedParts = installedString.split(".", "-").map(String::toInt)
            latestParts = latestString?.split(".", "-")?.map(String::toInt) ?: return VersionStatus.UNKNOWN
        } catch (nfe: NumberFormatException) {
            logger.log(Level.WARNING, "Could not parse current or latest version needed for update checking")
            return VersionStatus.UNKNOWN
        }

        for (index in 0 until max(installedParts.size, latestParts.size)) {
            val installed = installedParts.getOrNull(index) ?: 0
            val latest = latestParts.getOrNull(index) ?: 0
            if (installed > latest) {
                return VersionStatus.RECENT
            } else if (latest > installed) {
                return VersionStatus.OUT_OF_DATE
            }
        }
        return VersionStatus.RECENT
    }

    fun fetchLatestGithubReleaseTag(
        owner: String,
        repository: String,
        logger: Logger = Logger.getGlobal()
    ): String? {
        try {
            val connection = URL("https://api.github.com/repos/$owner/$repository/releases/latest").openConnection()
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json")
            val jsonString = connection.getInputStream().bufferedReader(StandardCharsets.UTF_8).use {
                it.readText()
            }
            val parsed: JSONObject = JSONParser().parse(jsonString) as JSONObject
            return (parsed["tag_name"] as String?)?.let {
                if (it.startsWith("v")) {
                    it.substring(1)
                } else {
                    it
                }
            }
        } catch (ioe: IOException) {
            logger.log(Level.WARNING, "Could not fetch latest version of this plugin", ioe)
        } catch (cce: ClassCastException) {
            logger.log(Level.WARNING, "Could not parse returned JSON data")
        }
        return null
    }
}

enum class VersionStatus {
    UNKNOWN, OUT_OF_DATE, RECENT;
}