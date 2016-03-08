package com.example.janiszhang.apdatacollector;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mWifiData;
    private WifiManager mWifiManager;
    private String mString;
    private MyDatabaseHelper mDbHelper;
    private SQLiteDatabase mAPDb;
    private Button mCollectButton;
    private Button mWriteButton;
    private ListView mListView;
    private EditText mApX;
    private EditText mApY;
    private EditText mCount;
    private TextView mMark;
    private List<APData> mApDataList;
    private MyAdapter mMyAdapter;
    private SharedPreferences mXysp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new MyDatabaseHelper(this, "APData.db", null, 1);
        mAPDb = mDbHelper.getWritableDatabase();
        mApDataList = new ArrayList<>();

        mWifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if(!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }

        mListView = (ListView) findViewById(R.id.lv_ap_data);
        mApX = (EditText) findViewById(R.id.ap_x);
        mApY = (EditText) findViewById(R.id.ap_y);
        mCount = (EditText) findViewById(R.id.collector_count);
        mMark = (TextView) findViewById(R.id.mark);
        mXysp = getSharedPreferences("xy", Activity.MODE_PRIVATE);
        mMark.setText("已完成坐标(" + String.valueOf(mXysp.getInt("x", 0)) + "," + String.valueOf(mXysp.getInt("y", 0)) + ")数据采集");


        mCollectButton = (Button) findViewById(R.id.bt_collect);
        mCollectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mApX.getText()) || TextUtils.isEmpty(mApY.getText()) || TextUtils.isEmpty(mCount.getText())) {
                    Toast.makeText(MainActivity.this, "请输入正确的参数", Toast.LENGTH_SHORT).show();
                } else {
                    List<ScanResult> scanResults = mWifiManager.getScanResults();
                    for (int i = 0; i< scanResults.size();i++) {
                        ScanResult temp = scanResults.get(i);
                        mApDataList.add(new APData(temp.BSSID,temp.level,temp.SSID));
                    }
                    mMyAdapter = new MyAdapter(MainActivity.this, R.layout.itemlayout, mApDataList);
                    mListView.setAdapter(mMyAdapter);
                }
            }
        });

        mWriteButton = (Button) findViewById(R.id.bt_write);
        mWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //记录
                mAPDb.beginTransaction();
                try {
                    //写入数据库
                    if(Integer.valueOf(mCount.getText().toString()) == 1) {//insert
                        for (int i = 0; i < mApDataList.size(); i++) {
                            APData temp = mApDataList.get(i);
                            ContentValues contentValues = new ContentValues();
//                            public static final String CREATE_TABLE = "create table apdate(" +
//                                    "id integer primary key autoincrement, " +
//                                    "ap_x integer, " +
//                                    "ap_y integer, " +
//                                    "ap_macaddr text, " +
//                                    "ap_level1 integer," +
//                                    "ap_level2 integer," +
//                                    "ap_level3 integer," +
//                                    "ap_name text)";
                            contentValues.put("ap_x", Integer.valueOf(mApX.getText().toString()));
                            contentValues.put("ap_y", Integer.valueOf(mApY.getText().toString()));
                            contentValues.put("ap_macaddr",temp.getMacAddr());
                            contentValues.put("ap_level1", temp.getLevel());
                            contentValues.put("ap_level2", 0);
                            contentValues.put("ap_level3", 0);
                            contentValues.put("ap_name", temp.getName());
                            mAPDb.insert("apdata", null, contentValues);
                        }
                    } else {//update
                        for(int i =0; i < mApDataList.size(); i++) {
                            APData temp = mApDataList.get(i);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("ap_level" + mCount.getText().toString(),temp.getLevel());
                            mAPDb.update("apdata", contentValues, "ap_x=? and ap_y=? and ap_macaddr=?", new String[]{mApX.getText().toString(),mApY.getText().toString(),temp.getMacAddr()});
                        }
                    }

                    mAPDb.setTransactionSuccessful();//事务已经执行成功
                    //数据写入成功后在执行下面的内容
                    mMyAdapter.clear();
                    mMyAdapter.notifyDataSetChanged();

                    mMark.setText("已完成坐标(" + mApX.getText().toString() + "," + mApY.getText().toString() + ")数据采集");
                    SharedPreferences.Editor edit = mXysp.edit();
                    edit.putInt("x", Integer.parseInt(mApX.getText().toString()));
                    edit.putInt("y", Integer.parseInt(mApY.getText().toString()));
                    edit.apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    mAPDb.endTransaction();//结束事务
                }

            }
        });
    }
}
