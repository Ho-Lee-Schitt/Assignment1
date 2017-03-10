/**
 * Created by Niall Hughes on 21/02/2017.
 */

package Assignment;

/* Activity class */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

// Represents recorded actions during the taxi activity
// - addActivity(<String>) adds a supporter to the taxi
// - removeActivity(<String>) removes a supporter from the taxi
// - removeAllActivities(<List of Strings>) removes all supporters from the taxi
// - printAllActivities (<List of Strings>) display all supporters in the taxi
public class Activity
{

    private final List<String> theActivities;

    /* Constructor for objects of class Activity */
    public Activity()
    {
        theActivities = Collections.synchronizedList(new ArrayList<String>());
    }

    public void addActivity(String s)
    {
        theActivities.add(s);
        synchronized (System.out)
        {
            System.out.println("Supporter " + s + " entered the taxi");
        }
    }// end addActivity

    public void removeActivity(String s)
    {
        if (!theActivities.remove(s))
        {
            synchronized (System.out)
            {
                System.out.println("Supporter " + s + " not found");
            }
        } else
        {
            synchronized (System.out)
            {
                System.out.println("Supporter " + s + " leaving the taxi");
            }
        }
    }// end removeActivity

    public void removeAllActivities()
    {
        synchronized (theActivities)
        {
            if (!theActivities.isEmpty())
            {
                Iterator<String> itr = theActivities.iterator();
                while (itr.hasNext())
                {
                    String s = itr.next();
                        System.out.println("Supporter " + s + " vacated the taxi");
                    itr.remove();
                }
            } else
            {
                    System.out.println("Taxi already empty");
            }
        }
        synchronized (System.out)
        {
            System.out.println();
        }
    }// end removeAllActivities

    public void printActivities()
    {
        synchronized (System.out)
        {
            System.out.print("SUPPORTERS IN TAXI [ ");

            for (String s : theActivities)
            {
                System.out.print(s);
                System.out.print(" ");
            }
            System.out.println("]");
        }
    }// end printActivities

}// end Activity
