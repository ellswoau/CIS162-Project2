import java.util.Calendar;
import java.util.GregorianCalendar;

public class Controller extends Rental{

    private ControllerTypes controllerType;
    private int numberOfControllers;
    private ConsoleTypes console;

    public Controller(){
    }
    public Controller(String nameOfRenter,
                      GregorianCalendar rentedOn,
                      GregorianCalendar dueBack,
                      GregorianCalendar actualDateReturned,
                      ControllerTypes controllerType,
                      int numberOfControllers,
                      ConsoleTypes console){
        super(nameOfRenter, rentedOn, dueBack, actualDateReturned);
        this.controllerType = controllerType;
        this.numberOfControllers = numberOfControllers;
        this.console = console;
    }

    public ControllerTypes getControllerType(){
        return this.controllerType;
    }
    public void setControllerType(ControllerTypes controllerType){
        this.controllerType = controllerType;
    }

    public int getNumberOfControllers() {
        return numberOfControllers;
    }

    public void setNumberOfControllers(int numberOfControllers) {
        this.numberOfControllers = numberOfControllers;
    }

    public ConsoleTypes getConsole() {
        return console;
    }

    public void setConsole(ConsoleTypes console) {
        this.console = console;
    }

    @Override
    public double getCost(GregorianCalendar dueBack){
        double cost = 0.0;
        if (console != null){
            Console temp = new Console(nameOfRenter, rentedOn, dueBack,actualDateReturned, console);
            cost += temp.getCost(dueBack);
        }
        GregorianCalendar gTemp = new GregorianCalendar();
        cost += 2;

        gTemp = (GregorianCalendar) dueBack.clone();

        for (int days = 0; days < 7; days ++)
            gTemp.add(Calendar.DATE, -1);

        while (gTemp.after(rentedOn)){
            while (gTemp.after(rentedOn)){
                if((this.controllerType ==ControllerTypes.NintendoSwitch)||
                        (this.controllerType == ControllerTypes.PlayStation4)||
                        (this.controllerType == ControllerTypes.XBoxOneS))
                    cost += 0.75;
                if((this.controllerType == ControllerTypes.PlayStation4Wired)||
                        (this.controllerType == ControllerTypes.XBoxOneSWired))
                    cost += 0.5;
                if ((this.controllerType == ControllerTypes.NintendoSwitchPro)||
                        (this.controllerType == ControllerTypes.SegaGenesisMini))
                    cost += 0.25;
                gTemp.add(Calendar.DATE, -1);
            }
        }

        return cost*this.numberOfControllers;
    }

    @Override
    public String toString(){
        return "Controller{" +
                "controllerType='" + controllerType +"\'" +
                ", #Controllers=" + numberOfControllers + "\'" +
                ", console=" + console + super.toString() +
                '}';
    }
}
