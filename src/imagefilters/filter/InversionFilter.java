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
public class InversionFilter
{
    public static BufferedImage apply(BufferedImage image)
    {
        BufferedImage in = image;
        
        int r,g,b,rgb;
        BufferedImage out = new BufferedImage(in.getWidth(),in.getHeight(),in.getType());
        for(int x = 0; x < in.getWidth(); x++)
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
    
}
