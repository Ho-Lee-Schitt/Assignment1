import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by Niall Hughes on 19/02/2017.
 */
public class Question1a {
    int manuPassengers = 12;
    int livPassengers = 12;
    int manuPassInTaxi = 0;
    int livPassInTaxi = 0;

    int numInManuQue = manuPassengers;
    int numInLivQue = livPassengers;

    //MageeSemaphore taxiPassengers = new MageeSemaphore(4);
    MageeSemaphore mutexSem = new MageeSemaphore(1);

    MageeSemaphore manuPassSem;
    MageeSemaphore livPassSem;

    MageeSemaphore taxiFullSem = new MageeSemaphore(0);
}

class manuSup extends Thread {

    MageeSemaphore livPassSem;
    MageeSemaphore manuPassSem;
    MageeSemaphore mutexSem;
    MageeSemaphore taxiFullSem;

    int manuPassInTaxi;
    int livPassInTaxi;

    int numInManuQue;

    public manuSup (int manuPassInTaxi, int livPassInTaxi, int numInManuQue, MageeSemaphore livPassSem, MageeSemaphore manuPassSem, MageeSemaphore mutexSem, MageeSemaphore taxiFullSem) {
        this.manuPassInTaxi = manuPassInTaxi;
        this. livPassInTaxi = livPassInTaxi;
        this.numInManuQue = numInManuQue;
        this.livPassSem = livPassSem;
        this.manuPassSem = manuPassSem;
        this.mutexSem = mutexSem;
        this.taxiFullSem = taxiFullSem;
    }

    public void run () {
        livPassSem.P();
        mutexSem.P();
        manuPassInTaxi++;
        numInManuQue--;
        if ((manuPassInTaxi + livPassInTaxi) < 4) {
            if (manuPassInTaxi == 3) {
                manuPassSem.V();
            }
            else if (livPassInTaxi == 1 && manuPassInTaxi == 2) {
                livPassSem.V();
            }
            else if (livPassInTaxi == 2 && manuPassInTaxi == 1) {
                manuPassSem.V();
            }
            else {
                Random rand = new Random();
                int r = rand.nextInt(2);
                if (r == 0) {
                    manuPassSem.V();
                }
                else
                {
                    livPassSem.V();
                }
            }
        }
        else
        {
            taxiFullSem.V();
        }
        mutexSem.V();
    }
}

class livSup extends Thread {

    MageeSemaphore livPassSem;
    MageeSemaphore manuPassSem;
    MageeSemaphore mutexSem;
    MageeSemaphore taxiFullSem;

    int manuPassInTaxi;
    int livPassInTaxi;

    int numInLivQue;

    public livSup (int manuPassInTaxi, int livPassInTaxi, int numInLivQue, MageeSemaphore livPassSem, MageeSemaphore manuPassSem, MageeSemaphore mutexSem, MageeSemaphore taxiFullSem) {
        this.manuPassInTaxi = manuPassInTaxi;
        this. livPassInTaxi = livPassInTaxi;
        this.numInLivQue = numInLivQue;
        this.livPassSem = livPassSem;
        this.manuPassSem = manuPassSem;
        this.mutexSem = mutexSem;
        this.taxiFullSem = taxiFullSem;
    }

    public void run () {
        livPassSem.P();
        mutexSem.P();
        livPassInTaxi++;
        numInLivQue--;
        if ((manuPassInTaxi + livPassInTaxi) < 4) {
            if (livPassInTaxi == 3) {
                livPassSem.V();
            }
            else if (livPassInTaxi == 2 && manuPassInTaxi == 1) {
                manuPassSem.V();
            }
            else if (livPassInTaxi == 1 && manuPassInTaxi == 2) {
                livPassSem.V();
            }
            else {
                Random rand = new Random();
                int r = rand.nextInt(2);
                if (r == 0) {
                    manuPassSem.V();
                }
                else
                {
                    livPassSem.V();
                }
            }
        }
        else
        {
            taxiFullSem.V();
        }
        mutexSem.V();
    }
}



//MageeSemaphore.java
//This is an implementation of the traditional (counting) Semaphore with P() and V() operations
class MageeSemaphore
{
    private Semaphore sem;

    public MageeSemaphore (int initialCount)
    {
        sem = new Semaphore(initialCount);
    } // end constructor

    public void P()
    {
        try {
            sem.acquire();
        } catch (InterruptedException ex) {System.out.println("Interrupted when waiting");}
    } // end P()

    public void V()
    {
        sem.release();
    } // end V()

} // end MageeSemaphore


