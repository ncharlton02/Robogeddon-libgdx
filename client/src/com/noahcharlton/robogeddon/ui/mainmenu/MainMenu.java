package com.noahcharlton.robogeddon.ui.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.ui.Scene;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.*;
import com.noahcharlton.robogeddon.world.settings.NewWorldSettings;

public class MainMenu extends Scene {

    private final Widget mainStack;
    private final Widget playStack;

    public MainMenu() {
        var versionText =  "v" + Core.VERSION + "-" + Core.VERSION_TYPE;
        var title = new Label().setText("Robogeddon").setFont(UIAssets.titleFont).pad().top(100);
        var version = new Label().setText(versionText).setFont(UIAssets.smallFont)
                .pad().right(20).bottom(5);

        mainStack = new Stack()
                .chainAdd(new TextButton("Play").setOnClick(this::toggleStack))
                .chainAdd(new TextButton("Settings").setOnClick(this::gotoSettings))
                .chainAdd(new TextButton("About").setOnClick(this::gotoAbout))
                .chainAdd(new TextButton("Quit").setOnClick(this::quit))
                .setMinSize(145f, 30f).pad().bottom(100);

        playStack = new Stack()
                .chainAdd(new TextButton("New Game").setOnClick(this::newGame))
                .chainAdd(new TextButton("Load Game").setOnClick(this::loadGame))
                .chainAdd(new TextButton("Multiplayer").setOnClick(this::playMulti))
                .chainAdd(new TextButton("Back").setOnClick(this::toggleStack))
                .setMinSize(145f, 30f).pad().bottom(100).setVisible(false);

        add(title).align(Align.top);
        add(version).align(Align.bottomRight);
        add(mainStack).align(Align.bottom);
        add(playStack).align(Align.bottom);
    }

    private void toggleStack(ClickEvent clickEvent, Button button) {
        Gdx.app.postRunnable(() -> {
            mainStack.setVisible(!mainStack.isVisible());
            playStack.setVisible(!playStack.isVisible());
        });
    }

    private void gotoAbout(ClickEvent clickEvent, Button button) {
        clickEvent.getUi().setScene(new AboutMenu());
    }

    private void gotoSettings(ClickEvent clickEvent, Button button) {
        clickEvent.getUi().setScene(new SettingsScreen());
    }

    private void loadGame(ClickEvent clickEvent, Button button) {
        clickEvent.getUi().setScene(new LoadGameScreen());
    }

    private void quit(ClickEvent clickEvent, Button button) {
        Gdx.app.exit();
    }

    private void playMulti(ClickEvent clickEvent, Button button) {
        clickEvent.getUi().setScene(new MultiplayerScreen());
    }

    private void newGame(ClickEvent clickEvent, Button button) {
        client.startGame(new NewWorldSettings());
    }

    @Override
    public boolean isMouseOver() {
        //the main menu covers the whole screen, so the mouse is always over it
        return true;
    }

    @Override
    public boolean isWorldVisible() {
        return true;
    }
}
