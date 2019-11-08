package com.stadio.mobi.payment;

import com.stadio.common.utils.RandomString;
import com.stadio.mobi.config.Security;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.impl.BaseService;
import com.stadio.mobi.service.impl.TransactionService;
import com.stadio.mobi.service.impl.UserService;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.enu.TransactionType;
import com.stadio.model.repository.main.CardRepository;
import com.stadio.model.repository.main.GooglePayloadRepository;
import com.stadio.model.repository.main.PurchaseRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IGooglePayImpl extends BaseService implements IGooglePay {

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    GooglePayloadRepository googlePayloadRepository;

    @Autowired
    UserService userService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    CardRepository cardRepository;

    private static final Logger logger = LogManager.getLogger(IGooglePayImpl.class);

    private static final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA"
            + "jgOe+ydl5i0uWPFv6YA0ze44TfbGYN1QVO+62fWlAlphZ5WCcl8JoJypoawt8nN"
            + "LyGYDTq+h/7/1RvQ9P3lSYRkWuXjm8zlzaZ/zw//RwWf01GyDl8NYWnoBPILLROb"
            + "XUzK7aY1Js4ZQGyNawLwQ7yqh7AhGSPAqLhM74pX9g8S2pdqjocShSV6HGwz5PUo"
            + "cLyCYwbDwDeiUmh1XVhJ4CUe95ABtnd+vz4zo/LpazfY/9eMttvAoz1H1SUNqlP4Yvc"
            + "x2I5pVyvkzjlRe+VZbiJsTZEVI6sfvB/gLQTrOuZY/T/g+pmABwbaR0vvxtpYV/B27vLL0O3lTUubOEXYYPQIDAQAB";

    @Override
    public ResponseResult processVerifyAndCreatePurchase(String token, Purchase purchase) {
        User user = getUserRequesting();

        logger.info("getMOriginalJson: " + purchase.getMOriginalJson());
        logger.info("getMSignature: " + purchase.getMSignature());

        //step 1: token, order id, and product id verify signature
        boolean verify = Security.verifyPurchase(base64EncodedPublicKey, purchase.getMOriginalJson(), purchase.getMSignature());
        if (!verify) {
            return ResponseResult.newErrorInstance("408", getMessage("google.security.verify"));
        }

        //step 2: payload was verify
        String payload = purchase.getMDeveloperPayload();
        GoogleOrder googleOrder = googlePayloadRepository.findByPayload(payload);
        if (googleOrder.getStatus() != 1 && !googleOrder.getUserId().equals(user.getId())) {
            return ResponseResult.newErrorInstance("01", getMessage("google.payload.invalid"));
        }

        googleOrder.setStatus(2);
        googleOrder.setUpdatedDate(new Date());
        googlePayloadRepository.save(googleOrder);

        //step 3: verify with Google Play: GET https://www.googleapis.com/androidpublisher/v2/applications/packageName/purchases/products/productId/tokens/token
        //You can check after
        //TODO - de sau lam cung duoc

        //step 4: save to transaction
        Card card = Card.getListCard().stream().filter(x -> x.getId().equals(googleOrder.getProductId())).findFirst().orElse(null);
        if (card != null) {
            transactionService.processRecharge(user, card.getPrice(), TransactionType.CARD);
        }

        if (user != null) {
            purchase.setUserId(user.getId());
        }
        purchaseRepository.save(purchase);
        return ResponseResult.newSuccessInstance(null);
    }

    @Override
    public ResponseResult processGetListProducts(String accessToken) {
        User user = getUserRequesting();
        Map<String, Object> body = new HashMap<>();
        if (user != null) {
            body.put("base64EncodedPublicKey", base64EncodedPublicKey);

            List<Card> cardList = cardRepository.findAll();
            if (cardList.isEmpty()) {
                cardList = Card.getListCard();
                for (Card card: cardList) {
                    cardRepository.save(card);
                }
            }
            body.put("products", cardList);
        }
        ResponseResult result = ResponseResult.newSuccessInstance(body);
        return result;
    }

    @Override
    public ResponseResult generatePayload(String token, String productId) {
        User user = getUserRequesting();
        if (user != null) {
            RandomString randomString = new RandomString(36);
            logger.info("RandomString>>>>" + randomString.nextString());
            //Demo: bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ
            String payload = randomString.nextString();
            GoogleOrder googleOrder;
            do {
                googleOrder = googlePayloadRepository.findByPayload(payload);
                if (googleOrder == null) {
                    googleOrder = new GoogleOrder(user.getId(), payload, 0, productId);
                    googlePayloadRepository.save(googleOrder);
                    return ResponseResult.newSuccessInstance(googleOrder);
                }
            } while(true);
        }
        return ResponseResult.newSuccessInstance(null);
    }

    @Override
    public ResponseResult verifyPayload(String token, String payload) {
        User user = getUserRequesting();
        if (user != null) {
            GoogleOrder googleOrder = googlePayloadRepository.findByPayload(payload);
            if (googleOrder == null) {
                return ResponseResult.newErrorInstance("01", getMessage("google.payload.not_found"));
            }

            if (googleOrder.getStatus() != 0) {
                return ResponseResult.newErrorInstance("01", getMessage("google.payload.invalid"));
            }

            googleOrder.setStatus(1);
            googleOrder.setUpdatedDate(new Date());
            googlePayloadRepository.save(googleOrder);
            return ResponseResult.newSuccessInstance("OK");
        }
        return ResponseResult.newErrorInstance("99", null);
    }


}
