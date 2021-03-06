package com.example.a123456.hebcartzhonggu;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

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

import application.MyApplication;
import base.BaseActivity;
import bean.BeanFlag;
import bean.Bran;
import bean.BuCartListBean;
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
import utils.ImgRote;
import utils.MyDBUtils;
import utils.MyModelDialog;
import utils.MySuccess;
import utils.Mydialog;
import utils.NameAndTelDialog;
import utils.SharedUtils;
import utils.getPicTku;
import View.GetJsonUtils;
//        zhengqian45 正前45度
//        zhengqian 正前
//        zhenghou 正后
//        youhou45 右后45度
//        fadongji 发动机
//        luntai 轮胎
//        toudeng 头灯
//        weideng 尾灯
//        yibiaopan 中控台

public class BuMessageActivity extends BaseActivity implements View.OnClickListener{
    private ImageView img_paizhao;
    private EditText edit_num;
    private View view;
    private PopupWindow window,window2,window3,window4;
    private View popView,popView2,popView3,popView4;
    private TextView tv_paizhao,tv_canle,tv_xiangce;
    private TextView tv_paizhao2,tv_canle2,tv_xiangce2;
    private TextView tv_paizhao3,tv_canle3,tv_xiangce3;
    private TextView tv_paizhao4,tv_canle4,tv_xiangce4;
    private ImageView img_topleft,img_topright;
    private TextView tv_topcenter;
    private TextView tv_time;//注册日期
    private EditText edt_licheng,edt_price,tv_tel;
    EditText edt_name;//里程，价格,联系电话
    private ImageView img_newfragment,img2_newfragment,img3_newfragment,
    img4_newfragment,img5_newfragment,img6_newfragment,img7_newfragment,img8_newfragment,img9_newfragment;
    private Button btn_commit;
    private LinearLayout linear3_newfragment,linear_nameandtel;
    private TextView tv_quyue,tv_cartFenlei;//tv_cartFenlei 车辆分类
    private TextView tv_cartmodel,tv_status;
    List imgListPath=new ArrayList();
    Mydialog mydialog;
    String zqfPath,zqPath,zhfPath,img4Path,img5Path,img6Path,img7Path,img8Path,img9Path;
    String quyuID,brandid,modelid,seriesid,cartName,fenleiID,brangName,seriseName,modelName;//商家信息ID,品牌ID，车系ID,车型,分类
    MySuccess mySuccess,mySuccess1;
    private TextView tv_getprice,tv_getmodel;
    Mydialog mydialog1;
    String quyuTelName;//车商信息对于的用户和电话
    String picID,currentID;//接收销售人员姓名和电话,当前ID
    private String picName;
    public String str;
    String status;
    SharedUtils utils = new SharedUtils();
    public int count ;//记录保存多少条数据
    String posion;
    public static final int PHOTOTAKE = 1; // 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private TextView Tv_guohu;
    private String guohuID;
    private String CartID;
    private List<BuCartListBean>list=new ArrayList<BuCartListBean>();
    private String zqfUrlPath,zfUrlPath,zhfUrlPath,img4UrlPath,img5UrlPath,img6UrlPath,img7UrlPath,img8UrlPath,img9UrlPath;
    private int imgCount;//记录要修改多少张图片
    Mydialog successdialog;
    int errorCount=0;
    private TextView tv_carnum;//车源编号
    private RelativeLayout relative_saomiao;//扫描获取车源编号
    private int REQUEST_CODE_SCAN = 111;//扫描状态码
    private LinearLayout linear_celiang;//中间布局--车源编号以下，里程以上
    int successCount=0;//记录成功上传图片个数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_bu_message);
        setPermissions();
        MyRegistReciver();
        initView();
        successdialog=new Mydialog(BuMessageActivity.this,"修改成功，稍后自动跳转...");
        CartID= getIntent().getStringExtra("cartID");
        Log.e("TAG","接收到的itemid=="+CartID);
        String str=getIntent().getStringExtra("Flag");
        //巡场扫描跳转
        String strURL=getIntent().getStringExtra("strUrl");
        String strxunchagn=getIntent().getStringExtra("xunchang");
        if(!TextUtils.isEmpty(str)&&str.equals("true")){//巡场进入，只能查看，不能修改提交
            BeanFlag.Flag=true;
            Log.e("TAG","这里巡场经来的");
            linear_celiang.setVisibility(View.VISIBLE);
//            btn_commit.setVisibility(View.GONE);
        }else{
            BeanFlag.Flag=false;//首页进入
            linear_celiang.setVisibility(View.GONE);
//            btn_commit.setVisibility(View.VISIBLE);
        }
        if(!TextUtils.isEmpty(strxunchagn)) {
            if (!TextUtils.isEmpty(strURL)) {
                getRFID(strURL);
            }
        }else{
            getItemCartDate();
        }

        //设置vin不可编辑
        edit_num.setFocusableInTouchMode(false);
        edit_num.setFocusable(false);
        //姓名电话,里程，价格不可修改
//        edt_licheng.setFocusableInTouchMode(false);
//        edt_price.setFocusable(false);
        edt_name.setFocusableInTouchMode(false);
        tv_tel.setFocusable(false);


        posion=getIntent().getStringExtra("i");
        if(!utils.readXML(MyApplication.cartlistmsg,"count",this).isEmpty()) {
            count = Integer.parseInt(utils.readXML(MyApplication.cartlistmsg, "count", this));
        }
        mydialog1=new Mydialog(this,"正在获取请稍后");
    }

    private void initView() {
        img_topleft=findViewById(R.id.img_left);
        img_topright=findViewById(R.id.img_right);
        tv_topcenter=findViewById(R.id.tv_center);
        tv_topcenter.setText("待补充车源");
//        img_topleft.setVisibility(View.GONE);
        img_topleft.setImageDrawable(this.getResources().getDrawable(R.mipmap.back));
        img_topleft.setOnClickListener(this);

        img_topright.setVisibility(View.GONE);
//        img_topright.setImageResource(R.mipmap.saoyisao);//设置右上角扫描图
        tv_getprice=findViewById(R.id.tv_getprice);
        tv_getprice.setOnClickListener(this);
        //所有要填写的控件
        //一下三个控件，当vin码识别识别，需要手动填写
        linear3_newfragment=findViewById(R.id.linear3_newfragment);
        tv_cartmodel=findViewById(R.id.tv_cartmodel);
        tv_time=findViewById(R.id.tv_time);//日期
//        linear_nameandtel=view.findViewById(R.id.linear_nameandtel);
        edt_name=findViewById(R.id.edt_name);
        tv_tel=findViewById(R.id.tv_tel);//联系电话
        edt_price=findViewById(R.id.edt_price);//价格
        edt_licheng=findViewById(R.id.edt_licheng);//里程
        tv_quyue=findViewById(R.id.tv_quyue);
        tv_cartFenlei=findViewById(R.id.tv_cartFenlei);
        tv_status=findViewById(R.id.tv_status);//车辆状态
        //设置里程和价格的数据，小数点后为0的话不现实0
//        getSubStr(edt_price);
//        getSubStr(edt_licheng);
        img_newfragment=findViewById(R.id.img_newfragment);//左前45°
        img2_newfragment=findViewById(R.id.img2_newfragment);//正前
        img3_newfragment=findViewById(R.id.img3_newfragment);//正后
        img4_newfragment=findViewById(R.id.img4_newfragment);
        img5_newfragment=findViewById(R.id.img5_newfragment);
        img6_newfragment=findViewById(R.id.img6_newfragment);
        img7_newfragment=findViewById(R.id.img7_newfragment);
        img8_newfragment=findViewById(R.id.img8_newfragment);
        img9_newfragment=findViewById(R.id.img9_newfragment);

        img_paizhao=findViewById(R.id.img_paizhao);//vin拍照
        edit_num=findViewById(R.id.edt_vinnum);//vin码显示

        btn_commit=findViewById(R.id.btn_commit);//提交按钮

        Tv_guohu=findViewById(R.id.tv_guohu);//是否过户

        tv_carnum=findViewById(R.id.tv_carnum);//车源编号
        relative_saomiao=findViewById(R.id.relative_saomiao);//扫描
        linear_celiang=findViewById(R.id.linear_celiang);//中间布局

        img_paizhao.setOnClickListener(this);
//        tv_time.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
        tv_status.setOnClickListener(this);
        edit_num.addTextChangedListener(new MyEditTextChangeListener(edit_num));
        edt_licheng.addTextChangedListener(new MyEditTextChangeListener(edt_licheng));
        edt_price.addTextChangedListener(new MyEditTextChangeListener(edt_price));
        tv_tel.addTextChangedListener(new MyEditTextChangeListener(tv_tel));
        edt_name.addTextChangedListener(new MyEditTextChangeListener(edt_name));

        img_newfragment.setOnClickListener(this);
        img2_newfragment.setOnClickListener(this);
        img3_newfragment.setOnClickListener(this);
        img4_newfragment.setOnClickListener(this);
        img5_newfragment.setOnClickListener(this);
        img6_newfragment.setOnClickListener(this);
        img7_newfragment.setOnClickListener(this);
        img8_newfragment.setOnClickListener(this);
        img9_newfragment.setOnClickListener(this);

//        tv_cartmodel.setOnClickListener(this);
//        tv_quyue.setOnClickListener(this);
//        tv_cartFenlei.setOnClickListener(this);
//        edt_name.setOnClickListener(this);
//        tv_getmodel=findViewById(R.id.tv_getmodel);
//        tv_getmodel.setOnClickListener(this);
//        Tv_guohu.setOnClickListener(this);
        tv_carnum.setOnClickListener(this);
        relative_saomiao.setOnClickListener(this);
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
    @Override
    public void onClick(View view) {
        Intent SMintent = new Intent(this, CaptureActivity.class);

            /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
             * 也可以不传这个参数
             * 不传的话  默认都为默认不震动  其他都为true
             * */

        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(true);
        config.setShake(true);
        SMintent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        switch (view.getId()) {
            case R.id.tv_status:
                getStatus();
                break;
            case R.id.tv_carnum:
                BeanFlag.Flag=false;
                startActivityForResult(SMintent, REQUEST_CODE_SCAN);
                break;
            case R.id.relative_saomiao:
                BeanFlag.Flag=false;
                startActivityForResult(SMintent, REQUEST_CODE_SCAN);
                break;
            case R.id.img_left:
                finish();
                break;
            case R.id.tv_guohu:
                getGuohu();
                break;
            case R.id.edt_name:
                edt_name.setText("");
                break;
            case R.id.tv_getmodel:
                //获取车型车系车牌
//                if (!TextUtils.isEmpty(edit_num.getText().toString())
//                        && !TextUtils.isEmpty(tv_time.getText().toString())
//                        && !tv_time.getText().toString().equals("请选择日期")) {
//                    getPrice("model");
//                } else {
//                    Toast.makeText(this, "vin码或注册日期不能为空", Toast.LENGTH_LONG).show();
//                }
                Toast.makeText(BuMessageActivity.this,"不可修改品牌车型车系",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_getprice:
                //获取价格
                if (!TextUtils.isEmpty(edit_num.getText().toString())
                        && !TextUtils.isEmpty(tv_time.getText().toString())
                        && !tv_time.getText().toString().equals("请选择日期")) {
//                    getPrice("price");
                } else {
                    Toast.makeText(this, "vin或者注册时间不能为空", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tv_quyue:
                Intent intentS = new Intent(this, MySerchActvity.class);
//                intentS.putExtra("myserch","update");
                startActivity(intentS);
                break;
            case R.id.img_paizhao:
//                getPopView();
                Toast.makeText(BuMessageActivity.this,"不可修改车辆VIN编号",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_paizhao:
                if(setPermissions()) {
                    Intent intent = new Intent(this, CameraActivity.class);
                    intent.putExtra("height", "70");
                    startActivity(intent);
                    window.dismiss();
                }
                break;
            case R.id.tv_xiangce:
                if (setPermissions()) {
                    Intent intent1 = new Intent(this, CameraActivity.class);
                    int h=getWindowManager().getDefaultDisplay().getHeight();
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
                Intent intent=new Intent(this, CartFenLei.class);
                startActivity(intent);
                break;
            case R.id.btn_commit:
                successCount=0;
//                updateImag(zqfPath);
//                updateImag(zqPath);
//                updateImag(zhfPath);
                Log.e("TAG","点击提交=="+BeanFlag.Flag);
//                getSubStr(edt_price);
//                getSubStr(edt_licheng);
                if (TextUtils.isEmpty(tv_carnum.getText().toString())||tv_carnum.getText().toString().trim().equals("请扫描二维码获取车源编号")){
                    tv_carnum.setBackgroundResource(R.drawable.rednull);
                    Toast.makeText(this,"车源编号不能为空",Toast.LENGTH_LONG).show();
                }else
                    if (TextUtils.isEmpty(tv_quyue.getText().toString())||tv_quyue.getText().toString().trim().equals("请选择车商信息")){
                    tv_quyue.setBackgroundResource(R.drawable.rednull);
                    Toast.makeText(this,"车商信息不能为空",Toast.LENGTH_LONG).show();
                }else if (!IsNullEdit(edit_num)||edit_num.getText().toString().length()!=17){
                    edit_num.setBackgroundResource(R.drawable.rednull);
                    Toast.makeText(this,"VIN码不能为空并且只能为17位",Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(tv_time.getText().toString())||tv_time.getText().toString().trim().equals("请选择日期")) {
                    tv_time.setBackgroundResource(R.drawable.rednull);
                    Toast.makeText(this,"注册日期不能为空",Toast.LENGTH_LONG).show();
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
                    Toast.makeText(this,"品牌，车系，车型不能为空",Toast.LENGTH_LONG).show();
                }else if (TextUtils.isEmpty(tv_cartFenlei.getText().toString())||tv_cartFenlei.getText().toString().trim().equals("请选取车辆分类信息")) {
                    tv_cartFenlei.setBackgroundResource(R.drawable.rednull);
                    Toast.makeText(this,"车辆分类信息不能为空",Toast.LENGTH_LONG).show();
                }else if (TextUtils.isEmpty(Tv_guohu.getText().toString())||Tv_guohu.getText().toString().trim().equals("请选择车辆是否过户")) {
                    Tv_guohu.setBackgroundResource(R.drawable.rednull);
                    Toast.makeText(this,"过户信息不能为空",Toast.LENGTH_LONG).show();
                }else if(!IsNullEdit(edt_licheng)){
                    Toast.makeText(this,"里程不能为空",Toast.LENGTH_LONG).show();
                }
                else if(!IsNullEdit(edt_price)){
                    Toast.makeText(this,"价格不能为空",Toast.LENGTH_LONG).show();
                }else if (TextUtils.isEmpty(tv_status.getText().toString())||tv_status.getText().toString().trim().equals("请选择车辆状态")) {
                        tv_status.setBackgroundResource(R.drawable.rednull);
                        Toast.makeText(this,"车辆状态不能为空",Toast.LENGTH_LONG).show();}else if (TextUtils.isEmpty(zqfPath)){
                    Toast.makeText(this,"左前45°图片不能为空",Toast.LENGTH_LONG).show();
                }
                else if (TextUtils.isEmpty(zqPath)){
                    Toast.makeText(this,"正前图片不能为空",Toast.LENGTH_LONG).show();
                }
                else if (TextUtils.isEmpty(zhfPath)){
                    Toast.makeText(this,"正后图片不能为空",Toast.LENGTH_LONG).show();
                }

                else{
                    Log.e("TAG","BeanFlag.Flag=="+ BeanFlag.Flag);
                    if(!BeanFlag.Flag){//fragment1进来，需要补充车源
                        mydialog=new Mydialog(this,"请稍等...");
                        Log.e("TAG","fragemtn1进来点击提交");
                        Log.e("TAG","先上传图片===="+zqfPath);
                        Log.e("TAG","先上传图片"+(TextUtils.isEmpty(zqfPath)&&TextUtils.isEmpty(zqPath)&&TextUtils.isEmpty(zhfPath)));
                        if(zqfPath.contains("http")
                                &&zqPath.contains("http")
                                &&zhfPath.contains("http")
                                &&TextUtils.isEmpty(img4Path)&&TextUtils.isEmpty(img5Path)&&TextUtils.isEmpty(img6Path)
                                &&TextUtils.isEmpty(img7Path)&&TextUtils.isEmpty(img8Path)&&TextUtils.isEmpty(img9Path)){
                            //走修改接口
//                            setCartMsg();
                            updateCartMsg(CartID);
                            mydialog.show();
                            Log.e("TAG","走修改接口");
                        }else{
                            mydialog.show();
                            Log.e("TAG","修改图片==zqfPath="+zqfPath);
                            Log.e("TAG","修改图片==zqPath="+zqPath);
                            Log.e("TAG","修改图片==zhfPath="+zhfPath);
                            Log.e("TAG","修改图片==img4Path="+img4Path);
                            Log.e("TAG","修改图片==img5Path="+img5Path);
                            Log.e("TAG","修改图片==img6Path="+img6Path);
                            Log.e("TAG","修改图片==img7Path="+img7Path);
                            Log.e("TAG","修改图片==img8Path="+img8Path);
                            Log.e("TAG","修改图片==img9Path="+img9Path);
                            Log.e("TAG","修改图片zqf=boolean=="+(!TextUtils.isEmpty(zqPath)&&!zqPath.contains("http")));
                            imgCount=0;
                            if(!TextUtils.isEmpty(zqfPath)&&!zqfPath.contains("http")){
                                imgCount++;
                                zqfUrlPath="";
                                updateImag(zqfPath);
                            }
                            if(!TextUtils.isEmpty(zqPath)&&!zqPath.contains("http")){
                                imgCount++;
                                zfUrlPath="";
                                updateImag(zqPath);
                            }
                            if(!TextUtils.isEmpty(zhfPath)&&!zhfPath.contains("http")){
                                imgCount++;
                                zhfUrlPath="";
                                updateImag(zhfPath);
                            }
                            if(!TextUtils.isEmpty(img4Path)){
                                imgCount++;
                                img4UrlPath="";
                                updateImag(img4Path);
                            }
                            if(!TextUtils.isEmpty(img5Path)){
                                imgCount++;
                                img5UrlPath="";
                                updateImag(img5Path);
                            }
                            if(!TextUtils.isEmpty(img6Path)){
                                imgCount++;
                                img6UrlPath="";
                                updateImag(img6Path);
                            }
                            if(!TextUtils.isEmpty(img7Path)){
                                imgCount++;
                                img7UrlPath="";
                                updateImag(img7Path);
                            }
                            if(!TextUtils.isEmpty(img8Path)){
                                imgCount++;
                                img8UrlPath="";
                                updateImag(img8Path);
                            }
                            if(!TextUtils.isEmpty(img9Path)){
                                imgCount++;
                                img9UrlPath="";
                                updateImag(img9Path);
                            }

                        }
                    }else {

//                        if (TextUtils.isEmpty(zqfPath)){
//                            Toast.makeText(this,"左前45°图片不能为空",Toast.LENGTH_LONG).show();
//                        }
//                        else if (TextUtils.isEmpty(zqPath)){
//                            Toast.makeText(this,"正前图片不能为空",Toast.LENGTH_LONG).show();
//                        }
//                        else if (TextUtils.isEmpty(zhfPath)){
//                            Toast.makeText(this,"正后图片不能为空",Toast.LENGTH_LONG).show();
//                        }
////                        else if (TextUtils.isEmpty(img4Path)){
////                            Toast.makeText(this,"img4Path正后图片不能为空",Toast.LENGTH_LONG).show();
////                        } else if (TextUtils.isEmpty(img5Path)){
////                            Toast.makeText(this,"img5Path正后图片不能为空",Toast.LENGTH_LONG).show();
////                        } else if (TextUtils.isEmpty(img6Path)){
////                            Toast.makeText(this,"img6Path正后图片不能为空",Toast.LENGTH_LONG).show();
////                        } else if (TextUtils.isEmpty(img7Path)){
////                            Toast.makeText(this,"img7Path正后图片不能为空",Toast.LENGTH_LONG).show();
////                        } else if (TextUtils.isEmpty(img8Path)){
////                            Toast.makeText(this,"img8Path正后图片不能为空",Toast.LENGTH_LONG).show();
////                        } else if (TextUtils.isEmpty(img9Path)){
////                            Toast.makeText(this,"img9Path正后图片不能为空",Toast.LENGTH_LONG).show();
////                        }
//                        else {//对应Flag为false,从巡场过来的
////                            mydialog.show();
////                            initGetDate(CartID);//保存本地数据库
////                            Log.e("TAG", "修改=" + zqfPath);
////                            updateImag(zqfPath);
////                            updateImag(zqPath);
////                            updateImag(zhfPath);
//updateCartMsg(CartID);
//                        }
                        mydialog=new Mydialog(this,"请稍等...");
                        Log.e("TAG","fragemtn1进来点击提交");
                        Log.e("TAG","先上传图片===="+zqfPath);
                        Log.e("TAG","先上传图片"+(TextUtils.isEmpty(zqfPath)&&TextUtils.isEmpty(zqPath)&&TextUtils.isEmpty(zhfPath)));
                        if(zqfPath.contains("http")
                                &&zqPath.contains("http")
                                &&zhfPath.contains("http")
                                &&TextUtils.isEmpty(img4Path)&&TextUtils.isEmpty(img5Path)&&TextUtils.isEmpty(img6Path)
                                &&TextUtils.isEmpty(img7Path)&&TextUtils.isEmpty(img8Path)&&TextUtils.isEmpty(img9Path)){
                            //走修改接口
//                            setCartMsg();
                            updateCartMsg(CartID);
                            mydialog.show();
                            Log.e("TAG","走修改接口");
                        }else{
                            mydialog.show();
                            Log.e("TAG","修改图片==zqfPath="+zqfPath);
                            Log.e("TAG","修改图片==zqPath="+zqPath);
                            Log.e("TAG","修改图片==zhfPath="+zhfPath);
                            Log.e("TAG","修改图片==img4Path="+img4Path);
                            Log.e("TAG","修改图片==img5Path="+img5Path);
                            Log.e("TAG","修改图片==img6Path="+img6Path);
                            Log.e("TAG","修改图片==img7Path="+img7Path);
                            Log.e("TAG","修改图片==img8Path="+img8Path);
                            Log.e("TAG","修改图片==img9Path="+img9Path);
                            Log.e("TAG","修改图片zqf=boolean=="+(!TextUtils.isEmpty(zqPath)&&!zqPath.contains("http")));
                            imgCount=0;
                            if(!TextUtils.isEmpty(zqfPath)&&!zqfPath.contains("http")){
                                imgCount++;
                                zqfUrlPath="";
                                updateImag(zqfPath);
                            }
                            if(!TextUtils.isEmpty(zqPath)&&!zqPath.contains("http")){
                                imgCount++;
                                zfUrlPath="";
                                updateImag(zqPath);
                            }
                            if(!TextUtils.isEmpty(zhfPath)&&!zhfPath.contains("http")){
                                imgCount++;
                                zhfUrlPath="";
                                updateImag(zhfPath);
                            }
                            if(!TextUtils.isEmpty(img4Path)){
                                imgCount++;
                                img4UrlPath="";
                                updateImag(img4Path);
                            }
                            if(!TextUtils.isEmpty(img5Path)){
                                imgCount++;
                                img5UrlPath="";
                                updateImag(img5Path);
                            }
                            if(!TextUtils.isEmpty(img6Path)){
                                imgCount++;
                                img6UrlPath="";
                                updateImag(img6Path);
                            }
                            if(!TextUtils.isEmpty(img7Path)){
                                imgCount++;
                                img7UrlPath="";
                                updateImag(img7Path);
                            }
                            if(!TextUtils.isEmpty(img8Path)){
                                imgCount++;
                                img8UrlPath="";
                                updateImag(img8Path);
                            }
                            if(!TextUtils.isEmpty(img9Path)){
                                imgCount++;
                                img9UrlPath="";
                                updateImag(img9Path);
                            }

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
            case R.id.img4_newfragment:
                getPicView(img4_newfragment);
                picName="img4";
                img4_newfragment.setBackgroundResource(0);
                break;
            case R.id.img5_newfragment:
                getPicView(img5_newfragment);
                picName="img5";
                img5_newfragment.setBackgroundResource(0);
                break;
            case R.id.img6_newfragment:
                getPicView(img6_newfragment);
                picName="img6";
                img6_newfragment.setBackgroundResource(0);
                break;
            case R.id.img7_newfragment:
                getPicView(img7_newfragment);
                picName="img7";
                img7_newfragment.setBackgroundResource(0);
                break;
            case R.id.img8_newfragment:
                getPicView(img8_newfragment);
                picName="img8";
                img8_newfragment.setBackgroundResource(0);
                break;
            case R.id.img9_newfragment:
                getPicView(img9_newfragment);
                picName="img9";
                img9_newfragment.setBackgroundResource(0);
                break;
            case R.id.tv_cartmodel:
                Intent intent6=new Intent(this, CartModelActivity.class);
                startActivity(intent6);
                break;
            case R.id.tv_paizhao2:
                //调取相机功能
//                startActivity(new Intent(getContext(), TakePhoteActivity.class));
                Log.e("TAG","调用本地相机");
                getXiangji(picName);
//                if(setPermissions()) {
//                    Intent intent5 = new Intent(this, CameraActivity.class);
//                    intent5.putExtra("name", picName);
//                    int h=this.getWindowManager().getDefaultDisplay().getHeight();
//                    Log.e("TAG","屏幕高度=="+h);
//                    intent5.putExtra("height", "466");
//                    startActivity(intent5);
//                }
                window2.dismiss();
                break;
            case R.id.tv_xiangce2:
                takePicture();
                window2.dismiss();
                break;
            case R.id.tv_canle2:
                if(window2!=null&&window2.isShowing()){
                    window2.dismiss();
                }
                break;
            case R.id.tv_pop_guohu:
                if(window3!=null&&window3.isShowing()) {
                    if (tv_paizhao3 != null) {
                        Tv_guohu.setText(tv_paizhao3.getText().toString());
                        guohuID = "0";
                        Log.e("TAG", "为什走这里么？");
                        window3.dismiss();
                    }
                }

                break;
            case R.id.tv_pop_weiguohu:
                Tv_guohu.setText(tv_xiangce3.getText().toString());
                guohuID="1";//未过户
                window3.dismiss();
                break;
            case R.id.tv_pop_xiajia:
                if(window4.isShowing()){
                    window4.dismiss();
                }
                tv_status.setText("下架");
                tv_status.setBackgroundResource(R.drawable.juxingnull);
                status="0";
                break;
            case R.id.tv_pop_zhengchang:
                if (window4.isShowing()){
                    window4.dismiss();
                }
                tv_status.setText("正常");
                tv_status.setBackgroundResource(R.drawable.juxingnull);
                status="1";
                break;
        }
    }
    //车辆状态
    private void getStatus(){
        popView4= View.inflate(this,R.layout.mystatus_popview,null);
        LinearLayout pop_linear=popView4.findViewById(R.id.pop_linear4);
        tv_paizhao4=popView4.findViewById(R.id.tv_pop_zhengchang);
        tv_xiangce4=popView4.findViewById(R.id.tv_pop_xiajia);
        tv_canle4=popView4.findViewById(R.id.tv_canle3);
        window4=new PopupWindow(this);
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        pop_linear.measure(w, h);
        int pop_height = pop_linear.getMeasuredHeight();
        int pop_width = pop_linear.getMeasuredWidth();
        int width=this.getWindowManager().getDefaultDisplay().getWidth();
        int height=this.getWindowManager().getDefaultDisplay().getHeight();
        Log.e("TAG","测量333333333h="+pop_height+"=="+width+"=="+(window3==null));

        window4.setWidth(width);
        window4.setHeight(pop_height);
        // 设置PopupWindow的背景
        window4.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window4.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window4.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        window4.setContentView(popView4);
        window4.setAnimationStyle(R.style.animTranslate);
        window4.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp=BuMessageActivity.this.getWindow().getAttributes();
                lp.alpha=1.0f;
                BuMessageActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                BuMessageActivity.this.getWindow().setAttributes(lp);
            }
        });
        window4.showAtLocation(tv_status, Gravity.BOTTOM,0,0);
//        window3.showAsDropDown(Tv_guohu);
//        window3.showAsDropDown(Tv_guohu,0,0,Gravity.NO_GRAVITY);
        WindowManager.LayoutParams lp=BuMessageActivity.this.getWindow().getAttributes();
        lp.alpha=0.3f;
        BuMessageActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        BuMessageActivity.this.getWindow().setAttributes(lp);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        // window.showAtLocation(parent, gravity, x, y);
        tv_xiangce4.setOnClickListener(this);
        tv_paizhao4.setOnClickListener(this);
//        tv_canle3.setOnClickListener(this);
        Log.e("TAG","window33333333=="+window4.getWidth()+"height=="+window4.getHeight());
    }
    // 获取价格
    private void getPrice(final String string) {
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
                list= GetJsonUtils.getCartMsg(BuMessageActivity.this,result);
                if(!TextUtils.isEmpty(string)&&string.equals("model")){
                    edt_price.setText(list.get(0).price.toString());
                    edt_licheng.setText(list.get(0).licheng.toString());
                    getSubStr(edt_price);
                    getSubStr(edt_licheng);
//                    tv_time.setText(list.get(0).data.toString());
                    seriesid=list.get(0).series_id;
                    brandid=list.get(0).brand_id;
                    Log.e("TAG","seriesid=="+seriesid+"=="+list.get(0).series_id);
                    MyModelDialog myModelDialog=new MyModelDialog(BuMessageActivity.this,ModelNameandID.list);
                    myModelDialog.show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG","getPrice=="+ex.getMessage().toString());
                if(mydialog1.isShowing()){
                    if(!TextUtils.isEmpty(ex.getMessage().toString())){
                        mydialog1.dismiss();
                        Toast.makeText(BuMessageActivity.this,"获取失败",Toast.LENGTH_LONG).show();
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

    private void initGetDate(String cartItemID) {
//        count++;
//        Log.e("TAG","count条数=="+count);
//        utils.saveXML(MyApplication.cartlistmsg,"vin"+posion,edit_num.getText().toString(),this);
//        utils.saveXML(MyApplication.cartlistmsg,"name"+posion,tv_cartmodel.getText().toString(),this);
//        utils.saveXML(MyApplication.cartlistmsg,"licensePlate"+posion,tv_quyue.getText().toString(),this);
//        utils.saveXML(MyApplication.cartlistmsg,"cardType"+posion,tv_cartmodel.getText().toString()
//                ,this);
//        //当前key值的posion
//        Log.e("TAG","存入的所有posion"+"posion=="+posion+"=="+count);
//        utils.saveXML(MyApplication.cartlistmsg,"posion"+count,posion,this);
//        //一共保存条数count
//        utils.saveXML(MyApplication.cartlistmsg,"count",count+"",this);
//        Toast.makeText(this,"保存",Toast.LENGTH_SHORT).show();
//        finish();
        MyDBUtils myDBUtils=new MyDBUtils(this);
        Log.e("TAG","过户=="+Tv_guohu.getText().toString()+"==guohuID=="+guohuID);
        Log.e("TAG","tv_time=="+tv_time.getText().toString()
        +"\n"+cartName);
        if(!edt_name.getText().toString().equals(list.get(0).contact_name)||!tv_tel.getText().toString().equals(list.get(0).tel)){
            picID="0";
        };

        Log.e("TAG","保存picID=="+picID);
        Log.e("TAG","保存zqfPath=="+zqfPath);
        String[] str=new String[]{cartItemID,tv_quyue.getText().toString(),quyuID,edit_num.getText().toString(),
                tv_time.getText().toString(),edt_name.getText().toString(),tv_tel.getText().toString(),
                brangName,brandid,seriseName,seriesid,cartName,modelid,tv_cartFenlei.getText().toString(),fenleiID,
                Tv_guohu.getText().toString(),guohuID,edt_licheng.getText().toString(),edt_price.getText().toString(),zqfPath,zqPath,zhfPath,
                img4Path,img5Path,img6Path,img7Path,img8Path,img9Path,picID
        };

        List<Bran>branlist=myDBUtils.chaXun();

        if(branlist.size()>0) {
            for (int i = 0; i < branlist.size(); i++) {
                Log.e("TAG","数据库中有没有状态数据--"+branlist.get(i).itemid);
                Log.e("TAG","要添加的ID==="+cartItemID);
                if (branlist.get(i).itemid.equals(cartItemID)) {
                    myDBUtils.setBulu(str);
                    break;
                } else {
                    if (i == branlist.size() - 1) {
                        myDBUtils.addBuLu(str);
                    }
                }

            }
        }else{
            myDBUtils.addBuLu(str);
        }
        finish();
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
    //显示日期
    private void showDate(final TextView Tv) {
        Calendar calend1 = Calendar.getInstance();
        calend1.setTimeInMillis(System.currentTimeMillis());
        int year = calend1.get(Calendar.YEAR);
        int month = calend1.get(Calendar.MONTH) + 1;
        int day = calend1.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog3 = new DatePickerDialog(
                this,
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
        //设置起始日期和结束日期
        DatePicker datePicker = dialog3.getDatePicker();
        //datePicker.setMinDate();
        datePicker.setMaxDate(System.currentTimeMillis());
        dialog3.show();
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
    //获取popwindow
    private void getPopView(){
        popView= View.inflate(this,R.layout.popwiew,null);
        LinearLayout pop_linear=popView.findViewById(R.id.pop_linear);
        tv_paizhao=popView.findViewById(R.id.tv_paizhao);
        tv_xiangce=popView.findViewById(R.id.tv_xiangce);
        tv_canle=popView.findViewById(R.id.tv_canle);
        window=new PopupWindow(this);
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        pop_linear.measure(w, h);
        int pop_height = pop_linear.getMeasuredHeight();
        int pop_width = pop_linear.getMeasuredWidth();
        int width=getWindowManager().getDefaultDisplay().getWidth();
        int height=getWindowManager().getDefaultDisplay().getHeight();
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
                WindowManager.LayoutParams lp=getWindow().getAttributes();
                lp.alpha=1.0f;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
            }
        });
//        window.showAtLocation(img_circle, Gravity.BOTTOM,0,0);
        window.showAsDropDown(img_paizhao,0,0);
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=0.3f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        // window.showAtLocation(parent, gravity, x, y);
        tv_xiangce.setOnClickListener(this);
        tv_paizhao.setOnClickListener(this);
        tv_canle.setOnClickListener(this);
    }
    static final String[] PERMISSION = new String[]{
            Manifest.permission.READ_CONTACTS,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,  //读取权限
            Manifest.permission.WRITE_CALL_LOG,        //读取设备信息
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
    };
    /**
     * 设置Android6.0的权限申请
     */
    private boolean setPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //Android 6.0申请权限
            Toast.makeText(this,"没有相关权限，请先开启",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,PERMISSION,1);
        }else{
            return true;
        }
        return false;
    }
    //获取popwindow
    ImageView selectImag=null;
    private void getPicView(ImageView imageView){
        selectImag=imageView;
        popView2= View.inflate(this,R.layout.popwiew2,null);
        LinearLayout pop_linear=popView2.findViewById(R.id.pop_linear);
        tv_paizhao2=popView2.findViewById(R.id.tv_paizhao2);
        tv_xiangce2=popView2.findViewById(R.id.tv_xiangce2);
        tv_canle2=popView2.findViewById(R.id.tv_canle2);
        window2=new PopupWindow(this);
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        pop_linear.measure(w, h);
        int pop_height = pop_linear.getMeasuredHeight();
        int pop_width = pop_linear.getMeasuredWidth();
        Log.e("TAG","测量h="+pop_height);
        int width=getWindowManager().getDefaultDisplay().getWidth();
        int height=getWindowManager().getDefaultDisplay().getHeight();
        window2.setWidth(width);
        window2.setHeight(pop_height);
        // 设置PopupWindow的背景
        window2.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window2.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window2.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        window2.setContentView(popView2);
        window2.setAnimationStyle(R.style.animTranslate);
        window2.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp=getWindow().getAttributes();
                lp.alpha=1.0f;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
            }
        });
        window2.showAtLocation(tv_topcenter, Gravity.BOTTOM,0,0);
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=0.3f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        // window.showAtLocation(parent, gravity, x, y);
        tv_xiangce2.setOnClickListener(this);
        tv_paizhao2.setOnClickListener(this);
        tv_canle2.setOnClickListener(this);
        Log.e("TAG","window=="+window2.getWidth()+"height=="+window2.getHeight());
    }
    //调取本地图库
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
        FileUtil fileUtil=new FileUtil(this);

        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                Log.e("TAG","这里走了吗");
                String photoPath="";
                Uri uri = data.getData();
                ContentResolver resolver=this.getContentResolver();
                Bitmap bitmap= null;
                //                    bitmap = MediaStore.Images.Media.getBitmap(resolver,uri);
////                    selectImag.setImageBitmap(bitmap);
//                    //获取图片路径
//                    String picPath="";
//                    //获取照片路径
//                    String[] filePathColumn = {MediaStore.Audio.Media.DATA};
//                    Cursor cursor = this.getContentResolver().query(data.getData(), filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//                    photoPath  = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
//                    cursor.close();
//                    Log.i(TAG, "photoPath = "+photoPath+"length=="+new File(photoPath).length()/1024);
                photoPath= getPicTku.getImageAbsolutePath(this,uri);
                Log.e("TAG","photoPath=="+photoPath);
                if(photoPath!=null) {
                    // 设置参数
                  Bitmap  bm=ImgRote.getyasuo(photoPath);
//                    selectImag.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    selectImag.setScaleType(ImageView.ScaleType.FIT_XY);
//                    selectImag.setImageBitmap(bm);
                    if(bm!=null) {
                        bm = ImgRote.rotateBitmapByDegree(bm, ImgRote.getBitmapDegree(photoPath));
                        new FileUtil(this).saveBitmap(bm);
                        photoPath = FileUtil.getJpegName();
                        selectImag.setImageBitmap(new FileUtil(this).readBitmap(photoPath));
                    }
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
                }else if(selectImag==img4_newfragment){
                    img4Path=photoPath;
                }else if(selectImag==img5_newfragment){
                    img5Path=photoPath;
                }
                else if(selectImag==img6_newfragment){
                    img6Path=photoPath;
                }else if(selectImag==img7_newfragment){
                    img7Path=photoPath;
                }else if(selectImag==img8_newfragment){
                    img8Path=photoPath;
                }else if(selectImag==img9_newfragment){
                    img9Path=photoPath;
                }
            }
        }else if(requestCode==PHOTOTAKE){
//            Bundle bundle = data.getExtras();
//            Bitmap bit= (Bitmap) bundle.get("data");
            Bitmap  bit=BitmapFactory.decodeFile(imgtakePath);
            if(bit!=null) {
                Log.e("TAG", "bit压缩前==" + bit.getWidth() + "/" + bit.getHeight());
                bit = ImgRote.getyasuo(imgtakePath);
                bit = ImgRote.rotateBitmapByDegree(bit, ImgRote.getBitmapDegree(imgtakePath));
                Log.e("TAG", "bit压缩后==" + bit.getWidth() + "/" + bit.getHeight());
                new FileUtil(this).saveBitmap(bit);
                if (picName.equals("zuoqian")) {
                    img_newfragment.setScaleType(ImageView.ScaleType.FIT_XY);
                    img_newfragment.setImageBitmap(new FileUtil(this).readBitmap(FileUtil.getJpegName()));
                    zqfPath = FileUtil.getJpegName();
                    Log.e("TAG", "length==" + new File(zqfPath).length() / 1024);
                } else if (picName.equals("zhengqian")) {
                    img2_newfragment.setImageBitmap(new FileUtil(this).readBitmap(FileUtil.getJpegName()));
                    zqPath = FileUtil.getJpegName();
                    Log.e("TAG", "length==" + new File(zqPath).length() / 1024);
                } else if (picName.equals("zhenghou")) {
                    img3_newfragment.setImageBitmap(new FileUtil(this).readBitmap(FileUtil.getJpegName()));
                    zhfPath = FileUtil.getJpegName();
                    Log.e("TAG", "length==" + new File(zhfPath).length() / 1024);
                }else if(picName.equals("img4")){
                    img4_newfragment.setImageBitmap(new FileUtil(this).readBitmap(FileUtil.getJpegName()));
                    img4Path=FileUtil.getJpegName();
                }else if(picName.equals("img5")){
                    img5_newfragment.setImageBitmap(new FileUtil(this).readBitmap(FileUtil.getJpegName()));
                    img5Path=FileUtil.getJpegName();
                }else if(picName.equals("img6")){
                    img6_newfragment.setImageBitmap(new FileUtil(this).readBitmap(FileUtil.getJpegName()));
                    img6Path=FileUtil.getJpegName();
                }else if(picName.equals("img7")){
                    img7_newfragment.setImageBitmap(new FileUtil(this).readBitmap(FileUtil.getJpegName()));
                    img7Path=FileUtil.getJpegName();
                }else if(picName.equals("img8")){
                    img8_newfragment.setImageBitmap(new FileUtil(this).readBitmap(FileUtil.getJpegName()));
                    img8Path=FileUtil.getJpegName();
                }else if(picName.equals("img9")){
                    img9_newfragment.setImageBitmap(new FileUtil(this).readBitmap(FileUtil.getJpegName()));
                    img9Path=FileUtil.getJpegName();
                }
            }
        } // 扫描二维码/条码回传
        else if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
//                Toast.makeText(getActivity(),"扫描结果为："+content,Toast.LENGTH_SHORT).show();
                Log.e("TAG","扫描结果为=="+content);
//                {"rfid":"d2a13ab3730b446aae8e6bc37cb014cb","carinfo":[]}
//                if(content.length()>32){
//                    Toast.makeText(BuMessageActivity.this,"此标签已绑定车辆",Toast.LENGTH_SHORT).show();
//                }else {
//                    tv_carnum.setText(content + "");
                    getRFID(content);
//                }
//                扫描结果为==http://tjkg.zgcw.cn:9008/carinfo.html?muid=tianjin001&code=5158
//                Intent intent =new Intent(getActivity(),WebViewActivity.class);
//                Intent intent=new Intent(this,BuMessageActivity.class);
//                intent.putExtra("url",content);
//                String arr[]=content.split("&");
//                Log.e("TAG","arr[1]="+arr[1].toString());//=5158
//                int index=arr[1].toString().indexOf("=");//获取等号的位置
//                String cartID=arr[1].substring(index+1,arr[1].length());
//                Log.e("TAG","cartID=="+cartID);
//                intent.putExtra("cartID",cartID);
//                startActivity(intent);
            }
        }
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
        registerReceiver(myBroadcastReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
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
                tv_status.setBackgroundResource(R.drawable.juxingnull);
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
                Log.e("TAG","修改接受到的图片地址=="+ ZQFBean.zqpath);
                ZQBean.zqpath=MyNewUpdate.img2;
                ZHFBean.zhfpath=MyNewUpdate.img3;
                if(!TextUtils.isEmpty(MyNewUpdate.img1)) {
                    Glide.with(BuMessageActivity.this).load(MyNewUpdate.img1).placeholder(R.drawable.zq45d).error(R.drawable.zq45d).into(img_newfragment);
                }
                if(!TextUtils.isEmpty(MyNewUpdate.img2)) {
                    Glide.with(BuMessageActivity.this).load(MyNewUpdate.img2).placeholder(R.drawable.zqf).error(R.drawable.zqf).into(img2_newfragment);
                }
                if(!TextUtils.isEmpty(MyNewUpdate.img3)) {
                    Glide.with(BuMessageActivity.this).load(MyNewUpdate.img3).placeholder(R.drawable.zhf).error(R.drawable.zhf).into(img3_newfragment);
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
                            NameAndTelDialog nameAndTelDialog = new NameAndTelDialog(BuMessageActivity.this, MySerchActvity.nameAndTelList.get(Integer.parseInt(currentID)));
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
                            Log.e("TAG","MyodelIDandName=="+ ModelNameandID.list.size());
                            MyModelDialog myModelDialog=new MyModelDialog(BuMessageActivity.this,ModelNameandID.list);
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
                tv_status.setText("");
                ZQFBean.zqpath="";ZQBean.zqpath="";ZHFBean.zhfpath="";str="";
                zqfPath="";zqPath="";zhfPath="";
                edit_num.setFocusableInTouchMode(true);
                edit_num.setFocusable(true);
                img_newfragment.setImageBitmap(BitmapFactory.decodeResource(BuMessageActivity.this.getResources(),R.drawable.zq45d));
                img2_newfragment.setImageBitmap(BitmapFactory.decodeResource(BuMessageActivity.this.getResources(),R.drawable.zqf));
                img3_newfragment.setImageBitmap(BitmapFactory.decodeResource(BuMessageActivity.this.getResources(),R.drawable.zhf));
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

    //获取相机
    String imgtakePath;
    public void getXiangji(String picName){
//        selectImag=img;
        String  photoSaveName = String.valueOf(System.currentTimeMillis()) + ".jpg";
        String  photoSavePath=this.getExternalCacheDir().getAbsolutePath()+"/";
        imgtakePath=photoSavePath+photoSaveName;//图片完整路径
        Log.e("TAG","这里获取完整路=="+imgtakePath);
        Uri imageUri = null;
        File file=new File(imgtakePath);
        if(!file.exists()){
            file.getParentFile();
        }
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  //针对Android7.0，需要通过FileProvider封装过的路径，提供给外部调用
            Log.e("TAG","这里走到了吗》7.0");
            openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Log.e("TAG","file=="+file);
            imageUri = FileProvider.getUriForFile(BuMessageActivity.this, "com.example.a123456.hebcartzhonggu.fileprovider", file);//通过FileProvider创建一个content类型的Uri，进行封装
            Log.e("TAG","这里走到了下》7.0");
        } else { //7.0以下，
            imageUri=Uri.fromFile(file);
        }
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, PHOTOTAKE);
    }
    private void getGuohu(){
        popView3= View.inflate(this,R.layout.myguohu_popview,null);
        LinearLayout pop_linear=popView3.findViewById(R.id.pop_linear3);
        tv_paizhao3=popView3.findViewById(R.id.tv_pop_guohu);
        tv_xiangce3=popView3.findViewById(R.id.tv_pop_weiguohu);
        tv_canle3=popView3.findViewById(R.id.tv_canle3);
        window3=new PopupWindow(this);
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        pop_linear.measure(w, h);
        int pop_height = pop_linear.getMeasuredHeight();
        int pop_width = pop_linear.getMeasuredWidth();
        int width=getWindowManager().getDefaultDisplay().getWidth();
        int height=getWindowManager().getDefaultDisplay().getHeight();
        Log.e("TAG","测量333333333h="+pop_height+"=="+width+"=="+(window3==null));

        window3.setWidth(width);
        window3.setHeight(pop_height);
        // 设置PopupWindow的背景
        window3.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window3.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window3.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        window3.setContentView(popView3);
        window3.setAnimationStyle(R.style.animTranslate);
        window3.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp=getWindow().getAttributes();
                lp.alpha=1.0f;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
            }
        });
        window3.showAtLocation(Tv_guohu, Gravity.BOTTOM,0,0);
//        window3.showAsDropDown(Tv_guohu);
//        window3.showAsDropDown(Tv_guohu,0,0,Gravity.NO_GRAVITY);
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=0.3f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        // window.showAtLocation(parent, gravity, x, y);
        tv_xiangce3.setOnClickListener(this);
        tv_paizhao3.setOnClickListener(this);
//        tv_canle3.setOnClickListener(this);
        Log.e("TAG","window33333333=="+window3.getWidth()+"height=="+window3.getHeight());
    }
    private void getItemCartDate(){
//        http://mkerp.zgcw.cn/api/api_car/getInfo?id=1695&json=1&makeup=0
        RequestParams params=new RequestParams(getInterface.CartItem);
        params.addBodyParameter("id",CartID);
        params.addBodyParameter("json","1");
        params.addBodyParameter("makeup","0");
        Log.e("TAG","params详情地址为=="+params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG","详情为==："+result+"==");
              list=GetJsonUtils.CartListItem(BuMessageActivity.this,result);
              Log.e("TAG","车源编号："+(TextUtils.isEmpty(list.get(0).rfid_id))+"=="+list.get(0).rfid_id.equals("null")+"=="+list.get(0).rfid_id);
              if(TextUtils.isEmpty(list.get(0).rfid_id)||list.get(0).rfid_id.equals("null")){
                  tv_carnum.setText("请扫描二维码获取车源编号");
              }else{
                  tv_carnum .setText(list.get(0).rfid_id+"");
              }
                Log.e("TAG","详情为==："+tv_carnum.getText());
              tv_quyue.setText(list.get(0).quyuName);
              quyuID=list.get(0).quyuID;
              edit_num.setText(list.get(0).vin);
              tv_time.setText(list.get(0).regTime);
              picID=list.get(0).NameTelID;

              edt_name.setText(list.get(0).contact_name);
              tv_tel.setText(list.get(0).tel);
              brandid=list.get(0).brandid;
              seriesid=list.get(0).seriseID;
              modelid=list.get(0).modelID;
              brangName=list.get(0).brandName;
              seriseName=list.get(0).seriseName;
              cartName=list.get(0).modelName;
              tv_cartmodel.setText(list.get(0).modelName);
              fenleiID=list.get(0).isDaTing;
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
                guohuID=list.get(0).transterstatus;
                if(guohuID.equals("0")){
                    //已过户
                    Tv_guohu.setText("已过户");
                }else{
                    Tv_guohu.setText("未过户");
                }
                status=list.get(0).status;
                if(status.equals("0")){
                    tv_status.setText("下架");
                }else{
                    tv_status.setText("正常");
                }
                edt_licheng.setText(list.get(0).mileage);
                edt_price.setText(list.get(0).price);
                zqfPath=list.get(0).img1;
                zqPath=list.get(0).img2;
                zhfPath=list.get(0).img3;
                zqfUrlPath=list.get(0).img1;
                zfUrlPath=list.get(0).img2;
                zhfUrlPath=list.get(0).img3;
                img4UrlPath=list.get(0).img4;
                img5UrlPath=list.get(0).img5;
                img6UrlPath=list.get(0).img6;
                img7UrlPath=list.get(0).img7;
                img8UrlPath=list.get(0).img8;
                img9UrlPath=list.get(0).img9;
                if(list.get(0).img1.contains("http")) {
                    Glide.with(BuMessageActivity.this).load(list.get(0).img1+"?x-oss-process=style/233_162").placeholder(R.drawable.zq45d).error(R.drawable.zq45d).into(img_newfragment);
                }
                if(list.get(0).img2 .contains("http")) {
                    Glide.with(BuMessageActivity.this).load(list.get(0).img2+"?x-oss-process=style/233_162").placeholder(R.drawable.zqf).error(R.drawable.zqf).into(img2_newfragment);
                }
                if(list.get(0).img3 .contains("http")){
                    Glide.with(BuMessageActivity.this).load(list.get(0).img3+"?x-oss-process=style/233_162").placeholder(R.drawable.zhf).error(R.drawable.zhf).into(img3_newfragment);
                }
                if(list.get(0).img4.contains("http")) {
                    Log.e("TAG", "img4==" + list.get(0).img4);
                    Glide.with(BuMessageActivity.this).load(list.get(0).img4 + "?x-oss-process=style/233_162").placeholder(R.drawable.yh45d).error(R.drawable.yh45d).into(img4_newfragment);
                }
                if(list.get(0).img5.contains("http")){
                    Glide.with(BuMessageActivity.this).load(list.get(0).img5+"?x-oss-process=style/233_162").placeholder(R.drawable.fdjc).error(R.drawable.fdjc).into(img5_newfragment);
                }
                if(list.get(0).img6.contains("http")){
                    Glide.with(BuMessageActivity.this).load(list.get(0).img6+"?x-oss-process=style/233_162").placeholder(R.drawable.lt).error(R.drawable.lt).into(img6_newfragment);
                }
                if(list.get(0).img7.contains("http")){
                    Glide.with(BuMessageActivity.this).load(list.get(0).img7+"?x-oss-process=style/233_162").placeholder(R.drawable.td).error(R.drawable.td).into(img7_newfragment);
                }
                if(list.get(0).img8.contains("http")){
                    Glide.with(BuMessageActivity.this).load(list.get(0).img8+"?x-oss-process=style/233_162").placeholder(R.drawable.wd).error(R.drawable.wd).into(img8_newfragment);
                }
                 if(list.get(0).img9.contains("http")) {
                     Glide.with(BuMessageActivity.this).load(list.get(0).img9 + "?x-oss-process=style/233_162").placeholder(R.drawable.zktzz).error(R.drawable.zktzz).into(img9_newfragment);
                 }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void updateImag(final String path){
        if(!mydialog.isShowing()) {
            mydialog.show();
        }
        final RequestParams params=new RequestParams(getInterface.UpdateImag);
        params.setMultipart(true);
        params.setConnectTimeout(80000);
        params.setMaxRetryCount(5);//
        Log.e("TAG","path路径=="+path);
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

                successCount++;
                Log.e("TAG","图片上传结果---"+result);
                Log.e("TAG","select4=="+"\n"+path+"\n"+img4Path+"\n"+path.equals(img4Path));
                if(!TextUtils.isEmpty(path)&&path.equals(zqfPath)){
                    zqfUrlPath= GetJsonUtils.getZQF(BuMessageActivity.this,result);
//                    str=GetJsonUtils.getZQF(getContext(),result);
                    Log.e("TAG","这里左前方=="+zqfUrlPath);
                }else if(!TextUtils.isEmpty(path)&&path.equals(zqPath)){
                    //正前方图pain
                    zfUrlPath= GetJsonUtils.getZQF(BuMessageActivity.this,result);
                    Log.e("TAG","这里正前方图片=="+zfUrlPath);
                }else if(!TextUtils.isEmpty(path)&&path.equals(zhfPath)){
                    //正后方图pain
                    zhfUrlPath= GetJsonUtils.getZQF(BuMessageActivity.this,result);
                    Log.e("TAG","这里正后方图片=="+zhfUrlPath);
                }else if(!TextUtils.isEmpty(path)&&path.equals(img4Path)){
                    //正后方图pain
                    img4UrlPath= GetJsonUtils.getZQF(BuMessageActivity.this,result);
                    Log.e("TAG","这里img4UrlPath图片=="+img4UrlPath);
                }else if(!TextUtils.isEmpty(path)&&path.equals(img5Path)){
                    //正后方图pain
                    img5UrlPath= GetJsonUtils.getZQF(BuMessageActivity.this,result);
                    Log.e("TAG","这里img5UrlPath图片=="+img5UrlPath);
                }else if(!TextUtils.isEmpty(path)&&path.equals(img6Path)){
                    //正后方图pain
                    img6UrlPath= GetJsonUtils.getZQF(BuMessageActivity.this,result);
                    Log.e("TAG","这里img6UrlPath图片=="+img6UrlPath);
                }else if(!TextUtils.isEmpty(path)&&path.equals(img7Path)){
                    //正后方图pain
                    img7UrlPath= GetJsonUtils.getZQF(BuMessageActivity.this,result);
                    Log.e("TAG","这里img7UrlPath图片=="+img7UrlPath);
                }else if(!TextUtils.isEmpty(path)&&path.equals(img8Path)){
                    //正后方图pain
                    img8UrlPath= GetJsonUtils.getZQF(BuMessageActivity.this,result);
                    Log.e("TAG","这里img8UrlPath图片=="+img8UrlPath);
                }
                else if(!TextUtils.isEmpty(path)&&path.equals(img9Path)){
                    //正后方图pain
                    img9UrlPath= GetJsonUtils.getZQF(BuMessageActivity.this,result);
                    Log.e("TAG","这里img9UrlPath图片=="+img9UrlPath);
                }
//                ZQFBean.zqpath=str;
                Log.e("TAG","上传图片结果左前方图片=="+zqfUrlPath);
                Log.e("TAG","上传图片结果正前方图片="+zfUrlPath);
                Log.e("TAg","上传图片结果正后方图片="+zhfUrlPath);
                Log.e("TAG","上传图片结果img4UrlPath图片=="+img4UrlPath);
                Log.e("TAG","上传图片结果img5UrlPath图片="+img5UrlPath);
                Log.e("TAg","上传图片结果img6UrlPath图片="+img6UrlPath);
                Log.e("TAG","上传图片结果img7UrlPath图片=="+img7UrlPath);
                Log.e("TAG","上传图片结果img8UrlPath图片="+img8UrlPath);
                Log.e("TAg","上传图片结果img9UrlPath图片="+img9UrlPath);
//                Log.e("TAG","三张大图上传结果=="+(!TextUtils.isEmpty(ZHFBean.zhfpath)&&!TextUtils.isEmpty(ZQBean.zqpath)&&!TextUtils.isEmpty(ZQFBean.zqpath)));
//                &&!TextUtils.isEmpty(img4UrlPath)&&!TextUtils.isEmpty(img5UrlPath)&&!TextUtils.isEmpty(img6UrlPath)
//                        &&!TextUtils.isEmpty(img7UrlPath)&&!TextUtils.isEmpty(img8UrlPath)&&!TextUtils.isEmpty(img9UrlPath)
                Log.e("TAG","successCount=="+successCount+"======imgCount=="+imgCount);
                if(!TextUtils.isEmpty(zqfUrlPath)&&!TextUtils.isEmpty(zfUrlPath)&&!TextUtils.isEmpty(zhfUrlPath)
                       &&(successCount==imgCount) ){
                    //上传全部信息
                    Log.e("TAG","上传补录车量信息====="+BeanFlag.Flag);
                    updateCartMsg(CartID);
//                    if(BeanFlag.Flag){
//                        //修改接口
//                        Log.e("TAG","修改接口");
////                        setCartMsg();
//                        updateCartMsg(CartID);
//                    }else {
//                        Log.e("TAG","上传接口");
//                        updateCartMsg(CartID);
//                    }

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG","onError()");
//                imgCount=0;
                errorCount++;
                if(!TextUtils.isEmpty(zqfPath)&&!zqfPath.contains("http")){
                    zqfUrlPath="";
                }
                if(!TextUtils.isEmpty(zqPath)&&!zqPath.contains("http")){
                    zfUrlPath="";
                }
                if(!TextUtils.isEmpty(zhfPath)&&!zhfPath.contains("http")){
                    zhfUrlPath="";
                }
                if(!TextUtils.isEmpty(img4Path)){
                    img4UrlPath="";
                }
                if(!TextUtils.isEmpty(img5Path)){
                    img5UrlPath="";
                }
                if(!TextUtils.isEmpty(img6Path)){
                    img6UrlPath="";
                }
                if(!TextUtils.isEmpty(img7Path)){
                   img7UrlPath="";
                }
                if(!TextUtils.isEmpty(img8Path)){
                    img8UrlPath="";
                }
                if(!TextUtils.isEmpty(img9Path)){
                    img9UrlPath="";
                }
                    if(!TextUtils.isEmpty(ex.getMessage().toString())) {
                        if (mydialog.isShowing()) {
                            mydialog.dismiss();
                        }
                        if (ex instanceof HttpException) { // 网络错误
                            HttpException httpEx = (HttpException) ex;
                            int responseCode = httpEx.getCode();
                            String responseMsg = httpEx.getMessage();
                            String errorResult = httpEx.getResult();
                            Log.e("TAG", "responseCode==" + responseCode + "=responseMsg=" + responseMsg + "=errorResult=" + errorResult);
                        } else {
// 其他错误//
                        }
                        Log.e("TAG", "ex.getMessage().toString()==" + ex.getMessage().toString());
//                        mydialog.dismiss();
//                        ZQFBean.zqpath="";ZQBean.zqpath="";ZHFBean.zhfpath="";str="";
                        if (errorCount == imgCount) {
                            Toast.makeText(BuMessageActivity.this, "图片上传失败", Toast.LENGTH_LONG).show();
                        }
                    }
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
    private void updateCartMsg(final String itemId){
        Log.e("TAG","开始上传");
        RequestParams requestParams=new RequestParams(getInterface.UpCartData);
        //    参数 时间   regDate  merchant_code 商家ID  vin,groupid,carName(车型),
        // mileage,target_price,userid,brandid,seriesid,zhengqian45,zhengqian,zhenghou
//        cartItemID,tv_quyue.getText().toString(),quyuID,edit_num.getText().toString(),
//                tv_time.getText().toString(),edt_name.getText().toString(),tv_tel.getText().toString(),
//                brangName,brandid,seriseName,seriesid,cartName,modelid,tv_cartFenlei.getText().toString(),fenleiID,
//                Tv_guohu.getText().toString(),guohuID,edt_licheng.getText().toString(),edt_price.getText().toString(),zqfPath,zqPath,zhfPath,
//                img4Path,img5Path,img6Path,img7Path,img8Path,img9Path,picID
        requestParams.addBodyParameter("id",itemId);
        requestParams.addBodyParameter("vin",edit_num.getText().toString());
        requestParams.addBodyParameter("merchant_code",quyuID);
        requestParams.addBodyParameter("groupid", UserBean.groupid);
        requestParams.addBodyParameter("userid",UserBean.id);
        requestParams.addBodyParameter("vendorId",brandid);
        requestParams.addBodyParameter("brandId",seriesid);
        requestParams.addBodyParameter("regDate",tv_time.getText().toString().trim());
        requestParams.addBodyParameter("mileage",edt_licheng.getText().toString());
        requestParams.addBodyParameter("target_price",edt_price.getText().toString());
        //九张大图
        requestParams.addBodyParameter("zhengqian45",zqfUrlPath);
        requestParams.addBodyParameter("zhengqian",zfUrlPath);
        requestParams.addBodyParameter("zhenghou",zhfUrlPath);
        requestParams.addBodyParameter("youhou45",img4UrlPath);
        requestParams.addBodyParameter("fadongji",img5UrlPath);
        Log.e("TAG","img66=="+img6UrlPath);
        requestParams.addBodyParameter("luntai",img6UrlPath);
        requestParams.addBodyParameter("toudeng",img7UrlPath);
        requestParams.addBodyParameter("weideng",img8UrlPath);
        requestParams.addBodyParameter("yibiaopan",img9UrlPath);
//
//        Log.e("TAG","上传车辆信息左前方==="+zhfUrlPath);
//        Log.e("TAG","上传车辆信息正前==="+zfUrlPath);
//        Log.e("TAG","上传车辆信息正后方==="+zhfUrlPath);
        requestParams.addBodyParameter("carStyleId",modelid);//modelid
        requestParams.addBodyParameter("carName",cartName);
        requestParams.addBodyParameter("pid",picID);
        //上传电话和名字
        //有对应id直接传ID

        requestParams.addBodyParameter("tel",tv_tel.getText().toString());
        requestParams.addBodyParameter("name",edt_name.getText().toString());
        //车辆分类
//        requestParams.addBodyParameter("isDaTing",fenleiID);
//        requestParams.addBodyParameter("transterstatus",guohuID);
        requestParams.addBodyParameter("makeup","2");
        requestParams.addBodyParameter("rfid_id",tv_carnum.getText().toString());//扫描结果rfid标签值        requestParams.setMaxRetryCount(2);
        requestParams.addBodyParameter("status",status);//车辆状态

        Log.e("TAG","上传地址=="+requestParams.getUri());
        Log.e("TAG","上传参数=="+requestParams.getBodyParams());
        Log.e("TAG","上传URL=="+requestParams);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG","修改成功=="+result);
                if(mydialog.isShowing()) {
                    mydialog.dismiss();
                }

//                pro.setProgress((k+1)*1);
//                if(k+1==selectList.size()){
//                    pro.dismiss();
//                }
                Log.e("TAG","");
//                mydialog.dismiss();
//                {"status":1,"msg":"添加成功","id":"2261"}
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    String status=jsonObject.getString("status");
                    final String msg;
                    if (status.equals("1")) {
                        msg = jsonObject.getString("msg");
//                        mySuccess=new MySuccess(BuMessageActivity.this,"修改成功");
//                        mySuccess.show();
                        cleanDate();
//                        mydialog.dismiss();
                        successdialog.show();
                        Log.e("TAG","imgCount=="+imgCount);
                            Log.e("TAG","取消dialog提示跳转");
                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    try {
                                        Thread.sleep(1000);
                                        if(successdialog.isShowing()){
                                            successdialog.dismiss();
                                        }
                                        finish();

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(!TextUtils.isEmpty(ex.getMessage().toString())){
//                    mydialog.dismiss();
//                    ZQFBean.zqpath="";ZQBean.zqpath="";ZHFBean.zhfpath="";
//                    zqfPath="";
//                    zhfPath="";
//                    zqPath="";
//                    cleanDate();
                    Log.e("TAG","上传信息识别=="+ex.getMessage().toString());
                    Toast.makeText(BuMessageActivity.this,"上传信息失败",Toast.LENGTH_LONG).show();
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
    private void cleanDate(){
        zqfUrlPath="";
        zfUrlPath="";
        zhfUrlPath="";
        img4UrlPath="";
        img5UrlPath="";
        img6UrlPath="";
        img7UrlPath="";
        img8UrlPath="";
        img9UrlPath="";
    }
    //设置返回键动作（防止按返回键直接退出程序)
    @Override
    public void onBackPressed() {
       if(window!=null&&window.isShowing()){
           window.dismiss();
       }
       if(window2!=null&&window2.isShowing()){
           window2.dismiss();
       }
       if(window3!=null&&window3.isShowing()){
           window3.dismiss();
       }
       finish();
    }
    private  void getRFID(final String strUrl){
        RequestParams params=new RequestParams(strUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG","result=="+result);
//                {"rfid":"d2a13ab3730b446aae8e6bc37cb014cb","carinfo":[]}
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    String carinfo=jsonObject.getString("carinfo");
                    JSONObject jsonObject1=new JSONObject(carinfo);
                    String carID=jsonObject1.getString("carid");
                    if(BeanFlag.Flag){//巡场进来
                        Log.e("TAG","!TextUtils.isEmpty(carID)"+carID);
                        if(TextUtils.isEmpty(carID)||carID.equals("null")){
                            Log.e("TAG","不进屋");
                            Toast.makeText(BuMessageActivity.this, "此标签未绑定车辆信息", Toast.LENGTH_SHORT).show();
                        }else{
                            //巡场扫描进入，解析并且展示
                            list=GetJsonUtils.CartListItem(BuMessageActivity.this,result);
                            Log.e("TAG","车源编号："+(TextUtils.isEmpty(list.get(0).rfid_id))+"=="+list.get(0).rfid_id.equals("null")+"=="+list.get(0).rfid_id);
                            if(TextUtils.isEmpty(list.get(0).rfid_id)||list.get(0).rfid_id.equals("null")){
                                tv_carnum.setText("请扫描二维码获取车源编号");
                            }else{
                                tv_carnum .setText(list.get(0).rfid_id+"");
                            }
                            Log.e("TAG","详情为==："+tv_carnum.getText());
                            tv_quyue.setText(list.get(0).quyuName);
                            quyuID=list.get(0).quyuID;
                            edit_num.setText(list.get(0).vin);
                            tv_time.setText(list.get(0).regTime);
                            picID=list.get(0).NameTelID;

                            edt_name.setText(list.get(0).contact_name);
                            tv_tel.setText(list.get(0).tel);
                            brandid=list.get(0).brandid;
                            seriesid=list.get(0).seriseID;
                            modelid=list.get(0).modelID;
                            brangName=list.get(0).brandName;
                            seriseName=list.get(0).seriseName;
                            cartName=list.get(0).modelName;
                            tv_cartmodel.setText(list.get(0).modelName);
                            fenleiID=list.get(0).isDaTing;
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
                            guohuID=list.get(0).transterstatus;
                            if(guohuID.equals("0")){
                                //已过户
                                Tv_guohu.setText("已过户");
                            }else{
                                Tv_guohu.setText("未过户");
                            }
                            status=list.get(0).status;
                            if(status.equals("0")){
                                tv_status.setText("下架");
                            }else{
                                tv_status.setText("正常");
                            }
                            status=list.get(0).status;
                            edt_licheng.setText(list.get(0).mileage);
                            edt_price.setText(list.get(0).price);
                            zqfPath=list.get(0).img1;
                            zqPath=list.get(0).img2;
                            zhfPath=list.get(0).img3;
                            zqfUrlPath=list.get(0).img1;
                            zfUrlPath=list.get(0).img2;
                            zhfUrlPath=list.get(0).img3;
                            img4UrlPath=list.get(0).img4;
                            img5UrlPath=list.get(0).img5;
                            img6UrlPath=list.get(0).img6;
                            img7UrlPath=list.get(0).img7;
                            img8UrlPath=list.get(0).img8;
                            img9UrlPath=list.get(0).img9;
                            if(list.get(0).img1.contains("http")) {
                                Glide.with(BuMessageActivity.this).load(list.get(0).img1+"?x-oss-process=style/233_162").placeholder(R.drawable.zq45d).error(R.drawable.zq45d).into(img_newfragment);
                            }
                            if(list.get(0).img2 .contains("http")) {
                                Glide.with(BuMessageActivity.this).load(list.get(0).img2+"?x-oss-process=style/233_162").placeholder(R.drawable.zqf).error(R.drawable.zqf).into(img2_newfragment);
                            }
                            if(list.get(0).img3 .contains("http")){
                                Glide.with(BuMessageActivity.this).load(list.get(0).img3+"?x-oss-process=style/233_162").placeholder(R.drawable.zhf).error(R.drawable.zhf).into(img3_newfragment);
                            }
                            if(list.get(0).img4.contains("http")) {
                                Log.e("TAG", "img4==" + list.get(0).img4);
                                Glide.with(BuMessageActivity.this).load(list.get(0).img4 + "?x-oss-process=style/233_162").placeholder(R.drawable.yh45d).error(R.drawable.yh45d).into(img4_newfragment);
                            }
                            if(list.get(0).img5.contains("http")){
                                Glide.with(BuMessageActivity.this).load(list.get(0).img5+"?x-oss-process=style/233_162").placeholder(R.drawable.fdjc).error(R.drawable.fdjc).into(img5_newfragment);
                            }
                            if(list.get(0).img6.contains("http")){
                                Glide.with(BuMessageActivity.this).load(list.get(0).img6+"?x-oss-process=style/233_162").placeholder(R.drawable.lt).error(R.drawable.lt).into(img6_newfragment);
                            }
                            if(list.get(0).img7.contains("http")){
                                Glide.with(BuMessageActivity.this).load(list.get(0).img7+"?x-oss-process=style/233_162").placeholder(R.drawable.td).error(R.drawable.td).into(img7_newfragment);
                            }
                            if(list.get(0).img8.contains("http")){
                                Glide.with(BuMessageActivity.this).load(list.get(0).img8+"?x-oss-process=style/233_162").placeholder(R.drawable.wd).error(R.drawable.wd).into(img8_newfragment);
                            }
                            if(list.get(0).img9.contains("http")) {
                                Glide.with(BuMessageActivity.this).load(list.get(0).img9 + "?x-oss-process=style/233_162").placeholder(R.drawable.zktzz).error(R.drawable.zktzz).into(img9_newfragment);
                            }
                        }
                    }else{//首页进入
                        if(TextUtils.isEmpty(carID)||carID.equals("null")){
                            String rfid=jsonObject.getString("rfid");
                            tv_carnum.setText(rfid);
                        }else{
                            Toast.makeText(BuMessageActivity.this, "此标签已绑定车辆信息", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
