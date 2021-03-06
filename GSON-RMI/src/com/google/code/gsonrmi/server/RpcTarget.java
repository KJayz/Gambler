package com.google.code.gsonrmi.server;

import com.google.gson.*;
import com.google.code.gsonrmi.*;

public class RpcTarget {

	private Invoker invoker;
	private Object obj;
	
	public RpcTarget(Object obj, Gson gson) {
		invoker = new Invoker(new DefaultParamProcessor(gson));
		this.obj = obj;
	}
	
	public RpcResponse doInvoke(RpcRequest request) {
//		//sequentialize access to the object
//		synchronized (obj) {

		// INTENTIONALLY DO NOT SYNCHRONIZE access to the object, since correct
		// synchronization is left to the higher layers of the application!
		return invoker.doInvoke(request, obj, null);
//		}
	}

}
