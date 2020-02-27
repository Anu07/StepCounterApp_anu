package com.sd.src.stepcounterapp.interfaces.interfaces;



public class InterfacesCall {

    public interface LocationInterface {
        void onresume();

        void onpause();
    }

    public  interface IndexClick{
        void clickIndex(int pos);
    }
    public  interface Callback{
        void selected(int pos);
    }
    
    public  interface ItemCategoryClick{
        void clickItem(int pos);
        void clickCategory(int pos);
    }
}
