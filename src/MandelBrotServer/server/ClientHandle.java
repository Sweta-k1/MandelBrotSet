
package MandelBrotServer.server;

import MandelBrotServer.Events.FinishEvent;
import MandelBrotServer.Events.FinishEventListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Sweta
 */

    
    public class ClientHandle extends Thread implements FinishEventListener{

	private Socket client;

	private BufferedReader reader;

	private PrintWriter writer;

	private boolean isActive;
  
        
        //Handles and communicates with clientby displaying the status everytime the server gets connected.
	public ClientHandle(Socket client){

		this.client = client;

		InputStream in = null;

		OutputStream out = null;

		try {

			out = client.getOutputStream();

			in = client.getInputStream();

		} catch (IOException e) {

			System.out.println("IO Error!!! - ID:"+client.getInetAddress().toString() + 

					" --- Date:"+  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		}

		reader = new BufferedReader(new InputStreamReader(in));

		writer = new PrintWriter(new OutputStreamWriter(out));

		isActive = true;

		Server.addFinishListener(this);

	}
        
        //Checks if theserver is connected and displays the id and portname and also updates the time at the same time.
	public void run(){

		try {

			System.out.println("Client Connect --- ID:"+client.getInetAddress().toString()+

					" --- Date:"+  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())+". Assigning job...");

        	String input, rs[];

			int line;

			int colors[] = new int[Server.width];

			while(isActive){

				line = Server.getNextLine();

				if(line != -1){

					writer.println(line+"#"+Server.width+"#"+Server.height);

					writer.flush();

					input = reader.readLine();

					rs = input.split("#");

					for(int i = 0;i<Server.width;i++){

						colors[i] = Integer.parseInt(rs[i+1]); 

					}

					Server.writeLineToImage(Integer.parseInt(rs[0]), colors);

				}else finish();

			}			reader.close();

			writer.close();

			client.close();

			System.out.println("Client Disconnect --- ID:"+client.getInetAddress().toString()+

					" --- Date:"+  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		} catch (IOException e) {

			System.out.println("Client input error!!! - ID:"+client.getInetAddress().toString() + 

					" --- Date:"+  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		}

	}
        @Override

	public void finish(FinishEvent dce) {

		isActive = false;	

		writer.println("END#"+Server.finishNR);

		Server.finishNR++;

		writer.flush();

	}

	//Checks if the server is active or not.

	public void finish() {

		isActive = false;	

		writer.println("END#"+Server.finishNR);

		Server.finishNR++;

		writer.flush();

	}
    

}