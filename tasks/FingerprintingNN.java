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
      KNearestNeighbor nn = new KNearestNeighbor(entry, lAlgorithm);
      
      GeoPosition pos = nn.Position();
      
      String status = "Estimeret: "+pos+" , Actual: "+entry.getGeoPosition();
      m.ConsoleWrite(status);
      m.Debug(4, status);
      
      break; // Temp: Only the first
    }
    
    m.ConsoleWrite("[FingerprintingNN] End");
  }
}
