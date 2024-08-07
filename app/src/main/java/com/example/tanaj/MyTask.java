package com.example.tanaj;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class MyTask extends AsyncTask<String, String, String>
{
    private ActivitySearch activity;
    private int val;
    private ProgressDialog dialog;


    public MyTask(ActivitySearch a, int val)
    {
        this.activity = a;
        this.val = val;
        this.dialog = new ProgressDialog(a);
    }

    protected void onPreExecute() {
        this.dialog.setMessage("Cargando");
        this.dialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        activity.getInfoStrong(val);
        return null;
    }

    protected void onPostExecute(String result)
    {
        activity.setRecycler();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
