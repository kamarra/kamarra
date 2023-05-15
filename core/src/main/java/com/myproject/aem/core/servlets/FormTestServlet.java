package com.myproject.aem.core.servlets;

import com.adobe.aemds.formset.common.FormResource;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import org.osgi.service.component.annotations.Reference;

@Component(service = {Servlet.class}, property = {
        ServletResolverConstants.SLING_SERVLET_PATHS +"="+ "/bin/formTestServlet",
        ServletResolverConstants.SLING_SERVLET_METHODS + "="+ HttpConstants.METHOD_POST,
        ServletResolverConstants.SLING_SERVLET_NAME +"="+"test the servlet code"
})
public class FormTestServlet extends SlingAllMethodsServlet {
    private static final long serialVersionUID = 1L;

    @Reference
    FormResource formResource;
    @Override
    protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse res) throws ServletException, IOException {

        System.out.println(formResource.getXMLRoot());
    }
}
