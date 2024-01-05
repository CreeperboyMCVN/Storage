/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.creeperboymcvn.storage.utils;

import com.creeperboymcvn.storage.Storage;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author long
 */
public class Config {
    
    private static final FileConfiguration config = Storage.instance.getConfig();
    
    public static int getPrice(String type) {
        return config.getInt("price."+ type, 1);
    }
    
}
