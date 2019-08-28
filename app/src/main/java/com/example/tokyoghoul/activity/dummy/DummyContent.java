package com.example.tokyoghoul.activity.dummy;

import com.example.tokyoghoul.database.model.CDKs;
import com.example.tokyoghoul.database.model.Psp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.tokyoghoul.activity.ItemListActivity.adapter;
import static com.example.tokyoghoul.activity.ItemListActivity.recyclerView;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 * report: fine
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    private static boolean visitFlag = false;

    public synchronized static boolean isVisit(){
        if(visitFlag){
            return true;
        }
        //recyclerView.getAdapter().notifyDataSetChanged();
        return false;
    }
    public static void setVisitFlag(boolean is){
//        if(!is)
//            adapter.notifyDataSetChanged();
        visitFlag = is;
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String title;
        public final String kind;
        public final String under_1;
        public final String under_2;
        public final String detail;
        public int web_id = 0;
        public int web_edition = 0;


        public DummyItem(String id, String cdk, String content, String date) {
            this.id = id;
            this.title = cdk;
            this.kind = "cdk";
            this.under_1 = content;
            this.under_2 = date;
            this.detail = "";
        }

        public DummyItem(String id, String title, String kind, String under_1, String under_2, String detail) {
            this.id = id;
            this.title = title;
            this.kind = kind;
            this.under_1 = under_1;
            this.under_2 = under_2;
            this.detail = detail;
        }
        public DummyItem(String id, String title, String kind,
                         String under_1, String under_2,
                         String detail, int web_id, int web_edition) {
            this.id = id;
            this.title = title;
            this.kind = kind;
            this.under_1 = under_1;
            this.under_2 = under_2;
            this.detail = detail;
            this.web_id = web_id;
            this.web_edition = web_edition;
        }

        public Psp toPsp(){
            return new Psp(title, kind, under_2, under_1, detail, web_id, web_edition);
        }

        public CDKs toCDK(){
            return new CDKs(Integer.parseInt(id), title, under_1, under_2);
        }

        @Override
        public String toString() {
            if(this.under_1 != null)
                return this.title + " by " + this.under_1;
            return this.title;
        }
    }
}