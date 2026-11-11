# POKYON security guidance

## Android signing material

This project previously tracked a Base64-encoded Android **debug** keystore and a debug APK. They have been removed from the current branch. They remain accessible through Git history until a repository history rewrite and any cached copies are addressed. The debug signing identity must be treated as **compromised** and must not be trusted for application distribution. If an old APK is installed, uninstall it before installing a new debug build, because a fresh debug key may have a different signature.

The Android build now uses Android's locally managed default debug signing configuration. **Never use debug builds for public distribution.**

For release builds, supply your own private upload key using the release signing environment variables defined in `app/build.gradle.kts`. Manage release credentials in your developer account/secure build environment, never in Git. Do not claim a release is verified until it has been rebuilt and tested.

## Reporting

If you find a potential vulnerability, report it privately to the repository owner. Do not open public issues containing credentials, payment details or user information.

## Scope

POKYON includes prototype game and billing components. Source code and static checks do not establish payment-system security, Android release signing readiness, production scaling or store compliance. Any real banking, payments or personal data processing must use a separate, independently reviewed backend and provider verification.
