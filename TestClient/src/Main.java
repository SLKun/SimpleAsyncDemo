import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Main {
    public static void main(String[] args) {
        final String[] players = {"Alice", "Bob", "Cathy", "David"};
        final String[] cardNames = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
        Random random = new Random(System.currentTimeMillis());
        Map<String, Double> scores = new HashMap<String, Double>();
        List<String> results = new ArrayList<String>();

        try{
            for(int i = 0; i < 20; i++){
                HTTPClient c = new HTTPClient("127.0.0.1", "8080");
                FutureTask<String> task = new FutureTask<String>(c);
                Thread t = new Thread(task);
                c.setPlayer(players[random.nextInt(players.length)]);
                c.setCard(cardNames[random.nextInt(cardNames.length)]);
                t.start();

                RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
                double interval = (System.currentTimeMillis() - mxBean.getStartTime()) / 1000.0;
                System.err.printf(String.format("%.3f: %s\n", interval, task.get()));

                Double score = scores.get(c.getPlayer()) == null ? 0 : scores.get(c.getPlayer());
                Double thisScore = Double.valueOf(task.get().split(" ")[2]);
                if(score >= 0){
                    scores.put(c.getPlayer(), score + thisScore); // update score
                }
                // check winner and dead
                if(score + thisScore < 10.6 && score + thisScore > 10.4){
                    scores.clear();
                    results.add(String.format("%.3f: Player %s wined.", interval,  c.getPlayer()));
                }else if(score + thisScore > 10.6){
                    scores.put(c.getPlayer(), -1.0);
                    results.add(String.format("%.3f: Player %s dead.", interval, c.getPlayer()));
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(ExecutionException e){
            e.printStackTrace();
        }
        for(String s : results){
            System.out.println(s);
        }
    }
}
