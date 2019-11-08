package com.stadio.task.baker.users;

import com.stadio.task.AppMainForTasks;
import com.stadio.task.utils.AppConst;
import com.stadio.common.utils.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Set;

public class generating_names extends AppMainForTasks
{
	public static void main(String[] args)
	throws Exception
	{
		Set<String> f = UserNameGenerator.loadVietNameFamily();
		Set<String> m = UserNameGenerator.loadVietNameMaleMiddle();
		Set<String> g = UserNameGenerator.loadVietNameMaleGiven();
		
		String pref = AppConst.FOLDER_USER;
		
		for(int k=0; k<100; k++)
		{
			List<String> lk = UserNameGenerator.nextName(f, m, g);
			String ek = UserNameGenerator.emailFromName(lk);
			File fk = new File(pref + StringUtils.beforeFirst('@', ek) + ".png");
			
			//UserNameGenerator.avatarFromText(lk + "; " + ek, fk);
			System.out.println(lk + " -> " + ek + " | " + fk);
		}
		
		System2.theEnd();
	}

}
