/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.filter;

import java.awt.image.BufferedImage;

/**
 *
 * @author asilkaratas
 */
public abstract class ImageFilter
{
    private String name;
    public ImageFilter(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
    
    public abstract BufferedImage apply(BufferedImage originalImage);
    public abstract ImageFilter clone();
    
    @Override
    public String toString()
    {
        return name;
    }
}
