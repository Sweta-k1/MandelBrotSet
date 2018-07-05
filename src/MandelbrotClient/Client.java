
package MandelbrotClient;

import MandelbrotClient.data.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Sweta
 */
public class Client {

    private int hostport;

    private Socket s;

    private static PrintWriter out;

    private BufferedReader in;

    public static String name;

    private int line;

    public Client(int hostport) {

        this.hostport = hostport;

    }
// This method is used to connect client to the server with the socket constructor with inetaddrress and hostport as the arguments.

    public void connect() {

        try {

            s = new Socket(InetAddress.getLocalHost(), hostport);

            out = new PrintWriter(s.getOutputStream());

            in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        } catch (IOException e) {

            System.out.println("Client connection error!");

        }

    }
// sends message to the response recieved through clientmain.

    public void sendMessage(int[] colors) {

        String s = line + "#";

        for (int i = 0; i < colors.length; i++) {

            s += colors[i] + "#";

        }

        out.println(s);

        out.flush();

    }
//This is for reading through the lines wherein we send the input in the form of bytes through the client main.

    public String readResponse() throws IOException {

        String s = in.readLine();

        System.out.println("Line : " + s);

        return s;

    }
// this method parses through the points for the zooming factor/effect.

    public Point[] parseLine(String input) {

        String[] p = input.split("#");

        this.line = Integer.parseInt(p[0]);

        Point[] line = new Point[Integer.parseInt(p[1])];

        int n = 1; //zoom

        int x = 0, y = 0; //translation

        double yy = -1.25 / n + y + 2.5 * (((double) this.line) / Integer.parseInt(p[2])) / n, xx;

        for (int i = 0; i < line.length; i++) {

            xx = -2.25 / n + x + 3.0 * (((double) i) / line.length) / n;

            line[i] = new Point(xx, yy);

        }

        return line;

    }
// does the ierations for the points to colour the mandelbrot image.

    public int[] calculate(Point[] line) {

        int[] colors = new int[line.length];

        for (int i = 0; i < line.length; i++) {

            colors[i] = iterate(line[i]);

        }

        return colors;

    }
//  this method is for itervatively creating the points for getting the image.It takes the values of re and im and does the calculations to get the perfect set.

    private int iterate(Point point) {

        double re=0.0,im=0.0,abs=0.0,tmp=0.0;

        int i = 0;

        do {

            tmp = re;

            re = re * re - im * im + point.getX();

            im = 2 * tmp * im + point.getY();

            abs = re * re + im * im;

        } while ((abs < 100) && (i++ < 254));

        return i;

    }

}
