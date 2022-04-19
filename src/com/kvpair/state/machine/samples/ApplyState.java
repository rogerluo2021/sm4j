package com.kvpair.state.machine.samples;

import com.kvpair.state.machine.core.State;

/**
 * @author Houfeng Luo
 * @since 1.0.0
 */
public enum ApplyState implements State {
    UN_COMMIT,
    TO_AUDIT,
    FALLBACK,
    REJECTED,
    PASSED;

    @Override
    public String getValue() {
        return this.name();
    }

}
