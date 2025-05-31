package com.biit.labstation.tests;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.ScreenShooter;

public interface ITestWithWebDriver {

    CustomChromeDriver getDriver();

    ScreenShooter getScreenShooter();
}
