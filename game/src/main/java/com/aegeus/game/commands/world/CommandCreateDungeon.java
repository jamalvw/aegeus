package com.aegeus.game.commands.world;

import com.aegeus.game.Aegeus;
import com.aegeus.game.dungeon.Dungeon;
import com.aegeus.game.util.Util;
import com.aegeus.game.util.exceptions.DungeonLoadingException;
import com.sk89q.worldedit.data.DataException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

/**
 * Created by Silvre on 7/11/2017.
 */
public class CommandCreateDungeon implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("aegeus.world"))   {
            sender.sendMessage(Util.colorCodes("&cYou do not have sufficient permissions to execute this command!"));
            return false;
        }
        if(sender instanceof ConsoleCommandSender)  {
            sender.sendMessage(Util.colorCodes("&cYou can't execute this command through the console! Get into the game!"));
            return false;
        }
        if(args.length != 5)    {
            sender.sendMessage(Util.colorCodes("&cInvalid arguments! Use /dungeon <directory> <length> <array size> <number of segments> <schematic size>"));
            return false;
        }
        Player p = (Player) sender;
        p.sendMessage(Util.colorCodes("&7Generating dungeon..."));
        Aegeus.getInstance().getServer().getScheduler().runTaskAsynchronously(Aegeus.getInstance(), () -> {
            try {
                Dungeon d = new Dungeon(p.getLocation(), args[0], Integer.valueOf(args[1]), p.getWorld(), Integer.valueOf(args[2]), Integer.valueOf(args[3]), Integer.valueOf(args[4]));
            } catch (DungeonLoadingException e) {
                e.printStackTrace();
            } catch (DataException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return true;
    }
}
