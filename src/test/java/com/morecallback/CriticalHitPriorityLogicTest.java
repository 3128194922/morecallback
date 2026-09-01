package com.morecallback;

public final class CriticalHitPriorityLogicTest {
    public static void main(String[] args) {
        assertTrue(CriticalHitPriorityLogic.shouldSkipApothic(true, CriticalHitPriority.VANILLA));
        assertFalse(CriticalHitPriorityLogic.shouldSkipApothic(true, CriticalHitPriority.APOTHIC));
        assertFalse(CriticalHitPriorityLogic.shouldSkipApothic(false, CriticalHitPriority.VANILLA));

        assertTrue(CriticalHitPriorityLogic.shouldSuppressVanilla(true, CriticalHitPriority.APOTHIC));
        assertFalse(CriticalHitPriorityLogic.shouldSuppressVanilla(true, CriticalHitPriority.VANILLA));
        assertFalse(CriticalHitPriorityLogic.shouldSuppressVanilla(false, CriticalHitPriority.APOTHIC));
    }

    private static void assertTrue(boolean value) {
        if (!value) {
            throw new AssertionError("expected true");
        }
    }

    private static void assertFalse(boolean value) {
        if (value) {
            throw new AssertionError("expected false");
        }
    }
}
