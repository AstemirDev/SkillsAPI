package org.astemir.api.client.animation;


public class AnimationBone {


    private AnimationFrame[] rotations;
    private AnimationFrame[] scales;
    private AnimationFrame[] positions;

    private String boneName;


    public AnimationBone(String boneName, AnimationFrame[] rotations, AnimationFrame[] scales, AnimationFrame[] positions) {
        this.rotations = rotations;
        this.scales = scales;
        this.positions = positions;
        this.boneName = boneName;
    }

    public AnimationFrame[] getRotations() {
        return rotations;
    }

    public AnimationFrame[] getScales() {
        return scales;
    }

    public AnimationFrame[] getPositions() {
        return positions;
    }


    public AnimationFrame getPosition(float time){
        for (AnimationFrame position : positions) {
            if (position.getPosition() == time){
                return position;
            }
        }
        return null;
    }

    public AnimationFrame getScale(float time){
        for (AnimationFrame scale : scales) {
            if (scale.getPosition() == time){
                return scale;
            }
        }
        return null;
    }

    public AnimationFrame getRotation(float time){
        for (AnimationFrame rotation : rotations) {
            if (rotation.getPosition() == time){
                return rotation;
            }
        }
        return null;
    }


    public String getBoneName() {
        return boneName;
    }
}