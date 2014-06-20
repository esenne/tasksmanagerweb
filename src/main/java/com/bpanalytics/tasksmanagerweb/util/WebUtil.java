package com.bpanalytics.tasksmanagerweb.util;

import java.util.Collection;

public class WebUtil {
	
    public static boolean validateResult(Collection<?> content) {
        return content != null && !content.isEmpty();
    }
    
    public static boolean validateResult(Object content) {
        return content != null;
    }
}
