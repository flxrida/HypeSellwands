package fr.syfizz.hypesellwands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import fr.syfizz.hypesellwands.Main;
import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {

    private Main main;
    public PlayerListener(Main main){
        this.main = main;
    }
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent e){
        Player p = (Player) e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null){
            Block block = e.getClickedBlock();
            if (block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST)){
                Chest chest = (Chest) block.getState();
                ItemStack item = new ItemStack(Material.STICK);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(main.getConfig().getString("configuration.itemName").replaceAll("&","§"));
                item.setItemMeta(meta);
                //ICI COMMENCE LA VERIFICATION DE L'ITEM
                if(e.getItem() != null && e.getItem().hasItemMeta()){
                    if(e.getItem().getItemMeta().hasDisplayName()){
                        if(e.getItem().getItemMeta().getDisplayName().equals(meta.getDisplayName())){
                            if(e.getItem().getType().equals(item.getType())){
                                e.setCancelled(true);
                                ItemStack[] inventory = p.getInventory().getContents();
                                p.getInventory().clear();
                                List<ItemStack> chestitems = new ArrayList<ItemStack>();
                                for (ItemStack item1 : chest.getInventory().getContents()){
                                    if (item1 != null){ chestitems.add(item1);}
                                }
                                chest.getInventory().clear();
                                boolean added = false;
                                while (!chestitems.isEmpty()){
                                    if (p.getInventory().firstEmpty() < 0){
                                        Bukkit.dispatchCommand(p, main.getConfig().getString("configuration.sellCommand"));
                                        for (ItemStack item1 : p.getInventory().getContents()){
                                            if(item1 !=null) { chest.getInventory().addItem(item1);}
                                        }
                                        p.getInventory().clear();
                                        added = false;

                                    } else {
                                        p.getInventory().addItem(chestitems.get(0));
                                        chestitems.remove(0);
                                        added = true;
                                    }
                                }
                                if (added){
                                    Bukkit.dispatchCommand(p,main.getConfig().getString("configuration.sellCommand"));
                                    for (ItemStack item1 : p.getInventory().getContents()){
                                        if (item1 != null){
                                            chest.getInventory().addItem(item1);
                                        }
                                    }
                                    p.getInventory().clear();
                                }
                                p.getInventory().setContents(inventory);
                                p.updateInventory();
                            }
                        }
                    }
                }
            }
        }
    }

}
