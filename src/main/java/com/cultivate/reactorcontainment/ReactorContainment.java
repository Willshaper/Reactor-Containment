package com.cultivate.reactorcontainment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cultivate.reactorcontainment.config.ReactorContainmentConfig;
import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Reactor Containment: stops IC2 nuclear reactors from blowing up the world around them.
 *
 * <p>All reactor logic in IC2 runs on the server, so this mod only needs to be installed there.
 * {@code acceptableRemoteVersions = "*"} lets clients without the mod join a server that has it.</p>
 */
@Mod(
    modid = ReactorContainment.MODID,
    name = "Reactor Containment",
    version = Tags.VERSION,
    dependencies = "required-after:IC2;required-after:gtnhlib",
    acceptedMinecraftVersions = "[1.7.10]",
    acceptableRemoteVersions = "*")
public class ReactorContainment {

    public static final String MODID = "reactorcontainment";
    public static final Logger LOG = LogManager.getLogger("ReactorContainment");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        try {
            ConfigurationManager.registerConfig(ReactorContainmentConfig.class);
        } catch (ConfigException e) {
            LOG.error("Failed to register Reactor Containment config; using defaults.", e);
        }
        LOG.info("Reactor Containment loaded: mode={}", ReactorContainmentConfig.mode);

        // In singleplayer the config can be edited in-game; ConfigChangedEvent is client-only.
        if (event.getSide().isClient()) {
            FMLCommonHandler.instance().bus().register(new ReactorContainmentConfigHandler());
        }
    }
}
