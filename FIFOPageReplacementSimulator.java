import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class FIFOPageReplacementSimulator extends JPanel {

    public static Queue<Integer> pageQueue = new LinkedList<>();
    public static Set<Integer> pageSet = new HashSet<>();
    public static ArrayList<Integer> referenceStringValues = new ArrayList<>();
    public static int numFrames = 0;
    public static int time = 0;
    public static int pageFaults = 0;
    public static Timer timer;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 20;
        int y = 50;
        int frameHeight = 50;

        // Draw page frames
        int index = 0;
        for (int page : pageQueue) {
            g.setColor(Color.BLACK);
            g.drawRect(x, y + index * frameHeight, 50, 50);
            g.drawString(String.valueOf(page), x + 20, y + 30 + index * frameHeight);
            index++;
        }

        // Draw page fault counter
        g.setColor(Color.RED);
        g.drawString("Page Faults: " + pageFaults, 20, 30);
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter number of frames: ");
        numFrames = input.nextInt();
        input.nextLine();

        System.out.print("Enter reference string: ");
        String referenceString = input.nextLine();
        String[] tokens = referenceString.split(" ");

        for (String token : tokens) {
            try {
                referenceStringValues.add(Integer.parseInt(token));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number: " + token);
            }
        }

        JFrame frame = new JFrame("FIFO Page Replacement Simulator");
        FIFOPageReplacementSimulator simulator = new FIFOPageReplacementSimulator();
        frame.add(simulator);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        startSimulation(simulator);
    }

    public static void startSimulation(FIFOPageReplacementSimulator simulator) {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (time < referenceStringValues.size()) {
                    int page = referenceStringValues.get(time);
                    if (!pageSet.contains(page)) {
                        pageFaults++;
                        if (pageQueue.size() == numFrames) {
                            int removedPage = pageQueue.poll();
                            pageSet.remove(removedPage);
                        }
                        pageQueue.add(page);
                        pageSet.add(page);
                    }
                    simulator.repaint();
                    time++;
                } else {
                    timer.stop();
                    System.out.println("Simulation Complete! Total Page Faults: " + pageFaults);
                }
            }
        });
        timer.start();
    }
}
