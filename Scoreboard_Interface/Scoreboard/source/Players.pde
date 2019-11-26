class Players {
  Player d;
  Player c;
  
  Players(String defender, int dr, int ds, String challenger, int cr, int cs) {
    d = new Player(defender, dr, ds);
    c = new Player(challenger, cr, cs);
  }
  
  void draw() {
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
  
  String toString() {
    return c.n+","+d.n +","+ c.r+","+d.r +","+ c.s+","+d.s;
    //        names             ranks             scores
  }
}
