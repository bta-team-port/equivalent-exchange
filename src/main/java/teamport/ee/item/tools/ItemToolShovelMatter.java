package teamport.ee.item.tools;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolShovel;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import teamport.ee.miscallaneous.enums.EnumItemToolModes;
import teamport.ee.miscallaneous.interfaces.IToolMatter;

public class ItemToolShovelMatter extends ItemToolShovel implements IToolMatter {
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
	public boolean onBlockDestroyed(World world, ItemStack itemstack, int id, int x, int y, int z, EntityLiving living) {
		return true;
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		if (itemstack.getMetadata() <= 2) {
			world.playSoundAtEntity(player, "ee.destruct", 0.7f, 1.0f);
		}

		if (!world.isClientSide) {
			// Get the metadata and set the digging size.
			// Metadata 1 is 3x3.
            Block block;
            int z;
            int x;
            int y;
            if (itemstack.getMetadata() == 1) {
				for (x = blockX - 1; x < blockX + 2; x++) {
					for (y = blockY - 1; y < blockY + 2; y++) {
						for (z = blockZ - 1; z < blockZ + 2; z++) {
							// Now get the blocks in range and check if they're diggable.
							block = world.getBlock(x, y, z);
							if (block != null && block.hasTag(BlockTags.MINEABLE_BY_SHOVEL)) {
								dropItems(world, player, x, y, z);
							}
						}
					}
				}
			} else if (itemstack.getMetadata() == 0) {
				for (x = blockX - 2; x < blockX + 3; x++) {
					for (y = blockY - 2; y < blockY + 3; y++) {
						for (z = blockZ - 2; z < blockZ + 3; z++) {
							// Now get the blocks in range and check if they're diggable.
							block = world.getBlock(x, y, z);
							if (block != null && block.hasTag(BlockTags.MINEABLE_BY_SHOVEL)) {
								dropItems(world, player, x, y, z);
							}
						}
					}
				}
			}

			return true;
		}

		return false;
	}

	@Override
	public EnumItemToolModes getCurrentMode() {
		return EnumItemToolModes.DEFAULT;
	}
}
