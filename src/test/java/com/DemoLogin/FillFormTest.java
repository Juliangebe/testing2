package com.DemoLogin;

import PageObjects.DemoRegisterFormPage;
import Utilities.BaseClass;
import org.testng.annotations.Test;

public class FillFormTest extends BaseClass {

    DemoRegisterFormPage demoRegisterFormPage;

    @Test
    public void clicOnAlert()  {
        demoRegisterFormPage = new DemoRegisterFormPage();
        driver.get("https://demo.automationtesting.in/Register.html");
        demoRegisterFormPage.fillForm();
    }
}
