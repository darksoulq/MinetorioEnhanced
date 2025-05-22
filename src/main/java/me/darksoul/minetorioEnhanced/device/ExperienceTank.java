package me.darksoul.minetorioEnhanced.device;

import com.MT.triggersUtility.TUItems;
import com.MT.xxxtrigger50xxx.Devices.Device;
import com.MT.xxxtrigger50xxx.Guide.ItemMenu;
import com.MT.xxxtrigger50xxx.Guide.MainMenu;
import com.MT.xxxtrigger50xxx.MineItems;
import com.MT.xxxtrigger50xxx.MineMain;
import com.MT.xxxtrigger50xxx.MineUtil;
import com.MT.xxxtrigger50xxx.MinetorioListener;
import com.MT.xxxtrigger50xxx.Recipes.RecipeData;
import me.darksoul.minetorioEnhanced.MinetorioEnhanced;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;

public class ExperienceTank extends Device {
    public static float MAX = 1000;
    public float contained = 0;
    private static String classSpacedName = "Experience Tank";

    public ExperienceTank(Location location) {
        super(location);
        this.setUseUI(true);
        this.setOpenable(true);
        this.deviceName = classSpacedName;
        this.setMaterial("GREEN_STAINED_GLASS");
    }

    @Override
    public ArrayList<String> stackDescription() {
        ArrayList<String> var1 = new ArrayList<>();
        var1.add("- Stores Experience");
        var1.add("- Can store a total of " + MAX + " experience");
        return var1;
    }

    @Override
    public void updateUI() {
        ItemStack info = TUItems.createItem(Material.HOPPER, MineItems.whiteBold() + "Device Information");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(MineUtil.colon(" Stored", contained + " / " + MAX));
        lore.add(" ");
        lore.add(ChatColor.WHITE + " " + MinetorioEnhanced.GLYPHS.mouse_left.unicode() + MineItems.redBold() + " Take 10 EXP");
        lore.add(ChatColor.WHITE + " " + MinetorioEnhanced.GLYPHS.mouse_right.unicode() + MineItems.greenBold() + " Add 10 EXP");
        TUItems.addLore(info, lore);
        ItemStack filler = MainMenu.createGoldPane();
        ItemStack[] fillers = new ItemStack[27];
        fillers[13] = info;

        for (int i = 0; i < 27; ++i) {
            if (i != 13) {
                fillers[i] = filler;
            }
        }
        inv.setContents(fillers);
    }

    @Override
    public void onUIClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof  Player player) {
            if (event.getSlot() == 13 && isAuthorized(player) && event.isRightClick()) {
                if (player.calculateTotalExperiencePoints() > 10 ) {
                    if (MAX - contained != 0) {
                        player.setExperienceLevelAndProgress(player.calculateTotalExperiencePoints() - 10);
                        contained += 10;
                        player.playSound(player,Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                        updateUI();
                    }
                } else {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You do not have enough experience!</red>"));
                }
            } else if (event.getSlot() == 13 && isAuthorized(player) && event.isLeftClick()) {
                if (contained != 0) {
                    player.setExperienceLevelAndProgress(player.calculateTotalExperiencePoints() + 10);
                    contained -= 10;
                    player.playSound(player,Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.5F);
                    updateUI();
                } else {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Not enough experience in tank</red>"));
                }
            }
        }
    }

    @Override
    public void action() {}

    public static void addDevice() {
        MinetorioListener.deviceClasses.put(classSpacedName, ExperienceTank.class);
        ItemMenu.addItem(new ExperienceTank(null).getDeviceStack(), ItemMenu.Category.LOGISTICS);
        ItemStack result = new ExperienceTank(null).getDeviceStack();
        RecipeData data = RecipeData.getRecipeData(result);
        if (data == null) {
            data = new RecipeData();
            ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(MineMain.getPlugin(), data.getFileName()), result);
            recipe.shape("IGI", "G G", "IEI");
            recipe.setIngredient('G', new RecipeChoice.ExactChoice(new ItemStack(Material.GLASS)));
            recipe.setIngredient('E', new RecipeChoice.ExactChoice(MineItems.getIndustrialComponent("Electronic Circuit")));
            recipe.setIngredient('I', new RecipeChoice.ExactChoice(MineItems.getIndustrialComponent("Iron Plate")));
            data.setRecipe(recipe);
            data.setCraftingDevice("Basic Assembler");
            data.setRecipeID(ChatColor.stripColor(result.getItemMeta().getDisplayName().toString()));
        }
    }
}
