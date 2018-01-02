import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * HTTPClient
 * By SL at 2018/1/2 14:35
 */
public class HTTPClient implements Callable<String>{
    private Socket socket;
    private String player;
    private String card;

    public HTTPClient(String host, String port) throws IOException {
        socket = new Socket(host, Integer.parseInt(port));
    }

    @Override
    public String call() throws Exception {
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer.println("GET /" + player + "/" + card);

        String t;
        StringBuilder result = new StringBuilder();
        while((t = reader.readLine()) != null){
            result.append(t);
        }
        return result.toString();
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
