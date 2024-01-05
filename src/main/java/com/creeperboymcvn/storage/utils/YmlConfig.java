/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.creeperboymcvn.storage.utils;

import com.creeperboymcvn.storage.Storage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author long
 */
public class YmlConfig {
    
    private final String fileName;
    private FileConfiguration config;
    private final Storage instance = Storage.instance;
    
    public YmlConfig(String fileName) {
        this.fileName = fileName;
    }
    
    public void saveDefaultConfig() {
        final File file = new File(instance.getDataFolder(), this.fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            instance.saveResource(fileName, true);
        }
        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException ex) {
            Logger.getLogger(YmlConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public FileConfiguration getConfig() {
        return this.config;
    }
    
    public void reloadConfig() {
        final File file = new File(instance.getDataFolder(), this.fileName);
        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException ex) {
            Logger.getLogger(YmlConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getFileName() {
        return fileName;
    }
    
    public void saveConfig() {
        try {
            final File file = new File(instance.getDataFolder(), this.fileName);
            getConfig().save(file);
        } catch (IOException ex) {
            Logger.getLogger(YmlConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
