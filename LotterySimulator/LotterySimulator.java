/*
 * *******BUGS******
 * ~type validation issues in some entry fields
 * ~no validation on confirming numbers
 * ~confirm number method gives false wins for whichever number is changed during this method
 * ~confirm number needs its own validation method to work bug free as entering the same value on index may glitch out and allow it to update
 * 
 * 
 * 
 * 
 * 
 * *******alternatives**********
 * ~alternatively more counter variables could be used
 * ~how the program currently is leaves room for more functionality to be implemented such as win percentages
 * ~i have stored results and player numbers in seperate objects since this could be changed in other implementations
 * 	for example when implementing a lotto system the playernumber can be set by other functions by storing them in objects as well as the drawn number be retrieved and converted back into int
 * 	based on positions
 * 	
 * 
 * 
 * 
 * 
 */

//class imports here
//this gives us access to extended functionality
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;
import java.util.Scanner;

//this is our main class file for the lottery simulator application
public class LotterySimulator {
//initialize our scanner
	private Scanner input;

	// initialize our objects
	private LottoResults[] Results;
	private PlayerNumbers[] playerNumbers;

	// initialize our member variables
	// poolSize is hardcoded as standard lottery array however can easilly be
	// changed by changing the variable here
	private int poolSize;
	private int[] pickNumbers;
	private int timesRolled;
	private String[] lottoNumberResults;

	// main method
	// this is the first memthod we run
	public static void main(String[] args) {
		// this method is only used for initalizing our object for this class file
		LotterySimulator runOrder = new LotterySimulator();
		// return nothing we have finished this method
		return;
	}

	// this is our main constructor, this controls the main bulk of our program and
	// is where our declarations
	// as well as methods are controlled
	public LotterySimulator() {

		// declaring and initalizing variables and arrays here
		int arraySize = arraySizeFromFile();
		this.Results = new LottoResults[arraySize];
		this.playerNumbers = new PlayerNumbers[10];
		this.input = new Scanner(System.in);
		this.poolSize = 49;
		// pick numbers is hard coded to 6 since this is the amount of numbers we draw
		// for bother winning and chosen numbers
		// this is hardcoded as standard lottery draws
		this.pickNumbers = new int[6];

		// here we control the order we execute our methods
		// get previous results retrieve our stored values
		GetPreviousResults();
		// getprevious playernumbers retrieves or previous chosen numbers for future
		// manipulation
		GetPreviousPlayerNumbers();
		// chosenNumbers is the method ran to decide our chosen numbers for this
		// instance of the application
		ChosenNumbers();
		// this method asks the user if they want to stay with playernumbers entered or
		// change them
		// it also uses a setter to update these numbers
		ConfirmNumbers();


		// this method decides how many times we wish to execute our winning
		// numbers/draw results
		RollHowMany();
		// this method is what displays our results and our winning numbers to show
		// the user if we they have won
		ShowResults();
		// record results based upon the array size we have generated
		RecordResults(arraySize);
		// record playeenumbers based on the array size we have generated
		RecordPlayerNumbers(arraySize);
		// close our scanner its no longer needed
		input.close();
		// end our constructor since its no longer needed to run
		return;
	}

	public void ConfirmNumbers() {
		//variable declaration
		String confirmAnswer;
		int changeNumber;
		int newValue;
		//prompt user to update numbers
		System.out.println("you have entered " + this.pickNumbers[0] + " " + this.pickNumbers[1] + " "
				+ this.pickNumbers[2] + " " + this.pickNumbers[3] + " " + this.pickNumbers[4] + " "
				+ this.pickNumbers[5]
				+ "\r\nWould you like to update these numbers?\r\nEnter 'No' or 'no' without quotes to continue with currently selected numbers \r\n otherwise enter anything else");
//take user input
		confirmAnswer = this.input.next();
		//declare and initalize count variable
		int count = 0;
		//if no or No return
		if (confirmAnswer.equals("no")||confirmAnswer.equals("No")) {
			return;
			//any other answer do below
		} else {
			//ask which index to update
			System.out.println("Enter the index for the number you want to update (0,1,2,3,4,5)");
			//update changeNumber
			changeNumber = this.input.nextInt();
			//ask for new value
			System.out.println("Enter new Value");
			//updte newValue with user input
			newValue = this.input.nextInt();
			
			//run while loop for picknumbers size
			while (count < this.pickNumbers.length) {
				this.pickNumbers[changeNumber] = newValue;
				//check for double within input
				inputValidation(count);
				//update new value
				
				// increment counter

				count++;
			}

			// store our chosen numbers in a string
			String playerChosenNumbers = +this.pickNumbers[0] + " " + this.pickNumbers[1] + " " + this.pickNumbers[2] + " "
					+ this.pickNumbers[3] + " " + this.pickNumbers[4] + " " + this.pickNumbers[5];
			// set our playernumbers
			this.playerNumbers[0].setResults(playerChosenNumbers);
			ConfirmNumbers();
		}
return;
	}

	// this method is used to create an array size from our file and return it
	public int arraySizeFromFile() {

		// declare array size
		int arraySize = 0;
		// try
		// so do this else print an error
		try {
			// use buffered reader to read the file location
			// system property "user dir" finds where our application is ran from
			// + the file name
			BufferedReader fileObj = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/test.txt"));
			// currline string is for finding the line we are printing off
			String currLine;
			// read line we are using
			currLine = fileObj.readLine();
			// ensure i = 0
			int i = 0;
			// keep reading and printing if line is not blank
			while (currLine != null) {
				// read next line
				currLine = fileObj.readLine();
				// increment
				i += 1;
			}
			// set array size to i incase program needs to be ran again in future
			arraySize = i;
			// close file
			fileObj.close();
			// catch exception checks if we can do above otherwise does what we want if
			// params met
			// in this case if any exception is met it will print stacktrace
			// we can also change this to print specific exceptions
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return array size
		// this dictates how large our array is for recording purposes.
		return arraySize;
	}

	// get method for our previous results(winning numbers)
	public void GetPreviousResults() {
		// ensure buffered reader directory is null
		BufferedReader inFilePR = null;
		// ensure line reader is 0
		int lineNum = 0;
		// try to read the file
		try {
			// retrieve our previously recorded results from file
			inFilePR = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/test.txt"));
			// ensure line num is 0
			lineNum = 0;
			// read curr line in file
			String currLine = inFilePR.readLine();
			while (currLine != null) {
				// print current line
				System.out.println("Game " + (lineNum + 1) + " wining numbers: " + currLine);
				// increment
				lineNum++;
				// update current line
				currLine = inFilePR.readLine();
			}
			// close file not needed now
			inFilePR.close();
			// exception to catch errors
		} catch (Exception e) {
			// print error msg
			System.out.println(e.getMessage());
		}
		// return results
		return;
	}

	// method to retrieve player number results of previously entered numbers
	public void GetPreviousPlayerNumbers() {
		// set file reader to null
		BufferedReader inFilePN = null;
		// ensure linenum is 0
		int lineNum = 0;
		// try to write to retrieve file otherwise print exception
		try {
			// print to text file within directory
			inFilePN = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/PlayerNumbers.txt"));
			// ensure linenum is 0
			lineNum = 0;
			// read curr line
			String currLine = inFilePN.readLine();
			// loop
			while (currLine != null) {
				// print player numbers
				System.out.println("Game " + (lineNum + 1) + " Player numbers: " + currLine);
				// increment
				lineNum++;
				// update currline
				currLine = inFilePN.readLine();
			}
			// close file no longer needed
			inFilePN.close();
			// catch our exception
		} catch (Exception e) {
			// print error msg
			System.out.println(e.getMessage());
		}
		// return since end of method
		return;
	}

	// this method is used to record our player numbers
	public void RecordPlayerNumbers(int arraySize) {
		// set our outfile to null
		// this is the file we our printing our data to
		BufferedWriter outFileRPN = null;
		// try to write to the file otherwise print an error message
		try {
			// write to given file
			outFileRPN = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/PlayerNumbers.txt"));
			// set counter to 0
			int i = 0;

			while (i < arraySize) {
				// write to file our chosen numbers
				outFileRPN.write(this.playerNumbers[0].getResults() + "\n");
				// increment variable
				i++;
			}
			// close file since no longer needed
			outFileRPN.close();
			// check if above worked otherwise print error message
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// return , end of method
		return;
	}

	// this method is to save our results
	public void RecordResults(int arraySize) {

		// ensure our bufferwriter is null
		BufferedWriter outFileRR = null;
		// try to write to file other print an error
		try {
			// this line sets and writes our outfile
			outFileRR = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/test.txt"));
			// ensure counter is 0
			int i = 0;

			while (i < this.lottoNumberResults.length) {
				// write our results and retrieve from object
				outFileRR.write(this.Results[i].getResults() + "\n");
				// increment
				i++;
			}
			// close file no longer needed
			outFileRR.close();
			// catch exception/errors
		} catch (Exception e) {
			// print error
			System.out.println(e.getMessage());
		}
		// return as end of method
		return;
	}

	// this method controls how many times we simulate a lottery roll
	public void RollHowMany() {
		// ask the user how many times to simulate
		System.out.println("how many times will we simulate a lottery roll?");
		// set times rolled var to nextint
		this.timesRolled = input.nextInt();
		// this.results size to timesrolled size
		this.Results = new LottoResults[this.timesRolled];

		// negative number validation
		while (this.timesRolled < 0) {
			// ask for a positive number if a number under 0 is tried
			System.out.println("enter a positive number for the amount of times rolled");
			// set timesrolled again
			this.timesRolled = input.nextInt();
		}
		// confirm user input
		System.out.println("you've chosen to roll " + this.timesRolled + " times");

		// perform number roll count method
		NumberRollCount();

		// return , end of method
		return;
	}

	public void NumberRollCount() {
		// initalize lottonumberoll array
		int[] lottoNumberRoll;
		// declare count variables
		int count = 0;
		// initalize size of lottonumberresults array
		this.lottoNumberResults = new String[this.timesRolled];
		// set size of lotto number roll aray
		// 6 is hardcoded for how many times to print out results/number within 1
		// winning number draw
		lottoNumberRoll = new int[6];

		// perform times rolled until end of loop
		while (count < this.timesRolled) {
			// perform times rolled
			NumberRoll(count, lottoNumberRoll);
			// increment counter
			count++;

		}

		// return, end of method
		return;
	}

	// this method shows the user the results and if theyve won
	public void ShowResults() {
		// declare and set timesRolledCounter to 0
		int timesRolledCounter = 0;
		// print our chosen numbers out
		System.out.println(
				"Chosen Numbers: " + this.pickNumbers[0] + " " + this.pickNumbers[1] + " " + this.pickNumbers[2] + " "
						+ this.pickNumbers[3] + " " + this.pickNumbers[4] + " " + this.pickNumbers[5]);

		// perform while loop until we catch our times rolled member variable
		while (timesRolledCounter != this.timesRolled) {

			// print winning numbers
			System.out.println("Winning Numbers: " + this.lottoNumberResults[timesRolledCounter]);
			// update results object
			this.Results[timesRolledCounter] = new LottoResults(this.lottoNumberResults[timesRolledCounter]);
			// incrementcounter
			timesRolledCounter++;
		}
		// return, end of method
		return;
	}

	// numberroll method for doing the random maths for our lotto numbers to be
	// generated
	public void NumberRoll(int count, int[] lottoNumberRoll) {
		// declare a secondcount variable and ensure its set to 0
		int secondCount = 0;

		// while second count is less then 6
		// this is because our standard lotto numbers per line is 6
		while (6 > secondCount) {

			// create a new random object and set to randPick
			Random randPick = new Random();
			// set our lottonumberroll second count index to a random number between 0 and
			// 49(our default lotto poolSize in standard lottos)
			lottoNumberRoll[secondCount] = randPick.nextInt(this.poolSize);

			// run our validation method to ensure we dont double up on numbers
			NumberRollValidation(lottoNumberRoll, secondCount, count);

			// check for winning numbers
			chosenNumberCheck(lottoNumberRoll, secondCount, count);

			// we need to check if first time ran since after this time we are adding
			// results onto previous answer
			// we change these numbers to string so we can add them on to each other without
			// performing addition
			if (secondCount < 1) {
				// cset our lottonumberResults count index to our lottonumberroll for second
				// count and convert from int to string
				this.lottoNumberResults[count] = Integer.toString(lottoNumberRoll[secondCount]);
				// else not first time ran do below
			} else {
				// add previous numberresults of index onto new one
				this.lottoNumberResults[count] = this.lottoNumberResults[count] + " "
						+ Integer.toString(lottoNumberRoll[secondCount]);
			}
			// increment our counter
			secondCount++;

		}
		// return, end of our method
		return;

	}

	// this method is used in determining if we have chosen a winning number or not
	public void chosenNumberCheck(int[] lottoNumberRoll, int secondCount, int count) {
		// declare a new count variable
		// this is because we are still keeping track of things with previous counting
		// variables
		int extraCount = 0;

		while (extraCount < 6) {
			// check picknumbers against lottonumberrolls
			// we check this against the lottonumberroll instead of the results since we can
			// check individual int's
			if (this.pickNumbers[extraCount] == lottoNumberRoll[secondCount]) {
				// print out which numbers match
				System.out.println(
						"line number " + count + " match found! winning number " + this.pickNumbers[extraCount]);
			}
			// increment our counter
			extraCount++;
		}
		// return, end of method
		return;
	}

	// this method is used to ensure we dont generate double numbers
	public void NumberRollValidation(int[] lottoNumberRoll, int secondCount, int count) {
		// set our second count to storage
		// this is so we can keep track of our original number for validation purposes
		int storage = secondCount;
		// declare new counter and set to 0
		// again we declare a new counter since other are being used still
		int validCount = 0;

		// create new random object
		Random randPick = new Random();
		// check if second count is 1
		if (secondCount == 1) {
			// check our validCount isnt 1
			while (validCount != 1) {
				// while our picknumbers of index valid count doesnt equal our roll in storage
				// index
				while (this.pickNumbers[validCount] == lottoNumberRoll[storage]) {
					// generate a new number based on poolSize
					lottoNumberRoll[secondCount] = randPick.nextInt(this.poolSize);
					// check if storage ==secondCount
					if (storage == secondCount) {
						// reset validcount
						// this keeps us in a constant loop until a non random number is drawn
						validCount = 0;

					}
				}
				// increment counter
				validCount++;

			}
		}
		// else if is used as this is another condition but not final
		// rest of this statement is same as first but different index's to be checked
		else if (secondCount == 2) {

			while (validCount != 2) {

				while (lottoNumberRoll[validCount] == lottoNumberRoll[storage]) {

					lottoNumberRoll[secondCount] = randPick.nextInt(this.poolSize);

					if (storage == secondCount) {
						validCount = 0;

					}
				}

				validCount++;

			}
		}

		// same as previous else if
		// new values
		else if (secondCount == 3) {

			while (validCount != 3) {

				while (lottoNumberRoll[validCount] == lottoNumberRoll[storage]) {

					lottoNumberRoll[secondCount] = randPick.nextInt(this.poolSize);

					if (storage == secondCount) {
						validCount = 0;

					}
				}

				validCount++;

			}
		}
		// same as previous else if
		// new values
		else if (secondCount == 4) {

			while (validCount != 4) {

				while (lottoNumberRoll[validCount] == lottoNumberRoll[storage]) {

					lottoNumberRoll[secondCount] = randPick.nextInt(this.poolSize);

					if (storage == secondCount) {
						validCount = 0;

					}
				}

				validCount++;

			}
		}

		// same as previous statement
		// new values
		// we now use an else statement since final entry to validation
		else if (secondCount == 5) {

			while (validCount != 5) {

				while (lottoNumberRoll[validCount] == lottoNumberRoll[storage]) {

					lottoNumberRoll[secondCount] = randPick.nextInt(this.poolSize);

					if (storage == secondCount) {
						validCount = 0;

					}
				}

				validCount++;

			}
		}
		// return, end of method
		return;
	}

	// this method we use to get the users chosennumbers
	public void ChosenNumbers() {
		// declare count as 0
		int count = 0;
		// prompt user to enter number between 1 and poolsize
		System.out.println("Choose 6 lotto numbers between 1 and " + poolSize + "(enter one at a time)");

		// while our count is less then the array size we are storing our variables in
		// do below
		while (count < this.pickNumbers.length) {
			// prompt user to enter a number
			System.out.println("enter a number");
			// set the member variable picknumbers count index to the next input int in the
			// scanner
			this.pickNumbers[count] = input.nextInt();
			// run our input validation method to ensure no numbers have been already
			// entered
			inputValidation(count);
			// confirm users input
			System.out.println("you have chosen number: " + this.pickNumbers[count]);
			// increment counter

			count++;
		}
		// store our chosen numbers in a string
		String playerChosenNumbers = +this.pickNumbers[0] + " " + this.pickNumbers[1] + " " + this.pickNumbers[2] + " "
				+ this.pickNumbers[3] + " " + this.pickNumbers[4] + " " + this.pickNumbers[5];
		// set our playernumbers
		this.playerNumbers[0] = new PlayerNumbers(playerChosenNumbers);
		// return, end of method
		return;

	}

	// this method handles our inputvalidation to ensure no double numbers are
	// entered
	public void inputValidation(int count) {
		// declare anoothercounter to 0
		int validCount = 0;
		// store our count in into our storage in for validation purposes
		int storage = count;
		// check if picknumbers is between 0 and 49
		while (this.pickNumbers[count] < 0 || this.pickNumbers[count] > poolSize) {

			System.out.println("enter a number between 0 and 49");

			this.pickNumbers[count] = input.nextInt();
		}

		// check if second time ran
		if (count == 1) {
			// check 2 arrays against each other since only 2 we can do things this way
			while (this.pickNumbers[0] == this.pickNumbers[1]) {
				// tell user to enter a number that hasnt been used
				System.out.println("enter a number which hasn't been used");
				// update our picknumbers count index to nextint put into scanner
				this.pickNumbers[count] = input.nextInt();

			}
			// else if since this is not last statement
			// check if count is 2
		} else if (count == 2) {
			// check validcount doesnt equal 2
			while (validCount != 2) {
				// while valid count equals count index of picknumbers
				while (this.pickNumbers[validCount] == this.pickNumbers[count]) {
					// ask for a number which hasnt been used
					System.out.println("enter a number which hasn't been used");
					// assign new numbers to next scanner int input
					this.pickNumbers[count] = input.nextInt();
					// check if storage number == count
					if (storage == count) {
						// if it does we reset counter until a new random number is generated to prevent
						// double numbers generating
						validCount = 0;

					}
				}
				// increment counter
				validCount++;

			}
			// same as method above
			// new values
		} else if (count == 3) {

			while (validCount != 3) {

				while (this.pickNumbers[validCount] == this.pickNumbers[count]) {

					System.out.println("enter a number which hasn't been used");

					this.pickNumbers[count] = input.nextInt();

					if (storage == count) {

						validCount = 0;

					}
				}

				validCount++;

			}
			// same as method above
			// new values
		} else if (count == 4) {

			while (validCount != 4) {

				while (this.pickNumbers[validCount] == this.pickNumbers[count]) {

					System.out.println("enter a number which hasnt been used");

					this.pickNumbers[count] = input.nextInt();

					if (storage == count) {

						validCount = 0;

					}
				}

				validCount++;

			}

			// else statement since final if
		} else if (count == 5) {

			while (validCount != 5) {

				while (this.pickNumbers[validCount] == this.pickNumbers[count]) {

					System.out.println("enter a number which hasnt been used");

					this.pickNumbers[count] = input.nextInt();

					if (storage == count) {

						validCount = 0;

					}
				}

				validCount++;

			}

		}
		// return,end of method
		return;
	}

}
