package teamport.ee;

import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import static teamport.ee.EquivalentExchange.MOD_ID;

public class EEConfig {
	private static final Toml properties = new Toml("Equivalent Exchange's TOML Config");
	public static TomlConfigHandler cfg;

	static {
		properties.addCategory("IDs")
			.addEntry("blockIDStart", 1575)
			.addEntry("itemIDStart", 16575);

		properties.addCategory("Tools")
			.addEntry("swordsUseFuel", true)
			.addEntry("shovelsUseFUel", true)
			.addEntry("pickaxesUseFuel", true)
			.addEntry("hoesUseFuel", true)
			.addEntry("hammersUseFuel", true)
			.addEntry("shearsUseFuel", true);



		cfg = new TomlConfigHandler(MOD_ID, properties);
	}
}
