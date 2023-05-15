package com.myproject.aem.core.servlets;

import org.apache.sling.api.resource.ResourceResolver;

public class Test {
    private String title = null;
    public Test(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
}
