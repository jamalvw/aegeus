package com.aegeus.game;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.aegeus.common.Common;
import com.aegeus.game.data.Data;
import com.aegeus.game.util.Utility;

public class Server implements Listener {

	private JavaPlugin parent;

	public Server(JavaPlugin parent) {
		this.parent = parent;
//		Runnable checkBySecond = new Runnable() {
//			public void run() {
//				if (buffLootTime > 0) {
//					buffLootTime -= 1;
//					if (buffLootTime <= 0) {
//						Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + ">>" + ChatColor.GOLD
//								+ " Global Loot Buff EXPIRED");
//					}
//				}
//				if (rebootTime > 1) {
//					rebootTime -= 1;
//					if (rebootTime == 1200) {
//						Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + ">>" + ChatColor.GOLD
//								+ " The server will reboot in 20 minutes.");
//					}
//					if (rebootTime == 600) {
//						Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + ">>" + ChatColor.GOLD
//								+ " The server will reboot in 10 minutes.");
//					}
//					if (rebootTime == 300) {
//						Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + ">>" + ChatColor.GOLD
//								+ " The server will reboot in 5 minutes.");
//					}
//					if (rebootTime == 60) {
//						Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + ">>" + ChatColor.GOLD
//								+ " The server will reboot in 1 minute. Use " + ChatColor.BOLD + "/logout"
//								+ ChatColor.GOLD + " to ensure your data is saved.");
//					}
//					if (rebootTime <= 5) {
//						Bukkit.broadcastMessage(
//								ChatColor.RED + "" + ChatColor.BOLD + ">>" + ChatColor.GOLD + " " + rebootTime + "...");
//					}
//					if (rebootTime <= 0) {
//						Bukkit.setWhitelist(true);
//						for (Player p : Bukkit.getOnlinePlayers()) {
//							p.sendMessage(
//									ChatColor.YELLOW + "Attempting to save player data. You should've logged out!");
//						}
//						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spigot:restart");
//					}
//				}
//			}
//		};
//		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//		executor.scheduleAtFixedRate(checkBySecond, 0, 1, TimeUnit.SECONDS);
	}

//	private int buffLootTime = 0;
//	private int rebootTime = 7200;

	private final String[] motds = { // List of MOTDs
			"Is that supposed to be a meme?",
			"Not supported by Atlas!",
			"What are you, a miner?",
			"pls, no ddos, am only 8",
			"5.2k Armor Energy, Scouts!",
			"You have to see to believe!",
			"<none>",
			"&mBeloved&7 Known by Gio himself!",
			"Fancy items!",
			"peppy pls fix",
			"oopspng",
			"I'm from PlanetMinecraft, op please?",
			"There's more where that came from!",
			"No, we aren't serving seconds!",
			"Free T5 on us!",
			"Close your eyes and make a wish!",
			"loads of memes.sk",
			"Divided Nations? What's that?",
			"Easy game, easy life!",
			"Too many daggers!",
			"Kilvre silled it!",
			"It's high noon somewhere in the world.",
			"Contrary to popular belief, Silvre did not kill it.",
			"There's something different about this place..."
	};		

	@EventHandler
	private void onLoginEvent(PlayerLoginEvent e) {
		
	}

	@EventHandler
	private void onCommandEvent(ServerCommandEvent e) {
		Bukkit.getOperators().stream()
			.filter((p) -> p.isOnline())
			.map((p) -> (Player) p)
			.forEach((p) -> p.sendMessage(Utility.colorCodes(
					"&8" + e.getSender() + " > " + e.getCommand())));
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	// Login messages and initial player setup
	// TODO clean this
	private void onJoinEvent(PlayerJoinEvent e) throws InterruptedException {
		Player player = e.getPlayer();
		e.setJoinMessage("");
		player.setHealthScaled(true);
		player.setHealthScale(20);
		
		Statistics.updateStats(player);
		
		if (!player.hasPlayedBefore()) player.sendTitle("", Utility.colorCodes("&bWelcome."));
		else player.sendTitle("", Utility.colorCodes("&fWelcome back."));
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(parent, new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 10; i++)
					player.sendMessage(" ");
				
				player.sendMessage(Utility.colorCodes(
						"          &bAegeus &f&lMMORPG&f\n" +
						"          &b• &7Build &b" + Common.BUILD + " &7(&o" + Common.BUILD_NOTE + "&7)\n" +
						"          &7Modify game settings with &b/settings"));
				
				for (int i = 0; i < 2; i++)
					player.sendMessage(" ");
				
				if(Bukkit.getOnlinePlayers().size() == 1)
					player.sendMessage(Utility.colorCodes(
							"&8That's strange. It's quiet around here, like everyone has gone away. Why's that..? Have you arrived early, or is the universe arriving late?"
					));
				
				player.sendMessage(" ");
			}
		}, 2);
		
	}

	@EventHandler
	// Clear user information and punish combat loggers
	private void onLogoutEvent(PlayerQuitEvent e) {
		e.setQuitMessage("");
		Data.remove(e.getPlayer());
	}

	@EventHandler
	// Random, custom MOTDs
	private void onServerListPingEvent(ServerListPingEvent event) {
		Random random = new Random();
		if(Bukkit.hasWhitelist()) event.setMotd(Utility.colorCodes(
			"&bAegeus &f&lMMORPG&7 - Build &b" + Common.BUILD + "\n&f"
			+ "&cUndergoing maintenance. Stay tuned!"));
		else event.setMotd(Utility.colorCodes(
			"&bAegeus &f&lMMORPG&7 - Build &b" + Common.BUILD + "\n&f"
			+ motds[random.nextInt(motds.length)]));
	}

	@EventHandler
	// Disable crafting
	private void onCraftEvent(CraftItemEvent event) {
		event.setCancelled(true);
	}
}
