package servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Bean.User;
import DB.DBdao;
import DB.DBdaoImpl;

@WebServlet(asyncSupported = true, urlPatterns = { "/getPost" })
public class getPost extends HttpServlet {
	private User user;//用户对象
	private DBdao dao;//数据库操作
	private String ACTION;//用户动作
	private String filename; 
	public getPost() {
		user =new User();
		dao = new DBdaoImpl();
		filename ="";
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		response.setCharacterEncoding("utf-8");//防止输出中文乱码
		PrintWriter pw = response.getWriter();
		ACTION = request.getParameter("ACTION");//动作
		Gson gson =new Gson();//创建gson//将字符串解析成javabean对象
		user = gson.fromJson(request.getParameter("user"), User.class);
		//登录
		if(ACTION==null) {
			ACTION ="";
		}
		if(ACTION.equals("LOGIN")) {
			try {
				if(login(user)) {
					//将用户完整信息返回
					String gsonString = gson.toJson(user);
					pw.write(gsonString);
				}
				else 
				{
					pw.write("loginfail");
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//注册
		else if(ACTION.equals("REGISTER")) {
			try {
				boolean exists = dao.findByacc(user.getAccount());
				System.out.println(exists);
				if (exists) {
					dao.add(user);//添加用户
					pw.write("successregister");
				}
				else {
					pw.write("exists");
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		//完善或修改
		else if(ACTION.equals("PERFECT")) {
			try {
				if (user != null) {
					String path=System.getProperty("catalina.home");
					filename=user.getAccount()+".jpg";
					user.setPath(path+"\\images"+"\\"+filename);
					dao.update(user);
				}
				pw.write("successperfect");
				System.out.println("successperfect");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			//创建文件
			if(!filename.equals("")) {
				InputStream in =request.getInputStream();
				BufferedInputStream bir = new BufferedInputStream(request.getInputStream());
				System.out.println(filename);
				String path=System.getProperty("catalina.home");
				File dir = new File(path,"images");
				if(!dir.exists()) {
					dir.mkdirs();
				}
				//存储目录
				byte[] bytes = new byte[4*1024];
				File file = new File(dir,filename);
				if(!file.exists()) {
					file.createNewFile( );
				}
				FileOutputStream out = new FileOutputStream(file);
				int len = 0;
				while((len=bir.read(bytes))!=-1) {
					out.write(bytes, 0, len);
					out.flush();
				}
				in.close();
				out.close();
				bir.close();
			}
		}
	}
	@Override
	public void destroy() {
		//销毁资源
		try {
			dao.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	 //是否成功登录
    public  boolean login(User u) throws SQLException
    {
    	
    	user = dao.findByaccAndpass(user);//新的user对象
        if (user.getAccount()==u.getAccount()&&user.getPassword().equals(u.getPassword()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
