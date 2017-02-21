/**
 * Created by Niall Hughes on 19/02/2017.
 */

package Assignment;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Question2
{
    public static void main(String[] args)
    {
        final int MANU_PASSENGERS = 12;
        final int LIV_PASSENGERS = 12;
        AtomicInteger manuPassInTaxi = new AtomicInteger();
        AtomicInteger livPassInTaxi = new AtomicInteger();

        AtomicInteger numInManuQue = new AtomicInteger(MANU_PASSENGERS);
        AtomicInteger numInLivQue = new AtomicInteger(LIV_PASSENGERS);

        //Assignment.MageeSemaphore taxiPassengers = new Assignment.MageeSemaphore(4);
        MageeSemaphore mutexSem = new MageeSemaphore(1);

        MageeSemaphore manuPassSem = new MageeSemaphore(0);
        MageeSemaphore livPassSem = new MageeSemaphore(0);

        MageeSemaphore taxiFullSem = new MageeSemaphore(0);
        MageeSemaphore pickFromQue = new MageeSemaphore(1);

        Activity taxiActivity = new Activity();

        Taxi taxiCar = new Taxi(manuPassInTaxi, livPassInTaxi, numInManuQue, numInLivQue, livPassSem, manuPassSem,
                mutexSem, taxiFullSem, pickFromQue, taxiActivity);
        taxiCar.start();

        PickQue teamQuePicker = new PickQue(numInManuQue, numInLivQue, manuPassInTaxi, livPassInTaxi, pickFromQue,
                manuPassSem, livPassSem, mutexSem);
        teamQuePicker.start();

        for (int i = 1; i <= MANU_PASSENGERS; i++)
        {
            Manu_Supporter supporter = new Manu_Supporter(i, manuPassInTaxi, livPassInTaxi, numInManuQue, livPassSem,
                    manuPassSem, mutexSem, taxiFullSem, pickFromQue, taxiActivity);
            supporter.start();
        }
        for (int i = 1; i <= LIV_PASSENGERS; i++)
        {
            Liv_Supporter supporter = new Liv_Supporter(i, manuPassInTaxi, livPassInTaxi, numInLivQue, livPassSem,
                    manuPassSem, mutexSem, taxiFullSem, pickFromQue, taxiActivity);
            supporter.start();
        }
    }
}


