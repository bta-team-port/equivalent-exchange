package teamport.ee.item;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.*;
import teamport.ee.EEConfig;
import teamport.ee.item.tools.ItemToolHammer;
import teamport.ee.item.tools.ItemToolHoeMatter;
import teamport.ee.item.tools.ItemToolPickaxeMatter;
import teamport.ee.item.tools.ItemToolShovelMatter;
import turniplabs.halplibe.helper.ItemHelper;

import static teamport.ee.EquivalentExchange.MOD_ID;

public class EEItems {
	public static Item philosophersStone;
	public static Item mobiusCoal;
	public static Item aeternalisCoal;
	public static Item matterDark;
	public static Item matterRed;
	public static Item toolSwordDarkMatter;
	public static Item toolShovelDarkMatter;
	public static Item toolPickaxeDarkMatter;
	public static Item toolAxeDarkMatter;
	public static Item toolHoeDarkMatter;
	public static Item toolHammerDarkMatter;
	public static Item toolShearsDarkMatter;

	public static ToolMaterial matDarkMatter = new ToolMaterial()
		.setDurability(-1)
		.setEfficiency(16.0F, 50.0F)
		.setMiningLevel(3)
		.setDamage(5)
		.setBlockHitDelay(4);
	public static ToolMaterial matRedMatter = new ToolMaterial()
		.setDurability(-1)
		.setEfficiency(20.0F, 60.0F)
		.setMiningLevel(3)
		.setDamage(6)
		.setBlockHitDelay(4);

	private static int itemID = EEConfig.cfg.getInt("IDs.itemIDStart");
	private static int nextID() {
		return itemID++;
	}

	public static void initializeItems() {
		philosophersStone = ItemHelper.createItem(MOD_ID,
			new ItemPhilosophersStone("stone.philosophers", nextID()),
			"philosophers_stone.png"
		);

		mobiusCoal = ItemHelper.createItem(MOD_ID,
			new Item("mobiuscoal", nextID()),
			"coal_mobius.png"
		);
		aeternalisCoal = ItemHelper.createItem(MOD_ID,
			new Item("aeternaliscoal", nextID()),
			"coal_aeternalis.png"
		);

		matterDark = ItemHelper.createItem(MOD_ID,
			new Item("matter.dark", nextID()),
			"matter_dark.png"
		);
		matterRed = ItemHelper.createItem(MOD_ID,
			new Item("matter.red", nextID()),
			"matter_red.png"
		);

		toolSwordDarkMatter = ItemHelper.createItem(MOD_ID,
			new ItemToolSword("tool.sword.darkmatter", nextID(), matDarkMatter),
			"sword_darkmatter.png"
		);
		toolShovelDarkMatter = ItemHelper.createItem(MOD_ID,
			new ItemToolShovelMatter("tool.shovel.darkmatter", nextID(), matDarkMatter),
			"shovel_darkmatter.png"
		);
		toolPickaxeDarkMatter = ItemHelper.createItem(MOD_ID,
			new ItemToolPickaxeMatter("tool.pickaxe.darkmatter", nextID(), matDarkMatter),
			"pickaxe_darkmatter.png"
		);
		toolAxeDarkMatter = ItemHelper.createItem(MOD_ID,
			new ItemToolAxe("tool.axe.darkmatter", nextID(), matDarkMatter),
			"axe_darkmatter.png"
		);
		toolHoeDarkMatter = ItemHelper.createItem(MOD_ID,
			new ItemToolHoeMatter("tool.hoe.darkmatter", nextID(), matDarkMatter),
			"hoe_darkmatter.png"
		);
		toolHammerDarkMatter = ItemHelper.createItem(MOD_ID,
			new ItemToolHammer("tool.hammer.darkmatter", nextID(), matDarkMatter),
			"hammer_darkmatter.png"
		);
	}
}
