

//Default values in case of errors
//number of players
int p = 2;
//frame size
int s = 100;
String settingsFile = "settings.txt";

Game g;
KeyPresses k;
GameFileIO io;
String[] playerNames;

boolean showUnplayedFrames = false;

void settings() {
  try {
    io = new GameFileIO(settingsFile);
    p = io.playerCount;
    s = io.frameSize;
  } catch (Exception e) {}
  size(s*11,s*p+s/3);
}

void setup() {
  frameRate(2);
  try {
    g = new Game(io.loadGame());
    playerNames = io.loadPlayers(p);
  } catch (Exception e) {
    System.out.print("Exception occured, loading default start up.");
    g = new Game(p);
    playerNames = io.loadPlayers(p);
  }  
  k = new KeyPresses();
}

void draw() {
  
  drawFrames();
  drawScores();
  drawTotals();
  drawIDs();
  drawFrameNumbers();
}

void drawFrames() {
  rectMode(CORNER);
  stroke(0);
  fill(255);
  for(int y=0; y<p; y++) { // y is current player being drawn
    rect(0,s*y+s/3,s/2,s); // player ID box
    for(int i=0; i<9; i++) { //draw all boxes but the 10th
      rect(s/2+s*i,s*y+s/3,s,s); // full boxes
      rect(s+s*i,s*y+s/3,s/2,s/2); // half boxes
    }
    rect(s*9.5 ,s*y+s/3,s*1.5,s); // full 10th box
    rect(s*10  ,s*y+s/3,s/2,s/2); // first half 10th box
    rect(s*10.5,s*y+s/3,s/2,s/2); // second half 10th box
  }
}

void drawFrameNumbers() {
  textSize(s/4);
  for(int f=1; f<=9; f++) { //frames 1-9
    text(f,s*f,s/4);
  }
  text(10,s*10 + s/4, s/4); //frame 10
}

void drawIDs() {
  for(int y=0; y<p; y++) { // for each player
    textSize(s/3-s/17*(playerNames[y].length()-1));
    text(playerNames[y], s/4, 19*s/20 + y*s);
  }
}

void drawScores() {
  textSize(s/3);
  textAlign(CENTER);
  stroke(0);
  fill(0);
  for(int y=0; y<p; y++) { // for each player
    for(int f=0; f<9; f++) {    // for each frame (exlcude 10th)
      for(int t=0; t<2; t++) {  // for each turn
        int score = g.players[y].scores[f][t];
        if(score > -1 || showUnplayedFrames) {
          if(t==0) 
            //display an "X" for a score >= 10, and score otherwise
            text(score<10 ? ""+score : "X", 3*s/4 + s*t/2 + s*f, 2*s/3 + s*y);
          if(t==1){
            int pScore = g.players[y].scores[f][t-1];
            //display an "/" for a frame score >= 10, and score otherwise
            text(score+pScore<10 ? ""+score : "/", 3*s/4 + s*t/2 + s*f, 2*s/3 + s*y);
          }
        }
      }
    }
    for(int t=0; t<3; t++) {    // for each turn in the 10th frame
      int score = g.players[y].scores[9][t];
      if(score > -1 || showUnplayedFrames) {
        if(t==0) { // for turn 1
          //display an "X" for a score >= 10, and score otherwise
          text(score<10 ? ""+score : "X", 3*s/4 + s*t/2 + s*9, 2*s/3 + s*y);
        }
        if(t==1) { // for turn 2
          int score0 = g.players[y].scores[9][0];
          if(score0==10) { // if the first turn was a strike
            //display an "X" for a score >= 10, and score otherwise
            text(score<10 ? ""+score : "X", 3*s/4 + s*t/2 + s*9, 2*s/3 + s*y);
          } else { // if the first turn was not a strike
            //display an "/" for a turn 1+2 score >= 10, and score otherwise
            text(score+score0<10 ? ""+score : "/", 3*s/4 + s*t/2 + s*9, 2*s/3 + s*y);
          }
        }
        if(t==2) { // for turn 3
          int score0 = g.players[y].scores[9][0];
          int score1 = g.players[y].scores[9][1];
          if(score0==10) { // if the first turn was a strike
            if(score1==10) { // if the second turn was a strike
              //display an "X" for a score >= 10, and score otherwise
              text(score<10 ? ""+score : "X", 3*s/4 + s*t/2 + s*9, 2*s/3 + s*y);
            } else {
              //display an "/" for a turn 2+3 score >= 10, and score otherwise
              text(score+score1<10 ? ""+score : "/", 3*s/4 + s*t/2 + s*9, 2*s/3 + s*y);
            }
          } if(score0+score1==10) {
            //display an "X" for a score >= 10, and score otherwise
            text(score<10 ? ""+score : "X", 3*s/4 + s*t/2 + s*9, 2*s/3 + s*y);
          }
        }
      }
    }
  }
}

void drawTotals() {
  textSize(s/3);
  textAlign(CENTER);
  stroke(0);
  fill(0);
  for(int y=0; y<p; y++) {
    for(int f=1; f<=9; f++) { //frames 1-9
      int score = g.players[y].getScoreToFrame(f);
      if(score > -1 || showUnplayedFrames)
        text(score,s*f, 5*s/4 + s*y);
    }
    int score = g.players[y].getScoreToFrame(10);
    if(score > -1 || showUnplayedFrames)
      text(score,s*10 + s/4, 5*s/4 + s*y); //frame 10
  }
}












//Button Interface

int b1=0, 
    b2=0, 
    b3=0, 
    b4=0, 
    b5=0, 
    b6=0, 
    b7=0, 
    b8=0, 
    b9=0, 
    b0=0, 
    ret=0,
    bksp=0,
    bx=0,
    slas=0;
    
void keyPressed() {
  switch(key) {
    case '1' : if(b1==0) { b1++; k.button(key); } break;
    case '2' : if(b2==0) { b2++; k.button(key); } break;
    case '3' : if(b3==0) { b3++; k.button(key); } break;
    case '4' : if(b4==0) { b4++; k.button(key); } break;
    case '5' : if(b5==0) { b5++; k.button(key); } break;
    case '6' : if(b6==0) { b6++; k.button(key); } break;
    case '7' : if(b7==0) { b7++; k.button(key); } break;
    case '8' : if(b8==0) { b8++; k.button(key); } break;
    case '9' : if(b9==0) { b9++; k.button(key); } break;
    case '0' : if(b0==0) { b0++; k.button(key); } break;
    case '\n' :if(ret==0)  { ret++;  k.button(key); } break;
    case '\b' :if(bksp==0) { bksp++; k.button(key); } break;
    case 'x' : if(bx==0)   { bx++;   k.button(key); } break;
    case '/' : if(slas==0) { slas++; k.button(key); } break;
  }
}

void keyReleased() {
  switch(key) {
    case '1' : b1=0; break;
    case '2' : b2=0; break;
    case '3' : b3=0; break;
    case '4' : b4=0; break;
    case '5' : b5=0; break;
    case '6' : b6=0; break;
    case '7' : b7=0; break;
    case '8' : b8=0; break;
    case '9' : b9=0; break;
    case '0' : b0=0; break;
    case '\n': ret=0;  break;
    case '\b': bksp=0; break;
    case 'x' : bx=0;   break;
    case '/' : slas=0; break;
  }
}
