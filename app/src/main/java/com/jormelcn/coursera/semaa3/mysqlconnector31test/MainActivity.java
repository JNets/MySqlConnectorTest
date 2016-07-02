package com.jormelcn.coursera.semaa3.mysqlconnector31test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jormelcn.coursera.semaa3.mysqlconnectortest.MysqlDataBase;

public class MainActivity extends AppCompatActivity {
    MysqlDataBase mysqlDataBase;
    EditText id;
    EditText name;
    EditText lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mysqlDataBase = new MysqlDataBase(this);
        id = (EditText) findViewById(R.id.id);
        name = (EditText) findViewById(R.id.name);
        lastName = (EditText) findViewById(R.id.lastName);
    }

    public void openDataBase(View view){
        if(mysqlDataBase.isOnLine()){
            Toast.makeText(this, "La Base de Datos ya está Abierta", Toast.LENGTH_SHORT).show();
        }else{
            mysqlDataBase.open();
        }

    }

    public void closeDataBase(View view){
        if(mysqlDataBase.isOnLine()) {
            mysqlDataBase.close();
        }else {
            Toast.makeText(this, "La Base de Datos ya está Cerrada", Toast.LENGTH_SHORT).show();
        }
    }

    public void insertOnDataBase(View view){
        if(mysqlDataBase.isOnLine()){
            try {
                mysqlDataBase.insert(new String[]{name.getText().toString()}, new String[]{lastName.getText().toString()});
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this, "La Base de Datos está cerrada", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateOnDataBase(View view){
        if(mysqlDataBase.isOnLine()){
            try {
                mysqlDataBase.update(Integer.parseInt(id.getText().toString()), name.getText().toString(), lastName.getText().toString());
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Ingrese un id valido", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "La Base de Datos está cerrada ", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOnDataBase(View view){
        if(mysqlDataBase.isOnLine()){
            try {
                mysqlDataBase.delete(Integer.parseInt(id.getText().toString()));
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Ingrese un id valido", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "La Base de Datos está cerrada", Toast.LENGTH_SHORT).show();
        }
    }

    public void selectOnDataBase(View view){
        if(mysqlDataBase.isOnLine()){
            try {
                mysqlDataBase.select(Integer.parseInt(id.getText().toString()));
            }catch (NumberFormatException e){
                e.printStackTrace();
                Toast.makeText(this, "Ingrese un id valido", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "La Base de Datos está cerrada", Toast.LENGTH_SHORT).show();
        }
    }
}
