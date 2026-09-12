package com.morecallback;

public final class CanBeAffectedOverrideLogicTest {
    public static void main(String[] args) {
        assertTrue(CanBeAffectedOverrideLogic.resolve(true, false));
        assertTrue(CanBeAffectedOverrideLogic.resolve(false, true));
        assertFalse(CanBeAffectedOverrideLogic.resolve(false, false));
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
