/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.au.cs.eagleEye.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author GalaxyNetworks
 */
public class MedianAvgCalculator {  
  private List<Double> errorDistances = new ArrayList<Double>();
  
  public void add(double errorDistance){
       errorDistances.add(errorDistance);
  }
  
  public double getAvg(){
    Collections.sort(errorDistances);
    
    if ((errorDistances.size() / 2.0) % 2 != 0){ // lookup
      int firstIndex = (int)(errorDistances.size() / 2.0);
      
      return (errorDistances.get(firstIndex) + errorDistances.get(firstIndex + 1)) / 2;
    }
    else
      return errorDistances.get(errorDistances.size() / 2);
  }
}
