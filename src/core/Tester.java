package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

public class Tester {
	int sumGuesses;
	static final int START_COUNTS = 100;
	int counts = START_COUNTS;
	
	public Tester() throws MalformedURLException, IOException {
		sumGuesses = 0;
		
		run();
	}
	
	public void run() throws MalformedURLException, IOException {
		while((counts--) > 0) {
			
			List<String> cookies = new LinkedList<String>();
			int low = 0;
			int high = 100;
			int currGuess = 50;
			int guesses = 0;
			boolean done = false;
			
			while(!done) {
				guesses++;
				URLConnection connection = new URL("http://localhost:8888/?guess=" + currGuess).openConnection();
				for (String cookie : cookies) {
				    connection.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
				}
				InputStream responseStream = connection.getInputStream();
				cookies = connection.getHeaderFields().get("Set-Cookie");
				BufferedReader response = new BufferedReader(new InputStreamReader(responseStream, "UTF-8"));
				
				String str = "";
				while((str = response.readLine()) != null && str.length() > 0) {
					if(str.contains("Thats right! The number was:")) {
						//success
						System.out.println("The guess: " + currGuess + " was right!");
						done = true;
						break;
					}
					else if(str.contains("Nope, guess lower")) {
						//guess lower
						high = currGuess - 1;
						System.out.println("guess lower");
					}
					else if(str.contains("Nope, guess higher")) {
						//guess higher
						low = currGuess + 1;
						System.out.println("guess higher");
					}
				}
				
				if(done) {
					continue;
				}
				
				currGuess = low + (high - low)/2;
				System.out.println("next guess: " + currGuess);
			}
			
			sumGuesses += guesses;
		}
		
		System.out.println("Sum guesses: " + sumGuesses);
		System.out.println("Num games: " + START_COUNTS);
		System.out.println("Mean number of guesses: " + ((double)sumGuesses/(double)START_COUNTS));
	}
	
	public static void main(String[] args) throws IOException {
		new Tester();
	}
}
