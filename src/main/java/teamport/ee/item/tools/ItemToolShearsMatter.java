package teamport.ee.item.tools;

import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.ItemToolShears;
import teamport.ee.miscallaneous.enums.EnumItemToolModes;
import teamport.ee.miscallaneous.interfaces.IToolMatter;

public class ItemToolShearsMatter extends ItemToolShears implements IToolMatter {
	public ItemToolShearsMatter(String name, int id) {
		super(name, id);
		setMaxDamage(2);
	}

	@Override
	public void onBlockSheared(EntityLiving entity, ItemStack itemStack) {

	}

	@Override
	public EnumItemToolModes getCurrentMode() {
		return EnumItemToolModes.DEFAULT;
	}
}
