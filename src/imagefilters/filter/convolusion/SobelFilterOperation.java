/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.filter.convolusion;

import imagefilters.filter.convolusion.edgemode.EdgeFillModeFactory;
import imagefilters.filter.convolusion.edgemode.EdgeFillMode;
import imagefilters.model.Color;
import imagefilters.util.PixelUtil;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author asilkaratas
 */
public class SobelFilterOperation 
{
    public enum EdgeMode
    {
        BLACK,
        REPEAT,
        WRAP
    }
    
    private BufferedImage originalImage;
    private ConvolutionFilter filter;
    
    public SobelFilterOperation(BufferedImage originalImage, ConvolutionFilter filter)
    {
        this.originalImage = originalImage;
        this.filter = filter;
    }
    
    public BufferedImage apply()
    {
        int d = filter.getD();
        int kernelSize = filter.getSize() - 1;
        int kernelWidth = filter.getKernelWidth();
        int kernelHeight = filter.getKernelHeight();
        
        Point pivot = filter.getPivot();
        
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        
        int enlargedWidth = originalWidth + kernelSize;
        int enlargedHeight = originalHeight + kernelSize;
        
        int[] originalPixels = originalImage.getRGB(0, 0, originalWidth, originalHeight, null, 0, originalWidth);
        
        int[] enlargedPixels = PixelUtil.getEnlargedPixels(originalPixels, originalWidth, originalHeight, enlargedWidth, enlargedHeight, pivot);
        
        EdgeFillMode edgeFillMode = EdgeFillModeFactory.getInstance().getEdgeFillMode(filter.getEdgeMode());
        if(edgeFillMode != null)
        {   
            edgeFillMode.fill(enlargedPixels, originalPixels, originalWidth, originalHeight, enlargedWidth, enlargedHeight, pivot, kernelWidth-1, kernelHeight-1);
        }
        
        int[] filteredPixels = new int[originalWidth * originalHeight];
        
        int[] kernel = filter.getKernel();
        
        for(int i = 0; i < originalHeight; ++i)
        {
            for(int j = 0; j < originalWidth; ++j)
            {
                int pixelValue = applyFilter(i, j, kernel, kernelWidth, kernelHeight, enlargedPixels, enlargedWidth, pivot, d);
                
                int index = i * originalWidth + j;
                filteredPixels[index] = pixelValue;
            }
        }
        
        BufferedImage filteredImage = new BufferedImage(originalWidth, originalHeight, originalImage.getType());
        filteredImage.setRGB(0, 0, originalWidth, originalHeight, filteredPixels, 0, originalWidth);

        return filteredImage;
    }
    
    private int applyFilter(int row, int column, int[] kernel, int kernelWidth, int kernelHeight, int[] pixels, int imageWidth, Point pivot, int d)
    {
        
        int x = 0;
        int y = 0;
        
        for(int i = 0; i < kernelHeight; ++i)
        {
            for(int j = 0; j < kernelWidth; ++j)
            {
                int kernelIndex = i * kernelWidth + j;
                
                int pixelRow = row + i;
                int pixelColumn = column + j;
                int pixelIndex = pixelRow * imageWidth + pixelColumn;
                int pixelValue = pixels[pixelIndex];
                
                int red = Color.getR(pixelValue);
                int green = Color.getG(pixelValue);
                int blue = Color.getB(pixelValue);
                
                int rgb = (red + green + blue) /3;
                
                switch(kernelIndex)
                {
                    case 0:
                        x -= rgb;
                        y -= rgb;
                        break;
                        
                    case 1:
                        y -= 2 * rgb;
                        
                        break;
                        
                    case 2:
                        x += rgb;
                        y -= rgb;
                        break;
                        
                    case 3:
                        x -= 2 * rgb;
                        
                        break;
                        
                    case 5:
                        x += 2 *rgb;
                        break;
                        
                    case 6:
                        x -= rgb;
                        y += rgb;
                        break;
                        
                    case 7:
                        y += 2 * rgb;
                        break;
                        
                    case 8:
                        x += rgb;
                        y += rgb;
                        break;
                }
            }
        }
        
        int rgb = (int)Math.sqrt(x * x + y * y);
        
        
        rgb = Color.toRGB(rgb, rgb, rgb);
        //byte byteValue = (byte)value;
        
        return rgb;
    }
    
    
    
}
