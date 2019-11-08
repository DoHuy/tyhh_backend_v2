package com.stadio.task.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class VietlangUtils 
{
	protected static UnicodeVocab vocab = UnicodeVocab.fromFile(); 

	public static String lookup(String str) 
	{
		return vocab.lookup(str);
	}
	
	
	public static File getTruyenKieu()
	{
		URL u = VietlangUtils.class.getResource("Truyen-Kieu.txt");
		System.out.println(u);
		return new File(u.getFile());
	}
	
	public static List<String> getTruyenKieuSentences()
	{
		List<String> res = new ArrayList<>();
		
		File f = getTruyenKieu();

		BufferedReader rd = null;

		try
		{
			rd = new BufferedReader(new FileReader(f));

			while (true)
			{
				String s = rd.readLine();
				if (s == null) break;
				res.add(s);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				rd.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return res;
	}

	public static String detone(String s, int ch) 
	{
		String res = "";
		for(int k=0; k<s.length(); k++)
		{
			int ck = s.charAt(k);
			res += (char)(ck < 128 ? ck : ch);
		}
		
		return res;
	}
	
	public static String detone(String s, Map<Character, Character> map) 
	{
		String res = "";
		for(int k=0; k<s.length(); k++)
		{
			Character sk = s.charAt(k);
			Character tk = map.get(sk);
			res += (tk==null ? sk : tk);
		}
		
		return res;
	}	

	public static void addEntry(Map<Character, Character> map, String sk, char tk) 
	{
		map.put(sk.charAt(0), tk);
	}


	public static String removePunctuators(String s) 
	{
		return s.replaceAll("[,.!?:;'\"��]+", "");
	}


	public static double compareCharSet(String a, String b) 
	{
		Set<Character> sa = toCharSet(a);
		Set<Character> sb = toCharSet(b);
		return jaccardScore(sa, sb);
	}



	public static<T1> double jaccardScore(Set<T1> s1, Set<T1> s2) 
	{
		int c = 0;
		for(T1 x: s1) if(s2.contains(x)) c++;
		
		int s = s1.size() + s2.size() - c;
		
		return c / (double)(s==0 ? 1 : s);
	}



	private static Set<Character> toCharSet(String a) 
	{
		Set<Character> res = new TreeSet<Character>(); 
		for(int k=0; k<a.length(); k++) res.add(a.charAt(k));
		return res;
	}



}
