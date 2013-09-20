package dk.au.cs.eagleEye.algorithms;

import java.util.ArrayList;

public class Euclidean implements DistanceAlgorithm {
  private ArrayList<Double> distances;
  
  @Override
  public void Add(Double distance){
    distances.add(distance);
  }
  
  @Override
  public final void Clear(){
    distances = new ArrayList<Double>();
  }
  
  public Euclidean(){
    Clear();
  }
  
  @Override
  public Double Calculate(){
    double total = 0;
    
    for(Double distance : distances){
      total += Math.pow(distance, 2.0);
    }
    
    return Math.sqrt(total);
  }
}
