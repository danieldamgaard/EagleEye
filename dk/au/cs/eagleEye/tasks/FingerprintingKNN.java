package dk.au.cs.eagleEye.tasks;

import dk.au.cs.eagleEye.algorithms.DistanceAlgorithm;
import dk.au.cs.eagleEye.algorithms.Euclidean;
import dk.au.cs.eagleEye.algorithms.IgnoreAP;
import dk.au.cs.eagleEye.algorithms.KNearestNeighbor;
import dk.au.cs.eagleEye.algorithms.LocalizationAlgorithm;
import dk.au.cs.eagleEye.main.Master;

import java.util.ArrayList;
import java.util.List;
import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.trace.TraceEntry;

public class FingerprintingKNN {
  public static void run(int numberOfNearest){
    Master m = Master.Inst();
    m.ConsoleWrite("[FingerprintingKNN] Start, with k = " + numberOfNearest);

    m.setBaseTrace(m.getOfflineTrace());
    KNNPositionEstimator.doSimulation(100, numberOfNearest, "offlineFingerprintKNN", true);

    m.ConsoleWrite("[FingerprintingKNN] End");
  }
}
