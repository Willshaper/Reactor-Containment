package com.cultivate.reactorcontainment;

import com.cultivate.reactorcontainment.config.ReactorContainmentConfig;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * Re-reads the config after an in-game edit. GTNHLib only re-syncs its own config on change, so
 * without this the mixins would keep the startup values. Client-only (see
 * {@link ReactorContainment#preInit}).
 */
public class ReactorContainmentConfigHandler {

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (!ReactorContainment.MODID.equals(event.modID)) {
            return;
        }
        try {
            ConfigurationManager.registerConfig(ReactorContainmentConfig.class);
        } catch (Exception e) {
            ReactorContainment.LOG.error("Failed to reload Reactor Containment config after a config change", e);
        }
    }
}
