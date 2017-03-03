/**
 * Created by Niall Hughes on 21/02/2017.
 */

package Assignment;

import java.util.concurrent.atomic.AtomicInteger;

class Taxi extends Thread
{
    private MageeSemaphore taxiFullSem;
    private MageeSemaphore mutexSem;
    private MageeSemaphore pickFromQue;

    private AtomicInteger manuPassInTaxi;
    private AtomicInteger livPassInTaxi;

    private AtomicInteger numInManuQue;
    private AtomicInteger numInLivQue;

    private Activity taxiActivity;

    Taxi(AtomicInteger manuPassInTaxi,
                AtomicInteger livPassInTaxi,
                AtomicInteger numInManuQue,
                AtomicInteger numInLivQue,
                MageeSemaphore mutexSem,
                MageeSemaphore taxiFullSem,
                MageeSemaphore pickFromQue,
                Activity taxiActivity)
    {
        this.manuPassInTaxi = manuPassInTaxi;
        this.livPassInTaxi = livPassInTaxi;
        this.numInManuQue = numInManuQue;
        this.numInLivQue = numInLivQue;
        this.mutexSem = mutexSem;
        this.taxiFullSem = taxiFullSem;
        this.pickFromQue = pickFromQue;
        this.taxiActivity = taxiActivity;
    }

    public void run()
    {
        // While there's people in the Que.
        while ((numInManuQue.get() != 0) || (numInLivQue.get() != 0))
        {
            // Grab taxi sem
            taxiFullSem.P();
            taxiActivity.printActivities();

            // Entering Critical Section
            mutexSem.P();

            // Wait for a bit to simulate the journey
            CDS.idleQuietly(100);

            // Passengers get out
            for (int i = 0; i < manuPassInTaxi.get(); i++)
            {
                numInManuQue.decrementAndGet();
            }
            for (int i = 0; i < livPassInTaxi.get(); i++)
            {
                numInLivQue.decrementAndGet();
            }

            // Clear taxi
            taxiActivity.removeAllActivities();

            manuPassInTaxi.set(0);
            livPassInTaxi.set(0);

            // Pick next passenger
            pickFromQue.V();

            taxiActivity.printActivities();

            // Leave Critical Section
            mutexSem.V();
        }
        System.out.println("No more waiting on Taxi.");
    }
}
