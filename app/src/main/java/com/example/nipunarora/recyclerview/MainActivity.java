package com.example.nipunarora.recyclerview;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import static com.example.nipunarora.recyclerview.R.id.et_country;

public class MainActivity extends AppCompatActivity {
    private RecyclerView movierecycler;
    private ArrayList<Movie> movielist;
    private RecyclerViewAdapter recycleradapter;
    private AlertDialog.Builder addmovie;
    FloatingActionButton fab;
    View view;
    EditText movieadd;
    private Paint p = new Paint();// holds the information on how to draw an image can be thought of as a smart brush which knows what colour it has

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView();
                addmovie.setTitle("Add Movie");
                movieadd.setText("");
                addmovie.show();

            }
        });
        movierecycler=(RecyclerView)findViewById(R.id.moviere);
        movielist=new ArrayList<Movie>();
        movielist.add(new Movie("Iron Man","Action"));
        movielist.add(new Movie("Harry Potter and Goblet of Fire","Adventure"));
        movielist.add(new Movie("Deep Sea","Adventure"));
        movielist.add(new Movie("Kingsman Service","Action"));

        recycleradapter=new RecyclerViewAdapter(movielist);
        RecyclerView.LayoutManager l=new LinearLayoutManager(getApplicationContext());
        movierecycler.setLayoutManager(l);
        movierecycler.setAdapter(recycleradapter);
        movierecycler.setItemAnimator(new DefaultItemAnimator());
        addMovieDialog();
        initSwipe();


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
                 recycleradapter.addMovie(new Movie(movieadd.getText().toString(),"Action"));
                 dialog.dismiss();



             }
         });


     }

     ///******************************** Adding the SwipeReveal Feature *********************//
     private void initSwipe(){
         ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

             @Override
             public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                 return false;
             }

             @Override
             public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                 int position = viewHolder.getAdapterPosition();


                 if (direction == ItemTouchHelper.LEFT){
                    /*************  Action for swipe left *********/
                     ;
                 } else {
                     /***************** action for swipe right ***************/
                     ;
                 }
             }

             @Override
             public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                 Bitmap icon;
                 if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                     View itemView = viewHolder.itemView;
                     float height = (float) itemView.getBottom() - (float) itemView.getTop();
                     float width = height / 3;

                     if(dX > 0){
                         p.setColor(Color.parseColor("#388E3C"));
                         RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                         c.drawRect(background,p);
                         icon = BitmapFactory.decodeResource(getResources(), R.drawable.save);
                         RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                         c.drawBitmap(icon,null,icon_dest,p);
                     } else {
                         p.setColor(Color.parseColor("#D32F2F"));
                         RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                         c.drawRect(background,p);
                         icon = BitmapFactory.decodeResource(getResources(), R.drawable.edit);
                         RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                         c.drawBitmap(icon,null,icon_dest,p);
                     }
                 }
                 super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
             }
         };
         ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
         itemTouchHelper.attachToRecyclerView(movierecycler);
     }

}
