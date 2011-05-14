package de.uplinkgmbh.lms.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class LMSToken implements Serializable{

	public static final long serialVersionUID = 923283090023232L;
	public String application;
	public Date genDate = new Date( System.currentTimeMillis() );
	public long userId;
	public static String magicString = "Magic string for LMS token";
	public final long uuid = UUID.randomUUID().getMostSignificantBits();
	
}
