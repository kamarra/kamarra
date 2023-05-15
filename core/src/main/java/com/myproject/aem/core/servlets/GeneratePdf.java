package com.myproject.aem.core.servlets;

import javax.servlet.Servlet;
import javax.servlet.http.Part;

import com.adobe.aemds.formset.common.SubmitResult;
import com.adobe.aemds.guide.addon.dor.DoRUtils;
import com.adobe.aemds.guide.model.FormSubmitInfo;
import com.adobe.aemds.guide.service.AdaptiveFormConfigurationService;
import com.adobe.aemds.guide.utils.GuideSubmitUtils;
import com.adobe.aemds.guide.utils.GuideUtils;
import com.adobe.fd.output.api.PDFOutputOptions;
import com.adobe.forms.common.service.FileAttachmentWrapper;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.aemfd.docmanager.Document;
import com.adobe.fd.forms.api.FormsService;
import com.adobe.aemds.guide.addon.dor.DoROptions;
import com.adobe.forms.common.submitutils.ParameterMap;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.request.RequestParameterMap;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component(service={Servlet.class}, property={"sling.servlet.methods=post", "sling.servlet.paths=/bin/generatePdf"})
public class GeneratePdf extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;
    @Reference
    FormsService formsService;

    @Reference
    private AdaptiveFormConfigurationService adaptiveFormConfig;

    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {

        String file_path = request.getParameter("save_location");

        InputStream pdf_document_is = null;
        InputStream xml_is = null;
        Part pdf_document_part = null;
        Part xml_data_part = null;
        try {
            pdf_document_part = request.getPart("pdf_file");
            xml_data_part = request.getPart("xml_data_file");
            pdf_document_is = pdf_document_part.getInputStream();
            xml_is = xml_data_part.getInputStream();
            Document data_merged_document = formsService.importData(new Document(pdf_document_is), new Document(xml_is));
            data_merged_document.copyToFile(new File(file_path));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



