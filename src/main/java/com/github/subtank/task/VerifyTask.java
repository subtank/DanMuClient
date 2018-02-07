package com.github.subtank.task;

public class VerifyTask{
	
	public static boolean verifyID(String ID) {
		
		//check ID is legal
		if (ID.isEmpty()) return false;
		int ids;
		try {
			ids = Integer.parseInt(ID);
		} catch (NumberFormatException e) {
			return false;
		}
		if (ids <= 0) {
			return false;
		}
		return true;
	}
	
}
