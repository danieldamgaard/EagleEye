package dk.au.cs.eagleEye.tasks;

import dk.au.cs.eagleEye.algorithms.IgnoreAP;
import dk.au.cs.eagleEye.algorithms.DistanceAlgorithm;
import dk.au.cs.eagleEye.algorithms.LocalizationAlgorithm;
import dk.au.cs.eagleEye.algorithms.Euclidean;
import dk.au.cs.eagleEye.algorithms.KNearestNeighbor;
import dk.au.cs.eagleEye.main.Master;
import java.util.List;
import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.trace.TraceEntry;

public class FingerprintingNN {  
  public static void run(List<TraceEntry> fingerprintList){
    Master m = Master.Inst();
    m.ConsoleWrite("[FingerprintingNN] Start");
    
    FingerprintingKNN.run(fingerprintList, 1);
    
    m.ConsoleWrite("[FingerprintingNN] End");
  }
}
