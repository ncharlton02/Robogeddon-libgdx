package com.noahcharlton.robogeddon.ui.event;

import com.noahcharlton.robogeddon.ui.UI;

public class KeyEvent {

    public static final char BACKSPACE = 8;
    public static final char DELETE = 127;
    public static final char SPACE = 32;

    private final char character;
    private final int keyCode;
    private final UI ui;

    public KeyEvent(char character, UI ui) {
        this.character = character;
        this.ui = ui;
        this.keyCode = -1;
    }

    public KeyEvent(int keyCode, UI ui) {
        this.character = 0;
        this.ui = ui;
        this.keyCode = keyCode;
    }

    public UI getUi() {
        return ui;
    }

    boolean isCharacterTypedEvent(){
        return character != 0;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public char getCharacter() {
        return character;
    }
}