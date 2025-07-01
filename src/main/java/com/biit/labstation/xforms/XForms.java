package com.biit.labstation.xforms;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.ToolTest;
import com.biit.labstation.components.Login;
import com.biit.labstation.components.Popup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class XForms extends ToolTest {

    @Value("${testing.server.domain}")
    private String serverDomain;

    @Value("${xforms.context}")
    private String context;

    public XForms(CustomChromeDriver customChromeDriver, Login login, Popup popup) {
        super(customChromeDriver, login, popup);
    }

    @Override
    public void access() {
        access(serverDomain, context);
    }
}
