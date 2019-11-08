package com.stadio.task;


import com.stadio.common.files.TomcatFileManager;

public class AppMainForTasks {

	private static TomcatFileManager FM = new TomcatFileManager();

	public static TomcatFileManager useFM()
	{
		return FM;
	}


}
