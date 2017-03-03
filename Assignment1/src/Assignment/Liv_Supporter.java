/**
 * Created by Niall Hughes on 21/02/2017.
 */

package Assignment;

import java.util.concurrent.atomic.AtomicInteger;

class Liv_Supporter extends Thread
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

    Liv_Supporter(int ID,
                         AtomicInteger manuPassInTaxi,
                         AtomicInteger livPassInTaxi,
                         MageeSemaphore livPassSem,
                         MageeSemaphore manuPassSem,
                         MageeSemaphore mutexSem,
                         MageeSemaphore taxiFullSem,
                         MageeSemaphore pickFromQue,
                         Activity taxiActivity)
    {
        this.threadID = ("Liv_" + ID);
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
        // Grab Liv supporter semaphore
        livPassSem.P();

        /*
         * Grab critical mutex
         * In Critical Section
         */
        mutexSem.P();

        // Thread LOgged
        taxiActivity.addActivity(threadID);

        // Decide who goes next
        if ((manuPassInTaxi.get() + livPassInTaxi.incrementAndGet()) < 4)
        {
            if (livPassInTaxi.get() == 3)
            {
                livPassSem.V();
            } else if (livPassInTaxi.get() == 2 && manuPassInTaxi.get() == 1)
            {
                manuPassSem.V();
            } else if (livPassInTaxi.get() == 1 && manuPassInTaxi.get() == 2)
            {
                livPassSem.V();
            } else
            {
                pickFromQue.V();
            }
        } else
        {
            // Taxi full
            taxiFullSem.V();
        }

        // Log Taxi
        taxiActivity.printActivities();

        // Leaving critical section
        mutexSem.V();
    }
}
