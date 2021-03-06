package fragment;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a123456.hebcartzhonggu.CartModelActivity;
import com.example.a123456.hebcartzhonggu.CartFenLei;
import com.example.a123456.hebcartzhonggu.MySerchActvity;
import com.example.a123456.hebcartzhonggu.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bean.BeanFlag;
import bean.JaShiZhengBean;
import bean.ModelNameandID;
import bean.MyNewUpdate;
import bean.UserBean;
import bean.ZHFBean;
import bean.ZQBean;
import bean.ZQFBean;
import camera.CameraActivity;
import camera.FileUtil;
import jiekou.getInterface;
import utils.MyModelDialog;
import utils.MySuccess;
import utils.Mydialog;
import View.GetJsonUtils;
import View.CommonPopupWindow;
import utils.NameAndTelDialog;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class newFragment extends Fragment implements View.OnClickListener{
    private ImageView img_paizhao;
    private EditText edit_num;
    private View view;
    private PopupWindow window;
    private View popView,BrandPopView,SerisePopView;
    private TextView tv_paizhao,tv_canle,tv_xiangce;
    private ImageView img_topleft,img_topright;
    private TextView tv_topcenter;
    private TextView tv_time;//注册日期
    private EditText edt_licheng,edt_price,tv_tel;
            EditText edt_name;//里程，价格,联系电话
    private ImageView img_newfragment,img2_newfragment,img3_newfragment;
    private Button btn_commit;
    private LinearLayout linear3_newfragment,linear_nameandtel;
    private ArrayAdapter arrayAdapter;
    private LinearLayout linear_celiang;
    private String serise_id,model_id;
    private TextView tv_quyue,tv_cartFenlei;//tv_cartFenlei 车辆分类
    CommonPopupWindow window2;
    private TextView tv_cartmodel;
    List imgListPath=new ArrayList();
    Mydialog mydialog;
    String zqfPath,zqPath,zhfPath;
    String quyuID,brandid,modelid,seriesid,cartName,fenleiID;//商家信息ID,品牌ID，车系ID,车型,分类
    MySuccess mySuccess,mySuccess1;
    private TextView tv_getprice,tv_getmodel;
    Mydialog mydialog1;
    String quyuTelName;//车商信息对于的用户和电话
    boolean IsClean=false;
    String picID,currentID;//接收销售人员姓名和电话,当前ID
    private String picName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        view=inflater.inflate(R.layout.fragment_new, container, false);
        initView();
//        MyRegistReciver();
        mydialog1=new Mydialog(getContext(),"正在获取请稍后");
        mydialog=new Mydialog(getContext(),"正在上传.....");
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("TAG","hidden==="+hidden+"=====BeanFlag.Flag===="+BeanFlag.Flag);
        if(hidden){
            if(BeanFlag.Flag) {
                edt_licheng.setText("");
                edit_num.setText("");
                tv_tel.setText("");
                edt_name.setText("");
                edt_price.setText("");
                tv_quyue.setText("请选择车商信息");
                tv_time.setText("请选择日期");
                tv_cartmodel.setText("请选择品牌，车系和车型");
                tv_cartFenlei.setText("请选取车辆分类信息");
                edit_num.setFocusableInTouchMode(true);
                edit_num.setFocusable(true);
                edit_num.requestFocus();
                BeanFlag.Flag = false;
                zqfPath = "";
                zqPath = "";
                zhfPath = "";
                ZQFBean.zqpath = "";
                ZQBean.zqpath = "";
                ZHFBean.zhfpath = "";
                str = "";
                tv_cartFenlei.setBackgroundResource(R.drawable.juxingnull);
                img_newfragment.setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.zq45d));
                img2_newfragment.setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.zqf));
                img3_newfragment.setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.zhf));
            }
        }
//        getSubStr(edt_licheng);
//        getSubStr(edt_price);
        linear3_newfragment.setVisibility(View.VISIBLE);
    }

    private void initView() {
        mySuccess=new MySuccess(getContext(),"提交成功");
        mySuccess1=new MySuccess(getContext(),"修改成功");
        img_topleft=view.findViewById(R.id.img_left);
        img_topright=view.findViewById(R.id.img_right);
        tv_topcenter=view.findViewById(R.id.tv_center);
        tv_topcenter.setText("待补充车源");
        img_topleft.setVisibility(View.GONE);
        img_topright.setVisibility(View.GONE);
        linear_celiang=view.findViewById(R.id.linear_celiang);

        tv_getprice=view.findViewById(R.id.tv_getprice);
        tv_getprice.setOnClickListener(this);
        //所有要填写的控件
        //一下三个控件，当vin码识别识别，需要手动填写
        linear3_newfragment=view.findViewById(R.id.linear3_newfragment);
        tv_cartmodel=view.findViewById(R.id.tv_cartmodel);
        tv_time=view.findViewById(R.id.tv_time);//日期
//        linear_nameandtel=view.findViewById(R.id.linear_nameandtel);
        edt_name=view.findViewById(R.id.edt_name);
        tv_tel=view.findViewById(R.id.tv_tel);//联系电话
        edt_price=view.findViewById(R.id.edt_price);//价格
        edt_licheng=view.findViewById(R.id.edt_licheng);//里程
        tv_quyue=view.findViewById(R.id.tv_quyue);
        tv_cartFenlei=view.findViewById(R.id.tv_cartFenlei);
        //设置里程和价格的数据，小数点后为0的话不现实0
//        getSubStr(edt_price);
//        getSubStr(edt_licheng);
        img_newfragment=view.findViewById(R.id.img_newfragment);//左前45°
        img2_newfragment=view.findViewById(R.id.img2_newfragment);//正前
        img3_newfragment=view.findViewById(R.id.img3_newfragment);//正后

        img_paizhao=view.findViewById(R.id.img_paizhao);//vin拍照
        edit_num=view.findViewById(R.id.edt_vinnum);//vin码显示

        btn_commit=view.findViewById(R.id.btn_commit);//提交按钮

        img_paizhao.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        btn_commit.setOnClickListener(this);

        edit_num.addTextChangedListener(new MyEditTextChangeListener(edit_num));
        edt_licheng.addTextChangedListener(new MyEditTextChangeListener(edt_licheng));
        edt_price.addTextChangedListener(new MyEditTextChangeListener(edt_price));
        tv_tel.addTextChangedListener(new MyEditTextChangeListener(tv_tel));
        edt_name.addTextChangedListener(new MyEditTextChangeListener(edt_name));

        img_newfragment.setOnClickListener(this);
        img2_newfragment.setOnClickListener(this);
        img3_newfragment.setOnClickListener(this);
        tv_cartmodel.setOnClickListener(this);
        tv_quyue.setOnClickListener(this);
        tv_cartFenlei.setOnClickListener(this);
        edt_name.setOnClickListener(this);
        tv_getmodel=view.findViewById(R.id.tv_getmodel);
        tv_getmodel.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edt_name:
                edt_name.setText("");
//                if(BeanFlag.Flag){
//                    if(NameAndTel.NameAndTellist!=null){
//                        NameAndTelDialog nameAndTelDialog = new NameAndTelDialog(getContext(),NameAndTel.NameAndTellist.get(Integer.parseInt(currentID)));
//                        nameAndTelDialog.show();
//                    }
//                }
//                setName();
                break;
            case R.id.tv_getmodel:
                //获取车型车系车牌
                if (!TextUtils.isEmpty(edit_num.getText().toString())
                        && !TextUtils.isEmpty(tv_time.getText().toString())
                        && !tv_time.getText().toString().equals("请选择日期")) {
                    getPrice("model");
                } else {
                    Toast.makeText(getContext(), "vin码或注册日期不能为空", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tv_getprice:
                //获取价格
                if (!TextUtils.isEmpty(edit_num.getText().toString())
                        && !TextUtils.isEmpty(tv_time.getText().toString())
                        && !tv_time.getText().toString().equals("请选择日期")) {
                    getPrice("price");
                } else {
                    Toast.makeText(getContext(), "vin或者注册时间不能为空", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tv_quyue:
                Intent intentS = new Intent(getContext(), MySerchActvity.class);
//                intentS.putExtra("myserch","update");
                startActivity(intentS);
                break;
            case R.id.img_paizhao:
                getPopView();
                break;
            case R.id.tv_paizhao:
                if(setPermissions()) {
                    Intent intent = new Intent(getContext(), CameraActivity.class);
                    intent.putExtra("height", "70");
                    startActivity(intent);
                    window.dismiss();
                }
                break;
            case R.id.tv_xiangce:
                if (setPermissions()) {
                    Intent intent1 = new Intent(getContext(), CameraActivity.class);
                    int h=getActivity().getWindowManager().getDefaultDisplay().getHeight();
                    Log.e("TAG","h=="+h);
                    intent1.putExtra("height", "300");
                    startActivity(intent1);
                window.dismiss();
               }
                break;
            case R.id.tv_canle:
                if(window!=null&&window.isShowing()){
                    window.dismiss();
                }
                break;
            case R.id.tv_time:
                showDate(tv_time);
                break;
            case R.id.tv_cartFenlei:
                Intent intent=new Intent(getActivity(), CartFenLei.class);
                startActivity(intent);
                break;
            case R.id.btn_commit:
//                updateImag(zqfPath);
//                updateImag(zqPath);
//                updateImag(zhfPath);
                Log.e("TAG","点击提交");
//                getSubStr(edt_price);
//                getSubStr(edt_licheng);
                if (TextUtils.isEmpty(tv_quyue.getText().toString())||tv_quyue.getText().toString().trim().equals("请选择车商信息")){
                    tv_quyue.setBackgroundResource(R.drawable.rednull);
                    Toast.makeText(getContext(),"车商信息不能为空",Toast.LENGTH_LONG).show();
                }else if (!IsNullEdit(edit_num)||edit_num.getText().toString().length()!=17){
                    edit_num.setBackgroundResource(R.drawable.rednull);
                    Toast.makeText(getContext(),"VIN码不能为空并且只能为17位",Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(tv_time.getText().toString())||tv_time.getText().toString().trim().equals("请选择日期")) {
                    tv_time.setBackgroundResource(R.drawable.rednull);
                    Toast.makeText(getContext(),"注册日期不能为空",Toast.LENGTH_LONG).show();
                }
//                else if(!IsNullEdit(edt_name)){
//                    edt_name.setBackgroundResource(R.drawable.rednull);
//                    Toast.makeText(getContext(),"姓名不能为空",Toast.LENGTH_LONG).show();
//                }else if(!IsNullEdit(tv_tel)){
//                    tv_tel.setBackgroundResource(R.drawable.rednull);
//                    Toast.makeText(getContext(),"联系电话不能为空",Toast.LENGTH_LONG).show();
//                }
                else if(TextUtils.isEmpty(tv_cartmodel.getText().toString())||tv_cartmodel.getText().toString().trim().equals("请选择品牌，车系和车型")){
                    tv_cartmodel.setBackgroundResource(R.drawable.rednull);
                    Toast.makeText(getContext(),"品牌，车系，车型不能为空",Toast.LENGTH_LONG).show();
                }else if (TextUtils.isEmpty(tv_cartFenlei.getText().toString())||tv_cartFenlei.getText().toString().trim().equals("请选取车辆分类信息")) {
                    tv_cartFenlei.setBackgroundResource(R.drawable.rednull);
                    Toast.makeText(getContext(),"车辆分类信息不能为空",Toast.LENGTH_LONG).show();
                }else if(!IsNullEdit(edt_licheng)){
                    Toast.makeText(getContext(),"里程不能为空",Toast.LENGTH_LONG).show();
                }
                else if(!IsNullEdit(edt_price)){
                    Toast.makeText(getContext(),"价格不能为空",Toast.LENGTH_LONG).show();
                }

                else{
                    Log.e("TAG","BeanFlag.Flag=="+BeanFlag.Flag);
                     if(BeanFlag.Flag){
                         mydialog=new Mydialog(getContext(),"请稍等...");
                         Log.e("TAG","先上传图片===="+zqfPath);
                         Log.e("TAG","先上传图片"+(TextUtils.isEmpty(zqfPath)&&TextUtils.isEmpty(zqPath)&&TextUtils.isEmpty(zhfPath)));
                         if(TextUtils.isEmpty(zqfPath)&&TextUtils.isEmpty(zqPath)&&TextUtils.isEmpty(zhfPath)){
                             //走修改接口
                             setCartMsg();
                             mydialog.show();
                             Log.e("TAG","走修改接口");
                         }else{
                             mydialog.show();
                             Log.e("TAG","修改图片==zqfPath="+zqfPath);
                             Log.e("TAG","修改图片==zqPath="+zqPath);
                             Log.e("TAG","修改图片==zhfPath="+zhfPath);
                             if(!TextUtils.isEmpty(zqfPath)){
                                 updateImag(zqfPath);
                             }
                             if(!TextUtils.isEmpty(zqPath)){
                                 updateImag(zqPath);
                             }
                             if(!TextUtils.isEmpty(zhfPath)){
                                 updateImag(zhfPath);
                             }


                         }
                     }else {

                         if (TextUtils.isEmpty(zqfPath)){
                             //img_newfragment.getDrawable().getCurrent().getConstantState()==getResources().getDrawable(R.drawable.zq45d).getConstantState()
//                             img_newfragment.setImageDrawable(getActivity().getDrawable(R.drawable.rednull));
                             Toast.makeText(getContext(),"左前45°图片不能为空",Toast.LENGTH_LONG).show();
                         }
                         else if (TextUtils.isEmpty(zqPath)){
//                             img2_newfragment.setImageDrawable(getActivity().getDrawable(R.drawable.rednull));
                             Toast.makeText(getContext(),"正前图片不能为空",Toast.LENGTH_LONG).show();
                         }
                         else if (TextUtils.isEmpty(zhfPath)){
//                             img3_newfragment.setImageDrawable(getActivity().getDrawable(R.drawable.rednull));
                             Toast.makeText(getContext(),"正后图片不能为空",Toast.LENGTH_LONG).show();
                         }else {
                             mydialog.show();
                             Log.e("TAG", "修改=" + zqfPath);
                             updateImag(zqfPath);
                             updateImag(zqPath);
                             updateImag(zhfPath);
                         }
                     }
                }
                break;
            case R.id.img_newfragment:
                img_newfragment.setBackgroundResource(0);
                picName="zuoqian";
                getPicView(img_newfragment);
//               if( setPermissions()) {
//                Intent intent3=new Intent(getContext(),CameraActivity.class);
//                intent3.putExtra("name","zuoqian");
//                   int h=getActivity().getWindowManager().getDefaultDisplay().getHeight();
//                   Log.e("TAG","h=="+h);
//                   intent3.putExtra("height", 466+"");
//                startActivity(intent3);
//               }
                break;
            case R.id.img2_newfragment:
                img2_newfragment.setBackgroundResource(0);
                getPicView(img2_newfragment);
                picName="zhengqian";
//                if(setPermissions()){
//                    Intent intent4=new Intent(getContext(),CameraActivity.class);
//                    intent4.putExtra("name","zhengqian");
//                    int h=getActivity().getWindowManager().getDefaultDisplay().getHeight()-200;
//                    Log.e("TAG","h的高低=="+h);
//                    intent4.putExtra("height",""+466);
//                    startActivity(intent4);
//                }

                break;
            case R.id.img3_newfragment:
                getPicView(img3_newfragment);
                picName="zhenghou";
                img3_newfragment.setBackgroundResource(0);

                break;
            case R.id.tv_cartmodel:
                Intent intent6=new Intent(getContext(), CartModelActivity.class);
                startActivity(intent6);
                break;
            case R.id.tv_paizhao2:
                //调取相机功能
//                startActivity(new Intent(getContext(), TakePhoteActivity.class));
                if(setPermissions()) {
                    Intent intent5 = new Intent(getContext(), CameraActivity.class);
                    intent5.putExtra("name", picName);
                    intent5.putExtra("height", "466");
                    startActivity(intent5);
                }
                window.dismiss();
                break;
            case R.id.tv_xiangce2:
                takePicture();
                window.dismiss();
                break;
            case R.id.tv_canle2:
                if(window!=null&&window.isShowing()){
                    window.dismiss();
                }
                break;
        }
    }
    //获取popwindow
    private void getPopView(){
        popView= View.inflate(getContext(),R.layout.popwiew,null);
        LinearLayout pop_linear=popView.findViewById(R.id.pop_linear);
        tv_paizhao=popView.findViewById(R.id.tv_paizhao);
        tv_xiangce=popView.findViewById(R.id.tv_xiangce);
        tv_canle=popView.findViewById(R.id.tv_canle);
        window=new PopupWindow(getContext());
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        pop_linear.measure(w, h);
        int pop_height = pop_linear.getMeasuredHeight();
        int pop_width = pop_linear.getMeasuredWidth();
        int width=getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int height=getActivity().getWindowManager().getDefaultDisplay().getHeight();
        window.setWidth(width/3);
        window.setHeight(pop_height);
        // 设置PopupWindow的背景
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        window.setContentView(popView);
        window.setAnimationStyle(R.style.animTranslate);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp=getActivity().getWindow().getAttributes();
                lp.alpha=1.0f;
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getActivity().getWindow().setAttributes(lp);
            }
        });
//        window.showAtLocation(img_circle, Gravity.BOTTOM,0,0);
        window.showAsDropDown(img_paizhao,0,0);
        WindowManager.LayoutParams lp=getActivity().getWindow().getAttributes();
        lp.alpha=0.3f;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        // window.showAtLocation(parent, gravity, x, y);
        tv_xiangce.setOnClickListener(this);
        tv_paizhao.setOnClickListener(this);
        tv_canle.setOnClickListener(this);
    }

    MyBroadcastReceiver myBroadcastReceiver;
    //注册广播
    private void MyRegistReciver(){
        myBroadcastReceiver=new MyBroadcastReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("vin");
        intentFilter.addAction("quyu");
        intentFilter.addAction("cartmodel");
        intentFilter.addAction("update");
        intentFilter.addAction("goon");
        intentFilter.addAction("modelname");
        intentFilter.addAction("updataCart");//获取收到添加车商信息
        intentFilter.addAction("cartfenlei");//
        intentFilter.addAction("strNameAndTelAndID");
        getActivity().registerReceiver(myBroadcastReceiver,intentFilter);
    }
    //显示日期
    private void showDate(final TextView Tv) {
        Calendar calend1 = Calendar.getInstance();
        calend1.setTimeInMillis(System.currentTimeMillis());
        int year = calend1.get(Calendar.YEAR);
        int month = calend1.get(Calendar.MONTH) + 1;
        int day = calend1.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog3 = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view,
                                          int year, int monthOfYear,
                                          int dayOfMonth) {
                        String month="";
                        String day="";
                        if((monthOfYear+1)<10){
                            month="0"+(monthOfYear+1);
                        }else{
                            month=""+(monthOfYear+1);
                        }
                        if(dayOfMonth<10){
                            day="0"+dayOfMonth;
                        }else{
                           day=dayOfMonth+"";
                        }
                        Tv.setText(year+"-"+month+"-"+day);
                        Tv.setBackgroundResource(R.drawable.juxingnull);
                    }
                }, year, month, day);
        dialog3.show();
    }
    //判断小数点后面是否都为"0",截取字符串
    private void getSubStr(EditText editText){
        String editStr=editText.getText().toString();
        if(editStr.contains(".")) {
//            if(editStr.length()>5){
//                Log.e("TAG","一共长的=="+editStr.length());
//                Log.e("TAG","小数点的位置"+editStr.indexOf(".")+"hhh=="+editStr.substring(0,editStr.indexOf(".")+3));
//                editStr=editStr.substring(0,5);
//             }
                int i = editStr.indexOf(".");
                String subStr = editStr.substring(i, editStr.length() - 1);
                int count = 0;
                for (int h = 0; h < subStr.length(); h++) {
                    char item = subStr.charAt(h);
                    if ((String.valueOf(item)).equals("0")) {
                        count++;
                    }
                }
                if (count == subStr.length() - 1) {
                    editText.setText(editStr.substring(0, i));
                }else{
                    editText.setText(editStr.substring(0,editStr.indexOf(".")+3));
                }
            }
    }
    //接受广播退出APP
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("TAG","接收广播newFragment===="+intent.getStringExtra("path"));
            if(intent.getAction().equals("update")){
                tv_quyue.setBackgroundResource(R.drawable.juxingnull);
                tv_cartmodel.setBackgroundResource(R.drawable.juxingnull);
                tv_time.setBackgroundResource(R.drawable.juxingnull);
                tv_cartFenlei.setBackgroundResource(R.drawable.juxingnull);
                img_newfragment.setBackgroundResource(R.drawable.zq45d);
                img2_newfragment.setBackgroundResource(R.drawable.zqf);
                img3_newfragment.setBackgroundResource(R.drawable.zhf);
                MyNewUpdate myNewUpdate=new MyNewUpdate();

               //TODO

                fenleiID=MyNewUpdate.isDaTing;
//                1=大厅车辆 ，2=市场车辆，3=商户自用车，4=新车登记商户卡车辆，5=职工车辆
                if(fenleiID.equals("1")){
                    tv_cartFenlei.setText("大厅车辆");
                }else if(fenleiID.equals("2")){
                    tv_cartFenlei.setText("市场车辆");
                }
                else if(fenleiID.equals("3")){
                    tv_cartFenlei.setText("商户自用车");
                }
                else if(fenleiID.equals("4")){
                    tv_cartFenlei.setText("新车登记商户卡车辆");
                }
                else if(fenleiID.equals("5")){
                    tv_cartFenlei.setText("职工车辆");
                }
                if(!TextUtils.isEmpty(MyNewUpdate.tel)){
                    tv_tel.setText(MyNewUpdate.tel.trim());
                }
                if(!TextUtils.isEmpty(MyNewUpdate.contact_name)) {
                    edt_name.setText(MyNewUpdate.contact_name.trim());
                }

                edit_num.setText(MyNewUpdate.vinnum);
                edit_num.setFocusableInTouchMode(false);
                edit_num.setFocusable(false);
                tv_quyue.setText(MyNewUpdate.quyu);
                tv_cartmodel.setText(MyNewUpdate.cartmodel);
                tv_time.setText(MyNewUpdate.time);
                edt_licheng.setText(MyNewUpdate.licheng);
                edt_price.setText(MyNewUpdate.price);
                Log.e("TAG","==MyNewUpdate.price=="+MyNewUpdate.price);
                Log.e("TAG","edt_price=="+edt_price.getText());
                str=MyNewUpdate.img1;
                Log.e("TAG","修改接受到的图片地址=="+ZQFBean.zqpath);
                ZQBean.zqpath=MyNewUpdate.img2;
                ZHFBean.zhfpath=MyNewUpdate.img3;
                if(!TextUtils.isEmpty(MyNewUpdate.img1)) {
                     Glide.with(getContext()).load(MyNewUpdate.img1).placeholder(R.drawable.zq45d).error(R.drawable.zq45d).into(img_newfragment);
                }
                if(!TextUtils.isEmpty(MyNewUpdate.img2)) {
                      Glide.with(getContext()).load(MyNewUpdate.img2).placeholder(R.drawable.zqf).error(R.drawable.zqf).into(img2_newfragment);
                }
                if(!TextUtils.isEmpty(MyNewUpdate.img3)) {
                    Glide.with(getContext()).load(MyNewUpdate.img3).placeholder(R.drawable.zhf).error(R.drawable.zhf).into(img3_newfragment);
                }
                quyuID=MyNewUpdate.quyuID;
                seriesid=MyNewUpdate.seriseID;
                modelid=MyNewUpdate.modelID;
                brandid=MyNewUpdate.brandid;
                cartName=MyNewUpdate.cartmodel;
                picID=MyNewUpdate.NameTelID;
                BeanFlag.Flag=true;
                currentID=MyNewUpdate.currentID;
            }
            else if(intent.getAction().equals("quyu")){

                String name=intent.getStringExtra("name");
                quyuID=intent.getStringExtra("ID");
                Log.e("TAG","接受区域广播=="+intent.getStringExtra("tel"));
                quyuTelName=intent.getStringExtra("tel");
                currentID=intent.getStringExtra("currentID");
                if(TextUtils.isEmpty(quyuTelName)||quyuTelName.equals("&&&")){
                    tv_tel.setText("");
                    edt_name.setText("");
                    picID="0";
                }else{
                    String []arr=quyuTelName.split("&");
                    if(!quyuTelName.equals("&&&")) {
                        Log.e("TAG","quyuName--"+quyuTelName);
                        tv_tel.setText(arr[0]);
                        edt_name.setText(arr[1]);
                        picID=arr[2];
                        if(MySerchActvity.nameAndTelList.get(Integer.parseInt(currentID))!=null) {
                            Log.e("TAG","");
                            NameAndTelDialog nameAndTelDialog = new NameAndTelDialog(getContext(), MySerchActvity.nameAndTelList.get(Integer.parseInt(currentID)));
                            nameAndTelDialog.show();

                        }
                    }
//                    Log.e("TAG","判断=="+(TextUtils.isEmpty(tv_tel.getText().toString())||TextUtils.isEmpty(edt_name.getText().toString())));
//                    Log.e("TAG","判断1"+(tv_tel.getText().toString()==null)+"="+(tv_tel.getText().toString().equals("null"))+"==="+tv_tel.getText().toString()+"="+(!TextUtils.isEmpty(tv_tel.getText().toString())));
//                    Log.e("TAG","判断2=+"+edt_name.getText().toString()+"+="+!TextUtils.isEmpty(edt_name.getText().toString()));
//                    if(edt_name.getText().toString().equals("null")||tv_tel.getText().toString().equals("null")||TextUtils.isEmpty(tv_tel.getText().toString())||TextUtils.isEmpty(edt_name.getText().toString())){
//                        linear_nameandtel.setVisibility(View.VISIBLE);
//                    }else{
//                        linear_nameandtel.setVisibility(View.GONE);
//                    }
                }
                Log.e("TAG","接收到的销售人员电话和姓名为="+quyuTelName+"=ID="+picID);
                tv_quyue.setText(name);
                tv_quyue.setBackgroundResource(R.drawable.juxingnull);
//                if(MySerchActvity.nameAndTelList.get(Integer.parseInt(currentID))!=null){
//                    if(!(MySerchActvity.nameAndTelList.get(Integer.parseInt(currentID)).get(0).id).equals("")) {
//                    }
//                }
            }else if(intent.getAction().equals("vin")){
                String name = intent.getStringExtra("name");
                if (!TextUtils.isEmpty(name)) {
                    String path = intent.getStringExtra("path");
                    imgListPath.add(path);
                    FileUtil fileUtil = new FileUtil(context);
                    if (name.equals("zhengqian")) {
                        zqPath=path;
                        img2_newfragment.setImageBitmap(fileUtil.readBitmap(path));
                    } else if (name.equals("zuoqian")) {
                        zqfPath=path;
                        Log.e("TAG","path=="+path);
                        Log.e("TAG","拍照返回=="+zqfPath);
                        img_newfragment.setImageBitmap(BitmapFactory.decodeFile(path));
//                        img_newfragment.setImageBitmap(fileUtil.readBitmap(path));
                    } else if (name.equals("zhenghou")) {
                        zhfPath=path;
                        img3_newfragment.setImageBitmap(fileUtil.readBitmap(path));
                    }
                } else {
                    //上传vin码返回
                    Log.e("TAG", "广播接受上传vin码返回====" + intent.getStringExtra("vinnum"));
                    String str = intent.getStringExtra("vinnum");
                    if (TextUtils.isEmpty(str)) {
//                        linear3_newfragment.setVisibility(View.GONE);
                    } else {
                        String vinStr = intent.getStringExtra("vinnum");
                        if (TextUtils.isEmpty(vinStr)) {
//                            linear3_newfragment.setVisibility(View.VISIBLE);
                        } else {
                            Log.e("TAG","MyodelIDandName=="+ModelNameandID.list.size());
                            MyModelDialog myModelDialog=new MyModelDialog(getContext(),ModelNameandID.list);
                            myModelDialog.show();
//                            linear3_newfragment.setVisibility(View.GONE);
                            edit_num.setText(vinStr);
                            edt_licheng.setText(intent.getStringExtra("licheng"));
                            edt_price.setText(intent.getStringExtra("price"));
                            tv_time.setText(intent.getStringExtra("data"));
                            brandid=intent.getStringExtra("vinbrand_id");
                            seriesid=intent.getStringExtra("vinseries_id");
                            List list=new ArrayList();
                            cartName=intent.getStringExtra("CartName");
                            list.add(cartName);
                            modelid=intent.getStringExtra("model_id");
                            tv_cartmodel.setText(cartName);
                        }
                    }
                }
            }else if(intent.getAction().equals("cartmodel")){
                tv_cartmodel.setText(intent.getStringExtra("brand")+
                        intent.getStringExtra("serise")+
                intent.getStringExtra("model"));
                seriesid=intent.getStringExtra("seriseID");
                brandid=intent.getStringExtra("barndID");
                cartName=intent.getStringExtra("model");
                modelid=intent.getStringExtra("modelID");
                tv_cartmodel.setBackgroundResource(R.drawable.juxingnull);
            }else if(intent.getAction().equals("goon")){
                Log.e("TAG","接收继续上传的广播");
                //继续上传接口
                BeanFlag.Flag=false;
                edit_num.setText("");
                edt_price.setText("");
                edt_licheng.setText("");
                tv_time.setText("");
                tv_cartmodel.setText("");
                tv_cartFenlei.setText("");
                ZQFBean.zqpath="";ZQBean.zqpath="";ZHFBean.zhfpath="";str="";
                zqfPath="";zqPath="";zhfPath="";
                edit_num.setFocusableInTouchMode(true);
                edit_num.setFocusable(true);
                img_newfragment.setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.zq45d));
                img2_newfragment.setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.zqf));
                img3_newfragment.setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.zhf));
            }else if(intent.getAction().equals("modelname")){
                cartName=intent.getStringExtra("modelname");
                linear3_newfragment.setVisibility(View.VISIBLE);
                tv_cartmodel.setText(cartName);
                modelid=intent.getStringExtra("modelID");
            }else if(intent.getAction().equals("updataCart")){
                quyuID=intent.getStringExtra("quyuID");
                tv_quyue.setText(intent.getStringExtra("quyuName"));
            }else if(intent.getAction().equals("cartfenlei")){
                tv_cartFenlei.setText(intent.getStringExtra("fenleiname"));
                fenleiID=intent.getStringExtra("fenleiID");
                tv_cartFenlei.setBackgroundResource(R.drawable.juxingnull);
            }else if(intent.getAction().equals("strNameAndTelAndID")){
                //广播返回姓名，电话，id
                String str=intent.getStringExtra("strNameAndTelAndID");
                if(!TextUtils.isEmpty(str)&&!str.equals("&&")){
                    String [] arr=str.split("&");
                    picID=arr[2];
                    edt_name.setText(arr[0]);
                    tv_tel.setText(arr[1]);
                }else{
                    picID="0";
                }
            }
        }
    }
    //点击提交判断是否有空的Edittext
    private boolean IsNullEdit(EditText editText){
        if(editText!=null) {
            String str = editText.getText().toString();
            if (TextUtils.isEmpty(str)) {
                editText.setBackgroundResource(R.drawable.rednull);
                return false;
            }else{
                return true;
            }
        }
        return false;
    }
    class MyEditTextChangeListener implements TextWatcher {
        EditText editText;
        public MyEditTextChangeListener(EditText editText){
            this.editText=editText;
        }
        /**
         * 编辑框的内容发生改变之前的回调方法
         */
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            Log.e("TAG","编辑框的内容发生改变之前的回调方法");
        }

        /**
         * 编辑框的内容正在发生改变时的回调方法 >>用户正在输入
         * 我们可以在这里实时地 通过搜索匹配用户的输入
         */
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            Log.e("TAG","发生改变");
            editText.setBackgroundResource(R.drawable.juxingnull);
        }

        /**
         * 编辑框的内容改变以后,用户没有继续输入时 的回调方法
         */
        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
    //上传三张大图
    String  str="";
    private void updateImag(final String path){
            final RequestParams params=new RequestParams(getInterface.UpdateImag);
            params.setMultipart(true);
            params.setConnectTimeout(80000);
            params.setMaxRetryCount(5);//
            params.addBodyParameter("imgdata",new File(path));
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(path));
            int fileLen = fis.available();
            Log.e("TAG","上传文件大小=="+fileLen/1024+"==path="+path.substring(path.length()-5,path.length()));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//           params.setMaxRetryCount(2);
            Log.e("TAG","参数--"+params.getParams("imgdata"));
            Log.e("TAG","上传图片URLparams=="+params);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.e("TAG","图片上传结果---"+result);
                    Log.e("TAG","path=="+path.equals(zqfPath));
                    if(!TextUtils.isEmpty(path)&&path.equals(zqfPath)){
                        ZQFBean.zqpath=GetJsonUtils.getZQF(getContext(),result);
                        str=GetJsonUtils.getZQF(getContext(),result);
                        Log.e("TAG","这里左前方=="+ZQFBean.zqpath);
                    }else if(!TextUtils.isEmpty(path)&&path.equals(zqPath)){
                        //正前方图pain
                        Log.e("TAG","这里正前方图片");
                        ZQBean.zqpath=GetJsonUtils.getZQF(getContext(),result);

                    }else if(!TextUtils.isEmpty(path)&&path.equals(zhfPath)){
                        //正后方图pain
                        Log.e("TAG","这里正后方图片");
                        ZHFBean.zhfpath=GetJsonUtils.getZQF(getContext(),result);
                    }
                    ZQFBean.zqpath=str;
                    Log.e("TAG","上传图片结果左前方图片=="+zqfPath+ ZQFBean.zqpath+"="+str);
                    Log.e("TAG","上传图片结果正前方图片="+zqPath+ZQBean.zqpath);
                    Log.e("TAg","上传图片结果正后方图片="+zhfPath+ ZHFBean.zhfpath);
                    Log.e("TAG","三张大图上传结果=="+(!TextUtils.isEmpty(ZHFBean.zhfpath)&&!TextUtils.isEmpty(ZQBean.zqpath)&&!TextUtils.isEmpty(ZQFBean.zqpath)));
                    if(!TextUtils.isEmpty(ZHFBean.zhfpath)&&!TextUtils.isEmpty(ZQBean.zqpath)&&!TextUtils.isEmpty(ZQFBean.zqpath)){
                        //上传全部信息
                            Log.e("TAG","上传补录信息");
                            if(BeanFlag.Flag){
                                //修改接口
                                Log.e("TAG","修改接口");
                                setCartMsg();
                            }else {
                                Log.e("TAG","上传接口");
                                updateCartMsg();
                            }

                    }
                    else {
                        Log.e("TAG","修改BeanFlag.Flag=="+BeanFlag.Flag);
                        if (BeanFlag.Flag) {
                            if (!TextUtils.isEmpty(zqfPath) && TextUtils.isEmpty(ZQFBean.zqpath)) {
                            Log.e("TAG","左前方45°角图片修改失败=="+ZQFBean.zqpath);
                                Toast.makeText(getContext(),"左前方45°角图片修改失败",Toast.LENGTH_SHORT).show();
                            } if (!TextUtils.isEmpty(zqPath) && TextUtils.isEmpty(ZQBean.zqpath)) {
                                //正前方图pain
                                Log.e("TAG","正前方图片修改失败=="+ZQBean.zqpath);
                                Toast.makeText(getContext(),"正前方图片修改失败",Toast.LENGTH_SHORT).show();
                            } if (!TextUtils.isEmpty(zhfPath) && TextUtils.isEmpty(ZHFBean.zhfpath)) {
                                //正后方图pain
                                Log.e("TAG","正后方图片修改失败=="+ZHFBean.zhfpath);
                                Toast.makeText(getContext(),"正后方图片修改失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.e("TAG","上传失败==="+(!TextUtils.isEmpty(ZHFBean.zhfpath)&&!TextUtils.isEmpty(ZQBean.zqpath)&&!TextUtils.isEmpty(ZQFBean.zqpath)));
                    if(mydialog.isShowing()){
                        mydialog.dismiss();
                    }
                    if(!TextUtils.isEmpty(zqfPath)){
                        ZQFBean.zqpath="";
                    }
                    if(!TextUtils.isEmpty(zqPath)){
                        ZQBean.zqpath="";
                    }
                    if(!TextUtils.isEmpty(zhfPath)){
                        ZHFBean.zhfpath="";
                    }
//                    if(!TextUtils.isEmpty(ZHFBean.zhfpath)&&!TextUtils.isEmpty(ZQBean.zqpath)&&!TextUtils.isEmpty(ZQFBean.zqpath)){
//                        ZQFBean.zqpath="";ZQBean.zqpath="";ZHFBean.zhfpath="";str="";
//                    }else{
                        if(!TextUtils.isEmpty(ex.getMessage().toString())){
                            if (ex instanceof HttpException) { // 网络错误
                                HttpException httpEx = (HttpException) ex;
                                int responseCode = httpEx.getCode();
                                String responseMsg = httpEx.getMessage();
                                String errorResult = httpEx.getResult();
                                Log.e("TAG","responseCode=="+responseCode+"=responseMsg="+responseMsg+"=errorResult="+errorResult);
                            } else {
// 其他错误//
                            }
                            Log.e("TAG","ex.getMessage().toString()=="+ex.getMessage().toString());
                            mydialog.dismiss();
                            ZQFBean.zqpath="";ZQBean.zqpath="";ZHFBean.zhfpath="";str="";
                            Toast.makeText(getContext(),"图片上传失败",Toast.LENGTH_LONG).show();
                        }
//                    }
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }
    //上传补录车辆信息
    private void updateCartMsg(){
        Log.e("TAG","开始上传");
        RequestParams requestParams=new RequestParams(getInterface.UpCartData);
        //    参数 时间   regDate  merchant_code 商家ID  vin,groupid,carName(车型),
        // mileage,target_price,userid,brandid,seriesid,zhengqian45,zhengqian,zhenghou
        requestParams.addBodyParameter("vin",edit_num.getText().toString().trim());
        requestParams.addBodyParameter("merchant_code",quyuID);
        requestParams.addBodyParameter("groupid",UserBean.groupids);
        requestParams.addBodyParameter("userid",UserBean.id);
        requestParams.addBodyParameter("brandid",brandid);
        requestParams.addBodyParameter("seriesid",seriesid);
        requestParams.addBodyParameter("regDate",tv_time.getText().toString().trim());
        requestParams.addBodyParameter("mileage",edt_licheng.getText().toString().trim());
        requestParams.addBodyParameter("target_price",edt_price.getText().toString().trim());
        requestParams.addBodyParameter("zhengqian45",ZQFBean.zqpath);
        requestParams.addBodyParameter("zhengqian",ZQBean.zqpath);
        requestParams.addBodyParameter("zhenghou",ZHFBean.zhfpath);
        Log.e("TAG","上传车辆信息左前方==="+ZQFBean.zqpath);
        Log.e("TAG","上传车辆信息正前==="+ZQBean.zqpath);
        Log.e("TAG","上传车辆信息正后方==="+ZHFBean.zhfpath);
        requestParams.addBodyParameter("modelid",modelid);//modelid
        requestParams.addBodyParameter("carName",cartName.replace(" ",""));
        //上传电话和名字
        //有对应id直接传ID
        if(quyuTelName!=null){
            String arr[]=quyuTelName.split("&");
            if(!edt_name.getText().toString().equals(arr[1])||!tv_tel.getText().toString().equals(arr[0])){
                picID="0";
            };
        }
        if(!picID.equals("0")&&!TextUtils.isEmpty(picID)){
            requestParams.addBodyParameter("pid",picID);
        }else{
            requestParams.addBodyParameter("pid ","0");
        }
        requestParams.addBodyParameter("tel",tv_tel.getText().toString());
        requestParams.addBodyParameter("name",edt_name.getText().toString());
        //车辆分类
        requestParams.addBodyParameter("isDaTing",fenleiID);
        requestParams.setMaxRetryCount(2);
        Log.e("TAG","上传地址=="+requestParams.getUri());
        Log.e("TAG","上传参数=="+requestParams.getBodyParams());
        Log.e("TAG","上传URL=="+requestParams);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG","上传成功=="+result);
                mydialog.dismiss();
//                {"status":1,"msg":"添加成功","id":"2261"}
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    String status=jsonObject.getString("status");
                    String msg;
                    if (status.equals("1")){
                       msg=jsonObject.getString("msg");
                        mySuccess=new MySuccess(getContext(),"上传成功");
                        mySuccess.show();
                        cleanDate();
                    }
                    Log.e("TAG","TAG"+(!mySuccess.isShowing()&&status.equals("0")));
                    if(!mySuccess.isShowing()&&status.equals("0")){
                        msg=jsonObject.getString("msg");
                        cleanDate();
                        Toast.makeText(getContext(),""+msg,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(!TextUtils.isEmpty(ex.getMessage().toString())){
                    mydialog.dismiss();
//                    ZQFBean.zqpath="";ZQBean.zqpath="";ZHFBean.zhfpath="";
//                    zqfPath="";
//                    zhfPath="";
//                    zqPath="";
                    Toast.makeText(getContext(),"上传信息失败",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if(mydialog.isShowing()){
                    mydialog.dismiss();
                }

            }
        });
    }
    //修改补录信息车辆
    private void setCartMsg(){
        Log.e("TAG","开始修改");
        RequestParams requestParams=new RequestParams(getInterface.UpCartData);
        //    参数 时间   regDate  merchant_code 商家ID  vin,groupid,carName(车型),
        // mileage,target_price,userid,brandid,seriesid,zhengqian45,zhengqian,zhenghou
        requestParams.addBodyParameter("id",MyNewUpdate.ItemID);
//        requestParams.addBodyParameter("vin",edit_num.getText().toString().trim());//vin码不可修改
        Log.e("TAG","quyuID=="+quyuID);
        requestParams.addBodyParameter("merchant_code",quyuID);
        requestParams.addBodyParameter("groupid",UserBean.groupids);
//        requestParams.addBodyParameter("userid",UserBean.id);
        requestParams.addBodyParameter("vendorId",brandid);
        requestParams.addBodyParameter("brandId",seriesid);
        requestParams.addBodyParameter("regDate",tv_time.getText().toString().trim());
        requestParams.addBodyParameter("mileage",edt_licheng.getText().toString().trim());
        requestParams.addBodyParameter("target_price",edt_price.getText().toString().trim());
//        Log.e("TAG","修改接受到的图片地址上传=="+str);
        requestParams.addBodyParameter("zhengqian45",str);
        requestParams.addBodyParameter("zhengqian",ZQBean.zqpath);
        requestParams.addBodyParameter("zhenghou",ZHFBean.zhfpath);
        requestParams.addBodyParameter("carStyleId",modelid);//modelid
        Log.e("TAG","修改接口中modelid=="+modelid);
        requestParams.addBodyParameter("carName",cartName.replace(" ",""));
        //上传电话和名字
        //有对应id直接传ID
//        if(quyuTelName!=null){
//            String arr[]=quyuTelName.split("&");
            Log.e("TAG","修改edt_name=="+!edt_name.getText().toString().equals(MyNewUpdate.contact_name));
            Log.e("TAG","修改tv_tel=="+(!tv_tel.getText().toString().equals(MyNewUpdate.tel)));
            Log.e("TAG","!edt_name.getText().toString().equals(MyNewUpdate.contact_name)||!tv_tel.getText().toString().equals(MyNewUpdate.tel)=="+(!edt_name.getText().toString().equals(MyNewUpdate.contact_name)||!tv_tel.getText().toString().equals(MyNewUpdate.tel)));
            if(!edt_name.getText().toString().equals(MyNewUpdate.contact_name)||!tv_tel.getText().toString().equals(MyNewUpdate.tel)){
                picID="0";
            };
//        }
        if(!picID.equals("0")&&!TextUtils.isEmpty(picID)){
            requestParams.addBodyParameter("pid",picID);
        }else{
            requestParams.addBodyParameter("pid","0");
        }
        Log.e("TAG","修改tv_tel=="+tv_tel.getText().toString());
        requestParams.addBodyParameter("tel",tv_tel.getText().toString());
        requestParams.addBodyParameter("name",edt_name.getText().toString());
        //车辆分类
        requestParams.addBodyParameter("isDaTing",fenleiID);
        requestParams.setMaxRetryCount(2);
//        requestParams.addBodyParameter("status","0");
        Log.e("TAG","修改地址=="+requestParams);
        Log.e("TAG","修改上传参数=="+requestParams.getBodyParams());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG","修改成功=="+result);
//                {"status":1,"msg":"添加成功","id":"2261"}
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    String status=jsonObject.getString("status");
                    String msg;
                    if (status.equals("1")){
                        msg=jsonObject.getString("msg");
                        mydialog.dismiss();
                        Log.e("TAG","!mySuccess.isShowing()"+(!mySuccess.isShowing()));
                       if(!mySuccess1.isShowing()) {
                           mySuccess1.show();
                       }
                    }else{
                        msg=jsonObject.getString("msg");
                        Toast.makeText(getContext(),""+msg,Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(!TextUtils.isEmpty(ex.getMessage().toString())){
                    mydialog.dismiss();
                    Toast.makeText(getContext(),"上传信息失败",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if(mydialog.isShowing()){
                    mydialog.dismiss();
                }
            }
        });
    }
    //获取价格
    private void getPrice(final String string){
        mydialog1.show();
        final RequestParams requestParams=new RequestParams(getInterface.getPrice);
        requestParams.addBodyParameter("vin",edit_num.getText().toString());
        if(string.equals("model")&&!tv_time.equals("请选择日期")){
            requestParams.addBodyParameter("regdate",tv_time.getText().toString());
        }
        requestParams.setMaxRetryCount(5);
        Log.e("TAG","获取价格params="+requestParams);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAg", "获取价格为：" + result);
//                {"status":0,"vin":"12345678901472589","msg":"获取VIN信息失败"}
                mydialog1.dismiss();
                List<JaShiZhengBean>list=new ArrayList<JaShiZhengBean>();
                list=GetJsonUtils.getCartMsg(getContext(),result);
                if(!TextUtils.isEmpty(string)&&string.equals("model")){
                    edt_price.setText(list.get(0).price.toString());
                    edt_licheng.setText(list.get(0).licheng.toString());
                    getSubStr(edt_price);
                    getSubStr(edt_licheng);
//                    tv_time.setText(list.get(0).data.toString());
                    seriesid=list.get(0).series_id;
                    brandid=list.get(0).brand_id;
                    Log.e("TAG","seriesid=="+seriesid+"=="+list.get(0).series_id);
                    MyModelDialog myModelDialog=new MyModelDialog(getContext(),ModelNameandID.list);
                    myModelDialog.show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG","getPrice=="+ex.getMessage().toString());
                if(mydialog.isShowing()){
                    if(!TextUtils.isEmpty(ex.getMessage().toString())){
                        mydialog.dismiss();
                        Toast.makeText(getContext(),"获取失败",Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if(mydialog.isShowing()){
                    mydialog.dismiss();
                }
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(myBroadcastReceiver);
    }

    private void cleanDate(){

        edit_num.setText("");
        edt_price.setText("");
        edt_licheng.setText("");
        tv_time.setText("");
        tv_cartmodel.setText("");
        tv_cartFenlei.setText("");
        ZQFBean.zqpath="";ZQBean.zqpath="";ZHFBean.zhfpath="";str="";
        zqfPath="";zqPath="";zhfPath="";
        edit_num.setFocusableInTouchMode(true);
        edit_num.setFocusable(true);
        img_newfragment.setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.zq45d));
        img2_newfragment.setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.zqf));
        img3_newfragment.setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.zhf));
    }
    static final String[] PERMISSION = new String[]{
            Manifest.permission.READ_CONTACTS,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,  //读取权限
            Manifest.permission.WRITE_CALL_LOG,        //读取设备信息
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    /**
     * 设置Android6.0的权限申请
     */
    private boolean setPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //Android 6.0申请权限
            Toast.makeText(getActivity(),"没有相关权限，请先开启",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(),PERMISSION,1);
        }else{
            return true;
        }
        return false;
    }
    //获取popwindow
    ImageView selectImag=null;
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private void getPicView(ImageView imageView){
        selectImag=imageView;
        popView= View.inflate(getContext(),R.layout.popwiew2,null);
        LinearLayout pop_linear=popView.findViewById(R.id.pop_linear);
        tv_paizhao=popView.findViewById(R.id.tv_paizhao2);
        tv_xiangce=popView.findViewById(R.id.tv_xiangce2);
        tv_canle=popView.findViewById(R.id.tv_canle2);
        window=new PopupWindow(getContext());
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        pop_linear.measure(w, h);
        int pop_height = pop_linear.getMeasuredHeight();
        int pop_width = pop_linear.getMeasuredWidth();
        Log.e("TAG","测量h="+pop_height);
        int width=getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int height=getActivity().getWindowManager().getDefaultDisplay().getHeight();
        window.setWidth(width);
        window.setHeight(pop_height);
        // 设置PopupWindow的背景
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        window.setContentView(popView);
        window.setAnimationStyle(R.style.animTranslate);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp=getActivity().getWindow().getAttributes();
                lp.alpha=1.0f;
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getActivity().getWindow().setAttributes(lp);
            }
        });
        window.showAtLocation(tv_topcenter, Gravity.BOTTOM,0,0);
        WindowManager.LayoutParams lp=getActivity().getWindow().getAttributes();
        lp.alpha=0.3f;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        // window.showAtLocation(parent, gravity, x, y);
        tv_xiangce.setOnClickListener(this);
        tv_paizhao.setOnClickListener(this);
        tv_canle.setOnClickListener(this);
        Log.e("TAG","window=="+window.getWidth()+"height=="+window.getHeight());
    }
    //调取本地相机
    public void takePicture(){
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        Bundle bundle=new Bundle();
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FileUtil fileUtil=new FileUtil(getContext());

        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                Log.e("TAG","这里走了吗");
                String photoPath="";
                Uri uri = data.getData();
                ContentResolver resolver=getActivity().getContentResolver();
                Bitmap bitmap= null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(resolver,uri);
//                    selectImag.setImageBitmap(bitmap);
                    //获取图片路径
                    String picPath="";
                    //获取照片路径
                    String[] filePathColumn = {MediaStore.Audio.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                    cursor.moveToFirst();
               photoPath  = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
                    cursor.close();
                    Log.i(TAG, "photoPath = "+photoPath+"length=="+new File(photoPath).length()/1024);
                    if(photoPath!=null) {
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        Log.e("TAG","开始压缩111=="+new File(photoPath).length()/1024);
//                        if(new File(photoPath).length()/1024>4000){
//                            bitmap.compress(Bitmap.CompressFormat.JPEG,80,baos);
//                        }else {
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//
//                        }
//                        int options = 100;
//                        Log.e("TAG","开始压缩222=="+baos.toByteArray().length/1024);
//                        while (baos.toByteArray().length / 1024 > 100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
//                            baos.reset();//重置baos即清空baos
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
//                            options -= 10;//每次都减少10
//                            if(options<11){
//                                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
//                                break;
//                            }
//                            Log.e("TAG", "baos.toByteArray().length==" + baos.toByteArray().length / 1024 + "");
//
//                        }
//                        Log.e("TAG", "总集baos.toByteArray().length==" + baos.toByteArray().length / 1024 + "");
//                        BitZip.compressImage(bitmap);
                        // 设置参数
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
                        BitmapFactory.decodeFile(photoPath, options);
                        int height = options.outHeight;
                        int width= options.outWidth;
                        int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
                        int minLen = Math.min(height, width); // 原图的最小边长
                        if(minLen > 100) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
                            float ratio = (float)minLen / 100.0f; // 计算像素压缩比例
                            inSampleSize = (int)ratio;
                        }
                        options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
                        options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
                        Bitmap bm = BitmapFactory.decodeFile(photoPath, options); // 解码文件
                        Log.w("TAG", "size: " + bm.getByteCount()/1024 + " width: " + bm.getWidth() + " heigth:" + bm.getHeight()); // 输出图像数据
                        selectImag.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        selectImag.setImageBitmap(bm);
                        new FileUtil(getContext()).saveBitmap(bm);
                        photoPath=FileUtil.getJpegName();
                        selectImag.setImageBitmap(new FileUtil(getContext()).readBitmap(photoPath));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
//                selectImag.setImageBitmap(bitmap);
                if(selectImag==img3_newfragment){
                    zhfPath=photoPath;
                    Log.e("TAG","图库里的zhfpath=="+zhfPath);
                }else if(selectImag==img2_newfragment){
                    zqPath=photoPath;
                    Log.e("TAG","图库里的zhpath=="+zqPath);
                }else if(selectImag==img_newfragment){
                    zqfPath=photoPath;
                    Log.e("TAG","图库里的path=="+zqfPath);
                }
            }
        }
    }
//    private void setName() {
//
//        edt_name.setDropDownVerticalOffset(10);
//        Log.e("TAG", "currentID==" + currentID);
//        List<String> list = new ArrayList<>();
//        if (currentID != null) {
//            List<NameAndTel> list1 = MySerchActvity.nameAndTelList.get(Integer.parseInt(currentID));
//            int count = MySerchActvity.nameAndTelList.get(Integer.parseInt(currentID)).size();
//            Log.e("TAG", "MySerchActvity.nameAndTelList.get(Integer.parseInt(currentID))=" + MySerchActvity.nameAndTelList.get(Integer.parseInt(currentID)).size());
//            for (int i = 0; i < count; i++) {
//                list.add(list1.get(i).name);
//                Log.e("TAG","list=="+list1.get(i).name);
//            }
////        Log.e("TAG","list=="+list.size()+MySerchActvity.nameAndTelList.get(Integer.parseInt(currentID)).name);
//            Log.e("TAG","list=="+list.size());
//            for(int i=0;i<list.size();i++){
//                Log.e("TAG","list=="+list.get(i));
//            }
//            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),R.layout.textlayout, list);
//            edt_name.setDropDownHeight(arrayAdapter.getCount()*30);
//            edt_name.setAdapter(arrayAdapter);
//        }
//    }
}
