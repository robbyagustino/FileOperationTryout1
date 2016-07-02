package com.dummy.sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KeyListenerTask implements Runnable {

	/**
	 * Main Constructor
	 */
	public KeyListenerTask() {
	}

	@Override
	public void run() {	
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String msg = null;
		boolean process = true;
		while(process){
		    try{
		    	msg=in.readLine();
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
		     
		    if(msg!=null && msg.length() == 0){
		    	log("Enter key detected.", "Exiting program..");
		    	process= false;
            }
		}
	}
	
	/**
	 * Method to print input to console
	 * @param input1
	 * @param input2
	 */
	private static void log(String input1, String input2) {
		StringBuffer sb = new StringBuffer();	
		Date currDate = new Date(System.currentTimeMillis()) ;
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd, HH:mm:ss.S");	
		System.out.println(sb.append(sdf.format(currDate)).append(" ").append(input1).append(" ").append(input2));
	}
	
}
