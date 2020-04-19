package com.noahcharlton.robogeddon.ui.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.ui.Scene;
import com.noahcharlton.robogeddon.ui.selectable.SelectableSubMenu;
import com.noahcharlton.robogeddon.ui.selectable.SelectableSubMenus;
import com.noahcharlton.robogeddon.ui.widget.Widget;
import com.noahcharlton.robogeddon.util.Selectable;

import java.util.Optional;

public class InGameScene extends Scene {

    private final SelectableMenu selectableMenu = new SelectableMenu(this);

    public InGameScene() {
        add(selectableMenu).align(Align.bottomRight);
        add(new InventoryList()).align(Align.topLeft);
        add(new BuildMenu()).align(Align.bottomLeft).pad().bottom(1).left(1);
    }

    @Override
    public void update() {
        if(client.getProcessor().getSelectable() != null){
            selectableMenu.setVisible(true);
        }else{
            closeSelectableSubMenu();
            selectableMenu.setVisible(false);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            client.pauseGame();
        }
    }

    public void closeSelectableSubMenu() {
        boolean close = false;
        var selectable = client.getProcessor().getSelectable();

        if(selectable == null || getSelectableSubMenu().isEmpty()){
            close = true;
        }else { //If the current selectable matches the menu, it was opened automatically (via right click),
            //so it should not be closed
            var selectableSubMenu = (SelectableSubMenu) getSelectableSubMenu().get();

            close = !selectableSubMenu.getId().equals(selectable.getSubMenuID());
        }

        if(close)
            getSelectableSubMenu().ifPresent(widget -> getChildren().remove(widget));
    }

    public void toggleSelectableSubMenu(Selectable selectable) {
        var subMenu = getSelectableSubMenu();

        if(subMenu.isPresent()){
            getChildren().remove(subMenu.get());
        }else{
            add(SelectableSubMenus.createFrom(selectable.getSubMenuID())).align(Align.center);
        }
    }

    public Optional<Widget> getSelectableSubMenu() {
        return getChildren().stream().filter(widget -> widget instanceof SelectableSubMenu).findAny();
    }
}
