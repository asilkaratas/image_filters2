/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.filter;

import imagefilters.filter.convolusion.*;
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
public class SobelFilter 
{
    public enum EdgeMode
    {
        BLACK,
        REPEAT,
        WRAP
    }
    
    private BufferedImage originalImage;
    private ConvolutionFilter filter;
    
    public SobelFilter(BufferedImage originalImage, ConvolutionFilter filter)
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
        int redTotal = 0;
        int greenTotal = 0;
        int blueTotal = 0;
        for(int i = 0; i < kernelHeight; ++i)
        {
            for(int j = 0; j < kernelWidth; ++j)
            {
                int kernelIndex = i * kernelWidth + j;
                int kernelValue = kernel[kernelIndex];
                
                int pixelRow = row + i;
                int pixelColumn = column + j;
                int pixelIndex = pixelRow * imageWidth + pixelColumn;
                int pixelValue = pixels[pixelIndex];
                
                int red = Color.getR(pixelValue);
                int green = Color.getG(pixelValue);
                int blue = Color.getB(pixelValue);
                
                redTotal += (red * kernelValue);
                greenTotal += (green * kernelValue);
                blueTotal += (blue * kernelValue);
            }
        }
        
        int r = redTotal / d;
        int g = greenTotal / d;
        int b = blueTotal / d;
        
        //value += filter.getOffset();
        
        r = Color.clamp(r);
        g = Color.clamp(g);
        b = Color.clamp(b);
        
        
        int rgb = Color.toRGB(r, g, b);
        //byte byteValue = (byte)value;
        
        return rgb;
    }
    
    
    
}
