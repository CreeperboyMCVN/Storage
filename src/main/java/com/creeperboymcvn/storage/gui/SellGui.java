/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.creeperboymcvn.storage.gui;

import com.creeperboymcvn.storage.Storage;
import com.creeperboymcvn.storage.StoragePlayer;
import com.creeperboymcvn.storage.utils.Config;
import com.creeperboymcvn.storage.utils.StringUtils;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.ClickType;
import static org.bukkit.event.inventory.ClickType.LEFT;
import static org.bukkit.event.inventory.ClickType.RIGHT;
import static org.bukkit.event.inventory.ClickType.SHIFT_LEFT;
import static org.bukkit.event.inventory.ClickType.SHIFT_RIGHT;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author long
 */
public final class SellGui {
    
    private ChestGui gui;
    private final Storage inst = Storage.instance;
    private final FileConfiguration config = inst.getConfig();
    private final StoragePlayer splayer;
    
    public SellGui(StoragePlayer splayer) {
        this.splayer = splayer;
        initGui();
    }
    
    public void initGui() {
        int row = Math.floorDiv(config.getStringList("items").size(), 9) + 1;
        List<String> types = config.getStringList("items");
        String title = StringUtils.message("gui.sellTitle")
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
            List<String> rawLore = config.getStringList("message.gui.sellLore");
            List<String> coloredLore = StringUtils.colorAllItems(rawLore);
            List<String> replaced = StringUtils.replace(coloredLore, "%price%", Config.getPrice(type) + "");
            replaced = StringUtils.replace(replaced, "%amount%", splayer.getAmount(type)+"");
            itemMeta.setDisplayName(name);
            itemMeta.setLore(replaced);
            item.setItemMeta(itemMeta);
            
            GuiItem guiItem = new GuiItem(item, event -> {
               ClickType click = event.getClick();
               switch (click) {
                   case LEFT:
                       splayer.sellItems(type, 1);
                       break;
                   case RIGHT:
                       splayer.sellItems(type, 16);
                       break;
                   case SHIFT_LEFT:
                       splayer.sellItems(type, 64);
                       break;
                   case SHIFT_RIGHT:
                       splayer.sellItems(type, -1);
                       break;
                   default:
                       splayer.sellItems(type, 1);
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
