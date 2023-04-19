package org.astemir.api.math.components;

public class Line2 {

    private Vector2 origin;
    private Vector2 position;

    public Line2(Vector2 origin, Vector2 position) {
        this.origin = origin;
        this.position = position;
    }

    public Line2(float x1,float y1,float x2,float y2) {
        this.origin = new Vector2(x1,y1);
        this.position = new Vector2(x2,y2);
    }

    public Vector2 direction(){
        return origin.direction(position);
    }

    public void setPositionX(float x){
        this.position.setX(x);
    }

    public void setPositionY(float y){
        this.position.setY(y);
    }

    public void setOriginX(float x){
        this.origin.setX(x);
    }

    public void setOriginY(float y){
        this.origin.setY(y);
    }

    public float getOriginX(){
        return origin.x;
    }

    public float getOriginY(){
        return origin.y;
    }

    public float getPositionX(){
        return position.x;
    }

    public float getPositionY(){
        return position.y;
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2 origin) {
        this.origin = origin;
    }

    public void setOrigin(float x,float y) {
        this.origin = new Vector2(x,y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setPosition(float x,float y) {
        this.position = new Vector2(x,y);
    }
}
