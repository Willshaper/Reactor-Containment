package com.cultivate.reactorcontainment.core;

import com.gtnewhorizon.gtnhmixins.builders.ITargetMod;
import com.gtnewhorizon.gtnhmixins.builders.TargetModBuilder;

public enum TargetMods implements ITargetMod {

    IC2_REACTOR(new TargetModBuilder()
        .setTargetClass("ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric"));

    private final TargetModBuilder builder;

    TargetMods(TargetModBuilder builder) {
        this.builder = builder;
    }

    @Override
    public TargetModBuilder getBuilder() {
        return this.builder;
    }
}
