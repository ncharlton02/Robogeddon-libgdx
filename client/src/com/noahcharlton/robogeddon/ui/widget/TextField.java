package com.noahcharlton.robogeddon.ui.widget;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.ui.Scale;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.background.NinePatchBackground;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.event.KeyEvent;

public class TextField extends Widget{

    private BitmapFont font = UIAssets.smallFont;
    private String text = "";
    private Color textColor = Color.WHITE;
    private String promptText = "...";
    private int maximumLength = 30;

    private int cursorPos;
    private float textHeight;

    public TextField() {
        setBackground(new NinePatchBackground(UIAssets.textField));
        setMinHeight(30);
        setMinWidth(100);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(SpriteBatch batch) {
        var x = getX();
        var y = getY() + (getHeight() / 2f) + (textHeight / 2f);

        font.getData().setScale(Scale.scale);

        if(this.text.isBlank()){
            font.setColor(Color.GRAY);
            font.draw(batch, promptText, x, y, getWidth(), Align.center, false);
        }else{
            font.setColor(Color.WHITE);
            font.draw(batch, text, x, y, getWidth(), Align.center, false);
        }

        if(client.getUi().getKeyFocus() == this && System.currentTimeMillis() % 1000 > 500)
            drawCursor();
    }

    public void drawCursor() {
        trimCursorPosition();

        var textLayout = new GlyphLayout(font, text);
        var charLayout = new GlyphLayout(font, text.substring(0, cursorPos));
        var x = getX() + (getWidth() / 2f) - (textLayout.width / 2f) + charLayout.width;
        var y = getY() + (getHeight() / 2f) - (textHeight / 2f);

        if(cursorPos == text.length()){ //if at the end of the text field, give a little extra room for easier reading
            x += 5;
        }

        getShapeDrawer().setColor(textColor);
        getShapeDrawer().filledRectangle(x, y - 4, 2, charLayout.height + 8);
    }

    @Override
    public void layout() {
        font.getData().setScale(Scale.scale);
        var textLayout = new GlyphLayout(font, text.isBlank() ? promptText : text);

        textHeight = textLayout.height;
        setMinWidth(textLayout.width + 10);
        setMinHeight(textLayout.height + 10);
    }

    @Override
    protected void onClick(ClickEvent event) {
        event.getUi().setKeyFocus(this);
    }

    @Override
    public void onKeyEvent(KeyEvent keyEvent) {
        if(keyEvent.getCharacter() == KeyEvent.BACKSPACE){
            if(text.length() == 0)
                return;

            text = text.substring(0, cursorPos - 1) + text.substring(cursorPos);
            cursorPos--;
        }else if(keyEvent.getCharacter() == KeyEvent.SPACE){
            onCharacterTyped(" ");
            cursorPos++;
        }else if(keyEvent.getCharacter() >= 33 && keyEvent.getCharacter() <= 122){
            onCharacterTyped(Character.toString(keyEvent.getCharacter()));
            cursorPos++;
        }else if(keyEvent.getKeyCode() == Input.Keys.LEFT){
            cursorPos--;
            trimCursorPosition();
        }else if(keyEvent.getKeyCode() == Input.Keys.RIGHT){
            cursorPos++;
            trimCursorPosition();
        }

        invalidateHierarchy();
        trimToMaxLength();
    }

    private void onCharacterTyped(String s) {
        text = text.substring(0, cursorPos) + s + text.substring(cursorPos);
    }

    private void trimCursorPosition() {
        if(cursorPos > text.length())
            cursorPos = text.length();

        if(cursorPos < 0)
            cursorPos = 0;
    }

    private void trimToMaxLength() {
        if(text.length() > maximumLength){
            text = text.substring(0, maximumLength);
        }
    }

    public TextField setMaximumLength(int maximumLength) {
        this.maximumLength = maximumLength;
        trimToMaxLength();

        return this;
    }

    public TextField setTextColor(Color textColor) {
        this.textColor = textColor;
        return this;
    }

    public Color getTextColor() {
        return textColor;
    }

    public TextField setFont(BitmapFont font) {
        invalidate();
        this.font = font;
        return this;
    }

    public BitmapFont getFont() {
        return font;
    }

    @Override
    public String toString() {
        return "TextField(" + text + ")";
    }

    public TextField setPromptText(String promptText) {
        this.promptText = promptText;

        return this;
    }

    public TextField setText(String text) {
        invalidate();
        this.text = text;

        return this;
    }

    public String getText() {
        return text;
    }
}
