import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class Score {

    private int score;
    public Score() {
        this.score = 0;
    }

    public void initScoreFromFile() {
        File file = new File("highScores.txt");
        Scanner sc;
        try {
            sc = new Scanner(file);
            score = sc.nextInt();
            sc.close();
        } catch (Exception ignored) {
        }
    }

    public boolean biggerThan(Score otherScore) {
        return this.score > otherScore.score;
    }

    public void add(int i) {
        score += i;
    }

    public void clear() {
        PrintWriter out;
        try {
            out = new PrintWriter("highScores.txt");
            out.println("0");
            out.close();
        } catch (Exception ignored) {
        }
        score = 0;
    }

    public void updateScore() {
        PrintWriter out;
        try {
            out = new PrintWriter("highScores.txt");
            out.println(score);
            out.close();
        } catch (Exception ignored) {
        }
    }

    public int getScore() {
        return score;
    }
}
