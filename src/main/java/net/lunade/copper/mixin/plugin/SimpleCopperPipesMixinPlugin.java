package net.lunade.copper.mixin.plugin;

import java.util.List;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class SimpleCopperPipesMixinPlugin implements IMixinConfigPlugin {
	private static final String IWRENCHABLE = "com/zurrtum/create/content/equipment/wrench/IWrenchable";
	private static final Set<String> CREATE_WRENCHABLE_MIXINS = Set.of(
		"net.lunade.copper.mixin.create.CopperPipeBlockMixin"
	);

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (CREATE_WRENCHABLE_MIXINS.contains(mixinClassName)) {
			return FabricLoader.getInstance().isModLoaded("create");
		}
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if (CREATE_WRENCHABLE_MIXINS.contains(mixinClassName) && !targetClass.interfaces.contains(IWRENCHABLE)) {
			targetClass.interfaces.add(IWRENCHABLE);
		}
	}
}
