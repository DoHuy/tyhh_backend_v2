package com.stadio.cms.service.impl;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.dtos.authorization.FeatureChild;
import com.stadio.cms.dtos.authorization.FeatureParent;
import com.stadio.cms.model.ApiDocument;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IDocumentService;
import com.stadio.common.utils.ClassFinder;
import com.stadio.common.utils.JsonUtils;
import com.stadio.common.utils.TypedMapper;
import com.stadio.model.documents.MDFeature;
import com.stadio.model.repository.main.FeatureRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Andy on 11/10/2017.
 */
@Service
public class DocumentService implements IDocumentService {

    private Logger logger = LogManager.getLogger(DocumentService.class);

    private static final String TAG = "DocumentService";

    @Autowired
    FeatureRepository featureRepository;

    @Override
    public Map<String, List<ApiDocument>> processGetListApi(String pkgName) {
        Map<String, List<ApiDocument>> results = new HashMap<>();
        for (Class<?> ck : ClassFinder.find(pkgName)) {
            if (ck.isAnnotationPresent(RestController.class)) {
                List<ApiDocument> dk = results.get(ck.getSimpleName());
                if (dk == null) {
                    dk = new ArrayList<>();
                }
                processScanMethod(ck, dk);
                results.put(ck.getSimpleName(), dk);
            }
        }

        return results;
    }

    @Override
    public Map<String, List<ApiDocument>> processGetListApi(String pkgName, TypedMapper<ApiDocument> tf) {
        Map<String, List<ApiDocument>> results = processGetListApi(pkgName);

        for (String sk : results.keySet()) {
            List<ApiDocument> items = new ArrayList<>();
            for (ApiDocument dk : results.get(sk)) {
                if (tf.test(dk)) items.add(dk);
            }
            results.put(sk, items);
        }
        return results;
    }

    @Override
    public ResponseResult processGetListFeature() {
        List<FeatureParent> featureParentList = new ArrayList<>();

        for (MDFeature parent: featureRepository.findByControllerNotNull()) {
            FeatureParent featureParent = new FeatureParent();
            featureParent.setController(parent.getController());
            featureParent.setHash(parent.getHash());
            featureParent.setPath(parent.getPath());
            featureParent.setName(parent.getName());

            List<MDFeature> children = featureRepository.findByFeatureId(parent.getId());
            List<FeatureChild> featureChildList = new ArrayList<>();
            for (MDFeature child: children) {
                FeatureChild featureChild = new FeatureChild();
                featureChild.setName(child.getName());
                featureChild.setFullPath(child.getPath());
                featureChild.setHash(child.getHash());
                featureChild.setMethod(child.getMethod());

                featureChildList.add(featureChild);
            }

            featureParent.setChildren(featureChildList);

            featureParentList.add(featureParent);
        }

        return ResponseResult.newSuccessInstance(featureParentList);
    }

    @Override
    public ResponseResult processUpdateListFeatureToDB() {
        //featureRepository.deleteAll();

        logger.info("processUpdateListFeatureToDB");

        updateFeature();

        return ResponseResult.newSuccessInstance(null);
    }

    @Async
    public void updateFeature() {
        String pkgName = "com.stadio.cms.controllers";
        List<Class<?>> classList = ClassFinder.find(pkgName).stream()
                .filter(x -> x.isAnnotationPresent(RestController.class) && hasFeatureName(x))
                .collect(Collectors.toList());

        for (Class<?> ck: classList) {
            RequestMapping requestMapping = ck.getAnnotation(RequestMapping.class);
            if (requestMapping.value().length == 0) continue;
            String parentPath = requestMapping.value()[0];
            String parentHash = "";

            try {
                parentHash = com.stadio.common.utils.StringUtils.identifier_MD5(parentPath);
            } catch (Exception e) {
                e.printStackTrace();
            }

            MDFeature mdFeatureParent = featureRepository.findByHash(parentHash);
            if (mdFeatureParent == null) {
                mdFeatureParent = new MDFeature();
            } else {
                mdFeatureParent.setUpdatedDate(new Date());
            }

            mdFeatureParent.setController(ck.getCanonicalName());
            mdFeatureParent.setHash(parentHash);
            mdFeatureParent.setName(requestMapping.name());
            mdFeatureParent.setFeatureId(null);
            mdFeatureParent.setPath(parentPath);

            featureRepository.save(mdFeatureParent);

            List<Method> methodList = Arrays.stream(ck.getDeclaredMethods()).filter(m ->
                    (m.isAnnotationPresent(GetMapping.class) || m.isAnnotationPresent(PostMapping.class) || m.isAnnotationPresent(RequestMapping.class))
                            && getFeatureName(m).length() > 0).collect(Collectors.toList());

            for (Method m: methodList) {
                String name = getFeatureName(m);
                String path = getFeaturePath(m);
                String fullPath = parentPath.endsWith("/") || path.startsWith("/") ? (parentPath + path) : (parentPath + "/" + path);
                fullPath = fullPath.startsWith("/") ? fullPath : ("/" + fullPath);
                String childHash = "";
                try {
                    childHash = com.stadio.common.utils.StringUtils.identifier_MD5(fullPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                MDFeature mdFeatureChild = featureRepository.findByHash(childHash);
                if (mdFeatureChild == null) {
                    mdFeatureChild = new MDFeature();
                } else {
                    mdFeatureChild.setUpdatedDate(new Date());
                }
                mdFeatureChild.setHash(childHash);
                mdFeatureChild.setPath(fullPath);
                mdFeatureChild.setFeatureId(mdFeatureParent.getId());
                mdFeatureChild.setName(name);
                mdFeatureChild.setController(null);
                mdFeatureChild.setMethod(m.getName());

                featureRepository.save(mdFeatureChild);
            }

        }
    }


    private boolean hasFeatureName(Class<?> x) {
        RequestMapping requestMapping = x.getAnnotation(RequestMapping.class);
        if (StringUtils.isNotBlank(requestMapping.name())) return true;
        else return false;
    }

    private String getFeatureName(Method m) {
        if (m.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = m.getAnnotation(RequestMapping.class);
            if (StringUtils.isNotBlank(requestMapping.name())) return requestMapping.name();
        }
        if (m.isAnnotationPresent(GetMapping.class)) {
            GetMapping getMapping = m.getAnnotation(GetMapping.class);
            if (StringUtils.isNotBlank(getMapping.name())) return getMapping.name();
        }
        if (m.isAnnotationPresent(PostMapping.class)) {
            PostMapping postMapping = m.getAnnotation(PostMapping.class);
            if (StringUtils.isNotBlank(postMapping.name())) return postMapping.name();
        }
        return "";
    }

    private String getFeaturePath(Method m) {

        if (m.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = m.getAnnotation(RequestMapping.class);
            if (requestMapping.value().length > 0) return requestMapping.value()[0];
        }
        if (m.isAnnotationPresent(GetMapping.class)) {
            GetMapping getMapping = m.getAnnotation(GetMapping.class);
            if (getMapping.value().length > 0) return getMapping.value()[0];
        }
        if (m.isAnnotationPresent(PostMapping.class)) {
            PostMapping postMapping = m.getAnnotation(PostMapping.class);
            if (postMapping.value().length > 0) return postMapping.value()[0];
        }

        return "";
    }

    private void processScanMethod(Class<?> ck, List<ApiDocument> items) {

        for (Method mk : ck.getDeclaredMethods()) {
            ApiDocument document = new ApiDocument();
            RequestMapping rest = ck.getAnnotation(RequestMapping.class);
            if (rest.value().length > 0) {
                document.setParent(rest.value()[0]);
            }

            if (mk.isAnnotationPresent(GetMapping.class)) {
                GetMapping gk = mk.getAnnotation(GetMapping.class);
                if (gk.value().length > 0) {
                    document.setChild(gk.value()[0]);
                }
                document.setMethod("GET");
                addFurtherInformation(mk, document);
                items.add(document);
            }

            if (mk.isAnnotationPresent(PostMapping.class)) {
                PostMapping pk = mk.getAnnotation(PostMapping.class);
                if (pk.value().length > 0) {
                    document.setChild(pk.value()[0]);
                }
                document.setMethod("POST");
                addFurtherInformation(mk, document);
                items.add(document);
            }

            if (mk.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping rk = mk.getAnnotation(RequestMapping.class);
                if (rk.value().length > 0) {
                    document.setChild(rk.value()[0]);
                }

                if (rk.method().length > 0) {
                    document.setMethod(rk.method()[0].name());
                }

                addFurtherInformation(mk, document);
                items.add(document);
            }

        }

    }

    private void addFurtherInformation(Method mk, ApiDocument document) {
        document.setName(mk.getName());
        System.out.println(">>> " + mk.getName());
        for (Parameter pk : mk.getParameters()) {
            if (pk.isAnnotationPresent(RequestBody.class)) {
                Class<?> ck = pk.getType();
                try {
                    document.setRequest(JsonUtils.pretty(ck.newInstance()));
                } catch (InstantiationException e) {
                    logger.debug("InstantiationException: ", e);
                } catch (IllegalAccessException e) {
                    logger.debug("IllegalAccessException: ", e);
                }
            } else if (pk.isAnnotationPresent(RequestParam.class)) {
                String request = document.getRequest();
                RequestParam param = pk.getAnnotation(RequestParam.class);

                String rk = "[key = " + param.value()
                        + ", required = " + param.required()
                        + "]</br>";
                document.setRequest((request == null) ? rk : (request + rk));
            } else if (pk.isAnnotationPresent(RequestHeader.class)) {

                String request = document.getHeaders();
                RequestHeader param = pk.getAnnotation(RequestHeader.class);

                String rk = "[key = " + param.value()
                        + ", required = " + param.required()
                        + "]</br>";
                document.setHeaders((request == null) ? rk : (request + rk));
            }
        }
    }
}
