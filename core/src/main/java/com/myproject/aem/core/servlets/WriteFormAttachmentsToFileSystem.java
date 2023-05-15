package com.myproject.aem.core.servlets;


import com.adobe.aemfd.docmanager.Document;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.caconfig.annotation.Property;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Node;
import javax.jcr.Session;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component(service = WorkflowProcess.class, property = {"process.label = save form custom workflow"})
public class WriteFormAttachmentsToFileSystem implements WorkflowProcess {

    @Reference
    QueryBuilder queryBuilder;



    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap processArguments) throws WorkflowException {
        String[] params = processArguments.get("PROCESS_ARGS", "string").toString().split(",");
        String attachmentsPath = params[0];
        String saveToLocation = params[1];
        String payloadPath = workItem.getWorkflowData().getPayload().toString();
        Map<String, String> map = new HashMap<String, String>();
        map.put("path", payloadPath+"/"+attachmentsPath);
        File saveLocationFolder = new File(saveToLocation);

        if (!saveLocationFolder.exists()) {
            saveLocationFolder.mkdir();
        }
        map.put("type", "nt:file");
        Query query = queryBuilder.createQuery(PredicateGroup.create(map), workflowSession.adaptTo(Session.class));
        query.setStart(0);
        query.setHitsPerPage(20);
        SearchResult searchResult = query.getResult();
        Node attachmentNode = null;

        for (Hit hit: searchResult.getHits()) {
            try {
                String path = hit.getPath();
                attachmentNode = workflowSession.adaptTo(Session.class).getNode(path+"/jcr:content");
                InputStream inputStream = attachmentNode.getProperty("jcr:data").getBinary().getStream();
                Document attachmentDoc = new Document(inputStream);
                attachmentDoc.copyToFile(new File(saveLocationFolder+File.separator+hit.getTitle()));
                attachmentDoc.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}
