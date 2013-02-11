package core;

import java.util.Random;

public class Game {
	int goal;
	int lastGuessed;
	int attempts;
	int id;
	
	String guessResponse;
	
	public Game() {
		Random generator = new Random();
		
		id = generator.nextInt(Integer.MAX_VALUE);
		goal = generator.nextInt(100);
		lastGuessed = -1;
		attempts = 0;
		guessResponse = "";
	}
	
	public int guess(int guess) {
		lastGuessed = guess;
		attempts++;
		
		if(guess < goal) {
			guessResponse = "Nope, guess higher";
			return -1;
		}
		else if(guess > goal) {
			guessResponse = "Nope, guess lower";
			return 1;
		}
		
		guessResponse = "Thats right! The number was: " + goal;
		return 0;
	}
	
	public int getId() {
		return id;
	}
	
	public String getCurrentGuess() {
		if(attempts == 0) {
			return "";
		}
		
		return "Your guess: " + lastGuessed;
	}
	
	public String getGuessResponse() {
		return guessResponse;
	}
	
	public String getNumberOfGuesses() {
		if(attempts == 0) {
			return "";
		}
		
		return "Number of guesses: " + attempts;
	}
}
