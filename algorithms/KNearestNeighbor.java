package algorithms;

import java.util.HashMap;
import main.Master;
import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.trace.TraceEntry;

public class KNearestNeighbor implements PositioningAlgorithm {
  private Master m;
  private TraceEntry actual;
  private LocalizationAlgorithm lAlgorithm;
  private int numberOfNearest;
  private HashMap<TraceEntry, Double> distances;
  private Double[] smallestDistances;
  private TraceEntry[] smallestEntries;

  public KNearestNeighbor(TraceEntry actual, LocalizationAlgorithm lAlgorithm, int numberOfNearest) {
    m = Master.Inst();

    this.actual = actual;
    this.lAlgorithm = lAlgorithm;
    this.numberOfNearest = numberOfNearest;
    this.distances = new HashMap();
    smallestDistances = new Double[numberOfNearest];
    smallestEntries = new TraceEntry[numberOfNearest];

    this.lAlgorithm.setActual(actual);
  }

  @Override
  public GeoPosition Position() {
    compare();
    findSmallest();
    
    return findAvarage();
  }

  private void compare() {
    for (TraceEntry entry : m.getBaseTrace()) {
      Double distance = lAlgorithm.CompareTo(entry);

      distances.put(entry, distance);

      m.Debug(
          4,
          "[NN] Actual: " + actual.getGeoPosition() + " ("
              + actual.getTimestamp() + "), GeoPos: " + entry.getGeoPosition()
              + " (" + entry.getTimestamp() + "), distance: " + distance
              + ", total number: " + distances.size());
    }
  }

  private void findSmallest() {
    
    int numberOfSaved = 0;
    
    for (TraceEntry entry : distances.keySet()) {
      Double distance = distances.get(entry);

      if (distance == null) {
        m.Debug(4, "[Skip] There was no APs from actual in this entry (Pos: "
            + entry.getGeoPosition() + ", Time: " + entry.getTimestamp()
            + ", MAC: " + entry.getId() + ")");
        continue;
      }

      m.Debug(4, "[Compare distances] current distance: " + distance + ".");

      if(numberOfSaved < numberOfNearest) {
        smallestDistances[numberOfSaved] = distance;
        smallestEntries[numberOfSaved] = entry;
        numberOfSaved++;
      }else {
        boolean isSmaller = false;
        double largestDistance = Double.MIN_VALUE;
        int largestIndex = 0;
        
        int index = 0;
        for(Double smallestDistance : smallestDistances) {
          if(distance < smallestDistance) {
            isSmaller = true;
          }
          
          if(largestDistance < smallestDistance) {
            largestDistance = smallestDistance;
            largestIndex = index;
          }
          
          index++;
        }
        
        if(isSmaller) {
          smallestDistances[largestIndex] = distance;
          smallestEntries[largestIndex] = entry;
        }
      }
    }
  }
  
  private GeoPosition findAvarage() {
    double xTotal = 0;
    double yTotal = 0;
    
    for(TraceEntry entry : smallestEntries) {
      xTotal += entry.getGeoPosition().getX();
      yTotal += entry.getGeoPosition().getY();
    }
    
    double xAvg = xTotal / numberOfNearest;
    double yAvg = yTotal / numberOfNearest;
    
    return new GeoPosition(xAvg, yAvg);
  }

}
