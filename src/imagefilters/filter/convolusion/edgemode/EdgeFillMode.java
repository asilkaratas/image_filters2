/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.filter.convolusion.edgemode;

import java.awt.Point;

/**
 *
 * @author asilkaratas
 */
public interface EdgeFillMode
{
    public void fill(int[] enlargedPixels, int[] originalPixels, int originalWidth, int originalHeight, int enlargedWidth, int enlargedHeight, Point pivot, int kernelWidth, int kernelHeight);
}
