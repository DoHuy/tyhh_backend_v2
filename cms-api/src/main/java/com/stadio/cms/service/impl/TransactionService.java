package com.stadio.cms.service.impl;

import com.hoc68.users.documents.Manager;
import com.hoc68.users.documents.User;
import com.mongodb.DBObject;
import com.stadio.cms.dtos.PushMessageDTO;
import com.stadio.cms.model.PageInfo;
import com.stadio.cms.response.TableList;
import com.stadio.cms.service.IChatService;
import com.stadio.cms.service.IManagerService;
import com.stadio.common.utils.ResponseCode;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.ITransactionService;
import com.stadio.model.documents.OnePayRequest;
import com.stadio.model.documents.Sms;
import com.stadio.model.documents.Transaction;
import com.stadio.model.documents.TransactionApprove;
import com.stadio.model.dtos.cms.ManagerDTO;
import com.stadio.model.dtos.cms.TransactionFormDTO;
import com.stadio.model.dtos.cms.TransactionItemDTO;
import com.stadio.model.dtos.cms.TransactionStatisticDTO;
import com.stadio.model.dtos.cms.transaction.TransactionApproveDTO;
import com.stadio.model.dtos.cms.transaction.TransactionApproveFromSearchDTO;
import com.stadio.model.enu.TransactionApproveStatus;
import com.stadio.model.enu.TransactionType;
import com.stadio.model.repository.main.OnePayRequestRepository;
import com.stadio.model.repository.main.SmsRepository;
import com.stadio.model.repository.main.TransactionApproveRepository;
import com.stadio.model.repository.main.TransactionRepository;
import com.stadio.model.repository.user.ManagerRepository;
import com.stadio.model.repository.user.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.stadio.common.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Andy on 03/03/2018.
 */
@Service
public class TransactionService extends BaseService implements ITransactionService
{

    private static final SimpleDateFormat fm = new SimpleDateFormat("dd-MM-yyyy");

    private Logger logger = LogManager.getLogger(TransactionService.class);

    @Autowired TransactionRepository transactionRepository;

    @Autowired
    TransactionApproveRepository transactionApproveRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    IManagerService managerService;

    @Autowired
    IChatService chatService;

    @Autowired OnePayRequestRepository onePayRequestRepository;

    @Autowired SmsRepository smsRepository;

    @Override
    public ResponseResult processSearch(TransactionFormDTO transactionFormDTO, int page, int pageSize) {

        String userCode = transactionFormDTO.getUserCode();

        List<Transaction> transactionList = new ArrayList<>();
        List<TransactionItemDTO> transactionItemDTOList = new LinkedList<>();

        if(StringUtils.isValid(userCode)){
            User user = userRepository.findByCode(userCode);
            if(user!=null){
                transactionList = transactionRepository.search(transactionFormDTO,user.getId(),page,pageSize);
                transactionList.forEach(transaction -> {
                    TransactionItemDTO transactionItemDTO = new TransactionItemDTO(transaction);
                    transactionItemDTO.setUserCode(userCode);
                    transactionItemDTOList.add(transactionItemDTO);
                });
                PageInfo pageInfo = new PageInfo(page, transactionRepository.countSearch(transactionFormDTO,user.getId(),page,pageSize), pageSize, "");
                TableList tableList = new TableList(pageInfo,transactionItemDTOList);
                return ResponseResult.newSuccessInstance(tableList);
            }
        }else{
            transactionList = transactionRepository.search(transactionFormDTO, null, page,pageSize);
            transactionList.stream().forEach(transaction -> {
                TransactionItemDTO transactionItemDTO = new TransactionItemDTO(transaction);
                User user = userRepository.findFirstById(transaction.getUserIdRef());
                if(user!=null){
                    transactionItemDTO.setUserCode(user.getCode());
                    transactionItemDTOList.add(transactionItemDTO);
                }
            });
            PageInfo pageInfo = new PageInfo(page, transactionRepository.countSearch(transactionFormDTO,null,page,pageSize), pageSize, "");
            TableList tableList = new TableList(pageInfo,transactionItemDTOList);
            return ResponseResult.newSuccessInstance(tableList);
        }
        return ResponseResult.newErrorInstance("400","Not Found");
    }

    @Override
    public ResponseResult processGetListTransaction(String from, String to)
    {
        ResponseResult result = new ResponseResult();
        try
        {
            if (from.isEmpty() || to.isEmpty())
            {
                List<Transaction> transactionList = transactionRepository.findAll();
                result.setErrorCode(ResponseCode.SUCCESS);
                result.setData(transactionList);
            }
            else
            {
                Date startDate = fm.parse(from);
                Date endDate = fm.parse(to);

                List<Transaction> transactionList = transactionRepository.findByCreatedDateBetween(startDate, endDate);
                result.setErrorCode(ResponseCode.SUCCESS);
                result.setData(transactionList);
            }

        }
        catch (Exception e)
        {
            logger.error("processGetListTransaction exception: ", e);
            result.setErrorCode("01");
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseResult processGetListSms(String from, String to)
    {
        ResponseResult result = new ResponseResult();
        try
        {
            if (from.isEmpty() && to.isEmpty())
            {
                List<Sms> smsList = smsRepository.findAll();
                result.setErrorCode(ResponseCode.SUCCESS);
                result.setData(smsList);
            }
            else
            {
                Date startDate = fm.parse(from);
                Date endDate = fm.parse(to);

                List<Sms> smsList = smsRepository.findByCreatedDateBetween(startDate, endDate);
                result.setErrorCode(ResponseCode.SUCCESS);
                result.setData(smsList);
            }

        }
        catch (Exception e)
        {
            logger.error("processGetListSms exception: ", e);
            result.setErrorCode("01");
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseResult processGetListCards(String from, String to)
    {
        ResponseResult result = new ResponseResult();
        try
        {
            if (from.isEmpty() && to.isEmpty())
            {
                List<OnePayRequest> requestList = onePayRequestRepository.findAll();
                result.setErrorCode(ResponseCode.SUCCESS);
                result.setData(requestList);
            }
            else
            {
                Date startDate = fm.parse(from);
                Date endDate = fm.parse(to);

                List<OnePayRequest> requestList = onePayRequestRepository.findByCreatedDateBetween(startDate, endDate);
                result.setErrorCode(ResponseCode.SUCCESS);

                result.setData(requestList);
            }

        }
        catch (Exception e)
        {
            logger.error("processGetListCards exception: ", e);
            result.setErrorCode("01");
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseResult groupTransactionByDay(String time) {
        ResponseResult result = new ResponseResult();
        try
        {
            Date monthDate = fm.parse(time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(monthDate);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH)+1;

            Iterable<DBObject> firstBatchRecharge = transactionRepository.groupTransactionRechargeByDay(month,year);
            Iterable<DBObject> firstBatchBuy = transactionRepository.groupTransactionBuyByDay(month,year);

            Map<Integer,Long> statisticRechargeMap = convertIteratorToDTO(firstBatchRecharge);
            Map<Integer,Long> statisticBuyMap = convertIteratorToDTO(firstBatchBuy);

            List<TransactionStatisticDTO>  amountGroupByTimeDTOList = new LinkedList<>();

            int numbersDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            for(int pos =0;pos<numbersDayOfMonth;pos++){
                TransactionStatisticDTO transactionStatisticDTO = new TransactionStatisticDTO();
                transactionStatisticDTO.setPosition(pos+1);

                Long amountRecharge = statisticRechargeMap.get(pos+1);
                if(amountRecharge!=null)
                    transactionStatisticDTO.setAmountRecharge(amountRecharge);

                Long amountBuy = statisticBuyMap.get(pos+1);
                if(amountBuy!=null)
                    transactionStatisticDTO.setAmountBuy(amountBuy);

                amountGroupByTimeDTOList.add(transactionStatisticDTO);
            }

            result.setErrorCode(ResponseCode.SUCCESS);
            result.setData(amountGroupByTimeDTOList);
        }
        catch (Exception e)
        {
            logger.error("processGetListTransaction exception: ", e);
            result.setErrorCode("01");
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseResult groupTransactionByMonth(String time) {
        ResponseResult result = new ResponseResult();
        try
        {
            Date monthDate = fm.parse(time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(monthDate);
            int year = cal.get(Calendar.YEAR);

            Iterable<DBObject> firstBatchRecharge = transactionRepository.groupTransactionRechargeByMonth(year);
            Iterable<DBObject> firstBatchBuy = transactionRepository.groupTransactionBuyByMonth(year);

            Map<Integer,Long> statisticRechargeMap = convertIteratorToDTO(firstBatchRecharge);
            Map<Integer,Long> statisticBuyMap = convertIteratorToDTO(firstBatchBuy);

            List<TransactionStatisticDTO>  amountGroupByTimeDTOList = new LinkedList<>();

            int numbersMonthOfYear = 12;
            for(int pos =0;pos<numbersMonthOfYear;pos++){
                TransactionStatisticDTO transactionStatisticDTO = new TransactionStatisticDTO();
                transactionStatisticDTO.setPosition(pos+1);

                Long amountRecharge = statisticRechargeMap.get(pos+1);
                if(amountRecharge!=null)
                    transactionStatisticDTO.setAmountRecharge(amountRecharge);

                Long amountBuy = statisticBuyMap.get(pos+1);
                if(amountBuy!=null)
                    transactionStatisticDTO.setAmountBuy(amountBuy);

                amountGroupByTimeDTOList.add(transactionStatisticDTO);
            }

            result.setErrorCode(ResponseCode.SUCCESS);
            result.setData(amountGroupByTimeDTOList);
        }
        catch (Exception e)
        {
            logger.error("processGetListTransaction exception: ", e);
            result.setErrorCode("01");
            result.setMessage(e.getMessage());

        }

        return result;
    }

    @Override
    public ResponseResult groupTransactionByYear() {
        ResponseResult result = new ResponseResult();
        try
        {


            Iterable<DBObject> firstBatchRecharge = transactionRepository.groupTransactionRechargeByYear();
            Iterable<DBObject> firstBatchBuy = transactionRepository.groupTransactionBuyByYear();

            Map<Integer,Long> statisticRechargeMap = convertIteratorToDTO(firstBatchRecharge);
            Map<Integer,Long> statisticBuyMap = convertIteratorToDTO(firstBatchBuy);

            List<TransactionStatisticDTO>  amountGroupByTimeDTOList = new LinkedList<>();

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int yearCurrent = cal.get(Calendar.YEAR);
            for(int pos =yearCurrent-4;pos<yearCurrent+1;pos++){
                TransactionStatisticDTO transactionStatisticDTO = new TransactionStatisticDTO();
                transactionStatisticDTO.setPosition(pos);

                Long amountRecharge = statisticRechargeMap.get(pos);
                if(amountRecharge!=null)
                    transactionStatisticDTO.setAmountRecharge(amountRecharge);

                Long amountBuy = statisticBuyMap.get(pos);
                if(amountBuy!=null)
                    transactionStatisticDTO.setAmountBuy(amountBuy);

                amountGroupByTimeDTOList.add(transactionStatisticDTO);
            }

            result.setErrorCode(ResponseCode.SUCCESS);
            result.setData(amountGroupByTimeDTOList);
        }
        catch (Exception e)
        {
            logger.error("processGetListTransaction exception: ", e);
            result.setErrorCode("01");
            result.setMessage(e.getMessage());
        }

        return result;
    }


    @Override
    public ResponseResult processGetTransactionApproveList(TransactionApproveFromSearchDTO searchFormDTO) {
        List<TransactionApprove> transactionApproves = transactionApproveRepository.findWithFormSearch(searchFormDTO);
        List<TransactionApproveDTO> transactionApproveDTOS = new ArrayList<>();
        for (TransactionApprove action: transactionApproves) {

            TransactionApproveDTO tran = new TransactionApproveDTO(action);
            try {
                Manager manager = managerRepository.findOne(action.getCreatedBy());
                tran.setManagerIdCreated(manager.getId());
                tran.setManagerUsernameCreated(manager.getUsername());

                Manager manager2 = managerRepository.findOne(action.getUpdatedBy());
                tran.setManagerIdUpdated(manager2.getId());
                tran.setManagerUsernameUpdated(manager2.getUsername());
            } catch (Exception e) {}

            transactionApproveDTOS.add(tran);
        }

        TableList tableList = new TableList();
        tableList.setPageInfo(new PageInfo(
                searchFormDTO.getPage(),
                transactionApproveRepository.countWithFormSearch(searchFormDTO),
                transactionApproveDTOS.size(),
                null)
        );
        tableList.setItems(transactionApproveDTOS);
        return ResponseResult.newSuccessInstance(tableList);
    }

    @Override
    public ResponseResult processUpdateTransactionApproveList(String id, int status) {
        TransactionApprove transactionApprove = transactionApproveRepository.findOne(id);
        if (transactionApprove == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL,getMessage("tran.aprrove.not.found"));
        }
        if (status < 0 || status > TransactionApproveStatus.WAITING.toInt()) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL,getMessage("tran.aprrove.wrong.status"));
        }
        if (transactionApprove.getStatus() == TransactionApproveStatus.APPROVED.toInt() ||
                transactionApprove.getStatus() == TransactionApproveStatus.DENY.toInt()) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL,getMessage("tran.aprrove.can.not.change.status"));
        }
        transactionApprove.setStatus(status);
        transactionApprove.setUpdatedBy(this.managerService.getManagerRequesting().getId());
        transactionApproveRepository.save(transactionApprove);

        if (status == TransactionApproveStatus.APPROVED.toInt()) {
            //Create new transaction
            Transaction transaction = new Transaction();
            transaction.setAmount(transactionApprove.getAmount());
            transaction.setTransContent(transactionApprove.getTransContent());
            transaction.setTransType(TransactionType.CONFIG);

            User user = userRepository.findByCode(transactionApprove.getUserCodeRef());
            if (user == null || !user.isEnabled()) {
                return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("user.profile.disabled"));
            }

            transaction.setUserIdRef(user.getId());
            transactionRepository.createNew(transaction);

            user.setBalance(user.getBalance() + transaction.getAmount());
            userRepository.save(user);

            PushMessageDTO pushMessageDTO = new PushMessageDTO();

            pushMessageDTO.setUsername(user.getUsername());
            pushMessageDTO.setContent("Bạn vừa được tặng " + StringUtils.separatorNumberWithCharacter(transaction.getAmount()) + " đồng vào tài khoản. Số dư " + StringUtils.separatorNumberWithCharacter(user.getBalance()) + " đồng.");
            pushMessageDTO.setTitle("Số dư thay đổi");
            chatService.processPushMessageNow(pushMessageDTO);
        }

        return ResponseResult.newSuccessInstance(null);
    }

    Map<Integer,Long> convertIteratorToDTO(Iterable<DBObject> firstBatch){
        Map<Integer,Long> transactionStaticResultList = new HashMap<>();

        Iterator var7 = firstBatch.iterator();

        while(var7.hasNext()) {
            DBObject dbObject = (DBObject)var7.next();
            Integer position = (Integer)dbObject.get("_id");
            Long amount = (Long)dbObject.get("amount");
            transactionStaticResultList.put(position,amount);
        }


        return transactionStaticResultList;
    }

}
