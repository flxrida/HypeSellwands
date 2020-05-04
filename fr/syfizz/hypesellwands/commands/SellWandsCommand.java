package fr.syfizz.hypesellwands.commands;

import fr.syfizz.hypesellwands.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SellWandsCommand implements CommandExecutor {
    private Main main;
    public SellWandsCommand(Main main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1){
            sender.sendMessage(main.getConfig().getString("messages.tooFewArguments").replaceAll("&","§"));
            return true;
        } else if (args.length > 2){
            sender.sendMessage(main.getConfig().getString("messages.tooMuchArguments").replaceAll("&","§"));
            return true;
        } else if (args.length==1) {
            if (sender instanceof Player){
                if (args[0].equalsIgnoreCase("give")){
                    if(sender.hasPermission(main.getConfig().getString("configuration.selfGivePermission"))){
                        ItemStack wand = new ItemStack(Material.STICK);
                        ItemMeta wandmeta = wand.getItemMeta();
                        wandmeta.setDisplayName(main.getConfig().getString("configuration.itemName").replaceAll("&","§"));
                        List<String> lore = (List<String>) main.getConfig().getList("configuration.itemLore");
                        List<String> finalLore = new ArrayList<>();
                        lore.forEach(s -> finalLore.add(ChatColor.translateAlternateColorCodes('&', s)));
                        wandmeta.setLore(finalLore);
                        wand.setItemMeta(wandmeta);
                        ((Player) sender).getInventory().addItem(wand);
                        ((Player) sender).updateInventory();
                        sender.sendMessage(main.getConfig().getString("messages.successfullySelfGive").replaceAll("&","§"));
                        return true;
                    } else {
                        sender.sendMessage(main.getConfig().getString("messages.missingPermission").replaceAll("&","§"));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("reload") && sender.hasPermission(main.getConfig().getString("configuration.pluginReloadPermission"))){
                    main.reloadConfig();
                    return true;
                }
            } else {
                sender.sendMessage("Cette commande ne peut pas être exécutée par la Console.");
                return true;
            }
        } else if (args.length==2) {
            if (args[0].equalsIgnoreCase("give")){
                if(sender.hasPermission(main.getConfig().getString("configuration.giveOtherPermission"))){
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (target==null){
                        String msg = main.getConfig().getString("messages.incorrectTarget").replaceAll("&","§");
                        sender.sendMessage(msg.replaceAll("%player%", args[1]));
                        return true;
                    } else {
                        ItemStack wand = new ItemStack(Material.STICK);
                        ItemMeta wandmeta = wand.getItemMeta();
                        wandmeta.setDisplayName(main.getConfig().getString("configuration.itemName").replaceAll("&","§"));
                        List<String> lore = (List<String>) main.getConfig().getList("configuration.itemLore");
                        List<String> finalLore = new ArrayList<>();
                        lore.forEach(s -> finalLore.add(ChatColor.translateAlternateColorCodes('&', s)));
                        wandmeta.setLore(finalLore);
                        wand.setItemMeta(wandmeta);
                        target.getInventory().addItem(wand);
                        target.updateInventory();
                        String msg = main.getConfig().getString("messages.successfullyGiveTo").replaceAll("&","§");
                        sender.sendMessage(msg.replaceAll("%player%", args[1]));
                        return true;
                    }

                } else {
                    sender.sendMessage(main.getConfig().getString("messages.missingPermission").replaceAll("&","§"));
                    return true;
                }
            }
        }
        return false;
    }
}
