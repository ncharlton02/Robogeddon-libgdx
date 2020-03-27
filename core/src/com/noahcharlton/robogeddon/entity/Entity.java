package com.noahcharlton.robogeddon.entity;

import com.noahcharlton.robogeddon.world.World;

import java.util.Objects;

public class Entity {

    public static final int DEFAULT_ID = -1;

    private final EntityType type;
    private final World world;

    private int id = DEFAULT_ID;
    protected float x;
    protected float y;
    protected float angle;
    protected float velocity;
    protected float angularVelocity;

    private boolean isDead;
    private boolean dirty;

    public Entity(EntityType type, World world) {
        this.type = type;
        this.world = world;
    }

    public final void fixedUpdate(){
        update();
        updateKinematics();

        if(dirty && world.isServer())
            sendUpdatedValues();
    }

    private void sendUpdatedValues() {
        var message = new EntityUpdateMessage(id, x, y, angle);

        world.sendMessageToClient(message);
    }

    public void onUpdateMessage(EntityUpdateMessage message) {
        x = message.getX();
        y = message.getY();
        angle = message.getAngle();
    }

    public void update(){}

    public void updateKinematics(){
        if(velocity < .001 && angularVelocity < .001){
            return;
        }

        x += velocity * Math.cos(angle);
        y += velocity * Math.sin(angle);
        angle += angularVelocity;
        dirty = true;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Entity)) return false;
        Entity entity = (Entity) o;
        return getId() == entity.getId() &&
                Objects.equals(getType(), entity.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getId());
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setId(int id) {
        if(this.id != DEFAULT_ID){
            throw new IllegalStateException("This entity already has an ID: " + this.id);
        }
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public EntityType getType() {
        return type;
    }
}
