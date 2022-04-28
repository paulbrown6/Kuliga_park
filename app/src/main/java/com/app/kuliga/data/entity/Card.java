package com.app.kuliga.data.entity;

import android.util.Log;

import com.app.kuliga.data.other.Converter;
import com.app.kuliga.data.api.entity.ResponseEntity;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Card extends SugarRecord<Card> {

    private static String TAG = "Card";

    private String category;
    private String code;
    private String cardId;
    private String name;
    private String cardType;
    private String tariffType;
    private String validFrom;
    private String validTo;
    private String balance;

    public Card(){}

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getTariffType() {
        return tariffType;
    }

    public void setTariffType(String tariffType) {
        this.tariffType = tariffType;
    }

    public Date getValidFrom() {
        if (validFrom != null)
            return Converter.stringToDate(validFrom.substring(0, 4), validFrom.substring(4, 6), validFrom.substring(6, 8));
        else
            return new Date();
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        if (validTo != null)
            return Converter.stringToDate(validTo.substring(0, 4), validTo.substring(4, 6), validTo.substring(6, 8));
        else
            return new Date();
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public static List<Card> createListFromXML(ResponseEntity response){
        List<ResponseEntity.Value> values = response.getaPacket().Value;
        if (values != null && values.size() > 1) {
            try {
                values = response.getaPacket().Value.get(1).Array.Value;
            } catch (Exception e) {
                Log.e(TAG, "setCardsFromXML: " + e.getMessage());
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
        List<Card> cards = new ArrayList<>();
        for (ResponseEntity.Value value: values) {
            String category = null, code = null, id = null, name = null,
                    cardType = null, tariffType = null, validFrom = null, validTo = null;
            try {
                category = value.Structure.getValue("category").optional.string;
                code = value.Structure.getValue("code").string;
                id = value.Structure.getValue("id").optional._int + "";
                if (value.Structure.getValue("name").optional != null) {
                    name = value.Structure.getValue("name").optional.string;
                } else {
                    name = "none";
                }
                cardType = value.Structure.getValue("permanent_rule").optional.string;
                tariffType = value.Structure.getValue("rate").optional.string;
                validFrom = value.Structure.getValue("valid_from").Time.string;
                validTo = value.Structure.getValue("valid_to").Time.string;
            } catch (Exception e){
                Log.e(TAG, "createFromXML: " + e.getMessage());
            }
            Card card = new Card();
            if (category != null && !category.equals("")) card.setCategory(category);
            if (code != null && !code.equals("")) card.setCode(code);
            if (id != null && !id.equals("")) card.setCardId(id);
            if (name != null && !name.equals("")) card.setName(name);
            if (cardType != null && !cardType.equals("")) card.setCardType(cardType);
            if (tariffType != null && !tariffType.equals("")) card.setTariffType(tariffType);
            if (validFrom != null && !validFrom.equals("")) card.setValidFrom(validFrom);
            if (validTo != null && !validTo.equals("")) card.setValidTo(validTo);
            cards.add(card);
        }
        return cards;
    }

    public void setBalanceFromXML(ResponseEntity response){
        List<ResponseEntity.Value> values = response.getaPacket().Value;
        if (values != null && values.size() > 1) {
            try {
                values = response.getaPacket().Value.get(1).Array.Value;
            } catch (Exception e) {
                Log.e(TAG, "setTariffFromXML: " + e.getMessage());
                return;
            }
        } else {
            return;
        }
        String balance = null;
        String currency = null;
        try {
            balance = values.get(0).Structure.getValue("balance").decimal.string;
            currency = values.get(0).Structure.getValue("currency").string;
        } catch (Exception e){
            Log.e(TAG, "addBalanceFromXML: " + e.getMessage());
        }
        if (balance != null && !balance.equals("")) {
            this.setBalance(balance);
            if (currency != null && !currency.equals("")) this.setBalance(balance + " " + currency);
        }
    }
}
