package teamport.ee.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.animal.EntityAnimal;
import net.minecraft.core.entity.animal.EntitySheep;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import teamport.ee.item.tools.ItemToolShearsMatter;
import teamport.ee.miscallaneous.FuelEMC;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = EntitySheep.class, remap = false)
public abstract class EntitySheepMixin extends EntityAnimal {
	@Unique
	FuelEMC fuel = new FuelEMC();

	@Shadow
	public abstract int getFleeceColor();

	@Shadow
	public abstract boolean getSheared();

	@Shadow
	public abstract void setSheared(boolean flag);

	public EntitySheepMixin(World world) {
		super(world);
	}

	// Copied function from IToolMatter.
	@Unique
	private boolean ee_canConsumeFuel(int countedItems, EntityPlayer player) {
		if (player != null) {
			int cumulativeFuel = 0;
			List<Integer> indices = new ArrayList<>();
			for (int i = 0; i < player.inventory.mainInventory.length; i++) {
				ItemStack stack = player.inventory.getStackInSlot(i);
				if (stack != null && fuel.getFuelList().containsKey(stack.getItem().id)) {
					cumulativeFuel += fuel.getYield(stack.itemID) * stack.stackSize;
					indices.add(i);
					if (cumulativeFuel >= countedItems) break;
				}
			}
			if (cumulativeFuel >= countedItems || !player.gamemode.consumeBlocks()) {
				int usedFuel = 0;
				for (Integer i : indices) {
					ItemStack stack = player.inventory.getStackInSlot(i);
					while (stack.stackSize > 0 && usedFuel < countedItems) {
						usedFuel += fuel.getYield(stack.itemID);
						stack.consumeItem(player);
					}
					if (stack.stackSize <= 0) {
						player.inventory.mainInventory[i] = null;
					}
				}

				return true;
			}
		}
		return false;
	}

	@Inject(method = "interact", at = @At(value = "HEAD"))
	private void ee_shearSheep(EntityPlayer entityplayer, CallbackInfoReturnable<Boolean> cir) {
        // Checks if the player's held item is DM or RM shears.
		ItemStack ee_ItemStack = entityplayer.inventory.getCurrentItem();

		// This checks if the metadata is 0, or a 'full charge' so to speak.
		// If it's not a full charge, continue to the end where it shears as normal.
		if (ee_ItemStack.getItem() instanceof ItemToolShearsMatter && ee_ItemStack.getMetadata() == 0 && !getSheared()) {

			// If the world is 'client side' and the sheep is sheared then play the transmute sound.
			world.playSoundAtEntity(entityplayer, "ee.transmute", 0.7f, 1.0f);

			if (!world.isClientSide) {
				// This spawns 4 to 8 wool blocks instead of the typical 2-4.
				// But ONLY if you have the fuel (random * 2). Otherwise, shear as normal.
				int eeCount = 4 + this.random.nextInt(5);
				if (ee_canConsumeFuel(eeCount * 2, entityplayer)) {
				for (int i = 0; i < eeCount; ++i) {
						this.setSheared(true);

						EntityItem entityitem = this.spawnAtLocation(new ItemStack(Block.wool.id, 1, this.getFleeceColor()), 1.0F);
						entityitem.yd += this.random.nextFloat() * 0.05F;
						entityitem.xd += (this.random.nextFloat() - this.random.nextFloat()) * 0.1F;
						entityitem.zd += (this.random.nextFloat() - this.random.nextFloat()) * 0.1F;
					}
				} else {
					eeCount = 2 + random.nextInt(3);
					for (int i = 0; i < eeCount; ++i) {
						this.setSheared(true);

						EntityItem entityitem = this.spawnAtLocation(new ItemStack(Block.wool.id, 1, this.getFleeceColor()), 1.0F);
						entityitem.yd += this.random.nextFloat() * 0.05F;
						entityitem.xd += (this.random.nextFloat() - this.random.nextFloat()) * 0.1F;
						entityitem.zd += (this.random.nextFloat() - this.random.nextFloat()) * 0.1F;
					}
				}

				// If the random chance here is 0, and you have 64 fuel, then clone the sheep! >:D
				if (random.nextInt(4) == 0) {
                    int i = 64;

                    if (ee_canConsumeFuel(i, entityplayer)) {
						EntitySheep newSheep = new EntitySheep(world);
						newSheep.setFleeceColor(this.getFleeceColor());
						newSheep.moveTo(x, y, z, yRot, xRot);
						world.entityJoinedWorld(newSheep);
					}
				}

				ee_ItemStack.setMetadata(2);
			}
		} else if (ee_ItemStack.getMetadata() != 0) {
			if (!world.isClientSide) {
				this.setSheared(true);

				int eeCount = 2 + random.nextInt(3);
				for (int i = 0; i < eeCount; ++i) {
					EntityItem entityitem = this.spawnAtLocation(new ItemStack(Block.wool.id, 1,
						getFleeceColor()), 1.0F);
					entityitem.yd += random.nextFloat() * 0.05F;
					entityitem.xd += (random.nextFloat() - random.nextFloat()) * 0.1F;
					entityitem.zd += (random.nextFloat() - random.nextFloat()) * 0.1F;
				}
			}
		}
	}
}
