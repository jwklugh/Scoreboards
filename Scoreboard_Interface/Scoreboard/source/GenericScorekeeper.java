import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.io.FileReader; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class GenericScorekeeper extends PApplet {

FileIO io;
Players ps;
Match m;
static final String SETTINGS_LOCATION = "ScoreboardSettings.txt";
                    

int i1 = 0, // Defender   Increment ......... q
    d1 = 0, // Defender   Decrement ......... Q
    i2 = 0, // Challenger Increment ......... p
    d2 = 0, // Challenger Decrement ......... P
    sb = 0, // Swap Break ................... B
    te = 0, // Time Extention  Toggle........ T
    le = 0, // Time Limit Extention Toggle .. L
    re = 0, // Race to Extention Toggle ..... R
    ce = 0, // Score cap Extention Toggle ... C
    ss = 0; // Start/Stop Timer ............. S
PImage backgroundImg;
boolean saved;

final int fps = 10;
final boolean HIDDEN = false, VISIBLE = true;

Extention timeExt, raceExt, limiExt, sCapExt;

public void setup() {
  frameRate(fps);
  
  backgroundImg = loadImage("background0.png");
  setupMatchFromIO();
  timeExt = new Extention(150,229,BOTTOM,VISIBLE);
  raceExt = new Extention(406,229,BOTTOM,VISIBLE);
  limiExt = new Extention(150,15,TOP,HIDDEN);
  sCapExt = new Extention(406,15,TOP,HIDDEN);
}

public void draw() {
  background(0,255,0);
  timeExt.draw(m.t.toString(),34);
  raceExt.draw(m.raceStr(),34);
  limiExt.draw(m.limitStr(),20);
  sCapExt.draw(m.capStr(),23);
  if(backgroundImg != null)
    image(backgroundImg,0,60,width,181);
  m.draw();
  m.t.updateTime();
  checkSave();
  //debug();
}

public void keyPressed() {
  switch(key) {
    case 'q' : { if(i1<1) {scoreChange(true ,true );  i1++;} break;}
    case 'Q' : { if(d1<1) {scoreChange(false,true );  d1++;} break;}
    case 'p' : { if(i2<1) {scoreChange(true ,false);  i2++;} break;}
    case 'P' : { if(d2<1) {scoreChange(false,false);  d2++;} break;}
    case 'T' : { if(te<1) {timeExt.toggle();          te++;} break;}
    case 'R' : { if(re<1) {raceExt.toggle();          re++;} break;}
    case 'L' : { if(le<1) {limiExt.toggle();          le++;} break;}
    case 'C' : { if(ce<1) {sCapExt.toggle();          ce++;} break;}
    case 'b' : { if(sb<1) {sB();                      sb++;} break;}
    case 'S' : { if(ss<1) {m.t.toggle();              ss++;} break;}
  }
}

public void keyReleased() {
  switch(key) {
    case 'q' : { i1=0; break;}
    case 'Q' : { d1=0; break;}
    case 'p' : { i2=0; break;}
    case 'P' : { d2=0; break;}
    case 'T' : { te=0; break;}
    case 'R' : { re=0; break;}
    case 'L' : { le=0; break;}
    case 'C' : { ce=0; break;}
    case 'b' : { sb=0; break;}
    case 'S' : { ss=0; break;}
  }
}

public void scoreChange(boolean incriment, boolean defender) {
  if(incriment)
    if(defender)
      m.p.d.s++;
    else
      m.p.c.s++;
  else
    if(defender)
      m.p.d.s--;
    else
      m.p.c.s--;
  sB();
  io.save(m.toString());
}

public void sB() { //Short Swap Break Method
  m.defBreak = !m.defBreak;
  io.save(m.toString());
}


public void setupMatchFromIO() {
  try {
    //Construct a fileIO from the settings file (edited by the launcher)
    io = new FileIO(SETTINGS_LOCATION);
    
    //Construct an info struct from the file contained in settings
    InfoStruct info = io.getInFile();
    
    //Build the players from the info struct
    ps = new Players(info.dn(),info.dr(), info.cs()
                    ,info.cn(),info.cr(), info.ds());
                    
    //Build the match from the info struct
    m  = new Match(ps, info.rt(),info.sc(),
                       info.tl()*1000,info.ct()*1000,info.db());
    saved = false;
  } catch (IOException e) {
    System.out.println("Error in file IO: \n" + SETTINGS_LOCATION +"\n" + e);
    System.exit(0);
  }
}

public void checkSave() {
  //For the first second every 30 seconds, if you haven't already saved, do so
  if(m.t.getMillis() % 30000 <= 1000 && !saved) {
    saved = io.save(m.toString());
  } else if(1000 < m.t.getMillis() %30000) {
    saved = false;
  }
}


public void debug() {
  //cursor pointers for drawing placement and other information
  textSize(10);
  textAlign(LEFT);
  String[] info = { ""+mouseX , ""+mouseY};
  for(int i=0; i<info.length; i++) {
    text(""+info[i],3,10+i*11);
  }
}
class Extention {
  int x;
  int y;//while visible
  float curY;
  boolean top;
  boolean visible;
  boolean out;
  float stage; //stage should be min 0 (all the way out) and max frameRate (all the way in)
  final float speed = .5f; //speed modifier for pop time : 1 = 1 second
  PImage img; 
  
  
  Extention(int x, int y, boolean t) {
    this.x=x;
    this.y=y;
    top = t;
    img = top?
          loadImage("ExtentionTop.png")   :
          loadImage("ExtentionBottom.png");
    visible = false;
    out = false;
    stage = fps * speed;
  }
  
  Extention(int x, int y, int t) {
    this.x=x;
    this.y=y;
    top = t != BOTTOM;
    img = top?
          loadImage("ExtentionTop.png")   :
          loadImage("ExtentionBottom.png");
    visible = false;
    out = false;
    stage = fps * speed;
  }
  
  Extention(int x, int y, int t, boolean out) {
    this.x=x;
    this.y=y;
    top = t != BOTTOM;
    img = top?
          loadImage("ExtentionTop.png")   :
          loadImage("ExtentionBottom.png");
    visible = out;
    this.out = out;
    stage = out?0:(fps * speed);
  }
  
  public void transition() {
    if(out) {
      if(!visible) visible = true;
      if(stage > 0) stage--;
    } else {
      if(stage < fps * speed) stage++;
      else visible = false;
    }
    curY = PApplet.parseInt(y +
              (top?
               stage*50/(fps * speed)  :
              -stage*50/(fps * speed) ));
  }
  
  public void toggle() {
    out = !out;
  }
  
  public void draw() {
    transition();
    if(visible) image(img,x,curY);
  }
  
  public void draw(String text, int textSize) {
    transition();
    if(visible) {
      textSize(textSize);
      textAlign(CENTER);
      float textX = x+100;
      float textY = curY+30+textSize*13/34;
      image(img,x,curY);
      text(text,textX,textY);
    }
  }
}



class FileIO {
  
  BufferedReader reader;
  String outFile;
  
  FileIO(String fileName) throws IOException {
    reader = createReader(fileName);
  }
  
  public void setOut(String fileName) {
    outFile = fileName;
  }
  
  public InfoStruct getInFile() throws IOException{
    setOut(reader.readLine());
    return getInfo();
  }
  
  // REQUIRED INFORMATION
  // Challenger Name, Challenger Rank,
  // Defeneder Name, Defender Rank,
  // Number to Race to, Score Cap, 
  // Time Limit, Current Time, and
  // Out File
  
  public InfoStruct getInfo() throws IOException{
    
    String[] in = reader.readLine().split(",");
    
    return new InfoStruct(
    in[0],in[1],           //CName, DName
    parseInt(in[2]),       //CRank
    parseInt(in[3]),       //DRank
    parseInt(in[4]),       //CScore
    parseInt(in[5]),       //DScore
    parseInt(in[6]),       //Race
    parseInt(in[7]),       //Cap
    parseInt(in[8]),       //Limit
    parseInt(in[9]),       //Curr
    parseBoolean(in[10])); //DBreak //<>//
  }
  
  public boolean save(String state) {
    if(outFile != null) {
      PrintWriter writer = createWriter(outFile);
      writer.append(state);
      writer.flush();
      writer.close();
      return true;
    }
    return false;
  }
}
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
  public String  cn() {return Challenger_name; }
  public String  dn() {return Defender_name;   }
  public int     cr() {return Challenger_rank; }
  public int     dr() {return Defender_rank;   }
  public int     cs() {return Challenger_score;}
  public int     ds() {return Defender_score;  }
  public int     rt() {return Race_to;         }
  public int     sc() {return Score_cap;       }
  public int     tl() {return Time_limit;      }
  public int     ct() {return Current_time;    }
  public boolean db() {return Defender_break;  }
  
  //short set Methods
  public void cn (String  s) {Challenger_name  =s;}
  public void dn (String  s) {Defender_name    =s;}
  public void cr (int     i) {Challenger_rank  =i;}
  public void dr (int     i) {Defender_rank    =i;}
  public void cs (int     i) {Challenger_score =i;}
  public void ds (int     i) {Defender_score   =i;}
  public void rt (int     i) {Race_to          =i;}
  public void sc (int     i) {Score_cap        =i;}
  public void tl (int     i) {Time_limit       =i;}
  public void ct (int     i) {Current_time     =i;}
  public void db (boolean b) {Defender_break   =b;}
  
  public String toString() {
    return cn()+dn()+cr()+dr()+cs()+ds()+rt()+sc()+tl()+ct()+db();
  }
}
class Match {
  Players p;
  int race;
  int cap;
  int limit;
  TimeKeeper t;
  boolean defBreak;

  Match(Players a, int b, int c, int d, int e, boolean f) {
    p = a;
    race = b;
    cap = c;
    limit = d;
    t = new TimeKeeper(e);
    defBreak = f;
  }
  
  public String toString() {
    return p.toString()+","+ race +","+ cap +","
           +limit/1000+","+t.getTime()+","+defBreak;
  }
  
  
  
  public void drawTime(int x, int y) {
    textSize(34);
    textAlign(CENTER);
    text("" + t.toString(),x,y);
  }
  
  public String limitStr() {
    return "Time up at "+ limit/1000 + ":00" ;
  }
  
  public String raceStr() {
    if(race > p.d.s && race > p.c.s)
      return "Race to " + race;
    return "Win by 2";
  }
  
  public String capStr() {
    return "Score Cap at " + cap;
  }
  
  public void draw() {
    p.draw();
    drawBreak();
  }
  
  public void drawBreak() {
    ellipseMode(RADIUS);
    if(defBreak) {
      ellipse(510,105,6,6);
    }
    else {
      ellipse(510,195,6,6);
    }
  }
}
class Player {
  String n;
  int r;
  int s;
  
  Player(String name, int rank, int score) {
    n = name;
    r = rank;
    s = score;
  }
}
class Players {
  Player d;
  Player c;
  
  Players(String defender, int dr, int ds, String challenger, int cr, int cs) {
    d = new Player(defender, dr, ds);
    c = new Player(challenger, cr, cs);
  }
  
  public void draw() {
    noStroke();
    fill(255);
    rectMode(CORNERS);
    //rect(width-23,width*4/3-30,width,width*4/3);
    //rect(width-23,height-30,width,height);

    stroke(0);
    fill(0);
    textSize(50);
    
    //Defender
    textAlign(LEFT);
    text(d.n,185,125);
    textAlign(CENTER);
    text(d.s,690,125);
    textAlign(RIGHT);
    text(d.r,590,125);
    
    
    //Challenger
    textAlign(LEFT);
    textSize(50);
    text(c.n,185,215);
    textAlign(CENTER);
    textSize(50);
    text(c.s,690,215);
    textAlign(RIGHT);
    text(c.r,590,215);
    
    
    ////Race to Text
    //textAlign(CENTER);
    //textSize(30);
    //text("Race to " + race,611,115);
  }
  
  public String toString() {
    return c.n+","+d.n +","+ c.r+","+d.r +","+ c.s+","+d.s;
    //        names             ranks             scores
  }
}
class TimeKeeper {
  int time;
  int iniTime;
  int difTime;
  boolean running;
  
  TimeKeeper(int in) {
    iniTime = in;
    time = iniTime;
  }
  
  public int getMillis() {
    return time;
  }
  
  public int getTime() {
    return time/1000;
  }
  
  public void updateTime() {
    if(running)
      time += (millis() - difTime);
    updateDif();
  }
  
  public void updateDif() {
    difTime = millis();
  }
  
  public void toggle() {
    running = !running;
  }
  
  public String toString() {
    return "" + getTime()/60 + ":" + (getTime()%60>9?"":"0") + getTime()%60;
  }
}
  public void settings() {  size(777,301); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "GenericScorekeeper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
