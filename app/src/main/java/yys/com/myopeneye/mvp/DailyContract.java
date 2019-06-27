package yys.com.myopeneye.mvp;

import java.util.List;

import yys.com.myopeneye.data.model.DailyEntity.IssueEntity.ItemListEntity;

/**
 * Created by yangys on 2019/3/1.
 */

public class DailyContract {

    public interface View{
        public void setPresenter(Presenter presenter);

        public void updateData(int lastsize,List<ItemListEntity> data);

        public void onError();

        public void setLodingIndicator(boolean isshow);

    }

    public interface Presenter{

        public void setView(View view);

        public void getDailyData();

        public void nextPageData();
    }

}
