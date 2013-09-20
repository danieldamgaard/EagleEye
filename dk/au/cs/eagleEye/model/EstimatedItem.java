/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.au.cs.eagleEye.model;

import org.pi4.locutil.GeoPosition;

/**
 *
 * @author GalaxyNetworks
 */
public class EstimatedItem {
  private GeoPosition estPos;
  public GeoPosition getEstPosition(){
    return estPos;
  }
  
  private GeoPosition actPos;
  public GeoPosition getActPosition(){
    return actPos;
  }
  
  private double errorDistance;
  public double getErrorDistance(){
    return errorDistance;
  }
  
  public EstimatedItem(GeoPosition estPos, GeoPosition actPos, double errorDistance){
    this.estPos = estPos;
    this.actPos = actPos;
    this.errorDistance = errorDistance;
  }
  
  int compareTo(EstimatedItem compareTo) {
    if (errorDistance > compareTo.errorDistance)
      return 1;
    else if (errorDistance == compareTo.errorDistance)
      return 0;
    else
      return -1;
  }
}
