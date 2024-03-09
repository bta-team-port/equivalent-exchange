package teamport.ee.item.tools;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolHoe;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import teamport.ee.miscallaneous.enums.EnumItemToolModes;
import teamport.ee.miscallaneous.interfaces.IToolMatter;

public class ItemToolHoeMatter extends ItemToolHoe implements IToolMatter {
	public static EnumItemToolModes currentToolMode = EnumItemToolModes.DEFAULT;
	private int x, z;
	private int vertical;

	public ItemToolHoeMatter(String name, int id, ToolMaterial enumtoolmaterial) {
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
							if (block != null && block.hasTag(BlockTags.MINEABLE_BY_HOE)) {
								dropItems(world, (EntityPlayer) living, x, y, blockZ);
							}
						}
					}
				} else if (playerDirection == Direction.EAST || playerDirection == Direction.WEST) {
					for (int z = blockZ - 1; z < blockZ + 2; z++) {
						for (int y = blockY - 1; y < blockY + 2; y++) {
							Block block = world.getBlock(blockX, y, z);

							if (block != null && block.hasTag(BlockTags.MINEABLE_BY_HOE)) {
								dropItems(world, (EntityPlayer) living, blockX, y, z);
							}
						}
					}
				}
			} else {
				for (int x = blockX - 1; x < blockX + 2; x++) {
					for (int z = blockZ - 1; z < blockZ + 2; z++) {
						Block block = world.getBlock(x, blockY, z);

						if (block != null && block.hasTag(BlockTags.MINEABLE_BY_HOE)) {
							dropItems(world, (EntityPlayer) living, x, blockY, z);
						}
					}
				}
			}
		}
		return true;
	}

	// Half-charge hoeing function.
	private void hoeInAreaHalf(int blockX, int blockZ, Runnable runnable) {
		for (x = blockX - 1; x < blockX + 2; x++) {
			for (z = blockZ - 1; z < blockZ + 2; z++) {
				if (runnable != null) {
					runnable.run();
				}
			}
		}
	}

	// Full-charge hoeing function.
	private void hoeInAreaFull(int blockX, int blockZ, Runnable runnable) {
		for (x = blockX - 2; x < blockX + 3; x++) {
			for (z = blockZ - 2; z < blockZ + 3; z++) {
				if (runnable != null) {
					runnable.run();
				}
			}
		}
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		if (world.getBlock(blockX, blockY, blockZ) != null) {
			world.playSoundAtEntity(player, "ee.destruct", 0.7f, 1.0f);
		}

		if (!world.isClientSide) {
			// Metadata 1 (half charge)
			if (itemstack.getMetadata() == 1) {
				hoeInAreaHalf(blockX, blockZ, () -> {
					// This gets the blockID of xyz. Then, it checks if the blocks are hoe-able.
					int id = world.getBlockId(x, blockY, z);
					boolean blocksAreHoeable = id == Block.grass.id || id == Block.dirt.id || id == Block.pathDirt.id || id == Block.grassRetro.id || id == Block.mud.id;


					if (blocksAreHoeable && world.getBlock(x, blockY + 1, z) == null && id != 0) {
						world.setBlockWithNotify(x, blockY, z, Block.farmlandDirt.id);
					}
				});

				return true;
			}

			// Metadata 0 (full charge)
			else if (itemstack.getMetadata() == 0) {
				hoeInAreaFull(blockX, blockZ, () -> {
					int id = world.getBlockId(x, blockY, z);
					boolean blocksAreHoeable = id == Block.grass.id || id == Block.dirt.id || id == Block.pathDirt.id || id == Block.grassRetro.id || id == Block.mud.id;


					if (blocksAreHoeable && world.getBlock(x, blockY + 1, z) == null && id != 0) {
						world.setBlockWithNotify(x, blockY, z, Block.farmlandDirt.id);
					}
				});

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
