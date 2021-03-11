//lottoResults Class
public class LottoResults {
//member variable of results class
	private String results;

	// constructor for class , always ran
	public LottoResults(String results) {
		// variables set here since they set new every instance
		this.results = results;
		return;
	}

	// getter for results object
	public String getResults() {
		// return results
		return this.results;
	}

}
