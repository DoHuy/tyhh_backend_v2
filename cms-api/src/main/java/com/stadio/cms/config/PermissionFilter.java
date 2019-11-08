package com.stadio.cms.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoc68.users.defines.RoleType;
import com.hoc68.users.documents.Manager;
import com.stadio.cms.dtos.authorization.NavChild;
import com.stadio.cms.dtos.authorization.NavParent;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.impl.ManagerService;
import com.stadio.common.custom.RequestHandler;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.*;
import com.stadio.model.enu.ConfigKey;
import com.stadio.model.repository.main.ConfigRepository;
import com.stadio.model.repository.main.FeatureRepository;
import com.stadio.model.repository.user.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PermissionFilter extends GenericFilterBean {

    private Logger logger = LogManager.getLogger(PermissionFilter.class);

    @Autowired
    RequestHandler requestHandler;

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    ManagerRoleRepository managerRoleRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    FeatureRepository featureRepository;

    @Autowired
    RoleFeatureRepository roleFeatureRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    ManagerService managerService;

    @Autowired
    ConfigRepository configRepository;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        try {
            userId = ((OAuth2Authentication) authentication).getUserAuthentication().getPrincipal().toString();;
        } catch (Exception e) {

        }
        Manager manager = managerRepository.findOne(userId);
        if (manager != null) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;

            if ("ROOT".equalsIgnoreCase(manager.getUsername())) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                List<ManagerRole> managerRoleList = managerRoleRepository.findByManagerId(userId);

                String path = request.getRequestURI();
                String hash = StringUtils.identifier_MD5(path);
                MDFeature feature = featureRepository.findByHash(hash);

                if (feature == null) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {

                    boolean permissionDenied = true;
                    for (ManagerRole managerRole : managerRoleList) {
                        Role role = roleRepository.findOne(managerRole.getRoleId());
                        RoleFeature roleFeature = roleFeatureRepository.findByRoleIdAndFeatureId(role.getId(), feature.getId());
                        if (roleFeature != null) {
                            permissionDenied = false;
                            filterChain.doFilter(servletRequest, servletResponse);
                            break;
                        }
                    }

                    if (permissionDenied) {
                        response.setStatus(403);
                        response.setHeader("Content-Type", "application/json");
                        OutputStream out = response.getOutputStream();
                        Map<String, String> data = new HashMap<>();

                        String pathDefault = this.findNavFirstDefault();
                        data.put("pathDefault", pathDefault);
                        String body = mapper.writeValueAsString(ResponseResult.newInstance("403", "permission denied", data));
                        out.write(body.getBytes());
                        out.flush();
                        out.close();
                    }
                }
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private String findNavFirstDefault() {

        try {
            Config config = configRepository.findConfigByKey(ConfigKey.CMS_NAV_BAR.name());
            String menuList = config.getValue();

            Manager manager = this.managerService.getManagerRequesting();

            List<ManagerRole> managerRoleList = managerRoleRepository.findByManagerId(manager.getId());
            if (managerRoleList.size() == 0) {
                return null;
            }
            ManagerRole managerRole = managerRoleList.get(0);

            List<NavParent> navParentList = mapper.readValue(menuList, new TypeReference<List<NavParent>>(){});

            for (NavParent parent: navParentList) {
                if (parent.isTitle()) {

                } else {
                    String role = parent.getRoles().stream()
                            .filter(x -> x.equals(managerRole.getRoleId()))
                            .findFirst().orElse(null);
                    if (role != null) {
                        return parent.getUrl();
                    } else {
                        for (NavChild child: parent.getChildren()) {
                            String roleChild = child.getRoles().stream()
                                    .filter(x -> x.equals(managerRole.getRoleId()))
                                    .findFirst().orElse(null);
                            if (roleChild != null) {
                                return child.getUrl();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
}
