package Electricity.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

public class ResourceUtilTest {

    @Test
    public void testGetResourcePath() {
        // Test getting path to resources directory
        String resource = "images";
        String resourcePath = ResourceUtil.getResourcePathString(resource);
        assertNotNull(resourcePath, "Resource path should not be null");
        assertTrue(resourcePath.endsWith("resources" + File.separator + "images") || 
                   resourcePath.endsWith("resources" + File.separator + "images" + File.separator), 
                  "Resource path should end with 'resources/images'");
    }
    
    @Test
    public void testGetConfigPath() {
        // Test getting path to config directory
        String config = "config";
        String configPath = ResourceUtil.getResourcePathString(config);
        assertNotNull(configPath, "Config path should not be null");
        assertTrue(configPath.endsWith("resources" + File.separator + "config") || 
                   configPath.endsWith("resources" + File.separator + "config" + File.separator), 
                  "Config path should end with 'resources/config'");
    }
    
    @Test
    public void testGetImagePath() {
        // Test getting path to specific image file
        String image = "images/pop.png";
        String imagePath = ResourceUtil.getResourcePathString(image);
        assertNotNull(imagePath, "Image path should not be null");
        assertTrue(imagePath.endsWith("resources" + File.separator + "images" + File.separator + "pop.png"), 
                  "Image path should end with 'resources/images/pop.png'");
    }
    
    @Test
    public void testGetImageFile() {
        // Test getting path to specific image file
        String testImageName = "images/login.png";
        File imageFile = ResourceUtil.getResourcePath(testImageName);
        assertNotNull(imageFile, "Image file path should not be null");
        assertTrue(imageFile.exists(), "Image file should exist");
        assertEquals("login.png", imageFile.getName(), "Image filename should be 'login.png'");
    }
    
    @Test
    public void testGetConfigFile() {
        // Test getting path to specific config file
        String testConfigName = "config/db.properties";
        String configPath = ResourceUtil.getResourcePathString(testConfigName);
        assertNotNull(configPath, "Config file path should not be null");
        assertTrue(configPath.endsWith("resources" + File.separator + "config" + File.separator + "db.properties"), 
                  "Config file path should end with 'resources/config/db.properties'");
    }
    
    @Test
    public void testImageExists() {
        // Test that the common images exist
        File loginPng = ResourceUtil.getResourcePath("images/login.png");
        File billPng = ResourceUtil.getResourcePath("images/bill.png");
        
        assertNotNull(loginPng, "login.png resource should not be null");
        assertNotNull(billPng, "bill.png resource should not be null");
        assertTrue(loginPng.exists(), "login.png should exist");
        assertTrue(billPng.exists(), "bill.png should exist");
    }
    
    @Test
    public void testConfigExists() {
        // Test that the database config file exists
        File dbProperties = ResourceUtil.getResourcePath("config/db.properties");
        assertNotNull(dbProperties, "db.properties resource should not be null");
        assertTrue(dbProperties.exists(), "config/db.properties should exist");
    }
}