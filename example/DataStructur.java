package example;

import java.util.List;
import main.Master;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.trace.SignalStrengthSamples;
import org.pi4.locutil.trace.TraceEntry;

public class DataStructur {
    public static void Run(){
        Master m = Master.Inst();
        
        for(TraceEntry entry: m.getOnlineTrace()) {
            System.out.println(
                "[TraceEntry] "+
                "GeoPos: "+entry.getGeoPosition()+", "+
                "Id / MAC: "+entry.getId()+", "+
                "Speed: "+entry.getSpeed()+", "+
                "Time: "+entry.getTimestamp()+"."
            );
            
            SignalStrengthSamples sss = entry.getSignalStrengthSamples();
            
            List<MACAddress> aps = sss.getSortedAccessPoints();
            
            for(MACAddress mac : aps){
                System.out.println(
                    "[AccessPoint] "+
                    "Id / MAC: "+mac+", "+
                    "Strengths: "+sss.getSignalStrengthValues(mac)+", "+
                    "Noises: "+sss.getNoiseValues(mac)+", "+
                    "Avg. strength: "+sss.getAverageSignalStrength(mac)+", "+
                    "Avg. noise: "+sss.getAverageNoise(mac)+", "+
                    "Deviation: "+sss.getStandardDeviationSignalStrength(mac)+", "+
                    "Variance: "+sss.getVarianceSignalStrength(mac)
                );
            }
        }
    }
}
