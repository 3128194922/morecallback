package com.morecallback.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class VanillaCriticalHitEvent extends Event {
    private final Player player;
    private final Entity target;
    private final float damageModifier;
    private final boolean suppressed;

    public VanillaCriticalHitEvent(Player player, Entity target, float damageModifier, boolean suppressed) {
        this.player = player;
        this.target = target;
        this.damageModifier = damageModifier;
        this.suppressed = suppressed;
    }

    public Player getPlayer() {
        return player;
    }

    public Entity getTarget() {
        return target;
    }

    public float getDamageModifier() {
        return damageModifier;
    }

    public boolean isSuppressed() {
        return suppressed;
    }
}
