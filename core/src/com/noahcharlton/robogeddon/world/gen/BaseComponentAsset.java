package com.noahcharlton.robogeddon.world.gen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.asset.Asset;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.world.floor.Floor;
import com.noahcharlton.robogeddon.world.io.Element;
import com.noahcharlton.robogeddon.world.io.XmlReader;

public class BaseComponentAsset extends Asset<Object> {

    private final BaseComponentType type;

    public BaseComponentAsset(BaseComponentType type) {
        this.type = type;

        setOnLoad(comp -> {});
    }

    @Override
    protected String getName() {
        return "BaseComponent(" + type.getID() + ")";
    }

    @Override
    protected Object load() {
        FileHandle folder = Gdx.files.classpath("components/" + type.getID());

        for(String name : type.getComponentNames()){
            FileHandle file = folder.child(name + ".xml");
            BaseComponent comp = new BaseComponent(type, name);

            parseComp(file, comp);

            type.getComponents().add(comp);
        }

        return new Object();
    }

    private void parseComp(FileHandle file, BaseComponent comp) {
        Element reader = new XmlReader().parse(file.readString());

        for(Element child : reader.getChildrenByName("Build")){
            addInstruction(child, comp);
        }
    }

    private void addInstruction(Element child, BaseComponent component) {
        Block block = Core.blocks.getOrNull(child.get("Block", null));
        Floor floor = Core.floors.getOrNull(child.get("Floor", null));
        Floor upperFloor = Core.floors.getOrNull(child.get("UpperFloor", null));
        int x = child.getIntAttribute("x");
        int y = child.getIntAttribute("y");

        component.addInstruction(x, y, block, floor, upperFloor);
    }

    @Override
    protected void dispose() {

    }
}
