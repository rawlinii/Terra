package gg.scenarios.terra.teams;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.Reference;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import gg.scenarios.terra.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class Teams {
    private Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
    private ArrayList<Team> teams = new ArrayList<Team>();
    private Terra terra = Terra.getInstance();
    private Utils utils = terra.getUtils();
    private Reference reference = terra.getReference();

    public Teams() {
    }

    /**
     * Gets a list of all teams.
     *
     * @return the list of teams.
     */
    public List<Team> getTeams() {
        return teams;
    }

    /**
     * Leaves the current team of the player.
     *
     * @param player the player leaving.
     */
    public void leaveTeam(Player player) {
        if (this.getTeam(player) != null) {
            this.getTeam(player).removePlayer(player);
        }
    }

    /**
     * Joins a team.
     *
     * @param teamName the team joining.
     * @param player   the player joining.
     */
    public void joinTeam(String teamName, Player player) {
        Team team = sb.getTeam(teamName);
        team.addPlayer(player);
    }

    private void giveDiamond(Player player) {
        player.getInventory().setItem(0, new ItemStack(Material.DIAMOND, 48));
    }

    @SuppressWarnings("deprecation")
    public void slaveOwner(Player player, int i) {
        switch (i) {
            case 1:
                Team team = getTeamFromColor(ChatColor.RED);
                team.addPlayer(player);
               // UHCPlayer.getByName(player.getName()).setSlave(false);
                giveDiamond(player);
            //    player.teleport(u.getGameManager().getTeamLocationHashMap().get(ChatColor.RED));
                utils.broadcast(reference.getMain() + ChatColor.RED + player.getName() + " has been added to team 1!");
                break;
            case 2:
                team = getTeamFromColor(ChatColor.BLUE);
                team.addPlayer(player);
                //UHCPlayer.getByName(player.getName()).setSlave(false);
                giveDiamond(player);
              //  player.teleport(u.getGameManager().getTeamLocationHashMap().get(ChatColor.BLUE));
                utils.broadcast(reference.getMain() + ChatColor.BLUE + player.getName() + " has been added to team 2!");
                break;
            case 3:
                team = getTeamFromColor(ChatColor.GREEN);
                team.addPlayer(player);
               // UHCPlayer.getByName(player.getName()).setSlave(false);
                giveDiamond(player);
            //    player.teleport(u.getGameManager().getTeamLocationHashMap().get(ChatColor.GREEN));
                utils.broadcast(reference.getMain() + ChatColor.GREEN + player.getName() + " has been added to team 3!");
                break;
            case 4:
                team = getTeamFromColor(ChatColor.DARK_PURPLE);
                team.addPlayer(player);
             //   UHCPlayer.getByName(player.getName()).setSlave(false);
                giveDiamond(player);
              //  player.teleport(u.getGameManager().getTeamLocationHashMap().get(ChatColor.DARK_PURPLE));
                utils.broadcast(reference.getMain() + ChatColor.DARK_PURPLE + player.getName() + " has been added to team 4!");
                break;
            case 5:
                team = getTeamFromColor(ChatColor.LIGHT_PURPLE);
                team.addPlayer(player);
             //   UHCPlayer.getByName(player.getName()).setSlave(false);
                giveDiamond(player);
               // player.teleport(u.getGameManager().getTeamLocationHashMap().get(ChatColor.LIGHT_PURPLE));
                utils.broadcast(reference.getMain() + ChatColor.LIGHT_PURPLE + player.getName() + " has been added to team 5!");
                break;
            case 6:
                team = getTeamFromColor(ChatColor.YELLOW);
                team.addPlayer(player);
              //  UHCPlayer.getByName(player.getName()).setSlave(false);
                giveDiamond(player);
               // player.teleport(u.getGameManager().getTeamLocationHashMap().get(ChatColor.YELLOW));

                utils.broadcast(reference.getMain() + ChatColor.YELLOW + player.getName() + " has been added to team 6!");
                ;
                break;
            case 7:
                team = getTeamFromColor(ChatColor.AQUA);
                team.addPlayer(player);
                //UHCPlayer.getByName(player.getName()).setSlave(false);
                giveDiamond(player);
              //  player.teleport(u.getGameManager().getTeamLocationHashMap().get(ChatColor.AQUA));
                utils.broadcast(reference.getMain() + ChatColor.AQUA + player.getName() + " has been added to team 7!");
                ;
                break;//BLACK, AQUA, YELLOW, PINK, PURPLE, GREEN, BLUE, RED
            case 8:
                team = getTeamFromColor(ChatColor.BLACK);
                team.addPlayer(player);
                //UHCPlayer.getByName(player.getName()).setSlave(false);
                giveDiamond(player);
             //   player.teleport(u.getGameManager().getTeamLocationHashMap().get(ChatColor.BLACK));
                utils.broadcast(reference.getMain() + ChatColor.BLACK + player.getName() + " has been added to team 8!");
                ;
                break;

        }
    }
    @SuppressWarnings("deprecation")
    public void addToTeam(Player player, int i) {
        switch (i) {
            case 1:
                Team team = getTeamFromColor(ChatColor.RED);
                team.addPlayer(player);
                //player.teleport(u.getGameManager().getTeamLocationHashMap().get(ChatColor.RED));
                utils.broadcast(reference.getMain() + ChatColor.RED + player.getName() + " has been added to team 1!");
                break;
            case 2:
                team = getTeamFromColor(ChatColor.BLUE);
                team.addPlayer(player);
               // player.teleport(u.getGameManager().getTeamLocationHashMap().get(ChatColor.BLUE));
                utils.broadcast(reference.getMain() + ChatColor.BLUE + player.getName() + " has been added to team 2!");
                break;
            case 3:
                team = getTeamFromColor(ChatColor.GREEN);
                team.addPlayer(player);
             //   player.teleport(u.getGameManager().getTeamLocationHashMap().get(ChatColor.GREEN));
                utils.broadcast(reference.getMain() + ChatColor.GREEN + player.getName() + " has been added to team 3!");
                break;
            case 4:
                team = getTeamFromColor(ChatColor.DARK_PURPLE);
                team.addPlayer(player);
            //    player.teleport(u.getGameManager().getTeamLocationHashMap().get(ChatColor.DARK_PURPLE));
                utils.broadcast(reference.getMain() + ChatColor.DARK_PURPLE + player.getName() + " has been added to team 4!");
                break;
            case 5:
                team = getTeamFromColor(ChatColor.LIGHT_PURPLE);
                team.addPlayer(player);
           //     player.teleport(u.getGameManager().getTeamLocationHashMap().get(ChatColor.LIGHT_PURPLE));
                utils.broadcast(reference.getMain() + ChatColor.LIGHT_PURPLE + player.getName() + " has been added to team 5!");
                break;
            case 6:
                team = getTeamFromColor(ChatColor.YELLOW);
                team.addPlayer(player);
             //   player.teleport(u.getGameManager().getTeamLocationHashMap().get(ChatColor.YELLOW));
                utils.broadcast(reference.getMain() + ChatColor.YELLOW + player.getName() + " has been added to team 6!");
                break;
            case 7:
                team = getTeamFromColor(ChatColor.AQUA);
                team.addPlayer(player);
       //         player.teleport(u.getGameManager().getTeamLocationHashMap().get(ChatColor.AQUA));
                utils.broadcast(reference.getMain() + ChatColor.AQUA + player.getName() + " has been added to team 7!");
                break;//BLACK, AQUA, YELLOW, PINK, PURPLE, GREEN, BLUE, RED
            case 8:
                team = getTeamFromColor(ChatColor.BLACK);
                team.addPlayer(player);
                giveDiamond(player);
         //       player.teleport(u.getGameManager().getTeamLocationHashMap().get(ChatColor.BLACK));
                utils.broadcast(reference.getMain() + ChatColor.BLACK + player.getName() + " has been added to team 8!");
                break;

        }
    }


    public Team getTeamFromColor(ChatColor chatColor) {
        return teams.stream().filter(team -> team.getPrefix().equalsIgnoreCase(chatColor.toString())).findFirst().orElseThrow(NullPointerException::new);

    }

    /**
     * Gets the team of a player.
     *
     * @param player the player wanting.
     * @return The team.
     */
    public Team getTeam(Player player) {
        return player.getScoreboard().getPlayerTeam(player);
    }

    /**
     * Sets up all the teams.
     */
    public void setupTeams() {
        ArrayList<String> list = new ArrayList<String>();

        for (ChatColor color : ChatColor.values()) {
            if (color == ChatColor.RESET || color == ChatColor.MAGIC || color == ChatColor.BOLD || color == ChatColor.ITALIC || color == ChatColor.UNDERLINE || color == ChatColor.STRIKETHROUGH) {
                continue;
            }

            list.add(color.toString());
        }

        ArrayList<String> list2 = new ArrayList<String>();

        for (String li : list) {
            list2.add(li + ChatColor.BOLD);
            list2.add(li + ChatColor.ITALIC);
            list2.add(li + ChatColor.UNDERLINE);
            list2.add(li + ChatColor.STRIKETHROUGH);
            list2.add(li + ChatColor.BOLD + ChatColor.ITALIC);
            list2.add(li + ChatColor.BOLD + ChatColor.ITALIC + ChatColor.UNDERLINE);
            list2.add(li + ChatColor.BOLD + ChatColor.ITALIC + ChatColor.STRIKETHROUGH);
            list2.add(li + ChatColor.BOLD + ChatColor.ITALIC + ChatColor.STRIKETHROUGH + ChatColor.UNDERLINE);
            list2.add(li + ChatColor.BOLD + ChatColor.STRIKETHROUGH + ChatColor.UNDERLINE);
            list2.add(li + ChatColor.BOLD + ChatColor.UNDERLINE);
            list2.add(li + ChatColor.BOLD + ChatColor.STRIKETHROUGH);
            list2.add(li + ChatColor.ITALIC + ChatColor.UNDERLINE);
            list2.add(li + ChatColor.ITALIC + ChatColor.STRIKETHROUGH);
            list2.add(li + ChatColor.ITALIC + ChatColor.STRIKETHROUGH + ChatColor.UNDERLINE);
            list2.add(li + ChatColor.UNDERLINE + ChatColor.STRIKETHROUGH);
        }

        list.remove(ChatColor.WHITE.toString());
        list.remove(ChatColor.GRAY.toString() + ChatColor.ITALIC.toString());
        list.remove(ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + ChatColor.STRIKETHROUGH.toString());

        list.addAll(list2);

        Team spec = (sb.getTeam("spec") == null ? sb.registerNewTeam("spec") : sb.getTeam("spec"));

        spec.setDisplayName("spec");
        spec.setPrefix("§7§o");
        spec.setSuffix("§r");
        spec.setAllowFriendlyFire(false);
        spec.setCanSeeFriendlyInvisibles(true);

        Team dead = (sb.getTeam("dead") == null ? sb.registerNewTeam("dead") : sb.getTeam("dead"));

        dead.setDisplayName("dead");
        dead.setPrefix("§7§o§m");
        dead.setSuffix("§r");
        dead.setAllowFriendlyFire(false);
        dead.setCanSeeFriendlyInvisibles(false);

        for (int i = 0; i < list.size(); i++) {
            Team team = (sb.getTeam("UHC" + (i + 1)) == null ? sb.registerNewTeam("UHC" + (i + 1)) : sb.getTeam("UHC" + (i + 1)));

            team.setDisplayName("UHC" + (i + 1));
            team.setPrefix(list.get(i));
            team.setSuffix("§r");
            team.setAllowFriendlyFire(true);
            team.setCanSeeFriendlyInvisibles(true);
            teams.add(team);

        }
    }
}