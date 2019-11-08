package com.stadio.mobi.controllers;

import com.stadio.mobi.service.IDocumentService;
import com.stadio.mobi.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Andy on 11/10/2017.
 */
@RestController
@RequestMapping("dev")
public class DevConfigController {

    @Autowired
    IDocumentService documentService;

    @Autowired
    UserService userService;

    private static final String PACKAGE_REST = "com.stadio.mobi.controllers";


//    @GetMapping(value = "documents", produces = {"text/html"})
//    public String documentsList(
//            @RequestParam(value = "type", required = false, defaultValue = "0") int type) throws BadHttpRequest {
//
//        Map<String, List<ApiDocument>> results;
//
//        if (type == 1) {
//            results = documentService.processGetListApi(PACKAGE_REST, x -> true);
//        }
//        else
//        {
//            results = documentService.processGetListApi(PACKAGE_REST, x -> x.getTar() == null
//                    || x.getTar().equals(Target.ALL)
//                    || x.getTar().equals(Target.MOBILE)
//                    || x.getTar().equals(Target.CMS)
//                    || x.getTar().equals(Target.WEB)
//            );
//        }
//
//        StringBuilder html = new StringBuilder();
//        html.append("<html><head>");
//        html.append("<title>HOC68 API DOCUMENTS</title>");
//        html.append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css\">");
//        html.append("</head><body><div style='padding: 10px;'>");
//
//        for (String sk: results.keySet())
//        {
//            List<ApiDocument> items = results.get(sk);
//            if (!items.isEmpty())
//            {
//                html.append("<h3>" + sk + "</h3>");
//            }
//            html.append("<table border=1 style=width:100%; class='table table-dark table-hover'><thead>");
//            if (!items.isEmpty())
//            {
//                html.append("<th style='text-align:center;'>API</th>");
//                html.append("<th style='text-align:center;'>Router</th>");
//                html.append("<th style='text-align:center;'>Method</th>");
//                html.append("<th style='text-align:center;'>Headers</th>");
//                html.append("<th style='text-align:center;'>Request</th>");
//
//            }
//            html.append("</thead><tbody>");
//            for (ApiDocument dk: results.get(sk))
//            {
//                html.append("<tr>");
//                html.append("<td><a href='/" + dk.getPath() + "'>" + dk.getName()  + "</a></td>");
//                html.append("<td><a href='/" + dk.getPath() + "'>" + dk.getPath()  + "</a></td>");
//                html.append("<td>" + dk.getMethod() + "</td>");
//                html.append("<td>" + dk.getHeaders() + "</td>");
//                html.append("<td>" + dk.getRequest() + "</td>");
//                html.append("</tr>");
//
//            }
//            html.append("</tbody></table>");
//        }
//
//        html.append("</div></body></html>");
//        return html.toString();
//    }
//
//    @GetMapping(value = "/profileUser")
//    public ResponseEntity getUserProfile(
//            @RequestParam("id") String id, Principal user)
//    {
//        ResponseResult result = userService.processGetProfileById(id);
//        return ResponseEntity.ok(result);
//    }

}
