class TimeKeeper {
  int time;
  int iniTime;
  int difTime;
  boolean running;
  
  TimeKeeper(int in) {
    iniTime = in;
    time = iniTime;
  }
  
  int getMillis() {
    return time;
  }
  
  int getTime() {
    return time/1000;
  }
  
  void updateTime() {
    if(running)
      time += (millis() - difTime);
    updateDif();
  }
  
  void updateDif() {
    difTime = millis();
  }
  
  void toggle() {
    running = !running;
  }
  
  String toString() {
    return "" + getTime()/60 + ":" + (getTime()%60>9?"":"0") + getTime()%60;
  }
}
