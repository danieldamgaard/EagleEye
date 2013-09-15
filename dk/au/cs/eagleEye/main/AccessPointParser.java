/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.au.cs.eagleEye.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import dk.au.cs.eagleEye.model.AccessPoint;
import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;

/**
 *
 * @author GalaxyNetworks
 */
public class AccessPointParser {
  private File file;  
  
  public AccessPointParser(File file){
    this.file = file;
  }
  
  public ArrayList<AccessPoint> parse() throws FileNotFoundException{
    ArrayList<AccessPoint> list = new ArrayList<AccessPoint>();
		
    BufferedReader in = new BufferedReader(new FileReader(file));
    String line;

    try {
      // Process each line.
      while ((line = in.readLine()) != null) {
        if (line.startsWith("#"))
                continue;
        
        int macPosSplit = line.indexOf(' ');
        
        MACAddress macAddress = MACAddress.parse(line.substring(0, macPosSplit));
        GeoPosition geoPos = GeoPosition.parse(line.substring(macPosSplit).trim());
        
        list.add(new AccessPoint(macAddress, geoPos));
      }
    } catch (Exception e) {

    }
				
    return list;
  }
}
