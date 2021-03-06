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

package com.magmaguy.elitemobs.mobpowers.majorpowers;

import com.magmaguy.elitemobs.MetadataHandler;
import com.magmaguy.elitemobs.mobpowers.offensivepowers.AttackArrow;
import com.magmaguy.elitemobs.powerstances.MajorPowerPowerStance;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SkeletonTrackingArrow extends MajorPowers implements Listener {

    String powerMetadata = MetadataHandler.SKELETON_TRACKING_ARROW_MD;

    @Override
    public void applyPowers(Entity entity) {

        MetadataHandler.registerMetadata(entity, powerMetadata, true);
        MajorPowerPowerStance majorPowerStanceMath = new MajorPowerPowerStance();
        majorPowerStanceMath.itemEffect(entity);
        repeatingTrackingArrowTask(entity);

    }

    @Override
    public boolean existingPowers(Entity entity) {

        return entity.hasMetadata(powerMetadata);

    }

    private void repeatingTrackingArrowTask(Entity entity) {

        new BukkitRunnable() {

            @Override
            public void run() {

                if (!entity.isValid() || entity.isDead()) {

                    cancel();
                    return;

                }

                for (Entity nearbyEntity : entity.getNearbyEntities(20, 20, 20))
                    if (nearbyEntity instanceof Player)
                        if (((Player) nearbyEntity).getGameMode().equals(GameMode.ADVENTURE) ||
                                ((Player) nearbyEntity).getGameMode().equals(GameMode.SURVIVAL)) {
                            Arrow arrow = AttackArrow.shootArrow(entity, (Player) nearbyEntity);
                            arrow.setVelocity(arrow.getVelocity().multiply(0.1));
                            arrow.setGravity(false);
                            trackingArrowLoop((Player) nearbyEntity, arrow);
                        }


            }

        }.runTaskTimer(MetadataHandler.PLUGIN, 0, 20 * 8);

    }

    public static List<Arrow> trackingArrowList = new ArrayList();

    private static void trackingArrowLoop(Player player, Arrow arrow) {

        trackingArrowList.add(arrow);

        new BukkitRunnable() {

            int counter = 0;

            @Override
            public void run() {

                if (player.isValid() && !player.isDead() && arrow.isValid() && arrow.getWorld().equals(player.getWorld())
                        && player.getLocation().distanceSquared(arrow.getLocation()) < 900 && !arrow.isOnGround()) {

                    if (counter % 10 == 0) {

                        arrow.setVelocity(arrow.getVelocity().add(arrowAdjustmentVector(arrow, player)));

                    }

                    arrow.getWorld().spawnParticle(Particle.FLAME, arrow.getLocation(), 10, 0.01, 0.01, 0.01, 0.01);

                } else {

                    arrow.setGravity(true);
                    trackingArrowList.remove(arrow);
                    cancel();

                }

                if (counter > 20 * 60) {
                    arrow.setGravity(true);
                    trackingArrowList.remove(arrow);
                    cancel();
                }

                counter++;

            }

        }.runTaskTimer(MetadataHandler.PLUGIN, 0, 1);

    }

    private static Vector arrowAdjustmentVector(Arrow arrow, Player player) {

        return player.getEyeLocation().subtract(arrow.getLocation()).toVector()
                .normalize().multiply(0.1);

    }

}
