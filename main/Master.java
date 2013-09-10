package main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JTextArea;
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
    
    public void setConsole(JTextArea console){
        this.console = console;
    }
    
    private Master(){
        debugLevel = 4; // Info (Alt)
        
        TraceGenerator();
    }
    
    private void TraceGenerator(){
        offlinePath = "data/MU.1.5meters.offline.trace";
        onlinePath = "data/MU.1.5meters.online.trace";
        
        File offlineFile = new File(offlinePath);
        Parser offlineParser = new Parser(offlineFile);
        Debug(4, "Offline File: " +  offlineFile.getAbsoluteFile());

        File onlineFile = new File(onlinePath);
        Parser onlineParser = new Parser(onlineFile);
        Debug(4, "Online File: " + onlineFile.getAbsoluteFile());
        
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
    
    public void ConsoleWriteBase(String message){
        console.append(message);
    }
    
    public void ConsoleWrite(String message){
        Date myDate = new Date();
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String myDateString = myFormat.format(myDate);
        
        ConsoleWriteBase("["+myDateString+"] "+message+"\n");
    }
    
    public void ConsoleWriteError(String message){
        ConsoleWrite("[Error] "+message);
    }
    
    public boolean Debug(int debugLevel){
        return debugLevel <= this.debugLevel;
    }
    
    public void Debug(int debugLevel, String message){
        if(Debug(debugLevel)){
            System.out.println(message);
        }
    }
    
    private String offlinePath;
    private String onlinePath;
    private TraceGenerator tg;
    private List<TraceEntry> offlineTrace;
    private List<TraceEntry> onlineTrace;
    private JTextArea console;
    private int debugLevel; // 0: None, 1: Fatal-errors, 2: Errors, 3: Warnings, 4: Info
}
