package teamport.ee.item.tools;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import teamport.ee.miscallaneous.enums.EnumItemToolModes;
import teamport.ee.miscallaneous.interfaces.IToolMatter;

public class ItemToolPickaxeMatter extends ItemToolPickaxe implements IToolMatter {
	public ItemToolPickaxeMatter(String name, int id, ToolMaterial enumtoolmaterial) {
		super(name, id, enumtoolmaterial);
	}


	@Override
	public EnumItemToolModes getCurrentMode() {
		return null;
	}
}
