package teamport.ee.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import teamport.ee.EquivalentExchange;
import teamport.ee.item.EEItems;
import teamport.ee.item.tools.ItemToolHammer;
import teamport.ee.miscallaneous.enums.EnumItemToolModes;
import teamport.ee.miscallaneous.interfaces.IToolMatter;

@Mixin(value = Minecraft.class, remap = false)
public class MinecraftMixin {
	@Unique
	private int modeWait = 0;
	@Unique
	private int chargeWait = 0;

	@Shadow
	public GuiScreen currentScreen;

	@Shadow
	public EntityPlayerSP thePlayer;

	@Shadow
	public World theWorld;

	// Check every tick if the EE keybind is pressed.
	@Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;next()Z", shift = At.Shift.AFTER))
	private void ee_keybindTick(CallbackInfo ci) {
		if (thePlayer != null) {
			if (modeWait > 0) {
				--modeWait;
			} else {
				if (thePlayer.getCurrentEquippedItem() != null) {
					if (EquivalentExchange.keyChangeMode.isPressed() && currentScreen == null) {
						modeWait = 10;
						if (thePlayer.getCurrentEquippedItem().getItem() instanceof IToolMatter) {
							EnumItemToolModes toolModes = ((IToolMatter) thePlayer.getCurrentEquippedItem().getItem()).getCurrentMode();

							// Check if the current item is a dark matter hammer.
							if (thePlayer.getCurrentEquippedItem().getItem() == EEItems.toolHammerDarkMatter) {
								switch (toolModes) {
									default:
									case DEFAULT:
										thePlayer.addChatMessage("ee.modeswitch.3x3");
										ItemToolHammer.currentToolMode = EnumItemToolModes.THREE_X_THREE;
										break;
									case THREE_X_THREE:
										thePlayer.addChatMessage("ee.modeswitch.default");
										ItemToolHammer.currentToolMode = EnumItemToolModes.DEFAULT;
										break;
								}
							}
						}
					}
				}
			}

			if (chargeWait > 0) {
				--chargeWait;
			} else {
				if (thePlayer.getCurrentEquippedItem() != null) {
					// Get the metadata of the held item.
					int meta = thePlayer.getCurrentEquippedItem().getMetadata();

					if (EquivalentExchange.keyCharge.isPressed() && currentScreen == null) {
						// Check if the player is sneaking.
						// If not then charge up until it's full. (metadata = 0)
						// Otherwise, if the player IS sneaking then charge down until empty. (metadata = 2)
						chargeWait = 10;
						if (thePlayer.getCurrentEquippedItem().getItem() instanceof IToolMatter) {
							if (!thePlayer.isSneaking()) {
								if (meta >= 2 || meta == 1) {
									thePlayer.getCurrentEquippedItem().setMetadata(--meta);
									theWorld.playSoundAtEntity(thePlayer, "ee.flash", 0.3f, 1.0f);
								}
							} else {
								if (meta <= 0 || meta == 1) {
									thePlayer.getCurrentEquippedItem().setMetadata(++meta);
									theWorld.playSoundAtEntity(thePlayer, "ee.break", 0.3f, 1.0f);
								}
							}
						}
					}
				}
			}
		}
	}
}
