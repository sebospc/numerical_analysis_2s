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
    public static ArrayList<String> listaOriginal = new ArrayList<String>();
    public static ArrayList<String> listaCeldas = new ArrayList<String>();

    public TableViewModel(Context context) {
        mContext = context;

    }

    public static ArrayList<String> getTitles(ArrayList<String> lista){
        listaOriginal = lista;
        return lista;
    }

    public static ArrayList<String> getCeldas(ArrayList<String> lista){
        listaCeldas = lista;
        return lista;
    }

    public List<RowHeader> getSimpleRowHeaderList() {
        List<RowHeader> list = new ArrayList<>();
        for (int i = 0;i< 10+1; i++) {
            RowHeader header = new RowHeader(String.valueOf(i),  ""+i);
            list.add(header);
        }
        return list;
    }


    public List<ColumnHeader> getSimpleColumnHeaderList() {
        List<ColumnHeader> list = new ArrayList<>();
        for (int i = 0; i < getTitles(listaOriginal).size(); i++) {
            String title = getTitles(listaOriginal).get(i);
            ColumnHeader header = new ColumnHeader(String.valueOf(i), title);
            list.add(header);
        }


        return list;
    }

    public List<List<Cell>> getSimpleCellList() {
        List<List<Cell>> list = new ArrayList<>();
        for (int i = 0; i < getCeldas(listaCeldas).size(); i++) {
            List<Cell> cellList = new ArrayList<>();
            Object text = getCeldas(listaCeldas).get(i);
            String id = i + "-" + i;

            Cell cell = new Cell(id, text);
            cellList.add(cell);
            for (int j = 0; j < getTitles(listaOriginal).size(); j++) {

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
