package com.morecallback.mixin;

import com.morecallback.CanBeAffectedOverrideLogic;
import com.morecallback.event.CanBeAffectedEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityEffectMixin {
    @Redirect(
            method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;canBeAffected(Lnet/minecraft/world/effect/MobEffectInstance;)Z"
            )
    )
    private boolean morecallback$publishCanBeAffected(LivingEntity entity, MobEffectInstance effectInstance) {
        boolean originalAllowed = entity.canBeAffected(effectInstance);
        CanBeAffectedEvent event = new CanBeAffectedEvent(entity, effectInstance, originalAllowed);
        MinecraftForge.EVENT_BUS.post(event);
        return CanBeAffectedOverrideLogic.resolve(event.isForceAllowed(), originalAllowed);
    }
}
