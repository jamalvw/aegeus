package com.aegeus.game.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.aegeus.game.item.PickaxeBuilder;

public class CommandSpawnPick implements CommandExecutor	{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0)	{
			return false;
		}
		else	{
			Player p = (Player) sender;
			ItemStack pickaxe = new PickaxeBuilder(Integer.valueOf(args[0]), true).build();
			p.getInventory().addItem(pickaxe);
			return true;
		}
	}
}
