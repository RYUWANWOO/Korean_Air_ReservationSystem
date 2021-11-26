package com.company;

import java.util.Scanner;
import java.sql.*;
import java.util.SortedSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException,SQLException {
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/korean_air?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","Wanwoo3615!");
            Scanner sc = new Scanner(System.in);
            printMainMenu();
            System.out.print(">>> ");
            int menuFlag = sc.nextInt();
            while(true){
                if(menuFlag == 1){ //항공편 기간별 조회
                    Statement statement = connection.createStatement();
                    String my_sql = SearchFlight();
                    ResultSet resultSet = statement.executeQuery(my_sql);

                    while(resultSet.next()){
                        System.out.println("Departure_Date: "+resultSet.getString(1)+" Flight_num: "+
                                resultSet.getString(2)+" Departure_Airport: "+resultSet.getString(4)+"("+resultSet.getString(3)+")"+
                                " Arrival_Airport: "+resultSet.getString(6)+"("+resultSet.getString(5)+")"+" Departure_Time: "+
                                resultSet.getString(7)+ " Gate: "+resultSet.getString(8));
                    }

                }
                else if(menuFlag ==2){ // 예약현황 조회
                    Statement statement = connection.createStatement();
                    String my_sql = ReservationStatus();
                    ResultSet resultSet = statement.executeQuery(my_sql);

                    while(resultSet.next()){
                        System.out.println("Booking ID: " + resultSet.getString(1) +
                                " SKYPASS_ID: "+ resultSet.getString(3)+
                                " Flight_num: "+resultSet.getString(8)+
                                " Passenger_name: "+resultSet.getString(4)+
                                " Seat_num: "+resultSet.getString(7));
                    }

                }
                else if(menuFlag==3){ // 수화물 관련 조회
                    Statement statement = connection.createStatement();
                    String my_sql = LuggageSearch();
                    ResultSet resultSet = statement.executeQuery(my_sql);

                    while(resultSet.next()){
                        System.out.println("Booking ID: " + resultSet.getString(1) +
                                " Ticket_num: "+resultSet.getString(2) +
                                " SKYPASS ID: "+resultSet.getString(3) +
                                " Passenger_name: "+resultSet.getString(4) +
                                " Sex: "+resultSet.getString(5) +
                                " Passport_ID: "+resultSet.getString(6) +
                                " Seat_num: "+resultSet.getString(7) +
                                " Flight_num: "+resultSet.getString(8) +
                                " Departure: "+resultSet.getString(9) +
                                " Arrivals: "+resultSet.getString(10) +
                                " Departure_date: "+resultSet.getString(11)+
                                " Status: "+resultSet.getString(12));
                    }

                }
                else if(menuFlag==4){
                    System.out.println("프로그램을 종료합니다.");
                    break;
                }
                else{
                    System.out.println("잘못된 입력입니다. 다시 입력해 주십시오.");
                    printMainMenu();
                    System.out.print(">>> ");
                    menuFlag = sc.nextInt();
                    continue;
                }
                printMainMenu();
                System.out.print(">>> ");
                menuFlag = sc.nextInt();
            }
        }
        catch (SQLException sqlException){
            System.out.println("SQLException: "+sqlException);
        }

        // write your code here
    }


    public static void printMainMenu(){
        System.out.println("--------------------------------------");
        System.out.println("| \tKOREAN AIR RESERVATION SYSTEM\t |");
        System.out.println("--------------------------------------");
        System.out.println("| 1. 항공편 기간별 조회              |");
        System.out.println("| 2. 예약 현황 조회                  |");
        System.out.println("| 3. 수화물 관련 조회                |");
        System.out.println("| 4. 종료                            |");
        System.out.println("--------------------------------------");
    }
    public static String SearchFlight(){
        Scanner searchScanner = new Scanner(System.in);
        System.out.println("<< 항공편 조회 >>");
        System.out.print("출발 날짜1 을 입력 하세요: ");
        String date1 = searchScanner.nextLine();
        System.out.print("출발 날짜2 를 입력 하세요: ");
        String date2 = searchScanner.nextLine();
        System.out.print("출발지를 입력하세요: ");
        String departure = searchScanner.nextLine();
        System.out.print("도착지를 입력하세요: ");
        String arrivals = searchScanner.nextLine();

        return "Select * from FLIGHT where Departure_date >=\"" + date1 + "\" and Departure_date <=\"" + date2 +
                "\" and (Departure, Arrivals) in (select Departure,Arrivals from FLIGHT where Departure = \""
                + departure +"\" and Arrivals=\""+arrivals+"\")";

    }

    public static String ReservationStatus(){
        Scanner reservationScanner = new Scanner(System.in);
        System.out.println("<< 예약현황 조회 >>");
        System.out.print("예약번호를 입력하세요: ");
        String reservation_num = reservationScanner.nextLine();
        return "Select * from RESERVATION_STATUS Where Booking_ID = \""+reservation_num+"\"";

    }

    public static String LuggageSearch(){
        System.out.println("<< 수화물이 두개 이상인 탑승객의 예약현황을 조회합니다.>>");
        return "SELECT * FROM RESERVATION_STATUS where SKYPASS_ID in (select SKYPASS_ID from LUGGAGE group by SKYPASS_ID having count(Luggage_code)>=2)";

    }








}
