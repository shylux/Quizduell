package quizduell;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Main class for Quizduell.
 * Holds all relevant data.
 * @author lukas
 *
 */
public class Quizduell {
	Map<String, Player> players = new HashMap<String, Player>();
	Map<Set<Player>, Duel> duels = new HashMap<Set<Player>, Duel>();
	
	/**
	 * Gets/creates a player.
	 * Creates player if he does not exist.
	 * @param name Name of the player.
	 * @return The requested player.
	 */
	public Player getPlayer(String name) {
		if (players.containsKey(name)) {
			return players.get(name);
		}
		Player p = new Player(name);
		players.put(p.getName(), p);
		System.out.format("Created Player '%s'\n", p.getName());
		return p;
	}
	
	/**
	 * Generates a key from two players.
	 * This key can be used to access the duel map.
	 * @param p1 Player 1
	 * @param p2 Player 2
	 * @return The key (Set) to access the duel map.
	 */
	private Set<Player> key(Player p1, Player p2) {
		Set<Player> key = new HashSet<Player>();
		key.add(p1);
		key.add(p2);
		return key;
	}
	
	/**
	 * Gets/creates a duel.
	 * Creates the dues if it does not exist.
	 * @param p1 The challenger.
	 * @param p2 The challenged Player.
	 * @return The requested duel.
	 * @throws IllegalArgumentException Thrown if the same user is used for the challenger and challenged.
	 */
	public Duel getDuel(Player p1, Player p2) throws IllegalArgumentException {
		if (p1 == p2) throw new IllegalArgumentException("You can't play against yourself.");
		Set<Player> key = key(p1, p2);
		if (duels.containsKey(key)) {
			// return existing duel
			return duels.get(key);
		}
		// create new duel
		Duel newDuel = new Duel(p1,p2);
		duels.put(key, newDuel);
		System.out.format("New Duel between %s and %s!\n", newDuel.player1.getName(), newDuel.player2.getName());
		return newDuel;
	}
	
	/**
	 * Returns all player names.
	 * @return All player names.
	 */
	public Set<String> getPlayerNames() {
		// use copy constructor to prevent changes to the hashmap
		return new HashSet<String>(players.keySet());
	}
	
	/**
	 * Return all player names not equal to the player's name.
	 * @param player The player to be excluded from the list.
	 * @return List with all player names except the player.
	 */
	public Set<String> getOpponentNames(Player player) {
		Set<String> res = getPlayerNames();
		res.remove(player.getName());
		return res;
	}
	
	/**
	 * Returns all duels.
	 * @return All duels.
	 */
	public Collection<Duel> getDuels() {
		return duels.values();
	}
	
	/**
	 * Deletes a duel and creates a new one with the same players.
	 * @param duel The duel to be restarted.
	 * @param player1 The challenger.
	 * @return The restarted duel.
	 */
	public Duel restartDuel(Duel duel, Player player1) {
		Player player2 = (player1 == duel.player1) ? duel.player2 : duel.player1;
		Set<Player> key = key(player1, player2);
		duels.remove(key);
		return getDuel(player1, player2);
	}
}
