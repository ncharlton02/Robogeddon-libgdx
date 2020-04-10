package com.noahcharlton.robogeddon.ui.background;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.ui.widget.Widget;

public class NinePatchBackground implements Background {

    private final NinePatch texture;

    public NinePatchBackground(NinePatch texture) {
        this.texture = texture;
    }

    @Override
    public void draw(SpriteBatch batch, Widget widget) {
        texture.draw(batch, widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
    }
}