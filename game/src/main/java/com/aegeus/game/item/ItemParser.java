package com.aegeus.game.item;

import com.aegeus.game.item.info.*;
import com.aegeus.game.item.tool.Armor;
import com.aegeus.game.item.tool.Pickaxe;
import com.aegeus.game.item.tool.Weapon;
import com.aegeus.game.util.Util;
import org.bukkit.Material;

public class ItemParser {
	public static ItemInfo parseItem(ItemInfo info, String[] args) {
		for (String arg : args) {
			try {
				String[] pair = arg.split("=");
				String key = pair[0];
				String value = pair[1];

				if (key.equalsIgnoreCase("material"))
					info.setMaterial(Material.getMaterial(value));
				else if (key.equalsIgnoreCase("name"))
					info.setName(Util.colorCodes(value.replace("_", " ")));
				else if (key.equalsIgnoreCase("lore")) {
					for (String line : value.split("||"))
						info.addLore(line.replace("_", " "));
				}

			} catch (Exception ignored) {
			}
		}
		return info;
	}

	private static EquipmentInfo parseEquipment(EquipmentInfo info, String[] args) {
		for (String arg : args) {
			try {
				String[] pair = arg.split("=");
				String key = pair[0];
				String value = pair[1];

				if (key.equalsIgnoreCase("tier"))
					info.setTier(Integer.parseInt(value));
				else if (key.equalsIgnoreCase("rarity"))
					info.setRarity(Rarity.fromName(value));
				else if (key.equalsIgnoreCase("enchant"))
					info.setEnchant(Integer.parseInt(value));

			} catch (Exception ignored) {
			}
		}
		return info;
	}

	private static LevelInfo parseLevel(LevelInfo info, String[] args) {
		for (String arg : args) {
			try {
				String[] pair = arg.split("=");
				String key = pair[0];
				String value = pair[1];

				if (key.equalsIgnoreCase("level"))
					info.setLevel(Integer.parseInt(value));
				else if (key.equalsIgnoreCase("xp"))
					info.setXp(Integer.parseInt(value));

			} catch (Exception ignored) {
			}
		}
		return info;
	}

	private static DuraInfo parseDura(DuraInfo info, String[] args) {
		for (String arg : args) {
			try {
				String[] pair = arg.split("=");
				String key = pair[0];
				String value = pair[1];

				if (key.equalsIgnoreCase("maxdura"))
					info.setMaxDura(Integer.parseInt(value));
				else if (key.equalsIgnoreCase("dura"))
					info.setDura(Integer.parseInt(value));

			} catch (Exception ignored) {
			}
		}
		return info;
	}

	private static ProfessionInfo parseProfession(ProfessionInfo info, String[] args) {
		for (String arg : args) {
			try {
				String[] pair = arg.split("=");
				String key = pair[0];
				String value = pair[1];

				if (key.equalsIgnoreCase("level"))
					info.setLevel(Integer.parseInt(value));
				else if (key.equalsIgnoreCase("xp"))
					info.setXp(Integer.parseInt(value));

			} catch (Exception ignored) {
			}
		}
		return info;
	}

	public static Weapon parseWeapon(Weapon weapon, String[] args) {
		weapon = (Weapon) parseItem(weapon, args);
		weapon = (Weapon) parseEquipment(weapon, args);
		weapon = (Weapon) parseLevel(weapon, args);
		weapon = (Weapon) parseDura(weapon, args);

		for (String arg : args) {
			try {
				String[] pair = arg.split("=");
				String key = pair[0];
				String value = pair[1];

				if (key.equalsIgnoreCase("dmg")) {
					String[] vals = value.split(";");
					weapon.setDmg(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]));
				} else if (key.equalsIgnoreCase("pen"))
					weapon.setPen(Float.parseFloat(value));

				else if (key.equalsIgnoreCase("firedmg"))
					weapon.setFireDmg(Integer.parseInt(value));
				else if (key.equalsIgnoreCase("icedmg"))
					weapon.setIceDmg(Integer.parseInt(value));
				else if (key.equalsIgnoreCase("poisondmg"))
					weapon.setPoisonDmg(Integer.parseInt(value));
				else if (key.equalsIgnoreCase("puredmg"))
					weapon.setPureDmg(Integer.parseInt(value));

				else if (key.equalsIgnoreCase("lifesteal"))
					weapon.setLifeSteal(Float.parseFloat(value));
				else if (key.equalsIgnoreCase("truehearts"))
					weapon.setTrueHearts(Float.parseFloat(value));
				else if (key.equalsIgnoreCase("blind"))
					weapon.setBlind(Float.parseFloat(value));
			} catch (Exception ignored) {
			}
		}

		return weapon;
	}

	public static Armor parseArmor(Armor armor, String[] args) {
		armor = (Armor) parseItem(armor, args);
		armor = (Armor) parseEquipment(armor, args);
		armor = (Armor) parseLevel(armor, args);
		armor = (Armor) parseDura(armor, args);

		for (String arg : args) {
			try {
				String[] pair = arg.split("=");
				String key = pair[0];
				String value = pair[1];

				if (key.equalsIgnoreCase("hp"))
					armor.setHp(Integer.parseInt(value));
				else if (key.equalsIgnoreCase("hpregen"))
					armor.setHpRegen(Integer.parseInt(value));
				else if (key.equalsIgnoreCase("physres"))
					armor.setPhysRes(Float.parseFloat(value));
				else if (key.equalsIgnoreCase("magres"))
					armor.setMagRes(Float.parseFloat(value));
				else if (key.equalsIgnoreCase("block"))
					armor.setBlock(Float.parseFloat(value));
				else if (key.equalsIgnoreCase("dodge"))
					armor.setDodge(Float.parseFloat(value));
				else if (key.equalsIgnoreCase("reflect"))
					armor.setReflect(Float.parseFloat(value));

			} catch (Exception ignored) {
			}
		}
		return armor;
	}

	public static Pickaxe parsePick(Pickaxe p, String[] args) {
		p = (Pickaxe) parseItem(p, args);
		p = (Pickaxe) parseProfession(p, args);

		for (int i = 0; i < args.length; i++) {
			try {
				String[] pair = args[i].split("=");
				String key = pair[0];
				String value = pair[1];
				switch (key.toLowerCase()) {
					case "miningsuccess":
						p.setMiningSuccess(Float.valueOf(value));
						break;
					case "densefind":
						p.setDenseFind(Float.valueOf(value));
						break;
					case "densemultiplier":
						p.setDenseMultiplier(Integer.valueOf(value));
						break;
					case "doubleore":
						p.setDoubleOre(Float.valueOf(value));
						break;
					case "tripleore":
						p.setTripleOre(Float.valueOf(value));
						break;
					case "gemfind":
						p.setGemFind(Float.valueOf(value));
						break;
				}
			} catch (Exception fuckexceptions) {
				fuckexceptions.printStackTrace();
			}
		}
		return p;
	}
}
