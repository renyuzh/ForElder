package com.heart.service;

import java.net.UnknownHostException;

import com.heart.dao.GuardianDao;
import com.heart.dao.OldManDao;

public class CommomService {

	public void binding(String oldmanPhone, String guardianPhone) throws UnknownHostException{
		
		new OldManDao().bindForOldMan(oldmanPhone, guardianPhone);
		
		new GuardianDao().addBindForGuardian(oldmanPhone, guardianPhone);
		
		
	}
}
