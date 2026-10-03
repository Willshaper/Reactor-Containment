# Reactor Containment

Stops IC2 nuclear reactors from blowing up the world around them, for Minecraft 1.7.10 / GT New Horizons.

**Server-side only.** All of IC2's reactor logic runs on the server, so install it there. Clients don't need it and
can join with or without it. In singleplayer, put it in your own `mods` folder.

Requires IC2 (experimental 2.2.828, as shipped in GTNH) and GTNHLib.

## Download

Get the jar from the [Releases](../../releases) page and put it in your `mods` folder. Use the plain
`reactorcontainment-<version>.jar`. The `-dev` and `-sources` jars are for developers.

## What it does

When a reactor reaches 100% heat, IC2 deletes the components, the chambers and the reactor core, then sets off an
explosion of up to 100 power, where TNT is 4. From 40% heat upward it also burns, evaporates and melts blocks
nearby. This mod keeps the risk but contains it:

| Mode | At 100% heat |
|---|---|
| `VOID` (default) | The reactor core, its chambers and every component are deleted. No explosion. Nothing else is touched. |
| `CAP` | Hull heat is held at *Cap Percent* (default 99%), so the reactor never melts down. |
| `VANILLA` | Unchanged IC2 behavior. |

`CAP` deletes any heat above the cap, so even a reactor with no cooling at all runs forever. Use `VOID` if you want
reactor design to still matter.

In `VOID` and `CAP` mode, IC2's heat effects on nearby blocks (fire from 40%, water evaporation from 50%, fire and
lava from 85%) are off by default. Radiation damage to players and mobs near a reactor above 70% heat stays on by
default.

Each voided reactor is logged as `Nuclear reactor at ... reached max heat and was voided`.

## Config

`config/reactorcontainment.cfg`. In singleplayer you can also change it from the in-game mod config screen.

| Option | Default | |
|---|---|---|
| Mode | `VOID` | `VOID`, `CAP` or `VANILLA` |
| Cap Percent | `99` | `CAP` only, 1 to 99 |
| Heat Block Effects | `false` | Keep IC2's fire, evaporation and lava near hot reactors |
| Heat Entity Damage | `true` | Keep radiation damage near reactors above 70% heat |

`VOID` and `CAP` also apply when IC2.ini has `reactorExplosionPowerLimit = 0`. Normally that setting turns off
meltdowns and every heat effect, which leaves reactors with no failure state at all.

## Building

The build runs Gradle on JDK 25 (`gradle/gradle-daemon-jvm.properties`). The mod compiles to Java 8 bytecode.

```
./gradlew build
```

The version comes from the latest git tag.

## License

LGPL-3.0. See [LICENSE](LICENSE).
