
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class BoardDAOImpl implements BoardDAO {

	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	int userno;

	public static final String DRIVER_NAME = "oracle.jdbc.OracleDriver";
	public static final String URL = "jdbc:oracle:thin:@//localhost:1521/xepdb1";
	public static final String USERID = "scott";
	public static final String USERPWD = "tiger";

	public BoardDAOImpl() {
		try {
			Class.forName(DRIVER_NAME);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("drive connect failed");
		}
	}


	public boolean user_check(String id, int writer) {
		int userno = 0;
		try {
			conn = DriverManager.getConnection(URL, USERID, USERPWD);
			String sql = "select yvuserno from yv_user where yvid = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				userno = rs.getInt("yvuserno");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (userno == writer) {
			return true;
		} else {
			return false;
		}
	}

	public String get_userid(int writer) {
		String userid = "";
		try {
			conn = DriverManager.getConnection(URL, USERID, USERPWD);
			String sql = "select yvid from yv_user where yvuserno = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, writer);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				userid = rs.getString("yvid");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userid;
	}

	public int get_userno(String userid) {
		List<Register> list = new ArrayList<Register>();
		int userno = 0;
		try {
			conn = DriverManager.getConnection(URL, USERID, USERPWD);
			String sql = "select yvuserno from yv_user where yvid = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				userno = rs.getInt("yvuserno");
				Register r = new Register();
				list.add(r);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (list.size() == 0) {
			return -1;
		}
		return userno;
	}

	public boolean password_check(String pw, int writer) {
		try {
			conn = DriverManager.getConnection(URL, USERID, USERPWD);
			String sql = "select yvuserno from yv_user where yvpw = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pw);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				userno = rs.getInt("yvuserno");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (userno == writer) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int insert(BoardVO vo) {
		try {
			conn = DriverManager.getConnection(URL, USERID, USERPWD);
			String sql = "insert into post (postno, vname, ptitle, pcontent, pwriter) values (SEQ_BOARD_NUM.NEXTVAL, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getVname());
			pstmt.setString(2, vo.getTitle());
			pstmt.setString(3, vo.getContent());
			pstmt.setInt(4, vo.getWriter());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}

//	@Override
//	public int update(BoardVO vo) {
//		try {
//			conn = DriverManager.getConnection(URL, USERID, USERPWD);
//			String sql = "update post set vname=?, ptitle=?, pcontent=?, pwriter=? where postno=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, vo.getVname());
//			pstmt.setString(2, vo.getTitle());
//			pstmt.setString(3, vo.getContent());
//			pstmt.setInt(4, vo.getWriter());
//			pstmt.setInt(5, vo.getNum());
//
//			pstmt.executeUpdate();
//
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			try {
//				conn.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return 0;
//	}

	@Override
	public int delete(BoardVO vo) {
		try {
			conn = DriverManager.getConnection(URL, USERID, USERPWD);
			String sql = "delete from post where postno=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, vo.getNum());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}

	@Override
	public BoardVO search(BoardVO vo) {

		return null;
	}

	@Override
	public List<BoardVO> select() {

		List<BoardVO> list = new ArrayList<BoardVO>();

		try {
			conn = DriverManager.getConnection(URL, USERID, USERPWD);
			String sql = "select postno, vname, ptitle, pcontent, pwriter from post order by postno desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				BoardVO vo = new BoardVO();
				vo.setNum(rs.getInt("postno"));
				vo.setVname(rs.getString("vname"));
				vo.setTitle(rs.getString("ptitle"));
				vo.setContent(rs.getString("pcontent"));
				vo.setWriter(rs.getInt("pwriter"));

				list.add(vo);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return list;
	}

	@Override
	public void search(String search, String searchString) {
		List<BoardVO> list = new ArrayList<BoardVO>();

		try {
			conn = DriverManager.getConnection(URL, USERID, USERPWD);
			String sql = "select postno, vname, ptitle, pcontent, pwriter from post where " + search + " like '%"
					+ searchString + "%' order by postno desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				BoardVO vo = new BoardVO();
				vo.setNum(rs.getInt("postno"));
				vo.setVname(rs.getString("vname"));
				vo.setTitle(rs.getString("ptitle"));
				vo.setContent(rs.getString("pcontent"));
				vo.setWriter(rs.getInt("pwriter"));

				list.add(vo);

			}

			if (list.size() == 0) {
				JOptionPane.showMessageDialog(null, "해당하는 게시글이 없습니다.");
				new BoardList();
			} else {
				new Post_Search(list);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
