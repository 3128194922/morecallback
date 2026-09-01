package com.morecallback.mixin;

import com.morecallback.Config;
import com.morecallback.CriticalHitPriorityController;
import com.morecallback.CriticalHitPriority;
import com.morecallback.CriticalHitPriorityLogic;
import com.morecallback.event.ApothicCriticalHitEvent;
import com.morecallback.event.ApothicDodgeEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "dev.shadowsoffire.attributeslib.impl.AttributeEvents", remap = false)
public abstract class ApothicAttributeEventsMixin {
    private static final ThreadLocal<DodgeContext> DODGE_CONTEXT = new ThreadLocal<>();

    @Inject(method = "apothCriticalStrike", at = @At("HEAD"), cancellable = true, require = 0)
    private void morecallback$skipApothicCritical(LivingHurtEvent event, CallbackInfo callbackInfo) {
        if (Config.criticalHitPriority == CriticalHitPriority.VANILLA
                && CriticalHitPriorityLogic.shouldSkipApothic(
                CriticalHitPriorityController.hasApothicCriticalChance(event.getSource().getEntity() instanceof LivingEntity living ? living : null),
                Config.criticalHitPriority
        )
                && CriticalHitPriorityController.consumeVanillaCriticalForApothic(event)) {
            callbackInfo.cancel();
        }
    }

    @Redirect(
            method = "apothCriticalStrike",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/event/entity/living/LivingHurtEvent;setAmount(F)V"
            ),
            require = 0
    )
    private void morecallback$publishApothicCritical(LivingHurtEvent event, float amount) {
        LivingEntity attacker = event.getSource().getEntity() instanceof LivingEntity living ? living : null;
        float originalAmount = event.getAmount();
        float damageMultiplier = originalAmount > 0.0F ? amount / originalAmount : 1.0F;
        if (attacker != null && !attacker.level().isClientSide() && damageMultiplier > 1.0F) {
            MinecraftForge.EVENT_BUS.post(new ApothicCriticalHitEvent(
                    attacker,
                    event.getEntity(),
                    event.getSource(),
                    damageMultiplier
            ));
        }
        event.setAmount(amount);
    }

    @Inject(method = "dodge(Lnet/minecraftforge/event/entity/living/LivingAttackEvent;)V", at = @At("HEAD"), require = 0)
    private void morecallback$captureMeleeDodge(LivingAttackEvent event, CallbackInfo callbackInfo) {
        DODGE_CONTEXT.set(new DodgeContext(event.getSource(), event.getSource().getDirectEntity()));
    }

    @Inject(method = "dodge(Lnet/minecraftforge/event/entity/living/LivingAttackEvent;)V", at = @At("TAIL"), require = 0)
    private void morecallback$clearMeleeDodge(LivingAttackEvent event, CallbackInfo callbackInfo) {
        DODGE_CONTEXT.remove();
    }

    @Inject(method = "dodge(Lnet/minecraftforge/event/entity/ProjectileImpactEvent;)V", at = @At("HEAD"), require = 0)
    private void morecallback$captureProjectileDodge(ProjectileImpactEvent event, CallbackInfo callbackInfo) {
        DODGE_CONTEXT.set(new DodgeContext(null, event.getProjectile()));
    }

    @Inject(method = "dodge(Lnet/minecraftforge/event/entity/ProjectileImpactEvent;)V", at = @At("TAIL"), require = 0)
    private void morecallback$clearProjectileDodge(ProjectileImpactEvent event, CallbackInfo callbackInfo) {
        DODGE_CONTEXT.remove();
    }

    @Inject(method = "onDodge", at = @At("HEAD"), require = 0)
    private void morecallback$publishApothicDodge(LivingEntity target, CallbackInfo callbackInfo) {
        DodgeContext context = DODGE_CONTEXT.get();
        if (context != null && !target.level().isClientSide()) {
            MinecraftForge.EVENT_BUS.post(new ApothicDodgeEvent(target, context.damageSource(), context.attackEntity()));
        }
    }

    private record DodgeContext(DamageSource damageSource, Entity attackEntity) {
    }
}
