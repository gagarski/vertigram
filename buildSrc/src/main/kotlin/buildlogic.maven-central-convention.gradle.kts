import com.vanniktech.maven.publish.Checksum
import com.vanniktech.maven.publish.DeploymentValidation

plugins {
    id("com.vanniktech.maven.publish.base")
}

val deploymentAction = providers.gradleProperty("vertigram.central.deployment.action").orElse("upload")

mavenPublishing {
    checksums(Checksum.MD5, Checksum.SHA1)
    excludeSignatureChecksums(true)

    publishToMavenCentral(
        automaticRelease = deploymentAction.map { it == "publish" }.get(),
        validateDeployment = deploymentAction.map {
            when (it) {
                "upload" -> DeploymentValidation.NONE
                "validate" -> DeploymentValidation.VALIDATED
                "publish" -> DeploymentValidation.PUBLISHED
                else -> throw GradleException(
                    "Unsupported vertigram.central.deployment.action=$it. " +
                        "Expected one of: upload, validate, publish."
                )
            }
        }.get()
    )
}