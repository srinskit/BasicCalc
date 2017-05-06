package es.hol.srinag.basiccalc;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {
    private TextView ipTextBox,opTextBox;
    private HorizontalScrollView ipScroll,opScroll;
    private boolean showingOp;
    private double ans;
    private Resources res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Adjust scrollable button heights after rendering view
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                adjustScrollButtons();
            }
        });
        ipTextBox = (TextView)findViewById(R.id.ip_disp);
        opTextBox = (TextView)findViewById(R.id.op_disp);
        ipScroll = (HorizontalScrollView)findViewById(R.id.ip_scroll);
        opScroll = (HorizontalScrollView)findViewById(R.id.op_scroll);
        showingOp = false;
        ans = 0;
        res = getResources();
    }
    private void adjustScrollButtons(){
        int height = (findViewById(R.id.b_openparen)).getHeight();
        final int sbs[] = {R.id.sb_0,R.id.sb_1,R.id.sb_2,R.id.sb_3,R.id.sb_4,R.id.sb_5,R.id.sb_6,R.id.sb_7,R.id.sb_8};
        int width = (findViewById(sbs[0])).getWidth();
        for(int id : sbs) {
            Button sb = (Button) findViewById(id);
            sb.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        }
    }
    private void ipScrollRight(){
        ipScroll.post(new Runnable() {
            @Override
            public void run() {
                ipScroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });
    }
    private void opScrollRight(){
        opScroll.post(new Runnable() {
            @Override
            public void run() {
                opScroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });
    }
    public void numPressed(View view){
        if(showingOp){
            opTextBox.setText("");
            ipTextBox.setText("");
            showingOp = false;
        }
        opTextBox.append (((Button)view).getText().toString());
        opScrollRight();
    }
    public void opPressed(View view){
        if(showingOp){
            ipTextBox.setText("");
            showingOp = false;
        }
        ipTextBox.append(opTextBox.getText().toString()+((Button)view).getText().toString());
        ipScrollRight();
        opTextBox.setText("");
    }
    public void delPressed(View view){
        if(showingOp){
            opTextBox.setText("");
            showingOp = false;
        }
        String ip = ipTextBox.getText().toString(),op = opTextBox.getText().toString();
        if(op.length()>0){
            opTextBox.setText(op.substring(0,op.length()-1));
        }
        else if(ip.length()>0){
            ipTextBox.setText(ip.substring(0,ip.length()-1));
        }
    }
    public void clrPressed(View view){
        ipTextBox.setText("");
        opTextBox.setText("");
        showingOp=false;
    }
    public String adjustInput(String in){
        //make ip string Exp4j compatible
        in = in.replaceAll(res.getString(R.string.op_multiply).toString(),"*");
        in = in.replaceAll(res.getString(R.string.op_multiply).toString(),"sqrt");
        in = in.replaceAll(res.getString(R.string.op_multiply).toString(),Double.toString(ans));
        return in;
    }
    public void eval(View view){
        ipTextBox.append(opTextBox.getText().toString());
        ipScrollRight();
        String in = ipTextBox.getText().toString();
        in = adjustInput(in);
        try{
            Expression expression = new ExpressionBuilder(in).build();
            ans = expression.evaluate();
            if(ans==Math.floor(ans)){
                opTextBox.setText(Integer.toString((int)ans));
            }
            else {
                opTextBox.setText(Double.toString(ans));
            }
        }
        catch(Exception E) {
            opTextBox.setText(getResources().getString(R.string.error_message));
        }
        showingOp = true;
    }
}
