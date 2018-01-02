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

/**
 * ${NAME}
 * By SL at 2018/1/2 10:55
 */
@WebServlet(name = "SimpleServlet", urlPatterns = "/*")
public class SimpleServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletOutputStream out = response.getOutputStream();
        String uri = request.getRequestURI();
        String[] uriParams = uri.split("/");
        String player = uriParams[1];
        String cardName = uriParams[2];
        // Check Player Name
        String[] players = {"Alice", "Bob", "Cathy", "David"};
        if(Arrays.binarySearch(players, player) < 0){
            out.println("Error Player.");
            return;
        }
        // Convert Card To Number
        double cardNum = -1;
        String[] cardNames = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
        for(int i = 0; i < cardNames.length; i++){
            if(cardName.equals(cardNames[i])){
                cardNum = i < 10 ? i + 1 : 0.5;
            }
        }
        out.println(player + " Played " + cardNum);
    }
}
