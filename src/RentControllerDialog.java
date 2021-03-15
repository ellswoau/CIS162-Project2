import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RentControllerDialog extends JDialog implements ActionListener {
    private JTextField txtRenterName;
    private JTextField txtRentedOn;
    private JTextField txtDueDate;
    private JComboBox<ControllerTypes> comBoxControllerType;
    private JTextField txtControllerNumber;
    private JComboBox<ConsoleTypes> comBoxConsoleType;

    private JButton okButton;
    private JButton cancelButton;
    private int closeStatus;
    private Controller controller;
    public static final int OK = 0;
    public static final int CANCEL = 1;

    public RentControllerDialog(JFrame parent, Controller controller){
        super(parent, true);
        this.controller = controller;

        setTitle("Controller dialog box");
        closeStatus = CANCEL;
        setSize(500,200);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        txtRenterName = new JTextField("Judy", 30);
        txtRentedOn = new JTextField(15);
        txtDueDate = new JTextField(15);
        comBoxControllerType = new JComboBox(ControllerTypes.values());
        txtControllerNumber = new JTextField("1" ,10);
        comBoxConsoleType = new JComboBox<>(ConsoleTypes.values());

        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String dateNow = formatter.format(currentDate.getTime());
        currentDate.add(Calendar.DATE, 1);
        String dateTomorrow = formatter.format(currentDate.getTime());

        txtRentedOn.setText(dateNow);
        txtDueDate.setText(dateTomorrow);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(7, 2));

        textPanel.add(new JLabel(""));
        textPanel.add(new JLabel(""));

        textPanel.add(new JLabel("Name of Renter: "));
        textPanel.add(txtRenterName);
        textPanel.add(new JLabel("Date rented on: "));
        textPanel.add(txtRentedOn);
        textPanel.add(new JLabel("Due date (est.): "));
        textPanel.add(txtDueDate);
        textPanel.add(new JLabel("Controller Type"));
        textPanel.add(comBoxControllerType);
        textPanel.add(new JLabel("Number of Controllers: "));
        textPanel.add(txtControllerNumber);
        textPanel.add(new JLabel("Console Type"));
        textPanel.add(comBoxConsoleType);

        getContentPane().add(textPanel, BorderLayout.CENTER);

        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();

        if (button == okButton){
            closeStatus = OK;
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

            Date d1 = null;
            Date d2 = null;
            try {
                GregorianCalendar gregTemp = new GregorianCalendar();
                d1 = df.parse(txtRentedOn.getText());
                gregTemp.setTime(d1);
                controller.setRentedOn(gregTemp);

                gregTemp = new GregorianCalendar();
                d2 = df.parse(txtDueDate.getText());
                gregTemp.setTime(d2);
                controller.setDueBack(gregTemp);
            } catch (ParseException e1){

            }

                int temp;
                temp = Integer.parseInt(txtControllerNumber.getText());
                controller.setNumberOfControllers(temp);

                controller.setNameOfRenter(txtRenterName.getText());

                if((ControllerTypes) comBoxControllerType.getSelectedItem() == ControllerTypes.NoSelection){
                    JOptionPane.showMessageDialog(null, "Select Controller.");
                    closeStatus = CANCEL;
                }
                controller.setControllerType((ControllerTypes) comBoxControllerType.getSelectedItem());

                controller.setConsole((ConsoleTypes) comBoxConsoleType.getSelectedItem());
                System.out.println(controller.toString());
        }

        dispose();
    }

    public int getCloseStatus(){return closeStatus;}
}
