package com.example.xiaojun.gonganposji.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import com.example.xiaojun.gonganposji.R;


/**
 * @Function: 自定义对话框
 * @Date: 2013-10-28
 * @Time: 下午12:37:43
 * @author Tom.Cai
 */
public class XiuGaiJiuDianDialog extends Dialog {
   // private TextView title2;
    private Button l1,l2;
    private EditText ipip,idid,zhujidizhi;
    public XiuGaiJiuDianDialog(Context context) {
        super(context, R.style.dialog_style2);
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.xiugaidialog3, null);

        ipip= (EditText) mView.findViewById(R.id.shexiangtou);
        idid= (EditText)mView.findViewById(R.id.idid);
        zhujidizhi= (EditText)mView.findViewById(R.id.zhuji);
       // title2= (TextView) mView.findViewById(R.id.title2);
        l1= (Button)mView. findViewById(R.id.queren);
        l2= (Button) mView.findViewById(R.id.quxiao);

        super.setContentView(mView);
    }

    public void setContents(String id, String ip,String zhuji){

        idid.setText(id);
        ipip.setText(ip);
        zhujidizhi.setText(zhuji);
    }

    public JiuDianBean getJiuDianBean(){
        JiuDianBean dianBean=new JiuDianBean();
        dianBean.setId(idid.getText().toString().trim());
        dianBean.setIp(ipip.getText().toString().trim());
        dianBean.setZhuji(zhujidizhi.getText().toString().trim());
        return dianBean;

    }



    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {
    }

    /**
     * 确定键监听器
     * @param listener
     */
    public void setOnQueRenListener(View.OnClickListener listener){
        l1.setOnClickListener(listener);
    }
    /**
     * 取消键监听器
     * @param listener
     */
    public void setQuXiaoListener(View.OnClickListener listener){
        l2.setOnClickListener(listener);
    }


}
