package org.astemir.api.client.animation;


public class AnimationBone {
    private KeyFrame[] rotations;
    private KeyFrame[] scales;
    private KeyFrame[] positions;
    private String boneName;

    public AnimationBone(String boneName, KeyFrame[] rotations, KeyFrame[] scales, KeyFrame[] positions) {
        this.rotations = rotations;
        this.scales = scales;
        this.positions = positions;
        this.boneName = boneName;
    }

    public KeyFrame[] getRotations() {
        return rotations;
    }

    public KeyFrame[] getScales() {
        return scales;
    }

    public KeyFrame[] getPositions() {
        return positions;
    }


    public KeyFrame getPosition(float time){
        for (KeyFrame position : positions) {
            if (position.getPosition() == time){
                return position;
            }
        }
        return null;
    }

    public KeyFrame getScale(float time){
        for (KeyFrame scale : scales) {
            if (scale.getPosition() == time){
                return scale;
            }
        }
        return null;
    }

    public KeyFrame getRotation(float time){
        for (KeyFrame rotation : rotations) {
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