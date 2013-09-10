package test;

import algorithms.Euclidean;

public class EuclideanTest {
  public static void run(){
    Euclidean e = new Euclidean();
    e.Add(4.0);
    e.Add(3.0);
    Double result = e.Calculate();
    
    System.out.println("Expected: 5, Actual: "+result);
  }
}
