/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.creeperboymcvn.storage.utils;

import com.creeperboymcvn.storage.Storage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author long
 */
public class ConfigManager {
    
    private Storage instance;
    private List<YmlConfig> configs = new ArrayList<>();
    
    public ConfigManager(Storage inst) {
        this.instance = inst;
    }
    
    public void registerConfig(String fileName) {
        for (YmlConfig conf : configs) {
            if (conf.getFileName().equals(fileName)) {
                instance.getLogger().log(Level.SEVERE, "There is a file named {0}", fileName);
                return;
            }
        }
        configs.add(new YmlConfig(fileName));
    }
    
    public void saveDefaultConfig(String fileName) {
        for (YmlConfig conf : configs) {
            if (conf.getFileName().equals(fileName)) {
                conf.saveDefaultConfig();
                return;
            }
        }
        instance.getLogger().log(Level.WARNING, "There is no file named {0}", fileName);
    }
    
    public void reloadConfig(String fileName) {
        for (YmlConfig conf : configs) {
            if (conf.getFileName().equals(fileName)) {
                conf.reloadConfig();
                return;
            }
        }
        instance.getLogger().log(Level.WARNING, "There is no file named {0}", fileName);
    }
    
    public void saveConfig(String fileName) {
        for (YmlConfig conf : configs) {
            if (conf.getFileName().equals(fileName)) {
                conf.saveConfig();
                return;
            }
        }
        instance.getLogger().log(Level.WARNING, "There is no file named {0}", fileName);
    }
    
    public FileConfiguration getConfig(String fileName) {
        for (YmlConfig conf : configs) {
            if (conf.getFileName().equals(fileName)) {
                return conf.getConfig();
            }
        }
        instance.getLogger().log(Level.WARNING, "There is no file named {0}", fileName);
        return null;
    }
    
    public void saveDefaultConfigs() {
        for (YmlConfig conf : configs) {
            conf.saveDefaultConfig();
        }
    }
    
    public void reloadConfigs() {
        for (YmlConfig conf : configs) {
            conf.reloadConfig();    
        }
    }
    
}
