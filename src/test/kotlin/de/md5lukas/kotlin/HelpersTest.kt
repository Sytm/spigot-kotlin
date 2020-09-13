package de.md5lukas.kotlin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import de.md5lukas.kotlin.Helpers.Companion.compareVersions as compareVersions
import de.md5lukas.kotlin.Helpers.Companion.fetchLatestGithubReleaseTag as fetchLatestGithubReleaseTag
import de.md5lukas.kotlin.Helpers.VersionStatus as VersionStatus

class HelpersTest {

    @Test
    fun `Versions are equal`() {
        assertEquals(VersionStatus.RECENT, compareVersions("2.3.4", "2.3.4"))
    }

    @Test
    fun `Versions and build are equal`() {
        assertEquals(VersionStatus.RECENT, compareVersions("2.3.4-5", "2.3.4-5"))
    }

    @Test
    fun `Latest build is ahead`() {
        assertEquals(VersionStatus.OUT_OF_DATE, compareVersions("2.3.4-5", "2.3.4-6"))
    }

    @Test
    fun `Latest build is behind`() {
        assertEquals(VersionStatus.RECENT, compareVersions("2.3.4-5", "2.3.4-4"))
    }

    @Test
    fun `Installed without build number, latest with build number`() {
        assertEquals(VersionStatus.OUT_OF_DATE, compareVersions("2.3.4", "2.3.4-1"))
    }

    @Test
    fun `Installed with build number, latest without build number`() {
        assertEquals(VersionStatus.RECENT, compareVersions("2.3.4-1", "2.3.4"))
    }

    @Test
    fun `Latest patch is ahead`() {
        assertEquals(VersionStatus.OUT_OF_DATE, compareVersions("2.3.4","2.3.5"))
    }

    @Test
    fun `Latest patch is behind`() {
        assertEquals(VersionStatus.RECENT, compareVersions("2.3.4","2.3.3"))
    }

    @Test
    fun `Latest minor is ahead`() {
        assertEquals(VersionStatus.OUT_OF_DATE, compareVersions("2.3.4","2.4.4"))
    }

    @Test
    fun `Latest minor is behind`() {
        assertEquals(VersionStatus.RECENT, compareVersions("2.3.4","2.2.4"))
    }

    @Test
    fun `Latest major is ahead`() {
        assertEquals(VersionStatus.OUT_OF_DATE, compareVersions("2.3.4","3.3.4"))
    }

    @Test
    fun `Latest major is behind`() {
        assertEquals(VersionStatus.RECENT, compareVersions("2.3.4","1.3.4"))
    }

    @Test
    fun `Fetch latest from this repository`() {
        println("This repo: " + fetchLatestGithubReleaseTag(UpdateChecker.OWNER, UpdateChecker.REPOSITORY))
    }

    @Test
    fun `Fetch latest from waypoints repository`() {
        println("Waypoints: " + fetchLatestGithubReleaseTag(UpdateChecker.OWNER, "waypoints"))
    }
}