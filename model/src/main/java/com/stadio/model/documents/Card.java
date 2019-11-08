package com.stadio.model.documents;

import com.stadio.model.enu.Unit;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "google_card_product")
@Data
public class Card {

    @Id
    private String id;

    @Field(value = "name")
    private String name;

    @Field(value = "price")
    private int price;

    @Field(value = "unit")
    private Unit unit;

    @Field(value = "actice")
    private boolean active;

    @Field(value = "created_date")
    private Date createdDate;

    @Field(value = "updated_date")
    private Date updatedDate;

    public Card() {}

    public Card(String id, String name, int price, boolean active) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.active = active;
        this.unit = Unit.VND;
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }

    public static List<Card> getListCard() {
        List<Card> cardList = new ArrayList<>();

        cardList.add(new Card("a1005", "Thẻ 6k", 6000, true));
        cardList.add(new Card("a1001", "Thẻ 10k", 10000, true));
        cardList.add(new Card("a1002", "Thẻ 20k", 20000, true));
        cardList.add(new Card("a1003", "Thẻ 50k", 50000, true));
        cardList.add(new Card("a1004", "Thẻ 100k", 100000, true));

        return cardList;
    }


}
