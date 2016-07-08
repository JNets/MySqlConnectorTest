package com.mysql.jdbc;

import android.os.AsyncTask;

import java.sql.*;

/**
 * Created by jormelcn on 7/07/16.
 */
public class RealTimeMysqlReader<O> {
    private PreparedStatement statement = null;
    private Listener<O> listener = null;
    private ResultSetAdapter<O> resultSetAdapter = null;
    private RealTimeTask realTimeTask = null;

    public interface Listener <Q>{
        void onResult(Q result);
    }

    public interface ResultSetAdapter<Q>{
        Q parse(java.sql.ResultSet resultSet);
    }

    public class RealTimeTask extends AsyncTask<Void,Object,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            try {
                statement.setResultSetAdapter(resultSetAdapter);
                statement.setRealTimeTask(this);
                statement.execute();
            }catch (Exception e){
                e.printStackTrace();
            }
            //statement.setRealTimeTask(null);
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            listener.onResult((O) values[0]);
        }

        public void showResults(Object result){
            publishProgress(result);
        }
    }

    public RealTimeMysqlReader(PreparedStatement statement){
        this.statement = statement;
    }

    public void setListener(Listener<O> listener) {
        this.listener = listener;
    }

    public void setResultSetAdapter(ResultSetAdapter<O> resultSetAdapter){
        this.resultSetAdapter = resultSetAdapter;
    }

    public void run(){
        realTimeTask = new RealTimeTask();
        realTimeTask.execute();
    }
    public  void pause(){
        realTimeTask.cancel(false);
    }
}
