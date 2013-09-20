package dk.au.cs.eagleEye.tasks;

import dk.au.cs.eagleEye.algorithms.DistanceAlgorithm;
import dk.au.cs.eagleEye.algorithms.Euclidean;
import dk.au.cs.eagleEye.algorithms.IgnoreAP;
import dk.au.cs.eagleEye.algorithms.KNearestNeighbor;
import dk.au.cs.eagleEye.algorithms.LocalizationAlgorithm;
import dk.au.cs.eagleEye.algorithms.Manhattan;
import dk.au.cs.eagleEye.main.CDFErrorCalculator;
import dk.au.cs.eagleEye.main.Master;
import dk.au.cs.eagleEye.main.MedianAvgCalculator;
import dk.au.cs.eagleEye.main.ResultLogger;
import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.trace.TraceEntry;


public class FingerprintingKNN {
  public static void run(String runName, Boolean useEuclidean, int numberOfNearest, Boolean writeToCsv){
    Master m = Master.Inst();
    m.ConsoleWrite("Fingerprinting: [" + runName + "] Start, with k = " + numberOfNearest);
        
    try{
      // Setup Result Logger!
      ResultLogger logger = new ResultLogger();
      logger.OpenLogger(runName, writeToCsv);
      logger.WriteComment("estimated x; estimated y; actual x; actual y; error distance");

      // Run the algorithm! And write results!
      DistanceAlgorithm dAlgorithm = new Manhattan();
      if (useEuclidean){
        dAlgorithm = new Euclidean();
      }
      
      LocalizationAlgorithm lAlgorithm = new IgnoreAP(dAlgorithm);

      m.Debug(1, m.getOfflineTrace().size() + "");
      for(TraceEntry entry: m.getOnlineTrace()) {
        KNearestNeighbor nn = new KNearestNeighbor(entry, lAlgorithm, numberOfNearest, true);

        GeoPosition pos = nn.Position();

        String status = "Estimated "+pos+" , Actual: "+entry.getGeoPosition();
        m.Debug(4, status);

        double errorDistanceSquared = Math.pow(pos.getX()-entry.getGeoPosition().getX(), 2) +
                Math.pow(pos.getY()-entry.getGeoPosition().getY(), 2);
        double errorDistance = Math.sqrt(errorDistanceSquared);

        logger.WriteRow(new Object[]{
          pos.getX(),
          pos.getY(),
          entry.getGeoPosition().getX(),
          entry.getGeoPosition().getY(),
          errorDistance
        }, ';');
      }
    } catch (Exception e){
      m.ConsoleWrite("Fingerprinting Failed!: " + e.getMessage());
    }
    
    //KNNPositionEstimator.doSimulation(100, numberOfNearest, "offlineFingerprintKNN", true);

    m.ConsoleWrite("Fingerprinting: [" + runName + "] End");
  }
  
  public static void runCDFError(String runName, Boolean useEuclidean, int numberOfNearest, int onlineSamples, int runs, Boolean writeToCsv){
    Master m = Master.Inst();
    m.ConsoleWrite("Fingerprinting: [" + runName + "] Start, with k = " + numberOfNearest);
            
    try{
      // Setup Result Logger!
      ResultLogger logger = new ResultLogger();
      logger.OpenLogger(runName, writeToCsv);
      logger.WriteComment("interval; count;");

      // Run the algorithm! And write results!
      DistanceAlgorithm dAlgorithm = new Manhattan();
      if (useEuclidean){
        dAlgorithm = new Euclidean();
      }
      
      LocalizationAlgorithm lAlgorithm = new IgnoreAP(dAlgorithm);

      CDFErrorCalculator cdfErrorCalc = new CDFErrorCalculator(0.1);
      
      m.Debug(1, m.getOfflineTrace().size() + "");
      for (int runIndex = 0; runIndex < runs; runIndex++){
        m.generate();
        
        for(int i = 0; i < m.getOnlineTrace().size() || i < onlineSamples; i = i + m.getOnlineTrace().size() / onlineSamples) {
          TraceEntry entry = m.getOnlineTrace().get(i);

          KNearestNeighbor nn = new KNearestNeighbor(entry, lAlgorithm, numberOfNearest, true);

          GeoPosition pos = nn.Position();

          String status = "Estimated "+pos+" , Actual: "+entry.getGeoPosition();
          m.Debug(4, status);

          double errorDistanceSquared = Math.pow(pos.getX()-entry.getGeoPosition().getX(), 2) +
                  Math.pow(pos.getY()-entry.getGeoPosition().getY(), 2);
          double errorDistance = Math.sqrt(errorDistanceSquared);

          cdfErrorCalc.add(errorDistance);
        }
      }
      
      for (Object[] resultRow : cdfErrorCalc.getResultSet()){
        logger.WriteRow(resultRow, ';');
      }
      
      logger.CloseLogger();
    } catch (Exception e){
      m.ConsoleWrite("Fingerprinting Failed!: " + e.getMessage());
    }

    m.ConsoleWrite("Fingerprinting: [" + runName + "] End");
  }
  
  public static void runMedianAvg(String runName, Boolean useEuclidean, int kValues[], int runs, Boolean writeToCsv){
    Master m = Master.Inst();

    m.ConsoleWrite("Fingerprinting: [" + runName + "] Start, with k = selected range!");

    try {
      // Setup Result Logger!
      ResultLogger logger = new ResultLogger();
      logger.OpenLogger(runName, writeToCsv);
      logger.WriteComment("k value; avg median over " + runs + " runs;");

      for (int k : kValues){
        // Run the algorithm! And write results!
        DistanceAlgorithm dAlgorithm = new Manhattan();
        if (useEuclidean){
          dAlgorithm = new Euclidean();
        }

        LocalizationAlgorithm lAlgorithm = new IgnoreAP(dAlgorithm);

        double medianSumOverRuns = 0;
        for (int runIndex = 0; runIndex < runs; runIndex++){
          MedianAvgCalculator medianAvgCalculator = new MedianAvgCalculator();

          m.generate();
          m.Debug(1, "Run: " + runIndex + ", k = " + k);
          for(TraceEntry entry : m.getOnlineTrace()) {
            KNearestNeighbor nn = new KNearestNeighbor(entry, lAlgorithm, k, true);

            GeoPosition pos = nn.Position();

            String status = "Estimated " + pos + " , Actual: "+entry.getGeoPosition();
            m.Debug(4, status);

            double errorDistanceSquared = Math.pow(pos.getX()-entry.getGeoPosition().getX(), 2) +
                    Math.pow(pos.getY()-entry.getGeoPosition().getY(), 2);
            double errorDistance = Math.sqrt(errorDistanceSquared);

            medianAvgCalculator.add(errorDistance);
          }

          medianSumOverRuns += medianAvgCalculator.getAvg();
        }

        logger.WriteRow(new Object[]{
          k,
          medianSumOverRuns / runs
        }, ';');
      }

      logger.CloseLogger();
    } catch (Exception e){
      m.ConsoleWrite("Fingerprinting Failed!: " + e.getMessage());
    }

    m.ConsoleWrite("Fingerprinting: [" + runName + "] End");
  }
}
