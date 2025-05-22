package me.darksoul.minetorioEnhanced.effect;

import com.MT.xxxtrigger50xxx.API.MTAPI;
import com.MT.xxxtrigger50xxx.Pollution.SuperChunk;
import com.github.darksoulq.abyssallib.item.Item;
import me.darksoul.minetorioEnhanced.MinetorioEnhanced;
import me.darksoul.minetorioEnhanced.events.PlayerEvents;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PollutionEffect {
    private static final Random random = new Random();

    private static final Map<Player, Integer> PLAYER_OXYGEN = new HashMap<>();
    private static final Map<Player, Integer> PLAYER_DAMAGE_TICKS = new HashMap<>();
    private static final int POLLUTION_THRESHOLD_SUFFOCATE = 500;
    private static final int DAMAGE_INTERVAL = 10;
    private static final int TICK_INTERVAL = 1;

    public static void startTicking() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : PlayerEvents.ONLINE_PLAYERS) {
                    handlePlayerSuffocate(player);
                }
            }
        }.runTaskTimer(MinetorioEnhanced.INSTANCE, 0, TICK_INTERVAL);
    }

    public static void handlePlayerSuffocate(Player player) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            clearPlayerState(player);
            return;
        }

        SuperChunk chunk = MTAPI.getSuperChunk(player.getLocation());
        boolean polluted = chunk.getPollution() > POLLUTION_THRESHOLD_SUFFOCATE;
        boolean wearingHelmet = Item.from(player.getInventory().getHelmet()) != null;

        if (polluted && !wearingHelmet) {
            int remainingOxygen = PLAYER_OXYGEN.getOrDefault(player, player.getMaximumAir()) - 1;
            PLAYER_OXYGEN.put(player, remainingOxygen);
            player.setRemainingAir(remainingOxygen);
        } else {
            clearPlayerState(player);
            return;
        }

        int oxygen = PLAYER_OXYGEN.getOrDefault(player, player.getRemainingAir());
        if (oxygen <= 0 || player.getRemainingAir() <= 0) {
            int damageTicks = PLAYER_DAMAGE_TICKS.getOrDefault(player, 0) + 1;
            if (damageTicks >= DAMAGE_INTERVAL) {
                player.damage(1, DamageSource.builder(DamageType.IN_WALL).build());
                damageTicks = 0;
            }
            PLAYER_DAMAGE_TICKS.put(player, damageTicks);
        } else {
            PLAYER_DAMAGE_TICKS.put(player, 0);
        }
    }

    private static void clearPlayerState(Player player) {
        PLAYER_OXYGEN.remove(player);
        PLAYER_DAMAGE_TICKS.put(player, 0);
    }
}
