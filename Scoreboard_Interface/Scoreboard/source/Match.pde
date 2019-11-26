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
  
  String toString() {
    return p.toString()+","+ race +","+ cap +","
           +limit/1000+","+t.getTime()+","+defBreak;
  }
  
  
  
  void drawTime(int x, int y) {
    textSize(34);
    textAlign(CENTER);
    text("" + t.toString(),x,y);
  }
  
  String limitStr() {
    return "Time up at "+ limit/1000 + ":00" ;
  }
  
  String raceStr() {
    if(race > p.d.s && race > p.c.s)
      return "Race to " + race;
    return "Win by 2";
  }
  
  String capStr() {
    return "Score Cap at " + cap;
  }
  
  void draw() {
    p.draw();
    drawBreak();
  }
  
  void drawBreak() {
    ellipseMode(RADIUS);
    if(defBreak) {
      ellipse(510,105,6,6);
    }
    else {
      ellipse(510,195,6,6);
    }
  }
}
