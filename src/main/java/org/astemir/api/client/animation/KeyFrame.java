package org.astemir.api.client.animation;

import org.astemir.api.math.components.Vector3;

public class KeyFrame {

    private Vector3 value;
    private float time;

    public KeyFrame(float time, Vector3 value) {
        this.value = value;
        this.time = time;
    }

    public Vector3 getValue() {
        return value;
    }

    public float getPosition() {
        return time;
    }
}
