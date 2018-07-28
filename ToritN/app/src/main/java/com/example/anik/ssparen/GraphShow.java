package com.example.anik.ssparen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class GraphShow extends AppCompatActivity {

    GraphView mScatterPlot;
    private LineChart mChart;
    private FirebaseAuth firebaseAuth;
    private TextView textView;
    private Button inputPower,cmpneighbour,graphpre;
    private DatabaseReference databaseReference,databaseReference2,databaseReference3,usageDatabase;
    private ArrayList<Integer> myArrayDate;
    private ArrayList<Float> myArrayValue;
    private ArrayList<Float> percentage;
    private int count,max,monthMaxDays,flatSize=-1,initChild=0;
    private float valueSet,meter,threshold;
    private String flat,zone,size,uid;
    private Description des1,des2,des3;
    private ArrayList<LineData> list1;
    private LineDataSet d1,d2;
    private ArrayList<Entry> e1,e2;
    private ArrayList<ILineDataSet> set1,set2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Input.class));
            }
        });

        textView=(TextView) findViewById(R.id.textView);
        mChart=(LineChart) findViewById(R.id.chart1);
        e1=new ArrayList<>();
        e2=new ArrayList<>();


        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();

        usageDatabase=FirebaseDatabase.getInstance().getReference().child("Residents").child(uid);
        databaseReference= FirebaseDatabase.getInstance().getReference("Residents").child(uid).child("consume");
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this, Home.class));
        }
        //mChart.notifyDataSetChanged();

        databaseReference3=FirebaseDatabase.getInstance().getReference("Residents").child(uid).child("size");
        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    size=dataSnapshot.getValue().toString();
                    flatSize=Integer.valueOf(size);

                    Calendar c=Calendar.getInstance();
                    monthMaxDays=c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    //threshold=flatSize/monthMaxDays;

                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    threshold = Float.valueOf(decimalFormat.format(flatSize/monthMaxDays));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference2=FirebaseDatabase.getInstance().getReference().child("Residents").child(uid).child("consume");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){

                    mChart.getDescription().setEnabled(false);

                    // enable touch gestures
                    mChart.setTouchEnabled(true);

                    mChart.setDragDecelerationFrictionCoef(0.9f);

                    // enable scaling and dragging
                    mChart.setDragEnabled(true);
                    mChart.setScaleEnabled(true);
                    mChart.setDrawGridBackground(false);
                    mChart.setHighlightPerDragEnabled(true);

                    // if disabled, scaling can be done on x- and y-axis separately
                    mChart.setPinchZoom(true);
                    //mScatterPlot = (GraphView) findViewById(R.id.graph);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //String name=dataSnapshot.getValue().toString();
                            //textViewUserEmail.setText(name);
                /*LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
                PointsGraphSeries<DataPoint> pseries=new PointsGraphSeries<DataPoint>();*/
                            myArrayDate = new ArrayList<Integer>();
                            myArrayValue = new ArrayList<Float>();
                            percentage = new ArrayList<Float>();

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                retriveTest post = postSnapshot.getValue(retriveTest.class);

                                int i = (int) dataSnapshot.getChildrenCount();

                                Float avg = Float.valueOf(post.getValues());
                                Float mark = Float.valueOf(post.getValues());
                                Integer diya = Integer.valueOf(post.getDay());
                                meter = (mark * 100) / flatSize;
                                myArrayDate.add(diya);
                                myArrayValue.add(avg);
                                percentage.add(mark);
                                e1.add(new Entry(diya, mark));

                                if (myArrayDate.size() == 1) {
                                    avg = threshold;
                                }

                                if (i > 1 && myArrayDate.size() > 1) {
                                    avg = myArrayValue.get(myArrayValue.size() - 1) - myArrayValue.get(myArrayValue.size() - 2);
                                    count = myArrayDate.get(myArrayDate.size() - 1) - myArrayDate.get(myArrayDate.size() - 2);
                                    avg = avg / count;
                                }
                                e2.add(new Entry(diya, avg));

                    /*series.appendData(new DataPoint(diya,mark), true , i);
                    pseries.appendData(new DataPoint(diya,mark),true,i);*/

                            }

                            d1 = new LineDataSet(e1, "Monthly usage       ");
                            d1.setAxisDependency(YAxis.AxisDependency.LEFT);
                            d1.setColor(Color.rgb(0, 100, 0));
                            d1.setCircleColor(Color.rgb(0, 100, 0));
                            d1.setLineWidth(2f);
                            d1.setCircleRadius(3f);
                            d1.setFillAlpha(65);
                            d1.setFillColor(Color.rgb(0, 100, 0));
                            d1.setHighLightColor(Color.rgb(244, 117, 117));
                            d1.setDrawCircleHole(false);
                            d1.setValueTextColor(Color.rgb(0, 100, 0));

                            d2 = new LineDataSet(e2, "Daily Average usage(Daily usage limit)-" + threshold);
                            d2.setAxisDependency(YAxis.AxisDependency.RIGHT);
                            d2.setColor(Color.BLUE);
                            d2.setCircleColor(Color.BLUE);
                            d2.setLineWidth(2f);
                            d2.setCircleRadius(3f);
                            d2.setFillAlpha(65);
                            d2.setFillColor(Color.BLUE);
                            d2.setDrawCircleHole(false);
                            d2.setHighLightColor(Color.rgb(244, 117, 117));
                            d2.setValueTextColor(Color.BLUE);

                /*mChart.getDescription().setText("X Axis: Days →");
                mChart.getDescription().setPosition(15f,10f);
                mChart.getDescription().setTextColor(Color.BLACK);*/

                            LineData data = new LineData(d1, d2);
                            //data.setValueTextColor(Color.BLACK);
                            data.setValueTextSize(9f);

                            // set data
                            mChart.setData(data);
                            //mChart.notifyDataSetChanged();
                            //mChart.invalidate();
                /*set1.add(d1);
                set2.add(d2);*/
                            //mChart.setData(new LineData(set1));
                            //mChart.setData(new LineData(set2));
                            //mChart.invalidate();


                            //mChart.animateX(2500);

                            // get the legend (only possible after setting data)
                            Legend l = mChart.getLegend();

                            // modify the legend ...
                            l.setForm(Legend.LegendForm.LINE);
                            l.setTextSize(11f);
                            //l.setTextColor(Color.GREEN);
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                            l.setDrawInside(false);

                /*LimitLine llXAxis = new LimitLine(10f, "Index 10");
                llXAxis.setLineWidth(4f);
                llXAxis.enableDashedLine(10f, 10f, 0f);
                llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
                llXAxis.setTextSize(10f);*/

                            XAxis xAxis = mChart.getXAxis();
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            //xAxis.setTypeface(mTfLight);
                            xAxis.setAxisMaximum(31);
                            xAxis.setAxisMinimum(0);
                            xAxis.setAxisLineWidth(3f);
                            xAxis.setTextSize(11f);
                            xAxis.setTextColor(Color.BLACK);
                            xAxis.setDrawGridLines(true);
                            xAxis.setDrawAxisLine(true);

                /*Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

                LimitLine ll1 = new LimitLine(400f, "Upper Limit");
                ll1.setLineColor(Color.BLUE);
                ll1.setLineWidth(4f);
                ll1.enableDashedLine(10f, 10f, 0f);
                ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll1.setTextSize(10f);
                ll1.setTypeface(tf);*/

                            YAxis leftAxis = mChart.getAxisLeft();
                            //leftAxis.setTypeface(mTfLight);
                            leftAxis.setTextColor(Color.rgb(0, 100, 0));
                            leftAxis.setAxisMaximum(myArrayValue.get(myArrayValue.size()-1)+10);
                            leftAxis.setAxisMinimum(myArrayValue.get(0)-20);
                            leftAxis.setDrawGridLines(true);
                            leftAxis.setGranularityEnabled(true);

                            YAxis rightAxis = mChart.getAxisRight();
                            //rightAxis.setTypeface(mTfLight);
                            rightAxis.setTextColor(Color.BLUE);
                            rightAxis.setAxisMaximum(threshold+(myArrayValue.get(myArrayValue.size()-1)-myArrayValue.get(0)));
                            rightAxis.setAxisMinimum(0);
                            rightAxis.setDrawGridLines(false);
                            rightAxis.setDrawZeroLine(false);
                            rightAxis.setGranularityEnabled(false);
                            //mChart.invalidate();

                            if (percentage.size() == 1) {
                                valueSet = (percentage.get(0) * 100) / flatSize;
                            } else {
                                valueSet = ((percentage.get(percentage.size() - 1) - percentage.get(0)) * 100) / flatSize;
                            }
                            DecimalFormat decimalFormat = new DecimalFormat("#.##");
                            valueSet = Float.valueOf(decimalFormat.format(valueSet));
                            Calendar c = Calendar.getInstance();
                            monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                            monthMaxDays = monthMaxDays - myArrayDate.get(myArrayDate.size() - 1);
                            if (valueSet < 75)
                                zone = "Green";
                            else if (valueSet >= 75 && valueSet <= 99)
                                zone = "Yellow";
                            else zone = "Red";

                            int usageValue = (int) valueSet;
                            usageDatabase.child("usage").setValue(usageValue);
                            textView.setText("You have consumed " + valueSet + "% " + "of your Power.There are " + monthMaxDays + " Days left.You are in the " + zone + " zone.");
                            textView.setTextSize(25f);
                            textView.setTextColor(Color.BLACK);


                /*mScatterPlot.getViewport().setYAxisBoundsManual(true);
                mScatterPlot.getViewport().setMaxY(250);
                mScatterPlot.getViewport().setMinY(0);

                //set manual y bounds
                mScatterPlot.getViewport().setXAxisBoundsManual(true);
                mScatterPlot.getViewport().setMaxX(30);
                mScatterPlot.getViewport().setMinX(0);

                GridLabelRenderer gridLabel = mScatterPlot.getGridLabelRenderer();
                gridLabel.setHorizontalAxisTitle("Days →");
                gridLabel.setVerticalAxisTitle("Power Consumption →");
                mScatterPlot.addSeries(series);
                mScatterPlot.addSeries(pseries);*/
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getMessage());

                        }
                    });
                }
                else {
                    textView.setText("Your data is not available.\n Please input your meter reading regularly.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_profile_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_update:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                //finish();
                startActivity(new Intent(this, ProinfoUpdate.class));

                return true;

            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(this, Home.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
