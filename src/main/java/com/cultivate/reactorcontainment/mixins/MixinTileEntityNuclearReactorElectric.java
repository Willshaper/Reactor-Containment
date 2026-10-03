package com.cultivate.reactorcontainment.mixins;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cultivate.reactorcontainment.ReactorContainment;
import com.cultivate.reactorcontainment.config.ReactorContainmentConfig;
import com.cultivate.reactorcontainment.config.ReactorContainmentConfig.Mode;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import ic2.core.ExplosionIC2;
import ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric;
import ic2.core.util.Util;

/**
 * Server-side reactor meltdown handling.
 *
 * <p>IC2 calls {@code calculateHeatEffects()} once per reactor tick, right after the components have
 * run. Above 40% heat it ignites, evaporates and melts nearby blocks and hurts nearby entities; at
 * 100% it calls {@code explode()}, which wipes the components, sets the chambers and core to air and
 * then fires an {@link ExplosionIC2}. VOID keeps everything except that explosion; CAP clamps the heat
 * first so 100% is never reached.</p>
 */
@Mixin(value = TileEntityNuclearReactorElectric.class, remap = false)
public abstract class MixinTileEntityNuclearReactorElectric {

    @Shadow
    public int heat;

    @Shadow
    public int maxHeat;

    @Inject(method = "calculateHeatEffects", at = @At("HEAD"))
    private void reactorcontainment$capHeat(CallbackInfoReturnable<Boolean> cir) {
        if (ReactorContainmentConfig.mode != Mode.CAP) {
            return;
        }
        int cap = (int) ((long) maxHeat * ReactorContainmentConfig.capPercent / 100);
        if (heat > cap) {
            heat = cap;
        }
    }

    /**
     * IC2 skips meltdowns and all heat effects when {@code reactorExplosionPowerLimit <= 0}. In VOID/CAP
     * this mod decides what happens, so treat a disabled limit as enabled.
     */
    @ModifyExpressionValue(
        method = "calculateHeatEffects",
        at = @At(
            value = "INVOKE",
            target = "Lic2/core/util/ConfigUtil;getFloat(Lic2/core/util/Config;Ljava/lang/String;)F"))
    private float reactorcontainment$ignoreDisabledLimit(float limit) {
        return ReactorContainmentConfig.mode != Mode.VANILLA && limit <= 0 ? 1.0F : limit;
    }

    @WrapOperation(
        method = "calculateHeatEffects",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z",
            remap = true))
    private boolean reactorcontainment$heatSetBlock(World world, int x, int y, int z, Block block, int meta,
        int flags, Operation<Boolean> original) {
        return blockEffectsAllowed() && original.call(world, x, y, z, block, meta, flags);
    }

    @WrapOperation(
        method = "calculateHeatEffects",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockToAir(III)Z", remap = true))
    private boolean reactorcontainment$heatSetBlockToAir(World world, int x, int y, int z,
        Operation<Boolean> original) {
        return blockEffectsAllowed() && original.call(world, x, y, z);
    }

    @WrapOperation(
        method = "calculateHeatEffects",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z",
            remap = true))
    private boolean reactorcontainment$heatDamage(Entity entity, DamageSource source, float amount,
        Operation<Boolean> original) {
        boolean allowed = ReactorContainmentConfig.mode == Mode.VANILLA || ReactorContainmentConfig.heatEntityDamage;
        return allowed && original.call(entity, source, amount);
    }

    /**
     * By the time the explosion runs, {@code explode()} has already deleted the components, chambers and
     * core. Skipping it leaves exactly that: the reactor is voided and nothing around it is touched.
     * Wrapping here (not in calculateHeatEffects) also covers other mods calling {@code IReactor.explode()}.
     */
    @WrapOperation(method = "explode", at = @At(value = "INVOKE", target = "Lic2/core/ExplosionIC2;doExplosion()V"))
    private void reactorcontainment$voidInsteadOfExplode(ExplosionIC2 explosion, Operation<Void> original) {
        if (ReactorContainmentConfig.mode == Mode.VANILLA) {
            original.call(explosion);
            return;
        }
        ReactorContainment.LOG.info(
            "Nuclear reactor at {} reached max heat and was voided (explosion suppressed)",
            Util.formatPosition((TileEntity) (Object) this));
    }

    private static boolean blockEffectsAllowed() {
        return ReactorContainmentConfig.mode == Mode.VANILLA || ReactorContainmentConfig.heatBlockEffects;
    }
}
