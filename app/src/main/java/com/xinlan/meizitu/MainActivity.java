package com.xinlan.meizitu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.xinlan.meizitu.task.FindRootNodeTask;

public class MainActivity extends AppCompatActivity {
    //
    private ImageView img;
    private FindRootNodeTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        img = (ImageView) findViewById(R.id.img);

        mTask = new FindRootNodeTask();
        mTask.execute(Constant.MEI_URL);
        //mTask.execute("http://www.mzitu.com/page/149");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTask != null) {
            mTask.cancel(true);
        }
    }
}//end class
