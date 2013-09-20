/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.au.cs.eagleEye.tasks;

import dk.au.cs.eagleEye.main.Master;
import dk.au.cs.eagleEye.main.ResultLogger;
import dk.au.cs.eagleEye.model.AccessPoint;
import java.util.ArrayList;
import java.util.List;
import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.trace.SignalStrengthSamples;
import org.pi4.locutil.trace.TraceEntry;

/**
 *
 * @author GalaxyNetworks
 */
public class Evaluation {
  private static ResultLogger logger = new ResultLogger();
  
  private static void calcSsForDistanceToMessuredAps(String name, Boolean writeToCsv) {
    Master m = Master.Inst();
    try {
      logger.OpenLogger(name, writeToCsv);

      // Do the eval.
      logger.WriteComment("Results for " + name);
      logger.WriteComment("Distance; Signal Strength;");

      List<GeoPosition> evalualtedPosition = new ArrayList<GeoPosition>();

      for (TraceEntry entry : m.getOfflineTrace()){
        if (evalualtedPosition.contains(entry.getGeoPosition()))
          continue;

        List<AccessPoint> aps = m.getAccessPoints();
        SignalStrengthSamples sss = entry.getSignalStrengthSamples();

        for (AccessPoint ap : aps) {
          if (sss.containsKey(ap.getMacAddress())){
            double distance = Math.sqrt(Math.pow(ap.getGeoPosition().getX() - entry.getGeoPosition().getX(), 2) +
                                        Math.pow(ap.getGeoPosition().getY() - entry.getGeoPosition().getY(), 2));

            double ss = sss.getAverageSignalStrength(ap.getMacAddress());

            logger.WriteRow(new Object[]{distance, ss}, ';');
          } 
        }

        evalualtedPosition.add(entry.getGeoPosition());
      }
      
      logger.CloseLogger();
    } catch (Exception e){
      m.Debug(1, "Simulation Failed!: " + e.getMessage());
    }
  }

  public static void evaluate(Boolean writeToCsv) {
    calcSsForDistanceToMessuredAps("ssForDistanceToMessuredAPs", writeToCsv);
  }  
}
