package com.example.djikon;

public class ServiceImage_Model {
    private  int Service_Gallery_Image;
    private  String Service_Iage_Name;

    public ServiceImage_Model(int service_Gallery_Image, String service_Iage_Name) {
        Service_Gallery_Image = service_Gallery_Image;
        Service_Iage_Name = service_Iage_Name;
    }

    public int getService_Gallery_Image() {
        return Service_Gallery_Image;
    }

    public String getService_Image_Name() {
        return Service_Iage_Name;
    }
}
