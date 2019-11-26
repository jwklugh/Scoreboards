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
  
  String loadGame() throws IOException{
    BufferedReader reader = createReader(loadFile);
    String game = "";
    String line = reader.readLine();
    while(line != null) {
      game += line + "\n";
      line = reader.readLine();
    }
    return game;
  }
  
  String[] loadPlayers(int numPlayers) {
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
  
  int countPlayers() throws IOException {
    BufferedReader reader = createReader(playersFile);
    int count = 0;
    while(reader.readLine() != null) {
      count++;
    }
    return count;
  }
  
  void saveGame() {
    PrintWriter writer = createWriter(saveFile);
    writer.print(g.toString());
    writer.flush();
    writer.close();
  }
}
