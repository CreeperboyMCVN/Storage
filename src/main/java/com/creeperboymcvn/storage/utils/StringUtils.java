/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.creeperboymcvn.storage.utils;

import com.creeperboymcvn.storage.Storage;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

/**
 *
 * @author long
 */
public class StringUtils {
    public static String color(String s) {
        Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
        if (Bukkit.getVersion().contains("1.16")) {
            Matcher match = pattern.matcher(s);
            while (match.find()) {
                String color = s.substring(match.start()+1, match.end());
                s = s.replace("&" + color, ChatColor.of(color) + "");
                match = pattern.matcher(s);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    
    public static String message(String path) {
        return color(Storage.instance.getConfig().getString("message." + path));
    }
    
    public static String itemName(String type) {
        return color(Storage.instance.getConfig().getString("itemName." + type, type));
    }
    
    public static List<String> colorAllItems(List<String> list) {
        List<String> res = new ArrayList<>();
        for (String s: list) {
            res.add(color(s));
        }
        return res;
    }
    
    public static List<String> replace(List<String> list, String target, String replacement) {
        List<String> res = new ArrayList<>();
        for (String s: list) {
            res.add(s.replace(target, replacement));
        }
        return res;
    }
    
}
