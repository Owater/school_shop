package com.siso.app.utils;

import java.util.Random;
import java.util.UUID;

public class UUIDGenerator {

	/**
	 * 
	 * @author Owater
	 * @createtime 2015-4-10 上午9:51:00
	 * @Decription
	 *
	 * @return
	 */
	public static String getUUID() {
		String filePath = UUID.randomUUID().toString();
		return filePath.replace("-", "0");
		
	}

}
