package com.stadio.common.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stadio.common.utils.FileUtils;
import com.stadio.common.utils.ResponseCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.asynchttpclient.request.body.multipart.FilePart;

import java.io.File;
import java.util.concurrent.Future;

public class MediaUploadRequest {

    private static Logger logger = LogManager.getLogger(MediaUploadRequest.class);

    public String uploadDocument(String serverHost, String yourToken, File document, boolean isSecret) throws Exception {
        //Revoke token

        AsyncHttpClient c = Dsl.asyncHttpClient();
        RequestBuilder requestBuilder = new RequestBuilder();
        requestBuilder.setHeader("Authorization", yourToken);
        requestBuilder.setUrl(serverHost + "/api/media/document/upload");
        requestBuilder.setMethod("POST");
        FilePart part = null;
        try {
            part = new FilePart("file", document);
        } catch (Exception e) {
            logger.error("upload document error", e);
        }
        requestBuilder.addBodyPart(part);
        requestBuilder.addQueryParam("isSecret", String.valueOf(isSecret));

        String url = null;
        Future<Response> whenResponse = c.prepareRequest(requestBuilder).execute();
        try {
            Response response = whenResponse.get();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode data = objectMapper.readTree(response.getResponseBody());

            JsonNode imageData = data.get("data");
            url = imageData.get("url").textValue();

        } catch (Exception e) {
            logger.error("upload document error", e);
            return null;
        } finally {
            try {
                c.close();
            } catch (Exception e){}
            return url;
        }
    }
}
