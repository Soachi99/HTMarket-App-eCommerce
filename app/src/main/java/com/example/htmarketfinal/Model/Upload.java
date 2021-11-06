package com.example.htmarketfinal.Model;

public class Upload {

    // DATOS DE IMAGENES
    private String mName, mImageUrl;

   public Upload()
   {

   }

   public Upload(String name, String imageURL)
   {
       if(name.trim().equals(""))
       {
           name = "No name";
       }

       mName = name;
       mImageUrl = imageURL;
   }

    public String getmName() {
        return mName;
    }

    public void setmName(String name) {mName = name;  }

    public String getmImageUrl() {return mImageUrl;}

    public void setmImageUrl(String imageUrl) {mImageUrl = imageUrl;}

}
