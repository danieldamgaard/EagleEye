package dk.au.cs.eagleEye.tasks;

import dk.au.cs.eagleEye.algorithms.ModelBuilder;
import dk.au.cs.eagleEye.main.Master;
import java.util.List;
import org.pi4.locutil.trace.TraceEntry;

public class ModelNN {
  public static void run(String runName, Boolean useEuclidean, double n, double pd0, double d0, double waf, int c, Boolean writeToCsv){
    Master m = Master.Inst();
    m.ConsoleWrite("Model: [" + runName + "] Start");

    ModelBuilder mBuilder = new ModelBuilder();

    List<TraceEntry> modelFingerprints = mBuilder.generateModel(n, pd0, d0, waf, c);
    m.setBaseTrace(modelFingerprints);
    
    FingerprintingNN.run(runName, useEuclidean, writeToCsv);
    
    m.ConsoleWrite("Model: [" + runName + "] End");
  }
  
  public static void runCDFError(String runName, Boolean useEuclidean, int onlineSamples, double n, double pd0, double d0, double waf, int c, int runs, Boolean writeToCsv){
    Master m = Master.Inst();

    m.ConsoleWrite("Model: [" + runName + "] Start");
    
    ModelBuilder mBuilder = new ModelBuilder();

    List<TraceEntry> modelFingerprints = mBuilder.generateModel(n, pd0, d0, waf, c);
    m.setBaseTrace(modelFingerprints);
    
    FingerprintingNN.runCDFError(runName, useEuclidean, onlineSamples, runs, writeToCsv);
    
    m.ConsoleWrite("Model: [" + runName + "] End");
  }
}
