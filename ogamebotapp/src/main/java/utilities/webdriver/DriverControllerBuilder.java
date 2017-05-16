package utilities.webdriver;

import org.openqa.selenium.Point;

import java.awt.Dimension;
import java.util.UUID;

/**
 * Created by jarndt on 5/8/17.
 */
public class DriverControllerBuilder {
    private String driverString, webDriverPath, proxy, driverPath, driverName;
    private Dimension windowSize = new java.awt.Dimension(1440,900);
    private Point windowPosition = new Point(0,0);

    public DriverControllerBuilder(){
        this.driverName = UUID.randomUUID().toString();
    }public DriverControllerBuilder setName(String name){
        this.driverName = name;
        return this;
    }public DriverControllerBuilder setWebDriverPath(String webDriverPath){
        this.webDriverPath = webDriverPath;
        return this;
    }public DriverControllerBuilder setProxy(String proxy){
        this.proxy = proxy;
        return this;
    }public DriverControllerBuilder setWindowSize(Dimension dimension){
        this.windowSize = dimension;
        return this;
    }public DriverControllerBuilder setWindowPosition(org.openqa.selenium.Point position){
        this.windowPosition = position;
        return this;
    }

    public DriverController build(){
        return new DriverController(driverString,webDriverPath,proxy,driverPath,driverName,windowSize,windowPosition);
    }
}