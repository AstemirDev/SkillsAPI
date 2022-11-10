package org.astemir.api.math;

public class Transform {

    private Vector3 rotation = new Vector3(0,0,0);
    private Vector3 position = new Vector3(0,0,0);
    private Vector3 scale = new Vector3(1,1,1);

    public Transform(Vector3 rotation,Vector3 position, Vector3 scale) {
        this.rotation = rotation;
        this.position = position;
        this.scale = scale;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getScale() {
        return scale;
    }

    public void setScale(Vector3 scale) {
        this.scale = scale;
    }

    public Vector3 getRotation() {
        return rotation;
    }

    public void setRotation(Vector3 rotation) {
        this.rotation = rotation;
    }
}
