package dk.au.cs.eagleEye.algorithms;

import java.util.ArrayList;

public class Manhattan implements DistanceAlgorithm {
  private ArrayList<Double> distances;
  
  @Override
  public void Add(Double distance){
    distances.add(distance);
  }
  
  @Override
  public final void Clear(){
    distances = new ArrayList<>();
  }
  
  public Manhattan(){
    Clear();
  }
  
  @Override
  public Double Calculate(){
    double total = 0;
    
    for(Double distance : distances){
      if(0 <= distance){
        total += distance;
      }else{
        total += distance * -1;
      }
    }
    
    return total;
  }
}
