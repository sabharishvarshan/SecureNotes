package com.sabharish.securenotes.card;
public class Card {

    private String NAME;
    private String CVV;

    private String CARDNO;
    private String EXPDATE;

    public Card(){}
    public Card(String NAME, String CVV, String CARDNO, String EXPDATE) {
        this.NAME = NAME;
        this.CVV = CVV;
        this.CARDNO = CARDNO;
        this.EXPDATE = EXPDATE;

    }

    public  String getNAME() {
        return NAME;
    }
    public String getCARDNO(){ return CARDNO; }
    public String getCVV() {
        return CVV;
    }
    public String getEXPDATE() { return EXPDATE; }

    public void setEXPDATE(String EXPDATE) {
        this.EXPDATE = EXPDATE;
    }
    public void setCVV(String CVV) { this.CVV = CVV; }
    public void setCARDNO(String CARDNO) { this.CARDNO = CARDNO; }
    public void setNAME(String NAME) {
        this.NAME = NAME;
    }


}