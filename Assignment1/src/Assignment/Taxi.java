/**
 * Created by Niall Hughes on 21/02/2017.
 */

package Assignment;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Taxi extends Thread
{
    private MageeSemaphore taxiFullSem;
    private MageeSemaphore manuPassSem;
    private MageeSemaphore livPassSem;
    private MageeSemaphore mutexSem;
    private MageeSemaphore pickFromQue;

    private AtomicInteger manuPassInTaxi;
    private AtomicInteger livPassInTaxi;

    private AtomicInteger numInManuQue;
    private AtomicInteger numInLivQue;

    private Activity taxiActivity;

    public Taxi(AtomicInteger manuPassInTaxi, AtomicInteger livPassInTaxi, AtomicInteger numInManuQue,
                AtomicInteger numInLivQue, MageeSemaphore livPassSem, MageeSemaphore manuPassSem,
                MageeSemaphore mutexSem, MageeSemaphore taxiFullSem, MageeSemaphore pickFromQue, Activity taxiActivity)
    {
        this.manuPassInTaxi = manuPassInTaxi;
        this.livPassInTaxi = livPassInTaxi;
        this.numInManuQue = numInManuQue;
        this.numInLivQue = numInLivQue;
        this.livPassSem = livPassSem;
        this.manuPassSem = manuPassSem;
        this.mutexSem = mutexSem;
        this.taxiFullSem = taxiFullSem;
        this.pickFromQue = pickFromQue;
        this.taxiActivity = taxiActivity;
    }

    public void run()
    {
        while ((numInManuQue.get() != 0) || (numInLivQue.get() != 0))
        {
            taxiFullSem.P();
            taxiActivity.printActivities();
            mutexSem.P();
            // Wait for a bit to simulate the journey
            CDS.idleQuietly(100);

            for (int i = 0; i < manuPassInTaxi.get(); i++)
            {
                numInManuQue.decrementAndGet();
            }
            for (int i = 0; i < livPassInTaxi.get(); i++)
            {
                numInLivQue.decrementAndGet();
            }

            taxiActivity.removeAllActivities();

            manuPassInTaxi.set(0);
            livPassInTaxi.set(0);

            pickFromQue.V();

            taxiActivity.printActivities();
            mutexSem.V();
        }
        System.out.println("No more waiting on Taxi.");
    }
}
