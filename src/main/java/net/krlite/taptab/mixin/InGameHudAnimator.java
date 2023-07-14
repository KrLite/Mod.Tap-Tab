package net.krlite.taptab.mixin;

import net.krlite.taptab.InventorySwapper;
import net.krlite.taptab.TapTab;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(InGameHud.class)
public abstract class InGameHudAnimator {
	@Shadow private int scaledWidth;

	@ModifyArgs(
			method = "renderHotbar",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V",
					ordinal = 0
			)
	)
	private void renderHotbarItem(Args args /* int x, int y, float tickDelta, PlayerEntity *, ItemStack *, int seed */ ) {
		int x = args.get(0), y = args.get(1), slot = (x - 2 + 90 - scaledWidth / 2) / 20;
		long start = InventorySwapper.HOTBAR_SLOTS_ANIMATION_START[slot];
		double progress = (double) Math.max((System.currentTimeMillis() - start), 0) / TapTab.ANIMATION_DURATION;

		if (start == -1 || progress > 1) return;

		boolean reversed = InventorySwapper.HOTBAR_SLOTS_ANIMATION_REVERSED[slot];
		args.set(1, y + (int) TapTab.easeOutBounce(progress, reversed));
	}
}
