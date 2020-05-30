package io.pantheonsite.alphaoptimus369.mrwaste.home_module.models;


public class WasteRequestConsumerItem
{

    public long id;
    public String email;
    public String contactNo;
    public String addressLine;


    public WasteRequestConsumerItem(long id, String email, String contactNo, String addressLine)
    {
        this.id = id;
        this.email = email;
        this.contactNo = contactNo;
        this.addressLine = addressLine;
    }

}
