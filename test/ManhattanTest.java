package test;

import algorithms.Manhattan;

public class ManhattanTest {
  public static void run(){
    Manhattan e = new Manhattan();
    e.Add(4.0);
    e.Add(-3.0);
    Double result = e.Calculate();
    
    System.out.println("Expected: 7, Actual: "+result);
  }
}
