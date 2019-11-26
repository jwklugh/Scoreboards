class InfoStruct {
  private String  Challenger_name
; private String  Defender_name
; 
  private int     Challenger_rank
; private int     Defender_rank
;
  private int     Challenger_score
; private int     Defender_score
;
  private int     Race_to
; private int     Score_cap;
; private int     Time_limit
; private int     Current_time
;
  private boolean Defender_break
;
  
  InfoStruct() { }
  InfoStruct(String a, String b,   //CName,  DName,
             int c,    int d,      //CRank,  DRank,
             int e,    int f,      //CScore, DScore,
             int g,    int h,      //Race,   Cap,
             int i,    int j,      //Limit,  Curr,
             boolean k) {          //DBreak
    cn(a);
    dn(b);
    cr(c);
    dr(d);
    cs(e);
    ds(f);
    rt(g);
    sc(h);
    tl(i);
    ct(j);
    db(k);
  }

  //short fetch Methods
  String  cn() {return Challenger_name; }
  String  dn() {return Defender_name;   }
  int     cr() {return Challenger_rank; }
  int     dr() {return Defender_rank;   }
  int     cs() {return Challenger_score;}
  int     ds() {return Defender_score;  }
  int     rt() {return Race_to;         }
  int     sc() {return Score_cap;       }
  int     tl() {return Time_limit;      }
  int     ct() {return Current_time;    }
  boolean db() {return Defender_break;  }
  
  //short set Methods
  void cn (String  s) {Challenger_name  =s;}
  void dn (String  s) {Defender_name    =s;}
  void cr (int     i) {Challenger_rank  =i;}
  void dr (int     i) {Defender_rank    =i;}
  void cs (int     i) {Challenger_score =i;}
  void ds (int     i) {Defender_score   =i;}
  void rt (int     i) {Race_to          =i;}
  void sc (int     i) {Score_cap        =i;}
  void tl (int     i) {Time_limit       =i;}
  void ct (int     i) {Current_time     =i;}
  void db (boolean b) {Defender_break   =b;}
  
  String toString() {
    return cn()+dn()+cr()+dr()+cs()+ds()+rt()+sc()+tl()+ct()+db();
  }
}
