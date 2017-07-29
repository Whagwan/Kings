package minigame.kings.kingsarena;

import minigame.kings.utilities.Cuboid;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

import java.util.ArrayList;
import java.util.HashMap;


public class Arena {

    public static ArrayList<Player> playersIngame = new ArrayList<>();
    public static ArrayList<Player> playersInLobby = new ArrayList<>();
    public static ArrayList<Player> spectators = new ArrayList<>();
    public static HashMap<Player, Integer> damage = new HashMap<>();
    public static HashMap<ArmorStand, PowerUps> power_ups = new HashMap<>();

    public static boolean gameStarted;
    public static boolean playable;
    public static boolean gameover;
    public static boolean phase2;
    public static boolean power_up = false;

    public static Entity king_entity;
    public static ArrayList<Entity> spawnedEntities = new ArrayList<>();
    public static ArrayList<TNTPrimed> explosives = new ArrayList<>();

    public static ArrayList<Player> attackers = new ArrayList<>();
    public static ArrayList<Player> deaths = new ArrayList<>();

    public static int health;
    public static int maxHealth;

    public static Location arenaLocation;
    public static String king;

    public static Cuboid game_arena;

    public static int tick = 0;

}
