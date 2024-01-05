/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.creeperboymcvn.storage;

import com.creeperboymcvn.storage.utils.Config;
import com.creeperboymcvn.storage.utils.StringUtils;
import java.util.List;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 *
 * @author long
 */
public class StoragePlayer {
    
    private final Player player;
    private final Storage inst = Storage.instance;
    
    public StoragePlayer(Player player) {
        this.player = player;
    }
    
    public int getAmount(String type) {
        String path = player.getUniqueId().toString() + "." + type;
        return inst.getConfigManager().getConfig("data.yml").getInt(path, 0);
    }
    
    public void setAmount(String type, int amount) {
        String path = player.getUniqueId().toString() + "." + type;
        inst.getConfigManager().getConfig("data.yml").set(path, amount);
        inst.getConfigManager().saveConfig("data.yml");
    }
    
    public void add(String type, int amount) {
        int prev = getAmount(type);
        setAmount(type, prev + amount);
    }
    
    public void remove(String type, int amount) {
        int prev = getAmount(type);
        setAmount(type, prev - amount);
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public boolean isEnableStorage() {
        String path = player.getUniqueId().toString() + ".settings.enable";
        return inst.getConfigManager().getConfig("data.yml").getBoolean(path, true);
    }
    
    public void setEnableStorage(boolean enable) {
        String path = player.getUniqueId().toString() + ".settings.enable";
        inst.getConfigManager().getConfig("data.yml").set(path, enable);
        inst.getConfigManager().saveConfig("data.yml");
    }
    
    public void toggleEnableStorage() {
        setEnableStorage(!isEnableStorage());
    }
    
    public int getMaxSpace() {
        String path = player.getUniqueId().toString() + ".settings.space";
        int defaultSpace = inst.getConfig().getInt("defaultSpace");
        return inst.getConfigManager().getConfig("data.yml").getInt(path, defaultSpace);
    }
    
    public void setMaxSpace(int space) {
        String path = player.getUniqueId().toString() + ".settings.space";
        inst.getConfigManager().getConfig("data.yml").set(path, space);
        inst.getConfigManager().saveConfig("data.yml");
    }
    
    public int getOccupiedSpace() {
        List<String> items = inst.getConfig().getStringList("items");   
        int total = 0;
        for (String item: items) {
            total += getAmount(item);
        }
        return total;
    }
    
    public int getEmptySlot() {
        PlayerInventory inv = getPlayer().getInventory();
        ItemStack[] items = inv.getContents();
        int count = 0;
        for (ItemStack i: items) {
            if (i != null && i.getType() != Material.AIR) count++;
        }
        return 36-count;
    }
    
    public void getItems(String type, int amount) {
         if(amount > getAmount(type)) {
             getPlayer().sendMessage(StringUtils.message("notEnoughItem"));
             return;
         }
         if (amount == -1) {
             int maxEmpty = getEmptySlot() * 64;
             int realAmount = 0;
             if (getAmount(type) < maxEmpty) {
                 realAmount = getAmount(type);
             }  else {
                 realAmount = maxEmpty;
             }
             getItems(type, realAmount);
             return;
         }
         if (amount < 0) return;
         int stack = (int) Math.floor(amount / 64);
         int odd = amount % 64;
         for (int i =0; i < stack; i++) {
             getPlayer().getInventory().addItem(new ItemStack(Material.getMaterial(type), 64));
         }
         if (odd > 0) {
             getPlayer().getInventory().addItem(new ItemStack(Material.getMaterial(type), odd));
         }
         remove(type, amount);
         player.sendMessage(StringUtils.message("gotItem")
             .replace("%type%", StringUtils.itemName(type))
             .replace("%amount%", amount + ""));
    }
    
    public void sellItems(String type, int amount) {
        if (amount > getAmount(type)) {
            getPlayer().sendMessage(StringUtils.message("notEnoughItem"));
            return;
        }
        if (amount == -1) {
            sellItems(type, getAmount(type));
            return;
        }
        int price = Config.getPrice(type);
        int totalPrice = amount * price;
        Economy economy = inst.getEconomy();
        economy.depositPlayer(Bukkit.getOfflinePlayer(getPlayer().getUniqueId()), totalPrice);
        remove(type, amount);
        player.sendMessage(StringUtils.message("soldItem")
             .replace("%type%", StringUtils.itemName(type))
             .replace("%amount%", amount + "")
             .replace("%price%", totalPrice + ""));
    }
    
}
