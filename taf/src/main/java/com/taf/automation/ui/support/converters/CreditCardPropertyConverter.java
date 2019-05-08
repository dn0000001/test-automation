package com.taf.automation.ui.support.converters;

import org.apache.commons.beanutils.Converter;

public class CreditCardPropertyConverter implements Converter {
    @Override
    public Object convert(Class aClass, Object obj) {
        if (!(obj instanceof String)) {
            return null;
        }

        String[] cards = ((String) obj).split(",");
        CreditCard[] creditCards = new CreditCard[cards.length];
        for (int i = 0; i < cards.length; i++) {
            creditCards[i] = new CreditCard(cards[i]);
        }

        return creditCards;
    }

}
