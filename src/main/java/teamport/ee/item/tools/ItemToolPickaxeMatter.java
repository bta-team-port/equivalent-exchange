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

import java.util.ArrayList;
import java.util.List;

public class ItemToolPickaxeMatter extends ItemToolPickaxe implements IToolMatter {
	public static EnumItemToolModes currentToolMode = EnumItemToolModes.DEFAULT;
	private int x, y, z;
	private int blockCount;
	private int vertical;
	private Block block;
	public static List<Block> coalOreBlocks = new ArrayList<>();
	public static List<Block> ironOreBlocks = new ArrayList<>();
	public static List<Block> goldOreBlocks = new ArrayList<>();
	public static List<Block> redstoneOreBlocks = new ArrayList<>();
	public static List<Block> lapisOreBlocks = new ArrayList<>();
	public static List<Block> diamondOreBlocks = new ArrayList<>();

	public ItemToolPickaxeMatter(String name, int id, ToolMaterial enumtoolmaterial) {
		super(name, id, enumtoolmaterial);
		setMaxDamage(2);
	}

	// A separate function to drop the items, so we don't have to paste it over and over.
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

		switch (currentToolMode) {
			case ONE_X_THREE_UPWARD:
				for (int mineY = blockY - 1; mineY < blockY + 2; mineY++) {
					block = world.getBlock(blockX, mineY, blockZ);
					if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE)) {
						dropItems(world, (EntityPlayer) living, blockX, mineY, blockZ);
					}
				}
				break;
			case ONE_X_THREE_SIDEWAYS:
				if (playerDirection == Direction.NORTH || playerDirection == Direction.SOUTH) {
					for (int mineX = blockX - 1; mineX < blockX + 2; mineX++) {
						block = world.getBlock(mineX, blockY, blockZ);
						if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE)) {
							dropItems(world, (EntityPlayer) living, mineX, blockY, blockZ);
						}
					}
				} else if (playerDirection == Direction.EAST || playerDirection == Direction.WEST) {
					for (int mineZ = blockZ - 1; mineZ < blockZ + 2; mineZ++) {
						block = world.getBlock(blockX, blockY, mineZ);
						if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE)) {
							dropItems(world, (EntityPlayer) living, blockX, blockY, mineZ);
						}
					}
				}
				break;
			case ONE_X_THREE_FORWARD:
				if (vertical == 0) {
					if (playerDirection == Direction.NORTH) {
						for (int mineZ = blockZ; mineZ > blockZ - 3; mineZ--) {
							block = world.getBlock(blockX, blockY, mineZ);
							if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE)) {
								dropItems(world, (EntityPlayer) living, blockX, blockY, mineZ);
							}
						}
					} else if (playerDirection == Direction.EAST) {
						for (int mineX = blockX; mineX < blockX + 3; mineX++) {
							block = world.getBlock(mineX, blockY, blockZ);
							if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE)) {
								dropItems(world, (EntityPlayer) living, mineX, blockY, blockZ);
							}
						}
					} else if (playerDirection == Direction.SOUTH) {
						for (int mineZ = blockZ; mineZ < blockZ + 3; mineZ++) {
							block = world.getBlock(blockX, blockY, mineZ);
							if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE)) {
								dropItems(world, (EntityPlayer) living, blockX, blockY, mineZ);
							}
						}
					} else if (playerDirection == Direction.WEST) {
						for (int mineX = blockX; mineX > blockX - 3; mineX--) {
							block = world.getBlock(mineX, blockY, blockZ);
							if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE)) {
								dropItems(world, (EntityPlayer) living, mineX, blockY, blockZ);
							}
						}
					}
				} else if (vertical == 1) {
					for (int mineY = blockY; mineY < blockY + 3; mineY++) {
						block = world.getBlock(blockX, mineY, blockZ);
						if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE)) {
							dropItems(world, (EntityPlayer) living, blockX, mineY, blockZ);
						}
					}
				} else if (vertical == 2) {
					for (int mineY = blockY; mineY > blockY - 3; mineY--) {
						block = world.getBlock(blockX, mineY, blockZ);
						if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE)) {
							dropItems(world, (EntityPlayer) living, blockX, mineY, blockZ);
						}
					}
				}
				break;
		}

		return true;
	}

	private void mineOreInRadius(World world, int blockX, int blockY, int blockZ, Runnable runnable) {
		// Create a 8x8 for loop around a clicked box.
		for (x = blockX - 4; x < blockX + 5; x++) {
			for (y = blockY - 4; y < blockY + 5; y++) {
				for (z = blockZ - 4; z < blockZ + 5; z++) {
					// Then get the blocks from the loop.
					block = world.getBlock(x, y, z);

					// A runnable so we don't have to paste this code over and over.
					if (runnable != null) {
						runnable.run();
					}
				}
			}
		}
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		// Client-side code.
		if (itemstack.getMetadata() < 2) {
			world.playSoundAtEntity(player, "ee.destruct", 0.7f, 1.0f);
		}

		// Server-side code.
		if (!world.isClientSide) {
			// Dummy value.
			blockCount = 0;

			Block clickedBlock = world.getBlock(blockX, blockY, blockZ);

			if (itemstack.getMetadata() < 2) {
				// Check the clicked block ore type. If it matches, then do the runnable code.
				// This one is for coal.
				if (coalOreBlocks.contains(clickedBlock)) {

					// Block count function.
					// This has to be separate but in each ore category.
					for (Block coalOre : coalOreBlocks) {
						mineOreInRadius(world, blockX, blockY, blockZ, () -> {
							if (block != null && block == coalOre) {
								blockCount++;
							}
						});
					}

					// Check if you have the required fuel, or if the config is disabled.
					if (canUseItem(blockCount, player) || !EEConfig.cfg.getBoolean("Tools.pickaxesUseFuel")) {

						// Now go through the entire list of coal ore.
						for (Block coalOre : coalOreBlocks) {
							mineOreInRadius(world, blockX, blockY, blockZ, () -> {
								if (block != null && block == coalOre) {
									ItemStack[] stacks = world.getBlock(x, y, z).getBreakResult(world, EnumDropCause.PROPER_TOOL, x, y, z, world.getBlockMetadata(x, y, z), world.getBlockTileEntity(x, y, z));
									world.setBlockWithNotify(x, y, z, 0);

									if (player.getGamemode().consumeBlocks()) {
										if (stacks != null) {
											for (ItemStack stack : stacks) {
												world.dropItem(x, y, z, stack);
											}
										}
									}
								}
							});
						}
					}
				}

				// Iron.
				if (ironOreBlocks.contains(clickedBlock)) {
					for (Block ironOre : ironOreBlocks) {
						mineOreInRadius(world, blockX, blockY, blockZ, () -> {
							if (block != null && block == ironOre) {
								blockCount++;
							}
						});
					}

					if (canUseItem(blockCount, player) || !EEConfig.cfg.getBoolean("Tools.pickaxesUseFuel")) {
						for (Block ironOre : ironOreBlocks) {
							mineOreInRadius(world, blockX, blockY, blockZ, () -> {
								if (block != null && block == ironOre) {
									ItemStack[] stacks = world.getBlock(x, y, z).getBreakResult(world, EnumDropCause.PROPER_TOOL, x, y, z, world.getBlockMetadata(x, y, z), world.getBlockTileEntity(x, y, z));
									world.setBlockWithNotify(x, y, z, 0);

									if (player.getGamemode().consumeBlocks()) {
										if (stacks != null) {
											for (ItemStack stack : stacks) {
												world.dropItem(x, y, z, stack);
											}
										}
									}
								}
							});
						}
					}
				}

				if (goldOreBlocks.contains(clickedBlock)) {
					for (Block goldOre : goldOreBlocks) {
						mineOreInRadius(world, blockX, blockY, blockZ, () -> {
							if (block != null && block == goldOre) {
								blockCount++;
							}
						});
					}

					if (canUseItem(blockCount, player) || !EEConfig.cfg.getBoolean("Tools.pickaxesUseFuel")) {
						for (Block goldOre : goldOreBlocks) {
							mineOreInRadius(world, blockX, blockY, blockZ, () -> {
								if (block != null && block == goldOre) {
									ItemStack[] stacks = world.getBlock(x, y, z).getBreakResult(world, EnumDropCause.PROPER_TOOL, x, y, z, world.getBlockMetadata(x, y, z), world.getBlockTileEntity(x, y, z));
									world.setBlockWithNotify(x, y, z, 0);

									if (player.getGamemode().consumeBlocks()) {
										if (stacks != null) {
											for (ItemStack stack : stacks) {
												world.dropItem(x, y, z, stack);
											}
										}
									}
								}
							});
						}
					}
				}

				// Redstone
				if (redstoneOreBlocks.contains(clickedBlock)) {
					for (Block redstoneOre : redstoneOreBlocks) {
						mineOreInRadius(world, blockX, blockY, blockZ, () -> {
							if (block != null && block == redstoneOre) {
								blockCount++;
							}
						});
					}

					if (canUseItem(blockCount, player) || !EEConfig.cfg.getBoolean("Tools.pickaxesUseFuel")) {
						for (Block redstoneOre : redstoneOreBlocks) {
							mineOreInRadius(world, blockX, blockY, blockZ, () -> {
								if (block != null && block == redstoneOre) {
									ItemStack[] stacks = world.getBlock(x, y, z).getBreakResult(world, EnumDropCause.PROPER_TOOL, x, y, z, world.getBlockMetadata(x, y, z), world.getBlockTileEntity(x, y, z));
									world.setBlockWithNotify(x, y, z, 0);

									if (player.getGamemode().consumeBlocks()) {
										if (stacks != null) {
											for (ItemStack stack : stacks) {
												world.dropItem(x, y, z, stack);
											}
										}
									}
								}
							});
						}
					}
				}

				// Lapis
				if (lapisOreBlocks.contains(clickedBlock)) {
					for (Block lapisOre : lapisOreBlocks) {
						mineOreInRadius(world, blockX, blockY, blockZ, () -> {
							if (block != null && block == lapisOre) {
								blockCount++;
							}
						});
					}

					if (canUseItem(blockCount, player) || !EEConfig.cfg.getBoolean("Tools.pickaxesUseFuel")) {
						for (Block lapisOre : lapisOreBlocks) {
							mineOreInRadius(world, blockX, blockY, blockZ, () -> {
								if (block != null && block == lapisOre) {
									ItemStack[] stacks = world.getBlock(x, y, z).getBreakResult(world, EnumDropCause.PROPER_TOOL, x, y, z, world.getBlockMetadata(x, y, z), world.getBlockTileEntity(x, y, z));
									world.setBlockWithNotify(x, y, z, 0);

									if (player.getGamemode().consumeBlocks()) {
										if (stacks != null) {
											for (ItemStack stack : stacks) {
												world.dropItem(x, y, z, stack);
											}
										}
									}
								}
							});
						}
					}
				}

				// Finally, diamond.
				if (diamondOreBlocks.contains(clickedBlock)) {
					for (Block diamondOre : diamondOreBlocks) {
						mineOreInRadius(world, blockX, blockY, blockZ, () -> {
							if (block != null && block == diamondOre) {
								blockCount++;
							}
						});
					}

					if (canUseItem(blockCount, player) || !EEConfig.cfg.getBoolean("Tools.pickaxesUseFuel")) {
						for (Block diamondOre : diamondOreBlocks) {
							mineOreInRadius(world, blockX, blockY, blockZ, () -> {
								if (block != null && block == diamondOre) {
									ItemStack[] stacks = world.getBlock(x, y, z).getBreakResult(world, EnumDropCause.PROPER_TOOL, x, y, z, world.getBlockMetadata(x, y, z), world.getBlockTileEntity(x, y, z));
									world.setBlockWithNotify(x, y, z, 0);

									if (player.getGamemode().consumeBlocks()) {
										if (stacks != null) {
											for (ItemStack stack : stacks) {
												world.dropItem(x, y, z, stack);
											}
										}
									}
								}
							});
						}
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

	static {
		coalOreBlocks.add(Block.oreCoalStone);
		coalOreBlocks.add(Block.oreCoalBasalt);
		coalOreBlocks.add(Block.oreCoalLimestone);
		coalOreBlocks.add(Block.oreCoalGranite);
		coalOreBlocks.add(Block.oreNethercoalNetherrack);

		ironOreBlocks.add(Block.oreIronStone);
		ironOreBlocks.add(Block.oreIronBasalt);
		ironOreBlocks.add(Block.oreIronLimestone);
		ironOreBlocks.add(Block.oreIronGranite);

		goldOreBlocks.add(Block.oreGoldStone);
		goldOreBlocks.add(Block.oreGoldBasalt);
		goldOreBlocks.add(Block.oreGoldLimestone);
		goldOreBlocks.add(Block.oreGoldGranite);

		redstoneOreBlocks.add(Block.oreRedstoneStone);
		redstoneOreBlocks.add(Block.oreRedstoneBasalt);
		redstoneOreBlocks.add(Block.oreRedstoneLimestone);
		redstoneOreBlocks.add(Block.oreRedstoneGranite);
		redstoneOreBlocks.add(Block.oreRedstoneGlowingStone);
		redstoneOreBlocks.add(Block.oreRedstoneGlowingBasalt);
		redstoneOreBlocks.add(Block.oreRedstoneGlowingLimestone);
		redstoneOreBlocks.add(Block.oreRedstoneGlowingGranite);

		lapisOreBlocks.add(Block.oreLapisStone);
		lapisOreBlocks.add(Block.oreLapisBasalt);
		lapisOreBlocks.add(Block.oreLapisLimestone);
		lapisOreBlocks.add(Block.oreLapisGranite);

		diamondOreBlocks.add(Block.oreDiamondStone);
		diamondOreBlocks.add(Block.oreDiamondBasalt);
		diamondOreBlocks.add(Block.oreDiamondLimestone);
		diamondOreBlocks.add(Block.oreDiamondGranite);
	}
}
