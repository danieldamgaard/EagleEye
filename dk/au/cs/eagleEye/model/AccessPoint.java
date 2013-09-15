/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.au.cs.eagleEye.model;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;

/**
 *
 * @author GalaxyNetworks
 */
public class AccessPoint {
  private MACAddress macAddress;
  private GeoPosition geoPosition;

  public AccessPoint(MACAddress macAddress, GeoPosition geoPosition) {
    this.macAddress = macAddress;
    this.geoPosition = geoPosition;
  }

  public void setMacAddress(MACAddress macAddress) {
    this.macAddress = macAddress;
  }

  public void setGeoPosition(GeoPosition geoPosition) {
    this.geoPosition = geoPosition;
  }

  public MACAddress getMacAddress() {
    return macAddress;
  }

  public GeoPosition getGeoPosition() {
    return geoPosition;
  }
}
