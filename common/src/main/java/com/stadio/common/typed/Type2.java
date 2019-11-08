package com.stadio.common.typed;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

public class Type2 
{
	public static Map<String, Object> mapFromObject(Object values)
	throws Exception
	{
		Map<String, Object> res = new LinkedHashMap<String, Object>();
	
		for (Field fj: values.getClass().getFields())
		if( Modifier.isPublic(fj.getModifiers()) )
		try 
		{
			Object vj = fj.get(values);
			res.put(fj.getName(), vj);
		} 
		catch (Exception xp) {}

		return res;
	}
	
	public static Map<String, Object> mapFromObjectNotNull(Object values)
	throws Exception
	{
		Map<String, Object> res = new LinkedHashMap<String, Object>();
	
		for (Field fj: values.getClass().getFields())
		if( Modifier.isPublic(fj.getModifiers()) )
		try 
		{
			Object vj = fj.get(values);
			if(vj != null) res.put(fj.getName(), vj);
		} 
		catch (Exception xp) {}

		return res;
	}


}
