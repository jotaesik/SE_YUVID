//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class Appoint {
//
//	public static final String DRIVER_NAME = "oracle.jdbc.OracleDriver";
//	public static final String URL = "jdbc:oracle:thin:@//localhost:1521/xepdb1";
//	public static final String USERID = "scott";
//	public static final String USERPWD = "tiger";
//
//	public static void main(String[] args) {
//		Connection conn;
//		PreparedStatement pstmt;
//		ResultSet rs;
//
//		List<BoardVO> list = new ArrayList<BoardVO>();
//		int pno = 0;
//		try {
//			conn = DriverManager.getConnection(URL, USERID, USERPWD);
//			String sql = "select postno, vname, ptitle, pcontent, pwriter from post order by postno desc";
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				BoardVO vo = new BoardVO();
//				vo.setNum(rs.getInt("postno"));
//				vo.setVname(rs.getString("vname"));
//				vo.setTitle(rs.getString("ptitle"));
//				vo.setContent(rs.getString("pcontent"));
//				vo.setWriter(rs.getInt("pwriter"));
//
//				list.add(vo);
//
//			}
//			pno = list.size() + 1;
//			System.out.println(pno);
//
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//
//		}
//	}
//}