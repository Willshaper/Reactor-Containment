package com.cultivate.reactorcontainment.core;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;

public enum Mixins implements IMixins {

    IC2_REACTOR_CONTAINMENT(new MixinBuilder("Void or heat-cap IC2 nuclear reactors instead of exploding")
        .addCommonMixins("MixinTileEntityNuclearReactorElectric")
        .setPhase(Phase.LATE)
        .addRequiredMod(TargetMods.IC2_REACTOR));

    private final MixinBuilder builder;

    Mixins(MixinBuilder builder) {
        this.builder = builder;
    }

    @Override
    public MixinBuilder getBuilder() {
        return this.builder;
    }
}
