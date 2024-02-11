package teamport.ee.item;

import net.minecraft.core.item.Item;
import teamport.ee.EEConfig;
import turniplabs.halplibe.helper.ItemHelper;

import static teamport.ee.EquivalentExchange.MOD_ID;

public class EEItems {
	public static Item philosophersStone;
	public static Item matterDark;
	public static Item matterRed;

	private static int itemID = EEConfig.cfg.getInt("IDs.itemIDStart");
	private static int nextID() {
		return itemID++;
	}

	public static void initializeItems() {
		philosophersStone = ItemHelper.createItem(MOD_ID,
			new ItemPhilosophersStone("stone.philosophers", nextID()),
			"philosophers_stone.png"
		);

		matterDark = ItemHelper.createItem(MOD_ID,
			new Item("matter.dark", nextID()),
			"matter_dark.png"
		);

		matterRed = ItemHelper.createItem(MOD_ID,
			new Item("matter.red", nextID()),
			"matter_red.png"
		);
	}
}
