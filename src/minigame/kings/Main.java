package minigame.kings;

import minigame.kings.kingsarena.Arena;
import minigame.kings.kingsarena.ArenaEvents;
import minigame.kings.kingsarena.kings.SlimeKing;
import minigame.kings.minigamecmds.CMDManager;
import minigame.kings.minigamecmds.cmds.*;
import minigame.kings.utilities.Cuboid;
import minigame.kings.utilities.Utilities;
import minigame.kings.kingsarena.kings.EvilWither;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

    public static String plugin_name = "Kings";
    public static String PERMISSION = plugin_name + ".";
    public static String plugin_command_label = "/" + plugin_name.toLowerCase();
    public static String plugin_verson = "1.0";
    public static String[] plugin_aliases = {plugin_name, "ks"};
    private static Main plugin;

    public CMDManager cmdManager;

    public static Main getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();
        getConfig().options().copyDefaults(true);

        PluginManager pluginManager = getServer().getPluginManager();
        //Kings EVENTS
        pluginManager.registerEvents(new EvilWither(), this);
        pluginManager.registerEvents(new SlimeKing(), this);

        //ARENA EVENTS
        pluginManager.registerEvents(new ArenaEvents(), this);

        cmdManager = new CMDManager(this);
        cmdManager.registerCommand(new ReloadCMD());
        cmdManager.registerCommand(new SpawnCMD());
        cmdManager.registerCommand(new JoinGameCMD());
        cmdManager.registerCommand(new SetSpawnCMD());
        cmdManager.registerCommand(new StartGameCMD());
        cmdManager.registerCommand(new SendAllCMD());
        cmdManager.registerCommand(new LeaveGameCMD());

        log("&6&l" + plugin_name + " &a" + plugin_verson + " &chas been enabled!");

        setup_arena();

        Arena.playable = true;
    }

    public void setup_arena() {
        Arena.arenaLocation = new Location(Bukkit.getWorld("world"),
                getConfig().getInt("spawn_area.x"),
                getConfig().getInt("spawn_area.y"),
                getConfig().getInt("spawn_area.z"));

        Arena.game_arena = new Cuboid(
                new Location(Bukkit.getWorld("world"),
                        getConfig().getInt("spawn_area.point1.x"),
                        getConfig().getInt("spawn_area.point1.y"),
                        getConfig().getInt("spawn_area.point1.z")),
                new Location(Bukkit.getWorld("world"),
                        getConfig().getInt("spawn_area.point2.x"),
                        getConfig().getInt("spawn_area.point2.y"),
                        getConfig().getInt("spawn_area.point2.z"))
        );
    }

    public void log(String messageToLog) {
        Server server = Bukkit.getServer();
        ConsoleCommandSender console = server.getConsoleSender();
        console.sendMessage(Utilities.color(messageToLog));
    }

    public void broadcast(String message) {
        getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void sendMessage(String message, Player... players) {
        for (Player player : players) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }
}