package de.md5lukas.kotlin

class UpdateChecker(private val main: Spigot) {

    companion object {
        const val OWNER = "Sytm"
        const val REPOSITORY = "spigot-kotlin"
    }

    private val installedVersion = main.description.version
    private var latestVersion: String? = null

    fun fetchLatest() {
        latestVersion = Helpers.fetchLatestGithubReleaseTag(OWNER, REPOSITORY)
    }

    fun isLatest(): UpdateResult {
        return UpdateResult(
            Helpers.compareVersions(installedVersion, latestVersion, main.logger), installedVersion, latestVersion
        )
    }

    class UpdateResult(val versionStatus: VersionStatus, val currentVersion: String, val latestVersion: String?)
}