package org.wso2.carbon.stat.publisher.ui;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by nipuna on 8/15/14.
 */


public class URLValidationServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {


        String URLS = req.getParameter("url_address");


        StatPublisherClient client;
        boolean validateResponse;
        try {
            client = StatPublisherClient.getStatPublisherClient();
            validateResponse = client.URLValidator(URLS);
            resp.getWriter().print(String.valueOf(validateResponse));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
