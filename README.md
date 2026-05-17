# CMIICore

CMIICore is a Cleanroom-powered Minecraft 1.12.2 mod skeleton for the CMII modpack.

This project is based on the official Cleanroom mod development template and uses Cleanroom's Unimined Gradle setup. It currently builds as a normal Forge/Cleanroom `@Mod`, which is the right starting point for a modpack core mod that registers content, config, integrations, recipes, events, or startup checks.

## Build

```powershell
.\gradlew.bat build
```

The remapped release jar is written to `build/libs`.

If Gradle cannot connect but Git can, your Git proxy is probably not being used by Java. For a local proxy on `127.0.0.1:7890`, run:

```powershell
$env:JAVA_OPTS='-Dhttp.proxyHost=127.0.0.1 -Dhttp.proxyPort=7890 -Dhttps.proxyHost=127.0.0.1 -Dhttps.proxyPort=7890'
.\gradlew.bat build
```

## Run Client

```powershell
.\gradlew.bat runClient
```

In IntelliJ IDEA, import the Gradle project and use the Gradle task named `2. Run Client`.

## Project Settings

Important values live in `gradle.properties`:

- `mod_id = cmiicore`
- `mod_name = CMIICore`
- `root_package = com.cmii`
- `mod_version = 0.1.0`
- `use_access_transformer = false`
- `is_coremod = false`

If you later need a real FML loading plugin/ASM coremod, set `is_coremod = true`, provide `coremod_plugin_class_name`, and implement `IFMLLoadingPlugin`. Do that only for bytecode transformation; most modpack core logic should stay in the normal `@Mod` lifecycle.
