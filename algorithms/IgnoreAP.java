package algorithms;

import java.util.List;
import main.Master;
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
    Master m = Master.Inst();
    algorithm.Clear();
    SignalStrengthSamples entrySss = entry.getSignalStrengthSamples();
    
    boolean containsAnyAP = false;
    
    for(MACAddress actualMac : actualAps){
      if(entrySss.containsKey(actualMac)){
        Double actualSs = actualSss.getAverageSignalStrength(actualMac);
        Double entrySs = entrySss.getAverageSignalStrength(actualMac);
        Double difference = actualSs - entrySs;
        
        m.Debug(4, "Actual SS: "+actualSs+", Entry SS: "+entrySs+", Difference: "+difference+" (MAC: "+actualMac+").");
        
        algorithm.Add(difference);
        containsAnyAP = true;
      }else{
        m.Debug(4, "The access point (AP) is not in the fingerprint (MAC: "+actualMac+").");
      }
    }
    
    if(!containsAnyAP){
      return null;
    }
    
    Double totalDistance = algorithm.Calculate();
    
    m.Debug(4, "Total distance: "+totalDistance+" ([Actual] Pos: "+actual.getGeoPosition()+", Time: "+actual.getTimestamp()+", MAC: "+actual.getId()+").");
    
    return totalDistance;
  }
  
}
