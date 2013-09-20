/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.au.cs.eagleEye.main;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author GalaxyNetworks
 */
public class CDFErrorCalculator {
  private HashMap<Integer, Integer> cdfErrorMap = new HashMap<Integer, Integer>();
  private double interval;
  
  public CDFErrorCalculator(double interval) {
    this.interval = interval;
  }
  
  public void add(double errorDistance){
    int index = (int)(errorDistance/ interval);
    
    if (cdfErrorMap.containsKey(index))
      cdfErrorMap.put(index, cdfErrorMap.get(index) + 1);
    else
      cdfErrorMap.put(index, 1);      
  }
  
  public Object[][] getResultSet(){
    Object[][] results = new Object[cdfErrorMap.size()][2];
    
    int index = 0;
    for (Map.Entry<Integer, Integer> indexCountSet : cdfErrorMap.entrySet()) {
      results[index][0] = interval * indexCountSet.getKey();
      results[index][1] = indexCountSet.getValue();
      
      index++;
    }
    
    return results;
  }
}
