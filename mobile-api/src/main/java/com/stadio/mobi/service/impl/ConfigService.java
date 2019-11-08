package com.stadio.mobi.service.impl;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IConfigService;
import com.stadio.model.documents.*;
import com.stadio.model.enu.ConfigKey;
import com.stadio.model.repository.main.ConfigRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigService extends BaseService implements IConfigService
{
    @Autowired ConfigRepository configRepository;

    private Logger logger = LogManager.getLogger();

    @Override
    public ResponseResult processGetMobileConfig()
    {
        //MobileConfig mobileConfig = new MobileConfig();

        Map<String, Object> mobileConfig = new HashMap<>();

        List<Config> configList = configRepository.findAll();

        configList.forEach(config ->
        {
            String key = config.getKey();
            if (key.equals(ConfigKey.MAX_FAST_PRACTICE.name()))
            {
                int maxFastPractice = 5;
                try
                {
                    maxFastPractice = Integer.parseInt(config.getValue());
                }
                catch (Exception e)
                {
                    logger.error("maxFastPractice exception: ", e);
                }

                mobileConfig.put(key, maxFastPractice);
            }

            if (key.equals(ConfigKey.MODE_PAYMENT.name()))
            {
                if ("1".equals(config.getValue()))
                {
                    mobileConfig.put(key, true);
                }
                else
                {
                    mobileConfig.put(key, false);
                }
            }

            addValueWithConditions(
                    mobileConfig,
                    config,
                    ConfigKey.PRICE_FAST_PRACTICE.name(),
                    ConfigKey.MODE_PAYMENT_IOS.name(),
                    ConfigKey.ANDROID_MIN_VERSION.name(),
                    ConfigKey.ANDROID_CURRENT_VERSION.name(),
                    ConfigKey.ANDROID_UPDATE_URL.name(),
                    ConfigKey.IOS_MIN_VERSION.name(),
                    ConfigKey.IOS_UPDATE_URL.name(),
                    ConfigKey.IOS_CURRENT_VERSION.name(),
                    ConfigKey.U_DATE_DEADLINE.name(),
                    ConfigKey.FACEBOOK_GROUP.name(),
                    ConfigKey.FACEBOOK_PAGE.name(),
                    ConfigKey.FACEBOOK_MESSENGER.name(),
                    ConfigKey.WEBSITE_URL.name(),
                    ConfigKey.POLICY_URL.name(),
                    ConfigKey.PRIVACY_URL.name(),
                    ConfigKey.PURCHASE_URL.name(),
                    ConfigKey.MOBILE_IMAGE_DEFAULT.name()
            );
        });

        return ResponseResult.newSuccessInstance(mobileConfig);
    }

    @Override
    public Config getConfigByKey(String key) {
        return configRepository.findConfigByKey(key);
    }

    void addValueWithConditions(Map<String, Object> mobileConfig, Config config, String... conditions)
    {
        for (String ck: conditions)
        {
            if (config.getKey().equals(ck))
            {
                mobileConfig.put(config.getKey(), config.getValue());
            }
        }
    }
}
