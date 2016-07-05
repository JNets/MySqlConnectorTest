package com.jormelcn.coursera.semaa3.mysqlconnectortest;

import android.content.Context;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.Time;
import java.util.TimerTask;

/**
 * Created by jormelcn on 14/06/16.
 */
public class MysqlDataBase implements MysqlAsync.OnConnectionResultListener{

    private static final String SERVER = "elias.colombiahosting.com.co";
    //private static final String SERVER = "sql154120.byetcluster.com";
    private static final int PORT = 3306;
    private static final String DATA_BASE = "insedupa_jormelTest";
    //private static final String DATA_BASE = "n260m_17931114_test";
    private static final String USER = "insedupa_jormel";
    //private static final String USER = "n260m_17931114";
    private static final String PASSWORD = "cer0tain";
    //private static final String PASSWORD = "cer0tain";
    private static final String TABLE_TEST = " test ";
    private static final String TAG_ID = " id ";
    private static final String TAG_NAME = " nombre ";
    private static final String TAG_LAST_NAME = " apellido ";

    private Context context;

    private static MysqlAsync mysqlAsync = null;

    public MysqlDataBase(Context context){
        this.context = context;
        if(mysqlAsync == null) {
            mysqlAsync = new MysqlAsync(SERVER, PORT, DATA_BASE, USER, PASSWORD);
        }
        mysqlAsync.setOnConnectionResultListener(this);
    }

    public void  open(){
        mysqlAsync.open();
    }

    public void  close(){
        mysqlAsync.close();
    }

    public boolean isOnLine(){
        return mysqlAsync.isOpen();
    }

    public void insert(String[] names, String[] lastNames) throws Exception {
        if(names.length != lastNames.length){
            throw new Exception(" Los Arreglos de nombres y Apellidos tienen tamaños diferentes ");
        }

        int n = names.length;
        String sqlQuery = "INSERT INTO" + TABLE_TEST + "(" + TAG_NAME + "," + TAG_LAST_NAME + ") VALUES ";

        for (int i = 0; i < n; i++){
            sqlQuery += "(\"" + names[i] + "\",\"" + lastNames[i] + "\")";
            if( i < n -1){
                sqlQuery += ", ";
            }
        }

        MysqlAsync.OnQueryResultListener onQueryResultListener = new MysqlAsync.OnQueryResultListener() {
            @Override
            public void onQueryResult(int resultIndex, MysqlAsync.QueryResult queryResult) {
                switch (resultIndex) {
                    case 0:
                        Toast.makeText(context, "Se ejecutó en: " + String.valueOf(queryResult.getElapsedTime()) + " ms" , Toast.LENGTH_SHORT).show();
                        if(!queryResult.isSuccess()) {
                            Toast.makeText(context, "Error en la ejecucion de la solicitud", Toast.LENGTH_SHORT).show();
                        }else if (queryResult.getUpdateCount() == 1) {
                            Toast.makeText(context, "Se insertó un Nuevo Registro", Toast.LENGTH_SHORT).show();
                        } else if (queryResult.getUpdateCount() > 1) {
                            Toast.makeText(context, "Se insertaron " + String.valueOf(queryResult.getUpdateCount()) + " Registros", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "No se insertaron registros", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        Toast.makeText(context, "Respuesta Inesperada", Toast.LENGTH_SHORT).show();
                }
            }
        };
        mysqlAsync.executeQuery(sqlQuery, onQueryResultListener);
    }

    public void select(int id){
        String sqlQuery = "SELECT" + TAG_NAME + "," + TAG_LAST_NAME + "FROM" + TABLE_TEST + " WHERE" + TAG_ID + "=" + String.valueOf(id);

        MysqlAsync.OnQueryResultListener onQueryResultListener = new MysqlAsync.OnQueryResultListener() {
            @Override
            public void onQueryResult(int resultIndex, MysqlAsync.QueryResult queryResult) {
                switch (resultIndex) {
                    case 0:
                        Toast.makeText(context, "Se ejecutó en: " + String.valueOf(queryResult.getElapsedTime()) + " ms" , Toast.LENGTH_SHORT).show();
                        if(!queryResult.isSuccess()){
                            Toast.makeText(context, "Error en la ejecucion de la solicitud", Toast.LENGTH_SHORT).show();
                        }
                        else if (queryResult.getResultSet() == null){
                            Toast.makeText(context, "No se obtuvo una respuesta valida", Toast.LENGTH_SHORT).show();
                        }else{
                            try {
                                while (queryResult.getResultSet().next()) {
                                    Toast.makeText(context, "Name: " + queryResult.getResultSet().getString(1), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, "Last Name: " + queryResult.getResultSet().getString(2), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, "Error al Procesar Resultado ", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        Toast.makeText(context, "Respuesta Inesperada", Toast.LENGTH_SHORT).show();
                }
            }
        };
        mysqlAsync.executeQuery(sqlQuery, onQueryResultListener);
    }

    public void update(int id, String name, String lastName){
        String sqlQuery = "UPDATE" + TABLE_TEST + "SET" + TAG_NAME + "=\"" + name + "\"," +
                TAG_LAST_NAME + "=\"" + lastName + "\" WHERE" + TAG_ID + "=" + String.valueOf(id);

        MysqlAsync.OnQueryResultListener onQueryResultListener = new MysqlAsync.OnQueryResultListener() {
            @Override
            public void onQueryResult(int resultIndex, MysqlAsync.QueryResult queryResult) {
                switch (resultIndex) {
                    case 0:
                        Toast.makeText(context, "Se ejecutó en: " + String.valueOf(queryResult.getElapsedTime()) + " ms" , Toast.LENGTH_SHORT).show();
                        if(!queryResult.isSuccess()) {
                            Toast.makeText(context, "Error en la ejecucion de la solicitud", Toast.LENGTH_SHORT).show();
                        }else if (queryResult.getUpdateCount() == 1) {
                            Toast.makeText(context, "Se Actualizó un Registro", Toast.LENGTH_SHORT).show();
                        } else if (queryResult.getUpdateCount() > 1) {
                            Toast.makeText(context, "Se Actualizaron " + String.valueOf(queryResult.getUpdateCount()) + " Registros", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "No se Actualizaron registros", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        Toast.makeText(context, "Respuesta Inesperada", Toast.LENGTH_SHORT).show();
                }
            }
        };
        mysqlAsync.executeQuery(sqlQuery, onQueryResultListener);
    }

    public void delete(int id){
        String sqlQuery = "DELETE FROM" + TABLE_TEST + "WHERE" + TAG_ID + "=" + String.valueOf(id);

        MysqlAsync.OnQueryResultListener onQueryResultListener = new MysqlAsync.OnQueryResultListener() {
            @Override
            public void onQueryResult(int resultIndex, MysqlAsync.QueryResult queryResult) {
                switch (resultIndex) {
                    case 0:
                        Toast.makeText(context, "Se ejecutó en: " + String.valueOf(queryResult.getElapsedTime()) + " ms" , Toast.LENGTH_SHORT).show();
                        if(!queryResult.isSuccess()) {
                            Toast.makeText(context, "Error en la ejecucion de la solicitud", Toast.LENGTH_SHORT).show();
                        }else if (queryResult.getUpdateCount() == 1) {
                            Toast.makeText(context, "Se Elimino un Registro", Toast.LENGTH_SHORT).show();
                        } else if (queryResult.getUpdateCount() > 1) {
                            Toast.makeText(context, "Se Eliminaron " + String.valueOf(queryResult.getUpdateCount()) + " Registros", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "No se Eliminaron registros", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        Toast.makeText(context, "Respuesta Inesperada", Toast.LENGTH_SHORT).show();
                }
            }
        };
        mysqlAsync.executeQuery(sqlQuery, onQueryResultListener);
    }

    public void test(){
        mysqlAsync.executeTest();
    }

    @Override
    public void onOpenResult(boolean result) {
        if(result) {
            Toast.makeText(context, "Conexion MySQL Abierta", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "No se pudo Abrir Conexion MySQL", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCloseResult(boolean result) {
        if(result) {
            Toast.makeText(context, "Conexion MySQL Cerrada", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "No se pudo Cerrar Conexion MySQL", Toast.LENGTH_SHORT).show();
        }
    }
}
