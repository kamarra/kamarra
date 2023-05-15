/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.myproject.aem.core.servlets;

import com.adobe.aemds.guide.model.BreakPointResponsiveConfiguration;
import com.adobe.aemfd.docmanager.Document;
import com.adobe.fd.forms.api.FormsService;
import com.adobe.fd.forms.api.DataFormat;
import com.adobe.fd.forms.api.FormsServiceException;
import com.adobe.fd.fp.common.FileAttachmentWrapper;
import com.adobe.fd.output.api.PDFOutputOptions;
import com.adobe.fd.pdfutility.services.PDFUtilityService;
import com.adobe.fd.pdfutility.services.client.PDFPropertiesResult;
import com.adobe.fd.pdfutility.services.client.PDFUtilityException;
import com.adobe.forms.common.service.DataXMLOptions;
import com.day.cq.commons.jcr.JcrConstants;

import javax.jcr.Repository;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;

import javax.jcr.RepositoryException;
import javax.jcr.SimpleCredentials;
import javax.print.Doc;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service = {Servlet.class}, property = {
        ServletResolverConstants.SLING_SERVLET_NAME + "=" + "Demo Json Servlet",
        ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
        ServletResolverConstants.SLING_SERVLET_PATHS + "=" + "/bin/trainingProject/testservlet"

})
@ServiceDescription("DemoJsonServlet")
public class SimpleServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Reference
    PDFUtilityService pdfUtilityService;

    @Reference
    FormsService formsService;

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.getWriter().print("servlet called");
        File file = new File("C:\\sampleDOR.pdf");
        Document document = new Document(file);
        String filePath = "C:\\test\\hello.xdp";

        try {
            Document newDocument = pdfUtilityService.convertPDFtoXDP(document);
            newDocument.copyToFile(new File(filePath));
        } catch (PDFUtilityException e) {
            System.out.println("PDF Exception occurred");
        }

        //File pdfFile = new File("C:\\pwd\\bg.pdf");
        Document pdfFile = new Document(new File("C:\\pwd\\bg.pdf"));
        DataFormat dataFormat = DataFormat.XmlData;
        Document exportedDataFile = null;
        try {
            exportedDataFile = formsService.exportData(pdfFile, dataFormat);
            exportedDataFile.copyToFile(new File("C:\\pwd\\bg.xml"));
        } catch (FormsServiceException e) {
            e.printStackTrace();
        }

        String reffer = req.getParameter("inputData");
        //resp.getWriter().println(reffer);
        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();
        String json = gson.toJson(reffer);
        //resp.getWriter().println(json);

        try {
            Repository repository = JcrUtils.getRepository("http://localhost:4502/crx/server/");
            resp.getWriter().println(repository);
            javax.jcr.Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        } catch (RepositoryException e) {
            System.out.println(e);
        }


        //final Resource resource = req.getResource();
        //resp.setContentType("text/plain");
        //resp.setHeader("Content-Type","text/html");
        //resp.getWriter().write("Title = " + resource.getValueMap().get(JcrConstants.JCR_TITLE));
        //resp.getWriter().print("<h1>sling servlet called</h1>");
        //resp.getWriter().close();

    }
}
