package com.jormelcn.coursera.semaa3.mysqlconnectortest;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.util.Properties;


/**
 * Created by jormelcn on 14/06/16.
 */
public class MysqlAsync {


    public static final boolean QUERY_SUCCESS = true;
    public static final boolean QUERY_FAILED = false;
    public static final boolean OPEN_SUCCESS = true;
    public static final boolean OPEN_FAILED = false;
    public static final boolean CLOSE_SUCCESS = true;
    public static final boolean CLOSE_FAILED = false;

    private String url;
    private String user;
    private String password;

    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement testStatement = null;

    private static final int IDLE = 0;
    private static final int ON_QUERY = 1;
    private static final int ON_OPEN = 2;
    private static final int ON_CLOSE = 3;
    private static final int RESULT_READY = 4;

    private int status = IDLE;
    private long startTime;

    public interface OnQueryResultListener{
        void onQueryResult(int resultIndex, QueryResult queryResult);
    }

    public interface OnConnectionResultListener{
        void onOpenResult(boolean result);
        void onCloseResult(boolean result);
    }
    private OnConnectionResultListener onConnectionResultListener = null;

    static class QueryResult{
        private  boolean success;
        private long elapsedTime;
        private int updateCount;
        private ResultSet resultSet;


        public QueryResult(boolean success, long elapsedTime, int updateCount, ResultSet resultSet){
            this.success = success;
            this.elapsedTime = elapsedTime;
            this.updateCount = updateCount;
            this.resultSet = resultSet;
        }

        public boolean isSuccess() {
            return success;
        }

        public int getUpdateCount() {
            return updateCount;
        }

        public ResultSet getResultSet() {
            return resultSet;
        }

        public long getElapsedTime() {
            return elapsedTime;
        }
    }
    private  QueryResult queryResult = null;


    public MysqlAsync(String server, int port, String dataBase, String user, String password){
        url = "jdbc:mysql://" + server + ":" + String.valueOf(port);
        if(dataBase != null){
            url += "/" + dataBase;
        }
        this.user = user;
        this.password = password;
    }

    public void open(){
        class Open extends AsyncTask<Void,Void,Boolean> {
            @Override
            protected void onPreExecute() {
                status = ON_OPEN;
            }
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    connection = DriverManager.getConnection(url, user, password);
                    statement = connection.createStatement();
                    testStatement = connection.prepareStatement("CALL test_procedure()");
                    return true;
                }catch (Exception e){
                    connection = null;
                    statement = null;
                    e.printStackTrace();
                }
                return false;
            }
            @Override
            protected void onPostExecute(Boolean openResult) {
                status = IDLE;
                if(onConnectionResultListener != null) {
                    onConnectionResultListener.onOpenResult(openResult);
                }
            }
        }
        new Open().execute();
    }

    public void close(){
        class Close extends AsyncTask<Void,Void,Boolean> {
            @Override
            protected void onPreExecute() {
                status = ON_CLOSE;
            }
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    statement.close();
                    connection.close();
                    statement = null;
                    connection = null;
                    return true;
                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }
            @Override
            protected void onPostExecute(Boolean closeResult) {
                status = IDLE;
                if(onConnectionResultListener != null) {
                    onConnectionResultListener.onCloseResult(closeResult);
                }
            }
        }
        new Close().execute();
    }

    public boolean isOpen(){
        return statement != null && connection != null;
    }

    public void executeTest(){
        class ExecuteTest extends AsyncTask<Void,Void,Void> {

            @Override
            protected Void doInBackground(Void... params) {
                try{
                    testStatement.execute();
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }
        new ExecuteTest().execute();
    }

    public void executeQuery(String sqlQuery, final OnQueryResultListener onQueryResultListener){
        class ExecuteQuery extends AsyncTask<String,Void,Boolean> {
            @Override
            protected void onPreExecute() {
                startTime = System.currentTimeMillis();
                status = ON_QUERY;
            }
            @Override
            protected Boolean doInBackground(String... sqlQuery) {
                try {
                    Log.i("MYSQL", sqlQuery[0]);
                    statement.execute(sqlQuery[0]);
                    return true;
                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }
            @Override
            protected void onPostExecute(Boolean querySuccess) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (querySuccess) {
                    try {
                        int resultIndex = 0;
                        boolean moreResults;
                        int updateCount = statement.getUpdateCount();
                        do {
                            Log.i("MYSQL", "updateCount = " + String.valueOf(updateCount));

                            ResultSet resultSet = statement.getResultSet();

                            if (resultSet != null)
                                Log.i("MYSQL", "resultSet = " + resultSet.toString());
                            else
                                Log.i("MYSQL", "resultSet = null");

                            queryResult = new QueryResult(true, elapsedTime, updateCount, resultSet);
                            status = RESULT_READY;

                            if(onQueryResultListener != null) {
                                onQueryResultListener.onQueryResult(resultIndex, getQueryResult());
                            }
                            resultIndex++;

                            moreResults = statement.getMoreResults();
                            Log.i("MYSQL", "moreResult = " + String.valueOf(moreResults));
                            updateCount = statement.getUpdateCount();

                        } while (moreResults || updateCount != -1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    queryResult = new QueryResult(false, elapsedTime, -1, null);
                    status = RESULT_READY;
                    if(onQueryResultListener != null) {
                        onQueryResultListener.onQueryResult(0, getQueryResult());
                    }
                }
            }
        }
        new ExecuteQuery().execute(sqlQuery);
    }

    public QueryResult getQueryResult() {
        if(status == RESULT_READY) {
            status = IDLE;
            return queryResult;
        }else{
            return null;
        }
    }

    public void executeQuery(String sqlQuery) {
        executeQuery(sqlQuery, null);
    }

    public void setOnConnectionResultListener(OnConnectionResultListener onConnectionResultListener){
        this.onConnectionResultListener = onConnectionResultListener;
    }
}
