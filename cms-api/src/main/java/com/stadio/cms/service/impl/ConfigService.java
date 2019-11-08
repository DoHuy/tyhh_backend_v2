package com.stadio.cms.service.impl;

import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IConfigService;
import com.stadio.model.documents.Config;
import com.stadio.model.repository.main.ConfigRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ConfigService extends BaseService implements IConfigService
{

    @Autowired
    ConfigRepository configRepository;

    @Override
    public ResponseResult processPutValue(String key, String value, String name)
    {
        Config config = configRepository.findConfigByKey(key);
        if (config == null)
        {
            config = new Config();
            config.setKey(key);
        } else {
            config.setUpdatedDate(new Date());
        }

        config.setValue(value);

        if (StringUtils.isNotBlank(name)) {
            config.setName(name);
        }
        configRepository.save(config);
        return ResponseResult.newSuccessInstance(config);
    }

    @Override
    public ResponseResult processGetListConfig()
    {
        return ResponseResult.newSuccessInstance(configRepository.findAll());
    }

    @Override
    public ResponseResult processGetDetails(String key)
    {
        return ResponseResult.newSuccessInstance(configRepository.findConfigByKey(key));
    }


}
