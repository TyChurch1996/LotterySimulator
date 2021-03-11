//playernumber class
public class PlayerNumbers {
//create member variable
	private String ChosenNumbers;

	// create constructor,always ran
	public PlayerNumbers(String chosenNumbers) {
		// declare variables here since instanced and creates from scratch each time
		// object initalized
		this.ChosenNumbers = chosenNumbers;
		return;

	}

	public void setResults(String chosenNumbers) {
		this.ChosenNumbers=chosenNumbers;
		return;
		
	}
	
	// getter method
	public String getResults() {
		// return chosennumber string
		return this.ChosenNumbers;
	}
}
