package br.org.cidadessustentaveis.util;

import br.org.cidadessustentaveis.dto.IPLookupDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class IPUtil {

    private IPUtil() {}

    public static IPLookupDTO lookup(String ip) {
        String ipLookupResponse = new RestClient().get("http://ip-api.com/json/" + ip);

        if(ipLookupResponse == null || ipLookupResponse.isEmpty()) return null;

        try {
            return new ObjectMapper().readValue(ipLookupResponse, IPLookupDTO.class);
        } catch (IOException e) {
            return null;
        }
    }

    public static String getIP() {
        String userIpAddress = getCurrentRequest().getHeader("X-Forwarded-For");

        if(userIpAddress == null) {
            userIpAddress = getCurrentRequest().getRemoteAddr();
        }

        return userIpAddress;
    }

    private static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.state(requestAttributes != null, "Could not find current request via RequestContextHolder");
        Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        Assert.state(servletRequest != null, "Could not find current HttpServletRequest");
        return servletRequest;
    }

}
