package com.stadio.common.request;

import com.stadio.common.utils.StringUtils;
import org.asynchttpclient.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class SSORequest {

    public Response revokeToken(String serverHost, String yourToken, String userIdToRevoke) throws ExecutionException, InterruptedException, IOException {
        //Revoke token

        String url = serverHost + "/oauth/token/revoke";
        if (StringUtils.isNotNull(userIdToRevoke)) {
            url = url + "?userId=" + userIdToRevoke;
        }
        AsyncHttpClient c = Dsl.asyncHttpClient();
        RequestBuilder requestBuilder = new RequestBuilder();
        requestBuilder.setHeader("Authorization",yourToken);
        requestBuilder.setUrl(url);
        Response response = c.prepareRequest(requestBuilder).execute().get();
        c.close();
        return response;
    }

}
