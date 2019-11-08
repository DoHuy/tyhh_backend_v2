package com.stadio.cms.service.impl;

import com.hoc68.users.documents.Manager;
import com.hoc68.users.documents.User;
import com.stadio.cms.dtos.PushMessageDTO;
import com.stadio.cms.dtos.user.MakeUserTransactionDTO;
import com.stadio.cms.dtos.user.UserProfileDTO;
import com.stadio.cms.dtos.user.UserTransactionDTO;
import com.stadio.cms.model.PageInfo;
import com.stadio.cms.service.IChatService;
import com.stadio.cms.service.IManagerService;
import com.stadio.common.custom.RequestHandler;
import com.stadio.common.request.SSORequest;
import com.stadio.common.utils.ResponseCode;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.response.TableList;
import com.stadio.cms.service.IUserService;
import com.stadio.cms.validation.UserValidation;
import com.stadio.common.define.Constant;
import com.stadio.common.service.PasswordService;
import com.hoc68.users.defines.RoleType;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.*;
import com.hoc68.users.documents.User;

import com.stadio.model.dtos.cms.UserListDTO;
import com.stadio.model.dtos.cms.UserSearchFormDTO;
import com.stadio.model.enu.TransactionApproveStatus;
import com.stadio.model.enu.TransactionType;
import com.stadio.model.repository.main.*;
import com.stadio.model.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Andy on 01/19/2018.
 */
@Service
public class UserService extends BaseService implements IUserService
{

    @Autowired
    ExamRepository examRepository;

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired UserValidation validation;

    @Autowired
    UserExamRepository userExamRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ClazzRepository clazzRepository;

    @Autowired ManagerService managerService;

    @Autowired
    RequestHandler requestHandler;

    @Autowired
    TransactionApproveRepository transactionApproveRepository;

    @Autowired
    IChatService chatService;

    @Value("${app.auth-server.host}")
    private String authServer;

    @Override
    public ResponseResult processSearchUser(UserSearchFormDTO userSearchFormDTO, Integer page, Integer pageSize, String uri)
    {
        Map userSearch = new HashMap<String, Object>();
        userSearch.put("code", userSearchFormDTO.getCode());
        userSearch.put("fullName", userSearchFormDTO.getFullName());
        userSearch.put("clazzId", userSearchFormDTO.getClazzId());
        long userQuantity = 0;
        if (page == 1)
        {
            userQuantity = userRepository.searchUserQuantity(userSearch);
        }
        List<User> users = userRepository.findUserByPage(page, pageSize, userSearch);
        List<UserListDTO> userListDTOS = new LinkedList<>();
        if (users != null)
        {
            users.forEach(user -> userListDTOS.add(new UserListDTO(user, clazzRepository)));
        }

        PageInfo pageInfo = new PageInfo(page, userQuantity, pageSize, uri);

        TableList<UserListDTO> tableList = new TableList<>(pageInfo, userListDTOS);
        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("user.success.getList"), tableList);

    }



    @Override
    public ResponseResult getProfileByUserId(String id) {

        User user = userRepository.findFirstById(id);
        if (user == null) {
            return ResponseResult.newInstance(ResponseCode.FAIL,getMessage("user.profile.notfound"), null);
        }

        UserProfileDTO userProfileDTO = new UserProfileDTO(user, clazzRepository);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("success"), userProfileDTO);
    }

    @Override
    public ResponseResult updateProfile(UserProfileDTO userProfileDTO) {

        User user = userRepository.findFirstById(userProfileDTO.getId());
        if (user == null) {
            return ResponseResult.newInstance(ResponseCode.FAIL,getMessage("user.profile.notfound"), null);
        }

        user.setUpdatedDate(new Date());

        if (StringUtils.isNotNull(userProfileDTO.getEmail()))
        {
            if (StringUtils.isValidEmailAddress(userProfileDTO.getEmail()))
            {
                user.setEmail(userProfileDTO.getEmail());
            }
            else
            {
                return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("manager.invalid.email"), null);
            }
        }

        if (StringUtils.isNotNull(userProfileDTO.getFullName()))
        {
            user.setFullName(userProfileDTO.getFullName());
        }

        if (StringUtils.isNotNull(userProfileDTO.getPhone()))
        {
            if (StringUtils.isPhoneNumber(userProfileDTO.getPhone()))
            {
                if (user.getPhoneComfirmed() && !userProfileDTO.getPhone().equals(user.getPhone())) {
                    return ResponseResult.newInstance(ResponseCode.FAIL, getMessage("user.comfirmed.phone"), null);
                } else {
                    user.setPhone(userProfileDTO.getPhone());
                }
            }
            else
            {
                return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("user.invalid.phone"), null);
            }
        }

        if (userProfileDTO.getBirthDay() != 0)
        {
            user.setBirthDay(new Date(userProfileDTO.getBirthDay()*1000));
        }

        if (StringUtils.isNotNull(userProfileDTO.getClazz()))
        {
            Clazz clazz = clazzRepository.findOneById(userProfileDTO.getClazz());

            if (clazz != null) {
                user.setClazzId(userProfileDTO.getClazz());
            }else{
                return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("user.invalid.clazzId"), null);
            }
        }

        userRepository.save(user);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage(""), null);
    }

    @Override
    public ResponseResult lockUser(String userId) {
        User user = userRepository.findFirstById(userId);
        if (user == null) {
            return ResponseResult.newInstance(ResponseCode.FAIL,getMessage("user.profile.notfound"), null);
        }

        if (!user.isEnabled()) {
            return ResponseResult.newInstance(ResponseCode.FAIL,getMessage("user.profile.disabled"), null);
        }

        user.setEnabled(false);

        SSORequest ssoRequest = new SSORequest();
        try {
            Manager manager = this.managerService.getManagerRequesting();
            if (RoleType.isCanRevokeUserToken(RoleType.fromInt(manager.getUserRole()))) {
                String result = ssoRequest.revokeToken(this.authServer ,this.requestHandler.getToken(), userId).getResponseBody();
                userRepository.save(user);
                UserProfileDTO userProfileDTO = new UserProfileDTO(user, clazzRepository);
                return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("success"), userProfileDTO);

            } else  {
                return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("error.permissionDeny"));
            }
        } catch (Exception e) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, e.getMessage());
        }


    }

    @Override
    public ResponseResult unlockUser(String userId) {
        User user = userRepository.findFirstById(userId);
        if (user == null) {
            return ResponseResult.newInstance(ResponseCode.FAIL,getMessage("user.profile.notfound"), null);
        }

        if (user.isEnabled()) {
            return ResponseResult.newInstance(ResponseCode.FAIL,getMessage("user.profile.enabled"), null);
        }

        user.setEnabled(true);
        userRepository.save(user);

        UserProfileDTO userProfileDTO = new UserProfileDTO(user, clazzRepository);
        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("success"), userProfileDTO);
    }

    @Override
    public ResponseResult resetPassword(String userId) {
        //TODO: need Authozie to do this
        User user = userRepository.findFirstById(userId);
        if (user == null) {
            return ResponseResult.newInstance(ResponseCode.FAIL,getMessage("user.profile.notfound"), null);
        }

        String rand = StringUtils.identifier256();
        user.setPasswordRand(rand);
        user.setPasswordHash(PasswordService.hidePassword(Constant.DEFAULT_PASS_USER, rand));
        userRepository.save(user);
        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("success"), null);
    }

    @Override
    public ResponseResult getPaymentHistories(String userId, Integer page, Integer pageSize) {
        List<Transaction> transactionList = transactionRepository.findTransactionsByPage(userId,page,pageSize);
        List<UserTransactionDTO> userTransactionDTOList = new ArrayList<>();

        for (Transaction transaction: transactionList) {
            UserTransactionDTO userTransactionDTO = new UserTransactionDTO(transaction);
            userTransactionDTOList.add(userTransactionDTO);
        }

        PageInfo pageInfo = new PageInfo(page, transactionRepository.countByUserIdRef(userId), pageSize, null);

        TableList tableList = new TableList(pageInfo, userTransactionDTOList);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("success"), tableList);
    }

    @Override
    public ResponseResult makePayment(MakeUserTransactionDTO makeUserTransactionDTO) {

        if (!StringUtils.isNotNull(makeUserTransactionDTO.getUserIdRef()) & !StringUtils.isNotNull(makeUserTransactionDTO.getUserCode())) {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM,getMessage("Thiếu mã người dùng"), null);
        }

        if (makeUserTransactionDTO.getAmount() == 0) {
            return ResponseResult.newInstance(ResponseCode.FAIL,getMessage("user.payment.invalid.amount"), null);
        }

        User user;
        if (StringUtils.isNotNull(makeUserTransactionDTO.getUserCode())) {
            user = userRepository.findByCode(makeUserTransactionDTO.getUserCode());
        } else {
            user = userRepository.findFirstById(makeUserTransactionDTO.getUserIdRef());
        }
        if (user == null) {
            return ResponseResult.newInstance(ResponseCode.FAIL,getMessage("user.profile.notfound"), null);
        }

        TransactionApprove transaction = new TransactionApprove();

        transaction.setAmount(makeUserTransactionDTO.getAmount());
        transaction.setTransContent(makeUserTransactionDTO.getTransContent());
        transaction.setTransTypeInt(TransactionType.CONFIG.toInt());
        transaction.setUserCodeRef(user.getCode());
        transaction.setCreatedBy(this.managerService.getManagerRequesting().getId());
        transaction.setStatus(TransactionApproveStatus.WAITING.toInt());

        transactionApproveRepository.save(transaction);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, "Đã cho vào danh sách kiểm duyệt", null);
    }
}
