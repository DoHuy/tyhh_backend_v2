package com.stadio.task.baker.users;

import com.stadio.task.utils.AppConst;
import com.stadio.task.utils.VietlangUtils;
import com.stadio.common.files.TomcatFileManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class UserNameGenerator 
{
	private static TomcatFileManager fm = new TomcatFileManager();

	public static Set<String> loadVietNameFamily() 
	throws Exception
	{
		File f = fm.getResourceFile(AppConst.PATH_NAME, "vietlang-name-family.txt");
		Set<String> lines = new HashSet<>(FileUtils.readLines(f, "UTF-8"));
		return lines.stream().filter(x -> !x.isEmpty()).collect(Collectors.toSet());
	}

	public static Set<String> loadVietNameMaleGiven() 
	throws Exception
	{
		File f = fm.getResourceFile(AppConst.PATH_NAME, "vietlang-name-male-given.txt");
		Set<String> lines = new HashSet<>(FileUtils.readLines(f, "UTF-8"));
		return lines.stream().filter(x -> !x.isEmpty()).collect(Collectors.toSet());
	}
	
	public static Set<String> loadVietNameMaleMiddle() 
	throws Exception
	{
		File f = fm.getResourceFile(AppConst.PATH_NAME, "vietlang-name-male-middle.txt");
		Set<String> lines = new HashSet<>(FileUtils.readLines(f, "UTF-8"));
		return lines.stream().filter(x -> !x.isEmpty()).collect(Collectors.toSet());
	}

	public static Set<String> loadVietNameFemaleGiven() 
	throws Exception
	{
		File f = fm.getResourceFile(AppConst.PATH_NAME, "vietlang-name-female-given.txt");
		Set<String> lines = new HashSet<>(FileUtils.readLines(f, "UTF-8"));
		return lines.stream().filter(x -> !x.isEmpty()).collect(Collectors.toSet());
	}
	
	public static Set<String> loadVietNameFemaleMiddle() 
	throws Exception
	{
		File f = fm.getResourceFile(AppConst.PATH_NAME, "vietlang-name-female-middle.txt");
		Set<String> lines = new HashSet<>(FileUtils.readLines(f, "UTF-8"));
		return lines.stream().filter(x -> !x.isEmpty()).collect(Collectors.toSet());
	}
	
	public static List<String>  nextName(Set<String> f, Set<String> m, Set<String> g) 
	{
		List<String> res = new ArrayList<String>();
		
		Set<String> used = new TreeSet<String>();
		
		String f1 = takeOne(f, used);
		res.add(f1);
		
		if(Math.random() > 0.5)
		{
			String f2 = takeOne(f, used);		
			res.add(f2);			
		}
			
		String mk = takeOne(m, used);		
		res.add(mk);			
		
		String gk = takeOne(m, used);		
		res.add(gk);			
		
		return res;
	}

	public static String takeOne(Set<String> f, Set<String> used) 
	{
		while(true)
		{
			String fk = takeOne(f);
			if(!used.contains(fk)) { used.add(fk); return fk; }
		}
		
	}

	private static String takeOne(Set<String> f)
	{
		int k = (int)Math.floor(f.size() * Math.random());
		
		for(String fk: f) 
		{
			if(k-- == 0) return fk;			
		}
		
		return null;
	}

	public static String emailFromName(List<String> x) 
	{
		String res = "";
		
		for(String xk: x)
		{
			xk = VietlangUtils.lookup(xk).toLowerCase();
			int k = (int)Math.floor(32*Math.random());
			
			res += xk + k;
		}
		
		return res + "@gmail.com";
	}

}
