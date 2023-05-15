package com.myproject.aem.core.servlets;

import com.adobe.aemds.guide.utils.GuideConstants;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONArray;
import org.osgi.service.component.annotations.Component;
import com.myproject.aem.core.servlets.Test;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = {Servlet.class}, property = {
        ServletResolverConstants.SLING_SERVLET_PATHS +"="+ "/bin/populatedropdown",
        ServletResolverConstants.SLING_SERVLET_METHODS + "="+ HttpConstants.METHOD_POST,
        ServletResolverConstants.SLING_SERVLET_NAME +"="+"populate state dropdowns based on country value"
})
public class DropDownPopulator extends SlingAllMethodsServlet {
    protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse res) throws ServletException, IOException {
    res.setHeader("Access-Control-Allow-Origin", "*");
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    Test test = new Test("My Test title");
    String testTitle = test.getTitle();
    System.out.println("sample title "+testTitle);


        try {
            //create a connection to CQ repository running on local host
            Repository repository = JcrUtils.getRepository("http://localhost:4503/crx/server");
            //create a session object
            Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));

            //create a node that represents root node
            Node root = session.getRootNode();
            //store content
            Node adobe = root.addNode("adobe");
            Node day = adobe.addNode("day");
            day.setProperty("message", "Adobe CQ Jagadeesh");

            //Retrieve node content
            Node node = session.getNode("adobe/day");
            System.out.println("node path "+node.getPath());
            System.out.println("node message property "+node.getProperty("message").getString());
            session.save();
            session.logout();

        } catch(Exception e) {
            e.printStackTrace();
        }

        System.out.println("dropdown servlet called");
        System.out.println("Jagadeesh");

    try {
        String US_STATES[] = {"0=Alabama",
                "1=Alaska",
                "2=Arizona",
                "3=Arkansas",
                "4=California",
                "5=Colorado",
                "6=Connecticut",
                "7=Delaware",
                "8=Florida",
                "9=Georgia",
                "10=Hawaii",
                "11=Idaho",
                "12=Illinois",
                "13=Indiana",
                "14=Iowa",
                "15=Kansas",
                "16=Kentucky",
                "17=Louisiana",
                "18=Maine",
                "19=Maryland",
                "20=Massachusetts",
                "21=Michigan",
                "22=Minnesota",
                "23=Mississippi",
                "24=Missouri",
                "25=Montana",
                "26=Nebraska",
                "27=Nevada",
                "28=New Hampshire",
                "29=New Jersey",
                "30=New Mexico",
                "31=New York",
                "32=North Carolina",
                "33=North Dakota",
                "34=Ohio",
                "35=Oklahoma",
                "36=Oregon",
                "37=Pennsylvania",
                "38=Rhode Island",
                "39=South Carolina",
                "40=South Dakota",
                "41=Tennessee",
                "42=Texas",
                "43=Utah",
                "44=Vermont",
                "45=Virginia",
                "46=Washington",
                "47=West Virginia",
                "48=Wisconsin",
                "49=Wyoming"
        };
        String AUSTRALIAN_STATES[] = {"0=Ashmore and Cartier Islands",
                "1=Australian Antarctic Territory",
                "2=Australian Capital Territory",
                "3=Christmas Island",
                "4=Cocos (Keeling) Islands",
                "5=Coral Sea Islands",
                "6=Heard Island and McDonald Islands",
                "7=Jervis Bay Territory",
                "8=New South Wales",
                "9=Norfolk Island",
                "10=Northern Territory",
                "11=Queensland",
                "12=South Australia",
                "13=Tasmania",
                "14=Victoria",
                "15=Western Australia"};
            String country = req.getParameter("country");
        System.out.println("country name"+country);
        System.out.println("country length"+country.length());
        JSONArray stateJsonArray = new JSONArray();
        if (country.length() > 0) {
            if ("australia".equalsIgnoreCase(country)) {
                System.out.println("Australia if");
                stateJsonArray = new JSONArray();
                for (String state : AUSTRALIAN_STATES) {
                    stateJsonArray.put(state);
                }
            } else if ("unitedstates".equalsIgnoreCase(country)) {
                System.out.println("united states if");
                stateJsonArray = new JSONArray();
                for (String state: US_STATES) {
                    stateJsonArray.put(state);
                }
            }
            res.setContentType("application/json");
            res.getWriter().write(stateJsonArray.toString());
        }

    } catch(Exception e) {
        System.out.println("Error occurred");
    }
    }
}
