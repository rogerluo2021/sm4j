package com.kvpair.state.machine.core;

import java.util.function.Function;

/**
 * @author Houfeng Luo
 * @since 1.0.0
 */
public interface StateTransferHandler<T, R> {

    String getKey();

    Function<T, R> getHandler();
    
}
