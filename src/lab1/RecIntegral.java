/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
/**
 *
 * @author student
 */
public class RecIntegral implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    private final int THREAD_COUNT = 6;
    private double lower_end;
    private double upper_end;
    private double step;
    private volatile double result;
    private int index;
    private transient CyclicBarrier barrier;
    
    class CurrentStep
    {
        private double x1;
        private double x2;
        CurrentStep(double le)
        {
            x1 = le;
            x2 = le + step;
        }
    }
    
    class Worker implements Runnable
    {
        private final ReentrantLock locker;
        private final CurrentStep cs;
        Worker(CurrentStep cs, ReentrantLock lock)
        {
            this.cs = cs;
            locker = lock;
        }
        private double f(double x)
        {
            return Math.sqrt(x);
        }
        @Override
        public void run()
        {
            /* Spaghetti code warning */
            double l_x1, l_x2, res;
            for(;;)
            {
                locker.lock();
                try
                {
                    if(cs.x2 > upper_end)
                        break;
                    l_x1 = cs.x1;
                    l_x2 = cs.x2;
                    cs.x1 = cs.x2;
                    cs.x2 += step;
                }
                finally
                {
                    locker.unlock();
                }
                res = (f(l_x2) + f(l_x1)) * step / 2;
                result += res;
            }
            try
            {   
                barrier.await();
            }
            catch (InterruptedException | BrokenBarrierException ex)
            {
                ex.printStackTrace();
            }
        }    
    }
    public RecIntegral()
    {
        Init(0.0, 0.0, 0.0, 0.0, 0);
    }
    public RecIntegral(double le, double ue, double s, int i)
    {
        Init(le, ue, s, 0.0, i);
    }
    public RecIntegral(Map<String, String> m)
    {
        Init(Double.parseDouble(m.get("lower_end")),
             Double.parseDouble(m.get("upper_end")),
             Double.parseDouble(m.get("step")),
             Double.parseDouble(m.get("result")),
             Integer.parseInt(m.get("index"))
        );
    }
    private void Init(double le, double ue, double s, double r, int i)
    {
        lower_end = le;
        upper_end = ue;
        step = s;
        result = r;
        index = i;
        barrier = new CyclicBarrier(THREAD_COUNT, () -> {
            synchronized(barrier)
            {
                barrier.notify();
            }
        });
    }
    public int index()
    {
        return index;
    }
    public double GetLowerEnd()
    {
        return lower_end;
    }
    public double GetUpperEnd()
    {
        return upper_end;
    }
    public double GetStep()
    {
        return step;
    }
    public double GetResult()
    {
        return result;
    }
    public double Calc()
    {
        //TODO: ???????????????? ???? ?????????? ???????????? ?????????????? ???????????????? ????????????????????
        if(result != 0.0)
            return result;
        CurrentStep cs = new CurrentStep(lower_end);
        ReentrantLock lock = new ReentrantLock(true);
        for(int i = 0; i < THREAD_COUNT; i++)
        {
            new Thread(new Worker(cs, lock)).start();
        }
        try
        {
            synchronized(barrier)
            {
                barrier.wait();
            }
        }
        catch(InterruptedException ex)
        {
            ex.printStackTrace();
        }
        return result;
    }
    public String AsJson()
    {
        return "{\n"
                + "\t\"lower_end\" : " + Double.toString(lower_end) + ",\n"
                + "\t\"upper_end\" : " + Double.toString(upper_end) + ",\n"
                + "\t\"step\" : " + Double.toString(step) + ",\n"
                + "\t\"result\" : " + Double.toString(result) + ",\n"
                + "\t\"index\" : " + Integer.toString(index) + "\n"
                + "}\n";
    }
}