package com.firoz.mahmud.accountbook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class AllMember extends Fragment {
    Context context;
    MemberDetailsDB mddb;
    FloatingActionButton fab;
    MainActivity ma;
    ArrayList<MemberDetailsDB.MyData> data;
    ExpandableListView elv;
    boolean exit=false;
    ExpandableListAdapter ela;
    SharedPreferences sh;
    public AllMember(final Context context, MemberDetailsDB mddb, FloatingActionButton fab, final MainActivity ma) {
        this.ma = ma;
        this.fab = fab;
        this.context = context;
        this.mddb = mddb;
        BackWork bw=new BackWork(){
            @Override
            public void customMethode(){
                if (!exit){
                    Toast.makeText(context, "Press Again to exit", Toast.LENGTH_SHORT).show();
                    exit=true;
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ma.finishAffinity();
                    }else{
                        ma.finish();
                    }
                }
            }
        };
        ma.bw=bw;
        this.data = new ArrayList<MemberDetailsDB.MyData>();
        this.data.addAll(mddb.getAllMemberDetails());
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_member, container, false);
        sh=context.getSharedPreferences(AllData.sqlDataBase.databasename,Context.MODE_PRIVATE);
        inisialize(inflater);
        elv = view.findViewById(R.id.all_member_listview);
        elv.setAdapter(ela);
        View v = elv.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - elv.getPaddingTop());
        int index=sh.getInt("Pos",elv.getFirstVisiblePosition());
        int topi=sh.getInt("Posi",top);
        elv.setSelectionFromTop(index, topi);
        ma.allmember=true;
        fab.setVisibility(View.VISIBLE);
        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                final MemberDetailsDB.MyData single = data.get(groupPosition);
                switch (childPosition) {
                    case 0:
                        fab.setVisibility(View.GONE);
                        MemberDetails md = new MemberDetails(single,context,ma);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.main_activity_frame, md).commit();
                        break;
                    case 1:
                        ma.allmember = false;
                        fab.setVisibility(View.VISIBLE);
                        MoneyDetails moneyDetails = new MoneyDetails(fab,single,ma,context);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.main_activity_frame, moneyDetails).commit();
                        break;
                    case 2:
                        ConfirmRemoveDialog crd=new ConfirmRemoveDialog(context) {
                            @Override
                            public void onComplete() {
                                mddb.removeMember(single.getId());
                                DueDb dueDb=new DueDb(context,AllData.sqlDataBase.databasename);
                                PaidDb paidDb=new PaidDb(context,AllData.sqlDataBase.databasename);
                                dueDb.deleteByTimeMiles(single.getTimemiles());
                                paidDb.deleteByTimeMiles(single.getTimemiles());
                                ma.getAllMember();
                            }
                        };
                        crd.showDialog(single.getMember_name(),single.getFull_circle_pic_path());
                        break;
                }
                savePosition();
                return true;
            }
        });
        return view;
    }

    private void inisialize(final LayoutInflater inflater) {
        ela = new ExpandableListAdapter() {
            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getGroupCount() {
                return data.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return 3;
            }

            @Override
            public Object getGroup(int groupPosition) {
                return null;
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return (groupPosition*10)+childPosition;
            }

            @Override
            public long getGroupId(int groupPosition) {
                return 0;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return (groupPosition*10)+childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.all_member_list_group_custom_view, null);
                }
                MemberDetailsDB.MyData single = data.get(groupPosition);
                ImageView iv = convertView.findViewById(R.id.all_member_custom_group_imageView);
                TextView name = convertView.findViewById(R.id.all_member_custom_group_textView);
                TextView address = convertView.findViewById(R.id.all_member_address_textview);
                File file = new File(single.getHalf_circle_pic_path());
                name.setText(single.getMember_name());
                address.setText(single.getAddress());
                Picasso.with(context).load(file).placeholder(R.drawable.error_picture).error(R.drawable.error_picture).into(iv);
                return convertView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.all_member_list_child_custom_view, null);
                }
                TextView tv = convertView.findViewById(R.id.all_member_custom_child_textview);
                ImageView iv=convertView.findViewById(R.id.all_member_list_child_custom_imageview);
                switch (childPosition) {
                    case 0:
                        tv.setText("About");
                        iv.setImageResource(R.drawable.about);
                        break;
                    case 1:
                        tv.setText("Money Details");
                        iv.setImageResource(R.drawable.mony_details);
                        break;
                    case 2:
                        tv.setText("Remove this person");
                        iv.setImageResource(R.drawable.remove_with_color);
                        break;
                }
                return convertView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }

            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public void onGroupExpanded(int groupPosition) {

            }

            @Override
            public void onGroupCollapsed(int groupPosition) {

            }

            @Override
            public long getCombinedChildId(long groupId, long childId) {
                return 0;
            }

            @Override
            public long getCombinedGroupId(long groupId) {
                return 0;
            }
        };
    }
    public void savePosition(){
        int index = elv.getFirstVisiblePosition();
        View v = elv.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - elv.getPaddingTop());
        SharedPreferences.Editor she=sh.edit();
        she.putInt("Pos",index);
        she.putInt("Posi",top);
        she.commit();
    }

}