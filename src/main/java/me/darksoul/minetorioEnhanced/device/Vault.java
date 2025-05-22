package me.darksoul.minetorioEnhanced.device;

import com.MT.xxxtrigger50xxx.Devices.Device;
import com.MT.xxxtrigger50xxx.Guide.ItemMenu;
import com.MT.xxxtrigger50xxx.MineItems;
import com.MT.xxxtrigger50xxx.MineMain;
import com.MT.xxxtrigger50xxx.MinetorioListener;
import com.MT.xxxtrigger50xxx.Recipes.RecipeData;
import me.darksoul.minetorioEnhanced.MinetorioEnhanced;
import me.darksoul.minetorioEnhanced.item.BlockDisplays;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Vault extends Device {
    public static int MAX = 1024;
    public ItemStack stored;
    public int storedAmount = 0;
    private static String classSpacedName = "Vault";

    public Vault(Location location) {
        super(location);
        this.deviceName = "Vault";
        this.setMaterial("BARREL");
        this.setUseUI(true);
        this.setOpenable(false);
        this.useSpecificInputs = true;
        this.addSpecificInput(new ItemStack(Material.AIR));
        this.setInputSlots(new ArrayList<>(List.of(0)));
        this.setOutputSlots(new ArrayList<>(List.of(1)));
        this.standardMoverBehavior = true;
        this.displayLockedHorizontal = true;
    }

    @Override
    public ArrayList<String> stackDescription() {
        ArrayList<String> var1 = new ArrayList<>();
        var1.add("- Stores A lot of a single item in itself");
        var1.add("- Items can only be taken out by movers");
        var1.add(ChatColor.WHITE + " " + MinetorioEnhanced.GLYPHS.mouse_left.unicode() + " " + MineItems.grayBold() + "with Wire Tool to check amount of stored Items");
        var1.add(ChatColor.WHITE + " " + MinetorioEnhanced.GLYPHS.mouse_right.unicode() + " " + MineItems.grayBold() + "Set the item to store");
        return var1;
    }

    @Override
    public boolean onRightClick(PlayerInteractEvent event) {
        if (event.getItem() == null || event.getClickedBlock() == null) return false;
        if (!event.getItem().getType().isAir()) {
            if (storedAmount > 0 && stored != null) {
                dropItems(event.getClickedBlock().getLocation());
            }
            set(event.getItem());
            return false;
        }
        return false;
    }

    @Override
    public boolean onLeftClick(PlayerInteractEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (stored == null) {
                event.getPlayer().sendMessage(MiniMessage.miniMessage()
                        .deserialize("<red>No item has been set for this vault, shift-right click with an item to set one</red>"));
            } else {
                event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<green>Contains <gold>"
                        + storedAmount
                        + "/" + Vault.MAX
                        + "</gold> Items</green>")
                );
            }
            return false;
        }
        return false;
    }

    @Override
    public void onDeviceRemove() {
        if (storedAmount > 0) {
            dropItems(getLocationREADONLY());
        }
    }

    @Override
    public void updateUI() {
        ItemStack input = inv.getItem(0);
        ItemStack output = inv.getItem(1);
        if (input != null) {
            for (int i = 0; i < input.getAmount(); i++) {
                if (freeSpace() > 0) {
                    add();
                }
            }
            inv.setItem(0, new ItemStack(Material.AIR));
        }
        if (storedAmount == 0 && output != null && !output.isEmpty()) {
            inv.setItem(1, new ItemStack(Material.AIR));
        }
        if (output != null && output.isEmpty() && storedAmount > 0) {
            inv.setItem(1, stored.clone());
            storedAmount--;
        } else if (inv.getItem(1) == null && storedAmount > 0) {
            inv.setItem(1, stored.clone());
            storedAmount--;
        }
    }
    @Override
    public void action() {}

    public void visualUpdate(boolean var1) {
        Material icon = stored == null ? Material.AIR : stored.getType();
        this.visualIconUpdate(var1, icon, 1.0F);
    }

    public void set(ItemStack stack) {
        if (stored != null) {
            this.removeSpecificInput(stored);
        }
        this.storedAmount = 0;
        stored = stack.clone();
        stored.setAmount(1);
        this.addSpecificInput(stored);
    }
    public void add() {
        if (stored == null) return;
        storedAmount++;
    }
    public int freeSpace() {
        return MAX - storedAmount;
    }
    public void dropItems(Location location) {
        if (storedAmount == 0) return;
        ItemStack toDrop = stored.clone();
        for (int i = 0; i < storedAmount; i++) {
            location.getWorld().dropItem(location, toDrop);
        }
    }

    @Override
    public void postCreate(boolean var1) {
        this.setDisplayStackString(BlockDisplays.VAULT.stack());
        this.handleDisplay();
    }

    public static void addDevice() {
        MinetorioListener.deviceClasses.put(classSpacedName, Vault.class);
        ItemMenu.addItem(new Vault(null).getDeviceStack(), ItemMenu.Category.LOGISTICS);
        ItemStack result = new Vault(null).getDeviceStack();
        RecipeData data = RecipeData.getRecipeData(result);
        if (data == null) {
            data = new RecipeData();
            ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(MineMain.getPlugin(), data.getFileName()), result);
            recipe.shape(" I ", " O ", " I ");
            recipe.setIngredient('I', new RecipeChoice.ExactChoice(MineItems.getIndustrialComponent("Iron Plate")));
            recipe.setIngredient('O', new RecipeChoice.ExactChoice(new ItemStack(Material.BARREL)));
            data.setRecipe(recipe);
            data.setCraftingDevice("Basic Assembler");
            data.setRecipeID(ChatColor.stripColor(result.getItemMeta().getDisplayName().toString()));
        }
    }
}
