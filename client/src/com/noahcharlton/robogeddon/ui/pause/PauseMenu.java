package com.noahcharlton.robogeddon.ui.pause;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.ui.Scene;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.background.ColorBackground;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.*;
import com.noahcharlton.robogeddon.world.io.SaveWorldMessage;

public class PauseMenu extends Scene {

    private Widget mainStack;
    private Widget saveMenu;
    private Button saveButton;

    public PauseMenu() {
        var label = new Label().setText("Paused!").setFont(UIAssets.largeFont);
        mainStack = getMainButtonStack();
        saveMenu  = new SaveMenu();

        add(mainStack).align(Align.center);
        add(saveMenu).align(Align.center);
        add(label.pad().top(100)).align(Align.top);

        this.setBackground(new ColorBackground(new Color(0f, 0f, 0f, .2f)));
    }

    public Widget getMainButtonStack() {
        var resume = new TextButton("Resume").setOnClick(this::resume);
        saveButton = new TextButton("Save Game").setOnClick(this::saveGame);
        var toMainMenu = new TextButton("Quit to Main Menu").setOnClick(this::quitToMainMenu);
        var quitGame = new TextButton("Quit to Desktop").setOnClick(this::quitGame);

        return new Stack()
                .chainAdd(resume.setSize(145f, 30f))
                .chainAdd(saveButton.setSize(145f, 30f))
                .chainAdd(toMainMenu.setSize(145f, 30f))
                .chainAdd(quitGame.setSize(145f, 30f))
                .pad();
    }

    @Override
    public void update() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            client.resumeGame();
        }

        if(client.getWorld().getServer().isRemote()){
            saveButton.setVisible(false);
        }
    }

    @Override
    protected void onClick(ClickEvent event) {
        if(UIAssets.isEventOnDialogCloseButton(saveMenu, event)){
            mainStack.setVisible(true);
            saveMenu.setVisible(false);
        }
    }

    private void saveGame(ClickEvent clickEvent, Button button) {
        client.getWorld().sendMessageToServer(new SaveWorldMessage());
        mainStack.setVisible(false);
        saveMenu.setVisible(true);
    }

    private void quitToMainMenu(ClickEvent clickEvent, Button button) {
        client.gotoMainMenu();
    }

    private void quitGame(ClickEvent clickEvent, Button button) {
        Log.info("Quit button clicked!");
        Gdx.app.exit();
    }

    private void resume(ClickEvent clickEvent, Button button) {
        client.resumeGame();
    }

}
