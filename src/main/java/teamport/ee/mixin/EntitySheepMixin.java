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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import teamport.ee.item.tools.ItemToolShearsMatter;

@Mixin(value = EntitySheep.class, remap = false)
public abstract class EntitySheepMixin extends EntityAnimal {

	@Shadow
	public abstract int getFleeceColor();

	@Shadow
	public abstract boolean getSheared();

	@Shadow
	public abstract void setSheared(boolean flag);

	public EntitySheepMixin(World world) {
		super(world);
	}

	@Inject(method = "interact", at = @At(value = "HEAD"))
	private void ee_shearSheep(EntityPlayer entityplayer, CallbackInfoReturnable<Boolean> cir) {
		// Checks if the player's held item is DM or RM shears.
		ItemStack ee_ItemStack = entityplayer.inventory.getCurrentItem();
		if (ee_ItemStack.getItem() instanceof ItemToolShearsMatter && ee_ItemStack.getMetadata() == 0 && !getSheared()) {
			// If the world is 'client side' and the sheep is sheared then play the transmute sound.
			world.playSoundAtEntity(entityplayer, "ee.transmute", 0.7f, 1.0f);

			if (!world.isClientSide) {
				this.setSheared(true);

				// This spawns 4 to 8 wool blocks instead of the typical 2-4.
				int eeCount = 4 + this.random.nextInt(5);
				for (int i = 0; i < eeCount; ++i) {
					EntityItem entityitem = this.spawnAtLocation(new ItemStack(Block.wool.id, 1, this.getFleeceColor()), 1.0F);
					entityitem.yd += this.random.nextFloat() * 0.05F;
					entityitem.xd += (this.random.nextFloat() - this.random.nextFloat()) * 0.1F;
					entityitem.zd += (this.random.nextFloat() - this.random.nextFloat()) * 0.1F;
				}

				// If the random chance here is 0, clone the sheep! >:D
				if (random.nextInt(4) == 0) {
					EntitySheep newSheep = new EntitySheep(world);
					newSheep.setFleeceColor(this.getFleeceColor());
					newSheep.moveTo(x, y, z, yRot, xRot);
					world.entityJoinedWorld(newSheep);
				}

				ee_ItemStack.setMetadata(2);
			}
		}
	}
}
