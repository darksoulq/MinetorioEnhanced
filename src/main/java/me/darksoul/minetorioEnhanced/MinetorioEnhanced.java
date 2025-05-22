package me.darksoul.minetorioEnhanced;

import com.MT.xxxtrigger50xxx.Guide.ItemMenu;
import com.github.darksoulq.abyssallib.event.EventBus;
import com.github.darksoulq.abyssallib.resource.ResourcePack;
import com.github.darksoulq.abyssallib.resource.glyph.GlyphWriter;
import me.darksoul.minetorioEnhanced.device.ExperienceTank;
import me.darksoul.minetorioEnhanced.device.PulseGenerator;
import me.darksoul.minetorioEnhanced.device.Vault;
import me.darksoul.minetorioEnhanced.effect.PollutionEffect;
import me.darksoul.minetorioEnhanced.events.PlayerEvents;
import me.darksoul.minetorioEnhanced.item.Items;
import me.darksoul.minetorioEnhanced.recipe.Recipes;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class MinetorioEnhanced extends JavaPlugin {
    public static final String MODID = "mtenhanced";
    public static MinetorioEnhanced INSTANCE;
    public static Glyphs GLYPHS;

    @Override
    public void onEnable() {
        INSTANCE = this;
        EventBus bus = new EventBus(this);

        bus.register(new PlayerEvents());

        Items.ITEMS.apply();

        PollutionEffect.startTicking();
        new BukkitRunnable() {
            @Override
            public void run() {
                registerItems();
                Recipes.registerAll();
                addDevices();
            }
        }.runTaskLater(this, 3);

        GLYPHS = new Glyphs();
        GlyphWriter.write(MODID);

        new ResourcePack(this, "mtenhanced").generate();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerItems() {
        ItemMenu.addItem(Items.GAS_MASK.get().stack(), ItemMenu.Category.TOOLS);
    }

    private void addDevices() {
        Vault.addDevice();
        ExperienceTank.addDevice();
        PulseGenerator.addDevice();
    }
}
