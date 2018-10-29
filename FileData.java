package project;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;



//Map of <File Name, File Object>
public class FileData<K, V> {

	protected HashMap<String, FileObject> map = new HashMap<>();


	public FileObject bestMatchedWC(String[] keyWords) {

		//Create an array that holds files with the highest occurrences of each word
		Map<FileObject, Integer> bestMatchedWC = createBestMatchedWordCountMap(keyWords);

		FileObject bestCount = null;

		//Sort the map and return a list
		LinkedList<Entry<FileObject, Integer>> sortedListWC = orderBestMatched(bestMatchedWC);	

		//Find most frequently occurring file from bestMatched
		bestCount = sortedListWC.peekFirst().getKey();

		return bestCount;
	}

	public FileObject bestMatchedS(String[] keyWords) {

		//Create an array that holds files with the highest occurrences of each word
		Map<FileObject, Integer> bestMatchedSentence = createBestMatchedSentenceMap(keyWords);

		FileObject bestSentence = null;

		//Sort the map and return a list
		LinkedList<Entry<FileObject, Integer>> sortedListS = orderBestMatched(bestMatchedSentence);

		//Find best file based on sentence matching
		bestSentence = sortedListS.peekFirst().getKey();

		return bestSentence;

	}

	public LinkedList<Entry<FileObject, Integer>> bestMatchedMultipleWC(String[] keyWords) {

		//Create an array that holds files with the highest occurrences of each word
		Map<FileObject, Integer> bestMatchedWC = createBestMatchedWordCountMap(keyWords);

		//Sort the map and return a list
		LinkedList<Entry<FileObject, Integer>> sortedListWC = orderBestMatched(bestMatchedWC);	

		return sortedListWC;
	}

	public LinkedList<Entry<FileObject, Integer>> bestMatchedMultipleS(String[] keyWords) {

		//Create an array that holds files with the highest occurrences of each word
		Map<FileObject, Integer> bestMatchedSentence = createBestMatchedSentenceMap(keyWords);

		//Sort the map and return a list
		LinkedList<Entry<FileObject, Integer>> sortedListS = orderBestMatched(bestMatchedSentence);

		//Return file based on user preference
		return sortedListS;

	}

	private Map<FileObject, Integer> createBestMatchedSentenceMap(String[] keyWords) {

		//Iterate over all loaded files
		Iterator<FileObject> it = map.values().iterator();

		Map<FileObject,Integer> bestMatched = new HashMap<FileObject,Integer>();

		//While there are more files
		while(it.hasNext()){

			//Get next file and initialize matching count to zero
			FileObject next = it.next();
			int count = 0;

			//Go through all sentences of a file and see if the file sentences contain the sentence fragment
			Iterator<String> it2 = next.getSentences().iterator();

			//While there are more sentences in the file
			while(it2.hasNext()){

				//Get next sentence in file and initialize found to true
				String fileSentence = it2.next();
				boolean allWordsFound = true;

				//If the file sentence contains the search key words
				for(int i = 0; i < keyWords.length; i++){
					//If word is not in sentence
					if (!fileSentence.toLowerCase().contains(keyWords[i])){
						allWordsFound = false;
						break;
					}
				}
				//If all the keyWords were in the sentence, increment the count
				if(allWordsFound)
					count++;

			}//End of While Looping through Sentences

			//If one or more sentences contained the keywords, add it to map
			if(count > 0){
				bestMatched.put(next, count);
			}

		}//End of While Looping through Files

		return bestMatched;
	}

	private Map<FileObject, Integer> createBestMatchedWordCountMap(String[] keyWords) {

		//Create Map
		Map<FileObject, Integer> freqCounter = new HashMap<FileObject, Integer>();
		
		//Loop through all the KeyWords
		for(int i = 0; i < keyWords.length; i++){

			//Iterate over all loaded files
			Iterator<FileObject> it = map.values().iterator();

			FileObject bestMatchedFile = it.next();

			//While there are more files
			while(it.hasNext()){
				FileObject next = it.next();
				//If the next file's WordCount is higher, set it as bestMatchedFile
				if( bestMatchedFile.getCount(keyWords[i]) < next.getCount(keyWords[i]))
					bestMatchedFile = next;
			}

			//If the count of the best matched file is zero, return null, because no file contains the word
			if(bestMatchedFile.getCount(keyWords[i]) == 0 || bestMatchedFile == null){
				//Do nothing, because no file matches that word
			}	
			else{
				//Find how many times file is already there
				Integer f = freqCounter.get(bestMatchedFile);
				if(f == null){
					freqCounter.put(bestMatchedFile, 1); //If it's not, add it 
				}
				else{
					freqCounter.put(bestMatchedFile, freqCounter.get(bestMatchedFile) + 1); //Inc count
				}
			}

		}//End of for loop

		return freqCounter;
	}

	//ENDED UP NOT BEING USED
	private FileObject findBestMatched(ArrayList<FileObject> bestMatched) {

		Iterator<FileObject> it = bestMatched.iterator();
		int max = 0;
		int numOfMax = 0;
		FileObject result = null;
		FileObject[] maxFiles = new FileObject[bestMatched.size()+1];

		while(it.hasNext()){

			FileObject next = it.next();

			//If the frequency of next file is larger than current max
			if( Collections.frequency(bestMatched, next) > max){
				max = Collections.frequency(bestMatched, next);
				result = next;
				numOfMax = 0; //Reset the number of max values
				maxFiles[numOfMax] = next;
			}	
			//If frequency is the same as max
			if(Collections.frequency(bestMatched, next) == max){
				numOfMax++;
				maxFiles[numOfMax] = next;
			}
		}

		//If there are multiple max files, pick a random one
		if(numOfMax > 0){
			int randomIndex = (int) (Math.random() * (numOfMax + 1));
			result = maxFiles[randomIndex];
		}
		//Return the file object. Could be null as well. 
		return result;
	}

	//ENDED UP NOT BEING USED
	private FileObject findBestMatchedWordCount(Map<FileObject,Integer> bestMatchedMap){

		int max = 0;
		int numOfMax = 0;
		FileObject result = null;
		FileObject[] maxFiles = new FileObject[bestMatchedMap.size()];

		for (Map.Entry<FileObject, Integer> entry : bestMatchedMap.entrySet()){
			//If the frequency of next file is larger than current max
			if(entry.getValue() > max){
				max = entry.getValue();
				result = entry.getKey();
				numOfMax = 0; //Reset the number of max values
				maxFiles[numOfMax] = entry.getKey();
			}	
			//If frequency is the same as max
			if(entry.getValue() == max){
				numOfMax++;
				maxFiles[numOfMax] = entry.getKey();
			}
		}

		//If there are multiple max files, pick a random one
		if(numOfMax > 0){
			int randomIndex = (int) (Math.random() * (numOfMax + 1));
			result = maxFiles[randomIndex];
		}

		//Return the file object. Could be null as well. 
		return result;
	}

	private <K, V extends Comparable<? super V>> LinkedList<Entry<FileObject, Integer>> orderBestMatched(Map<FileObject, Integer> bestMatchedWC) {

		//Convert entry set into a list
		LinkedList<Entry<FileObject, Integer>> list =  new LinkedList<>(bestMatchedWC.entrySet());

		//Sort the list based on created comparator, which sorts them by value (number of times file bestMatched KeyWord)
		Collections.sort( list, new Comparator<Entry<FileObject, Integer>>()
		{
			@Override
			public int compare( Entry<FileObject, Integer> f1, Entry<FileObject, Integer> f2 )
			{
				return ( f2.getValue() ).compareTo( f1.getValue() );
			}
		} );

		//Return the sorted list
		return list;
	}
	
}
