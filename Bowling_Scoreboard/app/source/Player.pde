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
  
  boolean setScore(int frame, int turn, int score) {
    if(turn == 0 || scores[frame][0] + score <= 10) {
      scores[frame][turn] = score;
      return true;
    }
    return false;
  }
  
  /**
   * Determines if a frame is valid for 
   */
  boolean isFrameValid(int frame) {
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
  
  boolean isFrameScoreValid(int frame) {
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
  
  int getScoreToFrame(int frame) {
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
  
  String toString() {
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
