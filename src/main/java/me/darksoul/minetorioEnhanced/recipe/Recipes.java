package me.darksoul.minetorioEnhanced.recipe;

import com.MT.xxxtrigger50xxx.Recipes.RecipeData;
import me.darksoul.minetorioEnhanced.MinetorioEnhanced;
import me.darksoul.minetorioEnhanced.item.Items;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class Recipes {
    public static void registerAll() {
        gasMask();
    }

    private static void gasMask() {
        ItemStack result = Items.GAS_MASK.get().stack();
        RecipeData data = RecipeData.getRecipeData(result);
        if (data == null) {
            data = new RecipeData();
            ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(MinetorioEnhanced.INSTANCE, "gas_mask"), result);
            recipe.shape(
                    "ICI",
                    "LGL",
                    "SIS"
            );
            recipe.setIngredient('I', new ItemStack(Material.IRON_INGOT));
            recipe.setIngredient('C', new ItemStack(Material.CHARCOAL));
            recipe.setIngredient('L', new ItemStack(Material.LEATHER));
            recipe.setIngredient('G', new ItemStack(Material.GLASS));
            recipe.setIngredient('S', new ItemStack(Material.STRING));
            data.setRecipe(recipe);
            data.setCraftingDevice("Crafting Table");
            data.setRecipeID(ChatColor.stripColor(result.getItemMeta().getDisplayName().toString()));
        }
    }
}
