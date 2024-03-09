package teamport.ee.item.tools;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import teamport.ee.EEConfig;
import teamport.ee.miscallaneous.enums.EnumItemToolModes;
import teamport.ee.miscallaneous.interfaces.IToolMatter;

public class ItemToolHammer extends ItemToolPickaxe implements IToolMatter {
	public static EnumItemToolModes currentToolMode = EnumItemToolModes.DEFAULT;
	private int x = 0;
	private int y = 0;
	private int z = 0;
	private int blockCount;
	private int vertical;
	private Block block;

	public ItemToolHammer(String name, int id, ToolMaterial enumtoolmaterial) {
		super(name, id, enumtoolmaterial);
		setMaxDamage(2);
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
		Direction playerDirection = Direction.getHorizontalDirection(living);
		float xRot = living.xRot % 360;

		// 0 - middle
		// 1 - up
		// 2 - down
		if (xRot < 60 && xRot > -60) vertical = 0;
		else if (xRot <= -45) vertical = 1;
		else if (xRot >= 45) vertical = 2;

		// Check the current tool mode. In this case, 3x3 mining mode.
		// Shout out to UselessBullets for the Direction and Vertical ideas!
		if (currentToolMode == EnumItemToolModes.THREE_X_THREE) {
			if (vertical == 0) {
				if (playerDirection == Direction.NORTH || playerDirection == Direction.SOUTH) {
					for (int x = blockX - 1; x < blockX + 2; x++) {
						for (int y = blockY - 1; y < blockY + 2; y++) {
							Block block = world.getBlock(x, y, blockZ);

							// Null and mine-ability check.
							if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE)) {
								dropItems(world, (EntityPlayer) living, x, y, blockZ);
							}
						}
					}
				} else if (playerDirection == Direction.EAST || playerDirection == Direction.WEST) {
					for (int z = blockZ - 1; z < blockZ + 2; z++) {
						for (int y = blockY - 1; y < blockY + 2; y++) {
							Block block = world.getBlock(blockX, y, z);

							if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE)) {
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

	//
	// The next batch of functions are mining loops for the right-click modes.
	//

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

			// Runnable values, so we don't have to paste over and over again.
			// The first function is when to count blocks. The second is when to break them.
			final Runnable countBlocks = () -> {
				if (!world.isAirBlock(x, y, z) && world.getBlock(x, y, z).hasTag(BlockTags.MINEABLE_BY_PICKAXE)) {
					++blockCount;
				}
			};
			final Runnable breakBlocks = () -> {
				block = world.getBlock(x, y, z);
				if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block.getHardness() >= 0) {
					dropItems(world, player, x, y, z);
				}
			};

			// Player rotation values. These are used in the switch statements below.
			// X rotation (up and down) needs to be manually detected due to a lack of 'middle' in the Enum.
			Direction playerDirHorizontal = Direction.getHorizontalDirection(player);
			float xRot = player.xRot % 360;

			// 0 - middle
			// 1 - up
			// 2 - down
			if (xRot < 60 && xRot > -60) vertical = 0;
			else if (xRot <= -45) vertical = 1;
			else if (xRot >= 45) vertical = 2;

			// Metadata 1 (half-charge)
			if (itemstack.getMetadata() == 1) {
				if (vertical == 0) {
					switch (playerDirHorizontal) {
						default:
						case NONE:
						case NORTH:
							mineNorthLoop(blockX, blockY, blockZ, 1, 1, 2, 2, 3, countBlocks);
							break;
						case EAST:
							mineEastLoop(blockX, blockY, blockZ, 1, 1, 3, 2, 2, countBlocks);
							break;
						case SOUTH:
							mineSouthLoop(blockX, blockY, blockZ, 1, 1, 2, 2, 3, countBlocks);
							break;
						case WEST:
							mineWestLoop(blockX, blockY, blockZ, 1, 1, 3, 2, 2, countBlocks);
							break;
					}
				} else if (vertical == 1) {
					mineUpLoop(blockX, blockY, blockZ, 1, 1, 2, 3, 2, countBlocks);
				} else if (vertical == 2) {
					mineDownLoop(blockX, blockY, blockZ, 1, 1, 2, 3, 2, countBlocks);
				}

				if (canUseItem(blockCount, player) || !EEConfig.cfg.getBoolean("Tools.hammersUseFuel")) {
					if (vertical == 0) {
						switch (playerDirHorizontal) {
							default:
							case NONE:
							case NORTH:
								mineNorthLoop(blockX, blockY, blockZ, 1, 1, 2, 2, 3, breakBlocks);
								break;
							case EAST:
								mineEastLoop(blockX, blockY, blockZ, 1, 1, 3, 2, 2, breakBlocks);
								break;
							case SOUTH:
								mineSouthLoop(blockX, blockY, blockZ, 1, 1, 2, 2, 3, breakBlocks);
								break;
							case WEST:
								mineWestLoop(blockX, blockY, blockZ, 1, 1, 3, 2, 2, breakBlocks);
								break;
						}
					} else if (vertical == 1) {
						mineUpLoop(blockX, blockY, blockZ, 1, 1, 2, 3, 2, breakBlocks);
					} else if (vertical == 2) {
						mineDownLoop(blockX, blockY, blockZ, 1, 1, 2, 3, 2, breakBlocks);
					}
				}
				return true;
			}
			// Metadata 0 (full charge)
			else if (itemstack.getMetadata() == 0) {
				if (vertical == 0) {
					switch (playerDirHorizontal) {
						default:
						case NONE:
						case NORTH:
							mineNorthLoop(blockX, blockY, blockZ, 2, 2, 3, 3, 5, countBlocks);
							break;
						case EAST:
							mineEastLoop(blockX, blockY, blockZ, 2, 2, 5, 3, 3, countBlocks);
							break;
						case SOUTH:
							mineSouthLoop(blockX, blockY, blockZ, 2, 2, 3, 3, 5, countBlocks);
							break;
						case WEST:
							mineWestLoop(blockX, blockY, blockZ, 2, 2, 5, 3, 3, countBlocks);
							break;
					}
				} else if (vertical == 1) {
					mineUpLoop(blockX, blockY, blockZ, 2, 2, 3, 5, 3, countBlocks);
				} else if (vertical == 2) {
					mineDownLoop(blockX, blockY, blockZ, 2, 2, 3, 5, 3, countBlocks);
				}

				if (canUseItem(blockCount, player) || !EEConfig.cfg.getBoolean("Tools.hammersUseFuel")) {
					if (vertical == 0) {
						switch (playerDirHorizontal) {
							default:
							case NONE:
							case NORTH:
								mineNorthLoop(blockX, blockY, blockZ, 2, 2, 3, 3, 5, breakBlocks);
								break;
							case EAST:
								mineEastLoop(blockX, blockY, blockZ, 2, 2, 5, 3, 3, breakBlocks);
								break;
							case SOUTH:
								mineSouthLoop(blockX, blockY, blockZ, 2, 2, 3, 3, 5, breakBlocks);
								break;
							case WEST:
								mineWestLoop(blockX, blockY, blockZ, 2, 2, 5, 3, 3, breakBlocks);
								break;
						}
					} else if (vertical == 1) {
						mineUpLoop(blockX, blockY, blockZ, 2, 2, 3, 5, 3, breakBlocks);
					} else if (vertical == 2) {
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

	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1) {
		return true;
	}
}
