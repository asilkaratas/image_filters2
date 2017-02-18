/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.util;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 *
 * @author asilkaratas
 */
public class PixelUtil
{
    public static int[] copyPixelsFromRect(int[] sourcePixels, int sourceWidth, int sourceHeight, Rectangle rectangle)
    {
        int[] pixels = new int[rectangle.width * rectangle.height];
        
        for(int i = 0; i < rectangle.height; ++i)
        {
            for(int j = 0; j < rectangle.width; ++j)
            {
                int sourceIndex = (rectangle.y + i) * sourceWidth + (rectangle.x + j);
                int index = i * rectangle.width + j;
                
                int pixelValue = sourcePixels[sourceIndex];
                pixels[index] = pixelValue;
            }
        }
        
        return pixels;
    }
    
    public static void pastePixelsToRect(int[] pixels, int pixelsWidth, int pixelsHeight,  int[] destinationPixels, int destinationWidth, int destinationHeight, Rectangle destinationRectangle)
    {
        for(int i = 0; i < pixelsHeight; ++i)
        {
            for(int j = 0; j < pixelsWidth; ++j)
            {
                int destinationInex = (destinationRectangle.y + i) * destinationWidth + (destinationRectangle.x + j);
                int index = i * pixelsWidth + j;
                
                int pixelValue = pixels[index];
                destinationPixels[destinationInex] = pixelValue;
            }
        }
    }
    
    
    public static int[] getEnlargedPixels(int[] originalPixels, int originalWidth, int originalHeight, 
                                            int enlargedWidth, int enlargedHeight, Point pivot)
    {
        int[] enlargedPixels = new int[enlargedWidth*enlargedHeight];
        
        int pivotX = pivot.x;
        int pivotY = pivot.y;
        
        for(int i = 0; i < originalHeight; ++i)
        {
            for(int j = 0; j < originalWidth; ++j)
            {
                int originalIndex = i * originalWidth + j;
                int enlargedIndex = (i + pivotY) * enlargedWidth + (j + pivotX);
                
                int pixelValue = originalPixels[originalIndex];
                enlargedPixels[enlargedIndex] = pixelValue;
            }
        }
        
        return enlargedPixels;
    }
}
