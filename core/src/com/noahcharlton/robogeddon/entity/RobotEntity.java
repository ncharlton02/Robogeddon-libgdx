package com.noahcharlton.robogeddon.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.Server;
import com.noahcharlton.robogeddon.block.Mineable;
import com.noahcharlton.robogeddon.entity.collision.HasCollision;
import com.noahcharlton.robogeddon.util.GraphicsUtil;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.AssignRobotMessage;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.World;
import com.noahcharlton.robogeddon.world.item.ItemStack;
import com.noahcharlton.robogeddon.world.team.Team;

import java.util.Objects;

public class RobotEntity extends Entity implements HasCollision {

    private static final int RADIUS = RobotEntityType.RADIUS;
    private static final float MAX_VELOCITY = 14;
    private static final float MAX_ANGULAR_VELOCITY = .1f;
    private static final int LASER_TIME = 120;
    private static final int SHOOT_TIME = 20;
    private static final int SHOOT_OFFSET = RADIUS;

    private boolean controlling = false;

    private boolean wKey;
    private boolean aKey;
    private boolean sKey;
    private boolean dKey;
    private boolean spaceKey;

    private Vector3 miningPos;
    @Side(Side.SERVER)
    private int laserTime = LASER_TIME;
    @Side(Side.SERVER)
    private int shootTime = SHOOT_TIME;

    public RobotEntity(World world, Team team) {
        super(EntityType.robotEntity, world, team);

        angle = (float) (Math.PI / 2);
    }

    @Override
    public void update() {
        if(world.isClient() && controlling){
            sendInputValues();
        }else if(world.isServer()){
            updateShooter();

            if(miningPos != null){
                updateMining();
            }
        }

        updateControls();
        trimVelocity();
    }

    @Side(Side.SERVER)
    private void updateShooter(){
        if(shootTime > 0){
            shootTime--;
        }

        if(spaceKey && shootTime <= 0){
            shoot();
            shootTime = SHOOT_TIME;
        }
    }

    @Side(Side.SERVER)
    private void shoot() {
        ServerWorld world = (ServerWorld) this.world;
        BulletEntity bullet = (BulletEntity) EntityType.bulletEntity.create(world, team);

        bullet.setX((float) (getX() + (SHOOT_OFFSET * Math.cos(angle))));
        bullet.setY((float) (getY() + (SHOOT_OFFSET * Math.sin(angle))));
        bullet.setAngle(angle);

        Server.runLater(() -> world.addEntity(bullet));
    }


    @Side(Side.SERVER)
    private void updateMining() {
        laserTime--;

        if(laserTime <= 0){
            onMine();
            laserTime = LASER_TIME;
        }
    }

    @Side(Side.SERVER)
    private void onMine() {
        Tile tile = world.tileFromPixel(miningPos);

        if(tile == null){
            Log.warn("Mined null tile???");
            return;
        }

        if(tile.getBlock() instanceof Mineable){
            ItemStack items = ((Mineable) tile.getBlock()).onMine();
            var serverWorld = (ServerWorld) world;

            serverWorld.getInventory().changeItem(items);
        }
    }

    @Override
    public float getRadius() {
        return RADIUS;
    }

    private void trimVelocity() {
        if(velocity > MAX_VELOCITY){
            velocity = MAX_VELOCITY;
        }else if(velocity < 0){
            velocity = 0;
        }

        if(Math.abs(angularVelocity) > MAX_ANGULAR_VELOCITY){
            angularVelocity = Math.signum(angularVelocity) * MAX_ANGULAR_VELOCITY;
        }
    }

    private void updateControls() {
        if(wKey){
            velocity += .5f;
        }else if(sKey){
            velocity -= 1.2f;
        }else{
            velocity -= .25f;
        }

        if(dKey){
            angularVelocity += -.01f;
        }else if(aKey){
            angularVelocity += .05f;
        }else{
            angularVelocity *= .85;
        }
    }

    @Side(Side.CLIENT)
    private void sendInputValues() {
        boolean w = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean a = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean s = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean d = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean space = Gdx.input.isKeyPressed(Input.Keys.SPACE);
        boolean rightClicking = Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && !Core.client.isMouseOnUI();
        Vector3 currMining = rightClicking ? trimMiningPosition(Core.client.mouseToWorld()) : null;

        if(wKey != w || a != aKey || s != sKey || d != dKey || space != spaceKey ||
                !Objects.equals(miningPos, currMining)){
            var message = new RobotInputMessage(getId(), w, a, s, d, space, currMining);
            wKey = w;
            aKey = a;
            sKey = s;
            dKey = d;
            spaceKey = space;
            miningPos = currMining;

            world.sendMessageToServer(message);
            Log.trace("Sending robot controls to server!");
        }
    }

    private Vector3 trimMiningPosition(Vector3 mouse) {
        if(mouse == null)
            return null;

        return mouse.sub(getX(), getY(), 0).clamp(0, 250).add(getX(), getY(), 0);
    }

    public void setControlling(boolean controlling) {
        this.controlling = controlling;
    }

    @Override
    public void onCustomMessageReceived(CustomEntityMessage message) {
        if(message instanceof RobotInputMessage && world.isServer()){
            var input = (RobotInputMessage) message;
            var clientMiningPos = trimMiningPosition(input.miningPos); //Limit the length in case client is bad
            var clientMiningTile = world.tileFromPixel(clientMiningPos);

            if(clientMiningTile == null || !clientMiningTile.equals(world.tileFromPixel(miningPos))){
                laserTime = LASER_TIME;
            }

            wKey = input.wKey;
            aKey = input.aKey;
            sKey = input.sKey;
            dKey = input.dKey;
            spaceKey = input.spaceKey;
            miningPos = clientMiningPos;
            Log.trace("Updated robot controls!");
        }else if(message instanceof AssignRobotMessage){
            Log.debug("Player now controlling robot " + getId());
            controlling = true;
        }else{
            super.onCustomMessageReceived(message);
        }
    }

    public static class RobotEntityType extends EntityType {

        private static final int RADIUS = 32;

        private TextureRegion onTexture;
        private TextureRegion offTexture;

        @Override
        public Entity create(World world, Team team) {
            return new RobotEntity(world, team);
        }

        @Override
        public void initRenderer() {
            Core.assets.registerTexture("entities/robot_on").setOnLoad(texture -> onTexture = texture);
            Core.assets.registerTexture("entities/robot_off").setOnLoad(texture -> offTexture = texture);
        }

        @Override
        public void render(SpriteBatch batch, Entity entity) {
            if(!(entity instanceof RobotEntity)){
                throw new UnsupportedOperationException();
            }
            var robotEntity = (RobotEntity) entity;
            var texture = robotEntity.wKey ? onTexture : offTexture;
            float angle = (float) (entity.getAngle() * 180 / Math.PI) - 90;
            float x = entity.getX() - RADIUS;
            float y = entity.getY() - RADIUS;

            GraphicsUtil.drawRotated(batch, texture, x, y, angle);
            renderHealthBar(batch, entity, RADIUS);

            if(robotEntity.miningPos != null)
                drawMiningLaser(robotEntity, robotEntity.miningPos);
        }

        private void drawMiningLaser(RobotEntity entity, Vector3 position) {
            var drawer = Core.client.getGameShapeDrawer();
            drawer.setColor(Color.YELLOW);
            drawer.line(entity.x, entity.y, position.x, position.y, 3);
            drawer.setColor(Color.RED);
            drawer.filledCircle(position.x, position.y, 4);
        }

        @Override
        public String getTypeID() {
            return "EntityRobot";
        }

        @Override
        public int getHealth() {
            return 50;
        }
    }

    public static class RobotInputMessage extends CustomEntityMessage{

        final Vector3 miningPos;
        final boolean wKey;
        final boolean aKey;
        final boolean sKey;
        final boolean dKey;
        final boolean spaceKey;

        public RobotInputMessage(int ID, boolean wKey, boolean aKey, boolean sKey, boolean dKey, boolean spaceKey,
                                 Vector3 miningPos) {
            super(ID);
            this.wKey = wKey;
            this.aKey = aKey;
            this.sKey = sKey;
            this.dKey = dKey;
            this.spaceKey = spaceKey;
            this.miningPos = miningPos;
        }
    }
}