package project;

import java.util.ArrayList;
import java.util.HashMap;

public class FileObject implements Comparable<FileObject>{

	private String fileName; //Name of file
	private HashMap<String, WordCount> wordCounts; //WordCounts of file
	private ArrayList<String> sentences; //Sentences in the file
	
	//Constructors
	public FileObject(String fileName, HashMap<String, WordCount> fileWordCounts, ArrayList<String> sentences) {
		this.fileName = fileName;
		this.wordCounts = fileWordCounts;
		this.sentences = sentences;
	}
	
	public FileObject(String name) {
		super();
		this.fileName = name;
	}

	public String getFileName() {
		return fileName;
	}
	
	@Override
	public int compareTo(FileObject another) {
		return this.fileName.compareToIgnoreCase(another.fileName);
	}
	
	@Override
	public String toString() {
		return "File Name: " + fileName + wordCounts.toString();
	}

	//Returns the count of given word in the file
	public int getCount(String word){
	
	WordCount wordCnt =  wordCounts.get(word);
	if(wordCnt == null)
		return 0;
	else
		return wordCnt.getCnt();
	}
	
	public ArrayList<String> getSentences(){
		return this.sentences;
	}
	
}
