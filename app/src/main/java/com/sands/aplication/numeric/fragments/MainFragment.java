/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.sands.aplication.numeric.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.sands.aplication.numeric.R;
import com.sands.aplication.numeric.fragments.tableview.TableViewAdapter;
import com.sands.aplication.numeric.fragments.tableview.TableViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    private TableView mTableView;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_table_view_main, container, false);
        mTableView = layout.findViewById(R.id.tableview);
        initializeTableView();
        return layout;
    }

    private void initializeTableView() {
        TableViewModel mTableViewModel = new TableViewModel();
        AbstractTableAdapter<com.sands.aplication.numeric.fragments.tableview.model.ColumnHeader, com.sands.aplication.numeric.fragments.tableview.model.RowHeader, com.sands.aplication.numeric.fragments.tableview.model.Cell> mTableViewAdapter = new TableViewAdapter(getContext());
        mTableView.setAdapter(mTableViewAdapter);
        mTableViewAdapter.setAllItems(mTableViewModel.getColumnHeaderList(), mTableViewModel
                .getRowHeaderList(), mTableViewModel.getCellList());
    }
}
