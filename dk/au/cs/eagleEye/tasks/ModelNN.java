package dk.au.cs.eagleEye.tasks;

import dk.au.cs.eagleEye.algorithms.ModelBuilder;
import dk.au.cs.eagleEye.main.Master;
import java.util.List;
import org.pi4.locutil.trace.TraceEntry;

public class ModelNN {
  public static void run(double n, double pd0, double d0, double waf, int c){
    Master m = Master.Inst();
    m.ConsoleWrite("[ModelNN] Start");

    ModelBuilder mBuilder = new ModelBuilder();

    List<TraceEntry> modelFingerprints = mBuilder.generateModel(n, pd0, d0, waf, c);
    m.setBaseTrace(modelFingerprints);
    KNNPositionEstimator.doSimulation(100, 1, "modelFingerprintNN", false);
    
    m.ConsoleWrite("[ModelNN] End");
  }
}
