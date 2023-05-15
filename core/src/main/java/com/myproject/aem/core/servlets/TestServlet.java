package com.myproject.aem.core.servlets;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = {Servlet.class}, property = {
        ServletResolverConstants.SLING_SERVLET_PATHS +"="+ "/bin/testServlet",
        ServletResolverConstants.SLING_SERVLET_METHODS + "="+ HttpConstants.METHOD_POST,
        ServletResolverConstants.SLING_SERVLET_NAME +"="+"test the servlet code"
})
public class TestServlet extends SlingAllMethodsServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(TestServlet.class);
    @Override
    protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse res) throws ServletException, IOException {
        try {
            logger.info("This is info log Jagadeesh");
            logger.debug("This is debug log Jagadeesh");
            logger.error("this is error log");
            System.out.println("entered");
            ResourceResolver resourceResolver = req.getResourceResolver();
            System.out.println(resourceResolver);
            Session session = resourceResolver.adaptTo(Session.class);
            System.out.println(session);
            //Repository repository = JcrUtils.getRepository();
            //System.out.println(repository);
            Repository repository = session.getRepository();
            Node root = session.getRootNode();
            Node adobe = root.addNode("adobe");
            Node day = root.addNode("adobe/day");
            day.setProperty("message", "sadfaf");
            Node node = root.getNode("adobe/day");
            System.out.println(node.getPath());
            System.out.println(node.getProperty("message").getString());
            System.out.println("success");
            session.save();
            session.logout();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
