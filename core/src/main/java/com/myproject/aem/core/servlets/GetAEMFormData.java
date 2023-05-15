package com.myproject.aem.core.servlets;

import com.adobe.fd.output.api.AcrobatVersion;
import com.adobe.fd.output.api.PDFOutputOptions;
import com.adobe.fd.pdfutility.services.PDFUtilityService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service={Servlet.class}, property={"sling.servlet.methods=post", "sling.servlet.paths=/bin/getFormData"})
public class GetAEMFormData extends SlingAllMethodsServlet {

    @Override
    protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse res)  {
        PDFOutputOptions pdfOutputOptions = new PDFOutputOptions();
        System.out.println(pdfOutputOptions.getAcrobatVersion());
        AcrobatVersion acrobatVersion = pdfOutputOptions.getAcrobatVersion();

    }
 }
