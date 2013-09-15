package dk.au.cs.eagleEye.tasks;

import dk.au.cs.eagleEye.algorithms.DistanceAlgorithm;
import dk.au.cs.eagleEye.algorithms.Euclidean;
import dk.au.cs.eagleEye.algorithms.IgnoreAP;
import dk.au.cs.eagleEye.algorithms.KNearestNeighbor;
import dk.au.cs.eagleEye.algorithms.LocalizationAlgorithm;
import dk.au.cs.eagleEye.main.Master;
import java.util.List;
import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.trace.TraceEntry;

public class FingerprintingKNN {
  public static void run(List<TraceEntry> fingerprintList, int k){
    Master m = Master.Inst();
    m.ConsoleWrite("[FingerprintingKNN] Start, with k = " + k);

    // Use the given list for calculations
    m.setBaseTrace(fingerprintList);
    
    DistanceAlgorithm dAlgorithm = new Euclidean();
    LocalizationAlgorithm lAlgorithm = new IgnoreAP(dAlgorithm);
    
    for(TraceEntry entry: m.getOnlineTrace()) {
      KNearestNeighbor nn = new KNearestNeighbor(entry, lAlgorithm, k);
      
      GeoPosition pos = nn.Position();
      
      String status = "Estimated "+pos+" , Actual: "+entry.getGeoPosition();
      m.ConsoleWrite(status);
      m.Debug(4, status);
      
      break; // Temp: Only the first
    }
    
    m.ConsoleWrite("[FingerprintingKNN] End");
  }
}
