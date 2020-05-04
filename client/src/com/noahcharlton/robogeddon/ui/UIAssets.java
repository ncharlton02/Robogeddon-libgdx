package com.noahcharlton.robogeddon.ui;import com.badlogic.gdx.graphics.g2d.BitmapFont;import com.badlogic.gdx.graphics.g2d.NinePatch;import com.badlogic.gdx.graphics.g2d.TextureRegion;import com.noahcharlton.robogeddon.Core;import com.noahcharlton.robogeddon.asset.BitmapFontAsset;import com.noahcharlton.robogeddon.ui.event.ClickEvent;import com.noahcharlton.robogeddon.ui.widget.Widget;public class UIAssets {    public static NinePatch button;    public static NinePatch buttonHover;    public static NinePatch buttonSelected;    public static NinePatch dialog;    public static NinePatch textField;    public static BitmapFont smallFont;    public static BitmapFont largeFont;    public static BitmapFont titleFont;    public static TextureRegion iconButton;    public static TextureRegion iconButtonHover;    public static TextureRegion itemDuctArrow;    public static void init(){        Core.assets.registerNinePatch("ui/button").setOnLoad(b -> button = b);        Core.assets.registerNinePatch("ui/buttonHover").setOnLoad(b -> buttonHover = b);        Core.assets.registerNinePatch("ui/buttonSelected").setOnLoad(b -> buttonSelected = b);        Core.assets.registerNinePatch("ui/dialog").setOnLoad(b -> dialog = b);        Core.assets.registerNinePatch("ui/textField").setOnLoad(b -> textField = b);        Core.assets.registerBitmapFont("large").setOnLoad(font -> largeFont = font);        Core.assets.registerBitmapFont("title").setOnLoad(font -> titleFont = font);        Core.assets.registerTexture("ui/iconButton").setOnLoad(t -> iconButton = t);        Core.assets.registerTexture("ui/iconButtonHover").setOnLoad(t -> iconButtonHover = t);        Core.assets.registerTexture("blocks/item_duct_arrow").setOnLoad(t -> itemDuctArrow = t);        var smallFontAsset = new BitmapFontAsset("small");        smallFontAsset.setOnLoad(font -> smallFont = font);        Core.assets.registerAssetFirst(smallFontAsset); //Should be up front so that the loading screen can use it    }    public static boolean isEventOnDialogCloseButton(Widget widget, ClickEvent event) {        float width = 14;        float height = 12;        float x = widget.getX() + widget.getWidth() - width;        float y = widget.getY() + widget.getHeight() - height;        return event.getX() > x && event.getY() > y && event.getX() < x + width && event.getY() < y + height;    }}