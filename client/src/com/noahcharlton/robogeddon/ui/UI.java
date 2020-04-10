package com.noahcharlton.robogeddon.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.client.GameClient;
import com.noahcharlton.robogeddon.graphics.LoadingScreen;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.Widget;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class UI {

    private final ScreenViewport screenViewport = new ScreenViewport();
    private final SpriteBatch batch = new SpriteBatch();
    private final ShapeDrawer drawer;
    private final GameClient client;
    private Scene currentScene;

    public UI(GameClient client) {
        this.client = client;

        drawer = new ShapeDrawer(batch, createDot());
        Widget.setShapeDrawer(drawer);
    }

    private TextureRegion createDot() {
        Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGB565);

        map.setColor(Color.WHITE);
        map.drawPixel(0, 0);

        return new TextureRegion(new Texture(map));
    }

    public void render(){
        var matrix = screenViewport.getCamera().projection;
        matrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setProjectionMatrix(matrix);

        batch.begin();
        if(currentScene != null)
            currentScene.render(batch);

        batch.end();

        updateScale();
    }

    private void updateScale() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            setScale(Scale.scale + .125f);
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            setScale(Scale.scale - .125f);
        }
    }

    public void setScale(float scale){
        Log.info("Set UI scale to: " + scale);
        Scale.scale = scale;

        if(currentScene != null)
            currentScene.invalidate();
    }

    public void resize(int width, int height){
        Log.debug("Resize UI!");
        screenViewport.update(width, height);

        if(currentScene != null)
            currentScene.onResize(width, height);
    }

    public void renderLoadingScreen() {
        var matrix = screenViewport.getCamera().projection;
        matrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setProjectionMatrix(matrix);

        batch.begin();
        LoadingScreen.render(batch, drawer);
        batch.end();
    }

    public void setScene(Scene scene){
        this.currentScene = scene;
        this.currentScene.onResize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public boolean isMouseOver() {
        if(currentScene == null)
            return false;

        return currentScene.isMouseOver();
    }

    public void onClick(ClickEvent clickEvent) {
        currentScene.handleClick(clickEvent);
    }

    public Scene getCurrentScene() {
        return currentScene;
    }
}