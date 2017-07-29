package minigame.kings.minigamecmds.cmds;

import minigame.kings.Main;
import minigame.kings.kingsarena.Arena;
import minigame.kings.minigamecmds.SubCommand;
import minigame.kings.permissions.PermissionSettings;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;


public class SetSpawnCMD extends SubCommand {

    public SetSpawnCMD() {
        super("Set spawn location.", "", "/kings setspawn", "setspawn", "ss");
    }

    @Override
    protected void onPlayerExecute(Player sender, String... args) {

        if (args.length == 1) {
            Arena.arenaLocation = sender.getLocation();
            Main.getInstance().sendMessage("&cSpawn point set!", sender);
            Arena.playable = true;
        } else {
            Main.getInstance().sendMessage(PermissionSettings.INVALID_ARGUMENTS, sender);
        }

    }

    @Override
    protected void onConsoleExecute(ConsoleCommandSender sender, String... args) {
        sender.sendMessage(PermissionSettings.INVALID_COMMAND_SENDER);
    }
}