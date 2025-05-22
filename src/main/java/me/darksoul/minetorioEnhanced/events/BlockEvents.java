package me.darksoul.minetorioEnhanced.events;

import com.MT.xxxtrigger50xxx.Devices.Device;
import com.github.darksoulq.abyssallib.event.SubscribeEvent;
import me.darksoul.minetorioEnhanced.device.ExperienceTank;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockEvents {
    @SubscribeEvent
    public void onBreak(BlockBreakEvent event) {
        Device device = Device.getDevice(event.getBlock());
        if (device != null) {
            if (device instanceof ExperienceTank tank) {
                event.getPlayer().setExperienceLevelAndProgress((int) (event.getPlayer().calculateTotalExperiencePoints() + tank.contained));
            }
        }
    }
}
