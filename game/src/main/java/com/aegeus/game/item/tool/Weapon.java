package com.aegeus.game.item.tool;

import com.aegeus.game.item.AgItem;
import com.aegeus.game.item.Rarity;
import com.aegeus.game.item.Tier;
import com.aegeus.game.item.info.DuraInfo;
import com.aegeus.game.item.info.EquipmentInfo;
import com.aegeus.game.item.info.LevelInfo;
import com.aegeus.game.item.info.SingletonInfo;
import com.aegeus.game.util.Util;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.NBTTagFloat;
import net.minecraft.server.v1_9_R1.NBTTagInt;
import net.minecraft.server.v1_9_R1.NBTTagString;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Weapon extends AgItem implements EquipmentInfo, LevelInfo, DuraInfo, SingletonInfo {
	private static final ThreadLocalRandom random = ThreadLocalRandom.current();

	// Level Info
	private int level = 0;
	private int xp = 0;

	// Equipment Info
	private int tier = 0;
	private Rarity rarity = null;
	private int enchant = 0;

	// Dura Info
	private int maxDura = 0;
	private int dura = 0;

	// Singleton Info
	private LocalDateTime time = LocalDateTime.now();

	// Weapon Stats
	private Rune rune = null;
	private int minDmg = 0;
	private int maxDmg = 0;
	private float pen = 0;
	private int fireDmg = 0;
	private int iceDmg = 0;
	private int poisonDmg = 0;
	private int pureDmg = 0;
	private float lifeSteal = 0;
	private float trueHearts = 0;
	private float blind = 0;

	public Weapon(Material material) {
		super(new ItemStack(material));
		setMaxDura(Tier.fromTier(tier).getWepDura());
	}

	public Weapon(ItemStack item) {
		super(item);
		impo();
	}

	public Weapon(Weapon other) {
		super(other);
		this.level = other.level;
		this.xp = other.xp;

		this.tier = other.tier;
		this.rarity = other.rarity;
		this.enchant = other.enchant;

		this.maxDura = other.maxDura;
		this.dura = other.dura;

		this.time = other.time;

		this.rune = other.rune;
		this.minDmg = other.minDmg;
		this.maxDmg = other.maxDmg;
		this.pen = other.pen;
		this.fireDmg = other.fireDmg;
		this.iceDmg = other.iceDmg;
		this.poisonDmg = other.poisonDmg;
		this.pureDmg = other.pureDmg;
		this.lifeSteal = other.lifeSteal;
		this.trueHearts = other.trueHearts;
		this.blind = other.blind;
	}

	@Override
	public void impo() {
		super.impo();
		EquipmentInfo.impo(this);
		LevelInfo.impo(this);
		DuraInfo.impo(this);
		SingletonInfo.impo(this);

		NBTTagCompound info = getAegeusInfo();
		rune = info.hasKey("runeType") ? (info.getInt("runeType") == -1 ? null : new Rune(Rune.RuneType.fromId(info.getInt("runeType")))) : null;
		minDmg = (info.hasKey("minDmg")) ? info.getInt("minDmg") : 0;
		maxDmg = (info.hasKey("maxDmg")) ? info.getInt("maxDmg") : 0;
		pen = (info.hasKey("pen")) ? info.getFloat("pen") : 0;
		fireDmg = (info.hasKey("fireDmg")) ? info.getInt("fireDmg") : 0;
		iceDmg = (info.hasKey("iceDmg")) ? info.getInt("iceDmg") : 0;
		poisonDmg = (info.hasKey("poisonDmg")) ? info.getInt("poisonDmg") : 0;
		pureDmg = (info.hasKey("pureDmg")) ? info.getInt("pureDmg") : 0;
		lifeSteal = (info.hasKey("lifeSteal")) ? info.getFloat("lifeSteal") : 0;
		trueHearts = (info.hasKey("trueHearts")) ? info.getFloat("trueHearts") : 0;
		blind = (info.hasKey("blind")) ? info.getFloat("blind") : 0;
	}

	@Override
	public void store() {
		super.store();
		EquipmentInfo.store(this);
		LevelInfo.store(this);
		DuraInfo.store(this);
		SingletonInfo.store(this);

		NBTTagCompound info = getAegeusInfo();
		info.set("type", new NBTTagString("weapon"));
		info.set("runeType", rune == null ? new NBTTagInt(-1) : new NBTTagInt(rune.getRuneType().getId()));
		info.set("minDmg", new NBTTagInt(minDmg));
		info.set("maxDmg", new NBTTagInt(maxDmg));
		info.set("pen", new NBTTagFloat(pen));
		info.set("fireDmg", new NBTTagInt(fireDmg));
		info.set("iceDmg", new NBTTagInt(iceDmg));
		info.set("poisonDmg", new NBTTagInt(poisonDmg));
		info.set("pureDmg", new NBTTagInt(pureDmg));
		info.set("lifeSteal", new NBTTagFloat(lifeSteal));
		info.set("trueHearts", new NBTTagFloat(trueHearts));
		info.set("blind", new NBTTagFloat(blind));
		setAegeusInfo(info);
	}

	public String buildPrefix() {
		return EquipmentInfo.buildPrefix(this);
	}

	public List<String> buildLore() {
		List<String> lore = new ArrayList<>();
		lore.add(Util.colorCodes("&cDMG: " + minDmg + " - " + maxDmg + (getLevelDmg() <= 0 ? "" : "&6 (+" + getLevelDmg() + ")")));
		if (pen > 0) lore.add(Util.colorCodes("&cPENETRATION: " + Math.round(pen * 100) + "%"));
		if (fireDmg > 0) lore.add(Util.colorCodes("&cFIRE DMG: +" + fireDmg));
		if (iceDmg > 0) lore.add(Util.colorCodes("&cICE DMG: +" + iceDmg));
		if (poisonDmg > 0) lore.add(Util.colorCodes("&cPOISON DMG: +" + poisonDmg));
		if (pureDmg > 0) lore.add(Util.colorCodes("&cPURE DMG: +" + pureDmg));
		if (lifeSteal > 0) lore.add(Util.colorCodes("&cLIFE STEAL: +" + Math.round(lifeSteal * 100) + "%"));
		if (trueHearts > 0) lore.add(Util.colorCodes("&cTRUE HEARTS: " + Math.round(trueHearts * 100) + "%"));
		if (blind > 0) lore.add(Util.colorCodes("&cBLIND: " + Math.round(blind * 100) + "%"));
		lore.addAll(EquipmentInfo.buildLore(this));
		if (rune != null) lore.add(Util.colorCodes("&5&oRune:&d&o " + rune.getRuneType().getName()));
		lore.addAll(LevelInfo.buildLore(this));
		return lore;
	}

	@Override
	public boolean verify() {
		NBTTagCompound info = getAegeusInfo();
		return info.hasKey("type") && info.getString("type").equalsIgnoreCase("weapon");
	}

	@Override
	public ItemStack build() {
		store();
		ItemStack item = super.build();

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(String.join("", buildPrefix(), getName()));
		meta.setLore(Util.union(buildLore(), getLore()));
		item.setItemMeta(meta);

		if (getEnchant() >= 4) item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);

		return item;
	}

	/*
	Info Overrides
	 */

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public int getXp() {
		return xp;
	}

	@Override
	public void setXp(int xp) {
		this.xp = xp;
	}

	@Override
	public int getMaxXp() {
		return (int) Util.calcMaxXP(getLevel());
	}

	@Override
	public int getTier() {
		return tier;
	}

	@Override
	public void setTier(int tier) {
		this.tier = tier;
		setMaxDura(Tier.fromTier(tier).getWepDura());
	}

	@Override
	public Rarity getRarity() {
		return rarity;
	}

	@Override
	public void setRarity(Rarity rarity) {
		this.rarity = rarity;
	}

	@Override
	public int getEnchant() {
		return enchant;
	}

	@Override
	public void setEnchant(int enchant) {
		this.enchant = enchant;
	}

	@Override
	public int getMaxDura() {
		return maxDura;
	}

	@Override
	public void setMaxDura(int maxDura) {
		this.maxDura = maxDura;
		setDura(getMaxDura());
	}

	@Override
	public int getDura() {
		return dura;
	}

	@Override
	public void setDura(int dura) {
		this.dura = dura;
		DuraInfo.update(this);
	}

	@Override
	public LocalDateTime getTime() {
		return time;
	}

	@Override
	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	/*
	Weapon Methods
	 */

	public Rune getRune() {
		return rune;
	}

	public void setRune(Rune rune) {
		this.rune = rune;
	}

	public int getDmg() {
		return (getMinDmg() == getMaxDmg() ? getMinDmg() : random.nextInt(getMinDmg(), getMaxDmg() + 1)) + getLevelDmg();
	}

	public void setDmg(int minDmg, int maxDmg) {
		this.minDmg = minDmg;
		this.maxDmg = maxDmg;
	}

	public int getMinDmg() {
		return minDmg;
	}

	public void setMinDmg(int minDmg) {
		this.minDmg = minDmg;
	}

	public int getMaxDmg() {
		return maxDmg;
	}

	public void setMaxDmg(int maxDmg) {
		this.maxDmg = maxDmg;
	}

	public int getLevelDmg() {
		return (int) Math.round(getLevel() * (0.15 * getTier()));
	}

	public float getPen() {
		return pen;
	}

	public void setPen(float pen) {
		this.pen = pen;
	}

	public int getFireDmg() {
		return fireDmg;
	}

	public void setFireDmg(int fireDmg) {
		this.fireDmg = fireDmg;
	}

	public int getIceDmg() {
		return iceDmg;
	}

	public void setIceDmg(int iceDmg) {
		this.iceDmg = iceDmg;
	}

	public int getPoisonDmg() {
		return poisonDmg;
	}

	public void setPoisonDmg(int poisonDmg) {
		this.poisonDmg = poisonDmg;
	}

	public int getPureDmg() {
		return pureDmg;
	}

	public void setPureDmg(int pureDmg) {
		this.pureDmg = pureDmg;
	}

	public float getLifeSteal() {
		return lifeSteal;
	}

	public void setLifeSteal(float lifeSteal) {
		this.lifeSteal = lifeSteal;
	}

	public float getTrueHearts() {
		return trueHearts;
	}

	public void setTrueHearts(float trueHearts) {
		this.trueHearts = trueHearts;
	}

	public float getBlind() {
		return blind;
	}

	public void setBlind(float blind) {
		this.blind = blind;
	}
}
