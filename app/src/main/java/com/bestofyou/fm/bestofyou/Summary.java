package com.bestofyou.fm.bestofyou;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bestofyou.fm.bestofyou.data.SummaryContract;
import com.bestofyou.fm.bestofyou.data.SummaryHelper;

public class Summary extends AppCompatActivity implements View.OnClickListener{
    SummaryHelper mDbHelper;
    int x;
    private Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        add = (Button)findViewById(R.id.test);
        //mDbHelper =  new SummaryHelper(this);



        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               /* mDbHelper.insert("positive", 123);
                x =mDbHelper.getCountAll();
                Toast.makeText(getBaseContext(), "data"+x, Toast.LENGTH_SHORT).show();*/

                insertRubric("Sports",2 );
                insertRubric("Reading",4 );
                insertRubric("Coding",2 );
                insertRubric("Networking",4 );
                insertRubric("Family Time",4 );
                insertRubric("Early Sleeping",4 );

            }
        });
    }

    public long insertRubric(String description,float weight ){

        ContentValues rubric = new ContentValues();
        rubric.put(SummaryContract.Rubric.NAME, description);
        rubric.put(SummaryContract.Rubric.WEIGHT, weight);
        rubric.put(SummaryContract.Rubric.POPULARITY, 0F);

        Uri insertedUri =  this.getContentResolver().insert(
                SummaryContract.Rubric.CONTENT_URI,
                rubric
        );
        long locationId = ContentUris.parseId(insertedUri);
        return locationId;
    }


    @Override
    public void onClick(View view) {
        if (view == add){
            mDbHelper.insert("positive", 1);
            x =mDbHelper.getCountAll();
            Toast.makeText(this, "data"+x, Toast.LENGTH_SHORT).show();
        }

    }


}