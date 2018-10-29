package project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Scanner;

/*Main driver to run file search. There are no checks for incorrect input, so errors will appear if user enters incorrect info.
 * Files are set up to be loaded by pathname and are split only by spaces. 
 */


//Testing file pathnames to copy from

// /Users/MaddyVan/Desktop/TermProjectFiles/TheThreeMusketeers.txt /Users/MaddyVan/Desktop/TermProjectFiles/Caesar.txt /Users/MaddyVan/Desktop/TermProjectFiles/Iliad.txt /Users/MaddyVan/Desktop/TermProjectFiles/LongText.txt /Users/MaddyVan/Desktop/TermProjectFiles/TheTimeMachine.txt
// /Users/MaddyVan/Desktop/TermProjectFiles/WikiSearch.txt /Users/MaddyVan/Desktop/TermProjectFiles/SkyrimWiki.txt
public class driverProgram {

	public static FileData<String, FileObject> fileDatabase;

	public static void main(String[] args) throws IOException {

		//Initialize Database and boolean variables
		fileDatabase = new FileData<String, FileObject>();
		boolean exit = false;

		//Create Scanner for user input
		Scanner input = new Scanner(System.in);
		Scanner read = new Scanner(System.in);

		//Loop to get user input while exit boolean is false
		do {
			System.out.println("Enter: \n"
					+ "1 to load files\n"
					+ "2 to do a single file search with matching word count preference\n"
					+ "3 to do a multiple file search with matching word count preference\n"
					+ "4 to do a single file search with matching sentence preference\n"
					+ "5 to do a multiple file search with matching sentence preference\n"
					+ "6 to exit");

			//Get User Decision
			int decision = input.nextInt();

			//Initialize variables 
			String[] keyWords;
			FileObject bestFile;
			String userInput;
			LinkedList<Entry<FileObject, Integer>> sortedList;
			Iterator<Entry<FileObject, Integer>> it;
			long startTime, endTime,duration;
			
			//Switch statement based on user input
			switch(decision){

			//LOADING FILES
			case 1: 
				//Get Input for files to load
				System.out.println("Specify files to load or enter 'exit' to quit: ");
				System.out.print("Load: ");

				String[] files = read.nextLine().split(" ");
				if(files[0] == "exit"){
					exit = true;
					break;
				}
				//startTime = System.nanoTime();
				//Load the files
				loadFiles(files);

				//endTime = System.nanoTime();

				//duration = (endTime - startTime)/1000000; //divide by 1000000 to get milliseconds
				//System.out.println("Execution took: " + duration + " milliseconds");
				break;

				//SINGLE FILE SEARCH (Word Count Preference)
			case 2: 
				//Get input for search
				System.out.println("Enter search or enter 'exit' to quit: ");
				userInput = read.nextLine();
				if(userInput == "exit")
					exit = true;

				keyWords = userInput.split(" ");

				//Find the file best matching the keyWords
				
				//startTime = System.nanoTime();
				
				bestFile = fileDatabase.bestMatchedWC(keyWords);
				
				//endTime = System.nanoTime();

				//duration = (endTime - startTime); //divide by 1000000 to get milliseconds
				//System.out.println("Execution took: " + duration + " milliseconds");

				if(bestFile == null)
					System.out.println("No File Contains Those Keywords");
				else
					System.out.println("The file best matching your search is: " + bestFile.getFileName());
				break;

				//MULTIPLE FILE SEARCH (Word Count Preference)
			case 3: 
				//Get input for search
				System.out.println("Enter search or enter 'exit' to quit: ");
				userInput = read.nextLine();
				if(userInput == "exit")
					exit = true;

				keyWords = userInput.split(" ");

				//Find the file best matching the keyWords
				//startTime = System.nanoTime();
				sortedList = fileDatabase.bestMatchedMultipleWC(keyWords);
				
				//endTime = System.nanoTime();

				//duration = (endTime - startTime); //divide by 1000000 to get milliseconds
				//System.out.println("Execution took: " + duration + " milliseconds");

				System.out.println("Files matching your search by word count (in order) are: \n");
				it = sortedList.iterator();
				while(it.hasNext())
					System.out.println(it.next().getKey().getFileName() + "\n");

				//System.out.println(sortedList.toString());

				break;

				//SINGLE FILE SEARCH (Sentence Preference)
			case 4: 
				//Get input for search
				System.out.println("Enter search or enter 'exit' to quit: ");
				userInput = read.nextLine();
				if(userInput == "exit")
					exit = true;

				keyWords = userInput.split(" ");

				//Find the file best matching the keyWords
				//startTime = System.nanoTime();
				bestFile = fileDatabase.bestMatchedS(keyWords);
				
				
				//endTime = System.nanoTime();

				//duration = (endTime - startTime)/1000000; //divide by 1000000 to get milliseconds
				//System.out.println("Execution took: " + duration + " milliseconds");

				if(bestFile == null)
					System.out.println("No File Contains Those Keywords");
				else
					System.out.println("The file best matching your search is: " + bestFile.getFileName());
				break;

				//MULTIPLE FILE SEARCH (Sentence Preference)
			case 5: 
				//Get input for search
				System.out.println("Enter search or enter 'exit' to quit: ");
				userInput = read.nextLine();
				if(userInput == "exit")
					exit = true;

				keyWords = userInput.split(" ");

				//Find the file best matching the keyWords
				//startTime = System.nanoTime();
				sortedList = fileDatabase.bestMatchedMultipleS(keyWords);
				
				//endTime = System.nanoTime();

				//duration = (endTime - startTime)/1000000; //divide by 1000000 to get milliseconds
				//System.out.println("Execution took: " + duration + " milliseconds");

				System.out.println("Files matching your search by sentence (in order) are: \n");
				it = sortedList.iterator();
				while(it.hasNext())
					System.out.println(it.next().getKey().getFileName() + "\n");
				break;

			case 6:
				exit = true;
				break;
			}

		} while(!exit);

		System.exit(0);
	}

	private static void loadFiles(String[] files) throws IOException {
		//Read information from each of the files
		for(String file : files){

			//Create reader for the file
			BufferedReader fileReader = new BufferedReader(new FileReader(file));

			//Create HashMap to store words and counts
			HashMap<String,WordCount> fileWordCounts = new HashMap<String,WordCount>();
			ArrayList<String> sentences = new ArrayList<String>();

			//While the file still has more lines to be read from
			while(fileReader.ready()){

				//Read a line from the file
				String fileLine = fileReader.readLine();
				//Add the line to file sentences
				sentences.add(fileLine);
				//Split the file and remove punctuation from words
				String[] fileWords = fileLine.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");

				//iterate through all words in each line
				for (String word : fileWords)
				{
					WordCount count = fileWordCounts.get(word);
					//If word is not in file, add it 
					if (count == null) {
						fileWordCounts.put(word,new WordCount(word));
					} else {
						count.inc(); //If not, increment it
					}
				}   
			}

			//Add file to fileDatabase 
			fileDatabase.map.put(file, new FileObject(file, fileWordCounts, sentences));

			fileReader.close(); // Close File

		}//End of For Loop reading from all files
	}

}
