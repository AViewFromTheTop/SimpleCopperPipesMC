package net.lunade.copper.config.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.frozenblock.lib.FrozenBools;
import net.minecraft.client.gui.screens.Screen;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        if (FrozenBools.HAS_CLOTH_CONFIG) {
            return SimpleCopperPipesConfigGui::buildScreen;
        }
        return null;
    }
}
