package com.can.output.handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.IOutput;
import com.can.word.utils.PropertyHandler;

@Component("FileBean")
public class FileOutputHandler implements IOutput {
	private static final Logger LOGGER = Logger.getLogger(FileOutputHandler.class);
	@Autowired
	private PropertyHandler propertyHandler;
	
	public FileOutputHandler() {
		
	}

	@Override
	public void write(String string) {
		BufferedWriter bufferedWriter=null;
		try {
			bufferedWriter=new BufferedWriter(new FileWriter(new File(propertyHandler.getOutputFile())));
			bufferedWriter.write(string);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (IOException e) {
			LOGGER.equals(e);
		}finally{
			if(bufferedWriter!=null){
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					LOGGER.equals(e);
				}
				bufferedWriter=null;
			}
		}
		

	}

}
