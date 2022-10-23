package net.lunade.copper.blocks;

import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class CopperPipeProperties {

    public static final BooleanProperty FRONT_CONNECTED = BooleanProperty.create("front_connected");
    public static final BooleanProperty BACK_CONNECTED = BooleanProperty.create("back_connected");
    public static final BooleanProperty SMOOTH = BooleanProperty.create("smooth");
    public static final BooleanProperty HAS_WATER = BooleanProperty.create("has_water");
    public static final BooleanProperty HAS_SMOKE = BooleanProperty.create("has_smoke");
    public static final BooleanProperty HAS_ELECTRICITY = BooleanProperty.create("has_electricity");
    public static final BooleanProperty HAS_ITEM = BooleanProperty.create("has_item");

    public static void init() {}
}
