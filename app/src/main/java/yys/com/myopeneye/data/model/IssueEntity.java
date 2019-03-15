package yys.com.myopeneye.data.model;

import java.util.List;

/**
 * Created by yangys on 2019/3/5.
 */

public class IssueEntity {

    private long publishTime;

    private int count;

    private List<ItemListEntity> itemList;

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ItemListEntity> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemListEntity> itemList) {
        this.itemList = itemList;
    }
}
