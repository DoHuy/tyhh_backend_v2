package com.stadio.cms.service.impl;

import com.stadio.cms.controllers.users.ManagerController;
import com.stadio.cms.model.PageInfo;
import com.stadio.common.custom.RequestHandler;
import com.stadio.common.utils.ResponseCode;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.response.TableList;
import com.stadio.cms.service.IManagerService;
import com.stadio.common.define.Action;
import com.stadio.common.service.PasswordService;
import com.hoc68.users.defines.RoleType;
import com.stadio.common.utils.StringUtils;
import com.hoc68.users.documents.Manager;
import com.stadio.model.documents.ManagerRole;
import com.stadio.model.documents.Role;
import com.stadio.model.dtos.cms.ManagerDTO;
import com.stadio.model.dtos.cms.ManagerDetailDTO;
import com.stadio.model.dtos.cms.ManagerItemDTO;
import com.stadio.model.repository.user.ManagerRepository;

import com.stadio.model.repository.user.ManagerRoleRepository;
import com.stadio.model.repository.user.RoleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Andy on 11/08/2017.
 */
@Service
public class ManagerService extends BaseService implements IManagerService {

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    RequestHandler requestHandler;

    @Autowired
    ManagerRoleRepository managerRoleRepository;

    @Autowired
    RoleRepository roleRepository;

    private Logger logger = LogManager.getLogger(ManagerService.class);

    @Override
    public ResponseResult processCreateNewManager(String token, ManagerDTO managerDTO) {

        Manager manager = this.getManagerRequesting();

        if (!RoleType.isCanCreateNewUserRole(RoleType.fromInt(manager.getUserRole()), managerDTO.getRole())) {
            return ResponseResult.newInstance(ResponseCode.FAIL, getMessage("error.permissionDeny"), null);
        }

        ResponseResult responseResult = validManagerDTO(managerDTO);

        if (responseResult != null) {
            return responseResult;
        }

        Manager newManager = new Manager();

        //newManager.setUserRole(Integer.valueOf(managerDTO.getRole()));
        newManager.setFullName(managerDTO.getFullName());
        newManager.setEmail(managerDTO.getEmail());
        newManager.setPhone(managerDTO.getPhone());
        newManager.setUsername(managerDTO.getUsername().toLowerCase());
        newManager.setBlock(false);

        String noise = new Date().toString() + UUID.randomUUID().toString();
        String hidePass = PasswordService.hidePassword(managerDTO.getPassword(), noise);

        newManager.setPasswordHash(hidePass);
        newManager.setPasswordRand(noise);

        Role roleContent = roleRepository.findRoleByName(RoleType.CONTENT_MANAGER.name());
        Role role = roleRepository.findOne(managerDTO.getRole());
        if (roleContent != null && role != null && roleContent.getId().equals(role.getId())) {
            manager.setUserRole(RoleType.CONTENT_MANAGER.toInt());
        }

        managerRepository.save(newManager);

        ManagerRole managerRole = managerRoleRepository.findByManagerIdAndRoleId(newManager.getId(), managerDTO.getRole());
        if (managerRole == null) {
            managerRole = new ManagerRole(newManager.getId(), managerDTO.getRole());
            managerRoleRepository.save(managerRole);
        }

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("manager.success.register"), null);

    }

    @Override
    public Manager getManagerRequesting() {
        return managerRepository.findOne(this.requestHandler.getPrincipal());
    }

    private ResponseResult validManagerDTO(ManagerDTO managerDTO) {
        if (!StringUtils.isNotNull(managerDTO.getEmail()) || !StringUtils.isValidEmailAddress(managerDTO.getEmail())) {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("manager.invalid.email"), null);
        }

        if (!StringUtils.isNotNull(managerDTO.getPassword()) || !StringUtils.isValidPassword(managerDTO.getPassword())) {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("manager.invalid.passWord"), null);
        }

        if (!StringUtils.isNotNull(managerDTO.getPhone()) || !StringUtils.isPhoneNumber(managerDTO.getPhone())) {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("manager.invalid.phone"), null);
        }

        if (!StringUtils.isNotNull(managerDTO.getUsername())) {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("manager.invalid.userName"), null);
        }

        List<Manager> managers = managerRepository.findManagerByEmail(managerDTO.getEmail());
        if (managers != null && !managers.isEmpty()) {
            return ResponseResult.newInstance(ResponseCode.EXIST_VALUE, getMessage("manager.exist.email"), null);
        }

        managers = managerRepository.findManagerByPhone(managerDTO.getPhone());
        if (managers != null && !managers.isEmpty()) {
            return ResponseResult.newInstance(ResponseCode.EXIST_VALUE, getMessage("manager.exist.phone"), null);
        }

        managers = managerRepository.findManagerByUsername(managerDTO.getUsername());
        if (managers != null && !managers.isEmpty()) {
            return ResponseResult.newInstance(ResponseCode.EXIST_VALUE, getMessage("manager.exist.useName"), null);
        }
        return null;
    }

    @Override
    public ResponseResult<?> processUpdateManager(ManagerDTO managerDTO) {

        Manager manager = findOneByUsername(managerDTO.getUsername());

        if (manager == null) {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("manager.notFound.id"), null);
        }

        manager.setUpdatedDate(new Date());

        if (StringUtils.isNotNull(managerDTO.getEmail())) {
            if (StringUtils.isValidEmailAddress(managerDTO.getEmail())) {
                manager.setEmail(managerDTO.getEmail());
            } else {
                return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("manager.invalid.email"), null);
            }
        }

        if (StringUtils.isNotNull(managerDTO.getFullName())) {
            manager.setFullName(managerDTO.getFullName());
        }

        if (StringUtils.isNotNull(managerDTO.getPhone())) {
            if (StringUtils.isPhoneNumber(managerDTO.getPhone())) {
                manager.setPhone(managerDTO.getPhone());
            } else {
                return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("manager.invalid.phone"), null);
            }
        }

        if (StringUtils.isNotNull(managerDTO.getAddress())) {
            manager.setAddress(managerDTO.getAddress());
        }

//        if (StringUtils.isNotNull(managerDTO.getRole())) {
//            manager.setUserRole(Integer.valueOf(managerDTO.getRole()));
//        }

        managerRepository.save(manager);

        //clear all role of this manager after update
        managerRoleRepository.removeByManagerId(manager.getId());

        ManagerRole managerRole = managerRoleRepository.findByManagerIdAndRoleId(manager.getId(), managerDTO.getRole());
        if (managerRole == null) {
            managerRole = new ManagerRole(manager.getId(), managerDTO.getRole());
            managerRoleRepository.save(managerRole);
            logger.info("Update managerRole: " + managerRole.getRoleId());
        }

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("manager.success.update"), new ManagerDetailDTO(manager));
    }

    @Override
    public Manager findOneByUsername(String currentName) {
        return managerRepository.findOneByUsername(currentName);
    }

    @Override
    public ResponseResult processChangePassword(String token, String oldPass, String newPass) {

        Manager manager = getManagerRequesting();

        if (PasswordService.validPassword(oldPass, manager.getPasswordRand(), manager.getPasswordHash())) {
            if (!StringUtils.isNotNull(newPass) || !StringUtils.isValidPassword(newPass)) {
                return ResponseResult.newInstance(ResponseCode.FAIL, getMessage("manager.invalid.passWord"), null);
            } else {
                manager.setUpdatedDate(new Date());

                String noise = StringUtils.identifier256();
                String hidePass = PasswordService.hidePassword(newPass, noise);

                manager.setPasswordHash(hidePass);
                manager.setPasswordRand(noise);

                managerRepository.save(manager);

                return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("manager.success.changePassword"), null);
            }
        } else {
            return ResponseResult.newInstance(ResponseCode.FAIL, getMessage("manager.notMath.passWord"), null);
        }
    }

    @Override
    public ResponseResult processDeleteManager(String token, String id) {
        if (!StringUtils.isNotNull(id)) {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("manager.invalid.id"), null);
        }

        Manager manager = getManagerRequesting();

        if (manager.getUserRole() == RoleType.ROOT.toInt()) {
            Manager exist = managerRepository.findOne(id);
            if (exist != null) {
                managerRepository.delete(exist);

                //remove dependencies
                managerRoleRepository.removeByManagerId(id);
            }

            return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("manager.success.delete"), null);
        } else {
            return ResponseResult.newInstance(ResponseCode.FAIL, "Không có quyền thao tác", null);

        }
    }

    @Override
    public ResponseResult<?> processGetListManager(Integer page, Integer pageSize, String uri) {
        if (page == null || page < 1) {
            page = 1;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = 1;
        }

        List<ManagerItemDTO> managerListDTO = new ArrayList<>();
        for (Manager manager : managerRepository.findManagerByPage(page, pageSize)) {
            ManagerItemDTO managerItemDTO = new ManagerItemDTO(manager);

            List<ManagerRole> managerRoleList = managerRoleRepository.findByManagerId(manager.getId());
            if (!managerRoleList.isEmpty()) {
                ManagerRole managerRole = managerRoleList.get(0);
                Role role = roleRepository.findOne(managerRole.getRoleId());
                if (role != null) {
                    managerItemDTO.setRole(role.getName());
                }
            }

            String actionDetails = MvcUriComponentsBuilder.fromMethodName(ManagerController.class, "details", manager.getId()).toUriString();
            managerItemDTO.getAction().put(Action.ACTION_DETAILS, actionDetails);
            managerListDTO.add(managerItemDTO);

        }

        //init page information
        PageInfo pageInfo = new PageInfo(page, managerRepository.count(), pageSize, uri);

        TableList<ManagerItemDTO> tableList = new TableList<>(pageInfo, managerListDTO);
        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("manager.success.getList"), tableList);
    }

    @Override
    public ResponseResult<?> processGetProfileManager(String id) {
        if (!StringUtils.isNotNull(id)) {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("manager.invalid.id"), null);
        }

        Manager manager = managerRepository.findOne(id);

        if (manager == null) {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("manager.notFound.id"), null);
        } else {
            ManagerDetailDTO managerDetailDTO = new ManagerDetailDTO(manager);

            List<ManagerRole> managerRoleList = managerRoleRepository.findByManagerId(manager.getId());

            if (!managerRoleList.isEmpty()) {
                managerDetailDTO.setRole(managerRoleList.get(0).getRoleId());
            }

            return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("manager.success.getProfile"), managerDetailDTO);
        }
    }

    @Override
    public ResponseResult<?> processSearchManager(String q) {
        List<Manager> managers = managerRepository.queryWithKeyword(q);

        List<ManagerDetailDTO> managerDetailDTOList = new ArrayList<>();
        if (managers != null && !managers.isEmpty()) {
            for (Manager manager : managers) {
                ManagerDetailDTO managerDetailDTO = new ManagerDetailDTO(manager);
                managerDetailDTOList.add(managerDetailDTO);
            }
        }

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("manager.success.search"), managerDetailDTOList);
    }


}
