package dk.au.cs.eagleEye.algorithms;

import java.util.*;

import dk.au.cs.eagleEye.main.Master;
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
  private List<TraceEntry> searchEntries;

  public KNearestNeighbor(TraceEntry actual, LocalizationAlgorithm lAlgorithm, int numberOfNearest, boolean filterOnTime) {
    m = Master.Inst();

    this.actual = actual;
    this.lAlgorithm = lAlgorithm;
    this.numberOfNearest = numberOfNearest;
    this.distances = new HashMap();
    smallestDistances = new Double[numberOfNearest];
    smallestEntries = new TraceEntry[numberOfNearest];
    searchEntries = m.getBaseTrace();

    if (filterOnTime) {
      doTimeFilter();
    }

    this.lAlgorithm.setActual(actual);
  }

  @Override
  public GeoPosition Position() {
    compare();
    findSmallest();
    
    return findAvarage();
  }

  private void doTimeFilter() {
    // Contains the best search results
    List<TraceEntry> timeFilteredTrace = new ArrayList<TraceEntry>();
    // Contains the best matching time for a given geo position.
    Map<GeoPosition, Integer> timeFilteredTraceMap = new HashMap<GeoPosition, Integer>();

    // Variables used for checking time differences.
    GregorianCalendar entryDate = new GregorianCalendar();
    GregorianCalendar actualDate = new GregorianCalendar();
    actualDate.setTimeInMillis(actual.getTimestamp());

    for (TraceEntry entry : searchEntries) {
      entryDate.setTimeInMillis(entry.getTimestamp());

      int actualTime = actualDate.get(Calendar.HOUR_OF_DAY)*3600 + actualDate.get(Calendar.MINUTE)*60 + actualDate.get(Calendar.SECOND);
      int entryTime = entryDate.get(Calendar.HOUR_OF_DAY)*3600 + entryDate.get(Calendar.MINUTE)*60 + entryDate.get(Calendar.SECOND);
      int timeDifference = Math.abs(actualTime-entryTime);

      if (timeFilteredTraceMap.containsKey(entry.getGeoPosition())) {
        int compareTime = timeFilteredTraceMap.get(entry.getGeoPosition());
        if (timeDifference < compareTime) {
          timeFilteredTraceMap.put(entry.getGeoPosition(), timeDifference);

          for (int i=0; i<timeFilteredTrace.size(); i++) {
            if (timeFilteredTrace.get(i).getGeoPosition().equals(entry.getGeoPosition())) {
              timeFilteredTrace.set(i, entry);
              break;
            }
          }
        }
      } else {
        timeFilteredTraceMap.put(entry.getGeoPosition(), timeDifference);
        timeFilteredTrace.add(entry);
      }
    }

    this.searchEntries = timeFilteredTrace;
  }

  private void compare() {
    for (TraceEntry entry : searchEntries) {
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
