package com.morecallback.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

public class ApothicCriticalHitEvent extends Event {
    private final LivingEntity attacker;
    private final LivingEntity target;
    private final DamageSource damageSource;
    private final float damageMultiplier;

    public ApothicCriticalHitEvent(LivingEntity attacker, LivingEntity target, DamageSource damageSource, float damageMultiplier) {
        this.attacker = attacker;
        this.target = target;
        this.damageSource = damageSource;
        this.damageMultiplier = damageMultiplier;
    }

    public LivingEntity getAttacker() {
        return attacker;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public DamageSource getDamageSource() {
        return damageSource;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }
}
