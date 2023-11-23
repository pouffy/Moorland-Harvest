package com.pouffydev.moorland_harvest.mixin;

import com.pouffydev.moorland_harvest.Moorland;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
	@Inject(method = "init", at = @At("TAIL"))
	public void exampleMod$onInit(CallbackInfo ci) {
		Moorland.LOGGER.info("Testing, testing, 1, 2, 3... Thank you for tuning in to Moorland Harvest!");
	}
}
