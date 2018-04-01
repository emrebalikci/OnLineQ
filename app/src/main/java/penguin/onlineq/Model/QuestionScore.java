package penguin.onlineq.Model;

/**
 * Created by emrebalikci on 27.12.2017.
 */

public class QuestionScore {
    private String Question_Score;
    private String User;
    private String Score;

    public QuestionScore(String question_Score, String user, String score) {
       this.Question_Score = question_Score;
        this.User = user;
       this.Score = score;
    }

    public String getQuestion_Score() {
        return Question_Score;
    }

    public void setQuestion_Score(String question_Score) {
        Question_Score = question_Score;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }
}
