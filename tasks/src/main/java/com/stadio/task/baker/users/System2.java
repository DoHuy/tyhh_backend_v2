package com.stadio.task.baker.users;

import com.stadio.common.typed.TypedAction;
import com.stadio.common.typed.TypedMapper;

import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;


public class System2 
{
	public static final long ONE_MONTH = 30*24*3600*1000;
	public static final long ONE_WEEK = 5*24*3600*1000;

	public static void size(Collection<?> items)
	{
		System.out.println(items.size());
	}

	public static void size(String pref, Collection<?> items)
	{
		System.out.println(pref + items.size());
	}

	public static void theEnd() 
	{
		System.out.println("-----------THE END-----------");		
	}
	

	public static void hr() 
	{
		System.out.println("----------------------");			
	}		

	public static<T1> T1 pickOne(List<T1> items) 
	{
		int k = (int)Math.floor(items.size() * Math.random());
		return items.get(k);
	}
	
	public static<T1> T1 pickOne(T1[] items) 
	{
		int k = (int)Math.floor(items.length * Math.random());
		return items[k];
	}
	
	public static<T1> T1 pickOne(Set<T1> items) 
	{
		int k = (int)Math.floor(items.size() * Math.random());
		
		for(T1 ik: items) 
		{
			if(k == 0) return ik;
			k--;
		}
		
		return null;
	}

	public static void pf(Object... args)
	{
		for(Object ak: args) System.out.print(ak + " ");
		System.out.println();
	}
	
	public static void pt(Object... args)
	{
		for(Object ak: args) System.out.print(ak + "\t");
		System.out.println();
	}
	
	public static void p(Object... args)
	{
		for(Object ak: args) System.out.print(ak);
		System.out.println();
	}
	
	public static void catf(Object... args)
	{
		for(Object ak: args) System.out.print(ak + " ");
		System.out.println();
	}

	public static void catt(Object... args)
	{
		for(Object ak: args) System.out.print(ak + "\t");
		System.out.println();
	}
	
	public static void cat(Object... args)
	{
		for(Object ak: args) System.out.print(ak);
		System.out.println();
	}
	
	public static<T1> Object notNull(T1 tj, TypedMapper<T1, Object> lf, String dv)
	{
		if(tj == null) return dv;
		Object v = lf.invokeMapperAction(tj);
		return v==null ? dv : v;
	}

	public static void consume(Object... args)
	{
		
	}

	public static<T1> T1 newInstance(Class<T1> cl, TypedAction<T1> lf)
	throws Exception
	{
		T1 obj = cl.newInstance();
		lf.invokeAction(obj);
		return obj;
	}

	public static void showTextFile(File f, TypedAction<PrintWriter> lf) 
	throws Exception
	{
		PrintWriter out = new PrintWriter(f);
		lf.invokeAction(out);
		out.close();
		Desktop.getDesktop().open(f);
	}	
	
	public static void showFigure(File f, int W, int H, TypedAction<Graphics> lf) 
	throws Exception
	{
		BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.createGraphics();
		lf.invokeAction(g);
		g.dispose();
		
		ImageIO.write(img, "png", f);
		Desktop.getDesktop().open(f);
	}

	public static File getResourceFile(Class<?> cl, String fname) 
	{
		URL u = cl.getResource(fname);
		if(u==null) return null;
		return new File(u.getFile());
	}

	public static double[] doubleArray(double... args)
	{
		return args;
	}

	public static int[] intArray(int... args)
	{
		return args;
	}
	
	public static String[] stringArray(String... args)
	{
		return args;
	}

	public static<K, V> void printMapInLines(Map<K, V> c) 
	{
		for(K ck: c.keySet()) System.out.println(ck + " -> " + c.get(ck));
	}
	
}
