package penguin.onlineq;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import penguin.onlineq.Common.Common;
import penguin.onlineq.Interface.ItemClickListener;
import penguin.onlineq.Interface.RankingCallBack;
import penguin.onlineq.Model.QuestionScore;
import penguin.onlineq.Model.Ranking;
import penguin.onlineq.ViewHolder.RankingViewHolder;


public class RankingFragment extends Fragment {

    View myfragment;

    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking,RankingViewHolder> adapter;


    FirebaseDatabase database;
    DatabaseReference questionScore,rankingTb1;
    int sum=0;


    public  static RankingFragment newInstance(){
        RankingFragment rankingFragment=new RankingFragment();
        return  rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database= FirebaseDatabase.getInstance();
        questionScore=database.getReference("Question_Score");
        rankingTb1=database.getReference("Ranking");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myfragment=inflater.inflate(R.layout.fragment_ranking,container,false);

        //init View
        rankingList=(RecyclerView)myfragment.findViewById(R.id.rankingList);
        layoutManager=new LinearLayoutManager(getActivity());
       rankingList.setHasFixedSize(true);
       //cunku firebase in orderbychild methodu asekron sıramlama yapıcak
        //recycler data tersine ihtiyacım var

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);



          updateScore(Common.currentUser.getUserName(), new RankingCallBack<Ranking>() {
              @Override
              public void callBack(Ranking ranking) {
                  rankingTb1.child(ranking.getUserName()).setValue(ranking);
                 // showRanking();
              }

          });
          adapter=new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(

                  Ranking.class,
                  R.layout.layout_ranking,
                  RankingViewHolder.class,
                  rankingTb1.orderByChild("score")
          ) {

              @Override
              protected void populateViewHolder(RankingViewHolder viewHolder, Ranking model, int position) {
                 viewHolder.txt_name.setText(model.getUserName());
                 viewHolder.txt_score.setText(String.valueOf(model.getScore()));

                 viewHolder.setItemClickListener(new ItemClickListener() {
                     @Override
                     public void onClick(View view, int position, boolean isLongClick) {

                     }
                 });
              }
          };
          adapter.notifyDataSetChanged();
          rankingList.setAdapter(adapter);

        return myfragment;
    }


    private void updateScore(final String userName, final RankingCallBack<Ranking> callback) {
        questionScore.orderByChild("user").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    QuestionScore ques= data.getValue(QuestionScore.class);
                    sum +=Integer.parseInt(ques.getScore());
                    //tum scoreları topladık.toplama islermi buraya gelicek.Firebase asekron veritabanı

                }
                Ranking ranking=new Ranking(userName,sum);
                callback.callBack(ranking);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
