package com.stadio.model.documents.chemistry;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "chemistry_periodic_table")
@Data
public class PeriodicTable {

    @Id
    //@JsonProperty(value = "_id")
    private String id;

    //@JsonProperty(value = "es_umvospr")
    @Field(value = "es_umvospr")
    private String esUmvospr; //

    //@JsonProperty(value = "at_elect")
    @Field(value = "at_elect")
    private String atElEct;

    //@JsonProperty(value = "symbol")
    @Field(value = "symbol")
    private String symbol;

    //@JsonProperty(value = "ys_per")
    @Field(value = "ys_per")
    private String ysPer;

    //@JsonProperty(value = "year")
    @Field(value = "year")
    private String year;

    //@JsonProperty(value = "wiki")
    @Field(value = "wiki")
    private String wiki;

    //@JsonProperty(value = "ys_rad")
    @Field(value = "ys_rad")
    private String ysRad;

    //@JsonProperty(value = "type")
    @Field(value = "type")
    private String type;

    //@JsonProperty(value = "rs2")
    @Field(value = "rs2")
    private String rs2;

    //@JsonProperty(value = "number")
    @Field(value = "number")
    private String number;

    //@JsonProperty(value = "ts_mti")
    @Field(value = "ts_mti")
    private String tsMti;

    //@JsonProperty(value = "rs4")
    @Field(value = "rs4")
    private String rs4;

    //@JsonProperty(value = "name_eng")
    @Field(value = "name_eng")
    private String nameEng;

    //@JsonProperty(value = "at_kov")
    @Field(value = "at_kov")
    private String atKov;

    //@JsonProperty(value = "sm_uprug")
    @Field(value = "sm_uprug")
    private String smUprUg;

    //@JsonProperty(value = "sm_ynga")
    @Field(value = "sm_ynga")
    private String smYnga;

    //@JsonProperty(value = "ts_ktp")
    @Field(value = "ts_ktp")
    private String tsKtp;

    //@JsonProperty(value = "ts_mtp")
    @Field(value = "ts_mtp")
    private String tsMtp;

    //@JsonProperty(value = "at_rad")
    @Field(value = "at_rad")
    private String atRad;

    //@JsonProperty(value = "sm_moss")
    @Field(value = "sm_moss")
    private String smMoss;

    //@JsonProperty(value = "group")
    @Field(value = "group")
    private String group;

    //@JsonProperty(value = "ts_sos")
    @Field(value = "ts_sos")
    private String tsSoS;

    //@JsonProperty(value = "es_mmvospr")
    @Field(value = "es_mmvospr")
    private String esMMvospr;

    //@JsonProperty(value = "rs_val")
    @Field(value = "rs_val")
    private String rsVal;

    //@JsonProperty(value = "el_conf")
    @Field(value = "el_conf")
    private String elConf;

    //@JsonProperty(value = "cas_number")
    @Field(value = "cas_number")
    private String casNumber;

    //@JsonProperty(value = "name_latin")
    @Field(value = "name_latin")
    private String nameLatin;

    //@JsonProperty(value = "massa")
    @Field(value = "massa")
    private String massa;

    //@JsonProperty(value = "es_mtype")
    @Field(value = "es_mtype")
    private String esMtype;

    //@JsonProperty(value = "img")
    @Field(value = "img")
    private String img;

    //@JsonProperty(value = "color")
    @Field(value = "color")
    private String color;

    //@JsonProperty(value = "raspr6")
    @Field(value = "raspr6")
    private String raspr6;

    //@JsonProperty(value = "es_temp")
    @Field(value = "es_temp")
    private String esTemp;

    //@JsonProperty(value = "at_step")
    @Field(value = "at_step")
    private String atStep;

    //@JsonProperty(value = "ts_ut")
    @Field(value = "ts_ut")
    private String tsUt;

    //@JsonProperty(value = "temp2")
    @Field(value = "temp2")
    private String temp2;

    //@JsonProperty(value = "temp1")
    @Field(value = "temp1")
    private String temp1;

    //@JsonProperty(value = "sm_color")
    @Field(value = "sm_color")
    private String smColor;

    //@JsonProperty(value = "at_rad2")
    @Field(value = "at_rad2")
    private String atRad2;

    //@JsonProperty(value = "raspr3")
    @Field(value = "raspr3")
    private String raspr3;

    //@JsonProperty(value = "raspr2")
    @Field(value = "raspr2")
    private String raspr2;

    //@JsonProperty(value = "raspr5")
    @Field(value = "raspr5")
    private String raspr5;

    //@JsonProperty(value = "raspr4")
    @Field(value = "raspr4")
    private String raspr4;

    //@JsonProperty(value = "sm_obem")
    @Field(value = "sm_obem")
    private String smObem;

    //@JsonProperty(value = "raspr1")
    @Field(value = "raspr1")
    private String raspr1;

    //@JsonProperty(value = "es_electro")
    @Field(value = "es_electro")
    private String esElEctro;

    //@JsonProperty(value = "sm_teplo")
    @Field(value = "sm_teplo")
    private String smTeplo;

    //@JsonProperty(value = "es_omvospr")
    @Field(value = "es_omvospr")
    private String esOmvOspr;

    //@JsonProperty(value = "es_udel")
    @Field(value = "es_udel")
    private String esUdel;

    //@JsonProperty(value = "density")
    @Field(value = "density")
    private String density;

    //@JsonProperty(value = "ys_vrem")
    @Field(value = "ys_vrem")
    private String ysVrem;

    //@JsonProperty(value = "rs_eng")
    @Field(value = "rs_eng")
    private String rsEng;

    //@JsonProperty(value = "ys_yader")
    @Field(value = "ys_yader")
    private String ysYader;

    //@JsonProperty(value = "sm_plot2")
    @Field(value = "sm_plot2")
    private String smPlot2;

    //@JsonProperty(value = "es_etype")
    @Field(value = "es_etype")
    private String esEtypE;

    //@JsonProperty(value = "sm_plot1")
    @Field(value = "sm_plot1")
    private String smPlot1;

    //@JsonProperty(value = "sm_prelom")
    @Field(value = "sm_prelom")
    private String smPrelom;

    //@JsonProperty(value = "rs_electro")
    @Field(value = "rs_electro")
    private String rsElEctro;

    //@JsonProperty(value = "sm_brin")
    @Field(value = "sm_brin")
    private String smBrin;

    //@JsonProperty(value = "sm_zvuk")
    @Field(value = "sm_zvuk")
    private String smZvuk;

    //@JsonProperty(value = "el_obolochka")
    @Field(value = "el_obolochka")
    private String elObOlOchka;

    //@JsonProperty(value = "name_in")
    @Field(value = "name_in")
    private String nameIn;

    //@JsonProperty(value = "category")
    @Field(value = "category")
    private String category;

    //@JsonProperty(value = "name_it")
    @Field(value = "name_it")
    private String nameIt;

    //@JsonProperty(value = "open")
    @Field(value = "open")
    private String open;

}
