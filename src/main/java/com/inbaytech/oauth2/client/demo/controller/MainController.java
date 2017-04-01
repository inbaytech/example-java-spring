package com.inbaytech.oauth2.client.demo.controller;

import com.inbaytech.oauth2.client.demo.idq.IdqClient;
import com.inbaytech.oauth2.client.demo.Utils;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Date;

/**
 */
@Controller
public class MainController {

    private Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping("/index")
    public ModelAndView index()
            throws OAuthSystemException, IOException {
        return new ModelAndView("index");
    }

    @RequestMapping("/main/idq")
    public ModelAndView authorize() {
        try {
            Utils utils = new Utils();
            IdqClient idqClient = utils.initialIdqClient();
            return new ModelAndView(new RedirectView(idqClient.getAuthzUrl(String.valueOf(new Date().getTime()), "email")));
        } catch (OAuthSystemException e) {
            logger.error("failed to validate OAuth authorization request parameters", e);
            return new ModelAndView("index");
        } catch (IOException e) {
            logger.error("failed to parse oauth credentials", e);
            return new ModelAndView("index");
        }
    }
}