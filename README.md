# POKYON — Android game prototype

POKYON is a Kotlin/Jetpack Compose Android project with local game screens, a web-based playable prototype, data persistence experiments and billing/routing **prototype code**. It is **not** a production banking integration or a published commercial game.

## Run in Android Studio

1. Install a current Android Studio with Android SDK support for the project's declared compile SDK.
2. Clone this repository and open it in Android Studio.
3. Review Gradle plugin versions in `gradle/libs.versions.toml` and sync with a compatible Android Gradle Plugin/JDK toolchain.
4. Set required local environment values (see `.env.example`) without committing secrets.
5. Select the debug variant and run on an emulator or test device. The standard local Android debug signing key is used; no shared debug key is required.

The repository does not currently contain Gradle wrapper scripts, so fully reproducible CLI Android builds are **not yet verified**. Use Android Studio and confirm sync/build locally.

## Project areas

- `app/src/main/java/com/example/`: Android UI, ViewModels, and local Room data components.
- `app/src/main/assets/`: embedded game/web scenes.
- `PokyonMultiBankRouting.js`, `PokyonNepalBillingEngine.js`: experimental logic, not verified financial integrations.
- `pokyon-build-package/`: experimental game engine, backend and web dashboard skeletons.

## Security notice

A debug signing key and its signed debug APK were previously committed. Neither should be trusted for public distribution. The current branch excludes them; **Git history still contains earlier copies**. See [SECURITY.md](SECURITY.md) for remediation and re-signing guidance. Only create new builds with local/private signing keys.

## Testing and publication

Static repository checks are in `.github/workflows/verify.yml`. They are not Android compilation tests. Before publishing, add a Gradle wrapper, run unit/instrumented tests, validate WebView handling, test payment flows using sandbox credentials and integrate server-side receipt/payment verification. No production readiness claim is made.
