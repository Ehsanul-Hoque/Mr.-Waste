package io.pantheonsite.alphaoptimus369.mrwaste.commons.models;


public class UserItem
{

    public String email, contactNo, userType, addressLine;
    public double latitude, longitude;

    public UserItem(String email, String contactNo, String userType, String addressLine,
                    double latitude, double longitude)
    {
        this.email = email;
        this.contactNo = contactNo;
        this.userType = userType;
        this.addressLine = addressLine;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
