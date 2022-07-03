/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1;
import java.io.*;
import java.util.*;
/**
 *
 * @author student
 */
public class RecIntegral implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    private double lower_end;
    private double upper_end;
    private double step;
    private double result;
    private int index;
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
    public static double F(double x)
    {
        return Math.sqrt(x);
    }
    public double Calc()
    {
        result = 0.0;
        double x1 = lower_end;
        double x2 = x1;
        while(x2 < upper_end)
        {
            x2 = x1 + step;
            result += (F(x2) + F(x1)) * step / 2;
            x1 = x2;
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