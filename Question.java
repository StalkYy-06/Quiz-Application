package quiz_application;

/**
 * The {@code Question} class represents a quiz question.
 * It stores the question text, possible answers, and the correct answer.
 */
public class Question {
    private String question;
    private String[] options;
    private int correctOption;
    
    /**
     * Constructs a new {@code Question}.
     *
     * @param question      The text of the question.
     * @param options       An array of four possible answers.
     * @param correctOption The index (1-4) of the correct answer.
     */
    public Question(String question, String[] options, int correctOption) {
        this.question = question;
        this.options = options;
        this.correctOption = correctOption;
    }
    
    /**
     * Gets the question text.
     * @return The question as a {@code String}.
     */
    public String getQuestion(){ 
    	return question; 
    	}
    
    /**
     * Gets the list of answer choices.
     * @return A {@code String} array of four answer options.
     */
    public String[] getOptions(){ 
    	return options; 
    	}
    
    /**
     * Gets the correct answer's index.
     * @return An {@code int} representing the correct option (1-4).
     */
    public int getCorrectOption() { 
    	return correctOption; 
    	}
}
