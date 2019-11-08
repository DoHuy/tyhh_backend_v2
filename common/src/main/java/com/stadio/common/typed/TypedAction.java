package com.stadio.common.typed;

public interface TypedAction<T1> 
{
	public void invokeAction(T1 res) throws Exception;

}
