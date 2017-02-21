/**
 * Created by Niall Hughes on 21/02/2017.
 */

package Assignment;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Liv_Supporter extends Thread
{

    private MageeSemaphore livPassSem;
    private MageeSemaphore manuPassSem;
    private MageeSemaphore mutexSem;
    private MageeSemaphore taxiFullSem;
    private MageeSemaphore pickFromQue;

    private AtomicInteger manuPassInTaxi;
    private AtomicInteger livPassInTaxi;

    private AtomicInteger numInLivQue;

    private String threadID;

    private Activity taxiActivity;

    public Liv_Supporter(int ID, AtomicInteger manuPassInTaxi, AtomicInteger livPassInTaxi, AtomicInteger numInLivQue,
                         MageeSemaphore livPassSem, MageeSemaphore manuPassSem, MageeSemaphore mutexSem,
                         MageeSemaphore taxiFullSem, MageeSemaphore pickFromQue, Activity taxiActivity)
    {
        this.threadID = ("Liv_" + ID);
        this.manuPassInTaxi = manuPassInTaxi;
        this.livPassInTaxi = livPassInTaxi;
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
        livPassSem.P();
        mutexSem.P();
        taxiActivity.addActivity(threadID);
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
            System.out.println("Taxi Sent");
            taxiFullSem.V();
        }
        taxiActivity.printActivities();
        mutexSem.V();
    }
}
