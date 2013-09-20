/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.au.cs.eagleEye.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author GalaxyNetworks
 */
public class ResultLogger {
  private FileWriter csvWriter;
  private BufferedWriter bufferedCsvWriter;
  
  private Master m;
  
  public ResultLogger(){
    m = Master.Inst();
  }
  
  public void OpenLogger(String name, Boolean writeToCsv) throws Exception {
    if (csvWriter != null)
      throw new Exception("Close existing logger first!");
    
    if (writeToCsv){
      // Save to disk
      File csvFile = new File("data/" + name + ".csv");

      try {
        csvFile.createNewFile();

        csvWriter = new FileWriter(csvFile.getAbsoluteFile());
        bufferedCsvWriter = new BufferedWriter(csvWriter);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  public void OpenLogger(String name, File csvFile) throws Exception {
    if (csvWriter != null)
      throw new Exception("Close existing logger first!");
    
    if (csvFile != null){
      // Save to disk
      
      try {
        csvFile.createNewFile();

        csvWriter = new FileWriter(csvFile.getAbsoluteFile());
        bufferedCsvWriter = new BufferedWriter(csvWriter);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  public void CloseLogger() throws IOException{
    if (csvWriter != null){
      csvWriter.close();
      
      csvWriter = null;
      bufferedCsvWriter = null;
    }
  }
  
  public void WriteComment(String comment) throws IOException{
    WriteLine("# " + comment);
  }
  
  public void WriteRow(Object[] values, char seperator) throws Exception{
    if (values.length == 0)
      throw new Exception("No Values!");
    
    String row = values[0].toString();
    
    for (int i = 1; i < values.length; i++){
      row += seperator + values[i].toString();
    }
    
    WriteLine(row);
  }
  
  private void WriteLine(String line) throws IOException{
    if (bufferedCsvWriter != null) {
      bufferedCsvWriter.write(line);
      bufferedCsvWriter.newLine();
      bufferedCsvWriter.flush();
    }
    
    m.ConsoleWrite(line);
  }
}
