package Electricity.util;

import java.io.File;

/**
 * Utility class for handling resources in the application
 */
public class ResourceUtil {
    
    /**
     * Gets the absolute path to a resource in the resources directory
     * 
     * @param resourcePath Path relative to resources directory (e.g., "images/hicon2.jpg")
     * @return File object representing the resource, or null if not found
     */
    public static File getResourcePath(String resourcePath) {
        try {
            String projectRoot = System.getProperty("user.dir");
            File resourceFile = new File(projectRoot + "/resources/" + resourcePath);
            
            if (resourceFile.exists()) {
                System.out.println("Resource found at: " + resourceFile.getAbsolutePath());
                return resourceFile;
            } else {
                System.out.println("Resource not found at: " + resourceFile.getAbsolutePath());
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error finding resource: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Gets the absolute path string to a resource in the resources directory
     * 
     * @param resourcePath Path relative to resources directory (e.g., "images/hicon2.jpg")
     * @return String with the absolute path to the resource, or null if not found
     */
    public static String getResourcePathString(String resourcePath) {
        File file = getResourcePath(resourcePath);
        return file != null ? file.getAbsolutePath() : null;
    }
}