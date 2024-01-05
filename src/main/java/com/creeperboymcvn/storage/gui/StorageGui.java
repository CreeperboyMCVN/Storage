/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.creeperboymcvn.storage.gui;

import com.creeperboymcvn.storage.Storage;
import com.creeperboymcvn.storage.StoragePlayer;
import com.creeperboymcvn.storage.utils.StringUtils;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author long
 */
public final class StorageGui {
    private ChestGui gui;
    private final Storage inst = Storage.instance;
    private final FileConfiguration config = inst.getConfig();
    private final StoragePlayer splayer;
    
    public StorageGui(StoragePlayer splayer) {
        this.splayer = splayer;
        initGui();
    }
    
    public void initGui() {
        int row = Math.floorDiv(config.getStringList("items").size(), 9) + 1;
        List<String> types = config.getStringList("items");
        String title = StringUtils.message("gui.title")
                .replace("%player%", splayer.getPlayer().getName())
                .replace("%current%", splayer.getOccupiedSpace() + "")
                .replace("%max%", splayer.getMaxSpace() + "");
        gui = new ChestGui(row, title);
        OutlinePane pane = new OutlinePane(0, 0, 9, row);
        for (String type : types) {
            ItemStack item = new ItemStack(Material.getMaterial(type), 1);
            ItemMeta itemMeta = item.getItemMeta();
            assert itemMeta != null;
            String name = StringUtils.message("gui.name")
                    .replace("%type%", StringUtils.itemName(type));
            List<String> rawLore = config.getStringList("message.gui.lore");
            List<String> coloredLore = StringUtils.colorAllItems(rawLore);
            List<String> replaced = StringUtils.replace(coloredLore, "%amount%", splayer.getAmount(type) + "");
            itemMeta.setDisplayName(name);
            itemMeta.setLore(replaced);
            item.setItemMeta(itemMeta);
            
            GuiItem guiItem = new GuiItem(item, event -> {
               ClickType click = event.getClick();
               switch (click) {
                   case LEFT:
                       splayer.getItems(type, 1);
                       break;
                   case RIGHT:
                       splayer.getItems(type, 16);
                       break;
                   case SHIFT_LEFT:
                       splayer.getItems(type, 64);
                       break;
                   case SHIFT_RIGHT:
                       splayer.getItems(type, -1);
                       break;
                   default:
                       splayer.getItems(type, 1);
                       break;
               }
               refresh();
               event.setCancelled(true);
            });
            pane.addItem(guiItem);
        }
        gui.addPane(pane);
    }
    
    public ChestGui getGui() {
        return gui;
    }
    
    public void refresh() {
        initGui();
        show();
    }
    
    public void show() {
        getGui().show(splayer.getPlayer());
    }
    
}
