import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

class ListModelTest {

    @org.junit.jupiter.api.Test
    public void testUpdateScreen() {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        GregorianCalendar g1 = new GregorianCalendar();
        GregorianCalendar g2 = new GregorianCalendar();
        GregorianCalendar g3 = new GregorianCalendar();
        GregorianCalendar g4 = new GregorianCalendar();

        try {
            Date d1 = df.parse("03/15/2021");
            g1.setTime(d1);
            Date d2 = df.parse("03/17/2021");
            g2.setTime(d2);
            Date d3 = df.parse("03/21/2021");
            g3.setTime(d3);
            Date d4 = df.parse("03/28/2021");
            g4.setTime(d4);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Game game1001 = new Game("Austin", g1, g2, null, "Call Of Duty", ConsoleTypes.PlayStation4);
        Console console1001 = new Console("Fabio", g1, g2, null, ConsoleTypes.PlayStation4);

        Game game1002 = new Game("Kit", g1, g2, null, "Fortnite", ConsoleTypes.PlayStation4);

        add(game1001);
        add(game1002);
        add(console1001);

        setDisplay(ScreenDisplay.DueWithinWeekGamesFirst);

        //going to use a loop to make sure the row of game 1001 is greater than the
        //row of console1001 in this screen
    }

    @org.junit.jupiter.api.Test
    void setDisplay(ScreenDisplay screen) {
    }

    @org.junit.jupiter.api.Test
    void getValueAt() {
    }

    @org.junit.jupiter.api.Test
    void add(Console console) {
    }

    void add(Game game) {
    }

    @org.junit.jupiter.api.Test
    void get() {
    }
}