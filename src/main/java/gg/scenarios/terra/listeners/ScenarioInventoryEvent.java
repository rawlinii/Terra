package gg.scenarios.terra.listeners;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameManager;
import gg.scenarios.terra.managers.Reference;
import gg.scenarios.terra.managers.TeamState;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.Utils;
import org.bukkit.event.Listener;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class ScenarioInventoryEvent implements Listener {

        private Terra uhc = Terra.getInstance();
        private Utils utils = uhc.getUtils();
        private GameManager gameManager = uhc.getGameManager();
        private Reference reference = uhc.getReference();

        private String teamSizeToString() {
            if (gameManager.getTeamState() == TeamState.SOLO) {
                return "FFA";
            } else {
                return "To" + gameManager.getTeamSize();
            }
        }

        @EventHandler(priority = EventPriority.NORMAL)
        public void onInventoryClick(InventoryClickEvent event) {
            try {

                if (event.getCurrentItem().getType() == Material.AIR) return;
                HumanEntity player = event.getWhoClicked();
                if (event.getInventory().getTitle().equals("Scenarios")) {
                    try {
                        Scenario scen = uhc.getScenarioManager().getScenarioItemStack(event.getCurrentItem());
                        if (event.getCurrentItem().getType().equals(Material.AIR)) event.setCancelled(true);
                        if (scen.isEnabled()) {
                            scen.disable();
                            event.getInventory().setItem(event.getSlot(), scen.getAdminItemStack());
                        } else {
                            Class<? extends Scenario> scenClazz = scen.getClass();
                            if (uhc.getScenarioManager().getEnabledScenarios().stream().allMatch(s -> s.isCompatibleWith(scenClazz))) {
                                scen.enable();
                                event.getInventory().setItem(event.getSlot(), scen.getAdminItemStack());
                            } else {
                                player.sendMessage(ChatColor.RED + scen.getName() + " is not compatible with one or more other scenarios!" );
                            }
                        }
                        event.setCancelled(true);
                    } catch (NullPointerException e) {

                    }
                    event.setCancelled(true);
                } else if (event.getInventory().getName().equalsIgnoreCase("Active Scenarios") || event.getInventory().getName().equalsIgnoreCase("Player Inventory")) {
                    event.setCancelled(true);
                } else if (event.getInventory().getName().equalsIgnoreCase("Config")) {
                    event.setCancelled(true);
                } else if (event.getInventory().getName().equalsIgnoreCase("Game Editor")) {
                    if (event.getCurrentItem().getType().equals(Material.DIAMOND_SWORD)) {
                        ItemMeta meta = event.getCurrentItem().getItemMeta();
                        if (event.getClick().equals(ClickType.RIGHT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setPvpTime(uhc.getGameManager().getPvpTime() + 1);
                            meta.setDisplayName("§cPVP Time " + reference.getArrow() + " §e" + uhc.getGameManager().getPvpTime());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        } else if (event.getClick().equals(ClickType.LEFT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setPvpTime(uhc.getGameManager().getPvpTime() - 1);
                            meta.setDisplayName("§cPVP Time " + reference.getArrow() + " §e" + uhc.getGameManager().getPvpTime());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        }

                    } else if (event.getCurrentItem().getType().equals(Material.ENDER_PORTAL_FRAME)) {
                        ItemMeta meta = event.getCurrentItem().getItemMeta();
                        if (event.getClick().equals(ClickType.RIGHT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setBorderTime(uhc.getGameManager().getBorderTime() + 1);
                            meta.setDisplayName("§cBorder Time " + reference.getArrow() + " §e" + uhc.getGameManager().getBorderTime());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        } else if (event.getClick().equals(ClickType.LEFT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setBorderTime(uhc.getGameManager().getBorderTime() - 1);
                            meta.setDisplayName("§cBorder Time " + reference.getArrow() + " §e" + uhc.getGameManager().getBorderTime());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        }
                    } else if (event.getCurrentItem().getType().equals(Material.APPLE)) {
                        ItemMeta meta = event.getCurrentItem().getItemMeta();
                        if (event.getClick().equals(ClickType.RIGHT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setHealTime(uhc.getGameManager().getHealTime() + 1);
                            meta.setDisplayName("§cHeal Time " + reference.getArrow() + " §e" + uhc.getGameManager().getHealTime());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        } else if (event.getClick().equals(ClickType.LEFT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setHealTime(uhc.getGameManager().getHealTime() - 1);
                            meta.setDisplayName("§cHeal Time " + reference.getArrow() + " §e" + uhc.getGameManager().getHealTime());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        }
                    } else if (event.getCurrentItem().getType().equals(Material.ANVIL)) {
                        ItemMeta meta = event.getCurrentItem().getItemMeta();
                        if (event.getClick().equals(ClickType.RIGHT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setMeetupTime(uhc.getGameManager().getMeetupTime() + 1);
                            meta.setDisplayName("§cMeetup Time " + reference.getArrow() + " §e" + uhc.getGameManager().getMeetupTime());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        } else if (event.getClick().equals(ClickType.LEFT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setMeetupTime(uhc.getGameManager().getMeetupTime() - 1);
                            meta.setDisplayName("§cMeetup Time " + reference.getArrow() + " §e" + uhc.getGameManager().getMeetupTime());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        }
                    } else if (event.getCurrentItem().getType().equals(Material.BEACON)) {
                        ItemMeta meta = event.getCurrentItem().getItemMeta();
                        if (event.getClick().equals(ClickType.RIGHT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setTeamSize(uhc.getGameManager().getTeamSize() + 1);
                            uhc.getGameManager().setTeamState(TeamState.TEAM);
                            meta.setDisplayName("§cTeam Size " + reference.getArrow() + " §e" + teamSizeToString());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        } else if (event.getClick().equals(ClickType.LEFT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setTeamSize(uhc.getGameManager().getTeamSize() - 1);
                            if (uhc.getGameManager().getTeamSize() == 0) {
                                uhc.getGameManager().setTeamState(TeamState.SOLO);
                            }
                            meta.setDisplayName("§cTeam Size " + reference.getArrow() + " §e" + teamSizeToString());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        }
                    } else if (event.getCurrentItem().getType().equals(Material.GOLDEN_APPLE)) {
                        ItemMeta meta = event.getCurrentItem().getItemMeta();
                        if (event.getClick().equals(ClickType.RIGHT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setAppleRates(uhc.getGameManager().getAppleRates() + 1);
                            meta.setDisplayName("§cApple Rates " + reference.getArrow() + " §e" + uhc.getGameManager().getAppleRates());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        } else if (event.getClick().equals(ClickType.LEFT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setAppleRates(uhc.getGameManager().getAppleRates() - 1);
                            meta.setDisplayName("§cApple Rates " + reference.getArrow() + " §e" + uhc.getGameManager().getAppleRates());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        }
                    } else if (event.getCurrentItem().getType().equals(Material.GRAVEL)) {
                        ItemMeta meta = event.getCurrentItem().getItemMeta();
                        if (event.getClick().equals(ClickType.RIGHT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setFlintRates(uhc.getGameManager().getFlintRates() + 1);
                            meta.setDisplayName("§cFlint Rates " + reference.getArrow() + " §e" + uhc.getGameManager().getFlintRates());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        } else if (event.getClick().equals(ClickType.LEFT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setFlintRates(uhc.getGameManager().getFlintRates() - 1);
                            meta.setDisplayName("§cFlint Size " + reference.getArrow() + " §e" + uhc.getGameManager().getFlintRates());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        }
                    } else if (event.getCurrentItem().getType().equals(Material.OBSIDIAN)) {
                        ItemMeta meta = event.getCurrentItem().getItemMeta();
                        if (event.getClick().equals(ClickType.RIGHT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setNether(false);
                            meta.setDisplayName("§cNether " + reference.getArrow() + " §e" + uhc.getGameManager().isNether());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        } else if (event.getClick().equals(ClickType.LEFT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setNether(true);
                            meta.setDisplayName("§cNether " + reference.getArrow() + " §e" + uhc.getGameManager().isNether());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        }
                    }else if (event.getCurrentItem().getType().equals(Material.POTION)) {
                        ItemMeta meta = event.getCurrentItem().getItemMeta();
                        if (event.getClick().equals(ClickType.RIGHT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setAbsorption(false);
                            meta.setDisplayName("§cAbsorption " + reference.getArrow() + " §e" + uhc.getGameManager().isAbsorption());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        } else if (event.getClick().equals(ClickType.LEFT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setAbsorption(true);
                            meta.setDisplayName("§cAbsorption " + reference.getArrow() + " §e" + uhc.getGameManager().isAbsorption());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        }
                    }  else if (event.getCurrentItem().getType().equals(Material.GOLD_BLOCK)) {
                        ItemMeta meta = event.getCurrentItem().getItemMeta();
                        if (event.getClick().equals(ClickType.RIGHT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setNotch(false);
                            meta.setDisplayName("§cNotches " + reference.getArrow() + " §e" + uhc.getGameManager().isNotch());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        } else if (event.getClick().equals(ClickType.LEFT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setNotch(true);
                            meta.setDisplayName("§cNotches " + reference.getArrow() + " §e" + uhc.getGameManager().isNotch());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        }
                    } else if (event.getCurrentItem().getType().equals(Material.SHEARS)) {
                        ItemMeta meta = event.getCurrentItem().getItemMeta();
                        if (event.getClick().equals(ClickType.RIGHT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setShears(false);
                            meta.setDisplayName("§cShears " + reference.getArrow() + " §e" + uhc.getGameManager().isShears());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        } else if (event.getClick().equals(ClickType.LEFT)) {
                            event.setCancelled(true);
                            uhc.getGameManager().setShears(true);
                            meta.setDisplayName("§cShears " + reference.getArrow() + " §e" + uhc.getGameManager().isShears());
                            event.getCurrentItem().setItemMeta(meta);
                            event.getInventory().setItem(event.getSlot(), event.getCurrentItem());
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }catch (NullPointerException e) {
            }
        }
    }

