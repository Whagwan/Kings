package minigame.kings.minigamecmds;

import minigame.kings.permissions.PermissionSettings;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;


public abstract class SubCommand {

    String[] aliases;
    String description, permission, usage;

    public SubCommand(String description, String permission, String usage, String... aliases) {
        this.aliases = aliases;
        this.description = description;
        this.permission = permission;
        this.usage = usage;
    }

    public boolean is(String arg) {
        return Arrays.asList(aliases).contains(arg.toLowerCase());
    }

    public String getUsage() {
        return usage.toLowerCase();
    }

    public String getDescription() {
        return description;
    }

    public String getPermission() {
        return permission;
    }

    protected abstract void onPlayerExecute(Player sender, String... args);

    protected abstract void onConsoleExecute(ConsoleCommandSender sender, String... args);

    protected void notAllowed(CommandSender commandSender) {
        commandSender.sendMessage(PermissionSettings.INVALID_COMMAND_SENDER);
    }

}