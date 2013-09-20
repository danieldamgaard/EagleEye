package dk.au.cs.eagleEye.tasks;

import dk.au.cs.eagleEye.main.Master;

public class FingerprintingNN {  
  public static void run(String runName, Boolean useEuclidean, Boolean writeToCsv){
    Master m = Master.Inst();

    m.ConsoleWrite("Fingerprinting: [" + runName + "] Start");
    
    FingerprintingKNN.run(runName, useEuclidean, 1, writeToCsv);
    
    m.ConsoleWrite("Fingerprinting: [" + runName + "] End");

  }
  
  public static void runCDFError(String runName, Boolean useEuclidean, int onlineSamples, int runs, Boolean writeToCsv){
    Master m = Master.Inst();

    m.ConsoleWrite("Fingerprinting: [" + runName + "] Start");
    
    FingerprintingKNN.runCDFError(runName, useEuclidean, 1, onlineSamples, runs, writeToCsv);
    
    m.ConsoleWrite("Fingerprinting: [" + runName + "] End");
  }
}
