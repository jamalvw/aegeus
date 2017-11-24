package com.aegeus.game.item;

import com.aegeus.game.Aegeus;
import com.aegeus.game.entity.AgPlayer;
import com.aegeus.game.util.InventoryBuilder;
import com.aegeus.game.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bank {
	private int slots = 9;
	private AgPlayer owner = null;
	private int numPages = 1;
	private List<Page> pages = new ArrayList<>();

	public Bank(AgPlayer owner, int slots) {
		this.owner = owner;
		this.slots = slots;
		this.numPages = (int) Math.ceil(slots / 45.0);
		for (int i = 1; i <= numPages; i++) {
			pages.add(createPage(numPages > 1 ? i != numPages ? 45 : slots - (45 * (i - 1)) : slots, i, owner.getPlayer(), "Page " + i));
		}
	}

	public InventoryBuilder getInventory(int page) {
		if (page > pages.size()) throw new RuntimeException("Invalid page number!");
		return pages.get(page - 1).getInventory();
	}

	public int getNumberOfPages() {
		return numPages;
	}

	public Page createPage(int size, int pageNumber, Player owner, String title) {
		InventoryBuilder i = new InventoryBuilder(owner, size + (getNumberOfPages() > 1 ? 9 : 0), title);
		if (getNumberOfPages() > 1) {
			ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0);
			ItemUtils.setDisplayItem(pane, true);
			ItemMeta meta = pane.getItemMeta();
			meta.setDisplayName(Util.colorCodes("&7Nothing"));
			meta.setLore(Collections.singletonList(Util.colorCodes("&7Display Item")));
			pane.setItemMeta(meta);

			ItemStack pageFlipPrevious = new ItemStack(Material.SPECTRAL_ARROW, 1);
			ItemUtils.setDisplayItem(pageFlipPrevious, true);
			ItemMeta pFPMeta = pane.getItemMeta();
			pFPMeta.setLore(Collections.singletonList(Util.colorCodes("&7Display Item")));
			if (pageNumber == 1) {
				pageFlipPrevious.setType(Material.STAINED_GLASS_PANE);
				pFPMeta.setDisplayName(Util.colorCodes("&7Nothing"));
			} else {
				pFPMeta.setDisplayName(Util.colorCodes("&fPrevious Page (" + (pageNumber - 1) + "/" + getNumberOfPages() + ")"));
			}
			pageFlipPrevious.setItemMeta(pFPMeta);

			ItemStack pageFlipNext = new ItemStack(Material.SPECTRAL_ARROW, 1);
			ItemUtils.setDisplayItem(pageFlipNext, true);
			ItemMeta pFNMeta = pageFlipNext.getItemMeta();
			pFNMeta.setLore(Collections.singletonList(Util.colorCodes("&7Display Item")));
			if (pageNumber == getNumberOfPages()) {
				pageFlipNext.setType(Material.STAINED_GLASS_PANE);
				pFNMeta.setDisplayName(Util.colorCodes("&7Nothing"));
			} else {
				pFNMeta.setDisplayName(Util.colorCodes("&fNext Page (" + (pageNumber + 1) + "/" + getNumberOfPages() + ")"));
			}
			pageFlipNext.setItemMeta(pFNMeta);

			i.setItem(i.getSize() - 9, pane, e1 -> e1.setCancelled(true), false);
			i.setItem(i.getSize() - 8, pane, e1 -> e1.setCancelled(true), false);
			i.setItem(i.getSize() - 7, pane, e1 -> e1.setCancelled(true), false);
			i.setItem(i.getSize() - 6, pageFlipPrevious,
					pageFlipPrevious.getType() == Material.SPECTRAL_ARROW ? e1 -> getInventory(pageNumber - 1).show((Player) e1.getWhoClicked()) : null, pageFlipPrevious.getType() == Material.SPECTRAL_ARROW);
			i.setItem(i.getSize() - 5, pane, false);
			i.setItem(i.getSize() - 4, pageFlipNext,
					pageFlipNext.getType() == Material.SPECTRAL_ARROW ? e1 -> getInventory(pageNumber + 1).show((Player) e1.getWhoClicked()) : null, pageFlipNext.getType() == Material.SPECTRAL_ARROW);
			i.setItem(i.getSize() - 3, pane, e1 -> Bukkit.getScheduler().scheduleSyncDelayedTask(Aegeus.getInstance(), () -> e1.setCancelled(true), 1L), false);
			i.setItem(i.getSize() - 2, pane, e1 -> e1.setCancelled(true), false);
			i.setItem(i.getSize() - 1, pane, e1 -> e1.setCancelled(true), false);
		}
		return new Page(size, pageNumber, i, this);
	}

	public class Page {
		private int size = 9;
		private int pageNumber;
		private InventoryBuilder inventory;
		private Bank origin;

		public Page(int size, int pageNumber, InventoryBuilder inventory, Bank origin) {
			this.size = size;
			this.pageNumber = pageNumber;
			this.inventory = inventory;
			this.origin = origin;
		}

		public Bank getOrigin() {
			return origin;
		}

		public int getPageNumber() {
			return pageNumber;
		}

		public int getSize() {
			return size;
		}

		public InventoryBuilder getInventory() {
			return inventory;
		}
	}
}