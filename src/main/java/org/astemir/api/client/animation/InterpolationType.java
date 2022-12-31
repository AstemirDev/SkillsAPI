package org.astemir.api.client.animation;

import org.astemir.api.math.Vector3;
import org.astemir.api.utils.AnimationUtils;

public abstract class InterpolationType {

    public static final InterpolationType LINEAR = new InterpolationType() {
        @Override
        public Vector3 interpolate(AnimationFrame[] frames,float animationTick) {
            return AnimationUtils.interpolatePoints(frames, animationTick);
        }
    };

    public static final InterpolationType CATMULLROM = new InterpolationType() {
        @Override
        public Vector3 interpolate(AnimationFrame[] frames, float animationTick) {
            return AnimationUtils.interpolatePointsCatmullRom(frames, animationTick);
        }
    };

    public abstract Vector3 interpolate(AnimationFrame[] frames, float animationTick);
}
