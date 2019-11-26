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
  
  void nextTurn() {
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
  
  void nextPlayer() {
    curT = 0;
    if(curP < players.length-1)
      curP++;
    else {
      curP = 0;
      curF++;
    }
  }
  
  void previousTurn() {
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
  
  void setCurrScore(int s) {
    players[curP].scores[curF][curT] = s;
  }
  
  int getPreviousScore() {
    if(curT == 0)
      return -1;
    return players[curP].scores[curF][curT-1];
  }
  
  String toString() {
    String result = "";
    for(int i=0;i<players.length;i++) {
      result += players[i].toString() + "\r\n";
    }
    result += "" + curF + "," + curP + "," + curT;
    return result;
  }
}
