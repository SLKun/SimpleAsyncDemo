import java.io.IOException;
import java.util.*;
import java.util.concurrent.FutureTask;

public class Main {
    private final static String[] players = {"Alice", "Bob", "Cathy", "David"};
    private final static String[] cardNames = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};

    private static double convertScore(String cardName) {
        for (int i = 0; i < cardNames.length; i++) {
            if (cardName.equals(cardNames[i])) {
                return i < 10 ? i + 1 : 0.5;
            }
        }
        throw new IllegalArgumentException("Unexpected Card Names.");
    }

    private static void initScores(Map<String, Double> scores) {
        for (String player : players) {
            scores.put(player, 0.0);
        }
    }

    public static void main(String[] args) {
        Random random = new Random(System.currentTimeMillis());
        Map<String, Double> scores = new HashMap<String, Double>();
        List<String> results = new ArrayList<String>();
        Integer deadCnt = 0;

        try {
            // init scores
            initScores(scores);
            for (int i = 0; i < 100; i++) {
                // generate threads to request server
                HTTPClient c = new HTTPClient("127.0.0.1", "8080");
                FutureTask<String> task = new FutureTask<String>(c);
                Thread t = new Thread(task);
                // random generate player and cards
                String player;
                do {
                    player = players[random.nextInt(players.length)];
                } while (scores.get(player) < 0);
                String card = cardNames[random.nextInt(cardNames.length)];
                c.setPlayer(player);
                c.setCard(card);
                t.start();
                // calculate scores
                Double score = scores.get(player);
                Double thisScore = convertScore(card);
                System.out.println(String.format("%d: Player %s get %.1f score.", i, player, thisScore));
                if (score >= 0) { // live
                    if (score + thisScore > 10.6) {
                        deadCnt++;
                        scores.put(c.getPlayer(), -1.0); // dead
                    } else {
                        scores.put(c.getPlayer(), score + thisScore); // update score
                    }
                }
                // print scores
                StringBuilder builder = new StringBuilder();
                for (String key : scores.keySet()) {
                    builder.append(key).append(": ").append(scores.get(key)).append(", ");
                }
                System.out.println(builder.toString());
                // to check winner
                if (score + thisScore < 10.6 && score + thisScore > 10.4) { // reach max point
                    deadCnt = 0;
                    initScores(scores);
                    results.add(String.format("Player %s wined: ", player) + builder.toString());
                    System.out.println(String.format("Player %s wined: ", player) + builder.toString());
                }
                if (deadCnt == 3) { // other player dead
                    String winner = "";
                    for (String key : scores.keySet()) {
                        if (scores.get(key) >= 0) {
                            winner = key;
                            break;
                        }
                    }
                    deadCnt = 0;
                    initScores(scores);
                    results.add(String.format("Player %s live in the end: ", winner) + builder.toString());
                    System.out.println(String.format("Player %s live in the end: ", winner) + builder.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("=============== Result =============");
        for (String r : results) {
            System.out.println(r);
        }
    }
}
