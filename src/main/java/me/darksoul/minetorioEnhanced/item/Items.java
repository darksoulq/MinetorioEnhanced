package me.darksoul.minetorioEnhanced.item;

import com.MT.triggersUtility.TUItems;
import com.MT.xxxtrigger50xxx.MineItems;
import com.github.darksoulq.abyssallib.item.Item;
import com.github.darksoulq.abyssallib.registry.BuiltinRegistries;
import com.github.darksoulq.abyssallib.registry.DeferredRegistry;
import com.github.darksoulq.abyssallib.registry.object.DeferredObject;
import me.darksoul.minetorioEnhanced.MinetorioEnhanced;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;

public class Items {
    public static final DeferredRegistry<Item> ITEMS = DeferredRegistry.create(BuiltinRegistries.ITEMS, MinetorioEnhanced.MODID);

    public static final DeferredObject<Item> GAS_MASK = ITEMS.register("gas_mask", (name, id) -> {
        Item item = new Item(id, Material.PAPER);
        List<String> lore = TUItems.basicLore(ChatColor.GOLD + "Industrial Item");
        lore.add(MineItems.grayBold() + "Wearing this mask prevents suffocation in highly polluted areas.");
        item.stack().editMeta(itemMeta -> itemMeta.setLore(lore));
        item.settings().equippable(EquipmentSlot.HEAD);
        return item;
    });

    public static final DeferredObject<Item> REDSTONE_CRYSTAL = ITEMS.register("redstone_crystal", (name, id) -> {
        Item item = new Item(id, Material.PAPER);
        List<String> lore = TUItems.basicLore(ChatColor.GOLD + "Industrial Component");
        lore.add(MineItems.grayBold() + "Used for crafting machines that utilize redstone");
        item.stack().editMeta(itemMeta -> itemMeta.setLore(lore));
        return item;
    });
}
