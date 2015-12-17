package com.heart.util;

import java.util.Random;

public class GenerateCode {


	public static String involk(){
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < 5; i++)
		{
			int k = new Random().nextInt(10);
			
			sb.append(k);
		}
		
		return sb.toString();
	}
}
