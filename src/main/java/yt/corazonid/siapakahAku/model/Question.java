package yt.corazonid.siapakahAku.model;

public class Question {
    private final String id;
    private final String clue1;
    private final String clue2;
    private final String clue3;
    private final String answer;

    public Question(String id, String clue1, String clue2, String clue3, String answer) {
        this.id = id;
        this.clue1 = clue1;
        this.clue2 = clue2;
        this.clue3 = clue3;
        this.answer = answer.toLowerCase();
    }

    public String getId() {
        return id;
    }

    public String getClue1() {
        return clue1;
    }

    public String getClue2() {
        return clue2;
    }

    public String getClue3() {
        return clue3;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isCorrectAnswer(String userAnswer) {
        return userAnswer.toLowerCase().trim().equals(answer);
    }

    @Override
    public String toString() {
        return String.format("Question[%s]", id);
    }
}

