package me.darksoul.minetorioEnhanced.device;

import com.MT.xxxtrigger50xxx.Devices.Device;
import com.MT.xxxtrigger50xxx.Guide.ItemMenu;
import com.MT.xxxtrigger50xxx.MineItems;
import com.MT.xxxtrigger50xxx.MineMain;
import com.MT.xxxtrigger50xxx.MinetorioListener;
import com.MT.xxxtrigger50xxx.Recipes.RecipeData;
import me.darksoul.minetorioEnhanced.item.Items;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;

public class PulseGenerator extends Device {
    private int pulseTimer = 0;
    private static String classSpacedName = "Pulse Generator";

    public PulseGenerator(Location location) {
        super(location);
        this.setMaterial("TARGET");
        this.deviceName = classSpacedName;
        this.setActionTimer(1);
        this.setGridRange(8);
        this.setPowerGen(30);
        this.setOpenable(false);
        this.setStoreForm(false);
    }

    @Override
    public ArrayList<String> stackDescription() {
        ArrayList<String> var1 = new ArrayList<>();
        var1.add(MineItems.whiteBold() + "Generates 2x Power of the redstone strength of the block.");
        var1.add(MineItems.whiteBold() + "If block has a redstone signal of 5, then it will generate 10 power.");
        return var1;
    }

    @Override
    public void updateUI() {}

    @Override
    public void action() {
        if (this.getGrid() != null) {
            if (this.pulseTimer >= 10) {
                if (getLocation().getBlock().isBlockPowered() || getLocation().getBlock().isBlockIndirectlyPowered()) {
                    int signalStrength = getLocation().getBlock().getBlockPower();
                    this.setPowerGen(signalStrength * 2);
                    this.pulseTimer = 0;
                    this.getGrid().addPower(this, this.getPowerGen());
                    getLocation().getWorld().playSound(getLocation().clone(), Sound.BLOCK_BEACON_ACTIVATE, 2.0f, 0.19f);
                    this.setProducingPower(true);
                }
            } else {
                if (getLocation().getBlock().isBlockPowered() || getLocation().getBlock().isBlockIndirectlyPowered()) {
                    int signalStrength = getLocation().getBlock().getBlockPower();
                    this.setPowerGen(signalStrength * 2);
                    this.pulseTimer += 1;
                    this.setProducingPower(false);
                } else {
                    int signalStrength = getLocation().getBlock().getBlockPower();
                    this.setPowerGen(signalStrength * 2);
                    this.pulseTimer = 0;
                    this.setProducingPower(false);
                }
            }
        }
    }

    public static void addDevice() {
        MinetorioListener.deviceClasses.put(classSpacedName, PulseGenerator.class);
        ItemMenu.addItem(new PulseGenerator(null).getDeviceStack(), ItemMenu.Category.LOGISTICS);
        ItemStack result = new PulseGenerator(null).getDeviceStack();
        RecipeData data = RecipeData.getRecipeData(result);
        if (data == null) {
            data = new RecipeData();
            ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(MineMain.getPlugin(), data.getFileName()), result);
            recipe.shape(" I ", "ICI", "QIQ");
            recipe.setIngredient('I', new RecipeChoice.ExactChoice(MineItems.getIndustrialComponent("Iron Plate")));
            recipe.setIngredient('C', new RecipeChoice.ExactChoice(Items.REDSTONE_CRYSTAL.get().stack()));
            recipe.setIngredient('Q', new RecipeChoice.ExactChoice(new ItemStack(Material.QUARTZ)));
            data.setRecipe(recipe);
            data.setCraftingDevice("Basic Assembler");
            data.setRecipeID(ChatColor.stripColor(result.getItemMeta().getDisplayName().toString()));
        }
    }
}
