package com.morecallback;

import com.morecallback.event.VanillaCriticalHitEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MoreCallback.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CallbackEvents {
    private CallbackEvents() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void applyCriticalHitPriority(CriticalHitEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide() || !event.isVanillaCritical()) {
            return;
        }

        if (CriticalHitPriorityLogic.shouldSuppressVanilla(
                CriticalHitPriorityController.hasApothicCriticalChance(player),
                Config.criticalHitPriority
        )) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void publishVanillaCriticalHit(CriticalHitEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide() || !event.isVanillaCritical()) {
            return;
        }

        boolean suppressed = event.getResult() == Event.Result.DENY;
        MinecraftForge.EVENT_BUS.post(new VanillaCriticalHitEvent(
                player,
                event.getTarget(),
                event.getDamageModifier(),
                suppressed
        ));

        if (!suppressed && CriticalHitPriorityLogic.shouldSkipApothic(
                CriticalHitPriorityController.hasApothicCriticalChance(player),
                Config.criticalHitPriority
        )) {
            CriticalHitPriorityController.markVanillaCritical(player, event.getTarget());
        }
    }
}
