/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.model;

/**
 *
 * @author asilkaratas
 */
public class Color
{
    public static int MIN = 0;
    public static int MAX = 255;
    
    public static int getR(int in)
    {
        return (int)((in << 8) >> 24) & 0xff;
    }
    
    public static int getG(int in) 
    {
        return (int)((in << 16) >> 24) & 0xff;
    }
    
    public static int getB(int in)
    {
        return (int)((in << 24) >> 24) & 0xff;
    }
    
    public static int toRGB(int r,int g,int b) 
    {
         return (int)((((r << 8)|g) << 8)|b); 
    }
    
    public static int clamp(int value)
    {
        if(value < MIN)
        {
            value = MIN;
        }
        
        if(value > MAX)
        {
            value = MAX;
        }
        
        return value;
    }
    
}
