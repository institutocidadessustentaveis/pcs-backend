package br.org.cidadessustentaveis.recaptcha;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.List;

public class HttpRequest {

    public static String doJsonPost(String uri, List<NameValuePair> formData) {
        String responseContent = "";

        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(uri);
            UrlEncodedFormEntity urlFormEntity = new UrlEncodedFormEntity(formData, Consts.UTF_8);
            httpPost.setEntity(urlFormEntity);

            CloseableHttpResponse response = httpclient.execute(httpPost);

            if(response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                responseContent = IOUtils.toString(entity.getContent(), "UTF-8");
            }

            response.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseContent;
    }
}
