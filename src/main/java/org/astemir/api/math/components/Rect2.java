package org.astemir.api.math.components;

public class Rect2 {

    private Vector2 position = new Vector2(0,0);
    private Vector2 size = new Vector2(0,0);

    public Rect2(Vector2 position, Vector2 size) {
        this.position = position;
        this.size = size;
    }

    public Rect2(float x,float y,float width,float height) {
        this(new Vector2(x,y),new Vector2(width,height));
    }

    public boolean contains(Vector2 point){
        return (point.x >= getX() && point.x <= getX()+getWidth()) && (point.y >= getY() && point.y <= getY()+getHeight());
    }

    public boolean contains(float x,float y){
        return contains(new Vector2(x,y));
    }

    public float getX(){
        return position.x;
    }

    public float getY(){
        return position.y;
    }

    public void setX(float x){
        this.position.setX(x);
    }

    public void setY(float y){
        this.position.setY(y);
    }

    public float getWidth(){
        return size.x;
    }

    public float getHeight(){
        return size.y;
    }

    public void setSize(float width,float height){
        this.size = new Vector2(width,height);
    }

    public void setWidth(float width){
        this.size.setX(width);
    }

    public void setHeight(float height){
        this.size.setY(height);
    }

    public void setPosition(float x,float y){
        this.position = new Vector2(x,y);
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
    }
}
