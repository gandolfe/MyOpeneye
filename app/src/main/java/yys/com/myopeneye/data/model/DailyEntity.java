package yys.com.myopeneye.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yangys on 2019/3/5.
 */

public class DailyEntity {
    private String nextPageUrl;

    private List<IssueEntity> issueList;

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public List<IssueEntity> getIssueList() {
        return issueList;
    }

    public void setIssueList(List<IssueEntity> issueList) {
        this.issueList = issueList;
    }

    public static class IssueEntity {

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

        public static class ItemListEntity {

            private String type;
            private ItemDataEntity data;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public ItemDataEntity getData() {
                return data;
            }

            public void setData(ItemDataEntity data) {
                this.data = data;
            }


            /**
             * 数据内部类
             */
            public static class ItemDataEntity{
                private String title;
                private String description;
                private AuthorEntity author;
                private String playUrl;

                /**
                 * 类型描述
                 */
                private String category;

                private Cover cover;

                public String getCategory() {
                    return category;
                }

                public void setCategory(String category) {
                    this.category = category;
                }

                public ItemListEntity.Cover getCover() {
                    return cover;
                }

                public void setCover(ItemListEntity.Cover cover) {
                    this.cover = cover;
                }

                /**
                 * 播放时长
                 */
                private int duration;

                /**
                 * banner类型的data才有的背景图片
                 */
                private String image;

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public AuthorEntity getAuthor() {
                    return author;
                }

                public void setAuthor(AuthorEntity author) {
                    this.author = author;
                }

                public String getPlayUrl() {
                    return playUrl;
                }

                public void setPlayUrl(String playUrl) {
                    this.playUrl = playUrl;
                }

                public int getDuration() {
                    return duration;
                }

                public void setDuration(int duration) {
                    this.duration = duration;
                }
            }


            /**
             * 背景图内部类（video独有）
             */
            public static class Cover{
                private String feed;

                public String getFeed() {
                    return feed;
                }

                public void setFeed(String feed) {
                    this.feed = feed;
                }
            }


            /**
             * 作者内部类
             */
            public static class AuthorEntity{
                private String icon;
                private String name;
                private String description;
                private int videoNum;

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public int getVideoNum() {
                    return videoNum;
                }

                public void setVideoNum(int videoNum) {
                    this.videoNum = videoNum;
                }
            }
    }
    }
}
