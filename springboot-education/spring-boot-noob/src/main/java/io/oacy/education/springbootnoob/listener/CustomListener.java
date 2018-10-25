package io.oacy.education.springbootnoob.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class CustomListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("监听器初始化...");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
