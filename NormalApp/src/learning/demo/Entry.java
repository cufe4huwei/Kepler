package learning.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Entry {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println(String.format("Hello%12s", "s"));
		
		System.out.println((int)' ');
		System.out.println((int)'A');
		System.out.println((int)'a');
		System.out.println((char)Utils.transmogrify('A'));
		System.out.println((char)Utils.transmogrify('b'));
		
		System.out.println(Integer.toBinaryString(' '));
		System.out.println(Integer.toBinaryString('A'));
		System.out.println(Integer.toBinaryString('a'));		
		
		String empty = new String("");
		System.out.println(empty.getBytes().length);
		
		String[] input = {"cars", "thing", "scar", "dog", "god", "arcs", "the"};
		String[][] output = groupAnagram(input);
		
		//output for log and debug
		for(int i = 0; i < output.length; i++){
			for(int j = 0; j < output[i].length; j++){
				System.out.print(" " + output[i][j]);
			}
			System.out.println();
		}
	}
	
	private static String[][] groupAnagram(String[] input){
		String[][] result = null;
		/*
		 * put string into the same ArrayList which belongs to one anagram;
		 * generate a map whose key is the anagram code while value is a ArrayList which contains the strings belong to one anagram 
		 */
		HashMap<String, ArrayList<String>> anagramMap = new HashMap<String, ArrayList<String>>();
		for(String item : input){
			if(item != null && !item.isEmpty()){
				String key = anagramCode(item);
				if(anagramMap.containsKey(key)){
					anagramMap.get(key).add(item);
				} else {
					ArrayList<String> group = new ArrayList<String>();
					group.add(item);
					anagramMap.put(key, group);
				}
			}
		}
		//generate final result from the temporary ArrayList which contains the strings which belongs to one anagram
		Collection<ArrayList<String>> anagrams = anagramMap.values();
		int anagramSize = anagrams.size();
		result = new String[anagramSize][];
		Iterator<ArrayList<String>> it = anagrams.iterator();
		int index = 0;
		while(it.hasNext()){
			ArrayList<String> item = it.next();
			result[index] = item.toArray(new String[item.size()]);
			index++;
		}
		return result;
	}
	
	/*
	 * return the same code for string which has the same letters
	 */
	private static String anagramCode(String src){
		if(src == null || src.isEmpty()){
			return "";
		}
		char[] chars = src.toCharArray();
		Arrays.sort(chars);
		StringBuilder sb = new StringBuilder();
		sb.append(chars);
		String code = sb.toString();
		return code;
	}

}
