import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class MyCalculator extends Frame {
    
    public boolean setClear=true;
    double number,memValue;           //number is value stored before operation
    char op;     //operation
    String digitButtonText[] = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "0", "+/-", "." };
    String operatorButtonText[] = {"/", "sqrt", "*", "%", "-", "1/X", "+", "=" };
    String memoryButtonText[] = {"MC", "MR", "MS", "M+" };
    String specialButtonText[] = {"Backspc", "C", "CE" };

    MyDigitButton[] digitButton=new MyDigitButton[digitButtonText.length];
    MyOperatorButton[] operatorButton=new MyOperatorButton[operatorButtonText.length];
    MyMemoryButton[] memoryButton=new MyMemoryButton[memoryButtonText.length];
    MySpecialButton[] specialButton=new MySpecialButton[specialButtonText.length];

    Label displayLabel=new Label("0",Label.RIGHT); // set 0 on right - displayarea
    Label memLabel=new Label(" ",Label.RIGHT);       //

    final int FRAME_WIDTH=325,FRAME_HEIGHT=325;       // final means fixed, can not be changed
    final int HEIGHT=30, WIDTH=30, H_SPACE=10,V_SPACE=10;     //V,H SPACE is vertical,horizontal space between buttons
    final int TOPX=30, TOPY=50;                       // distance from top

    MyCalculator(String frameText){
        super(frameText);               //using inbuilt constructor of Frame class

        int tempX=TOPX , y=TOPY;
        displayLabel.setBounds(tempX,y,240,HEIGHT);         //setting the displayarea
        displayLabel.setBackground(Color.DARK_GRAY);            //color of display area
        displayLabel.setForeground(Color.CYAN);           //color of text in display area
        add(displayLabel);

        memLabel.setBounds(TOPX , TOPY+HEIGHT+V_SPACE , WIDTH , HEIGHT);
        add(memLabel);
    
        //seting co-ordinates of memorybutton;
        tempX=TOPX;
        y+=2*(HEIGHT+V_SPACE);
        for(int i=0 ; i<memoryButton.length ; i++){
            memoryButton[i]=new MyMemoryButton(tempX,y,WIDTH,HEIGHT,memoryButtonText[i],this);
            memoryButton[i].setBackground(Color.GRAY);
            memoryButton[i].setForeground(Color.WHITE);
            y+=(HEIGHT+V_SPACE);
        }

        // setting co-ordinates of Special buttons
        tempX=TOPX+WIDTH+H_SPACE;
        y=TOPY+HEIGHT+V_SPACE;
        for(int i=0 ; i<specialButton.length ; i++){
            specialButton[i]=new MySpecialButton(tempX,y,WIDTH*2,HEIGHT,specialButtonText[i], this);
            specialButton[i].setBackground(Color.GRAY);
            specialButton[i].setForeground(Color.WHITE);
            tempX=tempX+2*WIDTH+H_SPACE;
        }

        //setting co-ordinates of Digit Buttons
        int digitX=TOPX+WIDTH+H_SPACE;
        int digitY=TOPY+2*(HEIGHT+V_SPACE);
        tempX=digitX;
        y=digitY;
        for(int i=0 ; i<digitButton.length ; i++){
            digitButton[i]=new MyDigitButton(tempX,y,WIDTH,HEIGHT,digitButtonText[i],this);
            digitButton[i].setBackground(Color.GRAY);
            digitButton[i].setForeground(Color.YELLOW);
            tempX+=WIDTH+H_SPACE;
            if((i+1)%3==0){
                tempX=digitX;
                y+=HEIGHT+V_SPACE;
            }
        }

        //setting co-ordinates of Operator Buttons
        int opsX=digitX+2*(WIDTH+H_SPACE)+H_SPACE;
        int opsY=digitY;
        tempX=opsX;
        y=opsY;
        for(int i=0 ; i<operatorButton.length ; i++){
            tempX+=WIDTH+H_SPACE;
            operatorButton[i]=new MyOperatorButton(tempX,y,WIDTH,HEIGHT,operatorButtonText[i],this);
            operatorButton[i].setBackground(Color.GRAY);
            operatorButton[i].setForeground(Color.WHITE);
            if((i+1)%2==0){
                tempX=opsX;
                y+=HEIGHT+V_SPACE;
            }
        }

        addWindowListener(new WindowAdapter(){                   // when window is opened,closed,iconified,deconified,focused...
            public void windowClosing(WindowEvent ev){
                System.exit(0);
            }
        });

        setLayout(null);
        setSize(FRAME_WIDTH,FRAME_HEIGHT);
        setVisible(true);
    }

    static String getFormattedText(double Temp){       // if 456.0 then convert to 456
        String res=""+Temp;
        if(res.lastIndexOf(".0")>0){
            res=res.substring(0,res.length()-2);
        }
        return res;
    }
    
    public static void main(String []args){
        new MyCalculator("Calculator");
    }
}

class MyDigitButton extends Button implements ActionListener{
    MyCalculator cl;

    MyDigitButton(int x , int y , int width , int height , String cap , MyCalculator clc){
        super(cap);             // extends to the button class of java and sets the text on button as cap
        setBounds(x,y,width,height);
        this.cl=clc;
        this.cl.add(this);
        addActionListener(this);
    }
    
    static boolean isInString(String s,char ch){          // check presence of a character in the displayed string of characters on calculator screen
        for(int i=0 ; i<s.length() ; i++){
            if(s.charAt(i)==ch){
                return true;
            }
        }
        return false;
    }
    public void actionPerformed(ActionEvent ev){

        String tempText=((MyDigitButton)ev.getSource()).getLabel(); // getting label of button pressed

        if(tempText.equals(".")){
            if(cl.setClear==true){            // if type box contains nothing or 0 then do 0. and setClear as false as it is no longer empty
                cl.displayLabel.setText("0.");
                cl.setClear=false;
            }else if(isInString(cl.displayLabel.getText(),'.')==false){      // if input does not contain "." then add it ,else wrong input so return
                cl.displayLabel.setText(cl.displayLabel.getText()+".");
            }else{
                return;
            }
        }

        if(tempText.equals("+/-")){
            if(cl.setClear==false){
                if(cl.displayLabel.getText().charAt(0)=='-'){
                    cl.displayLabel.setText(cl.displayLabel.getText().substring(1));
                }else{
                    cl.displayLabel.setText("-"+cl.displayLabel.getText());
                }
            }
            return;
        }
        
        int index=0;
        try{
            index=Integer.parseInt(tempText);
        }catch(NumberFormatException e){
            return;
        }
        if(index==0 && cl.displayLabel.getText().equals("0")){
            return;
        }
        if(cl.setClear==true){
            cl.displayLabel.setText(""+index);    // append the number and set that displayLabel is not empty
            cl.setClear=false;
        }else{
            cl.displayLabel.setText(cl.displayLabel.getText()+index);   // append the number typed on the already present string of numbers
        }
    }
}

class MyOperatorButton extends Button implements ActionListener{
    MyCalculator cl;

    MyOperatorButton(int x,int y,int width, int height , String cap , MyCalculator clc){
        super(cap);                  // extends to the button class of java and sets the text on button as cap
        setBounds(x,y,width,height);
        this.cl=clc;
        this.cl.add(this);
        addActionListener(this);
    }

    public void actionPerformed(ActionEvent ev){
        String oplabel=((MyOperatorButton)ev.getSource()).getLabel();

        cl.setClear=true;
        double temp=Double.parseDouble(cl.displayLabel.getText()); //convert string to double

        if(oplabel.equals("1/x")){
            try{
                double tempd=1/(double)temp;
                cl.displayLabel.setText(MyCalculator.getFormattedText(tempd));          // getFormattedText is a constructor in MyCalculator
            }catch(ArithmeticException excp){
                cl.displayLabel.setText("Divide by 0.");
            }
            return;
        }

        if(oplabel.equals("sqrt")){
            try{
                double tempd=Math.sqrt(temp);
                cl.displayLabel.setText(MyCalculator.getFormattedText(tempd));
            }catch(ArithmeticException excp){
                cl.displayLabel.setText("Divide by 0.");
            }
            return;
        }

        if(!oplabel.equals("=")){
            cl.number=temp;
            cl.op=oplabel.charAt(0);
            return;
        }

        switch(cl.op){

            case '+': temp+=cl.number; break;

            case '-': temp=cl.number-temp; break;

            case '*': temp*=cl.number; break;

            case '%': try{
                        temp=cl.number%temp;
                        }catch(ArithmeticException excp){
                            cl.displayLabel.setText("Divide by 0.");
                            return;
                        }
                    break;
            case '/':try{
                        temp=cl.number/temp;
                    }catch(ArithmeticException excp){
                        cl.displayLabel.setText("Divide by 0");
                        return;
                    }
                    break;
        }

        cl.displayLabel.setText(MyCalculator.getFormattedText(temp));
    }
}

class MyMemoryButton extends Button implements ActionListener{

    MyCalculator cl;

    MyMemoryButton(int x,int y,int width,int height,String cap,MyCalculator clc){
        super(cap);
        setBounds(x,y,width,height);
        this.cl=clc;
        this.cl.add(this);
        addActionListener(this);
    }

    public void actionPerformed(ActionEvent ev){
        char memop=((MyMemoryButton)ev.getSource()).getLabel().charAt(1);  // MC, MR,MS, M+

        cl.setClear=true;
        double temp=Double.parseDouble(cl.displayLabel.getText());

        switch(memop){
            case 'C': cl.memLabel.setText(" "); cl.memValue=0.0; break;

            case 'R': cl.displayLabel.setText(MyCalculator.getFormattedText(cl.memValue)); break;

            case 'S': cl.memValue=0.0;

            case '+':cl.memValue+=Double.parseDouble(cl.displayLabel.getText());
                     if(cl.displayLabel.getText().equals("0") || cl.displayLabel.getText().equals("0.0")){
                         cl.memLabel.setText(" ");
                     }else{
                         cl.memLabel.setText("M");
                     }
                     break;
            
        }
    }
}

class MySpecialButton extends Button implements ActionListener{
    MyCalculator cl;

    MySpecialButton(int x,int y,int width,int height,String cap,MyCalculator clc){
        super(cap);
        setBounds(x,y,width,height);
        this.cl=clc;
        this.cl.add(this);
        addActionListener(this);
    }

    static String backSpace(String s){
        String Res="";
        for(int i=0 ; i<s.length()-1 ; i++){
            Res+=s.charAt(i);
        }
        return Res;
    }

    public void actionPerformed(ActionEvent ev){
        String Text=((MySpecialButton)ev.getSource()).getLabel();

        if(Text.equals("Backspc")){
            String temp=backSpace(cl.displayLabel.getText());
            if(temp.equals("")){
                cl.displayLabel.setText("0");
            }else{
                cl.displayLabel.setText(temp);
            }
            return;
        }

        if(Text.equals("C")){
            cl.number=0.0;
            cl.op=' ';
            cl.memValue=0.0;
            cl.memLabel.setText(" ");
        }

        cl.displayLabel.setText("0");
        cl.setClear=true;
    }
}