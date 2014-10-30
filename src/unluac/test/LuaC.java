package unluac.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LuaC {

  public static void compile(String in, String out) throws IOException {
    ProcessBuilder pb = new ProcessBuilder("luac", "-o", out, in);
    //ProcessBuilder pb = new ProcessBuilder("C:\\LuaTest\\bin\\luac515_single.exe", "-o", out, in);
    pb.directory(null);
    Process p = pb.start();
    while(true) {
      try {
        if(p.waitFor() == 0) {
          return;
        } else {
          BufferedReader r = new BufferedReader(new InputStreamReader(p.getErrorStream()));
          String line = null;
          do {
            line = r.readLine();
            if(line != null) {
              System.err.println(line);
            }
          } while(line != null);
            
          throw new IOException("luac failed on file: " + in);
        }
      } catch(InterruptedException e ) {
        
      }
    } 
  }
  
}
