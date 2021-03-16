
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;
import static org.junit.Assert.*;

public class ListModelTest {

    @Test
    public void testUpdateScreenSort7DaysGamesFirst() {
        ListModel testList = new ListModel();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        GregorianCalendar g1 = new GregorianCalendar();
        GregorianCalendar g2 = new GregorianCalendar();
        GregorianCalendar g3 = new GregorianCalendar();
        GregorianCalendar g4 = new GregorianCalendar();
        GregorianCalendar g5 = new GregorianCalendar();

        try {
            Date d1 = df.parse("03/15/2021");
            g1.setTime(d1);
            Date d5 = df.parse("03/16/2021");
            g5.setTime(d5);
            Date d2 = df.parse("03/17/2021");
            g2.setTime(d2);
            Date d3 = df.parse("03/21/2021");
            g3.setTime(d3);
            Date d4 = df.parse("03/28/2021");
            g4.setTime(d4);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //within week
        Game game1001 = new Game("Austin", g1, g2,
                null, "Call Of Duty",
                ConsoleTypes.PlayStation4);
        Game game1002 = new Game("Kit", g1, g2,
                null, "Fortnite",
                ConsoleTypes.XBoxOneS);
        Console console1001 = new Console("Zach", g1, g5,
                null, ConsoleTypes.PlayStation4);
        Console console1002 = new Console("Aazad", g1, g2,
                null, ConsoleTypes.NintendoSwitch);
        Controller controller1001 = new Controller("Fabio",
                g2, g3, null,
                ControllerTypes.PlayStation4Wired, 3,
                ConsoleTypes.PlayStation4);

        //not within week
        Game game1003 = new Game("Jack", g1, g4,
                null, "Smash Bros Melee",
                ConsoleTypes.PlayStation4);

        //returned
        Controller controller1002 = new Controller("Betsy",
                g2, g3, g4, ControllerTypes.PlayStation4Wired,
                3, ConsoleTypes.PlayStation4);

        testList.clearAllRentals();
        testList.add(game1001);
        testList.add(game1002);
        testList.add(game1003);
        testList.add(console1001);
        testList.add(console1002);
        testList.add(controller1001);
        testList.add(controller1002);


        testList.setDisplay(ScreenDisplay.DueWithinWeekGamesFirst);

        //"Austin" comes before "Aazad" because games are listed first in screen
        assertEquals("Austin", testList.getValueAt(0,0));
        assertEquals("Aazad", testList.getValueAt(2,0));
        assertEquals("Fabio", testList.getValueAt(3,0));
    }

    @Test
    public void testUpdateScreenEverythingScn() {
        ListModel testList = new ListModel();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        GregorianCalendar g1 = new GregorianCalendar();
        GregorianCalendar g2 = new GregorianCalendar();
        GregorianCalendar g3 = new GregorianCalendar();
        GregorianCalendar g4 = new GregorianCalendar();
        GregorianCalendar g5 = new GregorianCalendar();

        try {
            Date d1 = df.parse("03/15/2021");
            g1.setTime(d1);
            Date d5 = df.parse("03/16/2021");
            g5.setTime(d5);
            Date d2 = df.parse("03/17/2021");
            g2.setTime(d2);
            Date d3 = df.parse("03/21/2021");
            g3.setTime(d3);
            Date d4 = df.parse("03/28/2021");
            g4.setTime(d4);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //within week
        Game game1001 = new Game("Austin", g1, g2,
                null, "Call Of Duty",
                ConsoleTypes.PlayStation4);
        Game game1002 = new Game("Kit", g1, g2,
                null, "Fortnite",
                ConsoleTypes.XBoxOneS);
        Console console1001 = new Console("Zach", g1, g5,
                null, ConsoleTypes.PlayStation4);
        Console console1002 = new Console("Aazad", g1, g2,
                null, ConsoleTypes.NintendoSwitch);
        Controller controller1001 = new Controller("Fabio",
                g2, g3, null,
                ControllerTypes.PlayStation4Wired,
                3, ConsoleTypes.PlayStation4);

        //not within week
        Game game1003 = new Game("Jack", g1, g4,
                null, "Smash Bros Melee",
                ConsoleTypes.PlayStation4);

        //returned
        Controller controller1002 = new Controller("Betsy",
                g2, g3, g4, ControllerTypes.PlayStation4Wired,
                3, ConsoleTypes.PlayStation4);

        testList.clearAllRentals();
        testList.add(game1001);
        testList.add(game1002);
        testList.add(game1003);
        testList.add(console1001);
        testList.add(console1002);
        testList.add(controller1001);
        testList.add(controller1002);

        testList.setDisplay(ScreenDisplay.EverythingScn);

        //Every rental should be sorted alphabetically
        assertEquals("Aazad", testList.getValueAt(0,0));
        assertEquals("Austin", testList.getValueAt(1,0));
        assertEquals("Betsy", testList.getValueAt(2,0));
        assertEquals("Fabio", testList.getValueAt(3,0));
        assertEquals("Jack", testList.getValueAt(4,0));
        assertEquals("Kit", testList.getValueAt(5,0));
        assertEquals("Zach", testList.getValueAt(6,0));
    }

    @Test
    public void testUpdateScreenCap14Days() {
        ListModel testList = new ListModel();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        GregorianCalendar g1 = new GregorianCalendar();
        GregorianCalendar g2 = new GregorianCalendar();
        GregorianCalendar g3 = new GregorianCalendar();
        GregorianCalendar g4 = new GregorianCalendar();
        GregorianCalendar g5 = new GregorianCalendar();
        GregorianCalendar g6 = new GregorianCalendar();


        try {
            Date d1 = df.parse("03/15/2021");
            g1.setTime(d1);
            Date d5 = df.parse("03/16/2021");
            g5.setTime(d5);
            Date d2 = df.parse("03/17/2021");
            g2.setTime(d2);
            Date d3 = df.parse("03/21/2021");
            g3.setTime(d3);
            Date d4 = df.parse("03/28/2021");
            g4.setTime(d4);
            Date d6 = df.parse("04/15/2021");
            g6.setTime(d6);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //within week
        Game game1001 = new Game("Austin", g1, g2,
                null, "Call Of Duty",
                ConsoleTypes.PlayStation4);
        Game game1002 = new Game("Kit", g1, g2,
                null, "Fortnite",
                ConsoleTypes.XBoxOneS);
        Console console1001 = new Console("Zach", g1, g5,
                null, ConsoleTypes.PlayStation4);
        Console console1002 = new Console("Aazad", g1, g2,
                null, ConsoleTypes.NintendoSwitch);
        Controller controller1001 = new Controller("Fabio",
                g2, g3, null,
                ControllerTypes.PlayStation4Wired, 3,
                ConsoleTypes.PlayStation4);

        //not within week
        Game game1003 = new Game("Jack", g1, g4,
                null, "Smash Bros Melee",
                ConsoleTypes.PlayStation4);

        //over 14 days
        Controller controller1003 = new Controller("Roger",
                g1, g6, null,
                ControllerTypes.PlayStation4Wired, 2,
                null);
        Game game1004 = new Game("Greg", g2, g6,
                null, "Tetris",
                ConsoleTypes.XBoxOneS);


        //returned
        Controller controller1002 = new Controller("Betsy",
                g2, g3, g4, ControllerTypes.PlayStation4Wired,
                3, ConsoleTypes.PlayStation4);

        testList.clearAllRentals();
        testList.add(game1001);
        testList.add(game1002);
        testList.add(game1003);
        testList.add(game1004);
        testList.add(console1001);
        testList.add(console1002);
        testList.add(controller1001);
        testList.add(controller1002);
        testList.add(controller1003);


        testList.setDisplay(ScreenDisplay.Cap14DaysOverdue);

        //"Jack" is last and uncapitalized because it does not have
        //equal to or greater than 14 days between rented on date and
        //due date
        assertEquals("GREG", testList.getValueAt(0,0));
        assertEquals("ROGER", testList.getValueAt(1,0));
        assertEquals("Jack", testList.getValueAt(2,0));
    }

    @Test
    public void testLoadFromText() {
        ListModel testList = new ListModel();
        testList.loadFromText("src/demo");

        testList.setDisplay(ScreenDisplay.EverythingScn);

        //All elements from src/demo text file are sorted alphabetically
        //ten elements total
        assertEquals("Adam", testList.getValueAt(0,0));
        assertEquals("Ashley", testList.getValueAt(1,0));
        assertEquals("Austin", testList.getValueAt(2,0));
        assertEquals("Jacob", testList.getValueAt(3,0));
        assertEquals("Jason", testList.getValueAt(4,0));
        assertEquals("Kara", testList.getValueAt(5,0));
        assertEquals("Ryan", testList.getValueAt(6,0));
        assertEquals("Sally", testList.getValueAt(7,0));
        assertEquals("Tara", testList.getValueAt(8,0));
        assertEquals("Todd", testList.getValueAt(9,0));

    }


}