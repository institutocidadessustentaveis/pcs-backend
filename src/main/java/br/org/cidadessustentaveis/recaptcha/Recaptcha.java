package br.org.cidadessustentaveis.recaptcha;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Recaptcha {

    private Logger logger = LoggerFactory.getLogger(Recaptcha.class);

    private RecaptchaRequest request;

    public Recaptcha(RecaptchaRequest request) {
        this.request = request;
    }

    public boolean validate() {
        ObjectMapper mapper = new ObjectMapper();

        List<NameValuePair> form = new ArrayList<>();
        form.add(new BasicNameValuePair("secret", request.getSecret()));
        form.add(new BasicNameValuePair("response", request.getResponse()));

        String response = HttpRequest.doJsonPost("https://www.google.com/recaptcha/api/siteverify", form);

        try {
            RecaptchaResponse recaptchaResponse =
                    mapper.readValue(response, RecaptchaResponse.class);

            return recaptchaResponse.isSuccess();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return false;
    }


}
