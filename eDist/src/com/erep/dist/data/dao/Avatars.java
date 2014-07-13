package com.erep.dist.data.dao;

import javax.jdo.PersistenceManager;

import com.erep.dist.data.Avatar;

public class Avatars extends GenericDAO {

	public Avatars(PersistenceManager pm) {
		super(pm);
	}

	public boolean save(Avatar avatar) {
		pm().makePersistent(avatar);
		return true;
	}

	public Avatar find(Avatar avatar) {
		return pm().getObjectById(Avatar.class, avatar.getKey());
	}
}
