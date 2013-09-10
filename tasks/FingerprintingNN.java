package tasks;

import algorithms.*;
import main.Master;
import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.trace.TraceEntry;

public class FingerprintingNN {
  public static void run(){
    Master m = Master.Inst();
    m.ConsoleWrite("[FingerprintingNN] Start");
    
    // Use the OfflineTrace for calculations
    m.setBaseTrace(m.getOfflineTrace());
    
    DistanceAlgorithm dAlgorithm = new Euclidean();
    LocalizationAlgorithm lAlgorithm = new IgnoreAP(dAlgorithm);
    
    for(TraceEntry entry: m.getOnlineTrace()) {
      NearestNeighbor nn = new NearestNeighbor(entry, lAlgorithm, dAlgorithm);
      
      GeoPosition pos = nn.Position();
      
      m.ConsoleWrite("Estimeret: "+pos+", Actual: "+entry.getGeoPosition());
      
      break; // Temp: Only the first
    }
    
    m.ConsoleWrite("[FingerprintingNN] End");
  }
}
