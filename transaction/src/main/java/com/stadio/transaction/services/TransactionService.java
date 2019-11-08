package com.stadio.transaction.services;

import com.hoc68.users.documents.User;
import com.stadio.model.repository.user.UserRepository;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    private Logger logger = LogManager.getLogger(TransactionService.class);

    private SimpleDateFormat fm = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

    @Autowired
    UserRepository userRepository;

    public void import20kToUser() {
        File f = new File("/opt/export_transaction_" + System.currentTimeMillis() + ".csv");

        try (FileOutputStream fos = new FileOutputStream(f);
             OutputStreamWriter osw = new OutputStreamWriter(fos)){

            List<User> userList = userRepository.findAll();
            logger.info("==>>> Has " + userList.size() + " users");

            for (User user : userList) {
                long balance = user.getBalance();
                user.setBalance(balance + 20000);
                userRepository.save(user);

                String currentDate = fm.format(new Date());
                String lineBuilder = user.getId() + "," + user.getCode() + "," + user.getUsername() + "," + balance + "," + user.getBalance() + "," + currentDate;
                logger.info(lineBuilder);

                //FileUtils.write(f, lineBuilder, "UTF-8");
                osw.write(lineBuilder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
