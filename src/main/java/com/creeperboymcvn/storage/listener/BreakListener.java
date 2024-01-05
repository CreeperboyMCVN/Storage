/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.creeperboymcvn.storage.listener;

import com.creeperboymcvn.storage.Storage;
import com.creeperboymcvn.storage.StoragePlayer;
import com.creeperboymcvn.storage.utils.StringUtils;
import java.util.Collection;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author long
 */
public class BreakListener implements Listener {
    
    private Storage inst;
    
    public BreakListener(Storage inst) {
        this.inst = inst;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent event) {
        // cancelled ?
        if (event.isCancelled()) return;
        if (event.getBlock().getType() == Material.CHEST) return;
        List<String> itemList = inst.getConfig().getStringList("items");
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        Player player = event.getPlayer();
        StoragePlayer splayer = inst.getPlayerManager().getPlayer(player);
        // check
        if (splayer.getOccupiedSpace() >= splayer.getMaxSpace()) {
            // storage is full
            String fullmsg = StringUtils.message("full");
            player.sendTitle("", fullmsg, 0, 20, 0);
            return;
        }
        if (!splayer.isEnableStorage()) {
            // not enabled yet
            return;
        }
        Collection<ItemStack> drops = event.getBlock().getDrops(item);
        for (ItemStack i: drops) {
            if (itemList.contains(i.getType().toString())) {
                drops.remove(i);
                splayer.add(i.getType().toString(), i.getAmount());
                event.setDropItems(false);
                //player.sendMessage("Added " + i + " " + i.getType() + " to your inv");
                String notifyMsg = StringUtils.message("notify")
                        .replace("%type%", StringUtils.itemName(i.getType().name()))
                        .replace("%amount%", i.getAmount() + "")
                        .replace("%current%", splayer.getOccupiedSpace() + "")
                        .replace("%max%", splayer.getMaxSpace() + "");
                player.sendTitle("", notifyMsg, 0, 20, 0);
            }
        }
        // drop items
        for (ItemStack i: drops) {
            player.getWorld().dropItemNaturally(event.getBlock().getLocation(), i);
        }
    }
    
    
}
