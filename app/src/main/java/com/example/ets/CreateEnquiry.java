package com.example.ets;


import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CreateEnquiry extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ConnectionHelper connectionHelper;
    Button btncreateENQ;
    EditText edtxtPartyname, customername,email,phonemo,address,remark;
    Spinner sallotedprs, senqsrc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_enquiry);

        senqsrc = (Spinner)findViewById(R.id.enqsrcid);
        ArrayAdapter<CharSequence> adapterenqsrc = ArrayAdapter.createFromResource(this, R.array.senqsrc, android.R.layout.simple_spinner_item);
        adapterenqsrc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        senqsrc.setAdapter(adapterenqsrc);
        senqsrc.setOnItemSelectedListener(this);
        sallotedprs = (Spinner)findViewById(R.id.altid);
        ArrayAdapter<CharSequence> adapterallotedprs = ArrayAdapter.createFromResource(this, R.array.sallotedprs, android.R.layout.simple_spinner_item);
        adapterallotedprs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sallotedprs.setAdapter(adapterallotedprs);
        sallotedprs.setOnItemSelectedListener(this);
        btncreateENQ=(Button)findViewById(R.id.crtenqbtnid);
        connectionHelper=new ConnectionHelper();
        edtxtPartyname=(EditText)findViewById(R.id.partynameid);
        customername=(EditText)findViewById(R.id.contactid);
        email=(EditText)findViewById(R.id.mailid);
        phonemo=(EditText)findViewById(R.id.phoneid);
        address=(EditText)findViewById(R.id.addid) ;
        remark=(EditText)findViewById(R.id.remarksid);
        btncreateENQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SyncData syncData=new SyncData();
                syncData.execute("");
                Toast.makeText(getApplicationContext(),"enquiry created",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String text = parent.getItemAtPosition(position).toString();
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
    public class SyncData extends AsyncTask<String,String,String> {
        public SyncData() {
            super();
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con=connectionHelper.CONN();
                if(con!=null)
                {
                    try {
                        System.out.println("connected successfully");
                        Statement stmt=con.createStatement();
                        stmt.executeUpdate("insert into EnquiryTrack_Transaction(customer,custperson,custemail,custcontact,caddr,enqsrc,remarks) values('"+edtxtPartyname.getText().toString()+"','"+customername.getText().toString()+"','"+email.getText().toString()+"','"+phonemo.getText().toString()+"','"+address.getText().toString()+"','"+senqsrc.getSelectedItem().toString()+"','"+remark.getText().toString()+"')");
                        con.close();
                        ResultSet rs=stmt.executeQuery("select * from EnquiryTrack_Transaction");
                        if(rs.next())
                        {
                            System.out.println(rs.getString(1));
                        }
                        con.close();
                    }
                    catch (Exception er){
                        System.out.println(er.toString());
                    }
                }
            }
            catch (Exception ee){
                System.out.println(ee.toString());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

}