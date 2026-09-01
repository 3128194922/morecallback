package com.morecallback;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.registries.ForgeRegistries;

public final class CriticalHitPriorityController {
    private static final ResourceLocation APOTHIC_CRIT_CHANCE = new ResourceLocation("attributeslib", "crit_chance");
    private static final ThreadLocal<VanillaCriticalAttack> VANILLA_CRITICAL_ATTACK = new ThreadLocal<>();

    private CriticalHitPriorityController() {
    }

    public static boolean hasApothicCriticalChance(LivingEntity entity) {
        if (entity == null) {
            return false;
        }

        var attribute = ForgeRegistries.ATTRIBUTES.getValue(APOTHIC_CRIT_CHANCE);
        return attribute != null
                && entity.getAttribute(attribute) != null
                && entity.getAttributeValue(attribute) > 0.0D;
    }

    public static void markVanillaCritical(Player player, Entity target) {
        VANILLA_CRITICAL_ATTACK.set(new VanillaCriticalAttack(player, target));
    }

    public static boolean consumeVanillaCriticalForApothic(LivingHurtEvent event) {
        VanillaCriticalAttack attack = VANILLA_CRITICAL_ATTACK.get();
        VANILLA_CRITICAL_ATTACK.remove();

        return attack != null
                && attack.player() == event.getSource().getEntity()
                && attack.target() == event.getEntity();
    }

    private record VanillaCriticalAttack(Player player, Entity target) {
    }
}
