package net.lunade.copper.blocks;

import net.minecraft.state.property.BooleanProperty;

public class CopperPipeProperties {

    public static final BooleanProperty FRONT_CONNECTED = BooleanProperty.of("front_connected");
    public static final BooleanProperty BACK_CONNECTED = BooleanProperty.of("back_connected");
    public static final BooleanProperty SMOOTH = BooleanProperty.of("smooth");
    public static final BooleanProperty HAS_WATER = BooleanProperty.of("has_water");
    public static final BooleanProperty HAS_SMOKE = BooleanProperty.of("has_smoke");
    public static final BooleanProperty HAS_ELECTRICITY = BooleanProperty.of("has_electricity");
    public static final BooleanProperty HAS_ITEM = BooleanProperty.of("has_item");

    public static void init() {}
}
