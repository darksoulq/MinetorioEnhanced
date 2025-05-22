package me.darksoul.minetorioEnhanced.device;

import com.MT.triggersUtility.TUItems;
import com.MT.xxxtrigger50xxx.Devices.Device;
import com.MT.xxxtrigger50xxx.Devices.MoverIOCenter;
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
import java.util.List;

public class Vault extends Device {

    public static final int MAX = 1024;
    private static final String CLASS_SPACED_NAME = "Vault";

    public transient ItemStack stored;
    public String storedString;
    public int storedAmount = 0;

    public Vault(Location location) {
        super(location);
        this.deviceName = CLASS_SPACED_NAME;
        this.setMaterial("BARREL");
        this.setUseUI(true);
        this.setActionTimer(1);
        this.useSpecificInputs = true;
        this.addSpecificInput(new ItemStack(Material.AIR));
        this.setInputSlots(new ArrayList<>(List.of(0)));
        this.setOutputSlots(new ArrayList<>(List.of(1, 2)));
        this.standardMoverBehavior = true;
        this.displayLockedHorizontal = true;
    }

    @Override
    public ArrayList<String> stackDescription() {
        ArrayList<String> desc = new ArrayList<>();
        desc.add("- Stores a lot of a single item in itself");
        desc.add("- Items can only be taken out by movers");
        desc.add(ChatColor.WHITE + " " + MinetorioEnhanced.GLYPHS.mouse_left.unicode() + " " +
                MineItems.grayBold() + "with Wire Tool to check amount of stored items");
        desc.add(ChatColor.WHITE + " " + MinetorioEnhanced.GLYPHS.mouse_right.unicode() + " " +
                MineItems.grayBold() + "Set the item to store");
        return desc;
    }

    @Override
    public boolean onRightClick(PlayerInteractEvent event) {
        if (event.getPlayer().isSneaking()) return true;
        ItemStack hand = event.getItem();
        if (hand == null || hand.getType().isAir() || event.getClickedBlock() == null) return false;

        if (storedAmount > 0 && stored != null) {
            dropItems(event.getPlayer().getLocation());
        }

        set(hand);
        return false;
    }

    @Override
    public boolean onLeftClick(PlayerInteractEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return false;

        if (stored == null) {
            event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize(
                    "<red>No item has been set for this vault, shift-right click with an item to set one</red>"
            ));
        } else {
            event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize(
                    "<green>Contains <gold>" + storedAmount + "/" + MAX + "</gold> Items</green>"
            ));
        }

        event.setCancelled(true);

        return true;
    }

    @Override
    public void onDeviceRemove() {
        if (storedAmount > 0) {
            dropItems(getLocationREADONLY());
        }
    }

    @Override
    public void updateUI() {
    }

    @Override
    public void action() {
        ItemStack input = inv.getItem(0);
        ItemStack output = inv.getItem(1);

        if (input != null && !input.getType().isAir()) {
            for (int i = 0; i <= input.getAmount(); i++) {
                if (freeSpace() > 0) add();
            }
            inv.setItem(0, null);
        }

        if (storedAmount == 0 && output != null && !output.getType().isAir()) {
            inv.setItem(1, null);
        }

        if ((output == null || output.getType().isAir()) && storedAmount > 0) {
            inv.setItem(1, stored.clone());
            storedAmount--;
        }
    }

    public void visualUpdate(boolean refresh) {
        Material icon = stored == null ? Material.AIR : stored.getType();
        this.visualIconUpdate(refresh, icon, 1.0F);
    }

    public void set(ItemStack stack) {
        if (stack.isSimilar(stored)) return;
        if (stored != null) {
            this.removeSpecificInput(stored);
        }

        this.storedAmount = 0;
        this.stored = stack.clone();
        this.stored.setAmount(1);
        this.storedString = TUItems.createStackString(stored);
        this.addSpecificInput(this.stored);
    }

    public void add() {
        if (stored != null && storedAmount < MAX) {
            storedAmount++;
        }
    }

    public int freeSpace() {
        return MAX - storedAmount;
    }

    public void dropItems(Location location) {
        if (storedAmount == 0 || stored == null) return;

        ItemStack toDrop = stored.clone();
        for (int i = 0; i < storedAmount; i++) {
            location.getWorld().dropItem(location, toDrop);
        }
    }

    @Override
    public void postCreate(boolean isNew) {
        this.setDisplayStackString(BlockDisplays.VAULT.stack());
        this.handleDisplay();
        this.stored = this.storedString != null ? TUItems.createStackFromString(storedString, true) : null;
    }

    public static void addDevice() {
        MinetorioListener.deviceClasses.put(CLASS_SPACED_NAME, Vault.class);
        ItemStack result = new Vault(null).getDeviceStack();
        ItemMenu.addItem(result, ItemMenu.Category.LOGISTICS);

        RecipeData data = RecipeData.getRecipeData(result);
        if (data == null) {
            data = new RecipeData();
            ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(MineMain.getPlugin(), data.getFileName()), result);
            recipe.shape(" I ", " O ", " I ");
            recipe.setIngredient('I', new RecipeChoice.ExactChoice(MineItems.getIndustrialComponent("Iron Plate")));
            recipe.setIngredient('O', new RecipeChoice.ExactChoice(new ItemStack(Material.BARREL)));
            data.setRecipe(recipe);
            data.setCraftingDevice("Basic Assembler");
            data.setRecipeID(ChatColor.stripColor(result.getItemMeta().getDisplayName()));
        }
    }
}
