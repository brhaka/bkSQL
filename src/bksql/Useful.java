/**
 *
 * @author Brhaka - Eduardo J Martinez
 */

package bksql;

public class Useful {
    protected static Boolean IsNullOrEmpty(String str) {
        if(str != null && !str.isEmpty()) {
            return true;
        }
        return false;
    }

    protected static Boolean IsNotNullOrEmpty(String str) {
        if(str != null && !str.isEmpty()) {
            return false;
        }
        return true;
    }
}
