# Publishing

This document covers what a CortenaUI maintainer needs to do to ship a release. End users do not need to read this — they only need [INSTALLATION.md](INSTALLATION.md).

CortenaUI publishes four artifacts to **Maven Central** under the `io.github.cortenaui` group ID:

- `io.github.cortenaui:ui-foundation`
- `io.github.cortenaui:ui-shape`
- `io.github.cortenaui:ui-motion`
- `io.github.cortenaui:ui`

The `ui` artifact is the meta-artifact users normally depend on — it transitively pulls every other module. The `ui-*` siblings let consumers cherry-pick.

Publishing is automated through the `Publish` GitHub Actions workflow. Pushing a tag of the form `v<version>` (for example `v0.2.0-alpha`) triggers a build, signs every artifact, uploads them to Maven Central, and attaches the per-module AARs to a GitHub Release.

## One-time setup

Before the first publish, you need three things in order: a Sonatype Central Portal account, a GPG key, and four GitHub Actions secrets.

### 1. Sonatype Central Portal namespace

1. Sign in at [central.sonatype.com](https://central.sonatype.com).
2. Go to **Namespaces** and add `io.github.cortenaui`. Sonatype verifies GitHub-based namespaces automatically by reading public repositories under the `cortenaui` org. Verification takes a few minutes.
3. Generate a **publishing token** under **View Account → Generate User Token**. Copy the token's username and password — you'll store them as GitHub secrets in step 3.

### 2. GPG signing key

Maven Central requires every artifact to be signed.

```bash
# Generate a key (RSA, 4096 bits, 0 expiry)
gpg --full-generate-key

# List secret keys to find the key id
gpg --list-secret-keys --keyid-format LONG

# Export the secret key as ASCII (this is what GitHub Actions consumes)
gpg --armor --export-secret-keys <KEY_ID> > signing.key

# Upload the public key to a keyserver so Maven Central can fetch it for verification
gpg --keyserver keyserver.ubuntu.com --send-keys <KEY_ID>
```

Pick a strong passphrase when generating the key — store it alongside the key in your password manager.

### 3. GitHub Actions secrets

Open `https://github.com/cortenaui/cortenaui/settings/secrets/actions` and add the following four secrets, then create an environment named `maven-central` and assign the secrets to it (the publish workflow runs in that environment):

| Secret                           | Value                                                                 |
| -------------------------------- | --------------------------------------------------------------------- |
| `MAVEN_CENTRAL_USERNAME`         | Username from the Sonatype publishing token (step 1).                 |
| `MAVEN_CENTRAL_PASSWORD`         | Password from the Sonatype publishing token (step 1).                 |
| `SIGNING_IN_MEMORY_KEY`          | Contents of `signing.key` from step 2 (the full ASCII-armored block). |
| `SIGNING_IN_MEMORY_KEY_PASSWORD` | Passphrase you used when generating the GPG key.                      |

The plugin (`com.vanniktech.maven.publish`) reads them as `ORG_GRADLE_PROJECT_*` environment variables — the workflow translates the secrets into that format automatically.

## Cutting a release

Each release is a signed Git tag of the form `v<version>`. Versions follow the layout `MAJOR.MINOR.PATCH-<channel>`, where channel is `alpha`, `beta`, or omitted for stable.

> The repository's `master` branch is protected, so version bumps go through a PR. The publish workflow is fired by the **tag push**, not by the merge, so branch protection does not block releases.

```bash
# 1. Create a release branch off master
git checkout master
git pull
git checkout -b release/0.1.1-alpha

# 2. Bump the version. The single line `version = "0.1.1-alpha"` in the project root
#    `build.gradle.kts` controls all four library modules. Mirror it in
#    `catalog/build.gradle.kts` for cosmetic parity.
${EDITOR:-vim} build.gradle.kts
${EDITOR:-vim} catalog/build.gradle.kts

# 3. Commit and push the branch.
git commit -am "release: 0.1.1-alpha"
git push origin release/0.1.1-alpha

# 4. Open a PR, wait for the Build workflow to pass, merge it.

# 5. Pull the merged commit and tag it.
git checkout master
git pull
git tag v0.1.1-alpha
git push origin v0.1.1-alpha
```

The tag push is what fires the Publish workflow. Watch progress at `https://github.com/cortenaui/cortenaui/actions`. The full pipeline takes around five minutes.

When the workflow finishes, head to [central.sonatype.com](https://central.sonatype.com) → **Deployments** and confirm a draft deployment is staged. Review the artifacts, then click **Publish** to release to Maven Central. Artifacts become consumable about ten to thirty minutes after the publish click.

A GitHub Release is also created automatically, with the four AARs attached as assets and release notes generated from commit history.

## Local testing before tagging

Before pushing a tag, smoke-test locally to confirm the publish pipeline works without exposing it to the world.

### `publishToMavenLocal`

Publishes every artifact to your local `~/.m2/repository`. Fast feedback, no credentials, no network.

```bash
./gradlew publishToMavenLocal
```

In a consumer project, point Gradle at the local cache and reference the artifacts:

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}
```

```kotlin
// app/build.gradle.kts
dependencies {
    implementation("io.github.cortenaui:ui:0.2.0-alpha")
}
```

If everything resolves and the demo app compiles, the publish config is sound.

### Dry-run signing

To verify your GPG setup without uploading anything, run:

```bash
./gradlew publishAllPublicationsToMavenLocalRepository \
  -PsigningInMemoryKey="$(cat signing.key)" \
  -PsigningInMemoryKeyPassword="<your passphrase>"
```

Look for `*.asc` files alongside each `.aar`, `.jar`, and `.pom` in the output directories. If they're missing, signing didn't fire.

## Release checklist

Before tagging, walk through this:

- [ ] Working tree clean on `master`.
- [ ] Version bumped in `build.gradle.kts` at the project root.
- [ ] Catalog `versionName` matches in `catalog/build.gradle.kts` (cosmetic — the catalog isn't published).
- [ ] `CHANGELOG.md` updated with a section for the new version (if maintained).
- [ ] All component docs in `docs/components/` reflect the current API.
- [ ] `./gradlew build` passes locally.
- [ ] `./gradlew publishToMavenLocal` succeeds and the artifacts look right under `~/.m2/repository/io/github/cortenaui/`.

After tagging:

- [ ] Workflow run is green at `https://github.com/cortenaui/cortenaui/actions`.
- [ ] Deployment is visible at [central.sonatype.com](https://central.sonatype.com) → Deployments.
- [ ] Reviewed and clicked **Publish** on the staging deployment.
- [ ] Verified the GitHub Release page has the four AAR assets attached.
- [ ] Smoke-tested in a fresh consumer project after Maven Central indexes the artifacts.

## Troubleshooting

**"401 Unauthorized" during upload.** The `MAVEN_CENTRAL_USERNAME` / `MAVEN_CENTRAL_PASSWORD` secrets are wrong or the Sonatype token expired. Regenerate the token at central.sonatype.com and update the secrets.

**"Failed to verify signature" on the deployment.** The public key isn't on a keyserver Maven Central can reach, or the `SIGNING_IN_MEMORY_KEY` doesn't match the public key. Re-upload the public key with `gpg --send-keys`.

**"Namespace not verified" on the deployment.** Sonatype hasn't finished verifying `io.github.cortenaui`. Wait a few minutes; if it persists, check the namespace status at [central.sonatype.com](https://central.sonatype.com) → Namespaces.

**Dependency cycle on local publish.** Inter-module `api(project(":foundation"))` declarations can sometimes confuse `publishToMavenLocal` ordering. Run with `--no-parallel` if you hit this.
