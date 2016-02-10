package common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dataModel.Day;

public class TransposeFile {
	private static String urlData = "dataIn/transposeFile";

	public static void main(String[] args) {
		try{
			File folder = new File(urlData);
			if(!folder.exists()){
				throw new NotDirectoryException(null);
			}

			BufferedReader reader = null;
			
			 FilenameFilter ActFilter = (dir, name) -> {
		            if(name.lastIndexOf('.')>0){
		                        int lastIndexDot = name.lastIndexOf('.');
		                        String filename = name.subSequence(0, lastIndexDot).toString();
		                        String extension = name.substring(lastIndexDot);
		                        if(extension.equals(".txt")){
		                            return true;
		                        }
		                    }      
		            return false;
		        };
			
			ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(folder.listFiles(ActFilter)));
			if(fileList.isEmpty()){
				throw new FileNotFoundException(null);
			}
			
			
			for (File currentFile : fileList) {
				ArrayList<String> configLines = new ArrayList<String>();
				System.out.println(currentFile.getName());
	            try {
	                reader = new BufferedReader(new FileReader(currentFile));
	                String line = null;
	                while ((line = reader.readLine()) != null) {
	                    configLines.add(line);
	                }
	            } catch (NullPointerException | IOException e) {
	                e.printStackTrace();
	            } finally {
	                try {
	                    if (reader != null){
	                        reader.close();
	                    }
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
			
			
			Map<Integer,List<String>> newLines =new HashMap<Integer,List<String>>();
			newLines.clear();
			for(int i=0;i<86400;i++){
				newLines.put(i, new ArrayList<String>());
			}
			for(String oldLine:configLines){
				String[] stV=oldLine.split(",");
				for(int i=0;i<stV.length;i++){
					newLines.get(i).add(stV[i]);
				}
			}
			File outputFile = new File(urlData+"/OUT"+currentFile.getName());
			FileOutputStream fos = new FileOutputStream(outputFile);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			for (Entry<Integer, List<String>> nl : newLines.entrySet()) {
				String lin="";
				for(String sss:nl.getValue()){
					lin+=sss+",";
				}
				lin=lin.substring(0,lin.length()-1);
				bw.write(lin);
				bw.newLine();
			}
			bw.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
