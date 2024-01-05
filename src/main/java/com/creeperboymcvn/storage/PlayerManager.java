/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.creeperboymcvn.storage;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

/**
 *
 * @author long
 */
public class PlayerManager {
    
    private List<StoragePlayer> players = new ArrayList<>();
    private Storage inst;
    
    public PlayerManager(Storage instance) {
        this.inst = instance;
    }
    
    public StoragePlayer register(Player player) {
        StoragePlayer splayer = new StoragePlayer(player);
        players.add(splayer);
        return splayer;
    }
    
    public StoragePlayer getPlayer(Player p) {
        for (StoragePlayer player : players) {
            if (player.getPlayer().equals(p)) {
                return player;
            }
        }
        return register(p);
    } 
    
    
}
