package dk.au.cs.eagleEye.tasks;

import dk.au.cs.eagleEye.algorithms.ModelBuilder;
import dk.au.cs.eagleEye.main.Master;
import java.util.List;
import org.pi4.locutil.trace.TraceEntry;

public class ModelKNN {
  public static void run(int k, double n, double pd0, double d0, double waf, int c){
    Master m = Master.Inst();
    m.ConsoleWrite("[ModelKNN] Start");

    ModelBuilder mBuilder = new ModelBuilder();
    
    
    List<TraceEntry> modelFingerprints = mBuilder.generateModel(n, pd0, d0, 0, 0);
    
    FingerprintingKNN.run(modelFingerprints, k);
    
    m.ConsoleWrite("[ModelKNN] End");
  }
}
