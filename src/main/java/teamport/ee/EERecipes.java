package teamport.ee;

import net.minecraft.core.block.Block;
import net.minecraft.core.data.DataLoader;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.util.RecipeEntrypoint;

public class EERecipes implements RecipeEntrypoint {
	public static final RecipeNamespace EQUIVALENT = new RecipeNamespace();
	public static final RecipeGroup<RecipeEntryCrafting<?, ?>> WORKBENCH = new RecipeGroup<>(new RecipeSymbol(new ItemStack(Block.workbench)));

	@Override
	public void onRecipesReady() {
		EQUIVALENT.register("workbench", WORKBENCH);
		Registries.RECIPES.register("equivalent", EQUIVALENT);
//		DataLoader.loadRecipes("/assets/ee/recipes/workbench/workbench_blocks.json");
		DataLoader.loadRecipes("/assets/ee/recipes/workbench/workbench_items.json");
		DataLoader.loadRecipes("/assets/ee/recipes/workbench/workbench_philosopher.json");
	}
}
