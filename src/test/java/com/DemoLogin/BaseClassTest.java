package com.DemoLogin;

import Utilities.BaseClass;
import org.testng.annotations.Test;

public class BaseClassTest extends BaseClass {

    @Test
    public void myTest(){

        driver.get("https://www.google.com.co/");

    }
}
