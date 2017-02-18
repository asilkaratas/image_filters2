package imagefilters.model;

import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;

/**
 * Created by asilkaratas on 1/20/15.
 */
public class ImageExtensions
{
    private static List<FileChooser.ExtensionFilter> extensionFilters;

    private static String[] extensions = new String[] {"bmp", "png", "jpg", "jpeg", "gif", "tiff", "tif"};

    public static List<? extends FileChooser.ExtensionFilter> getExtensionFilters()
    {
        if(extensionFilters == null)
        {
            extensionFilters = new ArrayList<FileChooser.ExtensionFilter>();
            for(String extension : extensions)
            {
                extensionFilters.add(new FileChooser.ExtensionFilter(extension, "*." + extension));
            }
        }

        return extensionFilters;
    }
    
    public static FileChooser.ExtensionFilter getImageFilter()
    {
        FileChooser.ExtensionFilter filters = new FileChooser.ExtensionFilter("Images", "*.bmp", "*.png", 
                                                                    "*.jpg", "*.jpeg", "*.gif", "*.tiff", "*.tif");
        
        return filters;
    }

    public static FileChooser.ExtensionFilter getExtensionFilter(String extension)
    {
        List<? extends FileChooser.ExtensionFilter> filters = getExtensionFilters();


        for(FileChooser.ExtensionFilter extensionFilter : filters)
        {
            if(extensionFilter.getDescription().equals(extension))
            {
                return extensionFilter;
            }
        }

        return filters.get(0);
    }

    public static final String[] getExtensions()
    {
        return extensions;
    }

    public static String getExtension(String filename)
    {
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        return extension;
    }
}
