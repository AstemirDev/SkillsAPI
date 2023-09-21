package org.astemir.api.math.components;


public class Transform2D {

    private Vector2 rotation = new Vector2(0,0);
    private Vector2 position = new Vector2(0,0);
    private Vector2 scale = new Vector2(1,1);
    public Transform2D(Vector2 rotation, Vector2 position, Vector2 scale) {
        this.rotation = rotation;
        this.position = position;
        this.scale = scale;
    }

    public Vector2 getRotation() {
        return rotation;
    }

    public void setRotation(Vector2 rotation) {
        this.rotation = rotation;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getScale() {
        return scale;
    }

    public void setScale(Vector2 scale) {
        this.scale = scale;
    }
}
