package com.firoz.mahmud.accountbook;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class MoneyDetails extends Fragment {
    FloatingActionButton fab;
    MemberDetailsDB.MyData md;
    MainActivity ma;
    ExpandableListView paidlist, duelist;
    ExpandableListAdapter eladue, elapaid;
    Context context;
    LayoutInflater li;
    Handler han=new Handler();
    DueDb duedb;
    PaidDb paiddb;
    ArrayList<DueDb.MyData> duedata;
    ArrayList<PaidDb.MyData> paiddata;

    @SuppressLint("RestrictedApi")
    public MoneyDetails(FloatingActionButton fab, MemberDetailsDB.MyData md, final MainActivity ma, Context context) {
        this.md = md;
        this.fab = fab;
        this.context = context;
        this.ma = ma;
        duedb = new DueDb(context, AllData.sqlDataBase.databasename);
        duedata = duedb.getAllData(md.getTimemiles());
        paiddb = new PaidDb(context, AllData.sqlDataBase.databasename);
        paiddata = paiddb.getAllData(md.getTimemiles());
        li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ma.allmember = false;
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_money_details, container, false);
        paidlist = view.findViewById(R.id.money_details_paid_listview);
        duelist = view.findViewById(R.id.money_details_due_listview);
        eladedue();
        eladepaid();
        ma.bw = new BackWork() {
            @Override
            public void customMethode() {
                ma.getAllMember();
            }

            @Override
            public void fabwork() {
                MoneyDialog moneyd = new MoneyDialog(context) {
                    @Override
                    public void onCompleate(String amount, String details, int day, int month, int year) {
                        duedb.addAmount(Integer.valueOf(amount), details, day, month, year, md.getTimemiles());
                        refrash();
                    }
                };
                moneyd.showDialog("Add due", String.valueOf(md.getMonthly_payment()),
                        "Monthly Payment",
                        DateFormat.format("dd/MM/yyyy", new Date()).toString());
            }
        };
        paidlist.setAdapter(elapaid);
        duelist.setAdapter(eladue);
        paidlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                final PaidDb.MyData sdata = paiddata.get(i);
                switch (i1) {
                    case 0:
                        MoneyDialog moneyd = new MoneyDialog(context) {
                            @Override
                            public void onCompleate(String amount, String details, int day, int month, int year) {
                                sdata.setAmount(Integer.valueOf(amount));
                                sdata.setDetails(details);
                                sdata.setDay(day);
                                sdata.setMonth(month);
                                sdata.setYear(year);
                                paiddb.updateData(sdata);
                                refrash();
                            }
                        };
                        moneyd.showDialog("Edit Paid", sdata.getAmount() + "",
                                sdata.getDetails(),
                                sdata.getDay() + "/" + sdata.getMonth() + "/" + sdata.getYear());
                        break;
                    case 1:
                        DetailsChangerDialog dcd = new DetailsChangerDialog(context, InputType.TYPE_CLASS_TEXT) {
                            @Override
                            public void getChangedData(String data) {
                                paiddb.deleteById(sdata.getId());
                                refrash();
                            }
                        };
                        dcd.showDialog("Remove Paid Item", "Type \"cf\" to remove.");
                        dcd.setInputDesable();
                        break;
                }
                return true;
            }
        });
        duelist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, final int i, int i1, long l) {
                final DueDb.MyData sdata = duedata.get(i);
                switch (i1) {
                    case 0:
                        MoneyDialog addpaid = new MoneyDialog(context) {
                            @Override
                            public void onCompleate(String amount, String details, int day, int month, int year) {
                                if (amount.equals(sdata.getAmount() + "")) {
                                    duedb.deleteById(sdata.getId());
                                    String dateins = DateFormat.format("dd/MM/yyyy", new Date()).toString();
                                    paiddb.addAmount(Integer.valueOf(amount), details + " of " + "" + getStringDate(month) + " " + year,
                                            Integer.valueOf(dateins.split("/")[0]),
                                            Integer.valueOf(dateins.split("/")[1]),
                                            Integer.valueOf(dateins.split("/")[2]),

                                            md.getTimemiles());
                                    refrash();
                                } else if (Integer.valueOf(amount) < sdata.getAmount()) {
                                    sdata.setAmount(sdata.getAmount() - Integer.valueOf(amount));
                                    duedb.updateData(sdata);
                                    String dateins = DateFormat.format("dd/MM/yyyy", new Date()).toString();
                                    paiddb.addAmount(Integer.valueOf(amount), details + " of " + getStringDate(month) + " " + year,
                                            Integer.valueOf(dateins.split("/")[0]),
                                            Integer.valueOf(dateins.split("/")[1]),
                                            Integer.valueOf(dateins.split("/")[2]),
                                            md.getTimemiles());
                                    refrash();
                                } else {
                                    Toast.makeText(context, "Amount is too high.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                        addpaid.showDialog("Add to paid", String.valueOf(sdata.getAmount()),
                                "Monthly Payment",
                                sdata.getDay() + "/" + sdata.getMonth() + "/" + sdata.getYear());
                        break;
                    case 1:
                        MoneyDialog moneyd = new MoneyDialog(context) {
                            @Override
                            public void onCompleate(String amount, String details, int day, int month, int year) {
                                sdata.setAmount(Integer.valueOf(amount));
                                sdata.setDetails(details);
                                sdata.setDay(day);
                                sdata.setMonth(month);
                                sdata.setYear(year);
                                duedb.updateData(sdata);
                                refrash();
                            }
                        };
                        moneyd.showDialog("Edit due", String.valueOf(sdata.getAmount()),
                                "Monthly Payment",
                                sdata.getDay() + "/" + sdata.getMonth() + "/" + sdata.getYear());
                        break;
                    case 2:
                        DetailsChangerDialog dcd = new DetailsChangerDialog(context, InputType.TYPE_CLASS_TEXT) {
                            @Override
                            public void getChangedData(String data) {
                                duedb.deleteById(sdata.getId());
                                refrash();
                            }
                        };
                        dcd.showDialog("Remove Due Item", "Type \"cf\" to remove.");
                        dcd.setInputDesable();
                        break;
                }
                return true;
            }
        });
        Thread th = new Thread() {
            @Override
            public void run() {
                final MonthDetails monthd = new MonthDetails(context);
                ArrayList<String> months = monthd.getAllData(md.getTimemiles());
                String admit = Integer.valueOf(md.getDate().split("/")[1])+"/"+md.getDate().split("/")[2];
                String d=DateFormat.format("MM/yyyy",new Date()).toString();
                String pda = Integer.valueOf(d.split("/")[0])+"/"+d.split("/")[1];
                boolean nrefre=false;
                while (!pda.equals(admit)) {
                    if (months.contains(pda)) {
                        break;
                    } else {
                        try {
                            String split[] = monthCorrection(pda.split("/"));
                            duedb.addAmount(md.getMonthly_payment(), "Monthly Payment", 28, Integer.valueOf(split[0]), Integer.valueOf(split[1]), md.getTimemiles());
                            monthd.addAllData(md.getTimemiles(), pda);
                            pda = decreaseMonth(pda);
                            nrefre = true;
                        }catch (Exception e){

                        }
                    }
                }
                if (nrefre){
                    refrash();
                }
            }

        };
        th.start();
        return view;
    }
        private void eladedue () {
            eladue = new ExpandableListAdapter() {
                @Override
                public void registerDataSetObserver(DataSetObserver dataSetObserver) {

                }

                @Override
                public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

                }

                @Override
                public int getGroupCount() {
                    return duedata.size();
                }

                @Override
                public int getChildrenCount(int i) {
                    return 3;
                }

                @Override
                public Object getGroup(int i) {
                    return i;
                }

                @Override
                public Object getChild(int i, int i1) {
                    return (10 * i + i1);
                }

                @Override
                public long getGroupId(int i) {
                    return (i * 100);
                }

                @Override
                public long getChildId(int i, int i1) {
                    return (1000 * i + i1);
                }

                @Override
                public boolean hasStableIds() {
                    return true;
                }

                @Override
                public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
                    if (view == null) {
                        view = li.inflate(R.layout.money_group_view, null);
                    }
                    TextView amount = view.findViewById(R.id.money_group_view_amount_textview),
                            date = view.findViewById(R.id.money_group_view_date_textview),
                            details = view.findViewById(R.id.money_group_view_details_textview);
                    amount.setText("" + duedata.get(i).getAmount());
                    date.setText(duedata.get(i).getDay() + "/" + duedata.get(i).getMonth() + "/" + duedata.get(i).getYear());
                    details.setText(duedata.get(i).getDetails());
                    return view;
                }
                @Override
                public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
                    if (view == null) {
                        view = li.inflate(R.layout.all_member_list_child_custom_view, null);
                    }
                    ImageView iv = view.findViewById(R.id.all_member_list_child_custom_imageview);
                    TextView tv = view.findViewById(R.id.all_member_custom_child_textview);
                    switch (i1) {
                        case 0:
                            iv.setImageResource(R.drawable.paid);
                            tv.setText("Add to Paid");
                            break;
                        case 1:
                            iv.setImageResource(R.drawable.edit);
                            tv.setText("Edit");
                            break;
                        case 2:
                            iv.setImageResource(R.drawable.remove_with_color);
                            tv.setText("Remove");
                            break;
                    }
                    return view;
                }

                @Override
                public boolean isChildSelectable(int i, int i1) {
                    return true;
                }

                @Override
                public boolean areAllItemsEnabled() {
                    return true;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public void onGroupExpanded(int i) {

                }

                @Override
                public void onGroupCollapsed(int i) {

                }

                @Override
                public long getCombinedChildId(long l, long l1) {
                    return (10 * l / 2 + l1);
                }

                @Override
                public long getCombinedGroupId(long l) {
                    return l;
                }
            };
        }
        private void eladepaid () {
            elapaid = new ExpandableListAdapter() {
                @Override
                public void registerDataSetObserver(DataSetObserver dataSetObserver) {

                }

                @Override
                public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

                }

                @Override
                public int getGroupCount() {
                    return paiddata.size();
                }

                @Override
                public int getChildrenCount(int i) {
                    return 2;
                }

                @Override
                public Object getGroup(int i) {
                    return i;
                }

                @Override
                public Object getChild(int i, int i1) {
                    return (10 * i + i1);
                }

                @Override
                public long getGroupId(int i) {
                    return (i * 100);
                }

                @Override
                public long getChildId(int i, int i1) {
                    return (1000 * i + i1);
                }

                @Override
                public boolean hasStableIds() {
                    return true;
                }

                @Override
                public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
                    if (view == null) {
                        view = li.inflate(R.layout.money_group_view, null);
                    }
                    TextView amount = view.findViewById(R.id.money_group_view_amount_textview),
                            date = view.findViewById(R.id.money_group_view_date_textview),
                            details = view.findViewById(R.id.money_group_view_details_textview);
                    amount.setText("" + paiddata.get(i).getAmount());
                    date.setText(paiddata.get(i).getDay() + "/" + paiddata.get(i).getMonth() + "/" + paiddata.get(i).getYear());
                    details.setText(paiddata.get(i).getDetails());
                    return view;
                }

                @Override
                public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
                    if (view == null) {
                        view = li.inflate(R.layout.all_member_list_child_custom_view, null);
                    }
                    ImageView iv = view.findViewById(R.id.all_member_list_child_custom_imageview);
                    TextView tv = view.findViewById(R.id.all_member_custom_child_textview);
                    switch (i1) {
                        case 0:
                            iv.setImageResource(R.drawable.edit);
                            tv.setText("Edit");
                            break;
                        case 1:
                            iv.setImageResource(R.drawable.remove_with_color);
                            tv.setText("Remove");
                            break;
                    }
                    return view;
                }

                @Override
                public boolean isChildSelectable(int i, int i1) {
                    return true;
                }

                @Override
                public boolean areAllItemsEnabled() {
                    return true;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public void onGroupExpanded(int i) {

                }

                @Override
                public void onGroupCollapsed(int i) {

                }

                @Override
                public long getCombinedChildId(long l, long l1) {
                    return (10 * l / 2 + l1);
                }

                @Override
                public long getCombinedGroupId(long l) {
                    return l;
                }
            };
        }

        private String decreaseMonth(String data){
        String arr[]=data.split("/");
        int month= Integer.valueOf(arr[0]);
        int year= Integer.valueOf(arr[1]);
        month--;
        if (month==0){
            year--;
            month=12;
        }
        return month+"/"+year;
        }
    private void refrash() {
        han.post(new Runnable() {
            @Override
            public void run() {
                MoneyDetails moneyDetails = new MoneyDetails(fab, md, ma, context);
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_frame, moneyDetails).commit();
            }});
    }
    private String getStringDate(int month){
        switch (month){
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "Mach";
            case 4:
                return "Afril";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "Octobur";
            case 11:
                return "November";
            case 12:
                return "December";
        }
        return "Nothing";
    }
    private String[] monthCorrection(String[] months){
        int month=Integer.valueOf(months[0]);
        int year=Integer.valueOf(months[1]);
        if (month==1){
            return new String[]{"12",String.valueOf(year-1)};
        }else{
            return new String[]{String.valueOf(month-1),months[1]};
        }
    }
}