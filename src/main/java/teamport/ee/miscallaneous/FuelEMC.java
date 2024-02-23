package teamport.ee.miscallaneous;

import net.minecraft.core.item.Item;
import teamport.ee.item.EEItems;

import java.util.HashMap;

public class FuelEMC {
	private static final HashMap<Integer, Integer> fuelList = new HashMap<>();

	public FuelEMC() {
		addFuel(Item.coal.id, 1);
		addFuel(Item.dustRedstone.id, 16);
		addFuel(Item.dustGlowstone.id, 32);
		addFuel(Item.nethercoal.id, 32);
		addFuel(EEItems.mobiusCoal.id, 64);
		addFuel(EEItems.aeternalisCoal.id, 96);
	}

	public static void addFuel(int inputItem, int outputYield) {
		fuelList.put(inputItem, outputYield);
	}

	public int getYield(int i) {
		return fuelList.get(i);
	}

	public HashMap<Integer, Integer> getFuelList() {
		return fuelList;
	}
}
