package inf8405.tp2.letsmeet;

// INF8405 - Laboratoire 2
//Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)

public class Adresse {
    private Integer streetNumber_=0;
    private String streetName_=new String();
    private String city_=new String();
    private String province_=new String() ;
    private String country_=new String();
    private String postalCode_=new String();

    public Adresse()
    {

    }

    public Adresse (Integer streetNum, String streetName, String city, String province, String country, String postalCode )
    {
        streetNumber_=streetNum;
        streetName_=streetName;
        city_=city;
        province_=province;
        country_=country;
        postalCode_=postalCode;
    }

    public Integer getStreetNumber() {
        return streetNumber_;
    }

    public void setStreetNumber(Integer streetNumber) {
        this.streetNumber_ = streetNumber;
    }

    public String getStreetName() {
        return streetName_;
    }

    public void setStreetName(String streetName) {
        this.streetName_ = streetName;
    }

    public String getCity() {
        return city_;
    }

    public void setCity_(String city) {
        this.city_ = city;
    }

    public String getProvince_() {
        return province_;
    }

    public void setProvince_(String province) {
        this.province_ = province;
    }

    public String getCountry_() {
        return country_;
    }

    public void setCountry_(String country) {
        this.country_ = country;
    }

    public String getPostalCode_() {
        return postalCode_;
    }

    public void setPostalCode_(String postalCode) {
        this.postalCode_ = postalCode;
    }

    public String to_String()
    {
        return streetNumber_.toString() + " " + streetName_ + ", " + city_ + ", " + province_ + ", " + country_ + " " + postalCode_;
    }
}
