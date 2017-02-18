/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.filter;

import imagefilters.model.AppModel;
import imagefilters.model.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 *
 * @author asilkaratas
 */
public class ErosionFilter
{
    public static BufferedImage apply(BufferedImage image, int iteration)
    {
        BufferedImage out = null;
        for(int i = 0; i < iteration; ++i)
        {
            out = applyErosion(image);
        }
        
        
        return out;
    }
    
    private static BufferedImage applyErosion(BufferedImage in)
    {
        BufferedImage out = new BufferedImage(in.getWidth(),in.getHeight(),in.getType());
        int r,g,b,rgb;
        
        for(int x = 0; x<in.getWidth(); x++)
        {
            for (int y = 0; y < in.getHeight();y++)
            {
                
                r = Color.getR(in.getRGB(x, y));
                g = Color.getG(in.getRGB(x, y));
                b = Color.getB(in.getRGB(x, y));
                
                if(hasNonBlackNeighbor(in, x, y))
                {
                    
                }
                
                rgb = Color.toRGB(r, g, b);
                
                out.setRGB(x, y, rgb);
            }
        }
        
        return out;
    }
    
    
    private static boolean isBlackPixel(int rgb)
    {
        int r,g,b;
        r = Color.getR(rgb);
        g = Color.getG(rgb);
        b = Color.getB(rgb);
        
        
        return (r + g + b) == 0;
    }
    
    private static boolean hasNonBlackNeighbor(BufferedImage in, int x, int y)
    {
        
        return false;
    }
    
    
    
    private static BufferedImage applyBrightnessWithPercentace(BufferedImage in, double percentage)
    {
        BufferedImage out = new BufferedImage(in.getWidth(),in.getHeight(),in.getType());
        int r,g,b,rgb;
        
        int target = percentage < 0 ? Color.MIN : Color.MAX;
        percentage = Math.abs(percentage);
        
        for(int x = 0; x<in.getWidth(); x++)
        {
            for (int y = 0; y < in.getHeight();y++)
            {
                r = Color.getR(in.getRGB(x, y));
                g = Color.getG(in.getRGB(x, y));
                b = Color.getB(in.getRGB(x, y));
                
                r += (target - r) * percentage;
                g += (target - g) * percentage;
                b += (target - b) * percentage;
                
                //r = Color.clamp(r);
                //g = Color.clamp(g);
                //b = Color.clamp(b);
                
                rgb = Color.toRGB(r, g, b);
                
                out.setRGB(x, y, rgb);
            }
        }
        
        return out;
    }
    
}
