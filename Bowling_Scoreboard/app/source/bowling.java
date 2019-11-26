import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class bowling extends PApplet {



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

public void settings() {
  try {
    io = new GameFileIO(settingsFile);
    p = io.playerCount;
    s = io.frameSize;
  } catch (Exception e) {}
  size(s*11,s*p+s/3);
}

public void setup() {
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

public void draw() {
  
  drawFrames();
  drawScores();
  drawTotals();
  drawIDs();
  drawFrameNumbers();
}

public void drawFrames() {
  rectMode(CORNER);
  stroke(0);
  fill(255);
  for(int y=0; y<p; y++) { // y is current player being drawn
    rect(0,s*y+s/3,s/2,s); // player ID box
    for(int i=0; i<9; i++) { //draw all boxes but the 10th
      rect(s/2+s*i,s*y+s/3,s,s); // full boxes
      rect(s+s*i,s*y+s/3,s/2,s/2); // half boxes
    }
    rect(s*9.5f ,s*y+s/3,s*1.5f,s); // full 10th box
    rect(s*10  ,s*y+s/3,s/2,s/2); // first half 10th box
    rect(s*10.5f,s*y+s/3,s/2,s/2); // second half 10th box
  }
}

public void drawFrameNumbers() {
  textSize(s/4);
  for(int f=1; f<=9; f++) { //frames 1-9
    text(f,s*f,s/4);
  }
  text(10,s*10 + s/4, s/4); //frame 10
}

public void drawIDs() {
  for(int y=0; y<p; y++) { // for each player
    textSize(s/3-s/17*(playerNames[y].length()-1));
    text(playerNames[y], s/4, 19*s/20 + y*s);
  }
}

public void drawScores() {
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

public void drawTotals() {
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
    
public void keyPressed() {
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

public void keyReleased() {
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
class Game {
  
  int curT;
  int curP;
  int curF;
  
  Player[] players;

  Game(int numPlayers) {
    players = new Player[numPlayers]; // create an array of the num of desired players
    for(int y=0; y<numPlayers; y++) {
      players[y] = new Player(); // intantiate each player
    }
    curT = 0;
    curP = 0;
    curF = 0;
  }
  
  Game(String input) {
    String[] playerInputs = input.split("\n");
    players = new Player[playerInputs.length-1];
    for(int y=0; y<players.length; y++) {
      players[y] = new Player(playerInputs[y]);
    }
    String[] gamePlace = playerInputs[playerInputs.length-1].split(",");
    curF = parseInt(gamePlace[0]);
    curP = parseInt(gamePlace[1]);
    curT = parseInt(gamePlace[2]);
  }
  
  public void nextTurn() {
    if(curF<9) {
      if(curT == 0) 
        if(players[curP].scores[curF][0] >= 10) {
          nextPlayer();
        } else {
          curT++;
        }
      else {
        nextPlayer();
      }
    } else {
      if(curT == 0) {
        curT++;
      } else if(curT == 1) {
        if(players[curP].scores[curF][0]+players[curP].scores[curF][1] >= 10) {
          curT++;
        } else {
          nextPlayer();
        }
      } else {
        nextPlayer();
      }
    }
  }
  
  public void nextPlayer() {
    curT = 0;
    if(curP < players.length-1)
      curP++;
    else {
      curP = 0;
      curF++;
    }
  }
  
  public void previousTurn() {
    if(curT != 0) // If the previous turn is the same player
      curT--;
    else {
      if(curF < 9) { // If it is before the tenth frame
        curT = 1;
        if(curP == 0) { // If it is the first player
          curP = players.length-1;
          curF--;
        }
        else
          curP--;
      } else if (curF == 9) { // If it IS the tenth frame
        if(curP == 0){ // If it is the first player
          curT = 1;
          curP = players.length-1;
          curF--;
        } else {
          if(players[curP-1].scores[9][2]!=-1) { // If the previous player bowled a bonus
            curT = 2;
          } else { //if the previous player did not bowl a bouns
            curT = 1;
          }
          curP--;
        }
      } else { // If it is the last score of the game
        curP = players.length-1;
        curF--;
        // set turn to bonus bowl if it was done
        curT = players[curP].scores[curF][2] == -1 ? 1 : 2;
      }
    }
  }
  
  public void setCurrScore(int s) {
    players[curP].scores[curF][curT] = s;
  }
  
  public int getPreviousScore() {
    if(curT == 0)
      return -1;
    return players[curP].scores[curF][curT-1];
  }
  
  public String toString() {
    String result = "";
    for(int i=0;i<players.length;i++) {
      result += players[i].toString() + "\r\n";
    }
    result += "" + curF + "," + curP + "," + curT;
    return result;
  }
}
class GameFileIO {
  
  String loadFile;
  String saveFile;
  String playersFile;
  int frameSize;
  int playerCount;
  
  GameFileIO(String settingsFileName) throws IOException {
    BufferedReader reader = createReader(settingsFileName);
    loadFile = reader.readLine();
    saveFile = reader.readLine();
    playersFile = reader.readLine();
    frameSize = parseInt(reader.readLine());
    playerCount = countPlayers();
  }
  
  public String loadGame() throws IOException{
    BufferedReader reader = createReader(loadFile);
    String game = "";
    String line = reader.readLine();
    while(line != null) {
      game += line + "\n";
      line = reader.readLine();
    }
    return game;
  }
  
  public String[] loadPlayers(int numPlayers) {
    String[] result = new String[numPlayers];
    for(int i=0; i<numPlayers; i++) {
      result[i] = ""+(i+1);
    }
    try {
      BufferedReader reader = createReader(playersFile);
      for(int i=0; i<numPlayers; i++) {
        result[i] = reader.readLine().toUpperCase();
        if(result[i].length() > 3)
          result[i] = result[i].substring(0,3);
      }
    } catch (Exception e) {}
    return result;
  }
  
  public int countPlayers() throws IOException {
    BufferedReader reader = createReader(playersFile);
    int count = 0;
    while(reader.readLine() != null) {
      count++;
    }
    return count;
  }
  
  public void saveGame() {
    PrintWriter writer = createWriter(saveFile);
    writer.print(g.toString());
    writer.flush();
    writer.close();
  }
}
class KeyPresses {
  
  public void button(char b) {
    if(b == '\b') {
      if(!(g.curP==0 && g.curF==0 && g.curT==0)) {
        g.previousTurn();
        if(g.players[g.curP].scores[g.curF][g.curT] == -1 && g.curF < 9)
          g.previousTurn();
        g.setCurrScore(-1);
      }
    }
    else if(g.curF < 10) {
      switch(b) {
        case '1' : g.setCurrScore(1); break;
        case '2' : g.setCurrScore(2); break;
        case '3' : g.setCurrScore(3); break;
        case '4' : g.setCurrScore(4); break;
        case '5' : g.setCurrScore(5); break;
        case '6' : g.setCurrScore(6); break;
        case '7' : g.setCurrScore(7); break;
        case '8' : g.setCurrScore(8); break;
        case '9' : g.setCurrScore(9); break;
        case '0' : g.setCurrScore(0); break;
        case 'x' : g.setCurrScore(10);break;
        case '/' : s=10-g.getPreviousScore(); break;
        case '\n': 
          if(g.curT==0){
            g.setCurrScore(10); 
          }
          else {
            if(g.curF==9){
              if(g.curT==1) {
                if(g.getPreviousScore()==10) {
                  g.setCurrScore(10);
                } else {
                  g.setCurrScore(10-g.getPreviousScore());
                }
              } else {
                if(g.players[g.curP].scores[g.curF][0] == 10) {
                  if(g.getPreviousScore()==10) {
                    g.setCurrScore(10);
                  } else {
                    g.setCurrScore(10-g.getPreviousScore());
                  }
                } else {
                  g.setCurrScore(10);
                }
              }
            } else
            g.setCurrScore(10-g.getPreviousScore());
          }
          break;
      }
      boolean invalid = !g.players[g.curP].isFrameScoreValid(g.curF);
      g.nextTurn();
      if(invalid) {
        g.previousTurn();
        g.setCurrScore(-1);
      }
    }
    //io.saveGame();
  }
}
class Player{
  int[][] scores;
  
  Player() {
    scores = new int[10][2];
    scores[9] = new int[3];
    for(int i=0; i<10; i++) {
      for(int k=0; k<2; k++) {
        scores[i][k] = -1;
      }
    }
    scores[9][2] = -1;
  }
  
  Player(String input) {
    scores = new int[10][2];
    scores[9] = new int[3];
    int curChar = 0; //<>//
    for(int i=0;i<scores.length;i++) {
      for(int j=0;j<scores[i].length;j++) {
        switch(""+input.charAt(curChar)) {
          case "-" : scores[i][j] = -1; break;
          case "X" : scores[i][j] = 10; break;
          default  : scores[i][j] = parseInt(""+input.charAt(curChar));
        }
        curChar++;
      }
    }
  }
  
  public boolean setScore(int frame, int turn, int score) {
    if(turn == 0 || scores[frame][0] + score <= 10) {
      scores[frame][turn] = score;
      return true;
    }
    return false;
  }
  
  /**
   * Determines if a frame is valid for 
   */
  public boolean isFrameValid(int frame) {
    int f = frame-1;
    if(f<8)
      return (scores[f][0] == 10)
              ? ((scores[f+1][0] == 10)
                   ? (scores[f+2][0] > -1)
                   : (scores[f+1][1] > -1)
                )
              : ((scores[f][0] + scores[f][1] == 10)
                   ? (scores[f+1][0] > -1)
                   : (scores[ f ][1] > -1)
                );
     if(f==8)
       return (scores[8][0] == 10)
               ? (scores[9][1] > -1)
               : ((scores[8][0] + scores[8][1] == 10)
                   ? (scores[9][0] > -1)
                   : (scores[8][1] > -1)
                 );
     if(f==9)
       return (scores[9][0] == 10 || scores[9][0] + scores[9][1] == 10)
               ? (scores[9][2] > -1)
               : (scores[9][1] > -1);
     return false;
  }
  
  public boolean isFrameScoreValid(int frame) {
    if(frame < 9) {
      return (scores[frame][0] + scores[frame][1] <= 10);
    } else {
      if(scores[9][0] == 10) {
        if(scores[9][1] == 10) {
          return scores[9][2] <= 10;
        } else {
          return scores[9][1] + scores[9][2] <= 10;
        }
      } else {
        return scores[9][0] + scores[9][1] <= 10;
      }
    } 
  }
  
  public int getScoreToFrame(int frame) {
    if(!isFrameValid(frame))
      return -1;
    int total = 0;
    for(int i=0; i<frame; i++) {
      total += getScoreFrame(i);
    }
    return total;
  }
  
  private int getScoreFrame(int frame) {
    int f = frame;
    if(f<8)
      return (scores[f][0] == 10)
              ? 10 + ((scores[f+1][0] == 10)
                   ? (10 + scores[f+2][0])
                   : (scores[f+1][0] + scores[f+1][1])
                )
              : ((scores[f][0] + scores[f][1] == 10)
                   ? (10 + scores[f+1][0])
                   : (scores[f][0] + scores[f][1])
                );
     if(f==8)
       return (scores[8][0] == 10)
               ? (10 + scores[9][0] + scores[9][1])
               : ((scores[8][0] + scores[8][1] == 10)
                   ? (10 + scores[9][0])
                   : (scores[8][0] + scores[8][1])
                 );
     if(f==9)
       return (scores[9][0] == 10 || scores[9][0] + scores[9][1] == 10)
               ? (scores[9][0] + scores[9][1] + scores[9][2])
               : (scores[9][0] + scores[9][1]);
     return -1;
  }
  
  public String toString() {
    String result = "";
    for(int i=0;i<scores.length;i++) {
      for(int j=0;j<scores[i].length;j++) {
        switch(scores[i][j]) {
          case -1 : result += "-"; break;
          case 10 : result += "X"; break;
          default : result += ""+scores[i][j];
        }
      }
    }
    
    return result;
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "bowling" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
