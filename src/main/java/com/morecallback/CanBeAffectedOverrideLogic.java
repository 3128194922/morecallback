package com.morecallback;

public final class CanBeAffectedOverrideLogic {
    private CanBeAffectedOverrideLogic() {
    }

    public static boolean resolve(boolean forceAllowed, boolean originalAllowed) {
        return forceAllowed || originalAllowed;
    }
}
