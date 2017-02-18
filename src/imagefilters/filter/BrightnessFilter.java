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
public class BrightnessFilter
{
    public static BufferedImage apply(BufferedImage image, int brightness, boolean usePercentace)
    {
        BufferedImage out = null;
        if(usePercentace)
        {
            double percentage = (double)brightness/(double)Color.MAX;
            out = applyBrightnessWithPercentace(image, percentage);
        }
        else
        {
            out = applyBrightness(image, brightness);
        }
        
        return out;
    }
    
    private static BufferedImage applyBrightness(BufferedImage in, int brightness)
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
                
                r += brightness;
                g += brightness;
                b += brightness;
                
                r = Color.clamp(r);
                g = Color.clamp(g);
                b = Color.clamp(b);
                
                rgb = Color.toRGB(r, g, b);
                
                out.setRGB(x, y, rgb);
            }
        }
        
        return out;
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
