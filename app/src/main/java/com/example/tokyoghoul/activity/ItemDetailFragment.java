package com.example.tokyoghoul.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tokyoghoul.R;
import com.example.tokyoghoul.activity.dummy.DummyContent;

import br.tiagohm.markdownview.MarkdownView;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;
    private boolean isUrl;
    private int id;
    private String title;
    private String detail;
    private MarkdownView markdownView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.DummyItem.class;

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

            Bundle extras = activity.getIntent().getExtras();
            if(extras != null){
                detail = extras.getString("detail");
                id = extras.getInt("id");
                title = extras.getString("title");
                isUrl = extras.getBoolean("isUrl");
            }
            if (appBarLayout != null) {
                appBarLayout.setTitle(title);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        markdownView = rootView.findViewById(R.id.item_detail);
        if(isUrl){
            //markdownView.loadMarkdownFromUrl(detail);
            markdownView.loadMarkdownFromUrl("https://raw.githubusercontent.com/G-eto/TokyoGhoul/master/README.md");
        }
        else{
            //markdownView.loadMarkdownFromUrl("https://raw.githubusercontent.com/G-eto/TokyoGhoul/master/README.md");
            markdownView.loadMarkdown(detail);
        }
        return rootView;
    }
}
