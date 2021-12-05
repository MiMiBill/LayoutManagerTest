package com.bill.example.layoutmanagergroup.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bill.example.layoutmanagergroup.R;
import com.bill.layoutmanagergroup.echelon.NewHorizontalLeftEchelonLayoutManager;
import com.bill.layoutmanagergroup.echelon.NewHorizontalRightEchelonLayoutManager;
import com.bill.layoutmanagergroup.echelon.NewVerticalEchelonLayoutManager;

/**
 * Created by Bill
 * github: https://github.com/MiMiBill
 *
 * 梯形布局
 */
public class EchelonFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private View btn;
    private View btn1;
    private NewVerticalEchelonLayoutManager verticalEchelonLayoutManager;
    private NewHorizontalRightEchelonLayoutManager horizontalEchelonLayoutManager;
    private NewHorizontalLeftEchelonLayoutManager leftEchelonLayoutManager;
    private MyAdapter myAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_echelon,container,false);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        btn = rootView.findViewById(R.id.btn);
        btn1 = rootView.findViewById(R.id.btn1);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        verticalEchelonLayoutManager = new NewVerticalEchelonLayoutManager(getContext());
        horizontalEchelonLayoutManager = new NewHorizontalRightEchelonLayoutManager(getContext());
        leftEchelonLayoutManager = new NewHorizontalLeftEchelonLayoutManager(getContext());
        mRecyclerView.setLayoutManager(leftEchelonLayoutManager);
        myAdapter =new MyAdapter();
        mRecyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClick(new onItemClick() {
            @Override
            public void onClick(MyAdapter.ViewHolder viewHolder) {
                mRecyclerView.scrollToPosition(viewHolder.pos);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.scrollToPosition(12);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAdapter.setItemCount(70);
            }
        });

    }

    public interface onItemClick{
        void onClick(MyAdapter.ViewHolder viewHolder);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        private int itemCount = 60;

        private onItemClick onItemClick;

        public void setOnItemClick(EchelonFragment.onItemClick onItemClick) {
            this.onItemClick = onItemClick;
        }

        private int[] icons = {R.mipmap.header_icon_1,R.mipmap.header_icon_2,R.mipmap.header_icon_3,R.mipmap.header_icon_4};
        private int[] bgs = {R.mipmap.bg_1,R.mipmap.bg_2,R.mipmap.bg_3,R.mipmap.bg_4};
        private String[] nickNames = {"左耳近心","凉雨初夏","稚久九栀","半窗疏影"};
        private String[] descs = {
                "回不去的地方叫故乡 没有根的迁徙叫流浪...",
                "人生就像迷宫，我们用上半生找寻入口，用下半生找寻出口",
                "原来地久天长，只是误会一场",
                "不是故事的结局不够好，而是我们对故事的要求过多",
                "只想优雅转身，不料华丽撞墙"
        };
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_echelon,parent,false);
            final ViewHolder viewHolder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null){
                        onItemClick.onClick(viewHolder);
                    }
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.icon.setImageResource(icons[position%4]);
            holder.nickName.setText(nickNames[position%4]  + "pos:" + position);
            holder.desc.setText(descs[position%5]);
            holder.bg.setImageResource(bgs[position%4]);
            holder.pos = position;
        }

        @Override
        public int getItemCount() {
            return itemCount;
        }

        public void setItemCount(int itemCount){
            this.itemCount = itemCount;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView icon;
            ImageView bg;
            TextView  nickName;
            TextView desc;
            public int pos;
            public ViewHolder(View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.img_icon);
                bg = itemView.findViewById(R.id.img_bg);
                nickName = itemView.findViewById(R.id.tv_nickname);
                desc = itemView.findViewById(R.id.tv_desc);

            }
        }
    }

}
