import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.MenuKeyEvent;

/*
Login_Panel : 로그인화면
SignUp_Panel : 회원가입
Find_ID_Panel : 아이디찾기
panel4 : 비밀번호찾기
panel5 : 본인확인 (비밀번호 재확인)
panel6 : 메인화면
panel7 : 회원정보수정 및 아이디 변경후 비밀번호변경
panel8 : 접종예약
panel9 : 예약확인 및 취소
panel10 : 게시판
panel11 : 
panel12 : 병원리스트
panel13 : 백신리스트
panel14 : 
panel15 : 
panel16 : 
panel17 :
panel18 :
*/

class cn { // connect
	static int userno = 1;
	static boolean ismanage = false;
	static boolean condition = false; // 예약취소시 true, 회원정보 수정시 false, 비밀번호 분실시 true
	static Connection conn = null;
	static Statement stmt = null;
	static PreparedStatement pstmt = null;
	static ResultSet rset = null;
}

@SuppressWarnings("serial")
class Login_Panel extends JPanel { // 1번째 패널 (로그인)
	private Test win;
	private static JTextField id_tf = new JTextField(30);
	private static JPasswordField pw_pf = new JPasswordField();
	private static final JLabel label1 = new JLabel("백신 예약 프로그램");
	private static final JLabel label2 = new JLabel("YUVID");
	private static final JLabel label3 = new JLabel("ID");
	private static final JLabel label4 = new JLabel("PW");
	private static final JButton reg_btn = new JButton("회원가입");
	private static final JButton find_id_btn = new JButton("ID찾기");
	private static final JButton find_pw_btn = new JButton("PW찾기");
	private static final JButton login_btn = new JButton("로그인");
	private static final JButton commu_btn = new JButton("게시판");

	public Login_Panel(Test win) { // UI구성
		this.win = win;
		setLayout(null);

		label1.setFont(new Font("Serif", Font.BOLD, 23));
		label1.setBounds(80, 40, 400, 50);
		add(label1);

		label2.setFont(new Font("Serif", Font.BOLD, 23));
		label2.setBounds(145, 70, 120, 65);
		add(label2);

		label3.setBounds(50, 170, 67, 15);
		add(label3);

		id_tf.setBounds(85, 167, 146, 21);
		add(id_tf);
		id_tf.setColumns(10);

		label4.setBounds(45, 204, 67, 15);
		add(label4);

		pw_pf.setBounds(85, 201, 146, 21);
		add(pw_pf);

		login_btn.setSize(86, 31);
		login_btn.setLocation(245, 178);
		add(login_btn);

		reg_btn.setSize(100, 23);
		reg_btn.setLocation(20, 270);
		add(reg_btn);

		find_id_btn.setSize(100, 23);
		find_id_btn.setLocation(132, 270);
		add(find_id_btn);

		find_pw_btn.setSize(100, 23);
		find_pw_btn.setLocation(245, 270);
		add(find_pw_btn);

		commu_btn.setSize(162, 31);
		commu_btn.setLocation(98, 338);
		add(commu_btn);

		reg_btn.addActionListener(new Reg_Action());
		find_id_btn.addActionListener(new Find_ID_Action());
		find_pw_btn.addActionListener(new Find_PW_Action());
		login_btn.addActionListener(new Login_Action());
		pw_pf.addActionListener(new Login_Action());
		id_tf.setFocusTraversalKeysEnabled(false);
		id_tf.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == MenuKeyEvent.VK_TAB)
					pw_pf.requestFocus();
			}
		});

	}

	private class Reg_Action implements ActionListener { // 회원가입버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel02");
			clear_field();
		}
	}

	private class Find_ID_Action implements ActionListener { // 아이디찾기 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel03");
			clear_field();
		}
	}

	private class Find_PW_Action implements ActionListener { // 비밀번호찾기 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel04");
			clear_field();
		}
	}

	private class Login_Action implements ActionListener { // 로그인버튼
		public void actionPerformed(ActionEvent e) {
			String id = id_tf.getText();
			String pw = String.copyValueOf(pw_pf.getPassword());
			if (check_id_input(id)) {
				JOptionPane.showMessageDialog(null, "아이디를 입력해주세요.", "", 0);
				return;
			}
			if (check_pw_input(pw)) {
				JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요.", "", 0);
				return;
			}
			if (check_login(id, pw)) {
				if (cn.ismanage) {
					JOptionPane.showMessageDialog(null, "관리자 권한으로 로그인 되었습니다.", "", 1);
					win.change("panel06");
				} else {
					JOptionPane.showMessageDialog(null, "로그인 되었습니다.", "", 1);
					win.change("panel06");
				}
				clear_field();
			}
			return;
		}
	}

	private static boolean check_id_input(String str) { // ID 공백확인
		if (str.equals(""))
			return true;
		return false;
	}

	private static boolean check_pw_input(String str) { // PW 공백확인
		if (str.equals(""))
			return true;
		return false;
	}

	private static boolean check_login(String id, String pw) { // 아이디 존재 여부 및 관리자권한 확인
		final String sql = "select yvpw, yvtype, yvuserno from yv_user where yvid=?";
		try {
			cn.pstmt = cn.conn.prepareStatement(sql);
			cn.pstmt.setString(1, id);
			cn.rset = cn.pstmt.executeQuery();
			if (cn.rset.next()) {
				if (cn.rset.getString("yvpw").equals(pw)) {
					if (cn.rset.getInt("yvtype") == 1)
						cn.ismanage = true;
					else
						cn.ismanage = false;
					cn.userno = cn.rset.getInt("yvuserno");
					return true;
				} else {
					JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.", "", 2);
				}
			} else {
				JOptionPane.showMessageDialog(null, "존재하지 않는 아이디 입니다", "", 0);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return false;
	}

	private static void clear_field() { // 텍스트박스 초기화
		id_tf.setText("");
		pw_pf.setText("");
	}
}

@SuppressWarnings("serial")
class SignUp_Panel extends JPanel { // 2번째 패널(회원가입)
	private static String checked_id = null;
	private Test win;
	private static final JLabel label1 = new JLabel("ID");
	private static final JLabel label2 = new JLabel("PW");
	private static final JLabel label3 = new JLabel("PW확인");
	private static final JLabel label4 = new JLabel("이름");
	private static final JLabel label6 = new JLabel("전화번호");
	private static JTextField id_tf = new JTextField();
	private static JTextField name_tf = new JTextField();
	private static JTextField phone_tf = new JTextField();
	private static JPasswordField pw_pf = new JPasswordField();
	private static JPasswordField pw_confirm_pf = new JPasswordField();
	private static final JButton id_check_btn = new JButton("중복확인");
	private static final JButton ok_btn = new JButton("가입");
	private static final JButton back_btn = new JButton("취소");

	public SignUp_Panel(Test win) { // UI구성
		setLayout(null);
		this.win = win;

		label1.setBounds(78, 100, 67, 15);
		add(label1);

		id_tf.setBounds(103, 97, 140, 21);
		add(id_tf);
		id_tf.setColumns(10);

		label2.setBounds(70, 140, 67, 15);
		add(label2);

		pw_pf.setBounds(103, 137, 140, 21);
		add(pw_pf);

		label3.setBounds(47, 180, 67, 15);
		add(label3);

		pw_confirm_pf.setBounds(103, 177, 140, 21);
		add(pw_confirm_pf);

		id_check_btn.setSize(85, 19);
		id_check_btn.setLocation(260, 97);
		add(id_check_btn);

		label4.setBounds(68, 220, 67, 15);
		add(label4);

		name_tf.setBounds(103, 217, 140, 21);
		add(name_tf);
		name_tf.setColumns(10);

		label6.setBounds(40, 300, 117, 15);
		add(label6);

		phone_tf.setBounds(103, 297, 140, 21);
		add(phone_tf);
		phone_tf.setColumns(10);

		ok_btn.setSize(120, 25);
		ok_btn.setLocation(54, 360);
		add(ok_btn);

		back_btn.setSize(120, 25);
		back_btn.setLocation(190, 360);
		add(back_btn);

		ok_btn.addActionListener(new Reg_Action());
		back_btn.addActionListener(new Back_Action());
		id_check_btn.addActionListener(new Check_ID_Action());
	}

	private class Check_ID_Action implements ActionListener { // 중복 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			final String sql = "select 1 from sys.dual where exists (select * from yv_user where yvid=?)";
			String register_id = id_tf.getText();

			for (int i = 0; i < register_id.length(); i++) {
				if (register_id.charAt(i) < 0x30 || (register_id.charAt(i) > 0x39 && register_id.charAt(i) < 0x41)
						|| (register_id.charAt(i) > 0x5A && register_id.charAt(i) < 0x61)
						|| register_id.charAt(i) > 0x7A) {
					JOptionPane.showMessageDialog(null, "아이디는 영문, 숫자만 사용 가능합니다.", "", 0);
					return;
				}
			}
			if (id_tf.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "아이디를 입력해주세요.", "", 0);
			} else if (register_id.length() > 30) {
				JOptionPane.showMessageDialog(null, "아이디가 너무 깁니다.", "", 0);
			} else if (register_id.length() < 5) {
				JOptionPane.showMessageDialog(null, "아이디가 너무 짧습니다.", "", 0);
			} else {
				try {
					cn.pstmt = cn.conn.prepareStatement(sql);
					cn.pstmt.setString(1, register_id);
					cn.rset = cn.pstmt.executeQuery();
					if (cn.rset.next()) {
						JOptionPane.showMessageDialog(null, "존재하는 아이디 입니다", "", 2);
					} else {
						JOptionPane.showMessageDialog(null, "생성 가능한 아이디 입니다.", "", 1);
						checked_id = register_id;
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			return;
		}
	}

	private class Reg_Action implements ActionListener { // 가입 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			String pw = String.copyValueOf(pw_pf.getPassword());
			String pw_confirm = String.copyValueOf(pw_confirm_pf.getPassword());
			String name = name_tf.getText();
			String phone = phone_tf.getText();
			if (pw.equals("") || pw_confirm.equals("") || name.equals("") || phone.equals("")) {
				JOptionPane.showMessageDialog(null, "모든 항목을 채워주세요.", "", 0);
				return;
			}

			if (checked_id.equals(id_tf.getText())) {
				if (pw.equals(pw_confirm)) {
					for (int i = 0; i < pw.length(); i++) {
						if (pw.charAt(i) < 0x21 || pw.charAt(i) > 0x7E) {
							JOptionPane.showMessageDialog(null, "비밀번호는 영문, 숫자 및 일부 특수문자만 사용 가능합니다.", "", 0);
							return;
						}
					}
					if (pw.length() < 4) {
						JOptionPane.showMessageDialog(null, "비밀번호가 너무 짧습니다.", "", 0);
						return;
					} else if (pw.length() > 20) {
						JOptionPane.showMessageDialog(null, "비밀번호가 너무 깁니다.", "", 0);
						return;
					} else {
						JOptionPane.showMessageDialog(null, "회원가입이 완료 되었습니다.", "", 1);
						insert_id(checked_id, pw, name, phone);
						clear_field();
						win.change("panel01");
					}
				} else {
					JOptionPane.showMessageDialog(null, "비밀번호 재입력이 올바르게 되지 않았습니다.", "", 2);
				}
			} else {
				JOptionPane.showMessageDialog(null, "아이디 중복확인을 해주세요", "", 2);
			}
			return;
		}
	}

	private class Back_Action implements ActionListener { // 뒤로 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel01");
			clear_field();
		}

	}

	private static void clear_field() { // 텍스트박스 초기화
		id_tf.setText("");
		pw_pf.setText("");
		pw_confirm_pf.setText("");
		name_tf.setText("");
		phone_tf.setText("");
	}

	private static void insert_id(String iid, String ipw, String iname, String iphone) {
		final String sql = "insert into YV_USER values(?, ?, ?, ?, ?, 0)";
		final String sql2 = "SELECT MAX(yvuserno) from YV_USER";
		try {
			cn.stmt = cn.conn.createStatement();
			cn.rset = cn.stmt.executeQuery(sql2);
			cn.rset.next();
			int no_tmp = cn.rset.getInt("MAX(yvuserno)") + 1;
			cn.pstmt = cn.conn.prepareStatement(sql);
			cn.pstmt.setInt(1, no_tmp);
			cn.pstmt.setString(2, iid);
			cn.pstmt.setString(3, ipw);
			cn.pstmt.setString(4, iname);
			cn.pstmt.setString(5, iphone);
			cn.pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

@SuppressWarnings("serial")
class Find_ID_Panel extends JPanel { // 3번째 패널(ID찾기)
	private static JTextField name_tf = new JTextField();
	private static JTextField phone_tf = new JTextField();
	private Test win;
	private static final JLabel label1 = new JLabel("이름:");
	private static final JLabel label2 = new JLabel("전화번호:");
	private static final JButton find_btn = new JButton("찾기");
	private static final JButton back_btn = new JButton("취소");

	public Find_ID_Panel(Test win) { // UI구성
		setLayout(null);
		this.win = win;
		label1.setBounds(84, 60, 77, 15);
		add(label1);

		name_tf.setBounds(123, 57, 160, 21);
		add(name_tf);
		name_tf.setColumns(10);

		label2.setBounds(60, 94, 107, 15);
		add(label2);

		phone_tf.setBounds(123, 91, 160, 21);
		add(phone_tf);

		find_btn.setSize(120, 25);
		find_btn.setLocation(50, 145);
		add(find_btn);

		back_btn.setSize(120, 25);
		back_btn.setLocation(190, 145);
		add(back_btn);

		back_btn.addActionListener(new Back_Action());
		find_btn.addActionListener(new Find_Action());

	}

	private class Back_Action implements ActionListener { // 뒤로 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel01");
			clear_field();
		}
	}

	private class Find_Action implements ActionListener { // 찾기 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			final String sql = "select yvid from yv_user where yvphone_number=? and yvname=?";
			String name = name_tf.getText();
			String phone_number = phone_tf.getText();
			if (name.equals("") || phone_number.equals("")) {
				JOptionPane.showMessageDialog(null, "이름과 전화번호를 모두 입력해주세요.", "", 0);
				return;
			}
			try {
				cn.pstmt = cn.conn.prepareStatement(sql);
				cn.pstmt.setString(1, phone_number);
				cn.pstmt.setString(2, name);
				cn.rset = cn.pstmt.executeQuery();
				if (cn.rset.next()) { // 기입한 정보와 일치하는 아이디가 있다면
					JOptionPane.showMessageDialog(null, "당신의 아이디는 " + cn.rset.getString("yvid") + " 입니다.", "", 1);
					clear_field();
					win.change("panel01");
				} else {
					JOptionPane.showMessageDialog(null, "입력하신 정보와 일치하는 계정이 없습니다", "", 2);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return;
		}
	}

	private static void clear_field() { // 텍스트박스 초기화
		name_tf.setText("");
		phone_tf.setText("");
	}
}

@SuppressWarnings("serial")
class Find_PW_Panel extends JPanel { // 4번째 패널(PW찾기)
	private static JTextField id_tf = new JTextField();
	private static JTextField name_tf = new JTextField();
	private static JTextField phone_tf = new JTextField();
	private Test win;
	private static final JLabel label1 = new JLabel("ID:");
	private static final JLabel label2 = new JLabel("이름:");
	private static final JLabel label3 = new JLabel("전화번호:");
	private static final JButton find_btn = new JButton("찾기");
	private static final JButton back_btn = new JButton("취소");

	public Find_PW_Panel(Test win) { // UI구성
		setLayout(null);
		this.win = win;

		label1.setBounds(98, 96, 77, 15);
		add(label1);

		id_tf.setBounds(123, 93, 160, 21);
		add(id_tf);
		id_tf.setColumns(10);

		label2.setBounds(84, 130, 77, 15);
		add(label2);

		name_tf.setBounds(123, 127, 160, 21);
		add(name_tf);
		name_tf.setColumns(10);

		label3.setBounds(59, 164, 107, 15);
		add(label3);

		phone_tf.setBounds(123, 161, 160, 21);
		add(phone_tf);

		find_btn.setSize(120, 25);
		find_btn.setLocation(50, 215);
		add(find_btn);

		back_btn.setSize(120, 25);
		back_btn.setLocation(190, 215);
		add(back_btn);

		back_btn.addActionListener(new Back_Action());
		find_btn.addActionListener(new Find_Action());

	}

	private class Back_Action implements ActionListener { // 뒤로 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel01");
			clear_field();
		}
	}

	private class Find_Action implements ActionListener { // 찾기 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			final String sql = "select yvuserno from yv_user where yvid=? and yvphone_number=? and yvname=?";
			String id = id_tf.getText();
			String name = name_tf.getText();
			String phone_number = phone_tf.getText();
			if (id.equals("") || name.equals("") || phone_number.equals("")) {
				JOptionPane.showMessageDialog(null, "빈칸없이 정보를 입력해주세요.", "", 0);
				return;
			}
			try {
				cn.pstmt = cn.conn.prepareStatement(sql);
				cn.pstmt.setString(1, id);
				cn.pstmt.setString(2, phone_number);
				cn.pstmt.setString(3, name);
				cn.rset = cn.pstmt.executeQuery();
				if (cn.rset.next()) { // 기입한 정보와 일치하는 아이디가 있다면
					cn.userno = cn.rset.getInt("yvuserno");
					JOptionPane.showMessageDialog(null, "비밀번호 설정 창으로 이동합니다.", "", 1);
					cn.condition = true;
					clear_field();
					win.change("panel07");
				} else {
					JOptionPane.showMessageDialog(null, "입력하신 정보와 일치하는 계정이 없습니다", "", 2);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return;
		}
	}

	private static void clear_field() { // 텍스트박스 초기화
		id_tf.setText("");
		name_tf.setText("");
		phone_tf.setText("");
	}
}

@SuppressWarnings("serial")
class Confirm_PW_Panel extends JPanel { // 5번째 패널 (비밀번호(본인)확인 화면)
	private Test win;
	private static JPasswordField pw_pf = new JPasswordField();
	private static final JLabel label1 = new JLabel("현재 비밀번호:");
	private static final JButton verify_btn = new JButton("확인");
	private static final JButton back_btn = new JButton("취소");

	public Confirm_PW_Panel(Test win) { // UI구성
		setLayout(null);
		this.win = win;

		label1.setBounds(45, 67, 107, 15);
		add(label1);

		pw_pf.setBounds(140, 64, 160, 21);
		add(pw_pf);
		pw_pf.setColumns(10);

		verify_btn.setSize(100, 25);
		verify_btn.setLocation(75, 150);
		add(verify_btn);

		back_btn.setSize(100, 25);
		back_btn.setLocation(190, 150);
		add(back_btn);
		back_btn.addActionListener(new Back_Action());
		verify_btn.addActionListener(new Verify_Action());
	}

	private class Back_Action implements ActionListener { // 뒤로 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel06");
			clear_field();
		}
	}

	private class Verify_Action implements ActionListener { // 확인 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			final String sql = "select yvpw from yv_user where yvuserno=?";
			String pw = String.copyValueOf(pw_pf.getPassword());
			if (pw.equals("")) {
				JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요.", "", 0);
				return;
			} else {
				try {
					cn.pstmt = cn.conn.prepareStatement(sql);
					cn.pstmt.setInt(1, cn.userno);
					cn.rset = cn.pstmt.executeQuery();
					if (cn.rset.next()) {
						if (cn.rset.getString("yvpw").equals(pw)) {
							if (cn.condition)
								win.change("panel09");
							else
								win.change("panel07");
							clear_field();
							return;
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.", "", 2);
			return;
		}
	}

	private static void clear_field() { // 텍스트박스 초기화
		pw_pf.setText("");
	}

}

@SuppressWarnings("serial")
class MainPanel extends JPanel { // 6번째 패널 (메인메뉴)
	private Test win;
	private static final JButton user_mod_btn = new JButton("회원정보 수정");
	private static final JButton back_btn = new JButton("로그아웃");
	private static final JButton appoint_btn = new JButton("접종 예약");
	private static final JButton inquiry_btn = new JButton("접종 조회");
	private static final JButton cancel_btn = new JButton("접종 취소");
	private static final JButton hospital_mod_btn = new JButton("병원리스트 수정");
	private static final JButton vaccine_mod_btn = new JButton("백신 재고리스트 수정");
	private static final JButton commu_btn = new JButton("게시판");

	public MainPanel(Test win) { // UI구성
		setLayout(null);
		this.win = win;

		user_mod_btn.setSize(120, 20);
		user_mod_btn.setLocation(10, 15);
		add(user_mod_btn);

		back_btn.setSize(90, 20);
		back_btn.setLocation(268, 15);
		add(back_btn);

		appoint_btn.setSize(160, 28);
		appoint_btn.setLocation(98, 115);
		add(appoint_btn);

		inquiry_btn.setSize(160, 28);
		inquiry_btn.setLocation(98, 160);
		add(inquiry_btn);

		cancel_btn.setSize(160, 28);
		cancel_btn.setLocation(98, 205);
		add(cancel_btn);

		hospital_mod_btn.setSize(160, 28);
		hospital_mod_btn.setLocation(98, 250);
		add(hospital_mod_btn);

		vaccine_mod_btn.setSize(160, 28);
		vaccine_mod_btn.setLocation(98, 295);
		add(vaccine_mod_btn);

		commu_btn.setSize(160, 28);
		commu_btn.setLocation(98, 340);
		add(commu_btn);

		user_mod_btn.addActionListener(new User_Mod_Action());
		appoint_btn.addActionListener(new Appoint_Action());
		inquiry_btn.addActionListener(new Inquiry_Action());
		back_btn.addActionListener(new Back_Action());
		hospital_mod_btn.addActionListener(new Hospital_Mod_Action());
		vaccine_mod_btn.addActionListener(new Vaccine_Mod_Action());
		cancel_btn.addActionListener(new Cancel_Action());
		commu_btn.addActionListener(new BoardList_Action());

	}

	void menu_manage() {
		if (cn.ismanage) {
			vaccine_mod_btn.setVisible(true);
			hospital_mod_btn.setVisible(true);
		} else {
			vaccine_mod_btn.setVisible(false);
			hospital_mod_btn.setVisible(false);
		}
	}

	private class BoardList_Action implements ActionListener { // 게시판 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			new BoardList();
			BoardDAO dao = new BoardDAOImpl();
			dao.get_userid(cn.userno);
		}
	}

	private class User_Mod_Action implements ActionListener { // 회원정보 수정 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			cn.condition = false;
			win.change("panel05");
		}
	}

	private class Appoint_Action implements ActionListener { // 예약 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel08");
		}
	}

	private class Inquiry_Action implements ActionListener { // 조회 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			cn.condition = false;
			win.change("panel09");
		}
	}

	private class Back_Action implements ActionListener { // 뒤로 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel01");
		}
	}

	private class Hospital_Mod_Action implements ActionListener { // 병원 관리 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel12");
		}
	}

	private class Vaccine_Mod_Action implements ActionListener { // 백신 관리 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel13");
		}
	}

	private class Cancel_Action implements ActionListener { // 예약 취소 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			cn.condition = true;
			win.change("panel05");
		}
	}

	private class MyActionListener8 implements ActionListener { // 접종 변경 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel14");
		}
	}

	private class MyActionListener9 implements ActionListener { // 게시판 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel14");
		}
	}
}

@SuppressWarnings("serial")
class User_Modify_Panel extends JPanel { // 7번째 패널 (비밀번호 변경 및 회원정보 수정)
	private Test win;
	private static JPasswordField pw_pf = new JPasswordField();
	private static JPasswordField pw_confirm_pf = new JPasswordField();
	private static JTextField phone_tf = new JTextField();
	private static final JLabel label1 = new JLabel("새 비밀번호:");
	private static final JLabel label2 = new JLabel("새 비밀번호 확인:");
	private static final JLabel label3 = new JLabel("새 전화번호 입력:");
	private static JButton modify_btn = new JButton("변경");
	private static JButton back_btn = new JButton("취소");

	public User_Modify_Panel(Test win) { // UI구성
		setLayout(null);
		this.win = win;

		label1.setBounds(68, 67, 107, 15);
		add(label1);

		pw_pf.setBounds(145, 64, 160, 21);
		add(pw_pf);
		pw_pf.setColumns(10);

		label2.setBounds(40, 107, 107, 15);
		add(label2);

		pw_confirm_pf.setBounds(145, 104, 160, 21);
		add(pw_confirm_pf);

		label3.setBounds(40, 147, 107, 15);
		add(label3);

		phone_tf.setBounds(145, 144, 160, 21);
		add(phone_tf);
		phone_tf.setColumns(10);

		modify_btn.setSize(80, 25);
		modify_btn.setLocation(100, 190);
		add(modify_btn);

		back_btn.setSize(80, 25);
		back_btn.setLocation(190, 190);
		add(back_btn);

		back_btn.addActionListener(new Back_Action());
		modify_btn.addActionListener(new Modify_Action());
	}

	private class Back_Action implements ActionListener { // 뒤로 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			if (cn.condition)
				win.change("panel01");
			else
				win.change("panel06");
			clear_field();
		}
	}

	private class Modify_Action implements ActionListener { // 변경 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			String pw = String.copyValueOf(pw_pf.getPassword());
			String pw_confirm = String.copyValueOf(pw_confirm_pf.getPassword());
			String phone_num = phone_tf.getText();
			if (pw.equals("") && pw_confirm.equals("") && phone_num.equals("")) {
				JOptionPane.showMessageDialog(null, "변경된 사항이 없습니다.", "", 2);
				return;
			}

			if (pw.equals(pw_confirm)) {
				for (int i = 0; i < pw.length(); i++) {
					if (pw.charAt(i) < 0x21 || pw.charAt(i) > 0x7E) {
						JOptionPane.showMessageDialog(null, "비밀번호는 영문, 숫자 및 일부 특수문자만 사용 가능합니다.", "", 0);
						return;
					}
				}

				if (pw.length() == 0) {
					JOptionPane.showMessageDialog(null, "전화번호가 수정 되었습니다.", "", 1);
					set_phone(phone_num);
					clear_field();
					win.change("panel06");
					return;
				} else if (pw.length() < 4) {
					JOptionPane.showMessageDialog(null, "비밀번호가 너무 짧습니다.", "", 0);
					return;
				} else if (pw.length() > 20) {
					JOptionPane.showMessageDialog(null, "비밀번호가 너무 깁니다.", "", 0);
					return;
				} else {
					JOptionPane.showMessageDialog(null, "재설정한 비밀번호로 로그인 해주세요.", "", 1);
					if (!phone_num.equals(""))
						set_phone(phone_num);
					set_pw(pw);
					clear_field();
					win.change("panel01");
					return;
				}
			} else {
				JOptionPane.showMessageDialog(null, "비밀번호 재입력이 올바르게 되지 않았습니다.", "", 2);
			}
			return;
		}
	}

	private static void clear_field() { // 텍스트박스 초기화
		pw_pf.setText("");
		pw_confirm_pf.setText("");
		phone_tf.setText("");
	}

	private static void set_phone(String sphone) {
		final String sql = "update yv_user set yvphone_number = ? where yvuserno = ?";
		try {
			cn.pstmt = cn.conn.prepareStatement(sql);
			cn.pstmt.setString(1, sphone);
			cn.pstmt.setInt(2, cn.userno);
			cn.pstmt.executeUpdate();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private static void set_pw(String spw) {
		final String sql = "update yv_user set yvpw = ? where yvuserno = ?";
		try {
			cn.pstmt = cn.conn.prepareStatement(sql);
			cn.pstmt.setString(1, spw);
			cn.pstmt.setInt(2, cn.userno);
			cn.pstmt.executeUpdate();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	void is_lost() {
		if (cn.condition) {
			label3.setVisible(false);
			phone_tf.setVisible(false);
		} else {
			label3.setVisible(true);
			phone_tf.setVisible(true);
		}
	}
}

@SuppressWarnings("serial")
class JPanel08 extends JPanel { // 8번째 패널 (접종예약 화면)
	private Test win;
	private static ArrayList<String> locate_arr = new ArrayList<String>();
	private static ArrayList<String> hospital_arr = new ArrayList<String>();
	private static ArrayList<String> vaccine_arr = new ArrayList<String>();
	private static JComboBox<String> name_cb = new JComboBox<String>();
	private static JComboBox<String> hospital_cb = new JComboBox<String>();
	private static JComboBox<String> vaccine_cb = new JComboBox<String>();
	private static final JButton appoint_btn = new JButton("예약");
	private static final JButton back_btn = new JButton("취소");

	public JPanel08(Test win) {

		setLayout(null);
		this.win = win;

		name_cb.setBounds(80, 67, 176, 21);
		add(name_cb);

		hospital_cb.setBounds(80, 104, 176, 21);
		add(hospital_cb);

		vaccine_cb.setBounds(56, 140, 224, 21);
		add(vaccine_cb);

		appoint_btn.setSize(60, 25);
		appoint_btn.setLocation(98, 190);
		add(appoint_btn);

		back_btn.setSize(60, 25);
		back_btn.setLocation(178, 190);
		add(back_btn);

		back_btn.addActionListener(new MyActionListener());
		appoint_btn.addActionListener(new MyActionListener2());

		/*
		 * locate.toArray(new String[locate.size()]) hospital.toArray(new
		 * String[hospital.size()]) vaccine.toArray(new String[vaccine.size()])
		 */

	}

	private class MyActionListener implements ActionListener { // 뒤로 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel06");
		}
	}

	private class MyActionListener2 implements ActionListener { // 예약 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel06");
		}
	}
}

@SuppressWarnings("serial")
class JPanel09 extends JPanel { // 9번째 패널 (예약 조회 및 예약 취소)
	private Test win;
	private static final JLabel label1 = new JLabel("xxx님의 접종일은");
	private static final JLabel label2 = new JLabel("xxx병원");
	private static final JLabel label3 = new JLabel("YYYY-MM-DD");
	private static final JLabel label4 = new JLabel("hh:mm 입니다.");
	private static final JLabel label5 = new JLabel("정말 취소하시겠습니까?");
	private static final JButton back_btn = new JButton("확인");
	private static final JButton confirm_btn = new JButton("예약취소");
	private static final JButton back2_btn = new JButton("돌아가기");

	public JPanel09(Test win) { // UI구성

		setLayout(null);
		this.win = win;

		label1.setFont(new Font("Serif", Font.BOLD, 20));
		label1.setHorizontalAlignment(JLabel.CENTER);
		label1.setBounds(-20, 20, 400, 50);
		add(label1);

		label2.setFont(new Font("Serif", Font.BOLD, 23));
		label2.setHorizontalAlignment(JLabel.CENTER);
		label2.setBounds(-20, 45, 400, 50);
		add(label2);

		label3.setFont(new Font("Serif", Font.BOLD, 23));
		label3.setHorizontalAlignment(JLabel.CENTER);
		label3.setBounds(-20, 70, 400, 50);
		add(label3);

		label4.setFont(new Font("Serif", Font.BOLD, 23));
		label4.setHorizontalAlignment(JLabel.CENTER);
		label4.setBounds(-20, 95, 400, 50);
		add(label4);

		label5.setFont(new Font("Serif", Font.BOLD, 27));
		label5.setHorizontalAlignment(JLabel.CENTER);
		label5.setBounds(-20, 135, 400, 50);
		add(label5);

		back_btn.setSize(80, 25);
		back_btn.setLocation(142, 165);
		add(back_btn);

		confirm_btn.setSize(85, 25);
		confirm_btn.setLocation(90, 205);
		add(confirm_btn);

		back2_btn.setSize(85, 25);
		back2_btn.setLocation(190, 205);
		add(back2_btn);

		back_btn.addActionListener(new MyActionListener());
		back2_btn.addActionListener(new MyActionListener());
		confirm_btn.addActionListener(new MyActionListener2());
	}

	void is_inquiry() {
		if (cn.condition) {
			back_btn.setVisible(false);
			label5.setVisible(true);
			back2_btn.setVisible(true);
			confirm_btn.setVisible(true);
		} else {
			back_btn.setVisible(true);
			label5.setVisible(false);
			back2_btn.setVisible(false);
			confirm_btn.setVisible(false);
		}
	}

	private class MyActionListener implements ActionListener { // 뒤로 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel06");
		}
	}

	private class MyActionListener2 implements ActionListener { // 예약취소 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel06");
		}
	}
}

@SuppressWarnings("serial")
class JPanel10 extends JPanel { // 10번째 패널 (글쓰기 화면)
	private JTextField textField;
	private Test win;

	public JPanel10(Test win) {
		String[] vac_list = { "화이자", "모더나", "아스트라제네카", "얀센" };

		setLayout(null);
		this.win = win;

		JComboBox<String> vacCombo = new JComboBox<String>(vac_list);
		vacCombo.setBounds(80, 67, 110, 21);
		add(vacCombo);

		textField = new JTextField();
		textField.setBounds(200, 67, 170, 21);
		add(textField);
		textField.setColumns(10);

		JButton loginBtn = new JButton("예약");
		loginBtn.setSize(60, 25);
		loginBtn.setLocation(98, 190);
		add(loginBtn);

		JButton backBtn = new JButton("취소");
		backBtn.setSize(60, 25);
		backBtn.setLocation(178, 190);
		add(backBtn);
		backBtn.addActionListener(new MyActionListener());

		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "경고창 !");

			}
		});

	}

	private class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel01");
		}
	}
}

@SuppressWarnings("serial")
class JPanel12 extends JPanel { // 12번째 패널 (병원리스트 수정)
	private Test win;
	private static ArrayList<String> locate_arr = new ArrayList<String>();
	private static final JLabel label1 = new JLabel("병원이름 입력");
	private static final JLabel label2 = new JLabel("전화번호 입력");
	private static final JButton back_btn = new JButton("뒤로");
	private static final JButton add_btn = new JButton("추가");
	private static final JButton del_btn = new JButton("삭제");

	public JPanel12(Test win) {

		setLayout(null);
		this.win = win;

		back_btn.setSize(70, 20);
		back_btn.setLocation(10, 15);
		add(back_btn);

		JComboBox<String> nameCombo = new JComboBox<String>();
		nameCombo.setBounds(113, 67, 140, 21);
		add(nameCombo);
		nameCombo.setModel(new DefaultComboBoxModel<String>() {
			boolean selectionAllowed = true;

			@Override
			public void setSelectedItem(Object anObject) {
				if (!"지역 선택".equals(anObject)) {
					super.setSelectedItem(anObject);
				} else if (selectionAllowed) {
					selectionAllowed = false;
					super.setSelectedItem(anObject);
				}
			}
		});
		nameCombo.addItem("지역 선택");
		/* locate.toArray(new String[locate.size()]) */

		label1.setBounds(25, 110, 150, 15);
		add(label1);

		JTextField input_hName = new JTextField();
		input_hName.setBounds(113, 107, 140, 21);
		add(input_hName);
		input_hName.setColumns(10);

		label2.setBounds(25, 155, 150, 15);
		add(label2);

		JTextField input_pNumber = new JTextField();
		input_pNumber.setBounds(113, 153, 140, 21);
		add(input_pNumber);
		input_pNumber.setColumns(10);

		add_btn.setSize(60, 23);
		add_btn.setLocation(112, 195);
		add(add_btn);

		del_btn.setSize(60, 23);
		del_btn.setLocation(192, 195);
		add(del_btn);

		back_btn.addActionListener(new MyActionListener3());

		add_btn.addActionListener(new MyActionListener2());

		del_btn.addActionListener(new MyActionListener1());

	}

	private class MyActionListener1 implements ActionListener { // 추가 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel06");
		}
	}

	private class MyActionListener2 implements ActionListener { // 삭제 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel06");
		}
	}

	private class MyActionListener3 implements ActionListener { // 뒤로 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel06");
		}
	}
}

@SuppressWarnings("serial")
class JPanel13 extends JPanel { // 13번째 (백신 재고리스트 수정)
	private Test win;
	private static JTextField volume_tf = new JTextField();
	private static JTextField input_time = new JTextField();
	private static ArrayList<String> locate_arr = new ArrayList<String>();
	private static ArrayList<String> hospital_arr = new ArrayList<String>();
	private static ArrayList<String> vaccine_arr = new ArrayList<String>();
	private static JComboBox<String> name_cb = new JComboBox<String>();
	private static JComboBox<String> hospital_cb = new JComboBox<String>();
	private static JComboBox<String> vaccine_cb = new JComboBox<String>();
	private static final JLabel lblLb1 = new JLabel("시간");
	private static final JLabel lblLb2 = new JLabel("재고");
	private static final JButton add_btn = new JButton("추가");
	private static final JButton del_btn = new JButton("삭제");
	private static final JButton back_btn = new JButton("뒤로");
	private static final JButton addVacType = new JButton("백신종류추가");

	public JPanel13(Test win) {

		setLayout(null);
		this.win = win;

		back_btn.setSize(70, 20);
		back_btn.setLocation(10, 15);
		add(back_btn);

		addVacType.setSize(115, 20);
		addVacType.setLocation(247, 15);
		add(addVacType);

		name_cb.setBounds(113, 67, 140, 21);
		add(name_cb);

		hospital_cb.setBounds(113, 107, 140, 21);
		add(hospital_cb);

		vaccine_cb.setBounds(113, 147, 140, 21);
		add(vaccine_cb);

		/*
		 * locate.toArray(new String[locate.size()]) hospital.toArray(new
		 * String[hospital.size()]) vaccine.toArray(new String[vaccine.size()])
		 */

		lblLb1.setBounds(80, 190, 150, 15);
		add(lblLb1);

		input_time.setBounds(113, 187, 140, 21);
		add(input_time);
		input_time.setColumns(10);

		lblLb2.setBounds(80, 230, 150, 15);
		add(lblLb2);

		volume_tf.setBounds(113, 227, 140, 21);
		add(volume_tf);
		volume_tf.setColumns(10);

		add_btn.setSize(60, 23);
		add_btn.setLocation(112, 272);
		add(add_btn);

		del_btn.setSize(60, 23);
		del_btn.setLocation(192, 272);
		add(del_btn);

		back_btn.addActionListener(new MyActionListener3());

		add_btn.addActionListener(new MyActionListener2());

		del_btn.addActionListener(new MyActionListener1());

		addVacType.addActionListener(new MyActionListener4());

	}

	private class MyActionListener1 implements ActionListener { // 추가 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel06");
		}
	}

	private class MyActionListener2 implements ActionListener { // 삭제 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel06");
		}
	}

	private class MyActionListener3 implements ActionListener { // 뒤로 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel06");
		}
	}

	private class MyActionListener4 implements ActionListener { // 백신종류 추가 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			win.change("panel14");
		}
	}
}

@SuppressWarnings("serial")
class JPanel14 extends JPanel { // 14번째 패널

	private Test win;
	private static JTextField vaccine_tf = new JTextField();
	private static final JLabel label1 = new JLabel("백신이름");
	private static final JButton add_btn = new JButton("추가");
	private static final JButton del_btn = new JButton("삭제");
	private static final JButton back_btn = new JButton("뒤로");

	public JPanel14(Test win) {

		setLayout(null);
		this.win = win;

		back_btn.setSize(70, 20);
		back_btn.setLocation(10, 15);
		add(back_btn);

		label1.setBounds(60, 190, 150, 15);
		add(label1);

		vaccine_tf.setBounds(125, 187, 160, 21);
		add(vaccine_tf);
		vaccine_tf.setColumns(10);

		add_btn.setSize(80, 23);
		add_btn.setLocation(98, 272);
		add(add_btn);

		del_btn.setSize(80, 23);
		del_btn.setLocation(188, 272);
		add(del_btn);

		back_btn.addActionListener(new MyActionListener3());

		add_btn.addActionListener(new MyActionListener1());

		del_btn.addActionListener(new MyActionListener2());

	}

	private class MyActionListener1 implements ActionListener { // 추가 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			final String sql = "insert into VACCINETYPE values(?)";
			try {
				cn.pstmt = cn.conn.prepareStatement(sql);
				cn.pstmt.setString(1, vaccine_tf.getText());
				cn.pstmt.executeUpdate();
			} catch (Exception ee) {
				System.out.println(ee);
			}
		}
	}

	private class MyActionListener2 implements ActionListener { // 삭제 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			final String sql = "delete from VACCINETYPE where vname = ?";
			try {
				cn.pstmt = cn.conn.prepareStatement(sql);
				cn.pstmt.setString(1, vaccine_tf.getText());
				cn.pstmt.executeUpdate();
			} catch (Exception ee) {
				System.out.println(ee);
			}
		}
	}

	private class MyActionListener3 implements ActionListener { // 뒤로 버튼
		@Override
		public void actionPerformed(ActionEvent e) {
			clear_field();
			win.change("panel13");
		}
	}

	private static void clear_field() { // 텍스트박스 초기화
		vaccine_tf.setText("");
	}

}

@SuppressWarnings("serial")
class Test extends JFrame {

	public Login_Panel jpanel01 = null;
	public SignUp_Panel jpanel02 = null;
	public Find_ID_Panel jpanel03 = null;
	public Find_PW_Panel jpanel04 = null;
	public Confirm_PW_Panel jpanel05 = null;
	public MainPanel JPanel06 = null;
	public User_Modify_Panel JPanel07 = null;
	public JPanel08 JPanel08 = null;
	public JPanel09 JPanel09 = null;
	public JPanel10 JPanel10 = null;
	public JPanel12 JPanel12 = null;
	public JPanel13 JPanel13 = null;
	public JPanel14 JPanel14 = null;

	public void change(String panelName) { // 패널 변경 후 재설정

		if (panelName.equals("panel01")) {
			getContentPane().removeAll();
			getContentPane().add(jpanel01);
			revalidate();
			repaint();
		}

		else if (panelName.equals("panel03")) {
			getContentPane().removeAll();
			getContentPane().add(jpanel03);
			revalidate();
			repaint();
		}

		else if (panelName.equals("panel04")) {
			getContentPane().removeAll();
			getContentPane().add(jpanel04);
			revalidate();
			repaint();
		}

		else if (panelName.equals("panel05")) {
			getContentPane().removeAll();
			getContentPane().add(jpanel05);
			revalidate();
			repaint();
		}

		else if (panelName.equals("panel06")) {
			JPanel06.menu_manage();
			getContentPane().removeAll();
			getContentPane().add(JPanel06);
			revalidate();
			repaint();
		}

		else if (panelName.equals("panel07")) {
			JPanel07.is_lost();
			getContentPane().removeAll();
			getContentPane().add(JPanel07);
			revalidate();
			repaint();
		}

		else if (panelName.equals("panel08")) {
			getContentPane().removeAll();
			getContentPane().add(JPanel08);
			revalidate();
			repaint();
		}

		else if (panelName.equals("panel09")) {
			JPanel09.is_inquiry();
			getContentPane().removeAll();
			getContentPane().add(JPanel09);
			revalidate();
			repaint();
		}

		else if (panelName.equals("panel10")) {
			getContentPane().removeAll();
			getContentPane().add(JPanel09);
			revalidate();
			repaint();
		}

		else if (panelName.equals("panel12")) {
			getContentPane().removeAll();
			getContentPane().add(JPanel12);
			revalidate();
			repaint();
		}

		else if (panelName.equals("panel13")) {
			getContentPane().removeAll();
			getContentPane().add(JPanel13);
			revalidate();
			repaint();
		}

		else if (panelName.equals("panel14")) {
			getContentPane().removeAll();
			getContentPane().add(JPanel14);
			revalidate();
			repaint();
		}

		else {
			getContentPane().removeAll();
			getContentPane().add(jpanel02);
			revalidate();
			repaint();
		}
	}

	public static void main(String[] args) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn.conn = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/xepdb1", "scott", "tiger");
		} catch (Exception e) {
			System.out.println(e);
		}
		Test win = new Test();

		win.setTitle("YUVID");
		win.jpanel01 = new Login_Panel(win);
		win.jpanel02 = new SignUp_Panel(win);
		win.jpanel03 = new Find_ID_Panel(win);
		win.jpanel04 = new Find_PW_Panel(win);
		win.jpanel05 = new Confirm_PW_Panel(win);
		win.JPanel06 = new MainPanel(win);
		win.JPanel07 = new User_Modify_Panel(win);
		win.JPanel08 = new JPanel08(win);
		win.JPanel09 = new JPanel09(win);
		win.JPanel10 = new JPanel10(win);
		win.JPanel12 = new JPanel12(win);
		win.JPanel13 = new JPanel13(win);
		win.JPanel14 = new JPanel14(win);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

		Dimension win_size = win.getSize();

		int x_pos = (int) (screen.getWidth() / 2 - win_size.getWidth() / 2);
		int y_pos = (int) (screen.getHeight() / 2 - win_size.getHeight() / 2);
		win.setLocation(x_pos - 190, y_pos - 350);

		win.add(win.jpanel01);
		win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		win.setSize(380, 700);
		win.setResizable(false);
		win.setVisible(true);
	}
}