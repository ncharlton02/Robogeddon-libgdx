package com.noahcharlton.robogeddon.ui.widget;

public abstract class NoChildrenWidget extends Widget{

    @Override
    public <T extends Widget> T add(T widget) {
        throw new UnsupportedOperationException("Cannot add widget to " + toString());
    }
}