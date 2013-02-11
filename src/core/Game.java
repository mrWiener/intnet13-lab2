package core;

import java.util.Random;

public class Game {
	int goal;
	int lastGuessed;
	int attempts;
	int id;
	
	public Game() {
		Random generator = new Random();
		
		id = generator.nextInt();
		goal = generator.nextInt(100);
		lastGuessed = -1;
		attempts = 0;
	}
	
	public int guess(int guess) {
		lastGuessed = guess;
		attempts++;
		
		if(guess < goal) {
			return -1;
		}
		else if(guess > goal) {
			return 1;
		}
		
		return 0;
	}
	
	public int getId() {
		return id;
	}
}
