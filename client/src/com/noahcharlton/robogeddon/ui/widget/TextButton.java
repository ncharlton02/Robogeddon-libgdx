package com.noahcharlton.robogeddon.ui.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.ui.Scale;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.background.NinePatchBackground;

public class TextButton extends Button {


    /**
     * The distance from the side of the texture to the text
     */
    private static final int sidePadding = 11;
    private static final int topPadding = 9;
    private static final int defaultHeight = 30;

    private BitmapFont font;
    private Color textColor = Color.WHITE;
    private String text;
    private float textHeight;

    public TextButton(String text) {
        this.text = text;
        this.font = UIAssets.smallFont;

        setDefaultBackground(new NinePatchBackground(UIAssets.button));
        setOnHover(new NinePatchBackground(UIAssets.buttonHover));
    }

    @Override
    public void draw(SpriteBatch batch) {
        var x = getX() + sidePadding;
        var y = getY() + (getHeight() / 2f) + (textHeight / 2f);
        var textWidth = getWidth() - (sidePadding * 2);

        font.getData().setScale(Scale.scale);
        font.setColor(Color.WHITE);
        font.draw(batch, text, x, y, textWidth, Align.center, false);
    }

    @Override
    public void layout() {
        font.getData().setScale(Scale.scale);
        var textLayout = new GlyphLayout(font, text);

        textHeight = textLayout.height;
        setMinWidth(textLayout.width + (2 * sidePadding));
        setMinHeight(textLayout.height + (2 * topPadding));
    }

    public TextButton setTextColor(Color textColor) {
        this.textColor = textColor;
        return this;
    }

    public Color getTextColor() {
        return textColor;
    }

    public TextButton setFont(BitmapFont font) {
        invalidate();
        this.font = font;
        return this;
    }

    public BitmapFont getFont() {
        return font;
    }

    public TextButton setText(String text) {
        invalidate();
        this.text = text;

        return this;
    }

    public String getText() {
        return text;
    }
}