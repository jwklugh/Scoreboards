class KeyPresses {
  
  void button(char b) {
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
