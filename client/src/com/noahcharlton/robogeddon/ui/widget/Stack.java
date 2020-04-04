package com.noahcharlton.robogeddon.ui.widget;

public class Stack extends Widget {

    private int spacing = 5;

    @Override
    public void layout() {
        var childrenCount = getChildren().size();

        float y = getY();
        float startY = y;
        float width = 0;

        for(int i = childrenCount - 1; i >= 0; i--) {
            var widget = getChildren().get(i);
            widget.setX(getX());
            widget.setY(y);

            y += widget.getHeight() + spacing;
            width = Math.max(width, widget.getWidth());
        }

        setHeight(y - startY);
        setWidth(width);
    }

    public Stack setSpacing(int spacing) {
        this.spacing = spacing;
        this.invalidate();
        return this;
    }
}
