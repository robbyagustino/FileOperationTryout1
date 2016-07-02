package com.dummy.sample;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FileUpdaterWorker {
	
	//From command prompt, go to bin, and start with:
	//java com.dummy.sample.FileUpdaterWorker
	public static void main(String[] args) {
		try {
			
			Scanner in = new Scanner(System.in);
			System.out.println("Please input data directory path followed by enter (example: d:/logs): ");
			String param1 = in.nextLine();
			System.out.println("Please input number of threads followed by enter (example: 3):");
			String param2 = in.nextLine();

			log("MAIN", "START - press Enter anytime to exit");
			final String BREAK = "BREAK";
			long starTime = System.currentTimeMillis(); 
			File inputFolder = new File(param1);
			int numberOfThreads = new Integer(param2).intValue();
			ExecutorService exec = Executors.newFixedThreadPool(numberOfThreads);
			
			mainProcess: try {  
				File file; 
				Future fTemp;
				boolean allTaskDone =  false;
				List listOfFile = getListOfFiles(inputFolder, new ArrayList());
				List listOfFuture = new ArrayList();
				Future f = exec.submit(new KeyListenerTask()); 
				
				for (int i = 0; i < listOfFile.size(); i++) {		
					file = (File) listOfFile.get(i);
					listOfFuture.add(exec.submit( new FileUpdaterTask(file, FileUpdaterTask.OPERATION_NOT, "File-"+i)));
				}

				while (true) {
					int doneCount = 0;
					for (int j = 0; j < listOfFuture.size(); j++) {	
						fTemp = (Future) listOfFuture.get(j);
						if(fTemp.isDone()){
							doneCount = doneCount+1;
						}
					}
					if(doneCount == listOfFuture.size()){
						exec.shutdownNow();
						throw new Exception(BREAK);
					}
					if(f.isDone()){
						exec.shutdownNow();
						throw new Exception(BREAK);
					}
				}	
			} catch(Exception e) {
				if(!BREAK.equalsIgnoreCase(e.getMessage())){
					e.printStackTrace();	
				}
			} finally {
				if(!exec.isShutdown()){
					exec.shutdown();
				}
				exec.awaitTermination(1, TimeUnit.SECONDS);
				long elapsedTime = System.currentTimeMillis() - starTime;
				log("MAIN", "FINISH in "+ (elapsedTime/1000F) + " seconds");
				System.exit(0);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to traverse directories and sub directories to get list of files 
	 * @param directory
	 * @param files
	 * @return list of files
	 */
	private static List getListOfFiles(File directory, List files){
	    File[] fList = directory.listFiles();
	    for (File file : fList) {
	        if (file.isFile()) {
	            files.add(file);
	        } else if (file.isDirectory()) {
	        	getListOfFiles(file, files);
	        }
	    }
	    return files;
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
