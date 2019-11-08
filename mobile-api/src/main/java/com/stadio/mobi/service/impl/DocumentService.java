package com.stadio.mobi.service.impl;

import com.stadio.common.utils.ClassFinder;
import com.stadio.common.utils.JsonUtils;
import com.stadio.common.utils.TypedMapper;
import com.stadio.mobi.service.IDocumentService;
import com.stadio.model.model.ApiDocument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 11/10/2017.
 */
@Service
public class DocumentService implements IDocumentService
{

    private Logger logger = LogManager.getLogger(DocumentService.class);

//    @Override
//    public Map<String, List<ApiDocument>> processGetListApi(String pkgName)
//    {
//        Map<String, List<ApiDocument>> results = new HashMap<>();
//        for (Class<?> ck : ClassFinder.find(pkgName))
//        {
//            if (ck.isAnnotationPresent(RestController.class))
//            {
//                List<ApiDocument> dk = results.get(ck.getSimpleName());
//                if (dk == null)
//                {
//                    dk = new ArrayList<>();
//                }
//                processScanMethod(ck, dk);
//                results.put(ck.getSimpleName(), dk);
//            }
//        }
//
//        return results;
//    }
//
//    @Override
//    public Map<String, List<ApiDocument>> processGetListApi(String pkgName, TypedMapper<ApiDocument> tf)
//    {
//        Map<String, List<ApiDocument>> results = processGetListApi(pkgName);
//
//        for (String sk: results.keySet())
//        {
//            List<ApiDocument> items = new ArrayList<>();
//            for (ApiDocument dk: results.get(sk))
//            {
//                if (tf.test(dk)) items.add(dk);
//            }
//            results.put(sk, items);
//        }
//        return results;
//    }
//
//    private void processScanMethod(Class<?> ck, List<ApiDocument> items)
//    {
//
//        for (Method mk : ck.getDeclaredMethods())
//        {
//            ApiDocument document = new ApiDocument();
//            RequestMapping rest = ck.getAnnotation(RequestMapping.class);
//            if (rest.value().length > 0)
//            {
//                document.setParent(rest.value()[0]);
//            }
//
//            if (mk.isAnnotationPresent(GetMapping.class))
//            {
//                GetMapping gk = mk.getAnnotation(GetMapping.class);
//                if (gk.value().length > 0)
//                {
//                    document.setChild(gk.value()[0]);
//                }
//                document.setMethod("GET");
//                addFurtherInformation(mk, document);
//                items.add(document);
//            }
//
//            if (mk.isAnnotationPresent(PostMapping.class))
//            {
//                PostMapping pk = mk.getAnnotation(PostMapping.class);
//                if (pk.value().length > 0)
//                {
//                    document.setChild(pk.value()[0]);
//                }
//                document.setMethod("POST");
//                addFurtherInformation(mk, document);
//                items.add(document);
//            }
//
//            if (mk.isAnnotationPresent(RequestMapping.class))
//            {
//                RequestMapping rk = mk.getAnnotation(RequestMapping.class);
//                if (rk.value().length > 0)
//                {
//                    document.setChild(rk.value()[0]);
//                }
//
//                if (rk.method().length > 0)
//                {
//                    document.setMethod(rk.method()[0].name());
//                }
//
//                addFurtherInformation(mk, document);
//                items.add(document);
//            }
//
//        }
//
//    }
//
//    private void addFurtherInformation(Method mk, ApiDocument document)
//
//    {
//        document.setName(mk.getName());
//        System.out.println(">>> " + mk.getName());
//        for(Parameter pk: mk.getParameters())
//        {
//            if (pk.isAnnotationPresent(RequestBody.class))
//            {
//                Class<?> ck = pk.getType();
//                try
//                {
//                    document.setRequest(JsonUtils.pretty(ck.newInstance()));
//                }
//                catch (InstantiationException e)
//                {
//                    logger.debug("InstantiationException: ", e);
//                }
//                catch (IllegalAccessException e)
//                {
//                    logger.debug("IllegalAccessException: ", e);
//                }
//            }
//            else if (pk.isAnnotationPresent(RequestParam.class))
//            {
//                String request = document.getRequest();
//                RequestParam param = pk.getAnnotation(RequestParam.class);
//
//                String rk = "[key = " + param.value()
//                        + ", required = " + param.required()
//                        + "]</br>";
//                document.setRequest((request == null) ? rk : (request + rk));
//            }
//            else if (pk.isAnnotationPresent(RequestHeader.class))
//            {
//
//                String request = document.getHeaders();
//                RequestHeader param = pk.getAnnotation(RequestHeader.class);
//
//                String rk = "[key = " + param.value()
//                        + ", required = " + param.required()
//                        + "]</br>";
//                document.setHeaders((request == null) ? rk : (request + rk));
//            }
//        }
//    }
}
