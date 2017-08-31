import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Movie {
	static final HashMap<Integer, String>movieMap = new HashMap<>();
	static final HashMap<Integer, HashMap<String, Double>> userMap = new HashMap<>();
	static final HashMap<String, Double>userList = new HashMap<>();
	static final HashMap<Integer, Double> result = new HashMap<>();
	static final Set<String> recommendList = new HashSet<String>();
	static void fileRead(){
		try{
		BufferedReader rmovieFile = new BufferedReader(new FileReader("moviesList.txt"));
		String line = null;
		while((line=rmovieFile.readLine()) != null)
		{
			movieMap.put(Integer.parseInt(line.split(":")[0]), line.split(":")[1]);
		}
		rmovieFile.close();	
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		try{
		String line = null;
		BufferedReader ruserFile = new BufferedReader(new FileReader("usersList.txt"));
		while((line=ruserFile.readLine()) != null)
		{
			if(!userMap.containsKey(Integer.parseInt(line.split(":")[0])))
			{
				HashMap<String,Double>ratingMap = new HashMap<String, Double>();
				ratingMap.put(line.split(":")[1], Double.parseDouble(line.split(":")[2]));
				userMap.put(Integer.parseInt(line.split(":")[0]), ratingMap);
			}
			else
			{
				userMap.get(Integer.parseInt(line.split(":")[0])).put(line.split(":")[1], Double.parseDouble(line.split(":")[2]));
			}
		}
		ruserFile.close();	
		} catch(IOException e)
		{
			e.printStackTrace();
		}
				
	}
	
	static void printHashmaps(){
		for(Map.Entry<Integer, String> pair: movieMap.entrySet()){
			System.out.println("Movie Id: "+ pair.getKey()+", Movie name: "+ pair.getValue());
		}
		for(Map.Entry<Integer, HashMap<String, Double>> outer: userMap.entrySet())
		{
			System.out.print("User Id: "+ outer.getKey());
			for(Map.Entry<String, Double> inner: outer.getValue().entrySet())
			{
				System.out.print(", Movie name: "+ inner.getKey()+", Rating: "+ inner.getValue());
			}
			System.out.println();
		}
		
	}
	
	static void userdefinedList(int userid){
		for(Map.Entry<String, Double> result: userMap.get(userid).entrySet())
		{
			userList.put(result.getKey(), result.getValue());
		}
	}
	
	static void eculidean(int userid){
		userdefinedList(userid);
		for(Map.Entry<Integer, HashMap<String, Double>> outer: userMap.entrySet())
		{
			double diff = 0.0;
			if(outer.getKey() != userid)
			{
				//System.out.println(outer.getKey());
				for(Map.Entry<String, Double> inner: outer.getValue().entrySet())
				{
					if(userList.containsKey(inner.getKey()))
					{
						//System.out.println(userList.get(inner.getKey()) +" "+ inner.getValue());
						diff += Math.pow((userList.get(inner.getKey())- inner.getValue()), 2);
						//System.out.println(diff);

					}
				}
				result.put(outer.getKey(), Math.sqrt(diff));
			}
		}
		
		
		Set<Map.Entry<Integer, Double>> set = result.entrySet();
		List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(set);
		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>()
				{
					public int compare(Map.Entry<Integer, Double>O1, Map.Entry<Integer, Double>O2)
					{
						if(O1.getValue() - O2.getValue() < 0)
							return -1;
						return 1;
					}
				});
		int top = 2;
		for(Map.Entry<Integer, Double> l: list)
		{
			if(top > 0)
			{
				top--;
				for(Map.Entry<String, Double> inner: userMap.get(l.getKey()).entrySet())
				{
					if(!userList.containsKey(inner.getKey()))
					{
						recommendList.add(inner.getKey());
					}
				}
			}
		}
		
	}

	public static void main(String[] args) {
		fileRead();
		//printHashmaps();
		System.out.println("Enter userId between 1 - 5:");
		Scanner input = new Scanner(System.in);
		int userid = input.nextInt();
		System.out.println("User entered Id is: "+ userid);
		eculidean(userid);
		for(Map.Entry<Integer, Double> outer: result.entrySet())
		{
			System.out.println(outer.getKey()+" "+ outer.getValue());
		}

		System.out.println("Recommended Moives:");
		for(String s: recommendList)
		{
			System.out.println(s);
		}
	}

}
