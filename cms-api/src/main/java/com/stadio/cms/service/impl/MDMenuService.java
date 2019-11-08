package com.stadio.cms.service.impl;


import com.stadio.cms.controllers.system.DevConfigController;
import com.stadio.cms.model.ApiDocument;
import com.stadio.cms.service.IMDMenuService;
import com.hoc68.users.defines.RoleType;
import com.stadio.model.documents.MDMenu;
import com.stadio.model.repository.main.MDMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class MDMenuService extends BaseService implements IMDMenuService {

    @Autowired
    MDMenuRepository mdMenuRepository;

    @Autowired
    DocumentService documentService;

    @Override
    public List<MDMenu> loadRouter() {
        if (mdMenuRepository.count() == 0) {
            Map<String, List<ApiDocument>> results;
            results = documentService.processGetListApi("com.stadio.cms.controllers", x -> true);
            List<MDMenu> menuList = new ArrayList<>();
            for (Map.Entry<String, List<ApiDocument>> entry : results.entrySet()) {
                List<ApiDocument> apiDocuments = entry.getValue();

                for (ApiDocument api : apiDocuments) {
                    MDMenu menu = new MDMenu();
                    menu.setName(api.getName());
                    menu.setClassName(entry.getKey());
                    menu.setRouter("/" + api.getPath());
                    menu.setMethod(api.getMethod());

                    if (DevConfigController.class.toString().equals(entry.getKey())) {
                        //Only root can access
                        menu.setRoles(Arrays.asList(RoleType.ROOT.toInt()));
                    } else {
                        menu.setRoles(RoleType.allIntegerValueExceptUser());
                    }

                    try {
                        mdMenuRepository.save(menu);
                        menuList.add(menu);
                    } catch (Exception e) {
                        throw e;
                    }
                }
            }
        }
        return mdMenuRepository.findAll();
    }



}
