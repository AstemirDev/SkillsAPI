package org.astemir.api.common.handler;

import org.astemir.api.common.misc.ValueHolder;

public class CustomEvent extends ValueHolder<Integer> {
    public static int UNIQUE_ID_IDENTIFIER = 1000;

    public CustomEvent() {
        super(UNIQUE_ID_IDENTIFIER);
        UNIQUE_ID_IDENTIFIER++;
    }
}
