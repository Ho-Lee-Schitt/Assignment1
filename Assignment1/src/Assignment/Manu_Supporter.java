/**
 * Created by Niall Hughes on 21/02/2017.
 */

package Assignment;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Manu_Supporter extends Thread
{

    private MageeSemaphore livPassSem;
    private MageeSemaphore manuPassSem;
    private MageeSemaphore mutexSem;
    private MageeSemaphore taxiFullSem;
    private MageeSemaphore pickFromQue;

    private AtomicInteger manuPassInTaxi;
    private AtomicInteger livPassInTaxi;

    private AtomicInteger numInManuQue;

    private String threadID;

    private Activity taxiActivity;

    public Manu_Supporter(int ID, AtomicInteger manuPassInTaxi, AtomicInteger livPassInTaxi,
                          AtomicInteger numInManuQue, MageeSemaphore livPassSem, MageeSemaphore manuPassSem,
                          MageeSemaphore mutexSem, MageeSemaphore taxiFullSem, MageeSemaphore pickFromQue,
                          Activity taxiActivity)
    {
        this.threadID = ("Manu_" + ID);
        this.manuPassInTaxi = manuPassInTaxi;
        this.livPassInTaxi = livPassInTaxi;
        this.numInManuQue = numInManuQue;
        this.livPassSem = livPassSem;
        this.manuPassSem = manuPassSem;
        this.mutexSem = mutexSem;
        this.taxiFullSem = taxiFullSem;
        this.pickFromQue = pickFromQue;
        this.taxiActivity = taxiActivity;
    }

    public void run()
    {
        manuPassSem.P();
        mutexSem.P();
        taxiActivity.addActivity(threadID);
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
            taxiFullSem.V();
        }
        taxiActivity.printActivities();
        mutexSem.V();
    }
}
