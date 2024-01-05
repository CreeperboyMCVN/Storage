/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.creeperboymcvn.storage.commands;

import com.creeperboymcvn.storage.Storage;
import com.creeperboymcvn.storage.StoragePlayer;
import com.creeperboymcvn.storage.gui.SellGui;
import com.creeperboymcvn.storage.gui.StorageGui;
import com.creeperboymcvn.storage.utils.StringUtils;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

/**
 *
 * @author long
 */
public class MainCommand implements CommandExecutor, TabExecutor {
    
    private final Storage plugin;
    
    public MainCommand(Storage plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "reload":
                    if (cs.hasPermission("storage.reload")) {
                        plugin.reloadConfig();
                        cs.sendMessage(StringUtils.message("reloaded"));
                        return true;
                    } else {
                        cs.sendMessage(StringUtils.message("noPerm"));
                        return true;
                    }
                    
                case "toggle":
                    if (cs instanceof Player) {
                        Player p = (Player) cs;
                        StoragePlayer sp = plugin.getPlayerManager().getPlayer(p);
                        if (p.hasPermission("storage.use")) {
                            sp.toggleEnableStorage();
                            p.sendMessage(StringUtils.message("toggle")
                                .replace("%state%", sp.isEnableStorage() + ""));
                            return true;
                        } else {
                            p.sendMessage(StringUtils.message("noPerm"));
                            return true;
                        }
                    } else {
                        cs.sendMessage("noConsole");
                        return true;
                    }
                    
                case "setmax":
                    if (!cs.hasPermission("storage.setmax")) {
                        cs.sendMessage(StringUtils.message("noPerm"));
                        return true;
                    }
                    if (args.length < 2) {
                        // not enough args
                        cs.sendMessage(StringUtils.message("setmaxUsage"));
                        return true;
                    } else {
                        try {
                            int amount = Integer.parseInt(args[1]);
                            if (args.length > 2) {
                                // player specified
                                String pname = args[2];
                                Player p = Bukkit.getPlayer(pname);
                                if (p == null) {
                                    // invaild player
                                    cs.sendMessage(StringUtils.message("invalidPlayer"));
                                    return true;
                                } else {
                                    StoragePlayer sp = plugin.getPlayerManager().getPlayer(p);
                                    sp.setMaxSpace(amount);
                                    cs.sendMessage(StringUtils.message("setMaxTarget")
                                    .replace("%amount%", amount + "")
                                    .replace("%target%", pname));
                                    return true;
                                }
                            } else {
                                // set for self
                                if (cs instanceof Player) {
                                    // is player
                                    Player p = (Player) cs; 
                                    StoragePlayer sp = plugin.getPlayerManager().getPlayer(p);
                                    sp.setMaxSpace(amount);
                                    cs.sendMessage(StringUtils.message("setMax")
                                    .replace("%amount%", amount + ""));
                                    return true;
                                } else {
                                    // console
                                    cs.sendMessage(StringUtils.message("noConsole"));
                                    return true;
                                }
                            }
                        } catch (NumberFormatException e) {
                            // invalid arg
                            cs.sendMessage(StringUtils.message("invalidArg"));
                            return true;
                        }
                    }
                    
                case "help":
                    cs.sendMessage(StringUtils.message("help"));
                    return true;
                case "sell":
                    if (cs instanceof Player) {
                        Player player = (Player) cs;
                        StoragePlayer splayer = plugin.getPlayerManager().getPlayer(player);
                        new SellGui(splayer).show();
                        return true;
                    } else {
                        cs.sendMessage(StringUtils.message("noConsole"));
                        return true;
                    }
            }
        } else {
            //open storage
            if (cs instanceof Player) {
                Player player = (Player) cs;
                StoragePlayer splayer = plugin.getPlayerManager().getPlayer(player);
                new StorageGui(splayer).show();
            } else {
                cs.sendMessage(StringUtils.message("noConsole"));
                return true;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmnd, String string, String[] args) {
        String[] subcommand = {"sell" , "toggle", "setmax", "reload", "help"};
        List<String> lscmd = new ArrayList<>(Arrays.asList(subcommand));
        List<String> result = new ArrayList<>();
        StringUtil.copyPartialMatches(args[0], lscmd, result);
        if (args.length > 1) return null;
        return result;
    }
    
}
