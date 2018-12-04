import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Main {

    private Graph g = new Graph();
    public Main() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height * 2 / 3;
        int width = screenSize.width * 2 / 3;

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // frame.setPreferredSize(new Dimension(500, 500));
        JTextField path = new JTextField(10);
        JButton enter = new JButton("enter");
        JButton displayLabels = new JButton("displayLabels");
        displayLabels.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                g.showLabels();
            }
        });
        displayLabels.setVisible(false);
        JTextField f = new JTextField(10);
        f.setVisible(false);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel labelPath = new JLabel("Enter Path: ");

        constraints.gridx = 0;
        constraints.gridy = 0;
        p.add(labelPath, constraints);

        constraints.gridx = 1;
        p.add(path,constraints);
        p.add(f,constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        p.add(enter,constraints);
        p.add(displayLabels,constraints);
        frame.add(p);

        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File fichier = new File(path.getText());
                if (fichier.exists()) {
                    f.setVisible(true);
                    path.setVisible(false);
                    displayLabels.setVisible(true);
                    enter.setVisible(false);
                    labelPath.setText("enter text:");
                    AutoSuggestor autoSuggestor = new AutoSuggestor(f, frame, null, Color.WHITE.brighter(), Color.BLUE, Color.RED, 1f, path.getText(),g) ;

                }
            }
        });
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
          @Override
         public void run() {
            new Main();
         }
        });
    }
}

class AutoSuggestor {

    private final JTextField textField;
    private final Window container;
    private JPanel suggestionsPanel;
    private JWindow autoSuggestionPopUpWindow;
    private String typedWord;
    private final ArrayList<String> dictionary = new ArrayList<>();
    private int currentIndexOfSpace, tW, tH;
    private final Graph g ;
    private DocumentListener documentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent de) {
            checkForAndShowSuggestions();
        }

        @Override
        public void removeUpdate(DocumentEvent de) {
            checkForAndShowSuggestions();
        }

        @Override
        public void changedUpdate(DocumentEvent de) {
            checkForAndShowSuggestions();
        }
    };
    private final Color suggestionsTextColor;
    private final Color suggestionFocusedColor;

    public AutoSuggestor(JTextField textField, Window mainWindow, ArrayList<String> words, Color popUpBackground, Color textColor, Color suggestionFocusedColor, float opacity, String File,Graph g) {
        this.textField = textField;
        this.suggestionsTextColor = textColor;
        this.container = mainWindow;
        this.suggestionFocusedColor = suggestionFocusedColor;
        this.g = g;
        this.textField.getDocument().addDocumentListener(documentListener);
        this.textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                g.addUsed(textField.getText().trim());
                textField.setText("");
            }
        });

        g.readFromFile(File);
        setDictionary(words);

        typedWord = "";
        currentIndexOfSpace = 0;
        tW = 0;
        tH = 0;

        autoSuggestionPopUpWindow = new JWindow(mainWindow);
        autoSuggestionPopUpWindow.setOpacity(opacity);

        suggestionsPanel = new JPanel();
        suggestionsPanel.setLayout(new GridLayout(0, 1));
        suggestionsPanel.setBackground(popUpBackground);


    }



    private void setFocusToTextField() {
        container.toFront();
        container.requestFocusInWindow();
        textField.requestFocusInWindow();
    }

    public ArrayList<SuggestionLabel> getAddedSuggestionLabels() {
        ArrayList<SuggestionLabel> sls = new ArrayList<>();
        for (int i = 0; i < suggestionsPanel.getComponentCount(); i++) {
            if (suggestionsPanel.getComponent(i) instanceof SuggestionLabel) {
                SuggestionLabel sl = (SuggestionLabel) suggestionsPanel.getComponent(i);
                sls.add(sl);
            }
        }
        return sls;
    }

    private void checkForAndShowSuggestions() {
        typedWord = getCurrentlyTypedWord();

        suggestionsPanel.removeAll();//remove previos words/jlabels that were added

        //used to calcualte size of JWindow as new Jlabels are added
        tW = 0;
        tH = 0;
        setDictionary(g.displayFiniteState(typedWord,false));

        if (dictionary.isEmpty()) {
            if (autoSuggestionPopUpWindow.isVisible()) {
                autoSuggestionPopUpWindow.setVisible(false);
            }
        } else {
            fillSuggestionBox();
            showPopUpWindow();
            setFocusToTextField();

        }
    }

    protected void addWordToSuggestions(String word) {
        SuggestionLabel suggestionLabel = new SuggestionLabel(word, suggestionFocusedColor, suggestionsTextColor, this);

        calculatePopUpWindowSize(suggestionLabel);

        suggestionsPanel.add(suggestionLabel);
    }

    public String getCurrentlyTypedWord() {//get newest word after last white spaceif any or the first word if no white spaces
        String text = textField.getText();
        String wordBeingTyped = "";
        if (text.contains(" ")) {
            int tmp = text.lastIndexOf(" ");
            if (tmp >= currentIndexOfSpace) {
                currentIndexOfSpace = tmp;
                wordBeingTyped = text.substring(text.lastIndexOf(" "));
            }
        } else {
            wordBeingTyped = text;
        }
        return wordBeingTyped.trim();
    }

    private void calculatePopUpWindowSize(JLabel label) {
        //so we can size the JWindow correctly
        if (tW < label.getPreferredSize().width) {
            tW = label.getPreferredSize().width;
        }
        tH += label.getPreferredSize().height;
    }

    private void showPopUpWindow() {
        autoSuggestionPopUpWindow.getContentPane().add(suggestionsPanel);
        autoSuggestionPopUpWindow.setMinimumSize(new Dimension(textField.getWidth(), 30));
        autoSuggestionPopUpWindow.setSize(tW, tH);
        autoSuggestionPopUpWindow.setVisible(true);

        int windowX = 200;
        int windowY = 200;

        windowX = container.getX() + textField.getX() + 5;
        if (suggestionsPanel.getHeight() > autoSuggestionPopUpWindow.getMinimumSize().height) {
            windowY = container.getY() + textField.getY() + textField.getHeight() + autoSuggestionPopUpWindow.getMinimumSize().height;
        } else {
            windowY = container.getY() + textField.getY() + textField.getHeight() + autoSuggestionPopUpWindow.getHeight();
        }

        autoSuggestionPopUpWindow.setLocation(windowX, windowY);
        autoSuggestionPopUpWindow.setMinimumSize(new Dimension(textField.getWidth(), 30));
        autoSuggestionPopUpWindow.revalidate();
        autoSuggestionPopUpWindow.repaint();

    }

    public void setDictionary(ArrayList<String> words) {
        dictionary.clear();
        if (words == null) {
            return;//so we can call constructor with null value for dictionary without exception thrown
        }
        for (String word : words) {
            dictionary.add(word);
        }
    }

    public JWindow getAutoSuggestionPopUpWindow() {
        return autoSuggestionPopUpWindow;
    }

    public Window getContainer() {
        return container;
    }

    public JTextField getTextField() {
        return textField;
    }

    public void addToDictionary(String word) {
        dictionary.add(word);
    }

    void fillSuggestionBox() {

        for (String word : dictionary) {
                addWordToSuggestions(word);
            }
    }
}

class SuggestionLabel extends JLabel {

    private boolean focused = false;
    private final JWindow autoSuggestionsPopUpWindow;
    private final JTextField textField;
    private final AutoSuggestor autoSuggestor;
    private Color suggestionsTextColor, suggestionBorderColor;

    public SuggestionLabel(String string, final Color borderColor, Color suggestionsTextColor, AutoSuggestor autoSuggestor) {
        super(string);

        this.suggestionsTextColor = suggestionsTextColor;
        this.autoSuggestor = autoSuggestor;
        this.textField = autoSuggestor.getTextField();
        this.suggestionBorderColor = borderColor;
        this.autoSuggestionsPopUpWindow = autoSuggestor.getAutoSuggestionPopUpWindow();

        initComponent();
    }

    private void initComponent() {
        setFocusable(true);
        setForeground(suggestionsTextColor);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);

                replaceWithSuggestedText();

                autoSuggestionsPopUpWindow.setVisible(false);
            }
        });

        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "Enter released");
        getActionMap().put("Enter released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                replaceWithSuggestedText();
                autoSuggestionsPopUpWindow.setVisible(false);
            }
        });
    }

    public void setFocused(boolean focused) {
        if (focused) {
            setBorder(new LineBorder(suggestionBorderColor));
        } else {
            setBorder(null);
        }
        repaint();
        this.focused = focused;
    }

    public boolean isFocused() {
        return focused;
    }

    private void replaceWithSuggestedText() {
        String suggestedWord = getText();
        String text = textField.getText();
        String typedWord = autoSuggestor.getCurrentlyTypedWord();
        String t = text.substring(0, text.lastIndexOf(typedWord));
        String tmp = t + text.substring(text.lastIndexOf(typedWord)).replace(typedWord, suggestedWord);
        textField.setText(tmp + " ");
    }
}