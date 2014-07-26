package com.ulop.dictionary;

/**
 * Created by ulop on 26.07.14.
 */
public class ChuvsuDictionary {
    public class PhoneItem{
        int id;
        String title;
        String phoneNumber;

        public PhoneItem(String title, String phoneNumber) {
            this.title = title;
            this.phoneNumber = phoneNumber;
        }
    }

    public class Address{
        int id;
        String title;
        String address;
        String image;

        public Address(String title, String address, String image) {
            this.title = title;
            this.address = address;
            this.image = image;
        }
    }
}
