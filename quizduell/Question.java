package quizduell;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Question {
	public static List<Question> QUESTION_POOL = new ArrayList<Question>();
	
	private String question;
	private List<String> answers = new ArrayList<String>();
	private String correctAnswer;
	
	public String getQuestion() {
		return question;
	}

	public List<String> getShuffeledAnswers() {
		Collections.shuffle(answers);
		return answers;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	static {
		QUESTION_POOL.add(new Question("Was ist eine Stadt der Schweiz?", new String[] {"Biel", "Berlin", "Bamberg", "Bremen"}, "Biel"));
		QUESTION_POOL.add(new Question("Was ist ein Teil einer Digitalkamera?", new String[] {"Sieb", "Maus", "Chip", "Eimer"}, "Chip"));
		QUESTION_POOL.add(new Question("Was ist KEINE Stadt der Schweiz?", new String[] {"Berlin", "Bern", "Zürich", "Basel"}, "Berlin"));
	}
	
	public Question(String quest, String[] ans, String correctAns) {
		question = quest;
		answers = Arrays.asList(ans);
		correctAnswer = correctAns;
	}
	
	public static Question getRandomQuestion() {
		SecureRandom random = new SecureRandom();
		return QUESTION_POOL.get(random.nextInt(QUESTION_POOL.size()));
	}
}
