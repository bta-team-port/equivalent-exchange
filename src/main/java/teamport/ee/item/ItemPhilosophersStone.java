package teamport.ee.item;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

import java.util.HashMap;
import java.util.Map;

public class ItemPhilosophersStone extends Item {
	private static final Map<Block, Block> transmutations = new HashMap<>();

	public ItemPhilosophersStone(String name, int id) {
		super(name, id);
		setMaxStackSize(1);
		setContainerItem(this);
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		// These two are simple. First one checks the block that the player is targeting.
		// The second one checks and gets the targeted block's return value from the hashmap.
		Block blockToGet = world.getBlock(blockX, blockY, blockZ);
		Block blockToPlace = transmutations.get(blockToGet);

		// Now check if the targeted block isn't null.
		// Also add a delay for when the player is swinging else the sound will clip horrifically.
		if (blockToGet != null && !player.isSwinging) {

			// Then check if the map contains the targeted block's key. If so place the block's value.
			// Otherwise, if it's a colored block increase metadata.
			if (transmutations.containsKey(blockToGet)) {
				// Play the sound first before any 'server-side' calls.
				world.playSoundAtEntity(player, "ee.transmute", 0.7f, 1.0f);

				if (!world.isClientSide) {
					world.setBlockWithNotify(blockX, blockY, blockZ, blockToPlace.id);
					player.swingItem();

					return true;
				}
			} else {
				if (blockToGet == Block.planksOakPainted || blockToGet == Block.wool || blockToGet == Block.lampIdle || blockToGet == Block.lampActive) {
					world.playSoundAtEntity(player, "ee.transmute", 0.7f, 1.0f);

					if (!world.isClientSide) {
						// Get the plank/wool/lamp's metadata first.
						int meta = world.getBlockMetadata(blockX, blockY, blockZ);

						// Now if metadata is below 15 (maximum) increase it.
						// If it's equal to 15, reset back to 0.
						if (meta < 15) {
							world.setBlockMetadataWithNotify(blockX, blockY, blockZ, ++meta);
						} else {
							world.setBlockMetadataWithNotify(blockX, blockY, blockZ, 0);
						}

						player.swingItem();
						return true;
					}
				}
			}
		}

		return false;
	}

	static {
		// Grass
		transmutations.put(Block.grass, Block.grassScorched);
		transmutations.put(Block.grassScorched, Block.grass);

		// Soil
		transmutations.put(Block.dirt, Block.dirtScorched);
		transmutations.put(Block.dirtScorched, Block.mudBaked);
		transmutations.put(Block.mudBaked, Block.dirt);

		// Gravity
		transmutations.put(Block.sand, Block.gravel);
		transmutations.put(Block.gravel, Block.mud);
		transmutations.put(Block.mud, Block.sand);

		// Stone
		transmutations.put(Block.stone, Block.basalt);
		transmutations.put(Block.basalt, Block.limestone);
		transmutations.put(Block.limestone, Block.granite);
		transmutations.put(Block.granite, Block.permafrost);
		transmutations.put(Block.permafrost, Block.stone);

		// Cobblestone
		transmutations.put(Block.cobbleStone, Block.cobbleBasalt);
		transmutations.put(Block.cobbleBasalt, Block.cobbleLimestone);
		transmutations.put(Block.cobbleLimestone, Block.cobbleGranite);
		transmutations.put(Block.cobbleGranite, Block.cobblePermafrost);
		transmutations.put(Block.cobblePermafrost, Block.cobbleStone);

		// Logs
		transmutations.put(Block.logOak, Block.logBirch);
		transmutations.put(Block.logBirch, Block.logPine);
		transmutations.put(Block.logPine, Block.logCherry);
		transmutations.put(Block.logCherry, Block.logEucalyptus);
		transmutations.put(Block.logEucalyptus, Block.logOak);

		// Leaves
		transmutations.put(Block.leavesOak, Block.leavesBirch);
		transmutations.put(Block.leavesBirch, Block.leavesPine);
		transmutations.put(Block.leavesPine, Block.leavesCherry);
		transmutations.put(Block.leavesCherry, Block.leavesEucalyptus);
		transmutations.put(Block.leavesEucalyptus, Block.leavesShrub);
		transmutations.put(Block.leavesShrub, Block.leavesCacao);
		transmutations.put(Block.leavesCacao, Block.leavesOak);

		// Sapling
		transmutations.put(Block.saplingOak, Block.saplingBirch);
		transmutations.put(Block.saplingBirch, Block.saplingPine);
		transmutations.put(Block.saplingPine, Block.saplingCherry);
		transmutations.put(Block.saplingCherry, Block.saplingEucalyptus);
		transmutations.put(Block.saplingEucalyptus, Block.saplingShrub);
		transmutations.put(Block.saplingShrub, Block.saplingCacao);
		transmutations.put(Block.saplingCacao, Block.saplingOak);

		// Tall grass
		transmutations.put(Block.tallgrass, Block.tallgrassFern);
		transmutations.put(Block.tallgrassFern, Block.spinifex);
		transmutations.put(Block.spinifex, Block.tallgrass);

		// Flowers
		transmutations.put(Block.flowerRed, Block.flowerYellow);
		transmutations.put(Block.flowerYellow, Block.flowerRed);

		// Mushrooms
		transmutations.put(Block.mushroomBrown, Block.mushroomRed);
		transmutations.put(Block.mushroomRed, Block.mushroomBrown);
	}
}
