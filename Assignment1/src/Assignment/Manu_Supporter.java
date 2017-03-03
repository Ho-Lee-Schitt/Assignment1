/**
 * Created by Niall Hughes on 21/02/2017.
 */

package Assignment;

import java.util.concurrent.atomic.AtomicInteger;

class Manu_Supporter extends Thread
{

    private MageeSemaphore livPassSem;
    private MageeSemaphore manuPassSem;
    private MageeSemaphore mutexSem;
    private MageeSemaphore taxiFullSem;
    private MageeSemaphore pickFromQue;

    private AtomicInteger manuPassInTaxi;
    private AtomicInteger livPassInTaxi;

    private String threadID;

    private Activity taxiActivity;

    Manu_Supporter(int ID,
                          AtomicInteger manuPassInTaxi,
                          AtomicInteger livPassInTaxi,
                          MageeSemaphore livPassSem,
                          MageeSemaphore manuPassSem,
                          MageeSemaphore mutexSem,
                          MageeSemaphore taxiFullSem,
                          MageeSemaphore pickFromQue,
                          Activity taxiActivity)
    {
        this.threadID = ("Manu_" + ID);
        this.manuPassInTaxi = manuPassInTaxi;
        this.livPassInTaxi = livPassInTaxi;
        this.livPassSem = livPassSem;
        this.manuPassSem = manuPassSem;
        this.mutexSem = mutexSem;
        this.taxiFullSem = taxiFullSem;
        this.pickFromQue = pickFromQue;
        this.taxiActivity = taxiActivity;
    }

    public void run()
    {
        // Grab Manu supporter semaphore
        manuPassSem.P();

        /*
         * Grab critical mutex
         * In Critical Section
         */
        mutexSem.P();

        // Thread Logged
        taxiActivity.addActivity(threadID);

        // Decide who goes next
        if ((manuPassInTaxi.incrementAndGet() + livPassInTaxi.get()) < 4)
        {
            if (manuPassInTaxi.get() == 3)
            {
                manuPassSem.V();
            } else if (livPassInTaxi.get() == 1 && manuPassInTaxi.get() == 2)
            {
                livPassSem.V();
            } else if (livPassInTaxi.get() == 2 && manuPassInTaxi.get() == 1)
            {
                manuPassSem.V();
            } else
            {
                pickFromQue.V();
            }
        } else
        {
            // Taxi is ready to roll
            taxiFullSem.V();
        }

        // Print the taxi
        taxiActivity.printActivities();

        // Leave critical section
        mutexSem.V();
    }
}
