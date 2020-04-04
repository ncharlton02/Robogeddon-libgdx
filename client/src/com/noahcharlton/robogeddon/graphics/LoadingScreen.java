package com.noahcharlton.robogeddon.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.Core;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * Not actually a part of the UI
 */
public class LoadingScreen {

    public static void render(SpriteBatch batch, ShapeDrawer drawer) {
        float width = Gdx.graphics.getWidth() * .6f;
        float innerWidth = (width - 8) * Core.assets.getPercentDone();
        float height = 40f;
        float x = (Gdx.graphics.getWidth() / 2f) - (width / 2f);
        float y = Gdx.graphics.getHeight() / 4f;

        drawer.setColor(Color.WHITE);
        drawer.filledRectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        drawer.setColor(Color.BLACK);
        drawer.filledRectangle(x, y, width, height);
        drawer.setColor(Color.FIREBRICK);
        drawer.filledRectangle(x + 4, y + 4, innerWidth, height - 8);
    }
}
