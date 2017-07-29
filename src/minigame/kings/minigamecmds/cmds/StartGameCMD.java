package minigame.kings.minigamecmds.cmds;

import minigame.kings.Main;
import minigame.kings.kingsarena.Arena;
import minigame.kings.minigamecmds.SubCommand;
import minigame.kings.permissions.PermissionSettings;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;


public class StartGameCMD extends SubCommand {

    public StartGameCMD() {
        super("Start game.", "", "/kings start", "start");
    }

    @Override
    protected void onPlayerExecute(Player sender, String... args) {
        if (args.length != 1) {
            Main.getInstance().sendMessage(PermissionSettings.INVALID_ARGUMENTS, sender);
            return;
        }

        if (Arena.gameStarted) {
            Arena.gameStarted = false;
            Main.getInstance().sendMessage("&cGame timer stopped!", sender);
        } else {
            Arena.gameStarted = true;
            Main.getInstance().sendMessage("&cGame timer started!", sender);
        }

    }

    @Override
    protected void onConsoleExecute(ConsoleCommandSender sender, String... args) {
        sender.sendMessage(PermissionSettings.INVALID_COMMAND_SENDER);
    }
}