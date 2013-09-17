package dk.au.cs.eagleEye.tasks;

import dk.au.cs.eagleEye.algorithms.*;
import dk.au.cs.eagleEye.main.Master;
import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.trace.TraceEntry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KNNPositionEstimator {
  public static void doSimulation(int numberOfSimulations, int numberOfNearest, String simulationName, boolean isOfflineTrace) {
    List<String> resultList = new ArrayList<String>();
    Master m = Master.Inst();

    for (int i = 1; i <= numberOfSimulations; i++) {
      resultList.addAll(KNNPositionEstimator.estimatePositions(numberOfNearest, isOfflineTrace));
      m.Debug(1, i + " of " + numberOfSimulations + " simulations done");
      break;
    }

     writeResultsToDisk(simulationName, resultList);
  }

  private static void writeResultsToDisk(String simulationName, List<String> resultList) {
    // Save to disk
    File outputFile = new File("data/" + simulationName + ".csv");

    try {
      outputFile.createNewFile();

      FileWriter fileWriter = new FileWriter(outputFile.getAbsoluteFile());
      BufferedWriter  bufferedWriter = new BufferedWriter(fileWriter);

      bufferedWriter.write("# estimated x; estimated y; actual x; actual y; error distance (1 decimal)");
      bufferedWriter.newLine();
      for (String result : resultList) {
        bufferedWriter.write(result);
        bufferedWriter.newLine();
      }

      bufferedWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static List<String> estimatePositions(int numberOfNearest, boolean isOfflineTrace) {
    Master m = Master.Inst();
    List<String> resultList = new ArrayList<String>();

    //DistanceAlgorithm dAlgorithm = new Euclidean();
    DistanceAlgorithm dAlgorithm = new Manhattan();
    LocalizationAlgorithm lAlgorithm = new IgnoreAP(dAlgorithm);

    m.generate();
    if (isOfflineTrace)
      m.setBaseTrace(m.getOfflineTrace());

    m.Debug(1, m.getOfflineTrace().size() + "");
    for(TraceEntry entry: m.getOnlineTrace()) {
      KNearestNeighbor nn = new KNearestNeighbor(entry, lAlgorithm, numberOfNearest, true);

      GeoPosition pos = nn.Position();

      String status = "Estimated "+pos+" , Actual: "+entry.getGeoPosition();
      m.Debug(4, status);

      double errorDistanceSquared = Math.pow(pos.getX()-entry.getGeoPosition().getX(), 2) +
              Math.pow(pos.getY()-entry.getGeoPosition().getY(), 2);
      double errorDistance = Math.sqrt(errorDistanceSquared);

      String result = pos.getX() + ";" + pos.getY() + ";"
              + entry.getGeoPosition().getX() + ";" + entry.getGeoPosition().getY() + ";"
              + errorDistance;
      resultList.add(result);
    }

    return resultList;
  }
}
