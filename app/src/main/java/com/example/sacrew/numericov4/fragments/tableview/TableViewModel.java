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

package com.example.sacrew.numericov4.fragments.tableview;

import android.content.Context;

import com.example.sacrew.numericov4.fragments.tableview.model.Cell;
import com.example.sacrew.numericov4.fragments.tableview.model.ColumnHeader;
import com.example.sacrew.numericov4.fragments.tableview.model.RowHeader;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by evrencoskun on 4.02.2018.
 */

public class TableViewModel {

    private Context mContext;

    public TableViewModel(Context context) {
        mContext = context;

    }

    private List<RowHeader> getSimpleRowHeaderList() {
        List<RowHeader> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            RowHeader header = new RowHeader(String.valueOf(i),  ""+i);
            list.add(header);
        }

        return list;
    }


    private List<ColumnHeader> getSimpleColumnHeaderList() {
        List<ColumnHeader> list = new ArrayList<>();
        ArrayList<String> lista = new ArrayList<String>();
        for(int i = 0; i < 20; i++){
            lista.add("column #"+i);
        }

        for (int i = 0; i < lista.size(); i++) {
            String title = lista.get(i);
            ColumnHeader header = new ColumnHeader(String.valueOf(i), title);
            list.add(header);
        }


        return list;
    }

    private List<List<Cell>> getSimpleCellList() {
        List<List<Cell>> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            List<Cell> cellList = new ArrayList<>();

            for (int j = 0; j < 20; j++) {
                Object text = "213434843894389";
                String id = j + "-" + i;

                Cell cell = new Cell(id, text);
                cellList.add(cell);
            }
            list.add(cellList);
        }

        return list;
    }

    public List<List<Cell>> getCellList() {
        return getSimpleCellList();
    }

    public List<RowHeader> getRowHeaderList() {
        return getSimpleRowHeaderList();
    }

    public List<ColumnHeader> getColumnHeaderList() {
        return getSimpleColumnHeaderList();
    }


}
