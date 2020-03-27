package com.noahcharlton.robogeddon.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.noahcharlton.robogeddon.client.GameClient;

public class GameRenderer {

    private final OrthographicCamera camera = new OrthographicCamera();
    private final ScreenViewport viewport = new ScreenViewport(camera);
    private final SpriteBatch batch = new SpriteBatch();

    private final GameClient client;

    public GameRenderer(GameClient client) {
        this.client = client;
    }

    public void render(){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        client.getWorld().render(batch);
        batch.end();
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }
}