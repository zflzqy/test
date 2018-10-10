package zfl.festival;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zfl.com.myapplication.R;
import zfl.festival.Bean.festival;
import zfl.festival.Database.SmsDAO;
import zfl.festival.Database.SmsDAOImpl;
import zfl.festival.festival_fragment.festival_blessing;


public class ChooseMessageActivity extends AppCompatActivity {
    private ListView mListview;
    private FloatingActionButton mFloatBtn;

    private ArrayAdapter<festival> mAdapter;
    private LayoutInflater mInflater;//添加布局文件
    private ImageView imageView;//listview最后的加号

    private List<festival> mFestivals;
    private SmsDAO mSmsDAO;//数据库操作
    private String mFestivalName;//节日名
    private int[] msgCount = new int[50];//存储数据库保存的短信id
    private int[] msgCountIndex = new  int[50];//保存msgCount下标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_choose_message);
        setTitle(mFestivalName+"祝福");

        mFestivalName= getIntent().getStringExtra(festival_blessing.FESTIVAL_NAME);//获得传来的节日名称
        imageView = (ImageView) getLayoutInflater().inflate(R.layout.itme_chosse_tag,null);
        mInflater = LayoutInflater.from(this);//初始化布局
        mSmsDAO = new SmsDAOImpl(this);//数据库操作实现

        initView();
        initEvent();
        mListview.addFooterView(imageView);//添加最后的加号布局
        mListview.setAdapter(mAdapter);//设置适配器
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("mFestivalName",mFestivalName);//销毁时保存节日名称
    }

    private void initEvent() {
        //点击发送按钮监听事件
        mFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO，跳转到sendmessage活动
                SendMessageActivity.toActivity(ChooseMessageActivity.this,mFestivalName,0);
            }
        });
        //添加数据
        imageView.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                int n = 0;
                if (msgCount!=null){
                    for (int i = 0; i < msgCount.length; i++) {
                        if (0!=msgCount[i])
                            n++;
                    }
                }
                //判断短信条数是否最大
                if (n==50){
                    Toast.makeText(getApplication(),"短信条数已达最大",Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplication(),"最多添加50条短信",Toast.LENGTH_SHORT).show();
                final EditText ed = (EditText) mInflater.inflate(R.layout.item_addmsg,null);
                //这里的context要这样写才不报错
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChooseMessageActivity.this);
                dialog.setTitle("添加祝福短信");
                dialog.setIcon(R.drawable.adddialog);
                dialog.setMessage("添加一条祝福语吧");
                dialog.setView(ed);
                dialog.setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    festival fest = new festival();
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (TextUtils.isEmpty(ed.getText().toString())){
                            Toast.makeText(getApplicationContext(),"短信内容不能为空",Toast.LENGTH_SHORT).show();
                        }
                        for (int i = 1; i <=50; i++)
                        {
                                //判断当前i值是否在数组内
                                if (i!=msgCount[i-1]){
                                    fest.setMsgId(i);
                                    fest.setMsg(ed.getText().toString());
                                    fest.setFestName(mFestivalName);
                                    fest.setDate(new Date());
                                    //数据库操作放入子线程
                                    DAO dao =new DAO(fest);
                                    dao.start();
                                    Toast.makeText(getApplicationContext(),"添加成功",Toast.LENGTH_SHORT).show();
                                    break;//中断循环，只添加一次
                                }
                        }//for循环
                        refreshadd(fest);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
               dialog.show();
            }
        });
    }

    private void initView() {
        mListview = findViewById(R.id.choosemessage_lv);
        mFloatBtn = findViewById(R.id.choosemessage_flotbtn);

        mFestivals = new ArrayList<>();
        mFestivals = mSmsDAO.query(mFestivalName);//初始操作，放入子线程返回不到值
        changegdata();
        //适配器设置
        mAdapter = new ArrayAdapter<festival>(this,-1,mFestivals){
            @NonNull
            @Override
            public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
              viewHolder vh;
                if (convertView == null) {
                    vh = new viewHolder();
                    convertView = mInflater.inflate(R.layout.item_choosemsg, null);
                    vh.content = convertView.findViewById(R.id.choosemessage__tv);
                    vh.btn = convertView.findViewById(R.id.choosemessage__btn);
                    vh.delete =convertView.findViewById(R.id.choosemessage__delete);
                    convertView.setTag(vh);
                }else {
                    vh = (viewHolder) convertView.getTag();
                }
                vh.content.setText("    "+getItem(position).getMsg());
                //点击发送事件
                vh.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SendMessageActivity.toActivity(ChooseMessageActivity.this,mFestivalName,getItem(position).getMsgId());
                    }
                });
                //删除短信
              vh.delete.setOnClickListener(new View.OnClickListener()
              {
                  @Override
                  public void onClick(View v) {
                      mAdapter.remove(getItem(position));
                      mAdapter.notifyDataSetChanged();
                      DAO dao =new DAO(mFestivalName, position);
                      dao.start();
//                      mSmsDAO.delete(mFestivalName,msgCount[msgCountIndex[position]]);//删除数据库内容
                  }
              });
                return convertView;
            }
            class viewHolder{
                TextView content;
                Button btn,delete;
            }
        };

    }
    //初始和更新数据操作
    private void changegdata() {
        List<festival> fets =new ArrayList<>();
        fets = mSmsDAO.query(mFestivalName);
        int i =0;//放在外边，防止多次设置为0
        for (festival festival : fets)
        {
            msgCount[festival.getMsgId()-1] =festival.getMsgId();//将数据库中的短信id存放到数组
            boolean write =true;
            while (write) {
                msgCountIndex[i] = festival.getMsgId() - 1;
                i++;
                write = false;//只读入一次
            }//while循环
        }//for循环
    }
    //增加的时候刷新ui界面显示
    private void refreshadd(festival festival) {
        mAdapter.add(festival);
    }
    //线程
    class  DAO extends Thread{
        private festival Festival;
        private String FestName;

        private int position;

        private int Startcode;//启动数据库操作码

        public DAO(festival Festival) {
            this.Festival = Festival;
            Startcode = 1;
        }

        public DAO(String FestName, int position) {
            this.FestName = FestName;
            this.position = position;
            Startcode =2;
        }
        @Override
        public void run() {
            switch (Startcode){
                case 1:
                    //插入操作
                    mSmsDAO.insert(Festival);
                    changegdata();
                    break;
                case 2:
                    //删除操作
                    mSmsDAO.delete(FestName,msgCount[msgCountIndex[position]]);
                    break;
            }
        }
    }
}
