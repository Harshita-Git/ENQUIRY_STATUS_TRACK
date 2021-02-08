package com.example.ets;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ViewEnq extends AppCompatActivity {
    private ArrayList<ClassListItems> itemArrayList;
    private MyAppAdapter myAppAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean success = false;
    private ConnectionHelper connectionClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_enq);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView); //Recylcerview Declaration
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        connectionClass = new ConnectionHelper(); // Connection Class Initialization
        itemArrayList = new ArrayList<ClassListItems>(); // Arraylist Initialization
        // Calling Async Task
        ViewEnq.SyncData orderData = new ViewEnq.SyncData();
        orderData.execute("");
    }
    // Async Task has three overrided methods,
    private class SyncData extends AsyncTask<String, String, String>
    {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;
        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            progress = ProgressDialog.show(ViewEnq.this, "Synchronising",
                    "RecyclerView Loading! Please Wait...", true);
        }
        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            try
            {
                Connection conn = connectionClass.CONN(); //Connection Object
                if (conn == null)
                {
                    success = false;
                }
                else {
                    // Change below query according to your own database.
                    String query = "SELECT enqid,pid FROM Enquiry_ProductData";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null) // if resultset not null, I add items to itemArraylist using class created
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems(rs.getString("enqid"),rs.getString("pid")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "Found from Enquiry_ProductData";
                        success = true;
                    } else {
                        msg = "No Data found!";
                        success = false;
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }
        @Override
        protected void onPostExecute(String msg) // disimissing progress dialoge, showing error and setting up my listview
        {
            progress.dismiss();
            Toast.makeText(ViewEnq.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false)
            {
            }
            else {
                try
                {
                    myAppAdapter = new ViewEnq.MyAppAdapter(itemArrayList , ViewEnq.this);
                    recyclerView.setAdapter(myAppAdapter);
                } catch (Exception ex)
                {
                }
            }
        }
    }
    public class MyAppAdapter extends RecyclerView.Adapter<MyAppAdapter.ViewHolder> {
        private ArrayList<ClassListItems> values;
        public Context context;
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            // public image title and image url
            public TextView textName1;
            public TextView textName2;
            public View layout;
            public ViewHolder(View v)
            {
                super(v);
                layout = v;
                textName1 = (TextView) v.findViewById(R.id.tv1);
                textName2 = (TextView) v.findViewById(R.id.tv2);
                layout = itemView;
            }
        }
        public MyAppAdapter(ArrayList<ClassListItems> myDataset, ViewEnq context)
        {
            values = myDataset;
            this.context = context;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.viewpdtlist_contents, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return (vh);
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position)
        {
            final ClassListItems ClassViewPdtListItems  = values.get(position);
            holder.textName1.setText(ClassViewPdtListItems.getName1());
            holder.textName2.setText(ClassViewPdtListItems.getName2());
        }
        @Override
        public int getItemCount() {
            return values.size();
        }
    }
}
