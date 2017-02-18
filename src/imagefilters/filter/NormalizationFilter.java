/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.filter;

import imagefilters.model.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 *
 * @author asilkaratas
 */
public class NormalizationFilter
{
    public static BufferedImage apply(BufferedImage image, boolean useChannel)
    {
        BufferedImage out = null;
        if(useChannel)
        {
            out = applyNormalizationWithChannel(image);
        }
        else
        {
            out = applyNormalization(image);
        }
        
        return out;
    }
    
    private static BufferedImage applyNormalizationWithChannel(BufferedImage in)
    {
        BufferedImage out = new BufferedImage(in.getWidth(),in.getHeight(),in.getType());
        int r,g,b,rgb;
        int minR = 255, minG = 255, minB = 255;
        int maxR = 0, maxG = 0, maxB = 0;
        
        for(int x = 0; x<in.getWidth();x++)
        {
            for (int y = 0; y < in.getHeight();y++)
            {
                r = Color.getR(in.getRGB(x, y));
                g = Color.getG(in.getRGB(x, y));
                b = Color.getB(in.getRGB(x, y));
                
                minR = r < minR ? r : minR;
                minG = g < minG ? g : minG;
                minB = b < minB ? b : minB;
                
                maxR = r > maxR ? r : maxR;
                maxG = g > maxG ? g : maxG;
                maxB = b > maxB ? b : maxB;
            }
        }
        
        double mulR = 255.0 / (maxR - minR);
        double mulG = 255.0 / (maxG - minG);
        double mulB = 255.0 / (maxB - minB);
        
        for(int x = 0; x<in.getWidth();x++)
        {
            for (int y = 0; y < in.getHeight();y++)
            {
                r = Color.getR(in.getRGB(x, y));
                g = Color.getG(in.getRGB(x, y));
                b = Color.getB(in.getRGB(x, y));
                
                r = (int)((r - minR) * mulR);
                g = (int)((g - minG) * mulG);
                b = (int)((b - minB) * mulB);
                
                r = Color.clamp(r);
                g = Color.clamp(g);
                b = Color.clamp(b);
                
                rgb = Color.toRGB(r,g,b);
                out.setRGB(x, y, rgb);
            }
        }
        
        return out;
    }
    
    private static BufferedImage applyNormalization(BufferedImage in)
    {
        BufferedImage out = new BufferedImage(in.getWidth(),in.getHeight(),in.getType());
        int r,g,b,rgb;
        int minA = 255;
        int maxA = 0;
        int average;
        
        for(int x = 0; x<in.getWidth();x++)
        {
            for (int y = 0; y < in.getHeight();y++)
            {
                r = Color.getR(in.getRGB(x, y));
                g = Color.getG(in.getRGB(x, y));
                b = Color.getB(in.getRGB(x, y));
                
                average = (r + g + b)/3;
                
                if(average < minA) 
                {
                    minA = average;
                }
                
                if(average > maxA) 
                {
                    maxA = average;
                }
            }
        }
        
        double mulA = 255.0 / (double)(maxA - minA);
        
        for(int x = 0; x<in.getWidth();x++)
        {
            for (int y = 0; y < in.getHeight();y++)
            {
                r = Color.getR(in.getRGB(x, y));
                g = Color.getG(in.getRGB(x, y));
                b = Color.getB(in.getRGB(x, y));
                
                r = (int)((r - minA) * mulA);
                g = (int)((g - minA) * mulA);
                b = (int)((b - minA) * mulA);
                
                r = Color.clamp(r);
                g = Color.clamp(g);
                b = Color.clamp(b);
                
                rgb = Color.toRGB(r,g,b);
                out.setRGB(x, y, rgb);
            }
        }
        
        return out;
    }
}
