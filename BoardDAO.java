import java.util.List;
 
public interface BoardDAO {
   
    public int insert(BoardVO vo);
   
//    public int update(BoardVO vo);
   
    public int delete(BoardVO vo);
   
    public BoardVO search(BoardVO vo);
   
    public List<BoardVO> select();
   
    public void search(String search, String searchString);
    
    public String get_userid(int writer);
    
    public int get_userno(String userid);
   
    public boolean password_check(String pw, int writer);
}

