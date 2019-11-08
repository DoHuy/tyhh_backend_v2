package com.stadio.cms.service.impl;

import com.stadio.cms.model.PageInfo;
import com.stadio.cms.response.TableList;
import com.stadio.cms.service.IRechargeCardService;
import com.stadio.common.custom.RequestHandler;
import com.stadio.common.request.MediaUploadRequest;
import com.stadio.common.utils.*;
import com.stadio.model.documents.*;
import com.stadio.model.dtos.cms.recharge.*;
import com.stadio.cms.response.ResponseResult;
import com.stadio.model.dtos.cms.recharge.CODOrderDTO;
import com.stadio.model.enu.CODOrderStatus;
import com.stadio.model.enu.SequenceKey;
import com.stadio.model.repository.main.CODOrderRepository;
import com.stadio.model.repository.main.RechargeCardExportRepository;
import com.stadio.model.repository.main.RechargeCardRepository;
import com.stadio.model.repository.main.SequenceIndexRepository;
import com.stadio.model.repository.main.UserRechargeActionRepository;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.stream.Collectors;

@Service
public class RechargeCardService extends BaseService  implements IRechargeCardService {

    public static final Random gen = new Random();

    @Autowired
    RechargeCardRepository rechargeCardRepository;

    @Autowired
    SequenceIndexRepository sequenceIndexRepository;

    @Autowired
    ManagerService managerService;

    @Autowired
    UserRechargeActionRepository userRechargeActionRepository;

    @Autowired
    RechargeCardExportRepository rechargeCardExportRepository;

    @Autowired
    CODOrderRepository codOrderRepository;

    @Autowired
    UserService userService;

    @Autowired
    RequestHandler requestHandler;

    @Value("${domain.mediation}")
    private String domainMedia;

    private Logger logger = LogManager.getLogger(RechargeCardService.class);

    private static final int numberOfCardPerInit = 1000;

    @Override
    public ResponseResult processInitNewCards() {

        /*
                xxx.xxx.xxx.xxx -> xac xuat laf 1/100.000

                1000 tu 1 - 100.000.000, tu 100.000.001 -> 200.000.000
                lan thu 9 -> tu
                lan thu 11 -> tu 1.100.000.001 - 1.200.000.000
                1000 tu 99.900.000.001 - 100.000.000.000
        */

        SequenceIndex sequenceIndex;
        sequenceIndex = sequenceIndexRepository.findFirstBySequenceKey(SequenceKey.Recharge.name());
        if (sequenceIndex == null) {
            sequenceIndex = new SequenceIndex();
            sequenceIndex.setSequenceKey(SequenceKey.Recharge.name());
            sequenceIndex.setCurrentSequence(0);
            sequenceIndexRepository.save(sequenceIndex);
        } else if (rechargeCardRepository.count() == 0) {
            sequenceIndex.setCurrentSequence(0);
            sequenceIndexRepository.save(sequenceIndex);
        }

        if (rechargeCardRepository.count() > sequenceIndex.getCurrentSequence()) {
            //If There is problem add card before, alert and check manual
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, "Tạo thẻ bị lỗi, check index" + String.valueOf(sequenceIndex.getCurrentSequence()));
        }

        int boundPerCardInit = 100000000;
        int maxNumberCanGenerate = numberOfCardPerInit * 1000;

        int timesGenarated = (int) ((rechargeCardRepository.count()) / numberOfCardPerInit);
        if (timesGenarated >= maxNumberCanGenerate) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, "Last round generate card found");
        }

        int[] suffixNumbers = this.getRandomNumbers(numberOfCardPerInit, (boundPerCardInit * (timesGenarated + 1)), (boundPerCardInit * timesGenarated) + 1);

        int idx = 0;
        for (int suffix: suffixNumbers) {
            RechargeCard rechargeCard = new RechargeCard();
            rechargeCard.setCardNumberSuffix(suffix);
            rechargeCard.setCardNumber(String.valueOf(rechargeCard.getCardNumberSuffix()));
            rechargeCard.setSerial(Hex.encodeHexString((String.valueOf(sequenceIndex.getCurrentSequence() + idx) + String.valueOf(gen.nextInt(100))).getBytes()));

            rechargeCardRepository.save(rechargeCard);

            idx++;
        }

        sequenceIndex.setCurrentSequence(sequenceIndex.getCurrentSequence() + numberOfCardPerInit);
        sequenceIndexRepository.save(sequenceIndex);

        return ResponseResult.newSuccessInstance(null);
    }

    int[] getRandomNumbers(int n, int max, int min) {
        assert n <= max : "cannot get more unique numbers than the size of the range";

        int[] result = new int[n];
        Set<Long> used = new HashSet<>();

        for (int i = 0; i < n; i++) {
            int newRandom;
            do {
                newRandom = gen.nextInt(max - min) + min;
            } while (used.contains(newRandom));
            result[i] = newRandom;
            used.add(Long.valueOf(newRandom));
        }
        return result;
    }

    @Override
    public ResponseResult searchCards(RechargeCardSearchFormDTO rechargeCardSearchFormDTO) {
        List<RechargeCard> rechargeCards = rechargeCardRepository.findWithFormSearch(rechargeCardSearchFormDTO);
        List<RechargeCardDTO> rechargeCardDTOS = new ArrayList<>();
        for (RechargeCard card: rechargeCards) {
            rechargeCardDTOS.add(new RechargeCardDTO(card));
        }
        TableList tableList = new TableList();
        tableList.setPageInfo(new PageInfo(
                rechargeCardSearchFormDTO.getPage(),
                rechargeCardRepository.countWithFormSearch(rechargeCardSearchFormDTO),
                rechargeCardDTOS.size(),
                null)
        );
        tableList.setItems(rechargeCardDTOS);
        return ResponseResult.newSuccessInstance( tableList);
    }

    @Override
    public ResponseResult getCardsStatus() {
        RechargeCardStatusDTO rechargeCardStatus = new RechargeCardStatusDTO();
        rechargeCardStatus.setTotalOfCards(rechargeCardRepository.count());
        rechargeCardStatus.setNumberOfBlankCard(rechargeCardRepository.countByValueIs(0));
        rechargeCardStatus.setNumberOfValuableCard(rechargeCardRepository.countByValueIsGreaterThan(0));
        rechargeCardStatus.setNumberOfPrintedCard(rechargeCardRepository.countByIsPrinted(true));
        rechargeCardStatus.setNumberOfUsedCard(rechargeCardRepository.countByUserIdUsedIsNotNull());
        rechargeCardStatus.setRevenue(rechargeCardRepository.sumRevenue());

        return ResponseResult.newSuccessInstance(rechargeCardStatus);
    }


    @Override
    public ResponseResult createCards(long value, int quantity) {
        if (quantity == 0) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("recharge.create.invalid.quantify"));
        }
        if (value < 10000 || value > 1000000) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("recharge.create.invalid.value"));
        }

        if (quantity > numberOfCardPerInit / 2) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("recharge.create.quantify.too.large") + String.valueOf(numberOfCardPerInit / 2));
        } else if (quantity > rechargeCardRepository.countByValueIs(0)) {
            ResponseResult initCardResults = this.processInitNewCards();
            if (!initCardResults.getErrorCode().equals(ResponseCode.SUCCESS)) {
                return initCardResults;
            }
        }

        List<RechargeCard> rechargeCards = rechargeCardRepository.findNoValueCards(1, quantity);
        List<RechargeCardDTO> rechargeCardDTOS = new ArrayList<>();
        for (RechargeCard card: rechargeCards) {
            card.setValue(value);
            rechargeCardRepository.save(card);
            rechargeCardDTOS.add(new RechargeCardDTO(card));
        }
        return ResponseResult.newSuccessInstance(rechargeCardDTOS);
    }

    @Override
    public ResponseResult processExportToExcel(long value, int quantity) {
        ResponseResult printCardResults = printCardWith(value, quantity);
        if (!printCardResults.getErrorCode().equals(ResponseCode.SUCCESS)) {
            return printCardResults;
        }
        List<RechargeCard> rechargeCards = (List<RechargeCard>) printCardResults.getData();
        return writeToExcel(rechargeCards, quantity, value);
    }

    private ResponseResult printCardWith(long value, int quantity) {
        if (quantity == 0) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("recharge.create.invalid.quantify"));
        }
        if (value < 10000 || value > 1000000) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("recharge.create.invalid.value"));
        }

        int countCardNotPrinted = rechargeCardRepository.countByValueIsAndIsPrinted(value, false);

        if (quantity > countCardNotPrinted) {
            ResponseResult initCardResults = this.createCards(value, quantity - countCardNotPrinted);
            if (!initCardResults.getErrorCode().equals(ResponseCode.SUCCESS)) {
                return initCardResults;
            }
        }

        Pageable pageRequest = new PageRequest(0, quantity);

        List<RechargeCard> rechargeCards = rechargeCardRepository.findAllByValueIsAndIsPrintedIs(value, false,pageRequest);
        return ResponseResult.newSuccessInstance(rechargeCards);
    }

    private ResponseResult writeToExcel(List<RechargeCard> rechargeCards, int quantity, long value) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet 1");
        Object[][] datas = new Object[rechargeCards.size() + 1][];

        datas[0] = new String[]{"Serial", "Mã thẻ", "Mệnh giá"};
        int idx = 1;
        for (RechargeCard card: rechargeCards) {
            //DO excel thing here
            datas[idx] = new String[]{card.getSerial(), card.getCardNumber(), StringUtils.separatorNumberWithCharacter(Long.valueOf(card.getValue()))};
            idx++;
        }

        int rowNum = 0;

        for (Object[] datatype : datas) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : datatype) {
                Cell cell = row.createCell(colNum++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
            }
        }

        try {
            File file = new File("hehe.xlsx");

            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);

            MediaUploadRequest request = new MediaUploadRequest();
            String urlDocument = request.uploadDocument(this.domainMedia, this.requestHandler.getToken(), file, true);

            if (StringUtils.isNotNull(urlDocument)) {
                for (RechargeCard card: rechargeCards) {
                    //Save
                    card.setPrinted(true);
                    rechargeCardRepository.save(card);
                }

                RechargeCardExport export = new RechargeCardExport();
                export.setManagerId(managerService.getManagerRequesting().getId());
                export.setManagerUsername(managerService.getManagerRequesting().getUsername());
                export.setCardValue(value);
                export.setQuantity(quantity);

                export.setTitle(String.valueOf(quantity) + " || " + String.valueOf(value) + " || " + DateUtils.normalFormatGMT7DateString(export.getCreatedDate()));
                export.setFileUrl(urlDocument);

                if (!HelperUtils.isEmptyArray(rechargeCards)) {
                    export.setRechargeCardIds(rechargeCards.stream().map(sc -> sc.getId()).collect(Collectors.toList()));
                }

                rechargeCardExportRepository.save(export);

                return ResponseResult.newSuccessInstance(export);
            } else {
                return ResponseResult.newErrorDefaultInstance("Upload file bị lỗi");
            }

        } catch (Exception e) {
            logger.error("Upload document error", e);
        }
        return ResponseResult.newErrorDefaultInstance("Upload file bị lỗi");
    }

    @Override
    public ResponseResult processCheckCardBySerial(String serial) {
        RechargeCard rechargeCard = rechargeCardRepository.findFirstBySerial(serial);
        if (rechargeCard == null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("recharge.no.serial.match"));
        }
        return ResponseResult.newSuccessInstance(new RechargeCardDTO(rechargeCard));
    }

    @Override
    public ResponseResult getRechargeCardActionHistory(UserRechargeActionSearchFormDTO searchFormDTO) {
        List<UserRechargeAction> userRechargeActions = userRechargeActionRepository.findWithFormSearch(searchFormDTO);
        List<UserRechargeActionDTO> userRechargeActionDTOS = new ArrayList<>();
        for (UserRechargeAction action: userRechargeActions) {
            userRechargeActionDTOS.add(new UserRechargeActionDTO(action, rechargeCardRepository));
        }

        TableList tableList = new TableList();
        tableList.setPageInfo(new PageInfo(
                searchFormDTO.getPage(),
                userRechargeActionRepository.countWithFormSearch(searchFormDTO),
                userRechargeActionDTOS.size(),
                null)
        );
        tableList.setItems(userRechargeActionDTOS);
        return ResponseResult.newSuccessInstance(tableList);
    }

    @Override
    public ResponseResult getRechargeCardExportHistory(ExportRechargeSearchFormDTO searchFormDTO) {
        List<RechargeCardExport> rechargeCardExports = rechargeCardExportRepository.findWithFormSearch(searchFormDTO);
        List<RechargeCardExportDTO> rechargeCardExportDTOS = new ArrayList<>();
        for (RechargeCardExport export: rechargeCardExports) {
            RechargeCardExportDTO rechargeCardExportDTO = new RechargeCardExportDTO(export);
            rechargeCardExportDTO.with(managerService.managerRepository);
            rechargeCardExportDTOS.add(rechargeCardExportDTO);
        }

        TableList tableList = new TableList();
        tableList.setPageInfo(new PageInfo(
                searchFormDTO.getPage(),
                rechargeCardExportRepository.countWithFormSearch(searchFormDTO),
                rechargeCardExportDTOS.size(),
                null)
        );
        tableList.setItems(rechargeCardExportDTOS);
        return ResponseResult.newSuccessInstance(tableList);
    }

    @Override
    public ResponseResult processGetListCODOrder(CODOrderSearchFormDTO searchFormDTO) {
        List<CODOrder> codOrders = codOrderRepository.findWithFormSearch(searchFormDTO);
        List<CODOrderDTO> codOrderDTOS = new ArrayList<>();
        for (CODOrder order: codOrders) {
            codOrderDTOS.add(CODOrderDTO.with(order, rechargeCardRepository));
        }

        TableList tableList = new TableList();
        tableList.setPageInfo(new PageInfo(
                searchFormDTO.getPage(),
                codOrderRepository.countWithFormSearch(searchFormDTO),
                searchFormDTO.getPageSize(),
                null)
        );
        tableList.setItems(codOrderDTOS);
        return ResponseResult.newSuccessInstance(tableList);
    }

    @Override
    public ResponseResult processUpdateOrderStatus(String orderId, CODOrderStatus status, String reason) {

        CODOrder order = codOrderRepository.findOne(orderId);
        if (order == null) {
            return ResponseResult.newErrorDefaultInstance("order not found");
        }
        if (status == null || status == order.getStatus()) {
            return ResponseResult.newErrorDefaultInstance("Trạng thái cập nhập bị trùng");
        }

        order.setStatus(status);
        order.setUpdateReason(reason);
        order.setUpdatedDate(new Date());
        order.setUpdatedBy(managerService.getManagerRequesting().getId());

        if (status == CODOrderStatus.IN_PROCESS) {
            ResponseResult result = this.printCardWith(order.getValue(), 1);
            if (!result.getErrorCode().equals(ResponseCode.SUCCESS)) {
                return result;
            } else {
                RechargeCard card = ((List<RechargeCard>)result.getData()).get(0);
                card.setPrinted(true);
                card.setUserIdOrdered(order.getUserId());
                rechargeCardRepository.save(card);

                order.setRechargeCardId(card.getId());

            }
        } else if (status == CODOrderStatus.CANCELLED_BY_USER && StringUtils.isNotNull(order.getRechargeCardId())) {
            //Destroy recharge card
            RechargeCard rechargeCard = rechargeCardRepository.findOne(order.getRechargeCardId());
            if (rechargeCard != null) {
                rechargeCard.setIsEnable(Boolean.FALSE);
                rechargeCardRepository.save(rechargeCard);
            }
        }

        codOrderRepository.save(order);

        return ResponseResult.newSuccessInstance(CODOrderDTO.with(order, rechargeCardRepository));
    }

    @Override
    public ResponseResult processUpdateOrder(CODOrderFormDTO form) {

        CODOrder order = codOrderRepository.findOne(form.getId());
        if (order == null) {
            return ResponseResult.newErrorDefaultInstance("order not found");
        }
        order.setValue(form.getValue());
        order.setAddress(form.getAddress());
        order.setUserFullName(form.getUserFullName());
        order.setUserPhone(form.getUserPhone());
        order.setNote(form.getNote());

        order.setUpdatedDate(new Date());
        order.setUpdatedBy(managerService.getManagerRequesting().getId());

        codOrderRepository.save(order);

        return ResponseResult.newSuccessInstance(CODOrderDTO.with(order, rechargeCardRepository));
    }

}
