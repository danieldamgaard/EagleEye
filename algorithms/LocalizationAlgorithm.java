package algorithms;

import org.pi4.locutil.trace.TraceEntry;

public interface LocalizationAlgorithm {
  // Returns a ratio which discribes the relationship
  public Double CompareTo(TraceEntry entry);
  
  public void setActual(TraceEntry actual);
}
