package com.noahcharlton.robogeddon.block.electricity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.DefaultBlockRenderer;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.electricity.HasElectricity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.world.Tile;

public class RelayBlock extends Block implements HasTileEntity {

    public RelayBlock(String id) {
        super(id);
    }

    @Override
    public void initRenderer() {
        renderer = new RelayRenderer(this);
    }

    @Override
    public String getDisplayName() {
        return "Relay";
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new RelayTileEntity(tile);
    }

    static class RelayRenderer extends DefaultBlockRenderer{

        public RelayRenderer(Block block) {
            super(block);
        }

        @Override
        public void renderSelected(SpriteBatch batch, Tile tile) {
            var squareRadius = HasElectricity.RELAY_RADIUS * Tile.SIZE;
            var startX = tile.getPixelX() - squareRadius;
            var startY = tile.getPixelY() - squareRadius;
            var shapeRenderer = Core.client.getGameShapeDrawer();
            //Add on one extra tile because relay radius does not count the relay tile itself
            var size = (squareRadius * 2) + Tile.SIZE;

            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rectangle(startX, startY, size, size, 4);
        }
    }
}
