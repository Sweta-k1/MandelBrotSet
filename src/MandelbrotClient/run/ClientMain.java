package MandelbrotClient.run;

import MandelbrotClient.Client;
import java.io.IOException;

/**
 *
 * @author Sweta
 */
public class ClientMain {

    public static void main(String[] args) {
        Client c = new Client(2222);

        c.connect();

        String input = "";

        boolean end = false;

        try {

            while (!end) {

                input = c.readResponse();

                if (input != null && input.startsWith("END")) {

                    System.out.println(input);

                    break;

                }

                c.sendMessage(c.calculate(c.parseLine(input)));

            }

        } catch (IOException e) {

        }

    }
}
