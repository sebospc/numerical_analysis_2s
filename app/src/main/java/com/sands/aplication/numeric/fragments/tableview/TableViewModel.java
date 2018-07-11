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

package com.sands.aplication.numeric.fragments.tableview;

import com.sands.aplication.numeric.fragments.tableview.model.Cell;
import com.sands.aplication.numeric.fragments.tableview.model.ColumnHeader;
import com.sands.aplication.numeric.fragments.tableview.model.RowHeader;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by evrencoskun on 4.02.2018.
 */

public class TableViewModel {

    private static List<String> listaTitulos = new LinkedList<>();
    private static List<List<String>> listaCeldas = new LinkedList<>();

    public TableViewModel() {

    }

    public static List<String> getTitles(List<String> lista) {
        listaTitulos = lista;
        return lista;
    }

    public static void getCeldas(List<List<String>> lista) {
        listaCeldas = lista;
    }


    private List<RowHeader> getSimpleRowHeaderList() {
        List<RowHeader> list = new LinkedList<>();
        for (int i = 0; i < listaCeldas.size(); i++) {
            RowHeader header = new RowHeader(String.valueOf(i), "" + i);
            list.add(header);
        }

        return list;
    }


    private List<ColumnHeader> getSimpleColumnHeaderList() {
        List<ColumnHeader> list = new LinkedList<>();
        for (int i = 0; i < getTitles(listaTitulos).size(); i++) {
            String title = getTitles(listaTitulos).get(i);
            ColumnHeader header = new ColumnHeader(String.valueOf(i), title);
            list.add(header);
        }


        return list;
    }

    private List<List<Cell>> getSimpleCellList() {
        List<List<Cell>> list = new LinkedList<>();

        for (int i = 0; i < listaCeldas.size(); i++) {
            LinkedList cellList = new LinkedList();
            List<String> aux = listaCeldas.get(i);
            for (int j = 0; j < aux.size(); j++) {
                Object text = aux.get(j);
                Cell cell = new Cell(j + "-" + i, text);
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
