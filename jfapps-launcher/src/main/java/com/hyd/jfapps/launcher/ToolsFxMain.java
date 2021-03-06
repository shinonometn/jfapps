package com.hyd.jfapps.launcher;

import com.hyd.jfapps.appbase.GlobalContext;
import com.hyd.jfapps.launcher.appmanager.AppManager;
import javafx.application.Application;

public class ToolsFxMain {

    public static void main(String[] args) throws Exception {
        AppManager.GLOBAL_CONTEXT.put(GlobalContext.APP_ICON, Icons.icon("/logo.png"));
        Application.launch(ToolsFxApplication.class, args);
    }
}
