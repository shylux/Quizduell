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
	
	public Player getPlayer(String name) {
		if (players.containsKey(name)) {
			return players.get(name);
		}
		Player p = new Player(name);
		players.put(p.getName(), p);
		System.out.format("Created Player '%s'\n", p.getName());
		return p;
	}
	
	private Set<Player> key(Player p1, Player p2) {
		Set<Player> key = new HashSet<Player>();
		key.add(p1);
		key.add(p2);
		return key;
	}
	
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
	
	public Set<String> getPlayerNames() {
		// use copy constructor to prevent changes to the hashmap
		return new HashSet<String>(players.keySet());
	}
	
	public Set<String> getOpponentNames(Player player) {
		Set<String> res = getPlayerNames();
		res.remove(player.getName());
		return res;
	}
	
	public Collection<Duel> getDuels() {
		return duels.values();
	}
	
	public Duel restartDuel(Duel duel, Player player1) {
		Player player2 = (player1 == duel.player1) ? duel.player2 : duel.player1;
		Set<Player> key = key(player1, player2);
		duels.remove(key);
		return getDuel(player1, player2);
	}
}
