/**
 * Created by Niall Hughes on 21/02/2017.
 */

package Assignment;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * PickQue Class
 *
 * Class used to chose who's next to get into taxi in very specific circumstances.
 */

class PickQue extends Thread
{
    private AtomicInteger numInManuQue;
    private AtomicInteger numInLivQue;
    private AtomicInteger manuPassInTaxi;
    private AtomicInteger livPassInTaxi;

    private MageeSemaphore pickFromQue;
    private MageeSemaphore manuPassSem;
    private MageeSemaphore livPassSem;
    private MageeSemaphore mutexSem;

    PickQue(AtomicInteger numInManuQue,
            AtomicInteger numInLivQue,
            AtomicInteger manuPassInTaxi,
            AtomicInteger livPassInTaxi,
            MageeSemaphore pickFromQue,
            MageeSemaphore manuPassSem,
            MageeSemaphore livPassSem,
            MageeSemaphore mutexSem)
    {
        this.numInManuQue = numInManuQue;
        this.numInLivQue = numInLivQue;
        this.manuPassInTaxi = manuPassInTaxi;
        this.livPassInTaxi = livPassInTaxi;
        this.pickFromQue = pickFromQue;
        this.manuPassSem = manuPassSem;
        this.livPassSem = livPassSem;
        this.mutexSem = mutexSem;
    }

    public void run()
    {
        while ((numInManuQue.get() != 0) || (numInLivQue.get() != 0))
        {
            pickFromQue.P();

            // Entering Critical section
            mutexSem.P();

            // Basically catches if the last 2 people in one que are in the taxi.
            if (((numInManuQue.get() - manuPassInTaxi.get()) == 0))
            {
                livPassSem.V();
            }
            else if ((numInLivQue.get() - livPassInTaxi.get()) == 0){
                manuPassSem.V();
            }
            else
            {
                Random rand = new Random();
                int r = rand.nextInt(2);
                if (r == 0)
                {
                    manuPassSem.V();
                } else
                {
                    livPassSem.V();
                }
            }

            // Leaving Critical section
            mutexSem.V();
        }
    }
}
