package code;
/**
 * Ngrams predictive probabilities for text
 */
public class Ngrams {
	/**
	 * The constructor would be a good place to compute and store the Ngrams probabilities.
	 * Take uncompressed input as a text string, and store a List of Maps. The n-th such
     * Map has keys that are prefixes of length n. Each value is itself a Map, from 
     * characters to floats (this is the probability of the char, given the prefix).
	 */
    List<Map<String,Map<char,float>>> ngram;  /* nb. suggestion only - you don't have to use 
                                                     this particular data structure */

	public void Ngrams(String input) {
		// TODO fill this in.
	}

	/**
	 * Take a string, and look up the probability of each character in it, under the Ngrams model.
     * Returns a List of Floats (which are the probabilities).
	 */
	public List <float> findCharProbs(String mystring) {
		// TODO fill this in.
		return;
	}

	/**
	 * Take a list of probabilites (floats), and return the sum of the logs (base 2) in the list.
	 */
	public float calcTotalLogProb(List<float> charProbs) {
		// TODO fill this in.
		return;
	}
}
