package org.astemir.api.math.components;

public class Rect3 {

    private Vector3 position = new Vector3(0,0,0);
    private Vector3 size = new Vector3(0,0,0);

    public Rect3(Vector3 position, Vector3 size) {
        this.position = position;
        this.size = size;
    }

    public Rect3(float x, float y,float z, float width, float height,float length) {
        this(new Vector3(x,y,z),new Vector3(width,height,length));
    }

    public boolean contains(Vector3 point){
        return (point.x >= getX() && point.x <= getX()+getWidth()) && (point.y >= getY() && point.y <= getY()+getHeight()) && (point.z >= getZ() && point.z <= getZ()+getLength());
    }

    public boolean contains(float x,float y,float z){
        return contains(new Vector3(x,y,z));
    }

    public float getX(){
        return position.x;
    }

    public float getY(){
        return position.y;
    }

    public float getZ(){
        return position.z;
    }

    public void setX(float x){
        this.position.setX(x);
    }

    public void setY(float y){
        this.position.setY(y);
    }

    public void setZ(float z){
        this.position.setZ(z);
    }

    public float getWidth(){
        return size.x;
    }

    public float getHeight(){
        return size.y;
    }

    public float getLength(){
        return size.z;
    }

    public void setSize(float width,float height,float length){
        this.size = new Vector3(width,height,length);
    }

    public void setWidth(float width){
        this.size.setX(width);
    }

    public void setHeight(float height){
        this.size.setY(height);
    }

    public void setLength(float length){
        this.size.setZ(length);
    }

    public void setPosition(float x,float y,float z){
        this.position = new Vector3(x,y,z);
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public void setSize(Vector3 size) {
        this.size = size;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getSize() {
        return size;
    }
}
