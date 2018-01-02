package test;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * ${NAME}
 * By SL at 2018/1/2 10:55
 */
@WebServlet(name = "SimpleServlet", urlPatterns = "/*")
public class SimpleServlet extends HttpServlet {
    private static Map<String, Double> scores = new HashMap<>();
    private static Integer deadCnt = 0;
    private static Integer index = 0;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletOutputStream out = response.getOutputStream();
        String uri = request.getRequestURI();
        String[] uriParams = uri.split("/");
        if (uriParams.length != 3) {
            out.println("Invalid Request.");
            return;
        }

        String player = uriParams[1];
        String cardName = uriParams[2];
        // Check Player Name
        String[] players = {"Alice", "Bob", "Cathy", "David"};
        if (Arrays.binarySearch(players, player) < 0) {
            out.println("Error Player.");
            return;
        }

        // Convert Card To Number
        double cardNum = -1;
        String[] cardNames = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
        for (int i = 0; i < cardNames.length; i++) {
            if (cardName.equals(cardNames[i])) {
                cardNum = i < 10 ? i + 1 : 0.5;
            }
        }
        out.println(player + " Played " + cardName + ", GET " + cardNum + " points.");
//        System.out.println(++index + ": " + player + " Played " + cardName + ", GET " + cardNum + " points.");

        // calculate scores
        Double score = scores.get(player) == null ? 0.0 : scores.get(player);
        if (score >= 0) { // live
            if (score + cardNum > 10.6) {
                deadCnt++;
                scores.put(player, -1.0); // dead
            } else {
                scores.put(player, score + cardNum); // update score
            }
        }
        // print scores
        StringBuilder builder = new StringBuilder();
        for (String key : scores.keySet()) {
            builder.append(key).append(": ").append(scores.get(key)).append(", ");
        }
//        System.out.println(builder.toString());
        // to check winner
        if (score + cardNum < 10.6 && score + cardNum > 10.4) { // reach max point
            deadCnt = 0;
            scores.clear();
            System.err.println(String.format("Player %s wined: ", player) + builder.toString());
        }
        if (deadCnt == 3) {
            String winner = "";
            for (String key : scores.keySet()) {
                if (scores.get(key) >= 0) {
                    winner = key;
                    break;
                }
            }
            deadCnt = 0;
            scores.clear();
            System.err.println(String.format("Player %s live in the end: ", winner) + builder.toString());
        }
    }
}
