package org.wso2.carbon.stat.publisher.ui;

import java.net.Socket;

/**
 * Created by nipuna on 9/11/14.
 */
public class TestServerAjaxProcessorHelper {


    public boolean isNotNullOrEmpty(String string){
        return string != null && !string.equals("");
    }

    public String backendServerExists(String URL) {
       String response = "true";
        Socket serverSocket;
        String[] URLArray = URL.split(";");


        for (int count = 0; count < URLArray.length; count++) {

            try {
                String serverIp = URLArray[count].split("://")[1].split(":")[0];
                String authPort = URLArray[count].split("://")[1].split(":")[1];


                serverSocket = new Socket(serverIp, Integer.parseInt(authPort));


            } catch (Exception e) {
                response = "false";

            }


        }


        return response;
    }
}
