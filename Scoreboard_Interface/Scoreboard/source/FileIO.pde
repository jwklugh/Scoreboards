
import java.io.FileReader;

class FileIO {
  
  BufferedReader reader;
  String outFile;
  
  FileIO(String fileName) throws IOException {
    reader = createReader(fileName);
  }
  
  void setOut(String fileName) {
    outFile = fileName;
  }
  
  InfoStruct getInFile() throws IOException{
    setOut(reader.readLine());
    return getInfo();
  }
  
  // REQUIRED INFORMATION
  // Challenger Name, Challenger Rank,
  // Defeneder Name, Defender Rank,
  // Number to Race to, Score Cap, 
  // Time Limit, Current Time, and
  // Out File
  
  InfoStruct getInfo() throws IOException{
    
    String[] in = reader.readLine().split(",");
    
    return new InfoStruct(
    in[0],in[1],           //CName, DName
    parseInt(in[2]),       //CRank
    parseInt(in[3]),       //DRank
    parseInt(in[4]),       //CScore
    parseInt(in[5]),       //DScore
    parseInt(in[6]),       //Race
    parseInt(in[7]),       //Cap
    parseInt(in[8]),       //Limit
    parseInt(in[9]),       //Curr
    parseBoolean(in[10])); //DBreak //<>//
  }
  
  boolean save(String state) {
    if(outFile != null) {
      PrintWriter writer = createWriter(outFile);
      writer.append(state);
      writer.flush();
      writer.close();
      return true;
    }
    return false;
  }
}
