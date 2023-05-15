package com.myproject.aem.core.services.impl;
import com.adobe.aemfd.docmanager.Document;
import com.adobe.fd.output.api.AcrobatVersion;
import com.adobe.fd.output.api.OutputService;
import com.adobe.fd.output.api.OutputServiceException;
import com.adobe.fd.output.api.PDFOutputOptions;
import com.myproject.aem.core.services.MyFirstInterface;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component (service = MyFirstInterface.class)
public class MyFirstInterfaceImpl implements MyFirstInterface {
    @Reference
    OutputService outputService;
    private static final Logger logger = LoggerFactory.getLogger(MyFirstInterfaceImpl.class);

    @Override
    public Document mergeDataWithXdpTemplate(Document xdpTemplate, Document xmlDocument) {
        PDFOutputOptions pdfOutputOptions = new PDFOutputOptions();
        pdfOutputOptions.setAcrobatVersion(AcrobatVersion.Acrobat_11);
        AcrobatVersion acrobatVersion = pdfOutputOptions.getAcrobatVersion();


        try {
            return outputService.generatePDFOutput(xdpTemplate, xmlDocument, pdfOutputOptions);
        } catch (OutputServiceException e) {
            logger.error("Failed to merge the data in the xdp", e);
        }
        return null;
    }

}
