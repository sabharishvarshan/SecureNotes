package com.sabharish.securenotes.model;

import com.sabharish.securenotes.note.AESEncryption;

public class Note {

    private String title;
    private String content;
    private final String password = "password";


    public Note(){}
    public Note(String title,String content){

        this.title=title;
        this.content = content;

    }

    public String getTitle() throws Exception {

       return AESEncryption.decrypt(password, title);
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getContent() throws Exception{
        return AESEncryption.decrypt(password, content);
    }

    public void setContent(String content) {
        this.content = content;
    }

}