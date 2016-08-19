package aegeus.com.aegeus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import aegeus.com.aegeus.obj.AegeusItem;
import aegeus.com.aegeus.util.Helper;
import aegeus.com.aegeus.util.InventorytoBase64;

public class Bank implements Listener{
	private JavaPlugin p;
	private Map<UUID, String> data = new HashMap<>(); //Map containing bank data for the players.  UUID = Player UUID, String = Base 64 Serialized Inventory.
	
	/**
	 * Main constructor
	 * @param p
	 */
	public Bank(JavaPlugin p)	{
		this.p = p;
	}
	
	@EventHandler
	public void onOpenChest(InventoryOpenEvent e)	{
		if(e.getInventory().getType() == InventoryType.ENDER_CHEST && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.EMPTY_MAP)	{
			e.setCancelled(true);
			return;
		}
		if(e.getInventory().getType() == InventoryType.ENDER_CHEST && e.getPlayer().getInventory().getItemInMainHand().getType() != Material.EMPTY_MAP)	{
			//The player has opened a vanilla ender chest.  Let's cancel it and give them our custom ender chest with more customizability.
			e.setCancelled(true);
			Player p = (Player) e.getPlayer();
			Inventory inv = Bukkit.createInventory(p, 9, "Bank");
			ItemStack block = new ItemStack(Material.GOLD_BLOCK);
			ItemMeta blockMeta = block.getItemMeta();
			blockMeta.setDisplayName(ChatColor.GOLD + "0 Gold(Test)");
			block.setItemMeta(blockMeta);
			inv.setItem(inv.getSize() - 1, block);
			if(data.containsKey(p.getUniqueId()))	{
				try {
					inv = InventorytoBase64.fromBase64(data.get(p.getUniqueId()), "Bank");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					p.getServer().getLogger().log(Level.INFO, "Could not read from string to inventory while loading bank.", e1);
				}
			}
			p.openInventory(inv);
		}
	}
	
	@EventHandler
	public void onCloseChest(InventoryCloseEvent e)	{
		if(e.getInventory().getName().equalsIgnoreCase("Bank"))	{
			//The player has closed our custom bank inventory.  Spit out the contents of the array as a string to the console.
			data.put(e.getPlayer().getUniqueId(), InventorytoBase64.toBase64(e.getInventory()));
		}
	}
	
	/**
	 * This method is used to check whether they want to withdraw money by clicking the gold block.
	 * @param e
	 */
	@EventHandler
	public void onClickEvent(InventoryClickEvent e)	{
		
	}
	
	/**
	 * This method is used to for upgrading the bank.
	 * @param e
	 */
	@EventHandler
	public void onBlockInteractEvent(PlayerInteractEvent e)	{
		if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.EMPTY_MAP)	{
			//This is called when a player tries to right-click a map(scroll).
			e.setCancelled(true);
		}
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Helper.colorCodes("&6Bank Upgrade Scroll")) && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.EMPTY_MAP && e.getClickedBlock().getType() == Material.ENDER_CHEST)	{
			//^The player right clicked an enderchest with an empty map named Bank Upgrade Scroll.
			e.getPlayer().sendMessage(ChatColor.AQUA + "You Shift-Right Clicked an Ender Chest Block!");
		}
	}
	private void upgradeBank(Player p)	{
		
	}
}