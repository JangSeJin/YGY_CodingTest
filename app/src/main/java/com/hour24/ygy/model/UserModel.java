package com.hour24.ygy.model;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

@Data
public class UserModel implements Serializable {

    private int total_count;
    private boolean incomplete_results;
    public ArrayList<Item> items;

    @Data
    public class Item {
        public String login;
        private int id;
        private String node_id;
        public String avatar_url;
        private String gravatar_id;
        private String url;
        private String html_url;
        private String followers_url;
        private String following_url;
        private String gists_url;
        private String starred_url;
        private String subscriptions_url;
        private String organizations_url;
        private String repos_url;
        private String events_url;
        private String received_events_url;
        private String type;
        private boolean site_admin;
        public double score;

        public boolean isLike;
    }


}
