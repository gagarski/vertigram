package ski.gagar.vertigram.dokka.tool

import java.nio.file.Path
import java.util.*
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.deleteRecursively

class Repository(
    val root: Path,
    val majorVersionParts: Int = 2,
    val keepMajor: Int = 3,
    val keepMinor: Int = 1
) {
    private val versions: MutableList<VersionEx> = mutableListOf()

    init {
        scan()
    }

    fun scan() {
        versions.clear()
        val archDir = archive.toFile()
        if (!archDir.isDirectory) {
            throw IllegalArgumentException("$archDir is not a directory")
        }

        for (file in archDir.listFiles()!!) {
            val v = file.name.toVersionOrNull(majorVersionParts)
            if (null == v) {
                continue
            }
            versions.add(VersionEx(v, false))
        }
    }

    fun addVirtual(version: Version) {
        require(version.majorParts == majorVersionParts)
        versions.add(VersionEx(version, true))
    }

    fun addVirtual(version: String) {
        addVirtual(version.toVersion(majorVersionParts))
    }

    private fun groupByMajor(): NavigableMap<Version, NavigableSet<VersionEx>> {
        val res = TreeMap<Version, NavigableSet<VersionEx>>(Comparator.reverseOrder())

        for (version in versions) {
            res.computeIfAbsent(version.version.major) {
                TreeSet(Comparator.reverseOrder())
            }.add(version)
        }

        return res
    }

    private fun getVersionsToKeepSeq() =
        groupByMajor()
            .asSequence()
            .take(keepMajor)
            .flatMap { (_, v) ->
                v.asSequence().take(keepMinor)
            }
            .filter {
                !it.virtual
            }
            .map {
                it.version
            }

    fun getVersionsToKeep() = getVersionsToKeepSeq().toList()
    fun getPathsToKeep() = getVersionsToKeepSeq().map { it.path }.toList()

    private fun getVersionsToRemoveSeq(): Sequence<Version> {
        val oldMajors =
            groupByMajor()
                .asSequence()
                .drop(keepMajor)
                .flatMap { (_, v) ->
                    v.asSequence()
                }
                .filter {
                    !it.virtual
                }
                .map {
                    it.version
                }

        val oldMinors =
            groupByMajor()
                .asSequence()
                .take(keepMajor)
                .flatMap { (_, v) ->
                    v.asSequence().drop(keepMinor)
                }
                .filter {
                    !it.virtual
                }
                .map {
                    it.version
                }

        return oldMajors + oldMinors
    }

    fun getVersionsToRemove() = getVersionsToRemoveSeq().toList()
    fun getPathsToRemove() = getVersionsToRemoveSeq().map { it.path }.toList()

    @OptIn(ExperimentalPathApi::class)
    fun removeOld() {
        for (path in getPathsToRemove()) {
            path.deleteRecursively()
        }
    }

    private val archive
        get() = root.resolve("archive")

    private val Version.path
        get() = archive.resolve(this.toString())

    data class VersionEx(val version: Version, val virtual: Boolean) : Comparable<VersionEx> {
        override fun compareTo(other: VersionEx): Int = version.compareTo(other.version)
    }
}