package com.adeasy.advertise.ui.favaourite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.adeasy.advertise.R;

public class divya_MActivity extends AppCompatActivity implements View.OnClickListener{

    int[] IMG = {R.drawable.teddy,R.drawable.teddy,R.drawable.teddy,R.drawable.teddy,R.drawable.teddy,R.drawable.teddy};
    String[] NAMES  ={"a", "b", "c", "d", "e", "f"};
    String[] DESCRIPTION = {"1", "2", "3","4", "5", "6"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.divya_activity__m);

        ListView ListView=(ListView)findViewById(R.id.ListView);

        CustomerAdapter customerAdapter = new CustomerAdapter();
        ListView.setAdapter(customerAdapter);

        onClick(findViewById(R.id.imageButton));

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, divya_Edit.class);
        startActivity(intent);
    }


    class CustomerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return IMG.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.divya_wishlist,null);

            ImageView imageVIew = view.findViewById(R.id.imageview);
            TextView textview = view.findViewById(R.id.title_textview);
            TextView textdescription = view.findViewById(R.id.description_textview);


            imageVIew.setImageResource(IMG[i]);
            textview.setText(NAMES[i]);
            textdescription.setText(DESCRIPTION[i]);

            return view;
        }
    }

}