package com.coderdeepayan.hospital.medibook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class HospitalDatabase extends SQLiteOpenHelper {
    public HospitalDatabase(@NonNull Context context) {
        super(context, "hospital", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists patient(crno text unique,name text, umid text);");
        db.execSQL("create table if not exists password(credential text unique);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void logout(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from patient;");
        sqLiteDatabase.execSQL("delete from password;");
    }

    public void savePatients(List<Patient> patientList){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        for (Patient p:patientList) {
            sqLiteDatabase.execSQL(
                    "insert into patient values('"+p.getCrNo()+"','"+p.getName()+"','"+p.getUmid_id()+"');");
        }
        sqLiteDatabase.close();
    }
    public void savePassword(String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("insert into password values('"+password+"');");
        sqLiteDatabase.close();
    }
    public List<Patient> getPatients(){
        List<Patient> patientList = new ArrayList<>();
        Cursor cursor = this.getReadableDatabase().rawQuery("select * from patient;",null);

        while (cursor.moveToNext()){
            patientList.add(new Patient(cursor.getString(1),
                    cursor.getString(0),
                    cursor.getString(2)));
        }
        cursor.close();
        return patientList;
    }

    public boolean matchPassword(String password){
        Cursor cursor = this.getReadableDatabase().rawQuery("select * from password;",null);
        cursor.moveToNext();
        boolean b = cursor.getString(0).trim().equalsIgnoreCase(password.trim());
        cursor.close();
        return b;
    }

    public boolean hasUserDetails(){
        Cursor cursor = this.getReadableDatabase().rawQuery("select * from password;",null);
        boolean b = cursor.moveToNext();
        cursor.close();
        return b;
    }



}
