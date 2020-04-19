package com.noahcharlton.robogeddon.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.client.GameClient;
import com.noahcharlton.robogeddon.ui.UI;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.event.KeyEvent;
import com.noahcharlton.robogeddon.ui.ingame.InGameScene;
import com.noahcharlton.robogeddon.util.Selectable;
import com.noahcharlton.robogeddon.world.Tile;

public class InputProcessor implements com.badlogic.gdx.InputProcessor {

    private final GameClient client;
    private final UI ui;

    private BuildAction buildAction;
    private Selectable selectable;
    private Tile lastTile;

    public InputProcessor(GameClient client) {
        this.client = client;
        this.ui = client.getUi();

        Log.debug("Initialized Input Processor");
        Gdx.input.setInputProcessor(this);
    }

    public void update() {
        if(client.getWorld() == null
                || client.getWorld().getPlayersRobot() == null
                || client.getWorld().getPlayersRobot().isDead()
                || client.getWorld().isPaused()) {
            buildAction = null;
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenY = Gdx.graphics.getHeight() - screenY;
        Vector3 pos = Core.client.mouseToWorld();
        Tile tile = client.getWorld().tileFromPixel(pos);

        if(ui.isMouseOver()) {
            ui.onClick(new ClickEvent(ui, screenX, screenY, button));
        } else if(buildAction != null) {
            lastTile = tile;

            if(tile != null) {
                buildAction.onClick(tile, button);
            }
        }else if(tile != null){
            if(button == Input.Buttons.RIGHT){
                if(tile.getSubMenuID() != null){
                    setSelectable(tile);
                    openSubMenuWindow();
                }else{
                    setSelectable(null);
                }
            }else{
                setSelectable(tile);
            }
        }else{
            setSelectable(null);
        }

        return false;
    }

    private void openSubMenuWindow() {
        if(ui.getCurrentScene() instanceof InGameScene){
            ((InGameScene) ui.getCurrentScene()).toggleSelectableSubMenu(selectable);
        }
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(ui.isMouseOver() || buildAction == null) {
            return false;
        }

        Vector3 pos = Core.client.mouseToWorld();
        Tile tile = client.getWorld().tileFromPixel(pos);

        if(tile != null && !tile.equals(lastTile)) {
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                buildAction.onClick(tile, Input.Buttons.LEFT);
            } else if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                buildAction.onClick(tile, Input.Buttons.RIGHT);
            }
        }
        lastTile = tile;

        return false;
    }

    public void setBuildAction(BuildAction buildAction) {
        if(buildAction != null) {
            Log.info("Set Build Action: " + buildAction.getName());
        } else {
            Log.info("Set Build Action: None");
        }

        if(buildAction instanceof Selectable){
            setSelectable((Selectable) buildAction);
        }else{
            setSelectable(null);
        }

        this.buildAction = buildAction;
    }

    public void setSelectable(Selectable selectable) {
        if(selectable != null) {
            Log.info("Set Selectable: " + selectable.getTitle());
        } else {
            Log.info("Set Selectable: None");
        }

        this.selectable = selectable;
    }

    @Override
    public boolean scrolled(int amount) {
        boolean onGui = ui.isMouseOver();

        if(onGui)
            return false;

        OrthographicCamera camera = client.getRenderer().getCamera();
        camera.zoom += amount / 10f;

        limitZoom(camera);

        return false;
    }

    private void limitZoom(OrthographicCamera camera) {
        if(camera.zoom < .5){
            camera.zoom = .5f;
        }else if(camera.zoom > 2){
            camera.zoom = 2f;
        }
    }

    public BuildAction getBuildAction() {
        return buildAction;
    }

    @Override
    public boolean keyDown(int keycode) {
        ui.onKeyEvent(new KeyEvent(keycode, ui));
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        ui.onKeyEvent(new KeyEvent(character, ui));
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public Selectable getSelectable() {
        return selectable;
    }
}
