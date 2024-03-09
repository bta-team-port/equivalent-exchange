package teamport.ee.item.tools;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolShovel;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import teamport.ee.EEConfig;
import teamport.ee.miscallaneous.enums.EnumItemToolModes;
import teamport.ee.miscallaneous.interfaces.IToolMatter;

public class ItemToolShovelMatter extends ItemToolShovel implements IToolMatter {
	public static EnumItemToolModes currentToolMode = EnumItemToolModes.DEFAULT;
	private int x, y, z;
	private int blockCount;
	private int vertical;
	private Block block;

    public ItemToolShovelMatter(String name, int id, ToolMaterial enumtoolmaterial) {
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
					if (block != null && block.hasTag(BlockTags.MINEABLE_BY_SHOVEL)) {
						dropItems(world, (EntityPlayer) living, blockX, mineY, blockZ);
					}
				}
				break;
			case ONE_X_THREE_SIDEWAYS:
				if (playerDirection == Direction.NORTH || playerDirection == Direction.SOUTH) {
					for (int mineX = blockX - 1; mineX < blockX + 2; mineX++) {
						block = world.getBlock(mineX, blockY, blockZ);
						if (block != null && block.hasTag(BlockTags.MINEABLE_BY_SHOVEL)) {
							dropItems(world, (EntityPlayer) living, mineX, blockY, blockZ);
						}
					}
				} else if (playerDirection == Direction.EAST || playerDirection == Direction.WEST) {
					for (int mineZ = blockZ - 1; mineZ < blockZ + 2; mineZ++) {
						block = world.getBlock(blockX, blockY, mineZ);
						if (block != null && block.hasTag(BlockTags.MINEABLE_BY_SHOVEL)) {
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
							if (block != null && block.hasTag(BlockTags.MINEABLE_BY_SHOVEL)) {
								dropItems(world, (EntityPlayer) living, blockX, blockY, mineZ);
							}
						}
					} else if (playerDirection == Direction.EAST) {
						for (int mineX = blockX; mineX < blockX + 3; mineX++) {
							block = world.getBlock(mineX, blockY, blockZ);
							if (block != null && block.hasTag(BlockTags.MINEABLE_BY_SHOVEL)) {
								dropItems(world, (EntityPlayer) living, mineX, blockY, blockZ);
							}
						}
					} else if (playerDirection == Direction.SOUTH) {
						for (int mineZ = blockZ; mineZ < blockZ + 3; mineZ++) {
							block = world.getBlock(blockX, blockY, mineZ);
							if (block != null && block.hasTag(BlockTags.MINEABLE_BY_SHOVEL)) {
								dropItems(world, (EntityPlayer) living, blockX, blockY, mineZ);
							}
						}
					} else if (playerDirection == Direction.WEST) {
						for (int mineX = blockX; mineX > blockX - 3; mineX--) {
							block = world.getBlock(mineX, blockY, blockZ);
							if (block != null && block.hasTag(BlockTags.MINEABLE_BY_SHOVEL)) {
								dropItems(world, (EntityPlayer) living, mineX, blockY, blockZ);
							}
						}
					}
				} else if (vertical == 1) {
					for (int mineY = blockY; mineY < blockY + 3; mineY++) {
						block = world.getBlock(blockX, mineY, blockZ);
						if (block != null && block.hasTag(BlockTags.MINEABLE_BY_SHOVEL)) {
							dropItems(world, (EntityPlayer) living, blockX, mineY, blockZ);
						}
					}
				} else if (vertical == 2) {
					for (int mineY = blockY; mineY > blockY - 3; mineY--) {
						block = world.getBlock(blockX, mineY, blockZ);
						if (block != null && block.hasTag(BlockTags.MINEABLE_BY_SHOVEL)) {
							dropItems(world, (EntityPlayer) living, blockX, mineY, blockZ);
						}
					}
				}
				break;
		}

		return true;
	}

	private void digHalfCharge(int blockX, int blockY, int blockZ, Runnable runnable) {
		for (x = blockX - 1; x < blockX + 2; x++) {
			for (y = blockY - 1; y < blockY + 2; y++) {
				for (z = blockZ - 1; z < blockZ + 2; z++) {
					if (runnable != null) {
						runnable.run();
					}
				}
			}
		}
	}

	private void digFullCharge(int blockX, int blockY, int blockZ, Runnable runnable) {
		for (x = blockX - 2; x < blockX + 3; x++) {
			for (y = blockY - 2; y < blockY + 3; y++) {
				for (z = blockZ - 2; z < blockZ + 3; z++) {
					if (runnable != null) {
						runnable.run();
					}
				}
			}
		}
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		// Dummy default value.
		blockCount = 0;
		block = world.getBlock(x, y, z);

		final Runnable countBlocks = () -> {
			if (!world.isAirBlock(x, y, z) && world.getBlock(x, y, z).hasTag(BlockTags.MINEABLE_BY_SHOVEL)) {
				++blockCount;
			}
		};

		if (itemstack.getMetadata() <= 2) {
			world.playSoundAtEntity(player, "ee.destruct", 0.7f, 1.0f);
		}

		if (!world.isClientSide) {
			// Get the metadata and set the digging size.
			// Metadata 1 is 3x3.
			if (itemstack.getMetadata() == 1) {
				// First count the blocks.
				digHalfCharge(blockX, blockY, blockZ, countBlocks);

				// Now check if the player can use fuel.
				if (canUseItem(blockCount, player) || !EEConfig.cfg.getBoolean("Tools.shovelsUseFuel")) {
					digHalfCharge(blockX, blockY, blockZ, () -> {
						// Now get the blocks in range and check if they're dig-able.
						block = world.getBlock(x, y, z);
						if (block != null && block.hasTag(BlockTags.MINEABLE_BY_SHOVEL)) {
							dropItems(world, player, x, y, z);
						}
					});
				}
			} else if (itemstack.getMetadata() == 0) {
				digFullCharge(blockX, blockY, blockZ, countBlocks);

				if (canUseItem(blockCount, player) || !EEConfig.cfg.getBoolean("Tools.shovelsUseFuel")) {
					digFullCharge(blockX, blockY, blockZ, () -> {
						block = world.getBlock(x, y, z);
						if (block != null && block.hasTag(BlockTags.MINEABLE_BY_SHOVEL)) {
							dropItems(world, player, x, y, z);
						}
					});
				}
			}
			return true;
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
