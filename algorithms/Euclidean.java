package algorithms;

import java.util.ArrayList;

public class Euclidean implements DistanceAlgorithm {
  private ArrayList<Double> distances;
  
  @Override
  public void Add(Double distance){
    distances.add(distance);
  }
  
  public Euclidean(){
    distances = new ArrayList<>();
  }
  
  @Override
  public Double Calculate(){
    double total = 0;
    
    for(Double distance : distances){
      total += Math.pow(distance, 2);
    }
    
    return Math.sqrt(total);
  }
}
