public class InfoServer {
    public InfoServer() {
        // Constructor
    }
    public String getTemp(String city){
        int value = (int) (Math.random() * 100);
        return "The temperature in " + city + " is " + value + " degrees.";
    }
    public String getRoadInfo(String roadId){
        boolean isOpen = Math.random() > 0.5;
        return "Road " + roadId + " is " + (isOpen ? "open" : "closed") + ".";
    }
    public String getUCLWinner(int year){
        if(year < 1955) {
            return "In that time was no UCL.";
        }
        String[] teams = {"Real Madrid", "FC Barcelona", "FCSB", "PSG", "Manchester City", "Bayern Munich", "Liverpool", "Chelsea", "Borussia Dortmund", "Inter Milan", "AC Milan", "Juventus", "Ajax", "Porto", "Benfica", "Manchester United", "Arsenal", "Tottenham Hotspur", "Atletico Madrid"};
        int index = (int) (Math.random() * teams.length);
        return "The winner of the UCL in " + year + " is " + teams[index] + ".";
    }
}
