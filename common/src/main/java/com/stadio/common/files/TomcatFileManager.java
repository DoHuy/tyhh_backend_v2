package com.stadio.common.files;

import java.awt.Desktop;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.servlet.http.HttpServletRequest;
import com.stadio.common.typed.TypedAction;
import org.apache.commons.io.FileUtils;
import org.jcodec.common.IOUtils;
import org.springframework.core.io.ClassPathResource;


public class TomcatFileManager 
{
	protected SimpleDateFormat fmt_log = new SimpleDateFormat("yyMMddHH");
	
	public File getDesktopFile(String url) 
	{
		return new File( System.getProperty("user.home") + "/Desktop/" + url );
	}
	
	public File getDropboxFile(String url) 
	{
		return new File( System.getProperty("user.home") + "/Dropbox/" + url );
	}
	
	public File getLoggingFolder(HttpServletRequest request, String root) 
	{
		String url = request.getServletContext().getRealPath("/");
		
		return new File(url + "/data-" + root + "/" + fmt_log.format(System.currentTimeMillis()));
	}	
	

	public void copyBytes(File srcf, OutputStream tar)
	throws Exception
	{
		FileInputStream src = new FileInputStream(srcf);
		IOUtils.copy(src, tar);
		src.close();
	}
	
	

	public File getWebContentFolder(HttpServletRequest request) 
	{
		String url = request.getServletContext().getRealPath("/");
		return new File(url);
	}

	public File getWebContentFile(HttpServletRequest request, String fname) 
	{
		String url = request.getServletContext().getRealPath("/") + fname;
		return new File(url);
	}	
	
	public URL getResourceUrl(Class<?> cl, String fname) 
	{
		return cl.getResource(fname);
	}	
	
	public File getResourceFile(String path, String fname)
	{
		InputStream inputStream = null;

		try {
			File s = File.createTempFile(fname, ".txt");
			inputStream = new ClassPathResource(path + "/" + fname).getInputStream();
			FileUtils.copyInputStreamToFile(inputStream, s);
			return s;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			IOUtils.closeQuietly(inputStream);
		}

		return null;

	}

	public Path getSystemResource(String path, String fname)
	throws URISyntaxException
	{
		return Paths.get(ClassLoader.getSystemResource(path + "/" + fname).toURI());
	}

	public void showFile(File f) 
	throws Exception
	{
		Desktop.getDesktop().open(f);
	}
	
	public void showTextFile(File f, TypedAction<PrintWriter> lf)
	throws Exception
	{
		PrintWriter out = new PrintWriter(f);
		lf.invokeAction(out);
		out.close();
		Desktop.getDesktop().open(f);
	}


	public List<File> readInnerFiles(File f0) 
	{
		List<File> res = new ArrayList<File> ();
		
		Stack<File> todo = new Stack<File>();
		todo.add(f0);
		
		while(!todo.isEmpty())
		{
			File fk = todo.pop();
			
			File[] files = fk.listFiles();
			if(files == null) continue;
			
			for(File fj: files)
			if( fj.isFile() ) res.add(fj);
			else if( fj.isDirectory() ) todo.add(fj);
		}
		
		return res;
	}
	


	


	
}
