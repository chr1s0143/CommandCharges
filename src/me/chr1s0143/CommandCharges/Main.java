package me.chr1s0143.CommandCharges;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by chris on 04/02/2015.
 */


public class Main extends JavaPlugin implements Listener{

    SettingsManager settings = SettingsManager.getInstance();
    public static final Logger log = Logger.getLogger("Minecraft");
    private static Main main;
    public static Economy vault = null;
    public int islandres = 50;

    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        boolean economySetup = setupVault();
        if (!economySetup) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    //check if server has vault
    private boolean setupVault() {
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
            if (economyProvider != null) {
                vault = (Economy) economyProvider.getProvider();
            }
            return vault != null;
        } else {
            log.info("[CommandCharges] Could not find Vault. CommandCharges require Vault to process it's economy. - Disabled CommandCharges");
        }
        return false;
    }

    @EventHandler
    public void processCommandEvent(PlayerCommandPreprocessEvent event) {
        Player eplayer = event.getPlayer();
        String playern = eplayer.getName();
        if (eplayer == null) {
            return;
        }
        if (eplayer.hasPermission("CommandCharges.free")) {
            return;
        }
        if (event.getMessage().toLowerCase().startsWith("/" + "island restart")) {
            if (vault.getBalance(eplayer) >= islandres) {
                vault.withdrawPlayer(eplayer.getName(), islandres);
                eplayer.sendMessage(ChatColor.RED + "You have chosen to restart your island. You have been charged" + ChatColor.AQUA + " $50");
            } else {
                eplayer.sendMessage(ChatColor.RED + "You do not have enough money to restart your island! You need" + ChatColor.AQUA + " $50 ");
                event.setCancelled(true);
            }
            return;
        }
        if (event.getMessage().toLowerCase().startsWith("/" + "is restart")) {
            if (vault.getBalance(eplayer) >= islandres) {
                vault.withdrawPlayer(eplayer.getName(), islandres);
                eplayer.sendMessage(ChatColor.RED + "You have chosen to restart your island. You have been charged" + ChatColor.AQUA + " $50");
            } else {
                eplayer.sendMessage(ChatColor.RED + "You do not have enough money to restart your island! You need" + ChatColor.AQUA + " $50 ");
                event.setCancelled(true);
            }
            return;
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                if (cmd.getName().equalsIgnoreCase("commandcharges")) {
                    if (args.length == 0)
                        player.sendMessage(ChatColor.DARK_GREEN + "try doing " + ChatColor.GOLD + "/cc reload ");
                    else if (args[0].equalsIgnoreCase("reload")) {
                        try {
                            settings.reloadConfigFile();
                            player.sendMessage(ChatColor.GREEN + "Config File has been reloaded!");
                        } catch (Exception exc) {
                            exc.printStackTrace();
                            player.sendMessage(ChatColor.RED + "Config file reload encounter a problem. See Console for more info");
                        }
                    } else {
                        player.sendMessage(ChatColor.YELLOW + "CommandCharges commands:");
                        player.sendMessage(ChatColor.GOLD + "/cc reload" + ChatColor.GRAY + ChatColor.BOLD + " - " + ChatColor.RESET
                                + ChatColor.GREEN + "reload the config file");
                    }
                }
            } else {
                player.sendMessage("Only OP's can use this command!");
            }
        }
        return true;
    }
}