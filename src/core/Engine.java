package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Engine {
	private GameManager games;
	Pattern cookieIdPattern;

	public Engine() throws IOException {
		games = new GameManager();
		cookieIdPattern = Pattern.compile("gameId=\\d+");
		
		run();
	}
	
	public void run() throws IOException {
		System.out.println("Skapar Serversocket");
		ServerSocket serverSocket = new ServerSocket(8888);

		while (true) {
			System.out.println("Väntar på klient...");
			Socket clientSocket = serverSocket.accept();
			System.out.println("Klient är ansluten");

			BufferedReader request = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			String str = request.readLine();
			System.out.println(str);
			/*StringTokenizer tokens = new StringTokenizer(str, " ?");
			tokens.nextToken(); // Ordet GET
			String requestedDocument = tokens.nextToken();*/
			
			while ((str = request.readLine()) != null && str.length() > 0) {
				System.out.println(str);
			
				if(str.matches("^Cookie:.*gameId=\\d+")) {
					Matcher m = cookieIdPattern.matcher(str);
					
					if (m.find()) {
					    int id = Integer.parseInt(m.group(0).split("=")[1]);
						System.out.println("id: " + id);
					}
				}
			}
			
			System.out.println("Förfrågan klar.");
			clientSocket.shutdownInput();

			PrintStream response = new PrintStream(clientSocket.getOutputStream());
			response.println("HTTP/1.1 200 OK");
			response.println("Server : Guess game");
			
			/*if (requestedDocument.indexOf(".html") != -1)
				response.println("Content-Type: text/html");
*/
			response.println("Set-Cookie: gameId=113131; expires=Wednesday,31-Dec-13 21:00:00 GMT");

			response.println();
			
			response.println("<html>");
			response.println("<head>");
			response.println("</head>");
			response.println("<body>");
			response.println("</body>");
			response.println("</html>");
			
			clientSocket.shutdownOutput();
			clientSocket.close();
		}
	}
	
	public static void main(String[] args) throws IOException {
		new Engine();
	}
}