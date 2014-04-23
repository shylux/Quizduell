package quizduell;

import java.util.HashMap;
import java.util.Map;

public class Round {
	public static int QUESTIONS_PER_ROUND = 3;

	public enum State {IN_PROGRESS, FINISHED}

	private Duel context;
	private Question questions[];
	private Map<Player, String[]> answers = new HashMap<Player, String[]>();
	
	public Round(Duel ctx) {
		context = ctx;
		questions = new Question[QUESTIONS_PER_ROUND];
		for (int i = 0; i < QUESTIONS_PER_ROUND; i++) {
			questions[i] = Question.getRandomQuestion();
		}
	}
	
	public State getState() {
		if (answers.size() < Duel.PLAYERS_PER_DUEL) return State.IN_PROGRESS;
		return State.FINISHED;
	}
	
	/**
	 * Get score of a specific player.
	 * @param playerNr 0 is player1, 1 is player2
	 * @return
	 */
	public int getScore(Player player) {
		int score = 0;
		if (!answers.containsKey(player)) return 0;
		String[] ans = answers.get(player);
		for (int i = 0; i < QUESTIONS_PER_ROUND; i++) {
			if (ans[i] != null && ans[i] == questions[i].getCorrectAnswer()) score += 1;
		}
		return score;
	}
	
	public void answer(Player player, String[] ans) throws IllegalArgumentException {
		if (ans.length != QUESTIONS_PER_ROUND) throw new IllegalArgumentException("Not the correct amount of answers given.");
		for (int i = 0; i < QUESTIONS_PER_ROUND; i++) {
			if (ans[i] == null) throw new IllegalArgumentException("An answer is null!");
		}
		if (getCurrentPlayer() != player) throw new IllegalArgumentException("It's not your turn!");

		answers.put(player, ans);
		context.notifyAnswer();
	}
	
	public Player getCurrentPlayer() {
		switch (answers.size()) {
		case 0:
			return context.player1;
		case 1:
			return context.player2;
		default:
			return null;
		}
	}
	
	public boolean hasAnswered(Player player) {
		return answers.containsKey(player);
	}
	
	public Question[] getQuestions() {
		return questions;
	}
}
