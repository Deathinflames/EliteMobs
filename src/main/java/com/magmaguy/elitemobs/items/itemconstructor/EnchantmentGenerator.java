package com.magmaguy.elitemobs.items.itemconstructor;

import com.magmaguy.elitemobs.config.ConfigValues;
import com.magmaguy.elitemobs.config.ItemsDropSettingsConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EnchantmentGenerator {

    public static ItemMeta generateEnchantments(ItemMeta itemMeta, HashMap<Enchantment, Integer> enchantmentMap) {

        for (Enchantment enchantment : enchantmentMap.keySet())
            if (enchantmentMap.get(enchantment) > 5 && ConfigValues.itemsDropSettingsConfig.getBoolean(ItemsDropSettingsConfig.ENABLE_CUSTOM_ENCHANTMENT_SYSTEM))
                itemMeta.addEnchant(enchantment, 5, true);
            else
                itemMeta.addEnchant(enchantment, enchantmentMap.get(enchantment), true);

        return itemMeta;

    }

    /*
    Note: For procedurally generated items the enchantments only get applied to the item at the lore phase
    This only gathers the list of enchantments to be applied
     */
    public static HashMap<Enchantment, Integer> generateEnchantments(double itemTier, Material material) {

        HashMap<Enchantment, Integer> enchantmentMap = new HashMap<>();

        /*
        No enchantments for items too low tier to have one
         */
        if (itemTier < 1) return enchantmentMap;

        HashMap<Enchantment, Integer> validSecondaryEnchantments = new HashMap();

        /*
        Primary enchantments get instantly validated and applies since there is only one per item type
        Secondary enchantments get added to a common pool to be randomized later
         */
        switch (material) {
            case DIAMOND_SWORD:
            case GOLD_SWORD:
            case IRON_SWORD:
            case STONE_SWORD:
            case WOOD_SWORD:
                enchantmentMap.putAll(validateAndApplyPrimaryEnchantment("DAMAGE_ALL", itemTier));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("DAMAGE_ARTHROPODS"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("DAMAGE_UNDEAD"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("DURABILITY"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("FIRE_ASPECT"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("KNOCKBACK"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("LOOT_BONUS_MOBS"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("MENDING"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("SWEEPING_EDGE"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("VANISHING_CURSE"));
                break;
            case BOW:
                enchantmentMap.putAll(validateAndApplyPrimaryEnchantment("ARROW_DAMAGE", itemTier));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("ARROW_FIRE"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("ARROW_INFINITE"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("ARROW_KNOCKBACK"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("DURABILITY"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("MENDING"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("VANISHING_CURSE"));
                break;
            case DIAMOND_PICKAXE:
            case GOLD_PICKAXE:
            case IRON_PICKAXE:
            case STONE_PICKAXE:
            case WOOD_PICKAXE:
                enchantmentMap.putAll(validateAndApplyPrimaryEnchantment("DIG_SPEED", itemTier));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("DURABILITY"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("MENDING"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("VANISHING_CURSE"));
                //TODO: this doesn't take config into account
                if (ThreadLocalRandom.current().nextDouble() < 0.5) {
                    validSecondaryEnchantments.putAll(validateSecondaryEnchantments("LOOT_BONUS_BLOCKS"));
                } else {
                    validSecondaryEnchantments.putAll(validateSecondaryEnchantments("SILK_TOUCH"));
                }
                break;
            case DIAMOND_SPADE:
            case GOLD_SPADE:
            case IRON_SPADE:
            case STONE_SPADE:
            case WOOD_SPADE:
                enchantmentMap.putAll(validateAndApplyPrimaryEnchantment("DIG_SPEED", itemTier));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("DURABILITY"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("MENDING"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("VANISHING_CURSE"));
                if (ThreadLocalRandom.current().nextDouble() < 0.5) {
                    validSecondaryEnchantments.putAll(validateSecondaryEnchantments("LOOT_BONUS_BLOCKS"));
                } else {
                    validSecondaryEnchantments.putAll(validateSecondaryEnchantments("SILK_TOUCH"));
                }
                break;
            case DIAMOND_HOE:
            case GOLD_HOE:
            case IRON_HOE:
            case STONE_HOE:
            case WOOD_HOE:
            case SHIELD:
                enchantmentMap.putAll(validateAndApplyPrimaryEnchantment("DURABILITY", itemTier));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("MENDING"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("VANISHING_CURSE"));
                break;
            case DIAMOND_AXE:
            case GOLD_AXE:
            case IRON_AXE:
            case STONE_AXE:
            case WOOD_AXE:
                enchantmentMap.putAll(validateAndApplyPrimaryEnchantment("DAMAGE_ALL", itemTier));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("DAMAGE_ARTHROPODS"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("DAMAGE_UNDEAD"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("DURABILITY"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("MENDING"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("VANISHING_CURSE"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("DIG_SPEED"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("LOOT_BONUS_BLOCKS"));
                break;
            case CHAINMAIL_HELMET:
            case DIAMOND_HELMET:
            case GOLD_HELMET:
            case IRON_HELMET:
            case LEATHER_HELMET:
                enchantmentMap.putAll(validateAndApplyPrimaryEnchantment("PROTECTION_ENVIRONMENTAL", itemTier));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("BINDING_CURSE"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("DURABILITY"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("MENDING"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("OXYGEN"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("PROTECTION_EXPLOSIONS"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("PROTECTION_FIRE"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("PROTECTION_PROJECTILE"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("THORNS"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("VANISHING_CURSE"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("WATER_WORKER"));
                break;
            case CHAINMAIL_CHESTPLATE:
            case DIAMOND_CHESTPLATE:
            case GOLD_CHESTPLATE:
            case IRON_CHESTPLATE:
            case LEATHER_CHESTPLATE:
                enchantmentMap.putAll(validateAndApplyPrimaryEnchantment("PROTECTION_ENVIRONMENTAL", itemTier));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("DURABILITY"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("MENDING"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("PROTECTION_EXPLOSIONS"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("PROTECTION_FIRE"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("PROTECTION_PROJECTILE"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("THORNS"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("VANISHING_CURSE"));
                break;
            case CHAINMAIL_LEGGINGS:
            case DIAMOND_LEGGINGS:
            case GOLD_LEGGINGS:
            case IRON_LEGGINGS:
            case LEATHER_LEGGINGS:
                enchantmentMap.putAll(validateAndApplyPrimaryEnchantment("PROTECTION_ENVIRONMENTAL", itemTier));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("BINDING_CURSE"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("DURABILITY"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("MENDING"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("PROTECTION_EXPLOSIONS"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("PROTECTION_FIRE"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("PROTECTION_PROJECTILE"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("THORNS"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("VANISHING_CURSE"));
                break;
            case CHAINMAIL_BOOTS:
            case DIAMOND_BOOTS:
            case GOLD_BOOTS:
            case IRON_BOOTS:
            case LEATHER_BOOTS:
                enchantmentMap.putAll(validateAndApplyPrimaryEnchantment("PROTECTION_ENVIRONMENTAL", itemTier));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("BINDING_CURSE"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("DURABILITY"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("MENDING"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("PROTECTION_EXPLOSIONS"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("PROTECTION_FALL"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("PROTECTION_FIRE"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("PROTECTION_PROJECTILE"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("THORNS"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("VANISHING_CURSE"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("DEPTH_STRIDER"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("FROST_WALKER"));
                break;
            case FISHING_ROD:
                enchantmentMap.putAll(validateAndApplyPrimaryEnchantment("DURABILITY", itemTier));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("VANISHING_CURSE"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("MENDING"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("LUCK"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("LURE"));
                break;
            case SHEARS:
                enchantmentMap.putAll(validateAndApplyPrimaryEnchantment("DIG_SPEED", itemTier));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("VANISHING_CURSE"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("MENDING"));
                validSecondaryEnchantments.putAll(validateSecondaryEnchantments("DURABILITY"));
                break;
        }

        /*
        Now that the primary enchantments are applied and the secondary enchantments are parsed, apply secondary enchants
         */
        int secondaryEnchantmentTotalParsedLevel = totalSecondaryEnchantmentCount(validSecondaryEnchantments);
        int maxSecondaryEnchantmentLevel = (secondaryEnchantmentTotalParsedLevel < itemTier - 2) ? secondaryEnchantmentTotalParsedLevel : (int) itemTier;
        int secondaryEnchantmentCount = ThreadLocalRandom.current().nextInt(maxSecondaryEnchantmentLevel + 1);

        if (itemTier < 2 || secondaryEnchantmentCount < 1 || validSecondaryEnchantments.size() == 0)
            return enchantmentMap;

        HashMap<Enchantment, Integer> validEnchantmentsClone = (HashMap<Enchantment, Integer>) validSecondaryEnchantments.clone();

        while (secondaryEnchantmentCount != 0) {

            if (validSecondaryEnchantments.size() < 1) {

                break;

            }

            int randomIndex = ThreadLocalRandom.current().nextInt(validSecondaryEnchantments.size());

            List<Enchantment> enchantmentList = new ArrayList();

            for (Enchantment enchantment : validSecondaryEnchantments.keySet()) {

                enchantmentList.add(enchantment);

            }

            Enchantment enchantment = enchantmentList.get(randomIndex);

            validSecondaryEnchantments.put(enchantment, validSecondaryEnchantments.get(enchantment) - 1);

            int finalEnchantLevel = validEnchantmentsClone.get(enchantment) - validSecondaryEnchantments.get(enchantment);
            enchantmentMap.put(enchantment, finalEnchantLevel);

            int newEnchantInt = validSecondaryEnchantments.get(enchantment);

            if (newEnchantInt == 0) validSecondaryEnchantments.remove(enchantment);

            secondaryEnchantmentCount--;

        }

        return enchantmentMap;

    }

    private static HashMap<Enchantment, Integer> validateAndApplyPrimaryEnchantment(String string, double itemTier) {

        HashMap<Enchantment, Integer> enchantmentMap = new HashMap<>();

        String mainString = "Valid Enchantments." + string;

        if (ConfigValues.itemsProceduralSettingsConfig.getBoolean(mainString + ".Allow")) {

            int maxEnchantmentLevel;

            if (ConfigValues.itemsProceduralSettingsConfig.contains(mainString + ".Max Level")) {

                maxEnchantmentLevel = ConfigValues.itemsProceduralSettingsConfig.getInt(mainString + ".Max Level");

            } else {

                maxEnchantmentLevel = 1;

            }

            int enchantmentLevel = (maxEnchantmentLevel < itemTier) ? maxEnchantmentLevel : (int) itemTier;

            //index for random getting

            enchantmentMap.put(Enchantment.getByName(string), enchantmentLevel);

        }

        return enchantmentMap;

    }

    private static HashMap<Enchantment, Integer> validateSecondaryEnchantments(String string) {

        HashMap<Enchantment, Integer> hashMap = new HashMap();

        if (enchantmentBackwardsCompatibility(7, 0, string, "LURE")) return hashMap;
        if (enchantmentBackwardsCompatibility(7, 0, string, "LUCK")) return hashMap;
        if (enchantmentBackwardsCompatibility(8, 0, string, "DEPTH_STRIDER")) return hashMap;
        if (enchantmentBackwardsCompatibility(9, 0, string, "MENDING")) return hashMap;
        if (enchantmentBackwardsCompatibility(9, 0, string, "FROST_WALKER")) return hashMap;
        if (enchantmentBackwardsCompatibility(11, 0, string, "VANISHING_CURSE")) return hashMap;
        if (enchantmentBackwardsCompatibility(11, 0, string, "BINDING_CURSE")) return hashMap;
        if (enchantmentBackwardsCompatibility(11, 1, string, "SWEEP")) return hashMap;

        String mainString = "Valid Enchantments." + string;

        if (ConfigValues.itemsProceduralSettingsConfig.getBoolean(mainString + ".Allow")) {

            int enchantmentLevel;

            if (ConfigValues.itemsProceduralSettingsConfig.contains(mainString + ".Max Level")) {

                enchantmentLevel = ConfigValues.itemsProceduralSettingsConfig.getInt(mainString + ".Max Level");

            } else {

                enchantmentLevel = 1;

            }

            hashMap.put(Enchantment.getByName(string), enchantmentLevel);

        }

        return hashMap;

    }

    private static int totalSecondaryEnchantmentCount(HashMap<Enchantment, Integer> validSecondaryEnchantments) {

        int totalCount = 0;

        for (Enchantment enchantment : validSecondaryEnchantments.keySet())
            totalCount += validSecondaryEnchantments.get(enchantment);

        return totalCount;

    }

    //Version and subVersion should be set to the update at which the enchantment was introduced
    private static boolean enchantmentBackwardsCompatibility(int version, int subVersion, String actualEnchantement, String enchantmentToAvoid) {

        //Version is always randomWords(MC: 1.xx.yy) xx is mandatory yy is optional
        //For versions with only an xx
        if (Bukkit.getBukkitVersion().split("[.]").length < 4) {
            return actualEnchantement.equalsIgnoreCase(enchantmentToAvoid) && Integer.parseInt(Bukkit.getBukkitVersion().split("[.]")[1].substring(0, 2)) < version;
        }
        //For other versions with yy
        return actualEnchantement.equalsIgnoreCase(enchantmentToAvoid) && Integer.parseInt(Bukkit.getBukkitVersion().split("[.]")[1]) < version ||
                actualEnchantement.equalsIgnoreCase(enchantmentToAvoid) && Integer.parseInt(Bukkit.getBukkitVersion().split("[.]")[1]) == version &&
                        Integer.parseInt(Bukkit.getBukkitVersion().split("[.]")[2].replace(")", "")) < subVersion;

    }

}
