package com.stadio.cms.controllers.chemistry;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stadio.cms.controllers.BaseController;
import com.stadio.model.documents.chemistry.PeriodicTable;
import com.stadio.model.repository.chemistry.PeriodicTableRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/periodic")
public class PeriodicTableController extends BaseController {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    PeriodicTableRepository periodicTableRepository;

    @Value("${domain.mediation}")
    private String domainMedia;

    @GetMapping(value = "/import")
    public ResponseEntity importDataToMongoDB() {

        periodicTableRepository.deleteAll();
        try {
            InputStream f = new ClassPathResource("imports/periodic_table.json").getInputStream();

            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            List<PeriodicTable> periodicTableList = mapper.readValue(f, new TypeReference<List<PeriodicTable>>(){});
            System.out.println(periodicTableList.size());
            for (PeriodicTable periodicTable: periodicTableList) {
                String imageUrl = domainMedia + "/api/chemistry/periodic/element_" + periodicTable.getNumber() + ".jpg";
                periodicTable.setImg(imageUrl);
                periodicTableRepository.save(periodicTable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("OK");
    }

}
