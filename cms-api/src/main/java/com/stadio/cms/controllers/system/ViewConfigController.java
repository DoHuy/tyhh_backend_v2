package com.stadio.cms.controllers.system;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.dtos.authorization.NavChild;
import com.stadio.cms.dtos.authorization.NavParent;
import com.stadio.cms.service.IDocumentService;
import com.stadio.cms.service.impl.ManagerService;
import com.stadio.common.custom.RequestHandler;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IConfigService;
import com.hoc68.users.defines.RoleType;
import com.stadio.model.documents.Config;
import com.hoc68.users.documents.Manager;
import com.stadio.model.documents.ManagerRole;
import com.stadio.model.documents.Role;
import com.stadio.model.enu.ConfigKey;
import com.stadio.model.repository.main.ConfigRepository;
import com.stadio.model.repository.user.ManagerRoleRepository;
import com.stadio.model.repository.user.RoleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by Andy on 11/10/2017.
 */
@RestController
@RequestMapping(value = "api/config", name = "Quản lý cấu hình hệ thống")
public class ViewConfigController extends BaseController {

    private Logger logger = LogManager.getLogger(ViewConfigController.class);

    @Autowired
    IConfigService configService;

    @Autowired
    ConfigRepository configRepository;

    @Autowired
    RequestHandler requestHandler;

    @Autowired
    ManagerService managerService;

    @Autowired
    IDocumentService documentService;

    @Autowired
    ManagerRoleRepository managerRoleRepository;

    @Autowired
    RoleRepository roleRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/menuList")
    public ResponseEntity menu() {
        Config config = configRepository.findConfigByKey(ConfigKey.CMS_NAV_BAR.name());
        String menuList = config.getValue();
        try {

            Manager manager = this.managerService.getManagerRequesting();

            if (manager.getUserRole() != RoleType.ROOT.toInt()) { //is not ROOT

                List<ManagerRole> managerRoleList = managerRoleRepository.findByManagerId(manager.getId());
                if (managerRoleList.size() == 0) {
                    return ResponseEntity.ok(null);
                }
                ManagerRole managerRole = managerRoleList.get(0);

                List<NavParent> navParentList = mapper.readValue(menuList, new TypeReference<List<NavParent>>(){});
                List<NavParent> results = new ArrayList<>();

                navParentList.forEach(parent -> {
                    if (parent.isTitle()) {
                        results.add(parent);
                    } else {
                        String role = parent.getRoles().stream()
                                .filter(x -> x.equals(managerRole.getRoleId()))
                                .findFirst().orElse(null);
                        if (role != null) {
                            results.add(parent);
                        } else {
                            List<NavChild> navChildList = new ArrayList<>();
                            parent.getChildren().forEach(child -> {
                                String roleChild = child.getRoles().stream()
                                        .filter(x -> x.equals(managerRole.getRoleId()))
                                        .findFirst().orElse(null);
                                if (roleChild != null) {
                                    navChildList.add(child);
                                }
                            });

                            if (!navChildList.isEmpty()) {
                                if (parent.getChildren() == null) {
                                    parent.setChildren(new ArrayList<>());
                                }
                                parent.getChildren().addAll(navChildList);
                                results.add(parent);
                            }
                        }
                    }
                });

                menuList = mapper.writeValueAsString(results);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }

        return ResponseEntity.ok(menuList);

    }

    @GetMapping(value = "/features")
    public ResponseEntity getFeatureList() {
        ResponseResult result = documentService.processGetListFeature();
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/listKey")
    public ResponseEntity getListKey() {
        return ResponseEntity.ok(ConfigKey.values());
    }

    @GetMapping(value = "/list")
    public ResponseEntity getListConfig() {
        ResponseResult result = configService.processGetListConfig();
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/put")
    public ResponseEntity actionPutValue(
            @RequestParam(value = "key") String key,
            @RequestParam(value = "value") String value,
            @RequestParam(value = "name", required = false, defaultValue = "") String name) {
        ResponseResult result = configService.processPutValue(key, value, name);
        return ResponseEntity.ok(result);
    }


}
