import javax.swing.JFrame;


public class FinalScienceProject extends JFrame {

	    public FinalScienceProject() {			
	    	add(new FinalScienceProjectPart2()); //ShowPictures

	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setSize(500*2+20, 650);
	        setLocationRelativeTo(null);
	        setTitle("Science Project");
	        setResizable(true);
	        setVisible(true);
	    }
		public static void main(String[] argv){
			FinalScienceProject i = new FinalScienceProject();
		}
	

}
