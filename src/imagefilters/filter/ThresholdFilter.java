/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.filter;

import imagefilters.model.AppModel;
import imagefilters.model.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author asilkaratas
 */
public class ThresholdFilter
{
  
    public static BufferedImage apply(BufferedImage image, int threshold, boolean inverted, boolean colored)
    {
        BufferedImage inputImage = image;
        
        if(inverted)
        {
            inputImage = applyInverted(inputImage);
        }
        
        if(colored)
        {
            inputImage = applyThresholdColored(inputImage, threshold);
        }
        else
        {
            inputImage = applyThresholdGrayscaled(inputImage, threshold);
        }
        
        return inputImage;
    }
    
    private static BufferedImage applyInverted(BufferedImage in)
    {
        int r,g,b,rgb;
        BufferedImage out = new BufferedImage(in.getWidth(),in.getHeight(),in.getType());
        for(int x = 0; x< in.getWidth(); x++)
        {
            for (int y = 0; y < in.getHeight(); y++)
            {
                r = 255 - Color.getR(in.getRGB(x, y));
                g = 255 - Color.getG(in.getRGB(x, y));
                b = 255 - Color.getB(in.getRGB(x, y));

                rgb = Color.toRGB(r, g, b);
                
                out.setRGB(x, y, rgb);
            }
        }
        
        return out;
    }
    
    private static BufferedImage applyThresholdGrayscaled(BufferedImage in, int threshold)
    {
        int r,g,b,rgb;
        BufferedImage out = new BufferedImage(in.getWidth(),in.getHeight(),in.getType());
        for(int x = 0; x< in.getWidth(); x++)
        {
            for (int y = 0; y < in.getHeight(); y++)
            {
                r = Color.getR(in.getRGB(x, y));
                g = Color.getG(in.getRGB(x, y));
                b = Color.getB(in.getRGB(x, y));

                rgb = (r+g+b)/3;
                rgb = rgb < threshold ? 0 : 255;
                rgb = Color.toRGB(rgb, rgb, rgb);
                
                out.setRGB(x, y, rgb);

            }
        }
        
        return out;
    }
    
    private static BufferedImage applyThresholdColored(BufferedImage in, int threshold)
    {
        int r,g,b,rgb;
        BufferedImage out = new BufferedImage(in.getWidth(),in.getHeight(),in.getType());
        for(int x = 0; x< in.getWidth(); x++)
        {
            for (int y = 0; y < in.getHeight(); y++)
            {
                r = Color.getR(in.getRGB(x, y));
                g = Color.getG(in.getRGB(x, y));
                b = Color.getB(in.getRGB(x, y));

                r = r < threshold ? 0 : 255;
                g = g < threshold ? 0 : 255;
                b = b < threshold ? 0 : 255;
                rgb = Color.toRGB(r,g,b);

                out.setRGB(x, y, rgb);
            }
        }
        
        return out;
    }
}
