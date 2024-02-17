package teamport.ee.miscallaneous.interfaces;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import teamport.ee.EquivalentExchange;
import teamport.ee.miscallaneous.FuelEMC;
import teamport.ee.miscallaneous.enums.EnumItemToolModes;

public interface IToolMatter {
	FuelEMC fuel = new FuelEMC();

	EnumItemToolModes getCurrentMode();

	// First check the entire inventory for EMC fuel items.
	// If found then get the yield/stack size for the fuel and check if it's above or equal to the counted blocks.
	// Finally, consume fuel then return true.
	default boolean canUseItem(int countedBlocks, EntityPlayer player) {
		if (player != null) {
			for (int i = 0; i < player.inventory.mainInventory.length; i++) {
				ItemStack stack = player.inventory.getStackInSlot(i);
				if (stack != null && fuel.getFuelList().containsKey(stack.getItem().id)) {
					if (fuel.getYield(stack.itemID) >= countedBlocks || stack.stackSize > countedBlocks) {
						stack.consumeItem(player);
						EquivalentExchange.LOGGER.info("Block count is " + countedBlocks);

						// Error prevention method for stack sizes below 0.
						if (stack.stackSize <= 0) {
							player.inventory.mainInventory[i] = null;
						}

						return true;
					}
				}
			}
		}
		return false;
	}
}
