/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.creeperboymcvn.storage;

import com.creeperboymcvn.storage.utils.ConfigManager;
import com.creeperboymcvn.storage.commands.MainCommand;
import com.creeperboymcvn.storage.listener.BreakListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author long
 */
public class Storage extends JavaPlugin {
    
    public static Storage instance;
    private ConfigManager cm;
    private PlayerManager pm;
    private Economy economyProvider;

    @Override
    public void onEnable() {
        // plugin startup logic
        instance = this;
        cm = new ConfigManager(this);
        loadConfigs();
        registerCommands();
        registerListeners();
        pm = new PlayerManager(this);
    }

    @Override
    public void onDisable() {
        // plugin disable logic
    }
    
    private void loadConfigs() {
        this.saveDefaultConfig();
        this.reloadConfig();
        cm.registerConfig("data.yml");
        cm.saveDefaultConfigs();
        cm.reloadConfigs();
        if (!setupEconomy()) {
            getLogger().severe("Vault not found, disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
    
    private void registerCommands() {
        this.getCommand("kho").setExecutor(new MainCommand(this));
        this.getCommand("kho").setTabCompleter(new MainCommand(this));
    }
    
    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new BreakListener(this), this);
    }
    
    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        getLogger().info("Vault found!");
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economyProvider = rsp.getProvider();
        return economyProvider != null;
    }
    
    public ConfigManager getConfigManager() {
        return cm;
    }
    
    public PlayerManager getPlayerManager() {
        return pm;
    }
    
    public Economy getEconomy() {
        return economyProvider;
    }
    
}
