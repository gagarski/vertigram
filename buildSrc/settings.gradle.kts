dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
        create("libsInternal") {
            from(files("../gradle/libs.internal.versions.toml"))
        }
    }
}