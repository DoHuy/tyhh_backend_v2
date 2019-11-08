package com.stadio.cms.service.impl;

import com.hoc68.users.documents.Manager;
import com.stadio.cms.dtos.authorization.FeatureChild;
import com.stadio.cms.dtos.authorization.FeatureParent;
import com.stadio.cms.dtos.authorization.RoleDetailsDTO;
import com.stadio.cms.dtos.authorization.RoleFormDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IRoleService;
import com.stadio.model.documents.*;
import com.stadio.model.enu.ConfigKey;
import com.stadio.model.repository.main.ConfigRepository;
import com.stadio.model.repository.main.FeatureRepository;
import com.stadio.model.repository.user.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleService extends BaseService implements IRoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    ManagerRoleRepository managerRoleRepository;

    @Autowired
    FeatureRepository featureRepository;

    @Autowired
    RoleFeatureRepository roleFeatureRepository;

    @Autowired
    ConfigRepository configRepository;


    @Override
    public ResponseResult processGetRoleList() {
        List<Role> roleList = roleRepository.findAll();
        if (roleList.isEmpty()) {
            //if role list is empty, system need default role for Root Account
            Role role = new Role("ROOT", "Phân quyền Hệ thống");
            roleRepository.save(role);

            Manager manager = managerRepository.findOneByUsername("root");
            if (manager != null) {
                ManagerRole managerRole = new ManagerRole(manager.getId(), role.getId());
                managerRoleRepository.save(managerRole);
            }
        }
        roleList.removeIf(x -> "ROOT".equals(x.getName()));

        roleList.forEach(x -> x.setRoleCount(roleFeatureRepository.countByRoleId(x.getId())));

        return ResponseResult.newSuccessInstance(roleList);
    }

    @Override
    public ResponseResult processCreateOrUpdateSingleRole(RoleFormDTO roleFormDTO) {

        if (StringUtils.isBlank(roleFormDTO.getName())) {
            return ResponseResult.newErrorInstance("01", getMessage("role.name.empty"));
        }

        Role exist = roleRepository.findRoleByName(roleFormDTO.getName());
        if (StringUtils.isBlank(roleFormDTO.getId()) && exist != null) {
            return ResponseResult.newErrorInstance("01", getMessage("role.name.exist"));
        }

        Role role;
        if (StringUtils.isNotBlank(roleFormDTO.getId())) {
            role = roleRepository.findOne(roleFormDTO.getId());
            if (role == null) {
                return ResponseResult.newErrorInstance("01", getMessage("role.not.found"));
            }
            role.setDescription(roleFormDTO.getDescription());
            role.setName(roleFormDTO.getName());

            roleRepository.save(role);

        } else {
            role = new Role();
            role.setDescription(roleFormDTO.getDescription());
            role.setName(roleFormDTO.getName());

            roleRepository.save(role);
        }

        roleFeatureRepository.removeByRoleId(role.getId());

        for (String hash: roleFormDTO.getPathList()) {
            MDFeature feature = featureRepository.findByHash(hash);
            if (feature != null) {
                RoleFeature roleFeature = new RoleFeature(role.getId(), feature.getId());
                roleFeatureRepository.save(roleFeature);
            }
        }

        return ResponseResult.newSuccessInstance(role);
    }

    @Override
    public ResponseResult processDeleteSingleRole(String id) {

        List<ManagerRole> managerRoleList = managerRoleRepository.findByRoleId(id);
        if (managerRoleList.size() > 0) {
            return ResponseResult.newErrorInstance("01", getMessage("role.not.delete"));
        } else {
            roleFeatureRepository.removeByRoleId(id);
            roleRepository.delete(id);
            return ResponseResult.newSuccessInstance(null);
        }


    }

    @Override
    public ResponseResult processDetailsRole(String id) {

        RoleDetailsDTO roleDetailsDTO = new RoleDetailsDTO();

        Role role = roleRepository.findOne(id);
        if (role != null) {
            roleDetailsDTO.setId(role.getId());
            roleDetailsDTO.setName(role.getName());
            roleDetailsDTO.setDescription(role.getDescription());
            List<FeatureParent> featureParentList = new ArrayList<>();

            for (MDFeature parent: featureRepository.findByControllerNotNull()) {
                FeatureParent featureParent = new FeatureParent();
                featureParent.setController(parent.getController());
                featureParent.setHash(parent.getHash());
                featureParent.setPath(parent.getPath());
                featureParent.setName(parent.getName());

                RoleFeature roleFeatureParent = roleFeatureRepository.findByRoleIdAndFeatureId(role.getId(), parent.getId());
                if (roleFeatureParent != null) {
                    featureParent.setSelected(true);
                } else {
                    featureParent.setSelected(false);
                }

                List<MDFeature> children = featureRepository.findByFeatureId(parent.getId());
                List<FeatureChild> featureChildList = new ArrayList<>();
                for (MDFeature child: children) {
                    FeatureChild featureChild = new FeatureChild();
                    featureChild.setName(child.getName());
                    featureChild.setFullPath(child.getPath());
                    featureChild.setHash(child.getHash());
                    featureChild.setMethod(child.getMethod());

                    RoleFeature roleFeatureChild = roleFeatureRepository.findByRoleIdAndFeatureId(role.getId(), child.getId());
                    if (roleFeatureChild != null) {
                        featureChild.setSelected(true);
                    } else {
                        featureChild.setSelected(false);
                    }

                    featureChildList.add(featureChild);
                }

                featureParent.setChildren(featureChildList);

                featureParentList.add(featureParent);
            }

            roleDetailsDTO.setFeatureParentList(featureParentList);
        }

        return ResponseResult.newSuccessInstance(roleDetailsDTO);
    }

    @Override
    public ResponseResult processUpdateMenuList(String menuList) {

        Config config = configRepository.findConfigByKey(ConfigKey.CMS_NAV_BAR.name());
        if (config == null) {
            config = new Config();
            config.setKey(ConfigKey.CMS_NAV_BAR.name());
        } else {
            config.setUpdatedDate(new Date());
        }

        config.setValue(menuList);


        configRepository.save(config);

        return ResponseResult.newSuccessInstance(null);
    }
}
