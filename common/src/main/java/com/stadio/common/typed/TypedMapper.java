package com.stadio.common.typed;

public interface TypedMapper<S1, S2> {

	public S2 invokeMapperAction(S1 rk);

}
