package com.example.motorizadoyego.View.ui.cerrarSesion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.example.motorizadoyego.R;

import org.json.JSONObject;

public class CerrarSesionFragment extends Fragment {

    private TextView mTextView;

    private Index index;

    private CompletionHandler completionHandler;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_send, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextView=view.findViewById(R.id.textView);
        Client client = new Client("3Y4G1MKC6Q", "c7929e113fd34b58dc374e0608cbfb66");
        index = client.getIndex("yegosearch");

        completionHandler = (content, error) -> {
           mTextView.setText(content.toString());
        };



        searchOrder(view);

    }

    private void searchOrder(View view) {

        // Associate searchable configuration with the SearchView
        //SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = view.findViewById(R.id.fragment_historial_searchview);
        // .getActionView();
        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                //adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                //adapter.getFilter().filter(query);
                index.searchAsync(new Query(query), completionHandler);
                return false;
            }
        });
    }
}