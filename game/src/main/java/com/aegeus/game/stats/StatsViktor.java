package com.aegeus.game.stats;

import com.aegeus.game.ability.AbilityConcuss;
import com.aegeus.game.ability.AbilityDetonate;
import com.aegeus.game.ability.AbilityTackle;
import com.aegeus.game.item.Rarity;
import com.aegeus.game.item.Tier;
import com.aegeus.game.util.Action;
import com.aegeus.game.util.Chance;
import com.aegeus.game.util.FloatPoss;
import com.aegeus.game.util.IntPoss;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;

public class StatsViktor extends Stats {
	@Override
	public void prepare() {
		setTier(Tier.TIER_5);
		setChance(0.01f);
		setDmgMultiplier(2.5f);
		setHpMultiplier(4);
		setGlowing(true);
		setGenName(false);

		getTypes().add(EntityType.SKELETON);

		getNames().add("&e&lViktor the Conqueror");
		setAbils(Arrays.asList(new AbilityConcuss(), new AbilityDetonate(),
				new AbilityTackle()));
		getSpawnActions().add(new Action<LivingEntity>() {
			@Override
			public void activate(LivingEntity e) {
				for (ItemStack i : e.getEquipment().getArmorContents()) {
					LeatherArmorMeta meta = (LeatherArmorMeta) i.getItemMeta();
					meta.setColor(Color.BLACK);
					i.setItemMeta(meta);
				}
			}
		});

		getDefArmor().rarity = Rarity.UNIQUE;
		getDefArmor().hpRegen = new IntPoss(80, 120);
		getDefArmor().physRes = new Chance<>(new FloatPoss(0.07f, 0.12f), 0.78f);
		getDefArmor().block = new Chance<>(new FloatPoss(0.06f, 0.11f), 0.67f);

		getHelmets().clear();
		ArmorPossible helmet = new ArmorPossible();
		helmet.material = Material.JACK_O_LANTERN;
		helmet.name = "&eViktor's Bloody Mask";
		helmet.hp = new IntPoss(2115, 2760);
		getHelmets().add(helmet);

		ArmorPossible chestplate = new ArmorPossible();
		chestplate.material = Material.LEATHER_CHESTPLATE;
		chestplate.name = "&eViktor's Chestplate";
		chestplate.hp = new IntPoss(3490, 4140);
		getChestplates().add(chestplate);

		ArmorPossible leggings = new ArmorPossible();
		leggings.material = Material.LEATHER_LEGGINGS;
		leggings.name = "&eViktor's Leggings";
		leggings.hp = new IntPoss(3490, 4140);
		getAllLeggings().add(leggings);

		ArmorPossible boots = new ArmorPossible();
		boots.material = Material.LEATHER_BOOTS;
		boots.name = "&eViktor's Boots";
		boots.hp = new IntPoss(2115, 2760);
		getAllBoots().add(boots);

		WeaponPossible axe = new WeaponPossible();
		axe.material = Material.GOLD_AXE;
		axe.name = "&eViktor's Axe";
		axe.rarity = Rarity.UNIQUE;
		axe.dmg = new IntPoss(280, 340);
		axe.range = new IntPoss(40, 65);
		axe.pen = new Chance<>(new FloatPoss(0.12f, 0.24f), 0.89f);
		axe.pureDmg = new Chance<>(new IntPoss(6, 25), 0.75f);
		axe.trueHearts = new Chance<>(new FloatPoss(0.01f, 0.07f), 0.47f);
		getWeapons().add(axe);
	}
}
