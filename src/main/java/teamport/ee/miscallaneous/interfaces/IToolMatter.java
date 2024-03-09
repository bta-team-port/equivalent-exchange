package teamport.ee.miscallaneous.interfaces;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import teamport.ee.miscallaneous.FuelEMC;
import teamport.ee.miscallaneous.enums.EnumItemToolModes;

import java.util.ArrayList;
import java.util.List;

public interface IToolMatter {
	FuelEMC fuel = new FuelEMC();

	EnumItemToolModes getCurrentMode();

	// First check the entire inventory for EMC fuel items.
	// If found then get the yield/stack size for the fuel and check if it's above or equal to the counted blocks.
	// Finally, consume fuel then return true.
	default boolean canUseItem(int countedBlocks, EntityPlayer player) {
		if (player != null) {
			int cumulativeFuel = 0;
			List<Integer> indices = new ArrayList<>();
			for (int i = 0; i < player.inventory.mainInventory.length; i++) {
				ItemStack stack = player.inventory.getStackInSlot(i);
				if (stack != null && fuel.getFuelList().containsKey(stack.getItem().id)) {
					cumulativeFuel += fuel.getYield(stack.itemID) * stack.stackSize;
					indices.add(i);
					if (cumulativeFuel >= countedBlocks) break;
				}
			}
			if (cumulativeFuel >= countedBlocks || !player.gamemode.consumeBlocks()) {
				int usedFuel = 0;
				for (Integer i : indices) {
					ItemStack stack = player.inventory.getStackInSlot(i);
					while (stack.stackSize > 0 && usedFuel < countedBlocks){
						usedFuel += fuel.getYield(stack.itemID);
						stack.consumeItem(player);
					}
					if (stack.stackSize <= 0){
						player.inventory.mainInventory[i] = null;
					}
				}
				player.addChatMessage(I18n.getInstance().translateKey("ee.debug.mining") + " " + countedBlocks);
				return true;
			}
		}
		return false;
	}
}
