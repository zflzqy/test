package zfl.com.festival;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import zfl.com.festival.Bean.festival;
import zfl.com.festival.Bean.sendMessage;
import zfl.com.festival.Database.SmsDAO;
import zfl.com.festival.Database.SmsDAOImpl;
import zfl.com.festival.biz.Smsbiz;
import zfl.com.festival.view.FlowLayout;


public class SendMessageActivity extends AppCompatActivity {
    public static final String FEST_NAME = "FEST_NAME";//节日名称
    public static final String MSG_ID = "MSG_ID";//节日id
    private static final int CODE_RESULT =1 ;//返回结果码

    private EditText et;
    private View loading;//发送等待视图
    private Button addbtn;//添加联系人
    private Smsbiz mSmsbiz;//发送业务
    private FlowLayout floalayout;//联系人布局
    private LayoutInflater mInflater;//找布局
    private FloatingActionButton float_btn;//发送按钮

    private HashSet<String> mContactNames = new HashSet<>();//联系人名集合
    private HashSet<String> mContactNunbers = new HashSet<>();//联系人电话集合

    public static final String ACTION_SEND_MSH = "ACTION_SEND_MSH";//广播过滤
    public static final String ACTION_DVLIER_MSH = "ACTION_DVLIER_MSH";//广播过滤

    private PendingIntent mSentPi;
    private PendingIntent mDvlierPi;

    private BroadcastReceiver mSendBrodcastReceiver;//发送广播接收者
    private BroadcastReceiver mDvlierBrodcastReceiver;//切割短信

    private int mTotalMsg;//总发送的
    private int mSendMsg;//已经发送的

    private festival mFestival;//节日短信
    private String mFestNmae;//获取传过来的节日名称
    private int msgId;//获取传过来的短信条目id
    private SmsDAO mSmsDAO;//数据库操作

    private List<String> mPhone;//要添加的联系人，防止重复
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_send_message);
        initDatas();//初始化数据
        initView();//初始化视图
        initEvent();//事件
        initReceivers();//初始化广播
    }
    private void initReceivers()
    {
        Intent sendIntent = new Intent(ACTION_SEND_MSH);
        mSentPi = PendingIntent.getBroadcast(this,0,sendIntent,0);
        Intent dvlierIntent = new Intent(ACTION_DVLIER_MSH);
        mDvlierPi = PendingIntent.getBroadcast(this,0,dvlierIntent,0);
        //注册广播发送广播
        mSendBrodcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mSendMsg++;
                if (getResultCode() == RESULT_OK){
//                    Log.i("zfl",mSendMsg+"/"+mTotalMsg+ "短信发送成功");
                }else {
//                    Log.i("zfl", "短信发送失败");
                }
                //显示发送的条数
                Toast.makeText(SendMessageActivity.this,(mSendMsg+"/"+mTotalMsg)+ "短信发送成功",Toast.LENGTH_SHORT).show();
                //发送完成销毁当前activity
                if (mSendMsg == mTotalMsg){
                    finish();
                }
            }
        };
        registerReceiver(mSendBrodcastReceiver,new IntentFilter(ACTION_SEND_MSH));
        //注册接收成功广播
        mDvlierBrodcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                    Log.i("zfl", "短信接收成功");
            }
        };
        registerReceiver(mDvlierBrodcastReceiver,new IntentFilter(ACTION_DVLIER_MSH));
    }

    private void initEvent() {
    //添加联系人
    addbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);//获取通讯录
            startActivityForResult(intent,CODE_RESULT);
        }
    });
    float_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //todo
            String content = et.getText().toString();
            if (mContactNunbers.size()==0){
                Toast.makeText(SendMessageActivity.this,"请先选择联系人",Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(content)){
                Toast.makeText(SendMessageActivity.this,"短信内容不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            loading.setVisibility(View.VISIBLE);
            mTotalMsg = mSmsbiz.sendMesssage(mContactNunbers,buildContent(content),mSentPi,mDvlierPi);
            mSendMsg = 0;
        }
    });
    }

    private sendMessage buildContent(String content) {
        sendMessage sm = new sendMessage();
        String id = java.util.UUID.randomUUID().toString();//标识短信唯一
        sm.setId(id);
        sm.setMsg(content);
        sm.setFestName(mFestival.getFestName());
        StringBuilder names = new StringBuilder();//用stringBuilder不会在赋值时有null字符
        StringBuilder numbers= new StringBuilder();
        for (String name :mContactNames){
            names.append(name+",");
        }
        for (String number:mContactNunbers){
            numbers.append(number+",");
        }
        sm.setNames(names.substring(0,names.length()-1));
        sm.setNumbers(numbers.substring(0,numbers.length()-1));
        return sm;
    }

    private void initView() {
        et = findViewById(R.id.sendmessage_et);
        addbtn = findViewById(R.id.sendmessage_btn);
        floalayout = findViewById(R.id.sendmessage_contacts);
        float_btn = findViewById(R.id.sendmessage_flaotbtn);
        loading = findViewById(R.id.sendmessage_loading);
        loading.setVisibility(View.GONE);
        if (msgId>0){
            et.setText(mFestival.getMsg());//设置显示的短信内容
        }
    }
    //初始化数据
    private void initDatas() {
        mInflater =LayoutInflater.from(this);
        mSmsbiz = new Smsbiz(this);
        mSmsDAO = new SmsDAOImpl(this);
        mFestival =new festival();
        mPhone =new ArrayList<>();

        mFestNmae = getIntent().getStringExtra(FEST_NAME);
        msgId = getIntent().getIntExtra(MSG_ID,0);


        mFestival = mSmsDAO.queryOne(mFestNmae,msgId);
        setTitle(mFestival.getFestName());//设置上方显示节日名称

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSendBrodcastReceiver);
        unregisterReceiver(mDvlierBrodcastReceiver);
    }
    //获取startActivityRuslt返回的值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==CODE_RESULT){
            if (resultCode == RESULT_OK){
                Uri contatcUri = data.getData();
                Cursor cursor = getContentResolver().query(contatcUri,null,null,null,null);
                cursor.moveToFirst();
                String ContactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));//得到联系人名
                String number= getContactNumber(cursor);
                if (!TextUtils.isEmpty(number))
                {
                            mContactNunbers.add(number);
                            mContactNames.add(ContactName);
                             addTag(ContactName,number);
                }
            }
        }
    }
    //得到电话号码
    private String getContactNumber(Cursor cursor) {
        int numBercont = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
        String number =null;
        if (numBercont>0){
            int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null
            ,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+id,null,null);
            phoneCursor.moveToFirst();
             number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneCursor.close();
        }
        cursor.close();
        return number;
    }
    //联系人显示
    private void addTag(String contactName,String number) {
        final View view = mInflater.inflate(R.layout.contacts,floalayout,false);
        TextView tv = view.findViewById(R.id.contacts);
        ImageView clear = view.findViewById(R.id.sendmessage_clear);
        tv.setText(contactName);
        //防止重复添加
        if (!mPhone.contains(number)){
            floalayout.addView(view);
            mPhone.add(number);
        }else {
            Toast.makeText(getApplicationContext(),"同一联系人只能添加一次",Toast.LENGTH_SHORT).show();
        }
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floalayout.removeView(view);
            }
        });
    }
    //跳转到该活动
    public static void  toActivity(Context context,String festName,int msgId){
        Intent intent = new Intent(context,SendMessageActivity.class);
        intent.putExtra(FEST_NAME,festName);
        intent.putExtra(MSG_ID,msgId);
        context.startActivity(intent);
    }
}
