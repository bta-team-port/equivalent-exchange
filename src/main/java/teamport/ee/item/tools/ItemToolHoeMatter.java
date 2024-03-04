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
import net.minecraft.core.world.World;
import teamport.ee.miscallaneous.enums.EnumItemToolModes;
import teamport.ee.miscallaneous.interfaces.IToolMatter;

public class ItemToolHoeMatter extends ItemToolHoe implements IToolMatter {
	public static EnumItemToolModes currentToolMode = EnumItemToolModes.DEFAULT;
	private int x, y, z;
	private int vertical;
	private Block block;

	public ItemToolHoeMatter(String name, int id, ToolMaterial enumtoolmaterial) {
		super(name, id, enumtoolmaterial);
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

	@Override
	public EnumItemToolModes getCurrentMode() {
		return currentToolMode;
	}
}
