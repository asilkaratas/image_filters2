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
public class KMMFilter 
{   
    private static KMMFilter instance = null;
    public static KMMFilter getInstance()
    {
        if(instance == null)
        {
            instance = new KMMFilter();
        }
        return instance;
    }
    
    private int[] cornerBgNeighborPos = new int[8];
    private int[] stickingBgNeighborPos = new int[8];
    private int[] allNeighborPos = new int[16];
    private int[] deletionArray = 
    {
        3, 5, 7, 12, 13, 14, 15, 20,
        21, 22, 23, 28, 29, 30, 31, 48,
        52, 53, 54, 55, 56, 60, 61, 62,
        63, 65, 67, 69, 71, 77, 79, 80,
        81, 83, 84, 85, 86, 87, 88, 89,
        91, 92, 93, 94, 95, 97, 99, 101,
        103, 109, 111, 112, 113, 115, 116, 117,
        118, 119, 120, 121, 123, 124, 125, 126,
        127, 131, 133, 135, 141, 143, 149, 151,
        157, 159, 181, 183, 189, 191, 192, 193,
        195, 197, 199, 205, 207, 208, 209, 211,
        212, 213, 214, 215, 216, 217, 219, 220,
        221, 222, 223, 224, 225, 227, 229, 231,
        237, 239, 240, 241, 243, 244, 245, 246,
        247, 248, 249, 251, 252, 253, 254, 255
    };
    
    private int[] candidate4Array = 
    {
        3, 6, 12, 24, 48, 96, 192, 129,
        7, 14, 28, 56, 112, 224, 193, 131,
        15, 30, 60, 120, 240, 225, 195, 135
    };
    
    private int[] weights = 
    {
        1, 2, 4, 8, 16, 32, 64, 128
    };
    
    private BufferedImage image;
    private int[] map;
    
    private KMMFilter()
    {
        
    }
    
    public BufferedImage start(BufferedImage image)
    {
        this.image = image;
        
        return image;
    }
    
    public BufferedImage covertMap()
    {
        int rgb;
        int width = image.getWidth();
        int height = image.getHeight();
        int index = 0;
        int mapLen = width * height;
        int[] map = new int[mapLen];
        
        for(int x = 0; x< width; ++x)
        {
            for (int y = 0; y < height; ++y)
            {       
            
                index = y * width + x;
                rgb = image.getRGB(x, y);
                
                if(Color.getR(rgb) == 0 && 
                        Color.getG(rgb) == 0 &&
                        Color.getB(rgb) == 0)
                {
                    map[index] = 1;
                }
                else
                {
                    map[index] = 0;
                }
            }
        }
        
        this.map = map;
        
        return createImage(map, width, height);
    }
    
    public BufferedImage convertTo2()
    {
        int width = image.getWidth();
        int height = image.getHeight();
        int index = 0;
        
        int[] clonedMap = map;
        
        for(int x = 0; x < width; ++x)
        {
            for (int y = 0; y < height; ++y)
            {
            
                index = y * width + x;
                
                
                if(map[index] == 1 && hasStickingBgNeighbor(map, x, y, 
                                                        width, height))
                {
                    clonedMap[index] = 2;
                }
            }
        }
        
        this.map = clonedMap;
        
        return createImage(map, width, height);
    }
    
    public BufferedImage convertTo3()
    {
        int width = image.getWidth();
        int height = image.getHeight();
        int index = 0;
        
        int[] clonedMap = map;
        
        for(int x = 0; x < width; ++x)
        {
            for (int y = 0; y < height; ++y)
            {
                index = y * width + x;
                
                if(map[index] == 1 && hasCornerBgNeighbor(map, x, y, 
                        width, height))
                {
                    clonedMap[index] = 3;
                }
                
            }
        }
        
        this.map = clonedMap;
        
        return createImage(map, width, height);
    }
    
    public BufferedImage calculate4()
    {
        int width = image.getWidth();
        int height = image.getHeight();
        int index = 0;
        
        int[] clonedMap = map;
        
        int totalWeight;
        for(int x = 0; x < width; ++x)
        {
              
            for (int y = 0; y < height; ++y)
            {
               index = y * width + x;
                
                if(map[index] != 0)
                {
                    totalWeight = getTotalWeight(map, x, y, width, height);
                    if(isCandidate4(totalWeight) && isDeletable(totalWeight))
                    {
                        clonedMap[index] = 4;
                    }
                    
                }
            }
        }
        
        this.map = clonedMap;
        
        return createImage(map, width, height);
    }
    
    public BufferedImage delete4()
    {
        int width = image.getWidth();
        int height = image.getHeight();
        int index = 0;
        
        int[] clonedMap = map;
        
        for(int x = 0; x < width; ++x)
        {
            for (int y = 0; y < height; ++y)
            {
            
                index = y * width + x;
                
                if(map[index] == 4)
                {
                    clonedMap[index] = 0;
                }
            }
        }
        
        this.map = clonedMap;
        
        return createImage(map, width, height);
    }
    
    public BufferedImage calculateWeight2()
    {
        int width = image.getWidth();
        int height = image.getHeight();
        int index = 0;
        
        int[] clonedMap = map;
        
        int totalWeight;
        int N = 2;
       
        for(int x = 0; x < width; ++x)
        {       
            for (int y = 0; y < height; ++y)
            {
                index = y * width + x;

                if(map[index] == N)
                {
                    totalWeight = getTotalWeight(map, x, y, width, height);
                    if(isDeletable(totalWeight))
                    {
                        clonedMap[index] = 0;
                    }
                    else
                    {
                        clonedMap[index] = 1;
                    }
                }
            }
        }
        
        this.map = clonedMap;
        
        return createImage(map, width, height);
    }
    
    
    
    public BufferedImage calculateWeight3()
    {
        int width = image.getWidth();
        int height = image.getHeight();
        int index = 0;
        
        int[] clonedMap = map;
        
        int totalWeight;
        int N = 3;
        for(int x = 0; x < width; ++x)
        {
            for (int y = 0; y < height; ++y)
            {
                index = y * width + x;

                if(map[index] == N)
                {
                    totalWeight = getTotalWeight(map, x, y, width, height);
                    if(isDeletable(totalWeight))
                    {
                        clonedMap[index] = 0;
                    }
                    else
                    {
                        clonedMap[index] = 1;
                    }
                }
            }
        }
        
        this.map = clonedMap;
        
        return createImage(map, width, height);
    }
    
    private BufferedImage createImage(int[] map, int width, int height)
    {
        BufferedImage out = new BufferedImage(width, height, 
                BufferedImage.TYPE_INT_RGB);
        int index;
        int rgb = 0;
        for(int x = 0; x < width; ++x)
        {
            for (int y = 0; y < height; ++y)
            {
                index = y * width + x;
                
                if(map[index] == 0)
                {
                    rgb = Color.toRGB(255, 255, 255);
                }
                else if(map[index] == 1)
                {
                    rgb = Color.toRGB(255, 0, 0);
                }
                else if(map[index] == 2)
                {
                    rgb = Color.toRGB(0, 255, 0);
                }
                else if(map[index] == 3)
                {
                    rgb = Color.toRGB(0, 0, 255);
                }
                else if(map[index] == 4)
                {
                    rgb = Color.toRGB(160, 160, 160);
                }
                out.setRGB(x, y, rgb);
            }
        }
        return out;
    }
    
    private boolean hasStickingBgNeighbor(int[] map, int x, int y, 
                                            int width, int height)
    {
        int xPos;
        int yPos;
        
        //top
        stickingBgNeighborPos[0] = x;
        stickingBgNeighborPos[1] = y - 1;
        
        //right
        stickingBgNeighborPos[2] = x + 1;
        stickingBgNeighborPos[3] = y;
        
        //bottom
        stickingBgNeighborPos[4] = x;
        stickingBgNeighborPos[5] = y + 1;
        
        //left
        stickingBgNeighborPos[6] = x - 1;
        stickingBgNeighborPos[7] = y;
        
        for(int i = 0; i < 8; i += 2)
        {
           xPos = stickingBgNeighborPos[i];
           yPos = stickingBgNeighborPos[i + 1];
           
           if(xPos < width && xPos >= 0 && 
              yPos < height && yPos >= 0 &&
              map[yPos * width + xPos] == 0)
           {
               return true;
           }
        }
        
        return false;
    }
    
    private boolean hasCornerBgNeighbor(int[] map, int x, int y, 
                                        int width, int height)
    {
        int xPos;
        int yPos;
        
        //topright
        cornerBgNeighborPos[0] = x + 1;
        cornerBgNeighborPos[1] = y - 1;
        
        //bottomright
        cornerBgNeighborPos[2] = x + 1;
        cornerBgNeighborPos[3] = y + 1;
        
        //bottomleft
        cornerBgNeighborPos[4] = x - 1;
        cornerBgNeighborPos[5] = y + 1;
        
        //topleft
        cornerBgNeighborPos[6] = x - 1;
        cornerBgNeighborPos[7] = y - 1;
        
        for(int i = 0; i < 8; i += 2)
        {
           xPos = cornerBgNeighborPos[i];
           yPos = cornerBgNeighborPos[i + 1];
           
           if(xPos < width && xPos >= 0 && 
              yPos < height && yPos >= 0 &&
              map[yPos * width + xPos] == 0)
           {
               return true;
           }
        }
        
        return false;
    }
    
    public int getTotalWeight(int[] map, int x, int y, 
                                int width, int height)
    {
        int xPos;
        int yPos;
        int totalWeight = 0;
        int weightIndex = 0;
        int index = y * width + x;
        
        setAllNeightbors(x, y);
        
        for(int i = 0; i < allNeighborPos.length; i += 2)
        {
           xPos = allNeighborPos[i];
           yPos = allNeighborPos[i + 1];
           
           if(xPos < width && xPos >= 0 && 
              yPos < height && yPos >= 0 &&
              map[yPos * width + xPos] != 0)
           {
               totalWeight += weights[weightIndex];
           }
           
           weightIndex ++;
           
        }
        //System.out.println(String.format("pixel m:%d x:%d y:%d weight:%d", map[index], x, y, totalWeight));
       
        return totalWeight;
    }
    
    private void setAllNeightbors(int x, int y)
    {
        //top
        allNeighborPos[0] = x;
        allNeighborPos[1] = y - 1;
        
        //topright
        allNeighborPos[2] = x + 1;
        allNeighborPos[3] = y - 1;
        
        //right
        allNeighborPos[4] = x + 1;
        allNeighborPos[5] = y;
        
        //bottomright
        allNeighborPos[6] = x + 1;
        allNeighborPos[7] = y + 1;
        
        //bottom
        allNeighborPos[8] = x;
        allNeighborPos[9] = y + 1;
        
        //bottomleft
        allNeighborPos[10] = x - 1;
        allNeighborPos[11] = y + 1;
        
        //left
        allNeighborPos[12] = x - 1;
        allNeighborPos[13] = y;
        
        //topleft
        allNeighborPos[14] = x - 1;
        allNeighborPos[15] = y - 1;
    }
    
    public int getNeightborCount(int[] map, int x, int y, int width, int height)
    {
        int xPos;
        int yPos;
        int neightborCount = 0;
        int index = y * width + x;
        
        setAllNeightbors(x, y);
        
        for(int i = 0; i < allNeighborPos.length; i += 2)
        {
           xPos = allNeighborPos[i];
           yPos = allNeighborPos[i + 1];
           
           if(xPos < width && xPos >= 0 && 
              yPos < height && yPos >= 0 &&
              map[yPos * width + xPos] != 0)
           {
               neightborCount ++;
           }
           
        }
        //System.out.println(String.format("pixel m:%d x:%d y:%d weight:%d", map[index], x, y, totalWeight));
       
        return neightborCount;
    }
    
    private boolean isCandidate4(int totalWeight)
    {
        for(int i = 0; i < candidate4Array.length; ++i)
        {
            if(candidate4Array[i] == totalWeight)
            {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean isDeletable(int totalWeight)
    {
        for(int i = 0; i < deletionArray.length; ++i)
        {
            if(deletionArray[i] == totalWeight)
            {
                return true;
            }
        }
        
        return false;
    }
    
    private int[] getClonedMap()
    {
        int[] clone = new int[map.length];
        System.arraycopy(map, 0, clone, 0, map.length);
        return clone;
    }
    
    public boolean hasAtLeast2CloseNeighbor(int[] map, int x, int y, 
                                               int width, int height)
    {
        int xPos;
        int yPos;
        int lastIndex = -4;
        boolean hasTopNeighbor = false;
        
        setAllNeightbors(x, y);
        
        for(int i = 0; i < allNeighborPos.length; i += 2)
        {
           xPos = allNeighborPos[i];
           yPos = allNeighborPos[i + 1];
           
           if(xPos < width && xPos >= 0 && 
              yPos < height && yPos >= 0 &&
              map[yPos * width + xPos] != 0)
           {
               if(i - lastIndex == 2)
               {
                   return true;
               }
               if(i == 0)
               {
                   hasTopNeighbor = true;
               }
               
               if(hasTopNeighbor && i == 14)
               {
                   return true;
               }
               lastIndex = i;
           }
        }
        return false;
    }
    
    public boolean isOnePixelSkeleton()
    {
        int width = image.getWidth();
        int height = image.getHeight();
        int index = 0;
        
        for(int x = 0; x < width; ++x)
        {
            for (int y = 0; y < height; ++y)
            {
                index = y * width + x;
                if(map[index] == 1)
                {
                    if(hasAtLeast2CloseNeighbor(map, x, y, width, height))
                    {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
}
