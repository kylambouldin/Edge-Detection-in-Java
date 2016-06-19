import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class FinalScienceProjectPart2 extends JPanel {
	BufferedImage inputSourceImage;
	String stringSource, status, stringShape;
	Random generator = new Random();
	int[][] processedImage;
	int[] edgepoints, rgbs;
	int[] totaldata, xDerivativeList,yDerivativeList; //derivatives
	int wpixel, hpixel, average, numofaverages, numofedges,numNoNoisePoints;
	int i = 0;
	int derivativeCounter = 0;
	int edgecounter = 0;
	double efficiency;	
	double percentNoise; // Noise value. Total value is usually 256.
	int edgeColor = 255;
	double maxpercent = .70;
	Boolean noiseOn = false;
	//SCREEN SETTINGS
	int titleHeight=20, buttonHeight=30, screenWidth, screenHeight, graphStartX, space=20, mag=5,
	buttonWidth = 12, PAD = 10;
	
	//buttons
	JButton circle = new JButton("Circle");
	JButton square = new JButton("Square");    
	JButton triangle = new JButton("Triangle");
	JButton zero = new JButton("0%");
	JButton twentyFive = new JButton("25%");
	JButton fifty = new JButton("50%");
	JButton seventyFive = new JButton("75%");
	JButton oneHundred = new JButton("100%");
	JLabel efficiencyLabel = new JLabel();

	public FinalScienceProjectPart2(){
		setFocusable(true);
		setBackground(Color.yellow);
		setDoubleBuffered(true);
		setVisible(true);
		generateButtons();
		setInputImage("/Users/kyla2/Documents/Programming/CompletedProjects/EdgeDetection/shapes/blank.jpg",percentNoise);
		setStringSource("/Users/kyla2/Documents/Programming/CompletedProjects/EdgeDetection/shapes/blank.jpg");
		stringShape = "Circle";
	}

	public void setInputImage(String file, double n){
		try {
			inputSourceImage = ImageIO.read(new File(file));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		percentNoise = n;
		wpixel = inputSourceImage.getWidth(null); //num of pixels
		hpixel = inputSourceImage.getHeight(null); //num of pixels

		screenWidth = wpixel*mag;
		screenHeight = (hpixel*mag + buttonHeight + titleHeight);
		setSize(screenWidth*2+space, screenHeight+100);
		graphStartX = screenWidth + space; //maybe *mag or (screenWidth+space);
		i=0;
		edgecounter =0;

		totaldata = new int[(wpixel*hpixel)*4]; //////////////
		edgepoints = new int[(wpixel*hpixel)*4]; ////////////

		processedImage = pixelArray();
		FindEdge(processedImage);

		//printPoints(edgepoints); THIS IS CALLED IN FINDEDGE()
		//printDerivative(totaldata); THIS IS CALLED IN FINDEDGE()



		//		System.out.println("totaldatalength: " + totaldata.length);
		//		System.out.println("edgepoints: " + edgepoints.length);
		//		System.out.println("w: " + wpixel);
		//		System.out.println("h: " + hpixel);


		efficiency = findEfficiency();
		status = "Signal to Noise: " + efficiency;
		efficiencyLabel.setText(status);
		System.out.println("E:" + efficiency);

	}

	public void setStringSource(String s){
		stringSource = s;
	}

	public void setNoiseOn(boolean o){
		noiseOn = o;
	}

	private void generateButtons(){
		circle.setBounds(buttonWidth, screenHeight, buttonHeight, buttonHeight);
		circle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				stringShape = "Circle";
				System.out.println(stringShape + " w/ " + percentNoise + "%");
				setInputImage("/Users/kyla2/Documents/Programming/CompletedProjects/EdgeDetection/shapes/circle_jpg.jpg",percentNoise);
				setStringSource("/Users/kyla2/Documents/Programming/CompletedProjects/EdgeDetection/shapes/circle_jpg.jpg");
			}
		});

		square.setBounds(buttonWidth*2, screenHeight, buttonWidth, buttonHeight);
		square.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				stringShape = "Square";
				System.out.println(stringShape + " w/ " + percentNoise + "%");
				setInputImage("/Users/kyla2/Documents/Programming/CompletedProjects/EdgeDetection/shapes/square7.jpg",percentNoise);
				setStringSource("/Users/kyla2/Documents/Programming/CompletedProjects/EdgeDetection/shapes/square7.jpg");
			}
		});

		triangle.setBounds(buttonWidth*3, screenHeight, buttonWidth, buttonHeight);
		triangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				stringShape = "Triangle";
				System.out.println(stringShape + " w/ " + percentNoise + "%");
				setInputImage("/Users/kyla2/Documents/Programming/CompletedProjects/EdgeDetection/shapes/goodtriangle.jpg",percentNoise);
				setStringSource("/Users/kyla2/Documents/Programming/CompletedProjects/EdgeDetection/shapes/goodtriangle.jpg");
				
			}
		});

		zero.setBounds(buttonWidth*5, screenHeight, buttonWidth, buttonHeight);
		zero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println(stringShape + " w/ " +" 0%");
				setInputImage(stringSource,0);
				setNoiseOn(false);
			}
		});

		twentyFive.setBounds(buttonWidth*6, screenHeight, buttonWidth, buttonHeight);
		twentyFive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println(stringShape + "w/" + "25%");
				setInputImage(stringSource,25);
				
				setNoiseOn(true);
			}
		});

		fifty.setBounds(buttonWidth*7, screenHeight, buttonWidth, buttonHeight);
		fifty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println(stringShape + " w/ " +" 50%");
				setInputImage(stringSource,50);
				setNoiseOn(true);
			}
		});

		seventyFive.setBounds(buttonWidth*8, screenHeight, buttonWidth, buttonHeight);
		seventyFive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println(stringShape + " w/ " +" 75%");
				setInputImage(stringSource,75);
				setNoiseOn(true);
			}
		});

		oneHundred.setBounds(buttonWidth*9, screenHeight, buttonWidth, buttonHeight);
		oneHundred.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println(stringShape + " w/ " +" 100%");
				setInputImage(stringSource,100);
				setNoiseOn(true);
			}
		});

		add(circle);
		add(square);
		add(triangle);
		add(zero);
		add(twentyFive);
		add(fifty);
		add(seventyFive);
		add(oneHundred);
		add(efficiencyLabel);
	}

	public int[][] pixelArray() {
		rgbs = new int[wpixel*hpixel];
		//		System.out.println("rgbs length: " + rgbs.length);
		int[][] pixelArrayData = new int[wpixel][hpixel];
		int colorcounter = 0;

		/*		24 bit color
		16777216 = 2^24

		16 bit color
		65536 = 2^16

		(2^24)/(2^24) = 1bit --> 0 or 1
		(2^24) / (2^16) = 2^8bits --> 0-225
		 */

		inputSourceImage.getRGB(0, 0, wpixel, hpixel, rgbs, 0, wpixel);
		for (int r=0;r<wpixel;r++) {
			for (int c=0;c<hpixel;c++) {
				pixelArrayData[r][c] = -(rgbs[colorcounter])/65536; //greyscale
				colorcounter++;
				//			System.out.print(pixelArrayData[r][c]+ "	");
			}
			//				System.out.println();
		}
		if (noiseOn){
			pixelArrayData = addNoise(pixelArrayData,percentNoise);
		}

		/*		System.out.println("WITH NOISE");

		for (int r=0;r<wpixel;r++){
			for (int c=0;c<hpixel;c++){
				//			System.out.print(pixelArrayData[r][c] + "	");
			}
			//		System.out.println();
		}*/

		return pixelArrayData;
	}

	public int[][] addNoise(int[][] data, double percent){
		double random;
		int color;
		int[][] noiseImage = data;

		for (int y=0;y<noiseImage.length;y++){
			for(int x=0;x<noiseImage.length;x++){
				random = generator.nextDouble() - 0.5; //256*(percent/100)
				random *= 256*(percent/100);
				color = noiseImage[x][y];
				if (color + (int)random > 256){
					color -= (int) random;
					noiseImage[x][y] = color;
					inputSourceImage.setRGB(x,y,color);
				}
				else if (color + (int)random < 0){
					color -= (int)random;
					noiseImage[x][y] = color;
					inputSourceImage.setRGB(x,y,color);
				}
				else{
					color += (int)random;
					noiseImage[x][y] = color;
					inputSourceImage.setRGB(x, y, color);
				}
			}
		}
		return noiseImage;
	}

	public void FindEdge(int[][] list){ //list of whole image (may include noise)
		//		System.out.println("listSizeFindEdge(): " + list.length);
		//each list of Z values in one row of Y need a derivative.

		//		System.out.println("YDERIVATIVE...");
		for (int y = 0; y<list.length;y++){
			yDerivativeList = new int[list.length];
			for (int x=0;x<list.length;x++){
				yDerivativeList[derivativeCounter] = list[y][x];
				derivativeCounter++;
			}
			YDerivative(yDerivativeList,y);
			derivativeCounter = 0;
		}

		//		System.out.println("XDERIVATIVE...");
		derivativeCounter = 0;
		for (int x = 0; x<list.length;x++){
			xDerivativeList = new int[list.length];
			for (int y=0;y<list.length;y++){
				xDerivativeList[derivativeCounter] = list[y][x];
				derivativeCounter++;
			}
			XDerivative(xDerivativeList,x);
			derivativeCounter = 0;
		}

		if (noiseOn == false){
			numNoNoisePoints = edgecounter;
		}
//		System.out.println("numNoNoisePoints: " + numNoNoisePoints);
//		System.out.println("edgecounter: " + edgecounter);

		printPoints(edgepoints);
		printDerivative(totaldata);



	}

	public void YDerivative(int[] list, int yvalue){
		int[] slope = new int[list.length];
		int counter = 0;
		int sum = 0;

		//		System.out.println();
		for(int x=0;x<list.length;x++){
			//			System.out.print(list[x] + ", ");
		}
		//		System.out.println();
		for (int y=1;y<list.length;y++){
			slope[counter] = Math.abs(list[y]-list[y-1]);
			counter++;
		}
		/*		System.out.println("SLOPE...");
		for (int i =0; i<list.length;i++){
			//			System.out.print(slope[i]+ ", ");
		}*/

		for (int k=0;k<list.length;k++){
			sum += slope[k];
		}
		average += Math.abs(sum/slope.length);
		numofaverages++;
		//		System.out.print("AverageD " + sum/slope.length);

		int max = findMax(slope);
		//		System.out.println("	max: " + max);

		counter = 0;
		for (int j=0;j<slope.length;j++){
			if (slope[j] > max*maxpercent){
				inputSourceImage.setRGB(j, yvalue, edgeColor);
				edgepoints[edgecounter] = j;
				edgecounter++;
				edgepoints[edgecounter] = yvalue;
				edgecounter++;
			}
		}

		addToTotalData(slope);
	}

	public void XDerivative(int[] list, int xvalue){
		int[] slope = new int[list.length];
		int counter = 0;
		int sum = 0;

		//		System.out.println();
		for(int x=0;x<list.length;x++){
			//			System.out.print(list[x] + ", ");
		}
		//		System.out.println();
		for (int y=1;y<list.length;y++){
			slope[counter] = Math.abs(list[y]-list[y-1]);
			counter++;
		}
		/*				System.out.println("SLOPE...");
		for (int i =0; i<list.length;i++){
					System.out.print(slope[i]+ ", ");
		}*/

		for (int k=0;k<list.length;k++){
			sum += slope[k];
		}
		average += Math.abs(sum/slope.length);
		numofaverages++;

		//		System.out.print("AverageD " + sum/slope.length);

		int max = findMax(slope);
		//		System.out.println("slopemax: " + max);

		counter = 0;
		for (int j=0;j<slope.length;j++){
			if (slope[j] > max*maxpercent){
				inputSourceImage.setRGB(xvalue, j, edgeColor);
				edgepoints[edgecounter] = xvalue;
				edgecounter++;
				edgepoints[edgecounter] = j;
				edgecounter++;
			}
		}
		addToTotalData(slope);
	}

	public void addToTotalData(int[] data){
		for (int x=0;x<data.length;x++){
			totaldata[i] = data[x];
			i++;
		}
	}

	public int findMax(int[] list){
		int max;
		max = list[0];
		for (int x=0;x<list.length;x++){
			if (list[x] > max){
				max = list[x];
			}
		}
		return max;
	}

	public int findMin(int[] list){
		int min;
		min = list[0];
		for (int x=0;x<list.length;x++){
			if (list[x] < min){
				min = list[x];
			}
		}
		return min;
	}

	public int findAverage(){
		int a = average/numofaverages;
		return a;
	}

	public void printPoints(int[] list){
		/*System.out.println("PRINTEDGEPOINTS");
		for (int x=0;x<list.length;x+=2){
			System.out.print("(" + list[x] + "," + list[x+1] + ")");
		}*/
	}

	public void printDerivative(int[] d){
		/*	System.out.println();
		System.out.println("PRINTDERIVATIVE");
		for (int x = 0;x<d.length;x++){
			System.out.print(d[x] + ", ");
		}*/
	}

	public double findEfficiency(){
		double efficiency;
		//		System.out.println();
		//		System.out.println("max: " + max);
		//		System.out.println("average: " + totalAverage);
		//		efficiency = (max-totalAverage)/totalAverage;
		efficiency =(double) numNoNoisePoints/(edgecounter - numNoNoisePoints);
		return efficiency;

	}

	public void paint(Graphics g){

		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;

		g.drawImage(inputSourceImage, 0, buttonHeight+titleHeight, screenWidth, screenHeight-buttonHeight-titleHeight, this);

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Draw Y axis.
		g.drawLine(graphStartX,buttonHeight+titleHeight,graphStartX, screenHeight);
		//draw X axis
		g.drawLine(graphStartX,screenHeight,screenWidth*2, screenHeight);

		// Draw labels.

		// Y label.
		int xCounter = 115;
		String[] s = "Value of Derivative".split("");
		for ( int x=0; x<s.length; x++) {
			xCounter +=15;
			g.drawString(s[x], graphStartX-10, xCounter);
		}
		//	g.drawString(s, graphWStart, i);
		// X label.
		String s2 = "X pixel number		Y pixel number";

		g.drawString(s2, graphStartX+150, screenHeight+15);

		// Draw lines.
		// The space between values along the x axis.
		//		System.out.println("totaldatalength: " + totaldata.length);

		double xInc = (double)(screenWidth - 2*PAD)/(totaldata.length-1);
		///////		double xInc = (double)(screenWidth-PAD-space)/(totaldata.length-1);
		//		System.out.println("xInc: " + xInc);
		// Scale factor for y/data values.

		double scale = (double)(((screenHeight-buttonHeight-titleHeight)) - 2*PAD)/findMax(totaldata);
		//drawLines
		g.setColor(Color.green);
		//		g2.setPaint(Color.green.darker());
		for(int i = 0; i < totaldata.length-1; i++) {
			double x1 = graphStartX+ PAD + i*xInc;
			double y1 = (screenHeight) -PAD- scale*totaldata[i];
			double x2 = graphStartX +PAD+ (i+1)*xInc;
			double y2 = (screenHeight)- PAD-scale*totaldata[i+1];
			g.drawLine((int) x1, (int)y1, (int)x2, (int)y2);
			//			g2.draw(new Line2D.Double(x1, y1, x2, y2));
		}
		// Mark data points.
		g.setColor(Color.red);
		//		g2.setPaint(Color.red);
		for(int i = 0; i < totaldata.length; i++) {
			double x = graphStartX +PAD+ i*xInc;
			double y = (screenHeight)-PAD- scale*totaldata[i];
			g.fillOval((int) x-2,(int) y-2, 4, 4);
			//			g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
		}


		Toolkit.getDefaultToolkit().sync();
		g.dispose();

		requestFocusInWindow(true);
	}	

	public void actionPerformed(ActionEvent e) {
		FindEdge(processedImage);
		add(efficiencyLabel);
		repaint();

	}

}	
