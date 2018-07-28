package com.example.anik.ssparen;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.anik.ssparen.R.id.chart;
import static java.lang.Math.ceil;

public class NeighbourGraph extends AppCompatActivity {

    GraphView mScatterPlot;
    private LineChart mChart;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1;
    private DatabaseReference databaseReference2,checkDatabase,checkerAllDatabase;
    private FirebaseDatabase firebaseDatabase;
    private TextView values;
    private String id,flat,size,Uid,house,houseNo,area,areaNo,eid;
    private int j=0,i=0,count,loopCount,prevDate,monthMaxDays,loop=-1,jar=-1,l,a=0,initChild=0;
    private float threshold;
    private char firstChar,UidFirstChar;
    //private double count;
    private String str1="test",flatNo;
    private ArrayList<String> str2;
    private ArrayList<Integer> colorSet;
    private ArrayList<Integer> myDate;
    private ArrayList<Float> myValue;
    private ArrayList<Integer> myArrayDate;
    private ArrayList<Float> myArrayValue;
    private ArrayList<String> myArrayFlat;
    private ArrayList<Float> myArrayThreshold;
    private ArrayList<BarData> list;
    private BarDataSet d;
    private ArrayList<LineData> list1;
    private LineDataSet d1;
    private ArrayList<ILineDataSet> sets;
    private ArrayList<BarEntry> entries;
    private ArrayList<Entry> e1;
    private ListView lv;
    private TextView updateText;
    private Button updateButton;
    ValueEventListener myevent1,myevent2,myevent3;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_neighbour_graph);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),Input.class));
            }
        });

        updateText=(TextView) findViewById(R.id.updateText);
        updateButton=(Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(NeighbourGraph.this,PowerCalculate.class));
            }
        });

        lv = (ListView) findViewById(R.id.listView1);
        list = new ArrayList<BarData>();

        mScatterPlot = (GraphView) findViewById(R.id.chart1);
        myDate=new ArrayList<Integer>();
        myValue=new ArrayList<Float>();
        myArrayFlat=new ArrayList<>();
        myArrayThreshold=new ArrayList<>();



        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        Uid=user.getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        checkDatabase=firebaseDatabase.getReference().child("Residents").child(Uid).child("consume");
        myevent1 = checkDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot checkSnapshot : dataSnapshot.getChildren()) {
                        initChild = (int) dataSnapshot.getChildrenCount();
                    }
                        if (initChild > 0) {

                            databaseReference = firebaseDatabase.getReference("Residents");
                            databaseReference2 = FirebaseDatabase.getInstance().getReference("Residents");
                            databaseReference2.child(Uid).child("flat").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    flatNo = dataSnapshot.getValue().toString();
                                    flatNo = flatNo.replaceAll("[^\\d.]", "");
                                    UidFirstChar = flatNo.charAt(0);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            databaseReference2.child(Uid).child("area").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    areaNo = dataSnapshot.getValue().toString();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });

                            databaseReference2.child(Uid).child("house").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    houseNo = dataSnapshot.getValue().toString();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });

                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        userid post = postSnapshot.getValue(userid.class);

                                        j = (int) dataSnapshot.getChildrenCount();
                                        eid = post.getUserId();

                                        if(dataSnapshot.child(eid).child("flat").exists()) {

                                            flat = post.getFlat();
                                            size = post.getSize();
                                            house = post.getHouse();
                                            area = post.getArea();
                                            flat = flat.replaceAll("[^\\d.]", "");
                                            Float flatSize = Float.valueOf(size);
                                            Calendar c = Calendar.getInstance();
                                            monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);

                                            DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                            threshold = Float.valueOf(decimalFormat.format(flatSize / monthMaxDays));
                                            firstChar = flat.charAt(0);
                                        }

                                        if (area.equals(areaNo)) {
                                            if (UidFirstChar == firstChar) {
                                                id = post.getUserId();
                                                myArrayFlat.add(flat);
                                                myArrayThreshold.add(threshold);



                                                if (dataSnapshot.exists() && house.equals(houseNo)) {

                                                    databaseReference1 = firebaseDatabase.getReference("Residents").child(id).child("consume");
                                                    databaseReference1.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
                                                            entries = new ArrayList<BarEntry>();
                                                            colorSet = new ArrayList<Integer>();
                                                            //sets = new ArrayList<ILineDataSet>();
                                                            myArrayDate = new ArrayList<Integer>();
                                                            myArrayValue = new ArrayList<Float>();
                                                            loop = loop + 1;
                                                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                                retriveTest post = postSnapshot.getValue(retriveTest.class);
                                                                //System.out.println(post.getAuthor() + " - " + post.getTitle() );

                                                                i = (int) dataSnapshot.getChildrenCount();
                                                                Float mark = Float.valueOf(post.getValues());
                                                                Integer diya = Integer.valueOf(post.getDay());

                                                                flat = myArrayFlat.get(loop);
                                                                threshold = myArrayThreshold.get(loop);

                                                                mark = (mark * 100) / threshold;

                                                                myArrayDate.add(diya);
                                                                myArrayValue.add(mark);

                                                                if (i > 1 && myArrayDate.size() > 1) {
                                                                    mark = myArrayValue.get(myArrayValue.size() - 1) - myArrayValue.get(myArrayValue.size() - 2);
                                                                    count = myArrayDate.get(myArrayDate.size() - 1) - myArrayDate.get(myArrayDate.size() - 2);
                                                                    mark = mark / count;
                                                                    //prevDate=myArrayDate.get(myArrayDate.size()-2);
                                                                }
                                                                myDate.add(diya);
                                                                myValue.add(mark);

                                                                if (myArrayDate.size() == 1) {
                                                                    mark = (threshold * 100) / threshold;
                                                                }


                                                                if (mark > 100)
                                                                    colorSet.add(Color.rgb(255, 0, 0));
                                                                else
                                                                    colorSet.add(Color.rgb(0, 255, 0));

                                                                series.appendData(new DataPoint(diya, mark), true, i);
                                                                entries.add(new BarEntry(diya, mark));
                                                                //e1.add(new Entry(diya,mark));
                                                            }



                                                            mScatterPlot.getViewport().setYAxisBoundsManual(true);
                                                            mScatterPlot.getViewport().setMaxY(200);
                                                            mScatterPlot.getViewport().setMinY(0);

                                                            //set manual y bounds
                                                            mScatterPlot.getViewport().setXAxisBoundsManual(true);
                                                            mScatterPlot.getViewport().setMaxX(32);
                                                            mScatterPlot.getViewport().setMinX(0);

                                                            GridLabelRenderer gridLabel = mScatterPlot.getGridLabelRenderer();
                                                            GridLabelRenderer.GridStyle.BOTH.drawHorizontal();
                                                            gridLabel.setHorizontalAxisTitle("Days →");
                                                            gridLabel.setVerticalAxisTitle("Power Consumption in percentage →");
                                                            gridLabel.setVerticalAxisTitleTextSize(22f);
                                                            series.setThickness(2);

                                                            for (; a <= myArrayFlat.size() - 1; ) {
                                                                if (flat == myArrayFlat.get(a)) {
                                                                    series.setColor(Color.GREEN);
                                                                    series.setTitle("Flat-" + flat);
                                                                    break;
                                                                }
                                                                if (flat == myArrayFlat.get(a + 1)) {
                                                                    series.setColor(Color.BLUE);
                                                                    series.setTitle("Flat-" + flat);
                                                                    break;
                                                                }

                                                                if (flat == myArrayFlat.get(a + 2)) {
                                                                    series.setColor(Color.CYAN);
                                                                    series.setTitle("Flat-" + flat);
                                                                    break;
                                                                }

                                                                if (flat == myArrayFlat.get(a + 3)) {
                                                                    series.setColor(Color.BLACK);
                                                                    series.setTitle("Flat-" + flat);
                                                                    break;
                                                                }
                                                                if (flat == myArrayFlat.get(a + 4)) {
                                                                    series.setColor(Color.MAGENTA);
                                                                    series.setTitle("Flat-" + flat);
                                                                    break;
                                                                }
                                                            }

                                                            mScatterPlot.getLegendRenderer().setVisible(true);
                                                            mScatterPlot.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                                                            mScatterPlot.setTitle("Comparision with Neighbours");
                                                            mScatterPlot.setTitleTextSize(30f);
                                                            //series.setAnimated(true);
                                                            series.setDrawDataPoints(true);
                                                            series.setDataPointsRadius(5f);
                                                            //series.isDrawDataPoints();

                                                            mScatterPlot.addSeries(series);


                                                            d = new BarDataSet(entries, "Flat-" + flat + " " + "Average Daily Usage Limit-" + threshold);
                                                            //d.setColors(ColorTemplate.VORDIPLOM_COLORS);
                                                            d.setColors(colorSet);
                                                            d.setBarShadowColor(Color.rgb(203, 203, 203));

                                                            ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();
                                                            sets.add(d);

                                                            BarData cd = new BarData(sets);
                                                            cd.setBarWidth(0.7f);
                                                            list.add(cd);

                                                            ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
                                                            lv.setAdapter(cda);

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                            Toast.makeText(NeighbourGraph.this, "An unexpected error occurred!", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }
                                                    });
                                                }
                                                //values.setText(id);
                                            }
                                        }
                                    }



                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(NeighbourGraph.this, "An unexpected error occurred!", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            });

                        } else {
                            mScatterPlot.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
                            mScatterPlot.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                            mScatterPlot.getGridLabelRenderer().setVerticalLabelsVisible(false);
                            updateText.setVisibility(View.VISIBLE);
                            updateButton.setVisibility(View.VISIBLE);
                        }

                }
                else{
                    mScatterPlot.getGridLabelRenderer().setGridStyle( GridLabelRenderer.GridStyle.NONE );
                    mScatterPlot.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                    mScatterPlot.getGridLabelRenderer().setVerticalLabelsVisible(false);
                    updateText.setVisibility(View.VISIBLE);
                    updateButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(NeighbourGraph.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
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

    private class ChartDataAdapter extends ArrayAdapter<BarData> {

        public ChartDataAdapter(Context context, List<BarData> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            BarData data = getItem(position);

            ViewHolder holder = null;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.list_item_barchart, null);
                holder.chart = (BarChart) convertView.findViewById(chart);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            // apply styling
            //data.setValueTypeface(mTfLight);
            data.setValueTextColor(Color.BLACK);
            holder.chart.getDescription().setEnabled(false);
            holder.chart.setDrawGridBackground(false);

            XAxis xAxis = holder.chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            //xAxis.setTypeface(mTfLight);
            xAxis.setDrawGridLines(false);

            YAxis leftAxis = holder.chart.getAxisLeft();
            //leftAxis.setTypeface(mTfLight);
            leftAxis.setLabelCount(12, false);
            leftAxis.setSpaceTop(15f);

            YAxis rightAxis = holder.chart.getAxisRight();
            //rightAxis.setTypeface(mTfLight);
            rightAxis.setLabelCount(12, false);
            rightAxis.setSpaceTop(15f);

            // set data
            holder.chart.setData(data);
            holder.chart.setFitBars(true);

            // do not forget to refresh the chart
//            holder.chart.invalidate();
            holder.chart.animateY(700);
            return convertView;
        }

        private class ViewHolder {

            BarChart chart;
        }
    }

    @Override
    protected void onStop() {
        checkDatabase.removeEventListener(myevent1);
        super.onStop();
    }

    @Override
    protected void onPause() {
        checkDatabase.removeEventListener(myevent1);
        super.onPause();
    }
}
