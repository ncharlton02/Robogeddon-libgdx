package com.noahcharlton.robogeddon.block.miner;

import com.noahcharlton.robogeddon.block.tileentity.GenericItemBuffer;
import com.noahcharlton.robogeddon.block.tileentity.StorageTileEntity;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Items;

public class MinerTileEntity extends StorageTileEntity {

    private static final int TIME = 45;
    private int time = TIME;

    public MinerTileEntity(Tile rootTile) {
        super(rootTile, new GenericItemBuffer(300));
    }

    @Override
    public void update() {
        if(world.isServer()){
            time--;

            if(time <= 0 && acceptItem(Items.rock)){
                time = TIME;
                dirty = true;
            }
        }
    }
}