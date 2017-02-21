/**
 * Created by Niall Hughes on 21/02/2017.
 */

package Assignment;

import java.util.concurrent.Semaphore;

//Assignment.MageeSemaphore.java
//This is an implementation of the traditional (counting) Semaphore with P() and V() operations
public class MageeSemaphore
{
    private Semaphore sem;

    public MageeSemaphore(int initialCount)
    {
        sem = new Semaphore(initialCount);
    } // end constructor

    public void P()
    {
        try
        {
            sem.acquire();
        } catch (InterruptedException ex)
        {
            System.out.println("Interrupted when waiting");
        }
    } // end P()

    public void V()
    {
        sem.release();
    } // end V()

} // end Assignment.MageeSemaphore