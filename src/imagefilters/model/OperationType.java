/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.model;

/**
 *
 * @author asilkaratas
 */
public enum OperationType
{
    THRESHOLDING("thresholding"),
    NORMALIZATION("normalization"),
    BRIGHTNESS("brightness"),
    INVERSION("inversion"),
    GRAYSCALE("grayscale"),
    HIGHPASS("highpass"),
    LOWPASS("lowpass"),
    GAUSSIAN("gaussian"),
    MEDIAN("median"),
    EROSION("erosion"),
    DILATION("dilation"),
    SOBEL("sobel"),
    SOBEL2("sobel2"),
    KMM("kmm"),
    KM3("km3");
    
    private final String name;
    private OperationType(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
}
