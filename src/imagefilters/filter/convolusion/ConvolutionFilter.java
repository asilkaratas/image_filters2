/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.filter.convolusion;

import imagefilters.filter.ImageFilter;
import imagefilters.filter.convolusion.edgemode.EdgeMode;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author asilkaratas
 */
public class ConvolutionFilter extends ImageFilter
{
    private int d;
    private int offset;
    private Point pivot;
    private EdgeMode edgeMode;
    private int kernelWidth;
    private int kernelHeight;
    
    private ArrayList<ArrayList<Integer>> kernelList;
    
    private boolean isSobel;
    public ConvolutionFilter(String name, int[] kernel, int d, int offset, boolean isSobel)
    {
        super(name);
        
        this.d = d;
        this.offset = offset;
        this.edgeMode = EdgeMode.REPEAT;
        
        this.isSobel = isSobel;
        
        setKernelList(kernel);
        
        int center = (kernelList.size()-1)/2;
        this.pivot = new Point(center, center);
        
        
        kernelWidth = kernelList.size();
        kernelHeight = kernelList.size();
    }
    
    public ConvolutionFilter(String name, int[] kernel, int d, int offset)
    {
        this(name, kernel, d, offset, false);
    }
    
    private ConvolutionFilter(String name, ArrayList<ArrayList<Integer>> kernelList, int d, int offset)
    {
        super(name);
        
        this.kernelList = kernelList;
        this.d = d;
        this.offset = offset;
        this.edgeMode = EdgeMode.BLACK;
        
        int center = (kernelList.size()-1)/2;
        this.pivot = new Point(center, center);
        
        kernelWidth = kernelList.size();
        kernelHeight = kernelList.size();
    }
    
    private void setKernelList(int[] kernel)
    {
        kernelList = new ArrayList<ArrayList<Integer>>();
        
        int size = (int)Math.sqrt(kernel.length);
        
        for(int i = 0; i < size; ++i)
        {
            ArrayList<Integer> kernelRow = new ArrayList<Integer>();
            for(int j = 0; j < size; ++j)
            {
                int index = i * size + j;
                int value = kernel[index];
                
                kernelRow.add(value);
            }
            
            kernelList.add(kernelRow);
        }
    }

    public ArrayList<ArrayList<Integer>> getKernelList() 
    {
        return kernelList;
    }

    public void setD(int d)
    {
        this.d = d;
    }
    
    public int getD()
    {
        return d;
    }
    
    public void setOffset(int offset)
    {
        this.offset = offset;
    }

    public int getOffset() 
    {
        return offset;
    }
    
    public Point getPivot()
    {
        return pivot;
    }
    
    public int getPivotIndex()
    {
        return pivot.y * getKernelWidth() + pivot.x;
    }
    
    public void setPivotIndex(int index)
    {
        int j = (int)Math.floor(index/getKernelWidth());
        int i = index% getKernelWidth();
        
        pivot.x = i;
        pivot.y = j;
    }
    
    public EdgeMode getEdgeMode()
    {
        return edgeMode;
    }
    
    public void setEdgeMode(EdgeMode edgeMode)
    {
        this.edgeMode = edgeMode;
    }
    
    public int getSize()
    {
        return this.kernelList.size();
    }
    
    public void setKernelWidth(int kernelWidth)
    {
        int oldKernelWidth = this.kernelWidth;
        this.kernelWidth = kernelWidth;
        
        int diff = kernelWidth - oldKernelWidth;
        
        System.out.println("setKernelWidth:" + oldKernelWidth + " kernelWidth:" + kernelWidth + " diff:" + diff);
        if(diff > 0)
        {
            for(int i = 0; i < diff; ++i)
            {
                for(ArrayList<Integer> kernelRow : kernelList)
                {
                    kernelRow.add(0);
                }
            }
        }
        else if(diff < 0)
        {
            diff *= -1;
            for(int i = 0; i < diff; ++i)
            {
                for(ArrayList<Integer> kernelRow : kernelList)
                {
                    kernelRow.remove(kernelRow.size()-1);
                }
            }
        }
    }
    
    public int getKernelWidth()
    {
        return kernelWidth;
    }
    
    public void setKernelHeight(int kernelHeight)
    {
        int oldKernelHeight = this.kernelHeight;
        this.kernelHeight = kernelHeight;
        
        int diff = kernelHeight - oldKernelHeight;
        if(diff > 0)
        {
            for(int i = 0; i < diff; ++i)
            {
                ArrayList<Integer> kernelRow = new ArrayList<Integer>();
                
                for(int j = 0; j < kernelWidth; ++j)
                {
                    kernelRow.add(0);
                }
                
                kernelList.add(kernelRow);
            }
        }
        else if(diff < 0)
        {
            diff *= -1;
            for(int i = 0; i < diff; ++i)
            {
                kernelList.remove(kernelList.size()-1);
            }
        }
            
    }
    
    public int getKernelHeight()
    {
        return kernelHeight;
    }
    
    
    public void setSize(int size)
    {
        int oldSize = kernelList.size();
        int sizeDiff = size - oldSize;
        
        int count = sizeDiff/2;
        
        if(count > 0)
        {
            for(int i = 0; i < count; ++i)
            {
                for(ArrayList<Integer> kernelRow:kernelList)
                {
                    kernelRow.add(0, 0);
                    kernelRow.add(0);
                }
            }
            
            for(int i = 0; i < count; ++i)
            {
                ArrayList<Integer> firstKernelRow = createKernelRow(size);
                ArrayList<Integer> lastKernelRow = createKernelRow(size);
                kernelList.add(0, firstKernelRow);
                kernelList.add(lastKernelRow);
            }
        }
        else if(count < 0)
        {
            count = count * -1;
            
            for(int i = 0; i < count; ++i)
            {
                kernelList.remove(0);
                kernelList.remove(kernelList.size() - 1);
            }
            
            for(int i = 0; i < count; ++i)
            {
                for(ArrayList<Integer> kernelRow:kernelList)
                {
                    kernelRow.remove(0);
                    kernelRow.remove(kernelRow.size() - 1);
                }
            }
        }
    }
    
    private ArrayList<Integer> createKernelRow(int size)
    {
        ArrayList<Integer> kernelRow = new ArrayList<Integer>();
        for(int j = 0; j < size; ++j)
        {
            kernelRow.add(0);
        }
        return kernelRow;
    }
    
    /*
    public void setSize(int size)
    {
        int oldSize = kernelList.size();
        int sizeDiff = size - oldSize;
        
        if(oldSize > size)
        {
            while(kernelList.size() > size)
            {
                kernelList.remove(kernelList.size() - 1);
            }
            
            for(ArrayList<Integer> kernelRow : kernelList)
            {
                while(kernelRow.size() > size)
                {
                    kernelRow.remove(kernelRow.size() - 1);
                }
            }
        }
        else if(oldSize < size)
        {
            while(kernelList.size() < size)
            {
                ArrayList kernelRow = new ArrayList<Integer>();
                while(kernelRow.size() < size)
                {
                    kernelRow.add(0);
                }
                kernelList.add(kernelRow);
            }
            
            for(ArrayList<Integer> kernelRow : kernelList)
            {
                while(kernelRow.size() < size)
                {
                    kernelRow.add(0);
                }
            }
        }
        
        int maxIndex = size - 1;
        if(pivot.x > maxIndex)
        {
            pivot.x = maxIndex;
        }
        
        if(pivot.y > maxIndex)
        {
            pivot.y = maxIndex;
        }
    }
    */
    
    public void setValueWithIndex(int value, int index)
    {
        int i = (int)Math.floor(index/getKernelWidth());
        int j = index% getKernelWidth();
        
        ArrayList<Integer> kernelRow = kernelList.get(i);
        kernelRow.set(j, value);
    }
    
    @Override
    public ConvolutionFilter clone()
    {
        ArrayList<ArrayList<Integer>> newKernelList = new ArrayList<ArrayList<Integer>>();
        for(ArrayList<Integer> kernelRow : this.kernelList)
        {
            ArrayList<Integer> newKernelRow = new ArrayList<Integer>();
            for(Integer value : kernelRow)
            {
                newKernelRow.add(value);
            }
            
            newKernelList.add(newKernelRow);
        }
        return new ConvolutionFilter(getName(), newKernelList, d, offset);
    }
    
    
    public int[] getKernel()
    {
        int kernelLength = getKernelWidth() * getKernelHeight();
        int[] kernel = new int[kernelLength];
        
        int index = 0;
        for(ArrayList<Integer> kernelRow : kernelList)
        {
            for(Integer value : kernelRow)
            {
                kernel[index] = value.intValue();
                index ++;
            }
        }
        
        return kernel;
    }
    
    public BufferedImage apply(BufferedImage originalImage)
    {
        if(isSobel)
        {
            SobelFilterOperation operation = new SobelFilterOperation(originalImage, this);
            return operation.apply();
        }
        else
        {
            ConvolutionFilterOperation operation = new ConvolutionFilterOperation(originalImage, this);
            return operation.apply();
        }
    }
}
