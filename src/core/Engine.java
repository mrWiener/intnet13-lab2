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
	Pattern paramsPattern;

	public Engine() throws IOException {
		games = new GameManager();
		cookieIdPattern = Pattern.compile("SESSIONID=\\d+");
		paramsPattern = Pattern.compile("/[?]guess=([\\d]|[\\d]{2})");
		
		run();
	}
	
	public void run() throws IOException {
		System.out.println("Skapar Serversocket");
		ServerSocket serverSocket = new ServerSocket(8888);

		while (true) {
			System.out.println("VŠntar pŒ klient...");
			Socket clientSocket = serverSocket.accept();
			System.out.println("Klient Šr ansluten");

			BufferedReader request = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			String str = request.readLine();
			System.out.println(str);
			
			if(str.contains("favicon")) {
				System.out.println("favicon request ignored.");
				continue;
			}
			
			int id = -1;
			int guess = -1;
			
			Matcher m = paramsPattern.matcher(str);
				
			if(m.find()) {
				guess = Integer.parseInt(m.group(0).split("=")[1]);
			}
			
			while ((str = request.readLine()) != null && str.length() > 0) {
				//System.out.println(str);
			
				if(str.matches("^Cookie:.*SESSIONID=\\d+")) {
					m = cookieIdPattern.matcher(str);
					
					if (m.find()) {
					    id = Integer.parseInt(m.group(0).split("=")[1]);
					}
				}
			}
			
			System.out.println("FšrfrŒgan klar.");
			clientSocket.shutdownInput();
			
			Game g = null;
			
			if(guess > -1) {
				System.out.println("Current guess: " + guess);
			}
			
			if(id > -1 && games.exists(id)) {
		    	g = resumeExistingGame(id);
		    }
			else {
				g = createNewGame();
			}

			PrintStream response = new PrintStream(clientSocket.getOutputStream());
			response.println("HTTP/1.1 200 OK");
			response.println("Server : Guess game");
			
			response.println("Set-Cookie: SESSIONID=" + g.getId() + "; expires=Wednesday,31-Dec-13 21:00:00 GMT");

			response.println();
			
			response.println("<html>");
			response.println("<head>");
			response.println("<title>Guessing game</title>");
			response.println("</head>");
			response.println("<body>");
			
			response.println("<p>" + g.getCurrentGuess() + "</p>");
			response.println("<p>" + g.getGuessResponse() + "</p>");
			response.println("<p>" + g.getNumberOfGuesses() + "</p>");
			
			response.println("<form method='GET'>");
			response.println("<input type='input' name='guess'/>");
			response.println("<input type='submit' />");
			response.println("</form>");
			
			response.println("</body>");
			response.println("</html>");
			
			clientSocket.shutdownOutput();
			clientSocket.close();
		}
	}
	
	public Game resumeExistingGame(int id) {
		Game g = games.getById(id);
		
		System.out.println("Existing game with id " + g.getId() + " resumed.");
		
		return g;
	}
	
	public Game createNewGame() {
		Game g = new Game();
		
		while(games.exists(g.getId())) {
			g = new Game();
		}
		
		games.add(g);
		
		System.out.println("New game created with id " + g.getId());
		
		return g;
	}
	
	public static void main(String[] args) throws IOException {
		new Engine();
	}
}