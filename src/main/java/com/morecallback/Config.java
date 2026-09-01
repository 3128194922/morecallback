package com.morecallback;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = MoreCallback.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.EnumValue<CriticalHitPriority> CRITICAL_HIT_PRIORITY = BUILDER
            .comment(
                    "Which critical-hit system has priority when Apothic Attributes is installed.",
                    "Apothic-Attributes 与原版跳劈同时满足时，哪一种暴击优先。默认原版跳劈优先。"
            )
            .defineEnum("criticalHitPriority", CriticalHitPriority.VANILLA);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static CriticalHitPriority criticalHitPriority = CriticalHitPriority.VANILLA;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        criticalHitPriority = CRITICAL_HIT_PRIORITY.get();
    }
}
