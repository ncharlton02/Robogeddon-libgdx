package com.noahcharlton.robogeddon.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.util.Direction;
import com.noahcharlton.robogeddon.util.GraphicsUtil;
import com.noahcharlton.robogeddon.world.Tile;

public class BuildItemDuct extends BuildBlock {

    private Direction direction = Direction.NORTH;

    public BuildItemDuct(Block block) {
        super(block);

        syncBlock();
    }

    @Override
    public void onClick(Tile tile, int button) {
        syncBlock();

        super.onClick(tile, button);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if(Gdx.input.isKeyJustPressed(Input.Keys.Q)){
            rotate();
        }

        Vector3 pos = Core.client.mouseToWorld();
        Tile tile = client.getWorld().tileFromPixel(pos);

        if(tile != null) {
            GraphicsUtil.drawRotated(batch, UIAssets.itemDuctArrow, tile.getPixelX(), tile.getPixelY(), getDegrees());
        }
    }

    private void syncBlock() {
        switch(direction){
            case NORTH:
                block = Blocks.itemDuctNorth;
                break;
            case EAST:
                block = Blocks.itemDuctEast;
                break;
            case SOUTH:
                block = Blocks.itemDuctSouth;
                break;
            case WEST:
                block = Blocks.itemDuctWest;
                break;
            default:
                throw new RuntimeException();
        }
    }

    private void rotate() {
        switch(direction){
            case NORTH:
                direction = Direction.EAST;
                break;
            case EAST:
                direction = Direction.SOUTH;
                break;
            case SOUTH:
                direction = Direction.WEST;
                break;
            case WEST:
                direction = Direction.NORTH;
                break;
            default:
                throw new RuntimeException();
        }

        syncBlock();
    }

    private float getDegrees() {
        switch(direction){
            case NORTH:
                return 90;
            case EAST:
                return 0;
            case SOUTH:
                return 270;
            case WEST:
                return 180;
            default:
                throw new RuntimeException();
        }
    }
}