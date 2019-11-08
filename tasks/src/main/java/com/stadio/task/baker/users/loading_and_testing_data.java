package com.stadio.task.baker.users;

import com.stadio.task.AppMainForTasks;
import com.stadio.task.utils.AppConst;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;


public class loading_and_testing_data extends AppMainForTasks
{
	public static void main(String[] args)
	throws Exception
	{
		{
			File f = useFM().getResourceFile(AppConst.PATH_NAME, "vietlang-name-family.txt");
			readAndPrint(f);			
		}
		
		{
			File f = useFM().getResourceFile(AppConst.PATH_NAME, "vietlang-name-male-middle.txt");
			readAndPrint(f);					
		}
	
		{
			File f = useFM().getResourceFile(AppConst.PATH_NAME, "vietlang-name-male-given.txt");
			readAndPrint(f);					
		}
		
		{
			File f = useFM().getResourceFile(AppConst.PATH_NAME, "vietlang-name-female-middle.txt");
			readAndPrint(f);					
		}
	
		{
			File f = useFM().getResourceFile(AppConst.PATH_NAME, "vietlang-name-female-given.txt");
			readAndPrint(f);					
		}		
	}

	private static void readAndPrint(File f) 
	throws Exception
	{
		List<String> lines = FileUtils.readLines(f, "UTF-8");
		System.out.println(lines);
	}
}
