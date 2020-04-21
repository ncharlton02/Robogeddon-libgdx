package com.noahcharlton.robogeddon.block.portal;

import com.noahcharlton.robogeddon.block.tileentity.HasInventory;
import com.noahcharlton.robogeddon.block.tileentity.ItemBuffer;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Item;

public class InventoryPortalTileEntity extends TileEntity implements HasInventory {

    public InventoryPortalTileEntity(Tile rootTile) {
        super(rootTile);
    }

    @Override
    public boolean acceptItem(Item item) {
        ((ServerWorld) world).getInventory().changeItem(item, 1);
        return true;
    }

    @Override
    public void setBuffers(ItemBuffer[] buffers) {

    }

    @Override
    public Item retrieveItem(boolean simulate) {
        return null;
    }

    @Override
    public ItemBuffer[] getBuffers() {
        return new ItemBuffer[0];
    }
}