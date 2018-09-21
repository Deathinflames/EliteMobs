/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.magmaguy.elitemobs.mobcustomizer.displays;

import com.magmaguy.elitemobs.MetadataHandler;
import com.magmaguy.elitemobs.config.ConfigValues;
import com.magmaguy.elitemobs.config.MobCombatSettingsConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class HealthDisplay implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onHitArmorStand(EntityDamageEvent event) {

        if (event.getEntity() instanceof ArmorStand && event.getEntity().hasMetadata(MetadataHandler.ARMOR_STAND_DISPLAY))
            event.setCancelled(true);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHit(EntityDamageEvent event) {

        if (event.isCancelled()) return;

        if (!(event.getEntity() instanceof LivingEntity) || event.getEntity() instanceof ArmorStand) return;

        if (ConfigValues.mobCombatSettingsConfig.getBoolean(MobCombatSettingsConfig.ONLY_SHOW_HEALTH_FOR_ELITE_MOBS)) {

            if (event.getEntity().hasMetadata(MetadataHandler.ELITE_MOB_MD) && event.getEntity() instanceof LivingEntity) {

                displayHealth((LivingEntity) event.getEntity(), event.getFinalDamage());

            }

        } else {

            displayHealth((LivingEntity) event.getEntity(), event.getFinalDamage());

        }

    }

    public static void displayHealth(LivingEntity livingEntity, double damage) {

        if (!ConfigValues.mobCombatSettingsConfig.getBoolean(MobCombatSettingsConfig.DISPLAY_HEALTH_ON_HIT)) return;

        int maxHealth = (int) livingEntity.getMaxHealth();
        int currentHealth = (int) (livingEntity.getHealth() - damage);

        Location entityLocation = new Location(livingEntity.getWorld(), livingEntity.getLocation().getX(),
                livingEntity.getLocation().getY() + livingEntity.getEyeHeight() + 0.5, livingEntity.getLocation().getZ());

        /*
        Dirty fix: armorstands don't render invisibly on their first tick, so it gets moved elsewhere temporarily
         */
        ArmorStand armorStand = (ArmorStand) entityLocation.getWorld().spawnEntity(entityLocation.add(new Vector(0, 50, 0)), EntityType.ARMOR_STAND);

        armorStand.setVisible(false);
        armorStand.setMarker(true);
        armorStand.setCustomName(setHealthColor(currentHealth, maxHealth) + " " + currentHealth);
        armorStand.setCustomNameVisible(true);
        armorStand.setGravity(false);
        MetadataHandler.registerMetadata(armorStand, MetadataHandler.ARMOR_STAND_DISPLAY, true);



        new BukkitRunnable() {

            int taskTimer = 0;

            @Override
            public void run() {

                Location newLocation = new Location(livingEntity.getWorld(), livingEntity.getLocation().getX(),
                        livingEntity.getLocation().getY() + livingEntity.getEyeHeight() + 0.5, livingEntity.getLocation().getZ());

                armorStand.teleport(newLocation);

                taskTimer++;

                if (taskTimer == 15) {

                    cancel();
                    armorStand.removeMetadata(MetadataHandler.ARMOR_STAND_DISPLAY, Bukkit.getPluginManager().getPlugin(MetadataHandler.ELITE_MOBS));
                    armorStand.remove();

                }

            }

        }.runTaskTimer(Bukkit.getPluginManager().getPlugin(MetadataHandler.ELITE_MOBS), 1, 1L);

    }

    /*
    Color progression: Dark green - Green - Red - Dark red
     */
    private static String setHealthColor(int currentHealth, int maxHealth) {

        double healthPercentage = currentHealth * 100 / maxHealth;

        String symbol;
        symbol = "\u2764";

        if (healthPercentage > 85) {

            return ChatColor.RED + symbol + ChatColor.DARK_GREEN;

        }

        if (healthPercentage > 70) {

            return ChatColor.RED + symbol + ChatColor.GREEN;

        }

        if (healthPercentage > 40) {

            return ChatColor.RED + symbol + ChatColor.YELLOW;

        }

        if (healthPercentage > 25) {

            return ChatColor.RED + symbol + ChatColor.RED;

        }

        if (healthPercentage > 0) {

            return ChatColor.RED + symbol + ChatColor.DARK_RED;

        }

        return ChatColor.RED + symbol + ChatColor.DARK_RED;

    }

}
