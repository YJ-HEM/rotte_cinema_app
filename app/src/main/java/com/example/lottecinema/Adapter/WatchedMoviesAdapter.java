package com.example.lottecinema.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lottecinema.AllQRCode;
import com.example.lottecinema.BitemapConverter;
import com.example.lottecinema.HttpMyReviewThread;
import com.example.lottecinema.HttpReviewThread;
import com.example.lottecinema.MyMovies;
import com.example.lottecinema.MyReviews;
import com.example.lottecinema.R;
import com.example.lottecinema.Review;
import com.example.lottecinema.ShowMyReview;
import com.example.lottecinema.databinding.ActivityWatchedMoviesBinding;
import com.example.lottecinema.databinding.WatchedmoviesItemBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.lottecinema.HttpReviewThread.list;

public class WatchedMoviesAdapter extends RecyclerView.Adapter<WatchedMoviesAdapter.ViewHolder> {

    static public SharedPreferences sharedPreferences;
    static public int item_position;
    private ArrayList<MyMovies> mData = list;
    private static WatchedmoviesItemBinding binding;
    private static ActivityWatchedMoviesBinding binding2;
    ViewHolder holder2;
    private static final String TAG = "MovieAdapter";
    private Context context;
    Bitmap poster_bitmap;
    Bitmap age_bitmap;

    // ??????????????? ????????? ????????? ????????? ????????????.
    public WatchedMoviesAdapter(Context context, ArrayList<MyMovies> list) {
        //  testbitmap = BitemapConverter.StringToBitmap(HttpReviewThread.Bitmap);

        this.context = context;
    }

    // onCreateViewHolder() - ????????? ?????? ?????? ????????? ?????? ???????????? ??????.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");

        binding = WatchedmoviesItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.watchedmovies_item, parent, false);
        View view = binding.getRoot();
        ViewHolder holder = new ViewHolder(view);

        HttpMyReviewThread httpMyReviewThread = new HttpMyReviewThread();
        httpMyReviewThread.start();

        try {
            httpMyReviewThread.join();
        } catch (Exception e){};

        return holder;
    }

    // onBindViewHolder() - position??? ???????????? ???????????? ???????????? ??????????????? ??????.
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(WatchedMoviesAdapter.ViewHolder holder, int position) {
        Log.d("onBindViewHolder", "onBindViewHolder: " + position);


        MyMovies myMoviesOrder = mData.get(position);

        sharedPreferences = context.getSharedPreferences("review", context.MODE_PRIVATE);
        String reviewResult = sharedPreferences.getString("review_result", "");
        String position_string = reviewResult.replaceAll("[^0-9]", "");


        if (reviewResult.equals("success")) {
            holder.btn_write_review.setText("?????? ??? ??????");
        }


        holder.txt_date.setText(myMoviesOrder.getDate());

        String show_time = myMoviesOrder.getDate();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DateFormat time = new SimpleDateFormat("HH:mm");

        Calendar cal2 = null;
        try {
            Date show_date = df.parse(show_time); //??????????????? df?????? ??????
             cal2 = Calendar.getInstance();
            cal2.setTime(show_date);
            cal2.add(Calendar.HOUR, 1); //????????? ?????????

            Log.d("time","+1?????? : "+cal2.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("time",e.toString());
        }

        Calendar cal = Calendar.getInstance(); //????????????
        Log.d("time", "???????????? : "+cal.getTime().toString());
        boolean a = cal2.before(cal);
        Log.d("time", a+"true/false"); //??????????????? ????????????+1?????? ????????? false??????

        //?????? ????????? ??????????????????+1?????? ?????? qr????????? ?????? ????????? ????????????????????????.
        if(cal2.before(cal)){
            holder.btn_qr_code.setBackgroundColor(Color.parseColor("#424242"));
            holder.btn_qr_code.setEnabled(false);
        }

        //qr?????? ????????????
        else{
            holder.btn_qr_code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AllQRCode.class);
                    intent.putExtra("movieTitle",myMoviesOrder.getMovie());
                    intent.putExtra("seat_info",myMoviesOrder.getCinema()+" "+myMoviesOrder.getCustomer()+myMoviesOrder.getSeat());
                    intent.putExtra("date",myMoviesOrder.getDate());
                    intent.putExtra("age_limit",myMoviesOrder.getAge());

                    context.startActivity(intent);
                }
            });
        }


        Map<String, String> reviewMap = new HashMap<String, String>();



        for(MyReviews i : HttpMyReviewThread.review_list){
            if(
            i.getMovieTitle().equals(myMoviesOrder.getMovie())){
                holder.btn_write_review.setText("?????? ??? ????????????");
            }
        }







        holder.txt_loca.setText(myMoviesOrder.getCinema());
            holder.txt_peopleNum.setText(myMoviesOrder.getCustomer());
            holder.txt_seatNum.setText(myMoviesOrder.getSeat());
            holder.txt_movieTitle.setText(myMoviesOrder.getMovie());

            age_bitmap = BitemapConverter.StringToBitmap(myMoviesOrder.getAge());
            poster_bitmap = BitemapConverter.StringToBitmap(myMoviesOrder.getPoster());
            holder.img_poster.setImageBitmap(poster_bitmap);
            holder.img_age.setImageBitmap(age_bitmap);
            holder.btn_write_review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                    Log.d("revieww", "?????? ??? ??? ?????? " + holder.getAbsoluteAdapterPosition());
                    Log.d("revieww", myMoviesOrder.getMovie());
                    Log.d("revieww", String.valueOf(myMoviesOrder.getMovie_index()));

                    if(holder.btn_write_review.getText().equals("????????????")){

                    int position_int = holder.getAbsoluteAdapterPosition();
                    String position_string = Integer.toString(position_int);
                    Intent intent = new Intent(context, Review.class);
                    intent.putExtra("moiveTitle", myMoviesOrder.getMovie());
                    intent.putExtra("position", position_int);
                    intent.putExtra("movieIndex", myMoviesOrder.getMovie_index());


                    context.startActivity(intent);}


                    else{
                        Intent intent = new Intent(context, ShowMyReview.class);

                        for(MyReviews i : HttpMyReviewThread.review_list){
                            if(
                                    i.getMovieTitle().equals(myMoviesOrder.getMovie())){
                                intent.putExtra("ratingNum",i.getRatingNum());
                                intent.putExtra("contents",i.getReviewTxt());
                                intent.putExtra("date",i.getDate());
                                Log.d("reviewdate",i.getRatingNum());

                            }
                        }



                        intent.putExtra("movieTitle",myMoviesOrder.getMovie());

                        context.startActivity(intent);
                    }
                }
            });

        }

        // getItemCount() - ?????? ????????? ?????? ??????.
        @Override
        public int getItemCount () {
            Log.d("getItemCount", mData.size() + "");

            return mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txt_date;
            TextView txt_loca;
            TextView txt_peopleNum;
            TextView txt_seatNum;
            TextView txt_movieTitle;

            ImageView img_poster;
            ImageView img_age;
            Button btn_write_review;
            Button btn_qr_code;
            public ViewHolder(@NonNull View view) {
                super(view);
                // ??? ????????? ?????? ??????. (hold strong reference)
                //  textView1 = view.findViewById(R.id.date);
                txt_date = binding.date;
                txt_loca = binding.theaterLocation;
                txt_peopleNum = binding.audienceNum;
                txt_seatNum = binding.seatNum;
                txt_movieTitle = binding.movieNameText;
                img_poster = binding.moviePoster;
                img_age = binding.imageAge;
                btn_write_review = binding.btnDoShowReview;
                btn_qr_code = binding.btnQrCode;

            }

        }

    }