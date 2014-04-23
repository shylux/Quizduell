package quizduell;

import java.util.List;
import java.util.Scanner;

public class DuellConsole {
	enum State {LOGGED_OUT, MENU, DUEL};
	
	Player currentPlayer = null;
	Duel currentDuel = null;
	
	public static void main(String[] args) {
		DuellConsole console = new DuellConsole();
		console.startConsole(new Quizduell());
	}
	
	public static void print(String s, Object... args) { System.out.format(s, args); }
	public static void println(String s, Object... args) { System.out.format(s+'\n', args); }
	public static void error(String s, Object... args) { System.err.format(s+'\n', args); }
	
	public State getState() {
		if (currentPlayer == null) return State.LOGGED_OUT;
		if (currentDuel == null) return State.MENU;
		return State.DUEL;
	}
	
	public void startConsole(Quizduell quiz) {
		Scanner s = new Scanner(System.in);
		
		while (true) {

			switch (getState()) {

			case LOGGED_OUT:
				println("(L)og in  (Q)uit");
				switch (s.next().toLowerCase()) {

				// LOGIN
				case "l":
				case "login":
					print("Username: ");
					String playerName = s.next();
					currentPlayer = quiz.getPlayer(playerName);
					println("Logged in!");
					break;

				// QUIT
				case "q":
				case "quit":
					error("Program Terminated.");
					return;
				default:
					error("Command not recognised.");
				}
				break;

			case MENU:
				println("Username: %s || (D)isplay Duels (S)elect Duel (L)og out", currentPlayer);
				switch(s.next().toLowerCase()) {
				// NEW DUEL
				case "s":
				case "select":
				case "select duel":
					println("Available opponents: ");
					for (String name: quiz.getOpponentNames(currentPlayer)) println(name);
					print("Select opponent: ");
					String opponentName = s.next();
					currentDuel = quiz.getDuel(currentPlayer, quiz.getPlayer(opponentName));
					break;
					
				// DISPLAY (all duels)
				case "d":
				case "display":
				case "display duels":
					for (Duel duel: quiz.getDuels()) {
						println(duel.toString());
					}
					break;
					
				// LOGOUT
				case "l":
				case "logout":
					currentPlayer = null;
					println("Logged out.");
					break;
					
				}
				break;
			
			case DUEL:
				println("*** DUEL ***");
				println("Username: %s || %s", currentPlayer, currentDuel);

				switch (currentDuel.getState()) {
				case IN_PROGRESS:

					Round currentRound = currentDuel.getCurrentRound();
					if (currentRound.getCurrentPlayer() != currentPlayer) {
						error("Waiting for opponent to answer the questions.");
						currentDuel = null;
						break;
					}
					
					Question[] questions = currentRound.getQuestions();
					String[] answers = new String[questions.length];
					for (int i = 0; i < questions.length; i++) {
						Question quest = questions[i];
						List<String> possibleAnswers = quest.getShuffeledAnswers();
						println("*** QUESTION %d ***", i+1);
						println(quest.getQuestion());
						for (int j = 0; j < possibleAnswers.size(); j++) {
							println("%d: %s", j, possibleAnswers.get(j));
						}
						print("Answer: ");
						while (true) {
							if (!s.hasNextInt()) {
								s.next(); //consume wrong number
								error("Please enter a valid number.");
								continue;
							}
							int answer = s.nextInt();
							if (answer < 0 || answer > possibleAnswers.size()-1) {
								error("Invalid answer number.");
								continue;
							}
							answers[i] = possibleAnswers.get(answer);
							
							if (answers[i].equals(quest.getCorrectAnswer()))
								println("Correct Answer!");
							else
								println("Wrong Answer! Correct answer was '%s'", quest.getCorrectAnswer());

							break;
						}
					}
					currentRound.answer(currentPlayer, answers);
					println("Answers saved.");
					
					if (currentDuel.getState() == Duel.State.FINISHED) {
						if (currentDuel.getWinner() == null)
							error("DRAW!");
						else if (currentDuel.getWinner() == currentPlayer)
							error("YOU WON!");
						else
							error("YOU LOST!");
					}
					
					currentDuel = null;
					break;
				
				case FINISHED:
					
					error("This duel has finished.\nDo you want to delete it to start a new one? (y/N)");
					String answer = s.next().toLowerCase();
					if (answer.equals("y") || answer.equals("yes"))
						currentDuel = quiz.restartDuel(currentDuel, currentPlayer);
					else
						currentDuel = null;
				}
			}
		}
	}
}
