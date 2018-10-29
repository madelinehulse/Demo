package project;

public class WordCount implements Comparable<WordCount>{
	
	//Class containing a word and integer holding the count of how many times the word appeared
	
	private String word; //Name of Word
	private int cnt; //Count of word
	
	//Constructors
	public WordCount(String word) {
		this.word = word;
		this.cnt = 1;
	}
	
	public WordCount(String word, int count) {
		this.word = word;
		this.cnt = count;
	}
	
	//Getters and setters
	public String getWord() {
		return word;
	}
	public int getCnt() {
		return this.cnt;
	}
	
	public void inc() {
		cnt++;
	}
	@Override
	public int compareTo(WordCount another) {
		return this.word.compareToIgnoreCase(another.word);
	}
	@Override
	public String toString() {
		return "word=" + word + ", cnt=" + cnt;
	}

	
}
