package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Bean.User;

//数据库操作实现类
public class DBdaoImpl  implements DBdao{
	private Connection conn =null;
	private PreparedStatement pstm =null;
	private ResultSet rs =null;
	
	public DBdaoImpl() {
		conn =DBUtil.getConn();
	}
	//添加账号
	@Override
	public void add(User user) throws SQLException {
		String sql = " INSERT INTO user (account,password ) VALUES (?,?)";	
		pstm = conn.prepareStatement(sql);
		pstm.setInt(1, user.getAccount());
		pstm.setString(2,user.getPassword());
		pstm.execute();//执行sql语句
		
	}
	//更新账号
	@Override
	public void update(User user) throws SQLException {
		String sql = " UPDATE  user  "+
				" SET name=?,age=?,sex=?,school=?,path=? "+				
				" WHERE account =?";	
	pstm = conn.prepareStatement(sql);
	pstm.setString(1,user.getName());
	pstm.setInt(2,user.getAge());
	pstm.setString(3, user.getSex());
	pstm.setString(4, user.getSchool());
	pstm.setString(5,user.getPath());
	pstm.setInt(6,user.getAccount());
	pstm.execute();//执行sql语句
	}
	//查找账号是否存在
	@Override
	public boolean findByacc(int account) throws SQLException {
		pstm =null;
		String sql ="SELECT * FROM user WHERE account=? ";
		pstm =conn.prepareStatement(sql);//预加载
		pstm.setInt(1,account);
		ResultSet rs = pstm.executeQuery();
		while(rs.next()) 
		{
			return false;//存在返回false
		}
		return true;
	}
	//查找账号信息
	@Override
	public User findByaccAndpass(User user) throws SQLException  {
		pstm =null;
		String sql ="SELECT * FROM user WHERE account=? and password =?";
		pstm =conn.prepareStatement(sql);//预加载
		pstm.setInt(1,user.getAccount());
		pstm.setString(2, user.getPassword());
		ResultSet rs = pstm.executeQuery();
		User u =new User();
		while(rs.next()) 
		{
			u.setAccount(rs.getInt("account"));
			u.setPassword(rs.getString("password"));
			u.setAge(rs.getInt("age"));
			u.setName(rs.getString("name"));
			u.setOtheraccount(rs.getString("otheraccount"));
			u.setPath(rs.getString("path"));
			u.setSex(rs.getString("sex"));
			u.setSchool(rs.getString("school"));
		}
		return u;
	}
	@Override
	public void close() throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
