package com.example.sacrew.numericov4.dynamicTable;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class DynamicTable{
    private TableLayout tableLayout;
    private Context context;
    private String[] header;
    private ArrayList<String[]> data;
    private TableRow tableRow;
    private TextView txtCell;
    private int indexC;
    private int indexR;

    public DynamicTable(TableLayout tableLayout, Context context){
        this.tableLayout=tableLayout;
        this.context=context;
    }

    public void addHeader(String[]header){
        this.header=header;
        createHeader();
    }

    public void addData(ArrayList<String[]>data){
        this.data=data;
        createDataTable();
    }

    private void newRow(){
        tableRow = new TableRow(context);
    }

    private void newCell(){
        txtCell = new TextView(context);
        txtCell.setGravity(Gravity.CENTER);
        txtCell.setTextSize(20);

    }

    public void backgroundHeader(int color){
        indexC = 0;
        while(indexC < header.length){
            txtCell = getCell(0, indexC++);
            txtCell.setBackgroundColor(color); 
        }
    }

    public void backgroundData(int color){
        String info;
        for(indexR=1; indexR <= header.length; indexR++){
            for(indexC=0; indexC < header.length; indexC++){
                txtCell = getCell(indexR, indexC);
                txtCell.setBackgroundColor(color);
            }
        }
    }

    public void lineColor(int color){
        indexR = 0;
        while(indexR < data.size()){
            getRow(indexR++).setBackgroundColor(color);
        }
    }

    public void textColorData(int color){
        for(indexR = 1; indexR <= header.length; indexR++){
            for(indexC = 0; indexC < header.length; indexC++){
                getCell(indexR, indexC).setTextColor(color);
            }
        }
    }

    public void textColorHeader(int color){
        indexC = 0;
        while(indexC < header.length){
            getCell(0, indexC++).setTextColor(color);
        }
    }

    private TableRow getRow(int index){
        return (TableRow) tableLayout.getChildAt(index);
    }

    private TextView getCell(int rowIndex, int columIndex){
        tableRow = getRow(rowIndex);
        return (TextView) tableRow.getChildAt(columIndex);
    }

    private void createHeader(){
        indexC = 0;
        newRow();
        while(indexC < header.length){
            newCell();
            txtCell.setText(header[indexC++]);
            tableRow.addView(txtCell, newTableRowParams());
        }
        tableLayout.addView(tableRow);
    }

    private void createDataTable(){
        String info;
        for(indexR=1; indexR <= header.length; indexR++){
            newRow();
            for(indexC=0; indexC < header.length; indexC++){
                newCell();
                String[] colums = data.get(indexR - 1);
                info=(indexC<colums.length)?colums[indexC]:"";
                txtCell.setText(info);
                tableRow.addView(txtCell, newTableRowParams());
            }
            tableLayout.addView(tableRow);
        }
    }

    private TableRow.LayoutParams newTableRowParams(){
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(1, 1, 1, 1);
        params.weight = 1;
        return params;
    }
}