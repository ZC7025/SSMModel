package com.zc.common;

import org.springframework.web.context.ContextLoader;

import java.io.File;

public class FileUtils {

    public static String getBpmnDir() {
        String dir = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/") + "uploads/bpmn";
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();
    }

    public static String getNameWithoutSuffix(String filename) {
        return filename.replace(".zip", "");
    }

}
