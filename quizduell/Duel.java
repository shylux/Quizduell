package quizduell;

import java.util.ArrayList;
import java.util.List;

/**
 * A quizduel between two players. 
 * @author lukas
 */
public class Duel {
	public static int PLAYERS_PER_DUEL = 2;
	public static int ROUNDS_PER_DUEL = 1;
	
	public enum State {IN_PROGRESS, FINISHED}
	
	Player player1;
	Player player2;
	
	List<Round> rounds = new ArrayList<Round>();
	
	public Duel(Player p1, Player p2) {
		player1 = p1;
		player2 = p2;
		rounds.add(new Round(this));
	}
	
	/**
	 * Returns the current state of the duel.
	 * A duel is finished when all of its rounds are finished.
	 * @return The current state of the duel.
	 */
	public State getState() {
		if (rounds.size() < ROUNDS_PER_DUEL) return State.IN_PROGRESS;
		if (allRoundsFinished()) return State.FINISHED;
		return State.IN_PROGRESS;
	}
	
	/**
	 * Checks if the current round is finished.
	 * The duel generates a new round if the old one is finished.
	 * @return True if all current rounds are finished
	 */
	private boolean allRoundsFinished() {
		for (Round round: rounds) {
			if (round.getState() != Round.State.FINISHED) return false;
		}
		return true;
	}
	
	/**
	 * Returns a String with all relevant duel data.
	 */
	public String toString() {
		String state;
		if (getState() == State.FINISHED) {
			Player winner = getWinner();
			if (winner == null) state = "Draw";
			else state = winner + " won!";
		} else
			state = getCurrentRound().getCurrentPlayer().toString()+"'s turn";
		return String.format("Duell %s vs %s: %d - %d: %s : Round %d", player1, player2, getScore(player1), getScore(player2), state, rounds.size());
	}
	
	/**
	 * 
	 * @param player
	 * @return
	 * @throws IllegalArgumentException
	 */
	public int getScore(Player player) throws IllegalArgumentException {
		if (player != player1 && player != player2) throw new IllegalArgumentException(String.format("Player '%s' does not exist in duel!", player));

		int score = 0;
		for (Round round: rounds) {
			score += round.getScore(player);
		}
		return score;
	}
	
	public Round getCurrentRound() throws IllegalStateException {
		for (Round round: rounds) {
			if (round.getState() == Round.State.IN_PROGRESS) return round;
		}
		throw new IllegalStateException("No round in progress. Duel may be finished.");
	}
	
	public void notifyAnswer() {
		if (getState() == State.FINISHED) return;
		if (!allRoundsFinished()) return;
		// soo they need a new round
		rounds.add(new Round(this));
	}
	
	public Player getWinner() throws IllegalStateException {
		if (getState() != State.FINISHED) throw new IllegalStateException("Game not finished.");
		int scorePlayer1 = getScore(player1);
		int scorePlayer2 = getScore(player2);
		if (scorePlayer1 == scorePlayer2) return null;
		return (scorePlayer1 > scorePlayer2) ? player1 : player2;
	}
}
