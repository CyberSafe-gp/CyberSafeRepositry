package com.example.cybersafe;


import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class ReceiveData extends AsyncTask<Void, Void, Void> {

    String text;

    public ReceiveData() {

    }

    public ReceiveData(String text) {
        this.text = text;
    }

    @Override
    protected Void doInBackground(Void... voids) {

try {
    URL url = new URL("https://cyberdetector.herokuapp.com");
    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
    System.out.println("!!!!!");
    int responseCode = httpConn.getResponseCode();

    // always check HTTP response code first
    if (responseCode == HttpURLConnection.HTTP_OK) {
//            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
        System.out.println("disposition "+disposition);
            String contentType = httpConn.getContentType();
        System.out.println("contentType "+contentType);

        int contentLength = httpConn.getContentLength();
            System.out.println("contentLength "+contentLength);
//
//            if (disposition != null) {
//                // extracts file name from header field
//                int index = disposition.indexOf("filename=");
//                if (index > 0) {
//                    fileName = disposition.substring(index + 10,
//                            disposition.length() - 1);
//                }
//            } else {
//                // extracts file name from URL
//                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
//                        fileURL.length());
//            }
//
//            System.out.println("Content-Type = " + contentType);
//            System.out.println("Content-Disposition = " + disposition);
//            System.out.println("Content-Length = " + contentLength);
//            System.out.println("fileName = " + fileName);
//
//            // opens input stream from the HTTP connection
        System.out.println("#######");

            InputStream inputStream = httpConn.getInputStream();
        System.out.println("#######");
        String reed = readStream(inputStream);
        System.out.println("####### "+reed);



        System.out.println("@@@@@@@@@@");

        System.out.println("#######");
        //httpConn.setDoOutput(true);
        System.out.println("1111");
        httpConn.setRequestMethod("POST");
//        httpConn.setRequestProperty("Content-Type", "text/plain");

        String input = "You are ugly";
        System.out.println("22222");
        OutputStream os = httpConn.getOutputStream();
        System.out.println("@@@@@@@@@");
        os.write(input.getBytes());
        System.out.println("@@@@@@@@@");
        os.flush();

  /*      OutputStream outStream = httpConn.getOutputStream();
        System.out.println("@@@@@@@@@");
        writeStream(outStream);
        System.out.println("@@@@@@ ");*/
//            String saveFilePath = saveDir + File.separator + fileName;
//
//            // opens an output stream to save into file
//            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
//
//            int bytesRead = -1;
//            byte[] buffer = new byte[BUFFER_SIZE];
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//
//            outputStream.close();
//            inputStream.close();

        System.out.println("File downloaded");
    } else {
        System.out.println("No file to download. Server replied HTTP code: " + responseCode);
    }
    httpConn.disconnect();
} catch (MalformedURLException e) {

    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
}


       /* try {

            URL url = new URL("https://www.google.com");
            System.out.println("########");
            URLConnection connection = url.openConnection();

            System.out.println("1111111111");
            connection.setDoOutput(true);
            System.out.println("222222222");
            OutputStreamWriter out = new OutputStreamWriter(
                    connection.getOutputStream());
            System.out.println("333333");
            out.close();
            System.out.println("444444");

            InputStreamReader input = new InputStreamReader(connection.getInputStream());
            System.out.println("!!!!!!!");

            BufferedReader in = new BufferedReader(input);


            System.out.println("5555555");
            String decodedString;
            while ((decodedString = in.readLine()) != null) {
                System.out.println("@@@@@@@@@  "+decodedString);
            }
            System.out.println("6666666");
            in.close();

        } catch (IOException  e) {
            System.out.println("EEEEERRROOOORR");
            e.printStackTrace();

        }*/
        return null;
    }
    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    private void writeStream(OutputStream out) throws IOException {
        String output = "You are ugly";

        out.write(output.getBytes());
        out.flush();
    }
/*
    String response = "";
    String SinceTime;
    String GoesAddress;
    Context myContext;

    ReceiveData(Context context, String since, String goes) {
        this.myContext = context;
        SinceTime = since;
        GoesAddress = goes;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder feedback = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                feedback.append("&");

            feedback.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            feedback.append("=");
            feedback.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return feedback.toString();
    }

    public void getData() throws IOException {

        HashMap<String, String> params = new HashMap<>();
        params.put("DCPID", GoesAddress);
        params.put("SINCE", SinceTime);

        URL url = new URL("https://cyberdetector.herokuapp.com");
        HttpURLConnection client = null;
        try {
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            // You need to specify the context-type.  In this case it is a
            // form submission, so use "multipart/form-data"
            client.setRequestProperty("multipart/form-data", "https://eddn.usgs.gov/fieldtest.html;charset=UTF-8");
            client.setDoInput(true);
            client.setDoOutput(true);

            OutputStream os = client.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(params));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = client.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            }
            else {
                response = "";
            }
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        finally {
            if(client != null) // Make sure the connection is not null.
                client.disconnect();
        }
    }

    @Override
    protected Long doInBackground(URL... params) {
        try {
            getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // This counts how many bytes were downloaded
        final byte[] result = response.getBytes();
        Long numOfBytes = Long.valueOf(result.length);
        return numOfBytes;
    }

    protected void onPostExecute(Long result) {
        System.out.println("Downloaded " + result + " bytes");
        // This is just printing it to the console for now.
        System.out.println(response);
        // In the following two line I pass the string elsewhere and decode it.
//        InputCode input = new InputCode();
//        input.passToDisplay(myContext, response);
    }*/
}