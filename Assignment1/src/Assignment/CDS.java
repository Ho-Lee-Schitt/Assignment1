/**
 * Created by Niall Hughes on 21/02/2017.
 */

package Assignment;

//CDS.java
public class  CDS {

    public static void idleQuietly(int millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (InterruptedException e) { }
    }
} // end CDS
