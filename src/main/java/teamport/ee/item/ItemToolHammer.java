package teamport.ee.item;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.world.World;
import teamport.ee.EquivalentExchange;

public class ItemToolHammer extends ItemToolPickaxe {

	public ItemToolHammer(String name, int id, ToolMaterial enumtoolmaterial) {
		super(name, id, enumtoolmaterial);
	}

	@Override
	public boolean onBlockDestroyed(World world, ItemStack itemstack, int id, int blockX, int blockY, int blockZ, EntityLiving living) {
		super.onBlockDestroyed(world, itemstack, id, blockX, blockY, blockZ, living);
		// Get the block breaker's X and Y rotations.
		float yRot = living.yRot % 360;
		float xRot = living.xRot % 360;

		// ROTATION VALUES
		// N - 180
		// E - 270
		// S - 90
		// W - 360

		// Shout out to Hammers by AwesomeBossDJ7 for the modified code!
		// First check if the miner is looking up and down through the X rotation.
		// If it isn't; continue to sided directions. Otherwise, skip to up and down directions.
		if (xRot < 60 && xRot > -60) {
			// North and South checks, rotated 45 degrees to both East and South.
			if ((yRot >= 135 && yRot < 225) || (yRot >= 315 || yRot < 45)) {
				for (int x = blockX - 1; x < blockX + 2; x++) {
					for (int y = blockY - 1; y < blockY + 2; y++) {
						Block block = world.getBlock(x, y, blockZ);

						// Null, mine-able, and bedrock check.
						if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block != Block.bedrock) {
							ItemStack[] stacks = world.getBlock(x, y, blockZ).getBreakResult(world, EnumDropCause.PROPER_TOOL, x, y, blockZ, world.getBlockMetadata(x, y, blockZ), world.getBlockTileEntity(x, y, blockZ));

							// If it passes, set block to 0 and drop all the Itemstacks.
							world.setBlockWithNotify(x, y, blockZ, 0);
							if (stacks != null) {
								for (ItemStack stack : stacks) {
									world.dropItem(x, y, blockZ, stack);
								}
							}
						}
					}
				}
			}
			// East and South checks are also rotated 45 degrees. This time to North and South.
			else if ((yRot >= 225 && yRot < 315) || (yRot >= 45 && yRot < 135)) {
				for (int z = blockZ - 1; z < blockZ + 2; z++) {
					for (int y = blockY - 1; y < blockY + 2; y++) {
						Block block = world.getBlock(blockX, y, z);

						if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block != Block.bedrock) {
							ItemStack[] stacks = world.getBlock(blockX, y, z).getBreakResult(world, EnumDropCause.PROPER_TOOL, blockX, blockY, blockZ, world.getBlockMetadata(blockX, y, z), world.getBlockTileEntity(blockX, y, z));

							world.setBlockWithNotify(blockX, y, z, 0);
							if (stacks != null) {
								for (ItemStack stack : stacks) {
									world.dropItem(blockX, y, z, stack);
								}
							}
						}
					}
				}
			}
		} else {
			for (int x = blockX - 1; x < blockX + 2; x++) {
				for (int z = blockZ - 1; z < blockZ + 2; z++) {
					Block block = world.getBlock(x, blockY, z);

					if (block != null && block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) && block != Block.bedrock) {
						ItemStack[] stacks = world.getBlock(x, blockY, z).getBreakResult(world, EnumDropCause.PROPER_TOOL, x, blockY, z, world.getBlockMetadata(x, blockY, z), world.getBlockTileEntity(x, blockY, z));

						world.setBlockWithNotify(x, blockY, z, 0);
						if (stacks != null) {
							for (ItemStack stack : stacks) {
								world.dropItem(x, blockY, z, stack);
							}
						}
					}
				}
			}
		}
		return true;
	}
}
