package main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.pi4.locutil.io.TraceGenerator;
import org.pi4.locutil.trace.Parser;
import org.pi4.locutil.trace.TraceEntry;

public class Master {
    private static Master inst;
    
    public static Master Inst(){
        if(inst == null){
            inst = new Master();
        }
        
        return inst;
    }
    
    // --
    
    private Master(){
        TraceGenerator();
    }
    
    private void TraceGenerator(){
        offlinePath = "data/MU.1.5meters.offline.trace";
        onlinePath = "data/MU.1.5meters.online.trace";
        
        File offlineFile = new File(offlinePath);
        Parser offlineParser = new Parser(offlineFile);
        //System.out.println("Offline File: " +  offlineFile.getAbsoluteFile());

        File onlineFile = new File(onlinePath);
        Parser onlineParser = new Parser(onlineFile);
        //System.out.println("Online File: " + onlineFile.getAbsoluteFile());
        
        try {
            int offlineSize = 25;
            int onlineSize = 5;
            tg = new TraceGenerator(offlineParser, onlineParser ,offlineSize, onlineSize);
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
        
        Generate();
    }
    
    public void Generate(){
        tg.generate();
        
        offlineTrace = tg.getOffline();
        onlineTrace = tg.getOnline();
    }
    
    public List<TraceEntry> getOfflineTrace(){
        return offlineTrace;
    }
    
    public List<TraceEntry> getOnlineTrace(){
        return onlineTrace;
    }
    
    private String offlinePath;
    private String onlinePath;
    private TraceGenerator tg;
    private List<TraceEntry> offlineTrace;
    private List<TraceEntry> onlineTrace;
}
