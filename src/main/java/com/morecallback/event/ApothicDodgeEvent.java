package com.morecallback.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

public class ApothicDodgeEvent extends Event {
    private final LivingEntity target;
    private final DamageSource damageSource;
    private final Entity attackEntity;

    public ApothicDodgeEvent(LivingEntity target, DamageSource damageSource, Entity attackEntity) {
        this.target = target;
        this.damageSource = damageSource;
        this.attackEntity = attackEntity;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public DamageSource getDamageSource() {
        return damageSource;
    }

    public Entity getAttackEntity() {
        return attackEntity;
    }

    public boolean isProjectile() {
        return damageSource == null;
    }
}
