package com.myproject.aem.core.servlets;

import javax.servlet.Servlet;

import com.adobe.fd.output.api.PDFOutputOptions;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.aemfd.docmanager.Document;
import com.adobe.fd.forms.api.FormsService;

import java.io.File;
import java.io.IOException;

@Component(service={Servlet.class}, property={"sling.servlet.methods=post", "sling.servlet.paths=/bin/mergedataWithAcroform"})
public class MyFirstAEMFormsServlet extends SlingAllMethodsServlet
{

    private static final long serialVersionUID = 1L;
    @Reference
    FormsService formsService;
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
    {
        //-------------------
        PDFOutputOptions pdf_output_options = new PDFOutputOptions();
        System.out.println("Acrobat pdf vesion "+pdf_output_options.getAcrobatVersion());
        //--------------------------
        String file_path = request.getParameter("save_location");

        java.io.InputStream pdf_document_is = null;
        java.io.InputStream xml_is = null;
        javax.servlet.http.Part pdf_document_part = null;
        javax.servlet.http.Part xml_data_part = null;
        try
        {
            pdf_document_part = request.getPart("pdf_file");
            xml_data_part = request.getPart("xml_data_file");
            pdf_document_is = pdf_document_part.getInputStream();
            xml_is = xml_data_part.getInputStream();
            Document data_merged_document = formsService.importData(new Document(pdf_document_is), new Document(xml_is));
            data_merged_document.copyToFile(new File(file_path));


        }
        catch(Exception e)
        {
            try {
                response.sendError(400,e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

