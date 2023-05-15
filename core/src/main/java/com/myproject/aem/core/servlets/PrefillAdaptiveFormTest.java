package com.myproject.aem.core.servlets;

import com.adobe.forms.common.service.*;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.jcr.Session;
import javax.xml.crypto.Data;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.sling.commons.json.xml.XML;

public class PrefillAdaptiveFormTest implements DataProvider {

    public String getServiceName() {
        return "Default Prefill Service";
    }
    public String getServiceDescription() {
        return "this is default prefill service to prefill user data in adaptive form";
    }

    @Override
    public PrefillData getPrefillData(final DataOptions dataOptions) throws FormsException {
    PrefillData prefillData = new PrefillData() {
        @Override
        public InputStream getInputStream() {
            return getData(dataOptions);
        }
        public ContentType getContentType() {
            return ContentType.XML;
        }
    };
    return prefillData;
    }

    public InputStream getData(DataOptions dataOptions) throws FormsException{

        try {
            Resource aemformsContainer = dataOptions.getFormResource();
            ResourceResolver resourceResolver = aemformsContainer.getResourceResolver();
            Session session = resourceResolver.adaptTo(Session.class);
            UserManager userManager = ((JackrabbitSession)session).getUserManager();
            Authorizable loggedinUser = userManager.getAuthorizable(session.getUserID());
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("data");
            doc.appendChild(rootElement);
            Element firstNameElement = doc.createElement("fname");
            firstNameElement.setTextContent(loggedinUser.getProperty("profile/givenName")[0].getString());
            InputStream inputStream = new ByteArrayInputStream(rootElement.getTextContent().getBytes());
            System.out.println(aemformsContainer);
            return inputStream;


        } catch (Exception e) {
            throw new FormsException(e);
        }

    }

}
