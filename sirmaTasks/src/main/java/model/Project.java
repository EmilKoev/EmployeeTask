package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Project {

  private static final String END_DATE = "endDate";
  private static final String START_DATE = "startDate";

  private String id;
  private Map<String, Map<String, LocalDate>> log;

  public Project(String id){
    this.setId(id);
    this.log = new HashMap<>();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Map<String, Map<String, LocalDate>> getLog() {
    return getCopyOfTheLog();
  }

  public void setLog(Map<String, Map<String, LocalDate>> log) {
    this.log = log;
  }

  //adding a data for the time of work of some employee in the project
  public void addData(String empId, LocalDate startDate, LocalDate endDate){
    if (!this.log.containsKey(empId)){
      log.put(empId,new HashMap<>());
    }
    log.get(empId).put(START_DATE, startDate);
    log.get(empId).put(END_DATE, endDate);
  }

  //printing statistics of the joint work of all pairs of employees
  public String printStatistics(){
    Map<String, Map<String, LocalDate>> copiedLog = getCopyOfTheLog();
    StringBuffer statistics = new StringBuffer("Project ").append(this.id).append(" statistic");
    for (Entry<String, Map<String, LocalDate>> e: log.entrySet()) {
      copiedLog.remove(e.getKey());
      if (copiedLog.isEmpty()){
        continue;
      }
      for (Entry<String, Map<String, LocalDate>> e2: copiedLog.entrySet()) {
        if (e.getValue().get(START_DATE).isBefore(e2.getValue().get(START_DATE))){
          if (e.getValue().get(END_DATE).isAfter(e2.getValue().get(START_DATE))){
            if (e.getValue().get(END_DATE).isBefore(e2.getValue().get(END_DATE))){
              long daysOfWorkTogether = ChronoUnit.DAYS.between(e2.getValue().get(START_DATE), e.getValue().get(END_DATE));
              statistics.append(prepareMessage(e.getKey(),e2.getKey(),daysOfWorkTogether, this.id));
            }else {
              long daysOfWorkTogether = ChronoUnit.DAYS.between(e2.getValue().get(START_DATE), e2.getValue().get(END_DATE));
              statistics.append(prepareMessage(e.getKey(),e2.getKey(),daysOfWorkTogether, this.id));
            }
          }
        }else {
          if (e2.getValue().get(END_DATE).isAfter(e.getValue().get(START_DATE))){
            if (e2.getValue().get(END_DATE).isBefore(e.getValue().get(END_DATE))){
              long daysOfWorkTogether = ChronoUnit.DAYS.between(e.getValue().get(START_DATE), e2.getValue().get(END_DATE));
              statistics.append(prepareMessage(e.getKey(),e2.getKey(),daysOfWorkTogether, this.id));
            }else {
              long daysOfWorkTogether = ChronoUnit.DAYS.between(e.getValue().get(START_DATE), e.getValue().get(END_DATE));
              statistics.append(prepareMessage(e.getKey(),e2.getKey(),daysOfWorkTogether, this.id));
            }
          }
        }
      }
    }
    return statistics.toString();
  }

  //returning a copy of the log
  private Map<String, Map<String, LocalDate>> getCopyOfTheLog() {
    Map<String, Map<String,LocalDate>> copiedLog = new HashMap<>();
    for (Entry<String, Map<String, LocalDate>> entry: log.entrySet()) {
      copiedLog.put(entry.getKey() ,new HashMap<String, LocalDate>());
      for (Entry<String, LocalDate> entry2: entry.getValue().entrySet()) {
        copiedLog.get(entry.getKey()).put(entry2.getKey(),entry2.getValue());
      }
    }
    return copiedLog;
  }

  //prepare a message for joint work of two employees
  private static String prepareMessage(String emp1, String emp2, long daysOfWorkTogether, String projectId){
    StringBuilder statistics = new StringBuilder("\n");
    statistics.append("employees - ").append(emp1).append(" and ").append(emp2).append(" have ")
        .append(daysOfWorkTogether).append(" days of work together in project ").append(projectId);
    return statistics.toString();
  }
}
