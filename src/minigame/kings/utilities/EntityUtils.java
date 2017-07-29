package minigame.kings.utilities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jake on 15/08/2016
 */

public class EntityUtils {

    //======== ENTITIES ========

    public static List<Entity> getEntitiesInRadius(Location center, double radius) {
        List<Entity> entities = new ArrayList<>();
        for (Entity entity : center.getWorld().getEntities())
            if (entity.getLocation().distance(center) <= radius)
                entities.add(entity);
        return entities;
    }

    public static void killEntitiesInRadius(Location center, int radius) {
        for (Entity entity : center.getWorld().getEntities()) {
            if (entity.getLocation().distance(center) <= radius) {
                entity.remove();
            }
        }
    }

    public static void killEntitiesInRadius(Location center, int radius, EntityType type) {
        for (Entity entity : center.getWorld().getEntities()) {
            if (entity.getLocation().distance(center) <= radius) {
                if (entity.getType() == type) {
                    entity.remove();
                }
            }
        }
    }

    public static void killEntitiesInRadius(Player player, int radius) {
        for (Entity entity : player.getLocation().getWorld().getEntities()) {
            if (entity.getLocation().distance(player.getLocation()) <= radius) {
                entity.remove();
            }
        }
    }

    public static void killEntitiesInRadius(Player player, int radius, EntityType type) {
        for (Entity entity : player.getLocation().getWorld().getEntities()) {
            if (entity.getLocation().distance(player.getLocation()) <= radius) {
                if (entity.getType() == type) {
                    entity.remove();
                }
            }
        }
    }

    //======== PLAYER ========

    public static Vector getHorizontalDirection(Player player, double multiplier) {
        Vector vector = new Vector();
        double rotX = (double)player.getLocation().getYaw();
        double rotY = 0;
        vector.setY(-Math.sin(Math.toRadians(rotY)));
        double xz = Math.cos(Math.toRadians(rotY));
        vector.setX((-xz * Math.sin(Math.toRadians(rotX))) * multiplier);
        vector.setZ((xz * Math.cos(Math.toRadians(rotX))) * multiplier);
        return vector;
    }

}