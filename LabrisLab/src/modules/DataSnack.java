package modules;
import java.io.*;

/* This program takes in a input.txt file and spits out the total masses in a different 
 * file
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap; 

public class DataSnack
{
	public static String C ="Cytidine";
	public static String A ="Adenosine";
	public static String G ="Guanosine";
	public static String U ="Uridine"; 
	
	public static String fiveendname; 
	public static String threeendname; 
	
	public static double finalMass;
	public static ArrayList<Double> hairpincc; 
	public static HashMap<String,Double> hmap = new HashMap<String,Double>();

	//This is global variables for type2
	//public static ArrayList<Double> strand2;
	//public static ArrayList<String> base2;
	//public static ArrayList<String> ybase2;
	public static void main(String[]args)
	{
		double a; 
		Scanner main = new Scanner(System.in); 
		//In order to have two values for one key, use an array list as the second arguments 
		//HashMap<String,Double> hmap = new HashMap<String,Double>();
		//opens the t.tab file and reads from it
		File file = new File("t.tab");
		try
		{
			Scanner scanner = new Scanner(file);
			String line = ""; 
			String delimiter = "\t";
			String [] data;
			System.out.println("File opened");
			//puts each line into a string and then it splits words by the delimiter and puts it into a Sting array 
			while(scanner.hasNextLine())
			{
				line = scanner.nextLine();
				data = line.split(delimiter); 
				double mass = Double.parseDouble(data[2]);
				hmap.put(data[1], mass);				
			}
			//System.out.println(hmap.toString());
		}
		catch(FileNotFoundException e)
		{
			System.out.print("File does not exist");
			return; 
		}		
		double currentMass; 
		double totalMass = 0; 
		double yTotalMass = 0;
		File input = new File("input.txt"); 
		ArrayList<String> base = new ArrayList<>();
		ArrayList<String> ybase = new ArrayList<>();
		
		//an arraylist that contains the total mass after each incrementation 
		ArrayList<String> cMass = new ArrayList<>();
		ArrayList<String> yMass = new ArrayList<>(); 
		String yFragments;
		
		//prompts for a charge
		System.out.print("Enter a charge: ");
		int charge = main.nextInt();
		
		//prompts for a 5' and 3' end 
		int fiveend; 
		int threeend; 
		double fiveendcustom = 0; 
		double threeendcustom = 0; 
		System.out.print("Choose a 5' end(enter the number): 1.OH 2.Phosphate 3.Cyclic phosh 4.Custom");
		fiveend = main.nextInt(); 
		
		//prompts user for custom mass for 5' end 
		if(fiveend==4)
		{
			System.out.print("Enter a custom charge mass for 5' end: "); 
			fiveendcustom = main.nextDouble();
		}
		System.out.print("Choose a 3' end(enter the number): 1.OH 2.Phosphate 3.Cyclic phosh 4.Custom");
		threeend = main.nextInt(); 
		
		//prompts user for custom mass for 3' end 
		if(threeend==4)
		{
			System.out.print("Enter a custom charge mass for 3' end: "); 
			threeendcustom = main.nextDouble();
		}
		
		setFiveEndName(fiveend); 
		setThreeEndName(threeend);
		
		try
		{
			//reads the input file that has the sequence 
			Scanner scanner2 = new Scanner(input);
			String data = scanner2.nextLine();
			//yFragments = data; 
			StringBuilder y = new StringBuilder(data); 
			yFragments = y.reverse().toString();
			System.out.print(data);
			System.out.println();
			System.out.println(yFragments);
			//String space = " "; 
			//base = data.split(space);
			for(int i = 0; i < data.length();i++)
			{
				base.add(i,data.substring(i, i+1)); 
				ybase.add(i,yFragments.substring(i,i+1)); 
			}
			
		}
		catch(FileNotFoundException e)
		{
			System.out.print("File does not exist");
			return; 
		}
		
		int length = base.size();
		//goes through the string array and if the element has a value in the hashmap, it prints the total mass 
		//and the mono mass of the base
		System.out.println("C-Fragments Mass");
		//goes until it reaches the second to last base 
		for(int i =0; i<length-1;i++)
		{
			String s = base.get(i); 
			//System.out.print(s);
			if(hmap.containsKey(s))
			{
				//adds the five prime end mass to the first base 
				if(i==0)
				{
					//if the 5' end is a custom mass
					if(fiveendname.equals("custom"))
					{
						currentMass = hmap.get(s) + fiveendcustom;
						totalMass += currentMass;
						System.out.println(currentMass + " , " + totalMass );
						cMass.add(Double.toString(totalMass)); 
					}
					else
					{
					currentMass = hmap.get(s) + hmap.get(fiveendname);
					totalMass += currentMass;
					System.out.println(currentMass + " , " + totalMass);
					cMass.add(Double.toString(totalMass)); 
					}
				}
				else
				{
				currentMass = hmap.get(s);
				totalMass += hmap.get(s);
				cMass.add(Double.toString(totalMass)); 
				System.out.println(currentMass + " , " + totalMass );
				}
			}
		}
		//check if this is correct; the y fragment mass calculation
		int yLength = ybase.size();
		System.out.println("Y-Fragments Mass");
		for(int i = yLength-2; i>=0;i--)
		{
			int j = 0; 
			while(j<=i)
			{
				String test = ybase.get(j);
				System.out.print(test);
				if(hmap.containsKey(test))
				{
					if(j==0)
					{
						//adds the three end prime mass to the last base in the sequence 
						if(threeendname.equals("custom"))
						{
							currentMass = hmap.get(test) + threeendcustom;
							yTotalMass += currentMass;

						}
						else
						{
							currentMass = hmap.get(test) + hmap.get(threeendname);
							yTotalMass += currentMass; 
 
						}
						
					}
					else
						yTotalMass += hmap.get(test);
				}
				j++;
			}
			yMass.add(Double.toString(yTotalMass)); 
			System.out.println(" " + yTotalMass);
			yTotalMass = 0; 
		}
		finalMass = Double.parseDouble(yMass.get(0)) + Double.parseDouble(cMass.get(0));
		createTotalChargeMass(charge); 
		createChargeMass(cMass,yMass,charge);
		
		
		//Hairpin section 
		System.out.println("Enter a keystone pair x: "); 
		int keystoneX = main.nextInt();
		System.out.println("Enter a keystone pair y: ");
		int keystoneY = main.nextInt();
		System.out.println("Enter charge for hairpin: ");
		int hairpincharge = main.nextInt(); 
		ArrayList<String> fragments = new ArrayList<>();
		//fragments will hold the bases between the keystone pairs  
		for(int i = keystoneX-1;i<=keystoneY-1;i++)
		{
			fragments.add(base.get(i)); 
		}
		System.out.println(finalMass);
		hairpin(fragments,hmap,keystoneX,keystoneY); 
		//hairpinChargeMass(fragments,hmap,keystoneX,keystoneY,hairpincharge);
		
		System.out.println();
		
		//Section for TYPE II 
		//Variables for TYPE II calculations
		ArrayList<String> base2 = new ArrayList<>();
		ArrayList<String> ybase2 = new ArrayList<>();	
		ArrayList<Double> cMass2 = new ArrayList<>();
		ArrayList<Double> yMass2 = new ArrayList<>();
		readTest2(base2,ybase2);
		calcMassStrand2(base2,ybase2,cMass2,yMass2,hmap);
		System.out.print("Choose where the overhang stars: "); 
		int hangnumber = main.nextInt();
		System.out.print("Choose Overhang: (1) 5' of Str1 pairs with Str2 (2) 3' of Str1 pairs with Str2");
		int overhang = main.nextInt();
		if(overhang == 1)
		{		
			typeII(cMass,yMass,yMass2,cMass2,hangnumber,base,ybase,base2,ybase2);
		}
		else if(overhang ==2)
		{
			typeII(cMass,yMass,yMass2,cMass2,hangnumber,base,ybase,base2,ybase2);

		}
		else
		{
			System.out.print("Input a correct number: 1 or 2");
		}
		//System.out.print(ybase2.get(ybase.size()-1));
		
		//This is for Digest 
		//To distinguish between enzyme; 1 is for RNaseA and 2 is for RNase T1
		digest(base,cMass,1); 
	}	
	
	public static void digest(ArrayList<String>base,ArrayList<String>Mass,int Enzyme)
	{
		String C = "C"; 
		String U = "U";
		String G = "G"; 
		Double totalMass = 0.0; 
		ArrayList<Integer> index = new ArrayList<>();
		index.add(0); 
		try
		{
			PrintWriter writer2 = new PrintWriter(new FileWriter("Digest.txt"));
			if(Enzyme == 1)
			{
				int length = base.size(); 
				//Finds all the C/U in the strand
				for(int i = 1;i<length;i++)
				{
					if(base.get(i).equals(C) || base.get(i).equals(U))
					{
						System.out.print(base.get(i));
						index.add(i); 
					}
				}
				System.out.println();
				for(int i = 1;i<index.size();i++)
				{
					//System.out.println(index.get(i));
					//System.out.println(base.get(index.get(i)));
					for(int y = index.get(i)-1;y>=0;y--)
					{
						//System.out.println(i + ":" + y);
						System.out.println(base.get(y)+":"+base.get(index.get(i)));
						//This is the mass part 
						for(int z = index.get(i);z>=y;z--)
						{ 
							System.out.print(z +"-");
							//totalMass += Double.valueOf(Mass.get(z)); 
							totalMass+=hmap.get(base.get(z));
							System.out.println("(" + totalMass + ")");
						}
						System.out.println(totalMass); 
						totalMass = 0.0; 
					}
					//totalMass = 0.0; 
					System.out.println();
				}
				
			}
		}
		catch(IOException ex)
		{
			System.out.print("Error in writing to file");
			return; 
		}			
	}
	//Reads in input2 from text file input2.txt 
	public static void readTest2(ArrayList<String>base,ArrayList<String>ybase)
	{
		File input2 = new File("input2.txt"); 
		try
		{
			Scanner scanner = new Scanner(input2);
			String data = scanner.nextLine();	
			StringBuilder y = new StringBuilder(data); 
			String yFragments = y.reverse().toString();
			//System.out.print(data);
			//System.out.println();
			//System.out.println(yFragments);
			//String space = " "; 
			//base = data.split(space);
			for(int i = 0; i < data.length();i++)
			{
				base.add(i,data.substring(i, i+1)); 
				ybase.add(i,yFragments.substring(i,i+1)); 
			}
			scanner.close();
			
		}
		catch(FileNotFoundException e)
		{
			System.out.print("File does not exist");
			return; 
		}
	}
	public static void calcMassStrand2(ArrayList<String>cbase,ArrayList<String>ybase,ArrayList<Double>cMass2,ArrayList<Double>yMass2,HashMap<String,Double> hmap)
	{
		double currentMass = 0;
		double totalMass =0;
		int length = cbase.size();
		for(int i =0; i<length-1;i++)
		{
			String s = cbase.get(i); 
			//System.out.print(s);
			if(hmap.containsKey(s))
			{
				//This 5' and 3' end needs to be implemented
				
				//adds the five prime end mass to the first base 
				/*
				if(i==0)
				{
					//if the 5' end is a custom mass
					if(fiveendname.equals("custom"))
					{
						currentMass = hmap.get(s) + fiveendcustom;
						totalMass += currentMass;
						System.out.println(currentMass + " , " + totalMass );
						cMass.add(Double.toString(totalMass)); 
					}
					else
					{
					currentMass = hmap.get(s) + hmap.get(fiveendname);
					totalMass += currentMass;
					System.out.println(currentMass + " , " + totalMass);
					cMass.add(Double.toString(totalMass)); 
					}
				}
				*/
				
				currentMass = hmap.get(s);
				totalMass += hmap.get(s);
				cMass2.add(totalMass); 
			}
		}
		totalMass=0;
		System.out.println();
		/*for(int i =0; i<length-1;i++)
		{
			String s = ybase.get(i); 
			//System.out.print(s);
			if(hmap.containsKey(s))
			{
				currentMass = hmap.get(s);
				totalMass += hmap.get(s);
				yMass2.add(totalMass); 
			}
		}
		*/
		int yLength = ybase.size();
		double yTotalMass =0;
		for(int i = yLength-2; i>=0;i--)
		{
			int j = 0; 
			while(j<=i)
			{
				String test = ybase.get(j);
				if(hmap.containsKey(test))
				{
					yTotalMass += hmap.get(test);
					j++;
				}
			}
			yMass2.add(yTotalMass); 
			yTotalMass = 0; 
		}
}
	public static void typeII(ArrayList<String>cMass,ArrayList<String>yMass,ArrayList<Double>yMass2,ArrayList<Double>cMass2,int overhang,ArrayList<String>base,ArrayList<String>ybase,ArrayList<String>base2,ArrayList<String>ybase2)
	{
		try
		{
			PrintWriter writer2 = new PrintWriter(new FileWriter("TypeII.txt"));
	        BufferedWriter writer = new BufferedWriter(writer2);
	        int n = cMass.size()-overhang+1;
        	//System.out.println(String.join("", Collections.nCopies(n, "//")));
	        writer2.write("Str1 c\t");
	        for(int i=0;i<=(cMass.size()-overhang);i++)
	        {
	        	//String.join("", Collections.nCopies(n, "//"));
	        	writer2.write("//\t");
	        }
	        for(int i=0;i<base.size();i++)
	        {
	        	writer2.write(base.get(i) + "\t");
	        }
	        //writer2.write("\n");
	        writer2.println();
	        writer2.write("Str2y\t");
	        for(int i=0;i<=(cMass.size()-overhang);i++)
	        {
	        	writer2.write("  \t");
	        }
	        for(int i = 0;i<cMass.size();i++)
	        {
	        	//writer2.format("%.2f  ",cMass.get(i));
	        	double value =  Double.valueOf(cMass.get(i));
	        	writer2.printf("%.2f\t",value);
	        }
	        writer2.printf("0");
	        int strand2inc = 0;
	        writer2.println();
			while(strand2inc<yMass2.size())
			{
				writer2.print(ybase2.get(strand2inc) + "     \t");
				for(int y = 0;y<(cMass.size()+1-overhang);y++)
				{
					writer2.printf("%.2f\t",cMass2.get(strand2inc)+1);
				}
				for(int i = 0;i<cMass.size();i++)
				{
					double v = Double.valueOf(cMass.get(i)) + cMass2.get(strand2inc);
					writer2.printf("%.2f\t", v);
				}
				writer2.printf("%.2f\t",cMass2.get(strand2inc)+1);
				writer2.println();
				strand2inc++;
			}
			writer2.print(ybase2.get(ybase2.size()-1) + "     \t");
			for(int i =0;i<(cMass.size()+1-overhang);i++)
			{
				writer2.print("0\t");
			}
			for(int i = 0;i<cMass.size();i++)
			{
				double value =  Double.valueOf(cMass.get(i));
	        	writer2.printf("%.2f\t",value + 1.007825);
			}
	        writer2.printf("0");
	        writer2.println();
	        int counter = 0; 
	        while(counter<cMass.size()+1-overhang)
	        {
	        	writer2.print("//   \t");
	        	for(int i =0;i<(cMass.size()+1-overhang);i++)
	        	{
	        		writer2.print("0\t");
	        	}
	        	for(int i = 0;i<cMass.size();i++)
	        	{
	        		double value =  Double.valueOf(cMass.get(i));
	        		writer2.printf("%.2f\t",value + 1.007825);
	        	}
        		writer2.print("0\t");
	        	writer2.println();
	        	counter++; 
	        }
	        writer2.println();
	   
	        //This is second part
	        //Nvm; it crashed because it too large
	        //Use a different file type 
	        
	        n = yMass.size()-overhang+1;
	        writer2.write("Str1 y\t");
	        for(int i=0;i<=(yMass.size()-overhang);i++)
	        {
	        	writer2.write("//\t");
	        }
	        for(int i=0;i<ybase.size();i++)
	        {
	        	writer2.write(ybase.get(i) + "\t");
	        }
	        writer2.println();
	        writer2.write("Str2c\t");
	        for(int i=0;i<=(yMass.size()-overhang);i++)
	        {
	        	writer2.write("  \t");
	        }
	        for(int i = 0;i<yMass.size();i++)
	        {
	        	double value2 =  Double.valueOf(yMass.get(i));
	        	writer2.printf("%.2f\t",value2);
	        }
	        writer2.printf("0");
	        strand2inc = 0;
	        writer2.println();
			while(strand2inc<cMass2.size())
			{
				writer2.print(base2.get(strand2inc) + "     \t");
				for(int y = 0;y<(yMass.size()+1-overhang);y++)
				{
					writer2.printf("%.2f\t",yMass2.get(strand2inc)+1);
				}
				for(int i = 0;i<yMass.size();i++)
				{
					double v = Double.valueOf(yMass.get(i)) + yMass2.get(strand2inc);
					writer2.printf("%.2f\t", v);
				}
				writer2.printf("%.2f\t",yMass2.get(strand2inc)+1);
				writer2.println();
				strand2inc++;
			}
			writer2.print(base2.get(base2.size()-1) + "     \t");
			for(int i =0;i<(yMass.size()+1-overhang);i++)
			{
				writer2.print("0\t");
			}
			for(int i = 0;i<yMass.size();i++)
			{
				double value =  Double.valueOf(yMass.get(i));
	        	writer2.printf("%.2f\t",value + 1.007825);
			}
	        writer2.printf("0");
	        writer2.println();
	        int counter2 = 0; 
	        while(counter2<yMass.size()+1-overhang)
	        {
	        	writer2.print("//   \t");
	        	for(int i =0;i<(yMass.size()+1-overhang);i++)
	        	{
	        		writer2.print("0\t");
	        	}
	        	for(int i = 0;i<yMass.size();i++)
	        	{
	        		double value =  Double.valueOf(yMass.get(i));
	        		writer2.printf("%.2f\t",value + 1.007825);
	        	}
        		writer2.print("0\t");
	        	writer2.println();
	      		counter2++; 
	        }
	        
	        
	        
        	writer.close();
        	writer2.close(); 


		}
		catch(IOException ex)
		{
			System.out.print("Error in writing to file");
			return; 
		}	
	}
	
	//creates a text file with the charge masses 
	public static void createChargeMass(ArrayList<String> c, ArrayList<String> y, int charge)
	{
		int Charge = charge; 
		int cLength = c.size(); 
		int yLength = y.size(); 
		try
		{
			PrintWriter writer2 = new PrintWriter("ChargeMassFile.txt"); 
	        BufferedWriter writer = new BufferedWriter(writer2);
	        String mass;
	        double test; 
	        String chargemass; 
	        writer.write("Charge: " + 0);
	        writer.newLine();
	        writer.write("C-Fragments \t Y-Fragments");
	        writer.newLine();
	        //this prints the mono mass regardless of what charge is inputed in 
	        for(int i = 0;i<=cLength-1;i++)
			{
				//for c fragment side 
				mass = c.get(i);
				writer.write(mass + "\t");
				//for the y fragment side 
				mass = y.get(i); 
				writer.write(mass);
				writer.newLine(); 
			}
	        writer.newLine();
	        //goes through from 0 to input charge 
	        for(int j =1;j<=charge;j++)
	        {
		        writer.write("Charge: " + j);
		        writer.newLine();
		        writer.write("C-Fragments \t Y-Fragments");
		        writer.newLine();
		        //System.out.println(j);
		        //System.out.println(c.get(0));
		        //this prints the charge mass of every incremented total mass 
	        	for(int i = 0;i<=cLength-1;i++)
				{
					//for c fragment side 
					mass = c.get(i);
					test = Double.parseDouble(mass); 
					test = (test-j * 1.00794)/j;
					chargemass = Double.toString(test); 
					writer.write(chargemass + "\t");
					//for the y fragment side 
					mass = y.get(i); 
					test = Double.parseDouble(mass); 
					test = (test-j * 1.00794)/j;
					chargemass = Double.toString(test); 
					writer.write(chargemass);
					writer.newLine(); 
				}
	        	writer.newLine();
	        }
	        
			writer.close(); 
			return;
		}
		catch(IOException ex)
		{
			System.out.print("Error in writing to file");
			return; 
		}		
	}
	public static void setFiveEndName(int a)
	{
		//sets the name for the five end so the data can be traced in the hashmap 
		if(a == 1)
		{
			fiveendname = "HO-";
		}
		if(a==2)
		{
			fiveendname = "P-";
		}
		if(a==3)
		{
			fiveendname = "Pc-";

		}
		if(a==4)
		{
			fiveendname = "custom";
		}
	}
	public static void setThreeEndName(int a)
	{
		//sets the name for the five end so the data can be traced in the hashmap 
		if(a == 1)
		{
			threeendname = "-OH";
		}
		if(a==2)
		{
			threeendname = "-P";
		}
		if(a==3)
		{
			threeendname = "-Pc";

		}
		if(a==4)
		{
			threeendname = "custom";
		}
	}
	public static void createTotalChargeMass(int charge)
	{
		try
		{
			PrintWriter writer2 = new PrintWriter("TotalChargeMassFile.txt"); 
	        BufferedWriter writer = new BufferedWriter(writer2);
	        writer.write("Charge: \t Total Mass:");
	        writer.newLine();
	        writer.write(0 + "\t" + finalMass);
	        writer.newLine();
        	double chargeMass; 
	        for(int i = 1;i<charge;i++)
	        {
	        	writer.write(i + "\t");
				chargeMass = (finalMass-i * 1.00794)/i;
				writer.write(Double.toString(chargeMass));
				writer.newLine();
	        }
	     writer.close();   
		}
		catch(IOException ex)
		{
			System.out.print("Error in writing to file");
			return; 
		}
	}
	//This methods calculates hairpin section and prints the z=0 masses 
	public static void hairpin(ArrayList<String>data, HashMap<String,Double> hmap,int x,int y)
	{
		int tempx = x; 
		//this calculates the cc segmentation of hairpin 
		for(int i = 0; i<data.size();i++)
		{
			String base = data.get(i);
			double mass = hmap.get(base);
			System.out.println(base + tempx + ":" + base + tempx + "cc : " + mass);
			int ycounter = tempx +1; 
			for(int j = i +1; j < data.size();j++)
			{
				String temp = data.get(j);
				double current = hmap.get(temp);
				mass += current; 
				System.out.println(base + tempx + ":" + temp + ycounter + "cc : " + mass);
				ycounter++; 
			}
			tempx++; 
		}
		System.out.println(); 
		tempx = x; 
		//this calcualtes the cy segmentation 
		for(int i = 0; i<data.size();i++)
		{
			String base = data.get(i);
			double mass = hmap.get(base) - 30.9820042;
			System.out.println(base + tempx + ":" + base + tempx + "cy : " + mass);
			int ycounter = tempx +1; 
			for(int j = i +1; j < data.size();j++)
			{
				String temp = data.get(j);
				double current = hmap.get(temp);
				mass += current; 
				System.out.println(base + tempx + ":" + temp + ycounter + "cy : " + mass);
				ycounter++; 
			}
			tempx++; 
		}
		System.out.println(); 
		tempx = x; 
		//this calcualtes the yc segmentation 
		for(int i = 0; i<data.size();i++)
		{
			String base = data.get(i);
			double mass = hmap.get(base) + 92.929531;
			System.out.println(base + tempx + ":" + base + tempx + "yc : " + mass);
			int ycounter = tempx +1; 
			for(int j = i +1; j < data.size();j++)
			{
				String temp = data.get(j);
				double current = hmap.get(temp);
				mass += current; 
				System.out.println(base + tempx + ":" + temp + ycounter + "yc : " + mass);
				ycounter++; 
			}
			tempx++; 
		}
		System.out.println();
		//this calculates the M-Portion for cc
		tempx = x; 
		for(int i = 0; i<data.size();i++)
		{
			String base = data.get(i);
			//double mass = finalMass - hmap.get(base);
			double keystonemass = hmap.get(base); 
			System.out.println("M-" + base + tempx + ":" + base + tempx + "cc : " + (finalMass - hmap.get(base)));
			int ycounter = tempx +1; 
			for(int j = i +1; j < data.size();j++)
			{
				String temp = data.get(j);
				keystonemass += hmap.get(temp);
				double tempMass = finalMass - keystonemass; 
				System.out.println("M-" + base + tempx + ":" + temp + ycounter + "cc : " + tempMass);
				ycounter++; 
			}
			tempx++;
		}
		//this calculates the M-Portion for cy
		System.out.println();
		tempx = x; 
		for(int i = 0; i<data.size();i++)
		{
			String base = data.get(i);
			//double mass = finalMass - hmap.get(base);
			double keystonemass = hmap.get(base) - 30.9820042; 
			System.out.println("M-" + base + tempx + ":" + base + tempx + "cy : " + (finalMass - keystonemass));
			int ycounter = tempx +1; 
			for(int j = i +1; j < data.size();j++)
			{
				String temp = data.get(j);
				//double current = hmap.get(temp);
				keystonemass += hmap.get(temp);
				double tempMass = finalMass - keystonemass; 
				System.out.println("M-" + base + tempx + ":" + temp + ycounter + "cy : " + tempMass);
				ycounter++; 
			}
			tempx++;
		}
		System.out.println(); 
		//this calculates the M-Portion for yc
		tempx = x; 
		for(int i = 0; i<data.size();i++)
		{
			String base = data.get(i);
			//double mass = finalMass - hmap.get(base);
			double keystonemass = hmap.get(base) + 92.929531; 
			System.out.println("M-" + base + tempx + ":" + base + tempx + "yc : " + (finalMass - keystonemass));
			int ycounter = tempx +1; 
			for(int j = i +1; j < data.size();j++)
			{
				String temp = data.get(j);
				//double current = hmap.get(temp);
				keystonemass += hmap.get(temp);
				double tempMass = finalMass - keystonemass; 
				System.out.println("M-" + base + tempx + ":" + temp + ycounter + "yc : " + tempMass);
				ycounter++; 
			}
			tempx++;
		}
	}

}
