import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class ListModel extends AbstractTableModel {

    /** holds all the rentals */
    private ArrayList<Rental> listOfRentals;

    /** holds only the rentals that are to be displayed */
    private ArrayList<Rental> filteredListRentals, filteredListRentalsUpperCaps;

    /** current screen being displayed */
    private ScreenDisplay display = ScreenDisplay.CurrentRentalStatus;

    /** String arrays used to store names of columns */
    private String[] columnNamesCurrentRentals = {"Renter\'s Name", "Est. Cost",
            "Rented On", "Due Date ", "Console", "Name of the Game"};
    private String[] columnNamesReturned = {"Renter\'s Name", "Rented On Date",
            "Due Date", "Actual date returned ", "Est. Cost", " Real Cost"};
    private String[] columnNamesEverythingScn = {"Renter\'s Name", "Rented On Date",
            "Due Date", "Actual date returned ", "Est. Cost", " Real Cost",
            "Console", "Name of the Game"};

    private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    public ListModel() {
        display = ScreenDisplay.CurrentRentalStatus;
        listOfRentals = new ArrayList<>();
        filteredListRentals = new ArrayList<>();
        //TODO: delete below before turning in
        //not currently using the below object, delete later if I dont change
        filteredListRentalsUpperCaps = new ArrayList<>();
        updateScreen();
        createList();
    }

    public void setDisplay(ScreenDisplay selected) {
        display = selected;
        updateScreen();
    }

    /*****************************************************************
     * Method that updates screen using a switch and different cases
     * for each different action listener/menu screen selection
     *****************************************************************/
    private void updateScreen() {
        GregorianCalendar calendarToday = new GregorianCalendar();
        Date todayDate = new Date();
        calendarToday.setTime(todayDate);
        switch (display) {
            case CurrentRentalStatus:
                filteredListRentals = (ArrayList<Rental>) listOfRentals.stream()
                        .filter(n -> n.actualDateReturned == null)
                        .map(n -> {
                            n.setNameOfRenter(n.getNameOfRenter().substring(0, 1).toUpperCase()
                                    + n.getNameOfRenter().substring(1).toLowerCase());
                            return n;
                        })
                        .collect(Collectors.toList());

                Collections.sort(filteredListRentals, (n1, n2)
                        -> n1.nameOfRenter.compareTo(n2.nameOfRenter));
                break;

            case ReturnedItems:
                //screen that shows all items that have been returned
                filteredListRentals = (ArrayList<Rental>) listOfRentals.stream()
                        .filter(n -> n.actualDateReturned != null)
                        .map(n -> {
                            n.setNameOfRenter(n.getNameOfRenter().substring(0, 1).toUpperCase()
                                    + n.getNameOfRenter().substring(1).toLowerCase());
                            return n;
                        })
                        .collect(Collectors.toList());

                // Note: This uses an anonymous class.
                Collections.sort(filteredListRentals, new Comparator<Rental>() {
                    @Override
                    public int compare(Rental n1, Rental n2) {
                        return n1.nameOfRenter.compareTo(n2.nameOfRenter);
                    }
                });
                break;

            case DueWithInWeek:
                //this screen shows all items that are not yet returned
                //and they have less than 7 days between Rented on and
                //Due back date.
                filteredListRentals = (ArrayList<Rental>) listOfRentals.stream()
                        .filter( n -> (daysBetween(n.getRentedOn(),
                                n.getDueBack()) <= 7) &&
                                n.actualDateReturned == null)
                        .map(n -> {
                            n.setNameOfRenter(n.getNameOfRenter().substring(0, 1).toUpperCase()
                                    + n.getNameOfRenter().substring(1).toLowerCase());
                            return n;
                        })
                        .collect(Collectors.toList());

                // Note: This uses Lambda function
                Collections.sort(filteredListRentals, (n1, n2)
                        -> n1.nameOfRenter.compareTo(n2.nameOfRenter));
                break;

            case DueWithinWeekGamesFirst:
                // This screen is the same as the one above but it also
                //sorts with Rental units of type Game at the top
                filteredListRentals = (ArrayList<Rental>) listOfRentals.stream()

                        .filter( n -> (daysBetween(n.getRentedOn(),
                                n.getDueBack()) <= 7)
                                && n.actualDateReturned == null)
                        .map(n -> {
                            n.setNameOfRenter(n.getNameOfRenter().substring(0, 1).toUpperCase()
                                    + n.getNameOfRenter().substring(1).toLowerCase());
                            return n;
                        })
                        .collect(Collectors.toList());

                //sort by name of renter first
                Collections.sort(filteredListRentals, (n1, n2)
                        -> n1.nameOfRenter.compareTo(n2.nameOfRenter));

                // Sort using an anonymous class comparator which makes
                // rentals that are a Game object listed first
                Collections.sort(filteredListRentals, new Comparator<Rental>() {
                    @Override
                    public int compare(Rental o1, Rental o2) {
                        if (o1 instanceof Game) {
                            if (o2 instanceof Game) {
                                return 0;
                            }
                            else {
                                return -1;
                            }
                        }
                        else {
                            if (o2 instanceof Game) {
                                return 1;
                            }
                            else {
                                return 0;
                            }
                        }
                    }
                });
                break;

            case Cap14DaysOverdue:

                //first filter stream to only have rentals with greater
                // than 14 day difference between rentedOn date
                //and dueback date. Then set the name of the renter to uppercase
                filteredListRentals = (ArrayList<Rental>) listOfRentals.stream()
                        .filter( n -> (daysBetween(n.getRentedOn(),
                                n.getDueBack()) >= 14))
                        .map(n -> {
                            n.setNameOfRenter(n.getNameOfRenter().toUpperCase());
                            return n;
                        })
                        .collect(Collectors.toList());

                //stream again, this time filtering for all elements
                // greater than a 7 day difference
                filteredListRentals = (ArrayList<Rental>) listOfRentals.stream()
                        .filter(n -> (daysBetween(n.getRentedOn(),
                                n.getDueBack()) >= 7)
                                && n.actualDateReturned == null)
                        .collect(Collectors.toList());

                //sort by name first
                Collections.sort(filteredListRentals, (n1, n2)
                        -> n1.nameOfRenter.compareTo(n2.nameOfRenter));

                //sort by whether or not the rental has more than 14 days
                // between due date and rented on date
                Collections.sort(filteredListRentals, new Comparator<Rental>() {
                    @Override
                    public int compare(Rental o1, Rental o2) {
                        if (daysBetween(o1.getRentedOn(),
                                o1.getDueBack()) >= 14) {
                            if (daysBetween(o2.getRentedOn(),
                                    o2.getDueBack()) >= 14) {
                                return 0;
                            }
                            else {
                                return -1;
                            }
                        }
                        else {
                            if (daysBetween(o2.getRentedOn(),
                                    o2.getDueBack()) >= 14) {
                                return 1;
                            }
                            else {
                                return 0;
                            }
                        }
                    }
                });
                break;

            case EverythingScn:
                //screen shows everything in listOfRentals w/ no filter
                filteredListRentals = (ArrayList<Rental>) listOfRentals.stream()
                        .map(n -> {
                            n.setNameOfRenter(n.getNameOfRenter().substring(0, 1).toUpperCase()
                                    + n.getNameOfRenter().substring(1).toLowerCase());
                            return n;
                        })
                        .collect(Collectors.toList());

                Collections.sort(filteredListRentals, (n1, n2)
                        -> n1.nameOfRenter.compareTo(n2.nameOfRenter));
                break;

            default:
                throw new RuntimeException("upDate is in undefined state: " + display);
        }
        fireTableStructureChanged();
    }

    /*****************************************************************
      Private helper method to count the number of days between two
      GregorianCalendar dates
      Note that this is the proper way to do this; trying to use other
      classes/methods likely won't properly account for leap days
      @param startDate - the beginning/starting day
      @param endDate - the last/ending day
      @return int for the number of days between startDate and endDate
     *****************************************************************/
    private int daysBetween(GregorianCalendar startDate, GregorianCalendar endDate) {
        // Determine how many days the Game was rented out
        GregorianCalendar gTemp = new GregorianCalendar();
        gTemp = (GregorianCalendar) endDate.clone(); //  gTemp = dueBack;  does not work!!
        int daysBetween = 0;
        while (gTemp.compareTo(startDate) > 0) {
            gTemp.add(Calendar.DATE, -1);                // this subtracts one day from gTemp
            daysBetween++;
        }

        return daysBetween;
    }

    /*****************************************************************
     Method gets the name of each column used for labels at the top of
     the results table
     @param col the column number to get name for, starting at 0 from
     the left
     @return String the corresponding name of the column
     *****************************************************************/
    @Override
    public String getColumnName(int col) {
        switch (display) {
            case CurrentRentalStatus:
                return columnNamesCurrentRentals[col];
            case ReturnedItems:
                return columnNamesReturned[col];
            case DueWithInWeek:
                return columnNamesCurrentRentals[col];
            case Cap14DaysOverdue:
                return columnNamesCurrentRentals[col];
            case EverythingScn:
                return columnNamesEverythingScn[col];
            case DueWithinWeekGamesFirst:
                return columnNamesCurrentRentals[col];

        }
        throw new RuntimeException("Undefined state for Col Names: " + display);
    }

    /*****************************************************************
     Method gets number of columns for table in screen from the length
     of the string array
     @return number of columns to be used for table
     *****************************************************************/
    @Override
    public int getColumnCount() {
        switch (display) {
            case CurrentRentalStatus:
                return columnNamesCurrentRentals.length;
            case ReturnedItems:
                return columnNamesReturned.length;
            case DueWithInWeek:
                return columnNamesCurrentRentals.length;
            case Cap14DaysOverdue:
                return columnNamesCurrentRentals.length;
            case EverythingScn:
                return columnNamesEverythingScn.length;
            case DueWithinWeekGamesFirst:
                return columnNamesCurrentRentals.length;
        }
        throw new IllegalArgumentException();
    }

    /*****************************************************************
     Method gets number of rows for table based on number of items
     in array returned by updateScreen streams
     @return number of rows for table
     *****************************************************************/
    @Override
    public int getRowCount() {
        return filteredListRentals.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        switch (display) {
            case CurrentRentalStatus:
                return currentRentScreen(row, col);
            case ReturnedItems:
                return rentedOutScreen(row, col);
            case DueWithInWeek:
                return currentRentScreen(row, col);
            case Cap14DaysOverdue:
                return currentRentScreen(row, col);
            case EverythingScn:
                return EverythingScn(row, col);
            case DueWithinWeekGamesFirst:
                return currentRentScreen(row, col);


        }
        throw new IllegalArgumentException();
    }

    private Object EverythingScn(int row, int col) {
        switch (col) {
            case 0:
                return (filteredListRentals.get(row).nameOfRenter);
            case 1:
                return (formatter.format(filteredListRentals.get(row).rentedOn.getTime()));

            case 2:
                if (filteredListRentals.get(row).dueBack == null)
                    return "-";

                return (formatter.format(filteredListRentals.get(row).dueBack.getTime()));

            case 3:
                if (filteredListRentals.get(row).actualDateReturned == null) {
                    return "Not Returned";
                }
                else {
                    return (formatter.format(filteredListRentals.get(row).
                            actualDateReturned.getTime()));
                }

            case 4:
                return (filteredListRentals.get(row).getCost(filteredListRentals.
                        get(row).dueBack));

            case 5:
                if (filteredListRentals.get(row).actualDateReturned == null) {
                    return "Not Returned";
                }
                else {
                    return (filteredListRentals.
                            get(row).getCost(filteredListRentals.get(row).
                            actualDateReturned));
                }

            case 6:
                if (filteredListRentals.get(row) instanceof Console)
                    return (((Console) filteredListRentals.get(row)).getConsoleType());
                else {
                    if (filteredListRentals.get(row) instanceof Game)
                        if (((Game) filteredListRentals.get(row)).getConsole() != null)
                            return ((Game) filteredListRentals.get(row)).getConsole();
                        else
                            return "";
                }

            case 7:
                if (filteredListRentals.get(row) instanceof Game)
                    return (((Game) filteredListRentals.get(row)).getNameGame());
                else
                    return "";
            default:
                throw new RuntimeException("Row,col out of range: " + row + " " + col);
        }
    }

    private Object currentRentScreen(int row, int col) {
        switch (col) {
            case 0:
                return (filteredListRentals.get(row).nameOfRenter);

            case 1:
                return (filteredListRentals.get(row).getCost(filteredListRentals.
                        get(row).dueBack));

            case 2:
                return (formatter.format(filteredListRentals.get(row).rentedOn.getTime()));

            case 3:
                if (filteredListRentals.get(row).dueBack == null)
                    return "-";

                return (formatter.format(filteredListRentals.get(row).dueBack.getTime()));

            case 4:
                if (filteredListRentals.get(row) instanceof Console)
                    return (((Console) filteredListRentals.get(row)).getConsoleType());
                else {
                    if (filteredListRentals.get(row) instanceof Game)
                        if (((Game) filteredListRentals.get(row)).getConsole() != null)
                            return ((Game) filteredListRentals.get(row)).getConsole();
                        else
                            return "";
                }

            case 5:
                if (filteredListRentals.get(row) instanceof Game)
                    return (((Game) filteredListRentals.get(row)).getNameGame());
                else
                    return "";
            default:
                throw new RuntimeException("Row,col out of range: " + row + " " + col);
        }
    }


    private Object rentedOutScreen(int row, int col) {
        switch (col) {
            case 0:
                return (filteredListRentals.get(row).nameOfRenter);

            case 1:
                return (formatter.format(filteredListRentals.get(row).rentedOn.
                        getTime()));
            case 2:
                return (formatter.format(filteredListRentals.get(row).dueBack.
                        getTime()));
            case 3:
                return (formatter.format(filteredListRentals.get(row).
                        actualDateReturned.getTime()));

            case 4:
                return (filteredListRentals.
                        get(row).getCost(filteredListRentals.get(row).dueBack));

            case 5:
                return (filteredListRentals.
                        get(row).getCost(filteredListRentals.get(row).
                        actualDateReturned
                ));

            default:
                throw new RuntimeException("Row,col out of range: " + row + " " + col);
        }
    }

    public void add(Rental a) {
        listOfRentals.add(a);
        updateScreen();
    }

    public Rental get(int i) {
        return filteredListRentals.get(i);
    }

    public void update(int index, Rental unit) {
        updateScreen();
    }

    public void saveDatabase(String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            System.out.println(listOfRentals.toString());
            os.writeObject(listOfRentals);
            os.close();
        } catch (IOException ex) {
            throw new RuntimeException("Saving problem! " + display);
        }
    }

    public void loadDatabase(String filename) {
        listOfRentals.clear();

        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream is = new ObjectInputStream(fis);

            listOfRentals = (ArrayList<Rental>) is.readObject();
            updateScreen();
            is.close();
        } catch (Exception ex) {
            throw new RuntimeException("Loading problem: " + display);

        }
    }

    public boolean saveAsText(String filename) {
        if (filename.equals("")) {
            throw new IllegalArgumentException();
        }

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new FileWriter(filename)));
            out.println(listOfRentals.size());
            for (int i = 0; i < listOfRentals.size(); i++) {
                Rental unit = listOfRentals.get(i);
                out.println(unit.getClass().getName());
                out.println("Name is " + unit.getNameOfRenter());
                out.println("Rented on " + formatter.format(unit.rentedOn.getTime()));
                out.println("DueDate " + formatter.format(unit.dueBack.getTime()));

                if (unit.getActualDateReturned() == null)
                    out.println("Not returned!");
                else
                    out.println(formatter.format(unit.actualDateReturned.getTime()));

                if (unit instanceof Game) {
                    out.println(((Game) unit).getNameGame());
                    if (((Game) unit).getConsole() != null)
                        out.println(((Game) unit).getConsole());
                    else
                        out.println("No Console");
                }

                if (unit instanceof Console)
                    out.println(((Console) unit).getConsoleType());
            }
            out.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public void loadFromText(String filename) {
        if(filename == null)
            throw new IllegalArgumentException();
        listOfRentals.clear();

        Scanner in = null;
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try{
           in = new Scanner(new File(filename));
           int size = Integer.parseInt(in.nextLine());

           for (int i = 0; i < size; i++) {
               String className = in.nextLine();

               String name = in.nextLine();

               name = name.substring(name.indexOf("Name is ") + 8);

               String s = in.nextLine();
               s = s.substring(s.indexOf("Rented on ") + 10);
               Date rentedDate = df.parse(s);
               GregorianCalendar rented = new GregorianCalendar();
               rented.setTime(rentedDate);

               s = in.nextLine();
               s = s.substring(s.indexOf("DueDate ") + 8);
               Date dueDate = df.parse(s);
               GregorianCalendar due = new GregorianCalendar();
               due.setTime(dueDate);

               s = in.nextLine();
               GregorianCalendar returned = new GregorianCalendar();
               if (s.equals("Not returned!"))
                   returned = null;
               else {
                   Date returnDate = df.parse(s);
                   returned.setTime(returnDate);
               }

               if (className.equals("Game")) {
                   String gameName = in.nextLine();
                   s = in.nextLine();
                   ConsoleTypes console;
                   if (s.equals("No Console"))
                       console = null;
                   else if (s.equals("PlayStation4"))
                       console = ConsoleTypes.PlayStation4;
                   else if (s.equals("XBoxOneS"))
                       console = ConsoleTypes.XBoxOneS;
                   else if (s.equals("PlayStation4Pro"))
                       console = ConsoleTypes.PlayStation4Pro;
                   else if (s.equals("NintendoSwitch"))
                       console = ConsoleTypes.NintendoSwitch;
                   else if (s.equals("SegaGenesisMini"))
                       console = ConsoleTypes.SegaGenesisMini;
                   else
                       throw new IllegalArgumentException();
                   listOfRentals.add(new Game(name, rented, due, returned, gameName, console));
               }
               else if (className.equals("Console")) {
                   s = in.nextLine();
                   ConsoleTypes console;
                   if (s.equals("No Console"))
                       console = null;
                   else if (s.equals("PlayStation4"))
                       console = ConsoleTypes.PlayStation4;
                   else if (s.equals("XBoxOneS"))
                       console = ConsoleTypes.XBoxOneS;
                   else if (s.equals("PlayStation4Pro"))
                       console = ConsoleTypes.PlayStation4Pro;
                   else if (s.equals("NintendoSwitch"))
                       console = ConsoleTypes.NintendoSwitch;
                   else if (s.equals("SegaGenesisMini"))
                       console = ConsoleTypes.SegaGenesisMini;
                   else
                       throw new IllegalArgumentException();
                   listOfRentals.add(new Console(name, rented, due, returned, console));
               }
               else
                   throw new IllegalArgumentException();
           }
        }
        catch(FileNotFoundException | ParseException e){
            throw new IllegalArgumentException();
        }


        updateScreen();
    }

    /**********************************************************************
     *
     *  DO NOT MODIFY THIS METHOD!!!!!!
     */
    public void createList() {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        GregorianCalendar g1 = new GregorianCalendar();
        GregorianCalendar g2 = new GregorianCalendar();
        GregorianCalendar g3 = new GregorianCalendar();
        GregorianCalendar g4 = new GregorianCalendar();
        GregorianCalendar g5 = new GregorianCalendar();
        GregorianCalendar g6 = new GregorianCalendar();
        GregorianCalendar g7 = new GregorianCalendar();
        GregorianCalendar g8 = new GregorianCalendar();

        try {
            Date d1 = df.parse("1/20/2020");
            g1.setTime(d1);
            Date d2 = df.parse("12/22/2020");
            g2.setTime(d2);
            Date d3 = df.parse("12/20/2019");
            g3.setTime(d3);
            Date d4 = df.parse("7/02/2020");
            g4.setTime(d4);
            Date d5 = df.parse("1/20/2010");
            g5.setTime(d5);
            Date d6 = df.parse("9/29/2020");
            g6.setTime(d6);
            Date d7 = df.parse("7/25/2020");
            g7.setTime(d7);
            Date d8 = df.parse("7/29/2020");
            g8.setTime(d8);

            Console console1 = new Console("Person1", g4, g6, null, ConsoleTypes.PlayStation4);
            Console console2 = new Console("Person2", g5, g3, null, ConsoleTypes.PlayStation4);
            Console console3 = new Console("Person5", g4, g8, null, ConsoleTypes.SegaGenesisMini);
            Console console4 = new Console("Person6", g4, g7, null, ConsoleTypes.SegaGenesisMini);
            Console console5 = new Console("Person1", g5, g4, g3, ConsoleTypes.XBoxOneS);

            Game game1 = new Game("Person1", g3, g2, null, "title1", ConsoleTypes.PlayStation4);
            Game game2 = new Game("Person1", g3, g1, null, "title2", ConsoleTypes.PlayStation4);
            Game game3 = new Game("Person1", g5, g3, null, "title2", ConsoleTypes.SegaGenesisMini);
            Game game4 = new Game("Person7", g4, g8, null, "title2", null);
            Game game5 = new Game("Person3", g3, g1, g1, "title2", ConsoleTypes.XBoxOneS);
            Game game6 = new Game("Person6", g4, g7, null, "title1", ConsoleTypes.NintendoSwitch);
            Game game7 = new Game("Person5", g4, g8, null, "title1", ConsoleTypes.NintendoSwitch);

            add(game1);
            add(game4);
            add(game5);
            add(game2);
            add(game3);
            add(game6);
            add(game7);

            add(console1);
            add(console2);
            add(console5);
            add(console3);
            add(console4);

            // create a bunch of them.
            int count = 0;
            Random rand = new Random(13);
            String guest = null;

            while (count < 0) {  // change this number to 300 for a complete test of your code
                Date date = df.parse("7/" + (rand.nextInt(10) + 2) + "/2020");
                GregorianCalendar g = new GregorianCalendar();
                g.setTime(date);
                if (rand.nextBoolean()) {
                    guest = "Game" + rand.nextInt(5);
                    Game game;
                    if (count % 2 == 0)
                        game = new Game(guest, g4, g, null, "title2", ConsoleTypes.NintendoSwitch);
                    else
                        game = new Game(guest, g4, g, null, "title2", null);
                    add(game);


                } else {
                    guest = "Console" + rand.nextInt(5);
                    date = df.parse("7/" + (rand.nextInt(20) + 2) + "/2020");
                    g.setTime(date);
                    Console console = new Console(guest, g4, g, null, getOneRandom(rand));
                    add(console);
                }

                count++;
            }
        } catch (ParseException e) {
            throw new RuntimeException("Error in testing, creation of list");
        }
    }

    public ConsoleTypes getOneRandom(Random rand) {

        int number = rand.nextInt(ConsoleTypes.values().length - 1);
        switch (number) {
            case 0:
                return ConsoleTypes.PlayStation4;
            case 1:
                return ConsoleTypes.XBoxOneS;
            case 2:
                return ConsoleTypes.PlayStation4Pro;
            case 3:
                return ConsoleTypes.NintendoSwitch;
            default:
                return ConsoleTypes.SegaGenesisMini;
        }
    }
}