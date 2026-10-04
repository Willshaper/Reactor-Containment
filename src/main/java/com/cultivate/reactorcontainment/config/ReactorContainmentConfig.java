package com.cultivate.reactorcontainment.config;

import com.cultivate.reactorcontainment.ReactorContainment;
import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = ReactorContainment.MODID, category = "general")
public class ReactorContainmentConfig {

    public enum Mode {
        VOID,
        CAP,
        VANILLA
    }

    @Config.Comment({ "What happens when an IC2 nuclear reactor reaches 100% heat.",
        "VOID: the reactor core, its chambers and all components are deleted. No explosion, and no blocks around it",
        "      are touched.",
        "CAP: hull heat is clamped to 'Cap Percent' so the reactor never melts down.",
        "      Warning: heat above the cap is simply deleted, so even uncooled reactors run forever.",
        "VANILLA: unchanged IC2 behavior (explosion, heat effects).",
        "VOID and CAP apply even if IC2.ini has reactorExplosionPowerLimit = 0.",
        "IC2 checks heat even while a reactor is switched off, so a reactor already at or above 100% is voided",
        "(VOID) or pulled down to the cap (CAP) on its first tick after its chunk loads.",
        "A fresh install starts in CAP so no existing reactor is deleted on first boot.",
        "Dedicated servers: restart after changing this file." })
    @Config.DefaultEnum("CAP")
    @Config.Name("Mode")
    public static Mode mode = Mode.CAP;

    @Config.Comment("CAP mode only: hull heat never goes above this percentage of the reactor's max heat.")
    @Config.DefaultInt(99)
    @Config.RangeInt(min = 1, max = 99)
    @Config.Name("Cap Percent")
    public static int capPercent = 99;

    @Config.Comment({ "VOID/CAP: keep IC2's heat effects on blocks near a hot reactor",
        "(fire at 40%+, water evaporation at 50%+, fire and lava at 85%+)." })
    @Config.DefaultBoolean(false)
    @Config.Name("Heat Block Effects")
    public static boolean heatBlockEffects = false;

    @Config.Comment("VOID/CAP: keep IC2's radiation damage to mobs and players near a reactor above 70% heat.")
    @Config.DefaultBoolean(true)
    @Config.Name("Heat Entity Damage")
    public static boolean heatEntityDamage = true;
}
