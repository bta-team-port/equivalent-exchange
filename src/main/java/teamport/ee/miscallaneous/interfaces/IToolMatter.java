package teamport.ee.miscallaneous.interfaces;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import teamport.ee.miscallaneous.FuelEMC;
import teamport.ee.miscallaneous.enums.EnumItemToolModes;

public interface IToolMatter {

	EnumItemToolModes getCurrentMode();

	default void consumeFuel(int count, ItemStack itemStack, EntityPlayer player) {
		FuelEMC fuel = new FuelEMC();

		int i = fuel.getYield(itemStack.itemID);
		if (count == i) {
			itemStack.consumeItem(player);
		}
	}
}
