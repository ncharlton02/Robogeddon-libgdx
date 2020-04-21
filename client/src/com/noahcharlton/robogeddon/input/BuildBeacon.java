package com.noahcharlton.robogeddon.input;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.team.Team;

public class BuildBeacon extends BuildBlock {

    public BuildBeacon(Block block) {
        super(block);
    }

    @Override
    public void onClick(Tile tile, int button) {
        updateBlock();

        super.onClick(tile, button);
    }

    @Override
    public void render(SpriteBatch batch) {
        updateBlock();

        super.render(batch);
    }

    private void updateBlock() {
        var playerRobot = client.getWorld().getPlayersRobot();

        if(playerRobot == null) {
            return;
        }

        if(playerRobot.getTeam() == Team.RED) {
            block = Blocks.redBeacon;
        } else {
            block = Blocks.blueBeacon;
        }
    }
}