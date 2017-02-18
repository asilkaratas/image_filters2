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
public class KM3Filter 
{   
    private static KM3Filter instance = null;
    public static KM3Filter getInstance()
    {
        if(instance == null)
        {
            instance = new KM3Filter();
        }
        return instance;
    }
    
    private int[] cornerBgNeighborPos = new int[8];
    private int[] stickingBgNeighborPos = new int[8];
    private int[] allNeighborPos = new int[16];
    private boolean modified;
    
    private int[] A0 = 
    {
        3,6,7,12,14,15,24,28,30,31,48,56,60, 
        62, 63, 96, 112, 120, 124, 126, 127, 129, 131, 135,
        143, 159, 191, 192, 193, 195, 199, 207, 223, 224, 225,
        227, 231, 239, 240, 241, 243, 247, 248, 249, 251, 252,
        253, 254
    };
    
    private int[] A1 = 
    {
        7, 14, 28, 56, 112, 131, 193, 224
    };
    
    private int[] A2 = 
    {
        7, 14, 15, 28, 30, 56, 60, 112, 120, 131, 135,
        193, 195, 224, 225, 240
    };
    
    private int[] A3 = 
    {
        7, 14, 15, 28, 30, 31, 56, 60, 62, 112, 120, 124, 131,
        135, 143, 193, 195, 199, 224, 225, 227, 240, 241, 248
    };
    
    private int[] A4 = 
    {
        7,14,15,28,30,31,56,60,62,63,112,120, 124, 126, 131, 135, 
        143, 159, 193, 195, 199, 207, 224, 225, 227, 231, 240, 241, 
        243, 248, 249, 252
    };
    
    private int[] A5 = 
    {
        7,14,15,28,30,31,56,60,62,63,112,120, 124, 126, 131, 135, 
        143, 159, 191, 193, 195, 199, 207, 224, 225, 227, 231, 239, 
        240, 241, 243, 248, 249, 251, 252, 254
    };
    
    private int[] A1Pix = 
    {
        3, 6, 7, 12, 14, 15, 24, 28, 30, 31, 48, 
        56, 60, 62, 63, 96, 112, 120, 124, 126, 
        127, 129, 131, 135, 143, 159, 191, 192, 
        193, 195, 199, 207, 223, 224, 225, 227, 
        231, 239, 240, 241, 243, 247, 248, 249, 
        251, 252, 253, 254,
        54, 216, 88, 80, 67,
        5, 13, 97, 20
    };
    

    private int[] weights = 
    {
        1, 2, 4, 8, 16, 32, 64, 128
    };
    
    private BufferedImage image;
    private int[] map;
    
    private KM3Filter()
    {
        
    }
    
    public boolean isModified()
    {
        return modified;
    }
    
    public BufferedImage start(BufferedImage image)
    {
        this.image = image;
        
        modified = false;
        
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
                
                if(Color.getR(rgb) == 0 && Color.getG(rgb) == 0 && Color.getB(rgb) == 0)
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
    
    public BufferedImage phase0()
    {
        int width = image.getWidth();
        int height = image.getHeight();
        int index = 0;
        int weight = 0;
        
        modified = false;
        
        //int[] clonedMap = map;
        
        for (int y = 0; y < height; ++y)
        {
            
            for(int x = 0; x < width; ++x)
            {
            
                index = y * width + x;
                
                
                if(map[index] == 1)
                {
                    weight = getTotalWeight(map, x, y, width, height);
                    
                    if(hasWeight(A0, weight))
                    {
                        map[index] = 2;
                        modified = true;
                    }
                }
            }
        }
        return createImage(map, width, height);
    }
    
    private BufferedImage phase(int[] array)
    {
        int width = image.getWidth();
        int height = image.getHeight();
        int index = 0;
        int weight = 0;
        
       // int[] clonedMap = getClonedMap();
        
        for (int y = 0; y < height; ++y)
        {
            for(int x = 0; x < width; ++x)
            {
                index = y * width + x;
                
                
                if(map[index] == 2)
                {
                    weight = getTotalWeight(map, x, y, width, height);
                    
                    if(hasWeight(array, weight))
                    {
                        map[index] = 0;
                        modified = true;
                    }
                }
            }
        }
        
        //this.map = clonedMap;
        
        return createImage(map, width, height);
    }
    
    
    
    public BufferedImage phase1()
    {
        return phase(A1);
    }
    
    public BufferedImage phase2()
    {
        return phase(A2);
    }
    
    public BufferedImage phase3()
    {
        return phase(A3);
    }
    
    public BufferedImage phase4()
    {
        return phase(A4);
    }
    
    public BufferedImage phase5()
    {
        return phase(A5);
    }
    
    public BufferedImage phase6()
    {
        int width = image.getWidth();
        int height = image.getHeight();
        int index = 0;
        int weight = 0;
        
        modified = false;
        
        //int[] clonedMap = map;
        
        for (int y = 0; y < height; ++y)
        {
            
            for(int x = 0; x < width; ++x)
            {
            
                index = y * width + x;
                
                
                if(map[index] == 2)
                {
                    map[index] = 1;
                    //modified = true;
                }
            }
        }
        
        //this.map = clonedMap;
        
        return createImage(map, width, height);
    }
    
    public BufferedImage findSkeleton()
    {
        int width = image.getWidth();
        int height = image.getHeight();
        int index = 0;
        int weight = 0;
        
        //int[] clonedMap = map;
        
        for (int y = 0; y < height; ++y)
        {
            for(int x = 0; x < width; ++x)
            {
                index = y * width + x;
                
                if(map[index] != 0)
                {
                    weight = getTotalWeight(map, x, y, width, height);
                    //System.out.println("weight:" + weight);
                    if(hasWeight(A1Pix, weight))
                    {
                        //System.out.println("has:weight:" + weight);
                        map[index] = 0;
                        modified = true;
                    }
                }
            }
        }
        
        //this.map = clonedMap;
        
        return createImage(map, width, height);
    }
    
 
    
    private BufferedImage createImage(int[] map, int width, int height)
    {
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
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
    
    
    
    public int getTotalWeight(int[] map, int x, int y, int width, int height)
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
    
   
    
    private int[] getClonedMap()
    {
        int[] clone = new int[map.length];
        System.arraycopy(map, 0, clone, 0, map.length);
        return clone;
    }
    
    private boolean hasWeight(int[] array, int weight)
    {
        //System.out.println("hasWeight:" + array.length + " weight:" + weight);
        for(int i = 0; i < array.length; ++i)
        {
            if(array[i] == weight)
            {
                return true;
            }
        }
        
        return false;
    }
    
    
    
}
