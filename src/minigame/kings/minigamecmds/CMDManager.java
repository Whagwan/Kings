package minigame.kings.minigamecmds;

import minigame.kings.Main;
import minigame.kings.permissions.PermissionSettings;
import minigame.kings.utilities.MathUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CMDManager implements CommandExecutor {

    public List<SubCommand> commands = new ArrayList<>();

    public CMDManager(Plugin plugin) {
        plugin.getServer().getPluginCommand(Main.plugin_name.toLowerCase()).setExecutor(this);
        plugin.getServer().getPluginCommand(Main.plugin_name.toLowerCase()).setAliases(Arrays.asList(Main.plugin_aliases));
    }

    public void registerCommand(SubCommand command) {
        commands.add(command);
        Main.getInstance().log("&a>>  Registered command '" + command.aliases[0] + "'  <<");
    }

    public void showHelp(CommandSender sender, int page) {
        sender.sendMessage("");
        sender.sendMessage("§6§l  " + Main.plugin_name
                + " Help §a(" + Main.plugin_command_label + " <page>) §8(§c" + page + "§8/§c" + getMaxPages() + "§8)");
        int from = 1;
        if (page > 1)
            from = 8 * (page - 1) + 1;
        int to = 8 * page;
        for (int h = from; h <= to; h++) {
            if (h > commands.size())
                break;
            SubCommand command = commands.get(h - 1);
            sender.sendMessage("    §a•  §7" + command.getUsage() + " §8> §f§o" + command.getDescription());
        }
        sender.sendMessage("");
    }

    private int getMaxPages() {
        int max = 8;
        int i = commands.size();
        if (i % max == 0) return i / max;
        double j = i / 8;
        int h = (int) Math.floor(j * 100) / 100;
        return h + 1;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player) && !(sender instanceof CommandSender))
            return false;

        if (args == null
                || args.length == 0) {
            showHelp(sender, 1);
            return true;
        }
        if (args.length == 1 && MathUtils.isInteger(args[0])) {
            showHelp(sender, Math.max(1, Math.min(Integer.parseInt(args[0]), getMaxPages())));
            return true;
        }

        for (SubCommand command : commands)
            if (command.is(args[0])) {

                if (!sender.hasPermission(command.getPermission())) {
                    sender.sendMessage(PermissionSettings.INVALID_PERMISSION);
                    return true;
                }

                if (sender instanceof Player)
                    command.onPlayerExecute((Player) sender, args);
                else
                    command.onConsoleExecute((ConsoleCommandSender) sender, args);
                return true;
            }
        showHelp(sender, 1);

        return true;
    }
}

