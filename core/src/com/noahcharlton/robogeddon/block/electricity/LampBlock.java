package com.noahcharlton.robogeddon.block.electricity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.BlockRenderer;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.electricity.PoweredTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.world.Tile;

public class LampBlock extends Block implements BlockRenderer, HasTileEntity {

    private TextureRegion off;
    private TextureRegion on;

    public LampBlock(String id) {
        super(id);
    }

    @Override
    public void initRenderer() {
        this.renderer = this;

        Core.assets.registerTexture("blocks/lamp_off").setOnLoad(t -> off = t);
        Core.assets.registerTexture("blocks/lamp_on").setOnLoad(t -> on = t);
    }

    @Override
    public String getDisplayName() {
        return "Lamp";
    }

    @Override
    public void render(SpriteBatch batch, Tile tile) {
        PoweredTileEntity tileEntity = (PoweredTileEntity) tile.getTileEntity();
        var texture = tileEntity.hasPower() ? on : off;

        batch.draw(texture, tile.getPixelX(), tile.getPixelY());
    }

    @Override
    public void buildRender(SpriteBatch batch, Tile tile) {
        batch.setColor(1f, 1f, 1f, .5f);
        var texture = System.currentTimeMillis() % 1500 > 750 ? off : on;
        batch.draw(texture, tile.getPixelX(), tile.getPixelY());
        batch.setColor(Color.WHITE);
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new LampTileEntity(tile, .1f);
    }

    static class LampTileEntity extends PoweredTileEntity {

        LampTileEntity(Tile rootTile, float usageRate) {
            super(rootTile, usageRate);
        }

        @Override
        public void update() {
            super.update();

            if(world.isServer())
                usePower();
        }
    }
}