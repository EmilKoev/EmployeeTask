import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import model.Project;

public class main {

  private static DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
  private static DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("yyyy MM dd");

  public static void main(String[] args) {
    File file = new File("src/main/resources/input.csv");
    try(
        Scanner scanner = new Scanner(file);
        PrintStream statistics = new PrintStream("src/main/resources/outputStats.txt")
    ){
      Map<String, Project> projects = new HashMap<>();
      while (scanner.hasNextLine()){
        String[] line = scanner.nextLine().split(",");
        if (line.length == 4){
          String empId = line[0].trim();
          String projectId = line[1].trim();
          String startDate = line[2].trim();
          String endDate = line[3].trim();
          if (!projects.containsKey(projectId)){
            projects.put(projectId, new Project(projectId));
          }
          Project project = projects.get(projectId);
          LocalDate start = dateConvertor(startDate);
          LocalDate end = dateConvertor(endDate);
          project.addData(empId, start, end);
        }else {
          statistics.println("something is wrong with this line: " + line);
        }
      }
      for (Project project : projects.values()) {
        statistics.print(project.printStatistics());
        statistics.println();
      }
    }catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

//  static LocalDate dateConvertor(String date){
//    date = date.trim();
//    if (date.equals("NULL")){
//      return LocalDate.now();
//    }else {
//      return LocalDate.parse(date);
//    }
//  }

  static LocalDate dateConvertor(String date){
    date = date.trim();
    if (date.equals("NULL")){
      return LocalDate.now();
    }else {
      if (date.contains("/")){
        return LocalDate.parse(date,formatter1);
      }
      if (date.contains("-")){
        return LocalDate.parse(date,formatter2);
      }
      if (date.contains(" ")){
        return LocalDate.parse(date,formatter3);
      }
    }
    return null;
  }
}