package com.stadio.mobi.config;

import com.stadio.common.define.Constant;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.enu.ConfigKey;
import com.stadio.model.repository.main.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCorsFilter extends OncePerRequestFilter
{

    @Value("${is.production:#{0}}")
    protected int isProduction;

    @Autowired
    ConfigRepository configRepository;

    private String headerKeyMobile;

    public SimpleCorsFilter() {
        this.getConfig();
    }

    private void getConfig() {
        if (this.headerKeyMobile == null) {
            try {
                Config config = configRepository.findConfigByKey(ConfigKey.MOBILE_KEY_HEADER.name());

                if (config != null) {
                    this.headerKeyMobile = config.getValue();
                } else {
                    config = new Config();
                    config.setKey(ConfigKey.MOBILE_KEY_HEADER.name());
                    config.setValue(Constant.DEFAULT_MOBILE_HEADER_PARAM);
                    configRepository.save(config);
                    this.headerKeyMobile = Constant.DEFAULT_MOBILE_HEADER_PARAM;
                }
            } catch (Exception e) {
                this.headerKeyMobile = Constant.DEFAULT_MOBILE_HEADER_PARAM;
            }
        }
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token");
        response.addHeader("Access-Control-Expose-Headers", "xsrf-token");
        String keyHeader = request.getHeader("key");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else if(StringUtils.isNotNull(this.headerKeyMobile) && StringUtils.isNotNull(keyHeader) && this.headerKeyMobile.contains(keyHeader)) {
            if (request.getRequestURI().contains("dev/") & this.isProduction == 1) {
                response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,"");
            } else {
                filterChain.doFilter(request, response);
            }
        } else {
            this.getConfig();
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,"Thiếu header key");
        }
    }
}