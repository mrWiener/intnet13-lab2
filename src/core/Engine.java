package core;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class Engine {

	public static void main(String[] args) throws IOException {
		System.out.println("Skapar Serversocket");
		ServerSocket serverSocket = new ServerSocket(8888);

		while (true) {
			System.out.println("Väntar på klient...");
			Socket clientSocket = serverSocket.accept();
			System.out.println("Klient är ansluten");

			BufferedReader request = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			String str = request.readLine();
			System.out.println(str);
			StringTokenizer tokens = new StringTokenizer(str, " ?");
			tokens.nextToken(); // Ordet GET
			String requestedDocument = tokens.nextToken();
			
			while ((str = request.readLine()) != null && str.length() > 0) {
				System.out.println(str);
			}
			
			System.out.println("Förfrågan klar.");
			clientSocket.shutdownInput();

			PrintStream response = new PrintStream(clientSocket.getOutputStream());
			response.println("HTTP/1.1 200 OK");
			response.println("Server : Guess game");
			
			if (requestedDocument.indexOf(".html") != -1)
				response.println("Content-Type: text/html");
			if (requestedDocument.indexOf(".gif") != -1)
				response.println("Content-Type: image/gif");

			response.println("Set-Cookie: clientId=1; expires=Wednesday,31-Dec-13 21:00:00 GMT");

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
}