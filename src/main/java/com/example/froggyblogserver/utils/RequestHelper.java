package com.example.froggyblogserver.utils;

import com.example.froggyblogserver.exception.ValidateException;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
@Component
public class RequestHelper {
    private static ReadableUserAgent getUserAgent(HttpServletRequest request){
        UserAgentStringParser uasParser = UADetectorServiceFactory.getCachingAndUpdatingParser();
        var userAgentHeader = request.getHeader("User-Agent");
        try{
            return uasParser.parse(userAgentHeader);
        }catch (Exception e){
            throw new ValidateException(e.getMessage());
        }

    }
    public static String getOperating(HttpServletRequest request){
        var userAgent = getUserAgent(request);
        return userAgent.getOperatingSystem().getName();
    }
    public static String getBrowser(HttpServletRequest request){
        var userAgent = getUserAgent(request);
        return userAgent.getName();
    }
}
