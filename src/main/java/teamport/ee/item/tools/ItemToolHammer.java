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

	public ItemToolHammer(String name, int id, ToolMaterial enumtoolmaterial) {
		super(name, id, enumtoolmaterial);
		setMaxDamage(2);
		showFullDurability();
	}

	// A separate function to drop the items, so we don't have to paste it over and over again.
	private void dropItems(World world, EntityPlayer entityPlayer, int x, int y, int z) {
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

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		// First check if there's a targeted block exists and is mine-able.
		// If it exists then play the 'destruct' sound.
		if (world.getBlock(blockX, blockY, blockZ) != null && world.getBlock(blockX, blockY, blockZ).hasTag(BlockTags.MINEABLE_BY_PICKAXE)) {
			world.playSoundAtEntity(entityplayer, "ee.destruct", 0.7f, 1.0f);
		}
		if (!world.isClientSide) {
			int blockCount = 0;
			float wrapY = MathHelper.wrapDegrees(Math.round(entityplayer.yRot));
			float xRot = entityplayer.xRot % 360;
			if (itemstack.getMetadata() == 1) {
				if (xRot < 60 && xRot > -60) {
					// North
					if ((wrapY >= 135 && wrapY < 225)) {
						for (int x = blockX - 1; x < blockX + 2; x++) {
							for (int y = blockY - 1; y < blockY + 2; y++) {
								for (int z = blockZ; z > blockZ - 3; z--) {
									// Raise the block count per block mined.
									++blockCount;
									Block block = world.getBlock(x, y, z);

									if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block != Block.bedrock) {
										dropItems(world, entityplayer, x, y, z);
									}
								}
							}
						}
					}
					// East
					else if (wrapY >= 225 && wrapY < 315) {
						for (int x = blockX; x < blockX + 3; x++) {
							for (int y = blockY - 1; y < blockY + 2; y++) {
								for (int z = blockZ - 1; z < blockZ + 2; z++) {
									++blockCount;
									Block block = world.getBlock(x, y, z);

									if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block != Block.bedrock) {
										dropItems(world, entityplayer, x, y, z);
									}
								}
							}
						}
					}
					// South
					else if (wrapY >= 315 || wrapY < 45) {
						for (int x = blockX - 1; x < blockX + 2; x++) {
							for (int y = blockY - 1; y < blockY + 2; y++) {
								for (int z = blockZ; z < blockZ + 3; z++) {
									++blockCount;
									Block block = world.getBlock(x, y, z);

									if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block != Block.bedrock) {
										dropItems(world, entityplayer, x, y, z);
									}
								}
							}
						}
					}
					// West
					else if (wrapY >= 45 && wrapY < 135) {
						for (int x = blockX; x > blockX - 3; x--) {
							for (int y = blockY - 1; y < blockY + 2; y++) {
								for (int z = blockZ - 1; z < blockZ + 2; z++) {
									++blockCount;
									Block block = world.getBlock(x, y, z);

									if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block != Block.bedrock) {
										dropItems(world, entityplayer, x, y, z);
									}
								}
							}
						}
					}
				} else if (xRot <= -45) {
					for (int x = blockX - 1; x < blockX + 2; x++) {
						for (int y = blockY; y < blockY + 3; y++) {
							for (int z = blockZ - 1; z < blockZ + 2; z++) {
								++blockCount;
								Block block = world.getBlock(x, y, z);

								if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block != Block.bedrock) {
									dropItems(world, entityplayer, x, y, z);
								}
							}
						}
					}
				} else if (xRot >= 45) {
					for (int x = blockX - 1; x < blockX + 2; x++) {
						for (int y = blockY; y > blockY - 3; y--) {
							for (int z = blockZ - 1; z < blockZ + 2; z++) {
								++blockCount;
								Block block = world.getBlock(x, y, z);

								if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block != Block.bedrock) {
									dropItems(world, entityplayer, x, y, z);
								}
							}
						}
					}
				}
			} else if (itemstack.getMetadata() == 0) {
				if (xRot < 60 && xRot > -60) {
					// North
					if ((wrapY >= 135 && wrapY < 225)) {
						for (int x = blockX - 2; x < blockX + 3; x++) {
							for (int y = blockY - 2; y < blockY + 3; y++) {
								for (int z = blockZ; z > blockZ - 5; z--) {
									++blockCount;
									Block block = world.getBlock(x, y, z);

									if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block != Block.bedrock) {
										dropItems(world, entityplayer, x, y, z);
									}
								}
							}
						}
					}
					// East
					else if (wrapY >= 225 && wrapY < 315) {
						for (int x = blockX; x < blockX + 5; x++) {
							for (int y = blockY - 2; y < blockY + 3; y++) {
								for (int z = blockZ - 2; z < blockZ + 3; z++) {
									++blockCount;
									Block block = world.getBlock(x, y, z);

									if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block != Block.bedrock) {
										dropItems(world, entityplayer, x, y, z);
									}
								}
							}
						}
					}
					// South
					else if (wrapY >= 315 || wrapY < 45) {
						for (int x = blockX - 2; x < blockX + 3; x++) {
							for (int y = blockY - 2; y < blockY + 3; y++) {
								for (int z = blockZ; z < blockZ + 5; z++) {
									++blockCount;
									Block block = world.getBlock(x, y, z);

									if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block != Block.bedrock) {
										dropItems(world, entityplayer, x, y, z);
									}
								}
							}
						}
					}
					// West
					else if (wrapY >= 45 && wrapY < 135) {
						for (int x = blockX; x > blockX - 5; x--) {
							for (int y = blockY - 2; y < blockY + 3; y++) {
								for (int z = blockZ - 2; z < blockZ + 3; z++) {
									++blockCount;
									Block block = world.getBlock(x, y, z);

									if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block != Block.bedrock) {
										dropItems(world, entityplayer, x, y, z);
									}
								}
							}
						}
					}
				} else if (xRot <= -45) {
					for (int x = blockX - 2; x < blockX + 3; x++) {
						for (int y = blockY; y < blockY + 5; y++) {
							for (int z = blockZ - 2; z < blockZ + 3; z++) {
								++blockCount;
								Block block = world.getBlock(x, y, z);

								if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block != Block.bedrock) {
									dropItems(world, entityplayer, x, y, z);
								}
							}
						}
					}
				} else if (xRot >= 45) {
					for (int x = blockX - 2; x < blockX + 3; x++) {
						for (int y = blockY; y > blockY - 5; y--) {
							for (int z = blockZ - 2; z < blockZ + 3; z++) {
								++blockCount;
								Block block = world.getBlock(x, y, z);

								if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block != Block.bedrock) {
									dropItems(world, entityplayer, x, y, z);
								}
							}
						}
					}
				}
				canUseItem(blockCount, entityplayer);
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
