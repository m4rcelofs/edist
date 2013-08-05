package com.erep.dist.dwr;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

@RemoteProxy
public class FormTester {

	@RemoteMethod
	public int theAnswer() {
		return 42;
	}

	@RemoteMethod
	public PlayerInfo testForm(String id) {
		PlayerInfo player = new PlayerInfo(false);
//		if (id.equals("Guilpas")){
//			player = new PlayerInfo(true,false,"Guilpas",
//					"http://static.erepublik.net/uploads/avatars/Citizens/2010/09/13/5711b24ddebb1a4e8faaae65c6250b9c_142x142.jpg",
//					160,0);
//		}
//		else{
//			player = new PlayerInfo(false);	
//		}

		return player;
	}
}