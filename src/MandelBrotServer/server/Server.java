/*Method for creating a graphical image of mandelbrot set and displaying it on server.*/

package MandelBrotServer.server;

import MandelBrotServer.Events.FinishEvent;
import MandelBrotServer.Events.FinishEventListener;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import javax.imageio.ImageIO;

/**
 *
 * @author Sweta
 */
public class Server {

    private static ServerSocket ss;

    private static boolean isRunning;

    public static int width = 4000;

    public static int height = 3000;

    private static int[] paletteR = new int[256];

    private static int[] paletteG = new int[256];

    private static int[] paletteB = new int[256];

    public static int lineIndex = 0;

    private static BufferedImage fractalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    private static Graphics2D graphics = fractalImage.createGraphics();

    private static int drawnLines = 0;

    private static LinkedList<FinishEventListener> finishListeners = new LinkedList<FinishEventListener>();

    public static int finishNR = 1;

//    Method for setting the pixel values of RBG to get the dimensions.
    private static void initPalette() {

        for (int i = 0; i < 256; i++) {

            paletteR[i] = (int) (127 + 127 * Math.sin(i * Math.PI / 90 + 125.87 * Math.PI / 180));

            paletteG[i] = (int) (127 + 127 * Math.sin(i * Math.PI / 90 + 161.8 * Math.PI / 180));

            paletteB[i] = (int) (127 + 127 * Math.sin(i * Math.PI / 90 + 219.17 * Math.PI / 180));

        }

    }

//    this checks if it finds the next line,if it ends,it will return -1.
    public static int getNextLine() {

        if (lineIndex < height) {

            return lineIndex++;

        } else {
            return -1;
        }

    }
    
//    This method translates the lines to image using the graphics with the values of R,B,G.Stops the server after translating the image.

    public static void writeLineToImage(int lineNR, int[] line) {

        for (int i = 0; i < width; i++) {

            graphics.setColor(new Color(paletteR[line[i]], paletteG[line[i]], paletteB[line[i]]));

            graphics.drawLine(i, lineNR, i, lineNR);

        }

        drawnLines++;

        if (drawnLines == height) {

            stopServer();

        }

    }
    
//    Connection between the client and the server is created by instatiating the socket constructor.

    public static void main(String[] args) throws IOException {

        initPalette();

        isRunning = true;

        try {

            ss = new ServerSocket(2222);

            System.out.println("Server started! --- Date:" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

            while (isRunning) {

                new ClientHandle(ss.accept()).start();

            }

        } catch (IOException e) {
        }

    }

//    We give the image file here and close the socket and server connetion.
    public static void stopServer() {
        try {

            ImageIO.write(fractalImage, "PNG", new File("src\\mandelbrot.png"));

            notifyFinish();

            ss.close();

        } catch (IOException e) {
        }

        isRunning = false;

    }
    
//     Use of Thread and Eventlistener 

    public static synchronized void notifyFinish() {

        FinishEvent fe = new FinishEvent(Server.class);

        for (FinishEventListener fel : finishListeners) {

            fel.finish(fe);

        }

    }
//Adding the EventListener
    public static synchronized void addFinishListener(FinishEventListener fel) {

        finishListeners.add(fel);

    }

    
    //Removing the EventListener.
    public static synchronized void removeFinishListener(FinishEventListener fel) {

        finishListeners.remove(fel);

    }
}
