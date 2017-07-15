package com.aegeus.game.item.tool;

import com.aegeus.game.item.ItemUtils;
import com.aegeus.game.item.Tier;
import com.aegeus.game.item.info.ItemInfo;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class Enchant implements ItemInfo {
	public static final int WEAPON = 0;
	public static final int ARMOR = 1;

	private ItemStack item;

	private Tier tier;
	private int type;

	public Enchant(Tier tier, int type) {
		this(tier, type, 1);
	}

	public Enchant(Tier tier, int type, int amount) {
		this.tier = tier;
		this.type = type;
		this.item = new ItemStack(Material.EMPTY_MAP, amount);
	}

	public Enchant(ItemStack item) {
		this.item = item;
		impo();
	}

	public static boolean hasEnchantInfo(ItemStack item) {
		return ItemUtils.getTag(item).hasKey("EnchantInfo");
	}

	public static NBTTagCompound getEnchantInfo(ItemStack item) {
		NBTTagCompound tag = ItemUtils.getTag(item);
		return hasEnchantInfo(item) ? tag.getCompound("EnchantInfo") : new NBTTagCompound();
	}

	public static ItemStack setEnchantInfo(ItemStack item, NBTTagCompound info) {
		NBTTagCompound tag = ItemUtils.getTag(item);
		tag.set("EnchantInfo", info);
		return ItemUtils.setTag(item, tag);
	}

	@Override
	public void impo() {
		ItemInfo.impo(this);

		NBTTagCompound info = getEnchantInfo(item);
		tier = info.hasKey("tier") ? Tier.fromTier(info.getInt("tier")) : null;
		type = info.hasKey("type") ? info.getInt("type") : WEAPON;
	}

	@Override
	public void store() {
		ItemInfo.store(this);

		NBTTagCompound info = getEnchantInfo(item);
		info.setInt("tier", tier == null ? -1 : Arrays.asList(Tier.values()).indexOf(tier));
		item = setEnchantInfo(item, info);
	}

	@Override
	public ItemStack build() {
		store();

		if (type == WEAPON) {
			setName("&f&lEnchant:&7 " + tier.getColor() + tier.getWeapon() + " Weapon");
			setLore(new ArrayList<>());
			addLore("&c+5% DMG");
			addLore("&7&oWeapon will VANISH if enchant above +3 FAILS.");
		} else if (type == ARMOR) {
			setName("&f&lEnchant:&7 " + tier.getColor() + tier.getArmor() + " Armor");
			setLore(new ArrayList<>());
			addLore("&c+5% HP");
			addLore("&c+5% HP REGEN");
			addLore("&7&oArmor will VANISH if enchant above +3 FAILS.");
		}

		return ItemInfo.build(this);
	}

	@Override
	public ItemStack getItem() {
		return item;
	}

	@Override
	public void setItem(ItemStack item) {
		this.item = item;
	}

	public Tier getTier() {
		return tier;
	}

	public int getType() {
		return type;
	}
}