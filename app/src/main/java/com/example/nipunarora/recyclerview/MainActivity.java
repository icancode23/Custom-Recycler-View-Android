package com.example.nipunarora.recyclerview;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static com.example.nipunarora.recyclerview.R.id.et_country;

public class MainActivity extends AppCompatActivity {
    private RecyclerView movierecycler;
    private ArrayList<Result> resultlist;
    private RecyclerViewAdapter recycleradapter;
    private AlertDialog.Builder addmovie;
    FloatingActionButton fab;
    View view;
    EditText movieadd;
    private RequestQueue mrequestQueue;
    private SwipeRefreshLayout pull_to_refresh;
    private Paint p = new Paint();// holds the information on how to draw an image can be thought of as a smart brush which knows what colour it has
    public LinearLayoutManager la;
    private ProgressBar progressBar;
    Handler mHandler;
    Runnable loadfrombottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab=(FloatingActionButton)findViewById(R.id.fab);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView();
                addmovie.setTitle("Add Result");
                movieadd.setText("");
                addmovie.show();

            }
        });

        //******************* WE HAVE COMMENTED OUT THE SWIPE REFRESH LAYOUT LISTENER SINCE WE NEEDED ONLY THE Pull up to Refresh ******/
        //pull_to_refresh=(SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        /*pull_to_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh(recycleradapter.getItemCount());
            }
        });*/
        /* pull_to_refresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);*/

        //************ UNCOMMENT THE ABOVE LINES TO ACHIEVE THE FEATURE OF SWIPE DOWN TO LOAD **********//


        final int[] pastVisiblesItems = new int[1];
        final int[] visibleItemCount = new int[1];
        final int[] totalItemCount = new int[1];



        mrequestQueue=VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        movierecycler=(RecyclerView)findViewById(R.id.moviere);
        resultlist=new ArrayList<Result>();
        resultlist.add(new Result("B.Sc Mathematics"));


        recycleradapter=new RecyclerViewAdapter(resultlist);
         la =new LinearLayoutManager(getApplicationContext());
        movierecycler.setLayoutManager(la);
        movierecycler.setAdapter(recycleradapter);
        movierecycler.setItemAnimator(new DefaultItemAnimator());
        addMovieDialog();
        movierecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount[0] = la.getChildCount();//Gives the number of children currently on the screen
                totalItemCount[0] = la.getItemCount();//gives total items of recycler view
                pastVisiblesItems[0] = la.findFirstVisibleItemPosition();//gives the index of item at the top of the screen

                if ((visibleItemCount[0] + pastVisiblesItems[0]) >= totalItemCount[0]) {

                    if(la.findLastCompletelyVisibleItemPosition()==recycleradapter.getItemCount()-1 /*&& recycleradapter.getItemCount()>5 You have your ofset values here*/){
                        //************************* Reached the End of recycler View ***********/
                        Log.d("Status","reached the Bottom");
                        //************** Set The visibility of progress bar as true *****/
                        progressBar.setVisibility(View.VISIBLE);
                        //******************** Call The function to load more data *********//
                        pullToRefresh(recycleradapter.getItemCount());


                    }

                }
            }
        });
        //initSwipe();

    }
    // ********************************** The function remove view is called to remove the previous dialog box from the parent since we want to bring up a newer version of it **********/
    private void removeView() {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

     public void addMovieDialog()
     {
          addmovie=new AlertDialog.Builder(this);
         view=getLayoutInflater().inflate(R.layout.newmovie_dialog,null);
         addmovie.setView(view);
         movieadd=(EditText)view.findViewById(et_country);
         addmovie.setPositiveButton("Add", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                  movieadd=(EditText)view.findViewById(et_country);
                 recycleradapter.addMovie(new Result(movieadd.getText().toString()));
                 dialog.dismiss();



             }
         });


     }
     ///********************************** Adding Pull To Refresh Feature *************/
    public void pullToRefresh(int currentSize)
    {

        int startindex=currentSize;
        int endIndex=currentSize+7;
        String url=String.format("http://139.59.40.238:88/api/results/1?from=0&to=%d",endIndex);
        Log.d("Url Formed",url);
        final ArrayList<Result> templist=new ArrayList<Result>();
        StringRequest getResults = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>()
                {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        /***************** JSON PARSING OF THE RESPONSE*********************/
                        try{
                            JSONObject res=new JSONObject(response);
                            int Json_length=res.length();
                            JSONArray key_array=res.names();
                            for (int i=(Json_length-1);i>=0;--i)
                            {
                                JSONObject temp=res.getJSONObject(key_array.getString(i));
                                /*Log.d("json object",temp.getString("title"));*/
                                templist.add(new Result(temp.getString("title")));
                            }


                        }
                        catch (Exception e)
                        {
                            Log.d("JSON Parse Error",e.toString());
                        }
                        /***************** END OF JSON PARSING *******************************/
                        /******************* notify that the dataset has changed *************/
                        recycleradapter.clear();
                        Log.d("templist size",String.format("%d",templist.size()));
                        recycleradapter.refreshAdd(templist);
                        progressBar.setVisibility(View.GONE);

                    }
                },
                //******************** Enable the starting of app even in the case when internet is no available with default banner images **********/
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", error.toString());
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),(String)error.toString(),Toast.LENGTH_LONG).show();

                    }
                }
        );
        getResults.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mrequestQueue.add(getResults);
        
    }

     ///******************************** Adding the SwipeReveal Feature *********************//
     private void initSwipe(){
         ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {

             @Override
             public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                 return false;
             }

             @Override
             public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                 int position = viewHolder.getAdapterPosition();


                 if (direction == ItemTouchHelper.RIGHT){
                    /*************  Action for swipe left *********/
                     Log.d("Action","right Swiped");

                 } else {
                     /**************** action for swipe right **************/
                     Log.d("Action","Right random");
                 }
             }

             @Override
             public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                 float translationX=0;
                 Bitmap icon;
                 if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                     View itemView = viewHolder.itemView;
                     float height = (float) itemView.getBottom() - (float) itemView.getTop();
                     float width = height / 3;


                     if(dX < 0){
                         p.setColor(Color.parseColor("#388E3C"));
                         RectF background = new RectF(Math.max((float) itemView.getRight() + dX,(3*itemView.getWidth())/4), (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                         c.drawRect(background,p);
                         //translationX=Math.max((float) itemView.getRight() + dX,itemView.getWidth()/4);
                         icon = BitmapFactory.decodeResource(getResources(), R.drawable.save);
                         RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                         c.drawBitmap(icon,null,icon_dest,p);

                     }
                     else {

                     }
                 }
                 super.onChildDraw(c, recyclerView, viewHolder,dX,dY, actionState, isCurrentlyActive);
             }
             @Override
             public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                 int dragFlags = 0;
                 int swipeFlags = ItemTouchHelper.START;
                 return makeMovementFlags(dragFlags, swipeFlags);
             }

         };
         ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
         itemTouchHelper.attachToRecyclerView(movierecycler);

     }


}
