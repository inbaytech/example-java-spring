package com.inbaytech.oauth2.client.demo.controller;

import com.inbaytech.oauth2.client.demo.idq.IdqClient;
import com.inbaytech.oauth2.client.demo.Utils;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class RedirectController {
    private Logger logger = LoggerFactory.getLogger(RedirectController.class);

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public ModelAndView handleRedirect(HttpServletRequest request,
                                       ModelMap model) {

        logger.debug("creating OAuth authorization response wrapper (/redirect)");

        try {
            Utils utils = new Utils();
            IdqClient idqClient = utils.initialIdqClient();
            // Create the response wrapper
            OAuthAuthzResponse oar;
            oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);

            // Get Authorization Code
            String code = oar.getCode();
            String accessToken = idqClient.getToken(code);

            String resourceJson = idqClient.getUserInfo(accessToken);
            model.addAttribute("resourceJson", resourceJson);

        } catch (OAuthProblemException e) {
            logger.error("failed to create OAuth authorization response wrapper", e);
            StringBuffer sb = new StringBuffer();
            sb.append("<br />");
            sb.append("Error code: ").append(e.getError()).append("<br />");
            sb.append("Error description: ").append(e.getDescription()).append("<br />");
            sb.append("Error uri: ").append(e.getUri()).append("<br />");
            sb.append("State: ").append(e.getState()).append("<br />");
            return new ModelAndView("index");
        } catch (IOException e) {
            logger.error("failed to parse oauth credentials", e);
            return new ModelAndView("index");
        } catch (OAuthSystemException e) {
            logger.error("failed to validate OAuth authorization request parameters", e);
            return new ModelAndView("index");
        }

        return new ModelAndView("resource");

    }
}
