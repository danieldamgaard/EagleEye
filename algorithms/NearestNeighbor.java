package algorithms;

import java.util.HashMap;
import main.Master;
import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.trace.TraceEntry;

public class NearestNeighbor implements PositioningAlgorithm {
  private Master m;
  private TraceEntry actual;
  private LocalizationAlgorithm lAlgorithm;
  private HashMap<TraceEntry, Double> distances;
  
  public NearestNeighbor(TraceEntry actual, LocalizationAlgorithm lAlgorithm){
    m = Master.Inst();
    
    this.actual = actual;
    this.lAlgorithm = lAlgorithm;
    this.distances = new HashMap();
    
    this.lAlgorithm.setActual(actual);
  }

  @Override
  public GeoPosition Position() {
    compare();
    
    Double smallestDistance = Double.MAX_VALUE;
    TraceEntry smallestEntry = null;
    
    for(TraceEntry entry: distances.keySet()){
      Double distance = distances.get(entry);
      
      if(distance == null){
        m.Debug(4, "[Skip] There was no APs from actual in this entry (Pos: "+entry.getGeoPosition()+", Time: "+entry.getTimestamp()+", MAC: "+entry.getId()+")");
        continue;
      }
      
      m.Debug(4, "[Compare distances] current distance: "+distance+", smallest distance: "+smallestDistance+".");
      
      if(distance < smallestDistance){
        smallestDistance = distance;
        smallestEntry = entry;
      }
    }
    
    m.Debug(4, "[NN] Smallest entry: "+smallestEntry.getGeoPosition()+" (MAC: "+smallestEntry.getId()+") (Time: "+smallestEntry.getTimestamp()+"), smallest distance: "+smallestDistance+".");
    
    return smallestEntry.getGeoPosition();
  }
  
  private void compare(){
    for(TraceEntry entry: m.getBaseTrace()) {
      Double distance = lAlgorithm.CompareTo(entry);
      
      distances.put(entry, distance);
      
      m.Debug(4, "[NN] Actual: "+actual.getGeoPosition()+" ("+actual.getTimestamp()+"), GeoPos: "+entry.getGeoPosition()+" ("+entry.getTimestamp()+"), distance: "+distance+", total number: "+distances.size());
    }
  }

}
