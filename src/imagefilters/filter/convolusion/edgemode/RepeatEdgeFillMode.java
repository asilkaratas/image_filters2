/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.filter.convolusion.edgemode;

import imagefilters.util.PixelUtil;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author asilkaratas
 */
public class RepeatEdgeFillMode implements EdgeFillMode
{
    @Override
    public void fill(int[] enlargedPixels, int[] originalPixels, int originalWidth, int originalHeight, int enlargedWidth, int enlargedHeight, Point pivot,  int kernelWidth, int kernelHeight)
    {
        //top
        int top = pivot.y;
        
        Rectangle topRectangle = new Rectangle(0, 0, originalWidth, 1);
        int[] topPixels = PixelUtil.copyPixelsFromRect(originalPixels, originalWidth, originalHeight, topRectangle);
        
        Rectangle topDestinationRectangle = new Rectangle(pivot.x, 0, originalWidth, 1);
        for(int i = 0; i < top; ++i)
        {
            PixelUtil.pastePixelsToRect(topPixels, topRectangle.width, topRectangle.height, enlargedPixels, enlargedWidth, enlargedHeight, topDestinationRectangle);
            topDestinationRectangle.y ++;
        }
        
        //bottom
        int bottom = kernelHeight - pivot.y;
        
        Rectangle bottomRectangle = new Rectangle(0, originalHeight-1, originalWidth, 1);
        int[] bottomPixels = PixelUtil.copyPixelsFromRect(originalPixels, originalWidth, originalHeight, bottomRectangle);
        
        Rectangle bottomDestinationRectangle = new Rectangle(pivot.x, pivot.y + originalHeight, originalWidth, 1);
        for(int i = 0; i < bottom; ++i)
        {
            PixelUtil.pastePixelsToRect(bottomPixels, bottomRectangle.width, bottomRectangle.height, enlargedPixels, enlargedWidth, enlargedHeight, bottomDestinationRectangle);
            bottomDestinationRectangle.y ++;
        }
        
        //left
        int left = pivot.x;
        
        System.out.println("left:" + left);
        
        Rectangle leftRectangle = new Rectangle(pivot.x, 0, 1, enlargedHeight);
        int[] leftPixels = PixelUtil.copyPixelsFromRect(enlargedPixels, enlargedWidth, enlargedHeight, leftRectangle);
        
        Rectangle destinationLeftRectangle = new Rectangle(0, 0, 1, enlargedHeight);
        
        for(int i = 0; i < left; ++i)
        {
            PixelUtil.pastePixelsToRect(leftPixels, leftRectangle.width, leftRectangle.height, enlargedPixels, enlargedWidth, enlargedHeight, destinationLeftRectangle);
            destinationLeftRectangle.x++;
        }
        
        //right
        int right = kernelWidth - pivot.x;
        
        System.out.println("right:" + right);
        
        Rectangle rightRectangle = new Rectangle(enlargedWidth - right -1, 0, 1, enlargedHeight);
        int[] rightPixels = PixelUtil.copyPixelsFromRect(enlargedPixels, enlargedWidth, enlargedHeight, rightRectangle);
        
        Rectangle rightDestinationRectangle = new Rectangle(enlargedWidth -right, 0, 1, enlargedHeight);
        for(int i = 0; i < right; ++i)
        {
            PixelUtil.pastePixelsToRect(rightPixels, rightRectangle.width, rightRectangle.height, enlargedPixels, enlargedWidth, enlargedHeight, rightDestinationRectangle);
            rightDestinationRectangle.x ++;
        }
    }
}
