package DB;

import java.sql.SQLException;

import Bean.User;
//数据库操作接口
public interface DBdao {
	//添加（账号）
	public void add(User user) throws SQLException;
	//更新（账号）
	public void update(User user) throws SQLException;
	//查找账号是否存在
	public boolean findByacc(int account) throws SQLException;
	//查找账号信息
	public User findByaccAndpass(User user) throws SQLException;
	//释放资源
	public void close() throws SQLException;
}
