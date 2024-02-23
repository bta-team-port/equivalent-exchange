package teamport.ee.item.tools;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import teamport.ee.miscallaneous.enums.EnumItemToolModes;
import teamport.ee.miscallaneous.interfaces.IToolMatter;
import teamport.ee.miscallaneous.math.MathHelper;

public class ItemToolHammer extends ItemToolPickaxe implements IToolMatter {
	public static EnumItemToolModes currentToolMode = EnumItemToolModes.DEFAULT;
	private int x = 0;
	private int y = 0;
	private int z = 0;
	private int blockCount;
	private Block block;

	public ItemToolHammer(String name, int id, ToolMaterial enumtoolmaterial) {
		super(name, id, enumtoolmaterial);
		setMaxDamage(2);
		showFullDurability();
	}

	// A separate function to drop the items, so we don't have to paste it over and over again.
	private void dropItems(World world, EntityPlayer entityPlayer, int x, int y, int z) {
		if (world.getBlock(x, y, z) != null) {
			ItemStack[] stacks = world.getBlock(x, y, z).getBreakResult(world, EnumDropCause.PROPER_TOOL, x, y, z, world.getBlockMetadata(x, y, z), world.getBlockTileEntity(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);

			if (entityPlayer.getGamemode().consumeBlocks()) {
				if (stacks != null) {
					for (ItemStack stack : stacks) {
						world.dropItem(x, y, z, stack);
					}
				}
			}
		}
	}

	@Override
	public boolean onBlockDestroyed(World world, ItemStack itemstack, int id, int blockX, int blockY, int blockZ, EntityLiving living) {
		// Get the block breaker's X and Y rotations.
		// Shout out to BestSoft for the wrap fix!
		float wrapY = MathHelper.wrapDegrees(Math.round(living.yRot));
		float xRot = living.xRot % 360;

		// ROTATION VALUES
		// N - 180
		// E - 270
		// S - 90
		// W - 360

		// Check the current tool mode.
		if (currentToolMode == EnumItemToolModes.THREE_X_THREE) {
			// Shout out to Hammers by AwesomeBossDJ7 for the modified code!
			// First check if the miner is looking up and down through the X rotation.
			// If it isn't; continue to sided directions. Otherwise, skip to the up and down directions at the end.
			if (xRot < 60 && xRot > -60) {
				// North and South checks, rotated 45 degrees to both East and South.
				if ((wrapY >= 135 && wrapY < 225) || (wrapY >= 315 || wrapY < 45)) {
					for (int x = blockX - 1; x < blockX + 2; x++) {
						for (int y = blockY - 1; y < blockY + 2; y++) {
							Block block = world.getBlock(x, y, blockZ);

							// Null, mine-able, and bedrock check.
							if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block != Block.bedrock) {
								dropItems(world, (EntityPlayer) living, x, y, blockZ);
							}
						}
					}
				}
				// East and South checks are also rotated 45 degrees. This time to North and South.
				else if ((wrapY >= 225 && wrapY < 315) || (wrapY >= 45 && wrapY < 135)) {
					for (int z = blockZ - 1; z < blockZ + 2; z++) {
						for (int y = blockY - 1; y < blockY + 2; y++) {
							Block block = world.getBlock(blockX, y, z);

							if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block != Block.bedrock) {
								dropItems(world, (EntityPlayer) living, blockX, y, z);
							}
						}
					}
				}
			} else {
				for (int x = blockX - 1; x < blockX + 2; x++) {
					for (int z = blockZ - 1; z < blockZ + 2; z++) {
						Block block = world.getBlock(x, blockY, z);

						if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block != Block.bedrock) {
							dropItems(world, (EntityPlayer) living, x, blockY, z);
						}
					}
				}
			}
		}
		return true;
	}

	private void mineNorthLoop(int blockX, int blockY, int blockZ, int minX, int minY, int maxX, int maxY, int maxZ, Runnable action) {
		for (x = blockX - minX; x < blockX + maxX; x++) {
			for (y = blockY - minY; y < blockY + maxY; y++) {
				for (z = blockZ; z > blockZ - maxZ; z--) {
					if (action != null) {
						action.run();
					}
				}
			}
		}
	}

	private void mineEastLoop(int blockX, int blockY, int blockZ, int minY, int minZ, int maxX, int maxY, int maxZ, Runnable action) {
		for (x = blockX; x < blockX + maxX; x++) {
			for (y = blockY - minY; y < blockY + maxY; y++) {
				for (z = blockZ - minZ; z < blockZ + maxZ; z++) {
					if (action != null) {
						action.run();
					}
				}
			}
		}
	}

	private void mineSouthLoop(int blockX, int blockY, int blockZ, int minX, int minY, int maxX, int maxY, int maxZ, Runnable action) {
		for (x = blockX - minX; x < blockX + maxX; x++) {
			for (y = blockY - minY; y < blockY + maxY; y++) {
				for (z = blockZ; z < blockZ + maxZ; z++) {
					if (action != null) {
						action.run();
					}
				}
			}
		}
	}

	private void mineWestLoop(int blockX, int blockY, int blockZ, int minY, int minZ, int maxX, int maxY, int maxZ, Runnable action) {
		for (x = blockX; x > blockX - maxX; x--) {
			for (y = blockY - minY; y < blockY + maxY; y++) {
				for (z = blockZ - minZ; z < blockZ + maxZ; z++) {
					if (action != null) {
						action.run();
					}
				}
			}
		}
	}

	private void mineUpLoop(int blockX, int blockY, int blockZ, int minX, int minZ, int maxX, int maxY, int maxZ, Runnable action) {
		for (x = blockX - minX; x < blockX + maxX; x++) {
			for (y = blockY; y < blockY + maxY; y++) {
				for (z = blockZ - minZ; z < blockZ + maxZ; z++) {
					if (action != null) {
						action.run();
					}
				}
			}
		}
	}

	private void mineDownLoop(int blockX, int blockY, int blockZ, int minX, int minZ, int maxX, int maxY, int maxZ, Runnable action) {
		for (x = blockX - minX; x < blockX + maxX; x++) {
			for (y = blockY; y > blockY - maxY; y--) {
				for (z = blockZ - minZ; z < blockZ + maxZ; z++) {
					if (action != null) {
						action.run();
					}
				}
			}
		}
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		// First check if there's a targeted block exists and is mine-able.
		// If it exists then play the 'destruct' sound.
		if (world.getBlock(blockX, blockY, blockZ) != null && world.getBlock(blockX, blockY, blockZ).hasTag(BlockTags.MINEABLE_BY_PICKAXE)) {
			world.playSoundAtEntity(player, "ee.destruct", 0.7f, 1.0f);
		}
		if (!world.isClientSide) {
			// Dummy default values.
			blockCount = 0;
			block = world.getBlock(x, y, z);

			final Runnable countBlocks = () -> { // Function for if block should be counted
				if (!world.isAirBlock(x, y, z) && world.getBlock(x, y, z).hasTag(BlockTags.MINEABLE_BY_PICKAXE)) {
					++blockCount;
				}
			};
			final Runnable breakBlocks = () -> { // Function for if block should be broken
				block = world.getBlock(x, y, z);
				if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block.getHardness() >= 0) {
					dropItems(world, player, x, y, z);
				}
			};

			// Player rotations.
			float wrapY = MathHelper.wrapDegrees(Math.round(player.yRot));
			float xRot = player.xRot % 360;

			// Metadata 1 (half-charge)
			if (itemstack.getMetadata() == 1) {
				if (xRot < 60 && xRot > -60) {
					// North
					if ((wrapY >= 135 && wrapY < 225)) {
						mineNorthLoop(blockX, blockY, blockZ, 1, 1, 2, 2, 3, countBlocks);
					}
					// East
					else if (wrapY >= 225 && wrapY < 315) {
						mineEastLoop(blockX, blockY, blockZ, 1, 1, 3, 2, 2, countBlocks);
					}
					// South
					else if (wrapY >= 315 || wrapY < 45) {
						mineSouthLoop(blockX, blockY, blockZ, 1, 1, 2, 2, 3, countBlocks);
					}
					// West
					else if (wrapY >= 45 && wrapY < 135) {
						mineWestLoop(blockX, blockY, blockZ, 1, 1, 3, 2, 2, countBlocks);
					}
				}
				// Up
				else if (xRot <= -45) {
					mineUpLoop(blockX, blockY, blockZ, 1, 1, 2, 3, 2, countBlocks);
				}
				// Down
				else if (xRot >= 45) {
					mineDownLoop(blockX, blockY, blockZ, 1, 1, 2, 3, 2, countBlocks);
				}

				if (canUseItem(blockCount, player)) {
					if (xRot < 60 && xRot > -60) {
						// North
						if ((wrapY >= 135 && wrapY < 225)) {
							mineNorthLoop(blockX, blockY, blockZ, 1, 1, 2, 2, 3, breakBlocks);
						}
						// East
						else if (wrapY >= 225 && wrapY < 315) {
							mineEastLoop(blockX, blockY, blockZ, 1, 1, 3, 2, 2, breakBlocks);
						}
						// South
						else if (wrapY >= 315 || wrapY < 45) {
							mineSouthLoop(blockX, blockY, blockZ, 1, 1, 2, 2, 3, breakBlocks);
						}
						// West
						else if (wrapY >= 45 && wrapY < 135) {
							mineWestLoop(blockX, blockY, blockZ, 1, 1, 3, 2, 2, breakBlocks);
						}
					}
					// Up
					else if (xRot <= -45) {
						mineUpLoop(blockX, blockY, blockZ, 1, 1, 2, 3, 2, breakBlocks);
					}
					// Down
					else if (xRot >= 45) {
						mineDownLoop(blockX, blockY, blockZ, 1, 1, 2, 3, 2, breakBlocks);
					}
				}
				return true;
			}
			// Metadata 0 (full charge)
			else if (itemstack.getMetadata() == 0) {
				if (xRot < 60 && xRot > -60) {
					// North
					if ((wrapY >= 135 && wrapY < 225)) {
						mineNorthLoop(blockX, blockY, blockZ, 2, 2, 3, 3, 5, countBlocks);
					}
					// East
					else if (wrapY >= 225 && wrapY < 315) {
						mineEastLoop(blockX, blockY, blockZ, 2, 2, 5, 3, 3, countBlocks);
					}
					// South
					else if (wrapY >= 315 || wrapY < 45) {
						mineSouthLoop(blockX, blockY, blockZ, 2, 2, 3, 3, 5, countBlocks);
					}
					// West
					else if (wrapY >= 45 && wrapY < 135) {
						mineWestLoop(blockX, blockY, blockZ, 2, 2, 5, 3, 3, countBlocks);
					}
				}
				// Up
				else if (xRot <= -45) {
					mineUpLoop(blockX, blockY, blockZ, 2, 2, 3, 5, 3, countBlocks);
				}
				// Down
				else if (xRot >= 45) {
					mineDownLoop(blockX, blockY, blockZ, 2, 2, 3, 5, 3, countBlocks);
				}
				if (canUseItem(blockCount, player)) {
					if (xRot < 60 && xRot > -60) {
						// North
						if ((wrapY >= 135 && wrapY < 225)) {
							mineNorthLoop(blockX, blockY, blockZ, 2, 2, 3, 3, 5, breakBlocks);
						}
						// East
						else if (wrapY >= 225 && wrapY < 315) {
							mineEastLoop(blockX, blockY, blockZ, 2, 2, 5, 3, 3, breakBlocks);
						}
						// South
						else if (wrapY >= 315 || wrapY < 45) {
							mineSouthLoop(blockX, blockY, blockZ, 2, 2, 3, 3, 5, breakBlocks);
						}
						// West
						else if (wrapY >= 45 && wrapY < 135) {
							mineWestLoop(blockX, blockY, blockZ, 2, 2, 5, 3, 3, breakBlocks);
						}
					}
					// Up
					else if (xRot <= -45) {
						mineUpLoop(blockX, blockY, blockZ, 2, 2, 3, 5, 3, breakBlocks);
					}
					// Down
					else if (xRot >= 45) {
						mineDownLoop(blockX, blockY, blockZ, 2, 2, 3, 5, 3, breakBlocks);
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public EnumItemToolModes getCurrentMode() {
		return currentToolMode;
	}
}
