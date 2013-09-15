/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.au.cs.eagleEye.algorithms;

import dk.au.cs.eagleEye.model.AccessPoint;
import java.util.*;
import dk.au.cs.eagleEye.main.Master;
import org.pi4.locutil.trace.SignalStrengthSamples;
import org.pi4.locutil.trace.TraceEntry;

/**
 *
 * @author GalaxyNetworks
 */
public class ModelBuilder {

  public List<TraceEntry> generateModel(double n, double pd0, double d0, double waf, int c){    
    List<TraceEntry> offlineList = Master.Inst().getOfflineTrace();
    List<TraceEntry> modelList = new ArrayList<TraceEntry>();
    
    
    for (TraceEntry entry : offlineList) {
      SignalStrengthSamples sss = new SignalStrengthSamples();
    
      for (AccessPoint ap : Master.Inst().getAccessPoints()) {
        //double distance = 0.0; // Mellem ap og entry
        double distance = Math.sqrt(Math.pow(ap.getGeoPosition().getX() - entry.getGeoPosition().getX(), 2) +
                                    Math.pow(ap.getGeoPosition().getY() - entry.getGeoPosition().getY(), 2));
                
        // 0 * waf, as waf is not implemented!
        int nW = 0;
        if (nW > c) nW = c;
        double pd = pd0 - 10 * n * Math.log(distance/d0) - nW * waf; // Forventet ss for T-R
        
        sss.put(ap.getMacAddress(), pd);
      }
      
      // Timestamp: -1 (No timestamp), MAC = null (no recording device, we use a model)
      TraceEntry modelEntry = new TraceEntry(-1, entry.getGeoPosition(), null, sss);
      modelList.add(modelEntry);
    }
    
    return modelList;
  }
}
