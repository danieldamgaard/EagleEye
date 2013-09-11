package test;

import algorithms.Euclidean;

public class EuclideanTest {
  public static void run(){
    Euclidean e = new Euclidean();
    e.Add(47.0);
    e.Add(7.0);
    e.Add(-35.0);
    Double result = e.Calculate();
    
    System.out.println("Expected: 59.02, Actual: "+result);
  }
}
