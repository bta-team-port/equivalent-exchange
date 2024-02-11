package teamport.ee;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import teamport.ee.block.EEBlocks;
import teamport.ee.item.EEItems;
import turniplabs.halplibe.helper.SoundHelper;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.GameStartEntrypoint;


public class EquivalentExchange implements ModInitializer, GameStartEntrypoint, ClientStartEntrypoint {
    public static final String MOD_ID = "ee";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Equivalent Exchange has been initialized.");
    }

	@Override
	public void beforeGameStart() {
		EEItems.initializeItems();
		EEBlocks.initializeBlocks();
	}

	@Override
	public void afterGameStart() {

	}

	@Override
	public void beforeClientStart() {
		SoundHelper.Client.addSound(MOD_ID, "break.wav");
		SoundHelper.Client.addSound(MOD_ID, "chargetick.wav");
		SoundHelper.Client.addSound(MOD_ID, "destruct.wav");
		SoundHelper.Client.addSound(MOD_ID, "flash.wav");
		SoundHelper.Client.addSound(MOD_ID, "gust.wav");
		SoundHelper.Client.addSound(MOD_ID, "heal.wav");
		SoundHelper.Client.addSound(MOD_ID, "kinesis.wav");
		SoundHelper.Client.addSound(MOD_ID, "launch.wav");
		SoundHelper.Client.addSound(MOD_ID, "nova.wav");
		SoundHelper.Client.addSound(MOD_ID, "philball.wav");
		SoundHelper.Client.addSound(MOD_ID, "tock.wav");
		SoundHelper.Client.addSound(MOD_ID, "transmute.wav");
		SoundHelper.Client.addSound(MOD_ID, "wall.wav");
		SoundHelper.Client.addSound(MOD_ID, "waterball.wav");
		SoundHelper.Client.addSound(MOD_ID, "wind.wav");
	}

	@Override
	public void afterClientStart() {

	}
}
