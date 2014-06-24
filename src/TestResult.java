/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Enumeration class TestResult - write a description of the enum class here
 * 
 * @author bala
 * @version (version number or date here)
 */
public enum TestResult {

    PASS, FAIL;

    @Override
    public String toString() {
        switch (this) {

            case PASS:
                return "Pass";
            case FAIL:
                return "---";
            default:
                throw new IllegalArgumentException();
        }
    }
}
