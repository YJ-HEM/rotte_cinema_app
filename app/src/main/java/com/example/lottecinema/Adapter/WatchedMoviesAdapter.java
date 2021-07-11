package com.example.lottecinema.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lottecinema.R;
import com.example.lottecinema.databinding.WatchedmoviesItemBinding;

import java.util.ArrayList;

public class WatchedMoviesAdapter extends RecyclerView.Adapter<WatchedMoviesAdapter.ViewHolder> {

private ArrayList<String> mData = null ;
private WatchedmoviesItemBinding binding = null;

// 아이템 뷰를 저장하는 뷰홀더 클래스.
public class ViewHolder extends RecyclerView.ViewHolder {
    TextView textView1 ;

    ViewHolder(View itemView) {
        super(itemView) ;
//        binding.date.setText("123");
//        binding.hour.setText("123");
//        binding.movieNameText.setText("123");
//        binding.theaterLocation.setText("123");
//
//
//
//        // 뷰 객체에 대한 참조. (hold strong reference)
        textView1 = binding.date;
    }
}

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public WatchedMoviesAdapter(ArrayList<String> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public WatchedMoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = WatchedmoviesItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.watchedmovies_item, parent, false) ;
        WatchedMoviesAdapter.ViewHolder vh = new WatchedMoviesAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(WatchedMoviesAdapter.ViewHolder holder, int position) {
        String text = mData.get(position) ;
        holder.textView1.setText(text) ;
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}