package modules;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
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

	public static void main(String[]args)
	{
		double a; 
		Scanner main = new Scanner(System.in); 
		//In order to have two values for one key, use an array list as the second arguments 
		HashMap<String,Double> hmap = new HashMap<String,Double>();
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
				//System.out.print(b);
				//if the data read equals the the four bases, it puts the symbol and mass into the hasmap 
				/*if(data[0].equals(C) || data[0].equals(A) || data[0].equals(G) || data[0].equals(U) )
				{
					double mass = Double.parseDouble(data[2]);
					//System.out.println(mass);
					hmap.put(data[1], mass);
				}
				*/
				//System.out.println(data[1]);
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
		//String [] cbase = new String[50]; 
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
				//System.out.println(data.substring(i, i+1));
				//cbase[i] = data.substring(i,i+1); 
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
					//System.out.println("The mass of the end is: " + hmap.get(fiveendname));
					currentMass = hmap.get(s) + hmap.get(fiveendname);
					totalMass += currentMass;
					System.out.println(currentMass + " , " + totalMass);
					cMass.add(Double.toString(totalMass)); 
					}
				}
				//adds the mass of the three end prime to the last base
				/*else if(i==length-2)
				{
					if(threeendname.equals("custom"))
					{
						currentMass = hmap.get(s) + threeendcustom;
						totalMass += currentMass;
						System.out.println(currentMass + " , " + totalMass );
					}
					//System.out.println("The mass of the end is: " + hmap.get(threeendname));
					else
					{
					currentMass = hmap.get(s) + hmap.get(threeendname);
					totalMass += currentMass; 
					System.out.println(currentMass + " , " + totalMass);
					}

				}
				*/
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
							//System.out.println(currentMass + " , " + yTotalMass );
							//yMass.add(Double.toString(yTotalMass)); 
						}
						//System.out.println("The mass of the end is: " + hmap.get(threeendname));
						else
						{
							currentMass = hmap.get(test) + hmap.get(threeendname);
							yTotalMass += currentMass; 
							//System.out.println(currentMass + " , " + yTotalMass);
							//yMass.add(Double.toString(yTotalMass)); 
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
		//System.out.println(Double.parseDouble(yMass.get(0)));
		//System.out.println(Double.parseDouble(cMass.get(0)));
		//System.out.println("Total mass is: " + finalMass);
		//System.out.print(yMass.get(0));
		createTotalChargeMass(charge); 
		createChargeMass(cMass,yMass,charge); 
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
					//System.out.println(chargemass);
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
	
	/*public static void createChargeMass(ArrayList<String> c, ArrayList<String> y, int charge)
	{
		int cLength = c.size(); 
		int yLength = y.size(); 
		try
		{
			PrintWriter writer2 = new PrintWriter("ChargeMassFile.txt"); 
	        BufferedWriter writer = new BufferedWriter(writer2);
	        String mass;
	        for(int j =0;j<=charge;j++)
	        {
		        writer.write("Charge: " + j + "\t\t");
		        //writer.newLine();
		        //writer.write("C-Fragments \t Y-Fragments \t");
	        }
	        writer.newLine();
	        for(int j =0;j<=charge;j++)
	        {
	        	writer.write("C-Fragments \t Y-Fragments \t");
	        }
	        writer.close(); return; 
		}
		catch(IOException ex)
		{
			System.out.print("Error in writing to file");
			return; 
		}		
	}
	*/
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
}
