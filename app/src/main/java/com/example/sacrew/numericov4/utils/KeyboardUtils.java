package com.example.sacrew.numericov4.utils;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.sacrew.numericov4.R;


public class KeyboardUtils {
    private View view;
    public KeyboardView keyboardView;
    private Keyboard key123,keyFunctions;
    private EditText actualEditText;
    private LinearLayout tabLayoutKeyboard;
    public boolean isUp = false;
    private final short xLetter = 967;
    private final short pi = 960;
    private final short euler = 8495;
    private final short multipier = 215;
    private final short div = 247;
    private final short xSquareTwo = 178;
    private final short xSquareAny = 120;
    private final short xSquareRoot = 8730;
    private final short plus = 727;
    private final short minus = 726;
    private final short leftParent = 40;
    private final short rightParent = 41;
    private final short absolute = 9116;
    private final short left = 706;
    private final short right = 707;
    private final short cos = -1;
    private final short sin = -2;
    private final short tan = -3;
    private final short dot = 803;
    private final short removeToLeft = 9003;
    private final short cot = -4;
    private final short sec = -5;
    private final short csc = -6;
    private final short ln = -12;
    private final short cosInverse = -7;
    private final short sinInverse = -8;
    private final short tanInverse = -9;
    private final short cotInverse = -10;
    private final short secInverse = -11;
    private final short cosh = -13;
    private final short sinh = -14;
    private final short tanh = -15;
    private final short coth = -16;
    private final short sech = -17;
    private final short csch = -18;
    private final short logTenBase = -19;
    private final short logAnyBase = -20;
    private final short tenPowerAny = -21;


    public KeyboardUtils(View view, int keyboardViewId, Context context){
        this.view = view;
        this.keyboardView = view.findViewById(keyboardViewId);
        key123 = new Keyboard(context,R.layout.keyboard_123);
        keyFunctions = new Keyboard(context,R.layout.default_functions_keyboard);
        tabLayoutKeyboard = view.findViewById(R.id.tabKeyboard);
        tabLayoutKeyboard.setVisibility(View.GONE);
        tabLayoutKeyboard.setEnabled(false);
        keyboardView.setOnKeyboardActionListener(keyboardActionListener);
        keyboardView.setPreviewEnabled(false);
        defaultConfig();

    }

    private void defaultConfig(){
        view.post(new Runnable() {
            @Override
            public void run() {
                TabLayout tabLayout = view.findViewById(R.id.tabLayout);

                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        switch (tab.getPosition()){
                            case 0: keyboardView.setKeyboard(key123); break;
                            case 1: keyboardView.setKeyboard(keyFunctions); break;
                            default: break;
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }
        });
    }

    public void registerEdittext(EditText aux,Context context, Activity activity ){
        keyboardView.setKeyboard(key123);
        aux.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        aux.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showKeyBoard();
                    closeKeyboard(context,((EditText)v).getWindowToken());
                    actualEditText = (EditText)v;
                } else {
                    closeInternalKeyboard();
                    actualEditText = null;
                }
            }
        });
        aux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyBoard();
                closeKeyboard(context,((EditText)v).getWindowToken());
            }
        });
        //aux.setInputType(InputType.TYPE_NULL);
        //aux.setTextIsSelectable(true);
        aux.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        });


    }

    private void showKeyBoard(){
        keyboardView.setVisibility(View.VISIBLE);
        keyboardView.setEnabled(true);
        tabLayoutKeyboard.setVisibility(View.VISIBLE);
        tabLayoutKeyboard.setEnabled(true);
        isUp = true;
    }
    public void closeInternalKeyboard(){
        keyboardView.setVisibility(View.GONE);
        keyboardView.setEnabled(false);
        tabLayoutKeyboard.setVisibility(View.GONE);
        tabLayoutKeyboard.setEnabled(false);
        isUp= false;
    }
    private void closeKeyboard(Context context, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }
    private KeyboardView.OnKeyboardActionListener keyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {
            System.out.println("xddd");
            switch (primaryCode){
                case removeToLeft: deleteLast(actualEditText,1);
                default: break;
            }
        }
        @Override
        public void onRelease(int primaryCode) { }
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {

            if( actualEditText==null) return;
            switch (primaryCode){
                case xLetter:  actualEditText.getText().insert(actualEditText.getSelectionStart(),"x");  break;
                case pi: actualEditText.getText().insert(actualEditText.getSelectionStart(),"pi");  break;
                case euler: actualEditText.getText().insert(actualEditText.getSelectionStart(),"e");  break;
                case 9: actualEditText.getText().insert(actualEditText.getSelectionStart(),"9");  break;
                case 8: actualEditText.getText().insert(actualEditText.getSelectionStart(),"8");  break;
                case 7: actualEditText.getText().insert(actualEditText.getSelectionStart(),"7");  break;
                case multipier: actualEditText.getText().insert(actualEditText.getSelectionStart(),"*");  break;
                case div: actualEditText.getText().insert(actualEditText.getSelectionStart(),"/");  break;
                case xSquareTwo: actualEditText.getText().insert(actualEditText.getSelectionStart(),"^(2)");  break;
                case xSquareAny: actualEditText.getText().insert(actualEditText.getSelectionStart(),"^()"); actualEditText.setSelection(actualEditText.getSelectionStart()-1);  break;
                case xSquareRoot: checkLeftParent(actualEditText,"sqrt");break;
                case 6: actualEditText.getText().insert(actualEditText.getSelectionStart(),"6");  break;
                case 5: actualEditText.getText().insert(actualEditText.getSelectionStart(),"5");  break;
                case 4: actualEditText.getText().insert(actualEditText.getSelectionStart(),"4");  break;
                case plus: actualEditText.getText().insert(actualEditText.getSelectionStart(),"+");  break;
                case minus: actualEditText.getText().insert(actualEditText.getSelectionStart(),"-");  break;
                case leftParent: checkLeftParent(actualEditText,"");break;
                case rightParent: checkRightParent(actualEditText);break;
                case 3: actualEditText.getText().insert(actualEditText.getSelectionStart(),"3");  break;
                case 2: actualEditText.getText().insert(actualEditText.getSelectionStart(),"2");  break;
                case 1: actualEditText.getText().insert(actualEditText.getSelectionStart(),"1");  break;
                case left: moveCursorLeft(actualEditText);break;
                case right: moveCursorRight(actualEditText);break;
                case cos: checkLeftParent(actualEditText,"cos");break;
                case sin: checkLeftParent(actualEditText,"sin");break;
                case tan: checkLeftParent(actualEditText,"tan");break;
                case absolute: checkLeftParent(actualEditText,"abs");break;
                case 0: actualEditText.getText().insert(actualEditText.getSelectionStart(),"0");  break;
                case dot: actualEditText.getText().insert(actualEditText.getSelectionStart(),".");  break;
                case cot: checkLeftParent(actualEditText,"cot");break;
                case sec: checkLeftParent(actualEditText,"sec");break;
                case csc: checkLeftParent(actualEditText,"csc");break;
                case ln: checkLeftParent(actualEditText,"ln");break;
                case cosInverse: checkLeftParent(actualEditText,"acos");break;
                case sinInverse: checkLeftParent(actualEditText,"asin");break;
                case tanInverse: checkLeftParent(actualEditText,"atan");break;
                case cotInverse: checkLeftParent(actualEditText,"acot");break;
                case secInverse: checkLeftParent(actualEditText,"asec");break;
                case cosh: checkLeftParent(actualEditText,"cosh");break;
                case sinh: checkLeftParent(actualEditText,"sinh");break;
                case coth: checkLeftParent(actualEditText,"coth");break;
                case tanh: checkLeftParent(actualEditText,"tanh");break;
                case sech: checkLeftParent(actualEditText,"sech");break;
                case csch: checkLeftParent(actualEditText,"csch");break;
                case logTenBase: checkLeftParent(actualEditText,"log10");break;
                case logAnyBase: checkLeftParent(actualEditText,"log");actualEditText.setSelection(actualEditText.getSelectionStart()-1);  break;
                case tenPowerAny: actualEditText.getText().insert(actualEditText.getSelectionStart(),"E");  break;
                default: break;
            }

        }
        @Override
        public void onText(CharSequence text) { }
        @Override
        public void swipeLeft() { }
        @Override
        public void swipeRight() { }
        @Override
        public void swipeDown() { }
        @Override
        public void swipeUp() { }


    };

    private void moveCursorLeft(EditText edit){
        if(edit.getSelectionStart()>0){
            System.out.println(" cursor "+edit.getSelectionStart());
            edit.setSelection(edit.getSelectionStart()-1);
        }
    }
    //
    private void moveCursorRight(EditText edit){
        System.out.println("right "+edit.getSelectionEnd());
        if(edit.getSelectionEnd()<edit.getText().toString().length()){
            edit.setSelection(edit.getSelectionEnd()+1);
        }
    }
    private void checkRightParent(EditText edit){


        String g = edit.getText().toString();
        int lastParent = 0;
        if(edit.getSelectionStart()>0){

            int i = edit.getSelectionEnd()-1;
            int contPar = 0;
            if(i == -1) i = 0;
            for(;i>= 0;i--){
                if(g.charAt(i) == ')'){
                    contPar++;
                }
                if(g.charAt(i) == '('){
                    contPar--;
                    if(contPar < 0){
                        i++;
                        break;
                    }
                }
                if(contPar == 0){
                    lastParent = i+1;
                }
            }
        }
        lastParent--;
        if(lastParent == -1) lastParent = edit.getSelectionStart()-1;
        System.out.println(lastParent);
        edit.getText().insert(lastParent,"(");
        edit.getText().insert(edit.getSelectionStart(), ")");
    }
    private void checkLeftParent(EditText edit,String character){
        String g = edit.getText().toString();

        if(edit.getSelectionEnd()<g.length()){
            int i = edit.getSelectionEnd();
            int contPar = 0;
            int lastParent = -1;
            for(;i<g.length();i++){
                System.out.println("searching )");
                if(g.charAt(i) == '('){
                    contPar++;
                }
                if(g.charAt(i) == ')'){
                    contPar--;
                    if(contPar < 0){
                        i++;
                        break;
                    }
                }
                if(contPar == 0){
                    lastParent = i+1;
                }
            }
            if(lastParent <= 0) lastParent = edit.getSelectionStart();
            edit.getText().insert(edit.getSelectionStart(),character+"(");
            edit.getText().insert(lastParent+1+character.length(),")");
        }else{
            edit.getText().insert(edit.getSelectionStart(),character+"()");
            edit.setSelection(edit.getSelectionStart()-1);
        }
    }
    public void deleteLast(EditText edit,int coef){
        if(edit.getSelectionStart()  >0) {
            StringBuilder text = new StringBuilder(edit.getText().toString());
            int aux = edit.getSelectionStart() - coef;
            text.deleteCharAt(aux);
            //edit.setText(text.subSequence(0, aux) );
            edit.setText(text);
            edit.setSelection(aux);

        }
    }
}
