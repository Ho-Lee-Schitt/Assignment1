/**
 * Created by Niall Hughes on 19/02/2017.
 */

package Assignment;

import java.util.concurrent.atomic.AtomicInteger;

public class Question2
{
    public static void main(String[] args)
    {
        // Define variables
        final int MANU_PASSENGERS = 12;
        final int LIV_PASSENGERS = 12;
        AtomicInteger manuPassInTaxi = new AtomicInteger();
        AtomicInteger livPassInTaxi = new AtomicInteger();

        AtomicInteger numInManuQue = new AtomicInteger(MANU_PASSENGERS);
        AtomicInteger numInLivQue = new AtomicInteger(LIV_PASSENGERS);

        // Define Semaphores
        MageeSemaphore mutexSem = new MageeSemaphore(1);

        MageeSemaphore manuPassSem = new MageeSemaphore(0);
        MageeSemaphore livPassSem = new MageeSemaphore(0);

        MageeSemaphore taxiFullSem = new MageeSemaphore(0);
        MageeSemaphore pickFromQue = new MageeSemaphore(1);

        // Create Taxi Logger
        Activity taxiActivity = new Activity();

        // Create Taxi Thread
        Taxi taxiCar = new Taxi(manuPassInTaxi,livPassInTaxi, numInManuQue, numInLivQue,
                mutexSem, taxiFullSem, pickFromQue, taxiActivity);

        // Start Taxi Thread
        taxiCar.start();

        // Create team picker thread
        PickQue teamQuePicker = new PickQue(numInManuQue, numInLivQue, manuPassInTaxi, livPassInTaxi, pickFromQue,
                manuPassSem, livPassSem, mutexSem);

        // Start picker thread
        teamQuePicker.start();

        // Create and Start Manu Threads
        for (int i = 1; i <= MANU_PASSENGERS; i++)
        {
            Manu_Supporter supporter = new Manu_Supporter(i, manuPassInTaxi, livPassInTaxi, livPassSem,
                    manuPassSem, mutexSem, taxiFullSem, pickFromQue, taxiActivity);
            supporter.start();
        }

        // Create and Start Liverpool Supporters
        for (int i = 1; i <= LIV_PASSENGERS; i++)
        {
            Liv_Supporter supporter = new Liv_Supporter(i, manuPassInTaxi, livPassInTaxi, livPassSem,
                    manuPassSem, mutexSem, taxiFullSem, pickFromQue, taxiActivity);
            supporter.start();
        }
    }
}


