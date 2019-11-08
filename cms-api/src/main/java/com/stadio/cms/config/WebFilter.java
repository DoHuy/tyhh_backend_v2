package com.stadio.cms.config;

import com.stadio.cms.i18n.IMessageService;
import com.stadio.cms.service.IMDMenuService;
import com.stadio.cms.service.impl.ManagerService;
import com.stadio.common.custom.RequestHandler;
import com.stadio.model.documents.MDMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import java.io.IOException;
import java.util.List;

//@Configuration
public class WebFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(WebFilter.class);

    private static final boolean CONDITION = false;

    public List<MDMenu> mdMenuRouters;

    @Autowired
    RequestHandler requestHandler;

    @Autowired
    ManagerService managerService;

    @Autowired
    IMessageService messageService;

    @Autowired
    IMDMenuService mdMenuService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.debug("Initiating WebFilter >> ");
        this.mdMenuRouters = this.mdMenuService.loadRouter();

    }

    @Override
    public void doFilter(ServletRequest requestX, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {

        chain.doFilter(requestX,response);
        return;


        //TODO: Uncoment when production
//        HttpServletRequest request = (HttpServletRequest) requestX;
//        String contextPath = request.getRequestURI().substring(request.getContextPath().length());
//        Manager manager = managerService.getManagerRequesting(requestHandler.getToken());
//
//        request.getMethod();
//        //TODO : hash router for more speed
//        for (MDMenu router: this.mdMenuRouters) {
//            if (contextPath.equals(router.getRouter()) && request.getMethod().equals(router.getMethod())
//                    && router.isUserCanAccess(manager)) {
//                chain.doFilter(request,response);
//                return;
//            }
//        }
//        //Handle Exception
//        ((HttpServletResponse) response).sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, messageService.getMessage("error.permissionDeny"));
    }

    @Override
    public void destroy() {
        logger.debug("Destroying WebFilter >> ");
    }
}
