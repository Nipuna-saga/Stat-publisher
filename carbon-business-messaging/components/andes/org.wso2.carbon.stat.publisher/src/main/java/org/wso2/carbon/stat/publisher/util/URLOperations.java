package org.wso2.carbon.stat.publisher.util;

import java.net.Socket;


public class URLOperations {

    //URL validation method
    public boolean URLValidator(String URL) {

        boolean response = true;

        String[] URLArray = URLSplitter(URL);
        for (int count = 0; count < URLArray.length; count++) {

            try {

                String[] URLParts = URLArray[count].split(":");
                String IPAddress = URLParts[0];
                int portNumber = Integer.parseInt(URLParts[1]);


                Socket serverSocket = new Socket(IPAddress, portNumber);
            } catch (Exception e) {
                response = false;
            }


        }


        return response;


    }

    //Url split method
    public String[] URLSplitter(String URL) {


        return URL.split(";");


    }
}
