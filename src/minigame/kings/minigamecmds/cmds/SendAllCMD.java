package minigame.kings.minigamecmds.cmds;

import minigame.kings.Main;
import minigame.kings.minigamecmds.SubCommand;
import minigame.kings.permissions.PermissionSettings;
import minigame.kings.utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;


public class SendAllCMD extends SubCommand {

    public SendAllCMD() {
        super("Send all to the Kings game.", "", "/kings sendall", "sendall", "sa");
    }

    @Override
    protected void onPlayerExecute(Player sender, String... args) {
        if (args.length == 1) {
            Main.getInstance().broadcast("&aSending all players to Kings game...");
            for (Player player : Bukkit.getOnlinePlayers()) {
                Utilities.sendPlayerToGame(player);
            }
        } else {
            Main.getInstance().sendMessage(PermissionSettings.INVALID_ARGUMENTS, sender);
        }
    }

    @Override
    protected void onConsoleExecute(ConsoleCommandSender sender, String... args) {
        if (args.length == 1) {
            Main.getInstance().broadcast("&aSending all players to Kings game...");
            for (Player player : Bukkit.getOnlinePlayers()) {
                Utilities.sendPlayerToGame(player);
            }
        } else {
            sender.sendMessage("Invalid arguments!");
        }
    }
}