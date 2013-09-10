package algorithms;

import java.util.List;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.trace.SignalStrengthSamples;
import org.pi4.locutil.trace.TraceEntry;

public class IgnoreAP implements LocalizationAlgorithm {
  private DistanceAlgorithm algorithm;
  private TraceEntry actual;
  private SignalStrengthSamples actualSss;
  private List<MACAddress> actualAps;
  
  @Override
  public void setActual(TraceEntry actual){
    this.actual = actual;
    
    actualSss = actual.getSignalStrengthSamples();
    actualAps = actualSss.getSortedAccessPoints();
  }
  
  public IgnoreAP(DistanceAlgorithm algorithm){
    this.algorithm = algorithm;
  }
  
  @Override
  public Double CompareTo(TraceEntry entry) {
    SignalStrengthSamples entrySss = entry.getSignalStrengthSamples();
    
    for(MACAddress actualMac : actualAps){
      if(entrySss.containsKey(actualMac)){
        Double actualSs = actualSss.getAverageSignalStrength(actualMac);
        Double entrySs = entrySss.getAverageSignalStrength(actualMac);
        Double difference = actualSs - entrySs;
        
        algorithm.Add(difference);
      }
    }
    
    return algorithm.Calculate();
  }
  
}
