package com.dummy.sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUpdaterTask implements Runnable {

	public static String OPERATION_NOT = "NOT";
	private File file; 
	private String taskName;
	private String operationType;
	
	/**
	 * Main Constructor
	 * @param file
	 * @param operationType
	 * @param taskName
	 */
	public FileUpdaterTask(File file, String operationType, String taskName) {
		this.file = file;
		this.operationType = operationType;
		this.taskName = taskName;
	}

	@Override
	public void run() {
		log("Start", "");
		textFileOperation(file, operationType);
		log("Finish", "");		
	}
	
	/**
	 * Main function, to update the file content  
	 * @param file
	 * @param operationType
	 */
	private void textFileOperation(File file, String operationType){
		try {
			log("Processing", file.getName());
			BufferedReader br = new BufferedReader(new FileReader(file));
			FileWriter fw = null;
			BufferedWriter bw = null;
			try {
			    StringBuilder sb = new StringBuilder();
			    String line = br.readLine();

			    while (line != null) {
			        sb.append(line);
			        sb.append(System.lineSeparator());
			        line = br.readLine();
			    }
			    
			    String content = sb.toString();			    
			    String updatedContent = getContentUpdated(content, operationType); 		
			    
				fw = new FileWriter(file);
				bw = new BufferedWriter(fw);
			    bw.write(updatedContent);			    
			} catch (IOException e){
				e.printStackTrace();
			} finally {
			    br.close();
			    bw.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}		
	}
	
	/**
	 * Method to update a string according to the update type.
	 * For now only have NOT operation
	 * @param text
	 * @param updateType
	 * @return updated content
	 */
	private String getContentUpdated(String text, String updateType){
		Integer temp = new Integer(0);
		char[] arrayChar = text.toCharArray();
		String result = "";
		
		if(OPERATION_NOT.equalsIgnoreCase(updateType)){
			for (int i = 0; i < arrayChar.length; i++) {
				temp = (int) arrayChar[i];
				temp = bitwiseNegation(temp.intValue());
				result = result + temp.toString();
			}
		}
		
		return result;
	}
	
	/**
	 * Method to do bitwise negation 
	 * @param input
	 * @return result
	 */
	private int bitwiseNegation(int input){
		int result = ~input;
		return result;
	}
	
	/**
	 * Method to print input to console
	 * @param input1
	 * @param input2
	 */
	private void log(String input1, String input2) {
		StringBuffer sb = new StringBuffer();		
		Date currDate = new Date(System.currentTimeMillis()) ;
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd, HH:mm:ss.S");
		System.out.println(sb.append(sdf.format(currDate)).append(" [").append(taskName).append("] ").append(input1).append(" ").append(input2));
	}
	
}
