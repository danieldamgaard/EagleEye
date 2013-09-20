/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.au.cs.eagleEye.tasks;

import dk.au.cs.eagleEye.main.Master;
import dk.au.cs.eagleEye.main.ResultLogger;
import dk.au.cs.eagleEye.model.AccessPoint;
import dk.au.cs.eagleEye.model.EstimatedItem;
import dk.au.cs.eagleEye.model.EstimatedItemComparator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;

/**
 *
 * @author GalaxyNetworks
 */
public class Score {
  public static void scoreSvc(File svcFile, String seperator, File scoredFile){
    Master m = Master.Inst();
    
    m.ConsoleWrite("Scoring selected file...");
    try {	
      BufferedReader in = new BufferedReader(new FileReader(svcFile));
      ResultLogger resultLogger = new ResultLogger();
      resultLogger.OpenLogger(scoredFile.getName(), scoredFile);
      
      List<EstimatedItem> estimatedResults = new ArrayList<EstimatedItem>();
      
      String line;
      // Process each line.
      while ((line = in.readLine()) != null) {
        if (line.startsWith("#")){
          continue;
        }
        
        String[] values = line.split(seperator);
        
        estimatedResults.add(new EstimatedItem(new GeoPosition(Double.parseDouble(values[0]),
                Double.parseDouble(values[1])), new GeoPosition(Double.parseDouble(values[2]),
                Double.parseDouble(values[3])), Double.parseDouble(values[4])));
      }
      
      Collections.sort(estimatedResults, new EstimatedItemComparator());
      
      // Calculate and write results!
      resultLogger.WriteComment("estimated x; estimated y; actual x; actual y; error distance; percentages of error values equal or smaller to this;");
      
      for (int i = estimatedResults.size() - 1; i >= 0; i--){
        int countSmallerOrEqual = estimatedResults.size() - 1 - i;
        
        EstimatedItem currentItem = estimatedResults.get(i);
        
        for (int y = i + 1; y < estimatedResults.size() - 1; y++){
          if (estimatedResults.get(y).getErrorDistance() > currentItem.getErrorDistance())
            break;
          
          countSmallerOrEqual++;
        }
        
        double procentSmallerOrEqual = (countSmallerOrEqual / (estimatedResults.size() - 1.0)) * 100;
        
        resultLogger.WriteRow(new Object[]{
          currentItem.getEstPosition().getX(),
          currentItem.getEstPosition().getY(),
          currentItem.getActPosition().getX(),
          currentItem.getActPosition().getY(),
          currentItem.getErrorDistance(),
          procentSmallerOrEqual
        }, ';');
      }
      
      resultLogger.CloseLogger();
      
      m.ConsoleWrite("Scoring complete!");
    } catch (Exception e) {
      m.ConsoleWrite("Scoring failed!");
    }
  }
}
