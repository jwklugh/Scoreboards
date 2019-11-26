class Extention {
  int x;
  int y;//while visible
  float curY;
  boolean top;
  boolean visible;
  boolean out;
  float stage; //stage should be min 0 (all the way out) and max frameRate (all the way in)
  final float speed = .5; //speed modifier for pop time : 1 = 1 second
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
  
  void transition() {
    if(out) {
      if(!visible) visible = true;
      if(stage > 0) stage--;
    } else {
      if(stage < fps * speed) stage++;
      else visible = false;
    }
    curY = int(y +
              (top?
               stage*50/(fps * speed)  :
              -stage*50/(fps * speed) ));
  }
  
  void toggle() {
    out = !out;
  }
  
  void draw() {
    transition();
    if(visible) image(img,x,curY);
  }
  
  void draw(String text, int textSize) {
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
