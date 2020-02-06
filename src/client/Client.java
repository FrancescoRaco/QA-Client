package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Client
 * @author Francesco Raco
 *
 */
public class Client
{
	/**
	 * Client mark: every client error message should start with it (so that users can distinguish client and server messages)
	 */
	public static final String MARK = "clientMark";
	
	/**
	 * Server host name
	 */
	protected String serverHostName;
	
	/**
	 * TCP connection port
	 */
	protected int port;
	
	/**
	 * Instantiate Client by server host name string and integer value of TCP
	 * connection port
	 * @param serverHostName Server host name
	 * @param port TCP connection port
	 */
	public Client(String serverHostName, int port)
	{
		//Assign server host name string and integer value of TCP connection port
		//to corresponding fields
		this.serverHostName = serverHostName;
		this.port = port;
	}

	/**
	 * Close all resources
	 * @param echoSocket Socket
	 * @param out PrintWriter instantiated by socket output stream (it writes data to server)
	 * @param in BufferedReader which reads data from input stream (equivalent to server output stream)
	 */
	protected void closeResources(Socket echoSocket, PrintWriter out, BufferedReader in)
	{
		try {
				if (out != null) out.close();
				if (in != null) in.close();
				if (echoSocket != null) echoSocket.close();
			}
			catch (IOException e) {}
	}
	
	/**
	 * Get server output by string query and chosen database index of the search
	 * @param index String query
	 * @param index Database index of the search
	 * @return Server output
	 */
	public String getServerOutput(String query, DatabaseChoice index)
	{
		//Try to execute and handle eventual exceptions
		try
		{
			//Declare a socket and its input reader and output print writer
			Socket echoSocket = null;
			PrintWriter out = null;
			BufferedReader in = null;

			//Create socket by server host name and connection TCP port
			echoSocket = new Socket(serverHostName, port);
				
			//Create print writer and buffered reader by socket output and input stream
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			
			//Send query to server depending on specific database index
			switch(index)
			{
				case FULLSCAN: out.println("index"); break;
				case QUICKSCAN: out.println("index2"); break;
			}
			
			//Send query to server
			out.println(query);
			
			//Tell the server that there are no other queries 
			out.println("END");
			
			//Create and initialize stringbuilder
			StringBuilder response = new StringBuilder();
			
			//While buffered reader reads a non null line and text received from the server is not "END"
			String responseLine;
			while ((responseLine = in.readLine()) != null && !responseLine.equals("END"))
			{
				//Append the line (without initial and ending white spaces) to the response string builder
				response.append(responseLine.trim()).append("\n");
			}
			
			//Get string object by String Builder response
			String responseString = response.toString();
			
			//If server answer is not empty
			if (!responseString.isEmpty())
			{
				//Thanks the server
				out.println("Thanks, I received your answer!");
				
				//Close all resources
				closeResources(echoSocket, out, in);
				
				//Return the server answer
				return responseString;
			}
			
			//Close all resources
			closeResources(echoSocket, out, in);
			
			//If server answer is empty or null,
			//explain the problem to server 
			out.println("I am sorry, I did not receive your request!");
		}
		catch (UnknownHostException e)
		{
			return MARK + "Not detected server: " + serverHostName + '!';
		}
		catch (IOException e)
		{
			return MARK + "Impossible to receive answer from server!";
		}
		catch (Exception e)
		{
			return MARK + "Impossible to establish a connection!";
		}
				
		//Server has not answered
		return MARK + "Unreliable server: it did not reply!";
	}
	
	/**
	 * Get server output with full scan as default choice
	 * @param query
	 * @return
	 */
	public String getServerOutput(String query)
	{
		return getServerOutput(query, DatabaseChoice.FULLSCAN);
	}
}