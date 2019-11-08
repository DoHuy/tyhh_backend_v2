package com.stadio.cms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stadio.common.utils.HttpClient;
import org.asynchttpclient.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class VimeoRequest {

    public int getVimeoVideoDurration(String vimeoId, String vimeoToken) throws ExecutionException, InterruptedException, IOException {

        String url =  "https://api.vimeo.com/videos/" + vimeoId;
        AsyncHttpClient c = Dsl.asyncHttpClient();
        RequestBuilder requestBuilder = new RequestBuilder();
        requestBuilder.setHeader("Authorization","Bearer "+ vimeoToken);
        requestBuilder.setUrl(url);
        requestBuilder.setMethod("GET");

        Future<Response> whenResponse = c.prepareRequest(requestBuilder).execute();
        Response response = whenResponse.get();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode data = objectMapper.readTree(response.getResponseBody()).get("duration");

        return data.numberValue().intValue();

    }
}
