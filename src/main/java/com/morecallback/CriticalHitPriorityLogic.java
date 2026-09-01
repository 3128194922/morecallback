package com.morecallback;

public final class CriticalHitPriorityLogic {
    private CriticalHitPriorityLogic() {
    }

    public static boolean shouldSkipApothic(boolean hasApothicCriticalChance, CriticalHitPriority priority) {
        return hasApothicCriticalChance && priority == CriticalHitPriority.VANILLA;
    }

    public static boolean shouldSuppressVanilla(boolean hasApothicCriticalChance, CriticalHitPriority priority) {
        return hasApothicCriticalChance && priority == CriticalHitPriority.APOTHIC;
    }
}
