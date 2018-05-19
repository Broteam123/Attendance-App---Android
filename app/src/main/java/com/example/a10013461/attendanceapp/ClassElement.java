package com.example.a10013461.attendanceapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Iman Din on 4/23/2018.
 */

public class ClassElement implements Parcelable{

    String block;
    String className;

    ArrayList<String> people;

    boolean done;

    public ClassElement(String block, String className){
        this.block=block;
        this.className=className;
        this.people=new ArrayList<>();
        done=false;
    }

    protected ClassElement(Parcel in) {
        block = in.readString();
        className = in.readString();
        people = in.createStringArrayList();
    }

    public static final Creator<ClassElement> CREATOR = new Creator<ClassElement>() {
        @Override
        public ClassElement createFromParcel(Parcel in) {
            return new ClassElement(in);
        }

        @Override
        public ClassElement[] newArray(int size) {
            return new ClassElement[size];
        }
    };

    public String getBlock(){
        return block;
    }

    public ArrayList<String> getPeople(){
        return people;
    }

    public String getClassName(){
        return className;
    }

    public boolean getDone(){
        return done;
    }

    public void setBlock(String str){
        block = str;
    }

    public void setClassName(String str){
        className = str;
    }

    public void setPeople(ArrayList<String> list){
        people = list;
    }

    public void setDone(boolean bool){
        done = bool;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(block);
        parcel.writeString(className);
        parcel.writeStringList(people);
    }
}
