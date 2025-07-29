package me.darksoul.minetorioEnhanced.events;

import com.MT.xxxtrigger50xxx.Devices.Device;
import com.github.darksoulq.abyssallib.event.SubscribeEvent;
import io.papermc.paper.event.entity.FishHookStateChangeEvent;
import io.papermc.paper.event.player.PlayerPickBlockEvent;
import me.darksoul.minetorioEnhanced.device.Vault;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerEvents {
    public static List<Player> ONLINE_PLAYERS = new ArrayList<>();

    @SubscribeEvent
    public void onPick(PlayerPickBlockEvent event) {
        Device device = Device.getDevice(event.getBlock());
        if (device != null) {
            if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                event.setCancelled(true);
                HashMap<Integer, ItemStack> remaining = event.getPlayer().getInventory().addItem(device.getDeviceStack());
                if (!remaining.isEmpty()) {
                    event.getPlayer().getInventory().setItem(EquipmentSlot.HAND, device.getDeviceStack());
                }
            }
        }
    }

    @SubscribeEvent
    public void onJoin(PlayerJoinEvent event) {
        ONLINE_PLAYERS.add(event.getPlayer());
    }

    @SubscribeEvent
    public void onLeave(PlayerQuitEvent event) {
        ONLINE_PLAYERS.remove(event.getPlayer());
    }
}
