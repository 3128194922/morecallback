package com.morecallback.event;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

public class CanBeAffectedEvent extends Event {
    private final LivingEntity entity;
    private final MobEffectInstance effectInstance;
    private final boolean originalAllowed;
    private boolean forceAllowed;

    public CanBeAffectedEvent(LivingEntity entity, MobEffectInstance effectInstance, boolean originalAllowed) {
        this.entity = entity;
        this.effectInstance = effectInstance;
        this.originalAllowed = originalAllowed;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public MobEffectInstance getEffectInstance() {
        return effectInstance;
    }

    public boolean isOriginalAllowed() {
        return originalAllowed;
    }

    public boolean isForceAllowed() {
        return forceAllowed;
    }

    public void setForceAllowed(boolean forceAllowed) {
        this.forceAllowed = forceAllowed;
    }

    public String getEntityTypeId() {
        return BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString();
    }

    public String getEffectId() {
        return BuiltInRegistries.MOB_EFFECT.getKey(effectInstance.getEffect()).toString();
    }
}
