package minigame.kings.kingsarena;

import minigame.kings.Main;
import minigame.kings.kingsarena.kings.armour.KingsGear;
import minigame.kings.utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;


public class ArenaEvents implements Listener {

    private BukkitTask task;
    private ArrayList<WitherSkull> projectiles = new ArrayList<>();

    //##################STOP BLOCK BREAKING#######################

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();

        if (Arena.playersIngame.contains(player)
                || Arena.playersInLobby.contains(player)
                || Arena.spectators.contains(player)) {
            event.setCancelled(true);
        }
    }

    //#######################STOP HUNGER###########################

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {

        Player player = (Player) event.getEntity();

        if (Arena.playersIngame.contains(player)) {
            event.setCancelled(true);
        }
    }

    //##################SPECTATOR MODE RESPAWNING#######################

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {

        Player target = event.getPlayer();

        if (Arena.spectators.contains(target)) {

            event.setRespawnLocation(Arena.arenaLocation);

            target.setGameMode(GameMode.SPECTATOR);

            Main.getInstance().sendMessage("&cYou are now spectating.", target);

            target.setAllowFlight(true);

            for (Player ingame : Arena.playersIngame) {
                ingame.hidePlayer(target);
            }
        }
    }

    //##################EXPLOSION EVENT#######################

    @EventHandler
    public void onRespawn(EntityExplodeEvent event) {

        if (event.getEntity() instanceof TNTPrimed) {
            if (Arena.explosives.contains(event.getEntity())) {
                event.setCancelled(true);
                Arena.explosives.remove(event.getEntity());
            }
        } else if (event.getEntity() instanceof WitherSkull) {
            WitherSkull skull = (WitherSkull) event.getEntity();

            if (projectiles.contains(skull)) {
                event.setCancelled(true);
                event.blockList().clear();
            }
        }
    }

    //#########################DEATH EVENT#############################

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player target = event.getEntity();

        if (!Arena.playersIngame.contains(target)) {
            return;
        }

        event.setDeathMessage("");
        event.getDrops().clear();

        if (Arena.attackers.contains(event.getEntity())) {
            Arena.attackers.remove(target);
            Main.getInstance().sendMessage("&cYour soul was claimed by the " + Arena.king + "!", target);
            for (Player ingame : Arena.playersIngame) {
                Main.getInstance().sendMessage("&c&o" + target.getDisplayName() + " was obliterated by the " + Arena.king + "!", ingame);
            }
            Arena.spectators.add(target);
            Arena.deaths.add(target);
            if (Arena.attackers.size() == 0 || Arena.attackers.isEmpty()) {
                ArenaManager.lose();
            }
            for (Player hearer : Bukkit.getOnlinePlayers()) {
                if (hearer.getLocation().distance(target.getLocation()) < 10) {
                    hearer.playSound(hearer.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
                }
            }
        }
    }

    //#########################FRIENDLY FIRE EVENT#############################

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {

            Player player = (Player) event.getEntity();

            if (Arena.playersIngame.contains(player)) {
                event.setCancelled(true);
            }
        }
    }

    //#########################PICKING UP POWER-UPS#############################

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        if (!Arena.playersIngame.contains(event.getPlayer())) {
            return;
        }

        final Player player = event.getPlayer();

        if (!Arena.spectators.contains(player)) {
            for (ArmorStand stand : Arena.power_ups.keySet()) {
                if (player.getLocation().distance(stand.getLocation()) < 2) {
                    stand.remove();

                    PowerUps powerup = Arena.power_ups.get(stand);
                    Arena.power_ups.remove(stand);

                    if (powerup.equals(PowerUps.HEALTH)) {
                        if (player.getHealth() + 10 > player.getMaxHealth()) {
                            player.setHealth(player.getMaxHealth());
                        } else {
                            player.setHealth(player.getHealth() + 10);
                        }
                    } else if (powerup.equals(PowerUps.UPGRADE)) {
                        player.getInventory().removeItem(KingsGear.kingssword());
                        player.getInventory().addItem(KingsGear.slimeball());
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.getInventory().removeItem(KingsGear.slimeball());
                                player.getInventory().addItem(KingsGear.kingssword());
                            }
                        }, 20 * 5);
                    } else if (powerup.equals(PowerUps.POTIONS)) {
                        for (int i = 1; i < 6; i++) {
                            player.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 16421));
                        }
                        player.sendMessage(Utilities.color("&7&o\"I love me some potions!\""));
                    } else {
                        player.addPotionEffect(powerup.getEffect());
                    }
                    player.sendMessage(Utilities.color("&aYou picked up a " + powerup.label() + " &apower-up!"));
                    player.playSound(player.getLocation(), Sound.ANVIL_USE, 1, 1);
                    return;
                }
            }
        }
    }

    //#########################PREVENT REGEN#############################

    @EventHandler
    public void onHealthRegen(EntityRegainHealthEvent event) {

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (Arena.playersIngame.contains(player)
                    && event.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED)) {
                event.setCancelled(true);
            }
        }
    }

    //#########################BOW EFFECT#############################

      /*@EventHandler
    public void onLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Arrow) {
            final Arrow arrow = (Arrow) event.getEntity();
            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();
                if (!Arena.playersIngame.contains(player)
                        && !player.getName().equalsIgnoreCase("BadEntities")) { //<-- Debug
                    return;
                }
                final WitherSkull skull = arrow.getWorld().spawn(arrow.getLocation(), WitherSkull.class);
                skull.setGlowing(true);
                projectiles.add(skull);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
                    @Override
                    public void run() {
                        skull.teleport(arrow.getLocation());
                        skull.setVelocity(arrow.getVelocity());
                        task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new ArrowSpecialities(skull, arrow), 0, 1);
                    }
                }, 3);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
                    @Override
                    public void run() {
                        skull.remove();
                        task.cancel();
                        projectiles.remove(skull);
                    }
                }, 20*2);
            }
        }
    }*/

    //#########################king DAMAGE#############################

    @EventHandler
    void onkingDamage(EntityDamageByEntityEvent event) {

        if (!event.getEntity().equals(Arena.king_entity)) {
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING
                || event.getCause() == EntityDamageEvent.DamageCause.FIRE
                || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK
                || event.getCause() == EntityDamageEvent.DamageCause.FALL
                || event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            event.setCancelled(true);
        }

        if (event.getDamager() instanceof Player) {
            if (Arena.spectators.contains(event.getDamager())) {
                event.setCancelled(true);
            }
            if (!Arena.playersIngame.contains(event.getDamager())) {
                event.setCancelled(true);
            }
        }

        if (event.getEntity().equals(Arena.king_entity)) {

            LivingEntity king = (LivingEntity) event.getEntity();

            if (Arena.king_entity instanceof MagmaCube) {
                if (event.getDamager() instanceof Arrow) {
                    event.setCancelled(true);
                    return;
                }
            }

            king.setCustomName(Utilities.color(Arena.king + " &a" + Arena.health + " &d❤"));

            if (Arena.health - event.getDamage() <= 0) {

                event.getEntity().remove();
                ArenaManager.death();

                return;
            }

            if ((Arena.health - event.getDamage()) <= (Arena.maxHealth / 2)) {
                if (!Arena.phase2) {
                    Arena.phase2 = true;
                    for (Player player : Arena.playersIngame) {
                        Main.getInstance().sendMessage("&6The " + Arena.king + "&6 is now enraged!", player);
                        Utilities.sendTitle(player, "", "&6&lThe " + Arena.king + "&6 is enraged!", 3);
                    }
                    ((LivingEntity) Arena.king_entity).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 0));
                }
            }

            Arena.health = Arena.health - (int) event.getDamage();

            if (event.getDamager() instanceof Player) {
                Player player = (Player) event.getDamager();
                ArenaManager.showTitleOnDamage(player);

                if (!Arena.attackers.contains(player)) {
                    Arena.attackers.add(player);
                }

                if (Arena.damage.containsKey(player)) {
                    Arena.damage.put(player, Arena.damage.get(player) + (int) event.getDamage());
                } else {
                    Arena.damage.put(player, (int) event.getDamage());
                }
            }

            if (event.getDamager() instanceof Arrow) {

                Arrow arrow = (Arrow) event.getDamager();

                if (!(arrow.getShooter() instanceof Player)) {
                    return;
                }

                Player player = (Player) arrow.getShooter();
                ArenaManager.showTitleOnDamage(player);

                if (!Arena.attackers.contains(player)) {
                    Arena.attackers.add(player);
                }

                if (Arena.damage.containsKey(player)) {
                    Arena.damage.put(player, Arena.damage.get(player) + (int) event.getDamage());
                } else {
                    Arena.damage.put(player, (int) event.getDamage());
                }
            }

        }

        event.setDamage(0);
    }

}
