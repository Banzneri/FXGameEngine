package de.eppleton.fx2d.samplegames.bck2brwsr;

import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/** Demonstrating POJO testing of HTML page model. Runs in good old HotSpot
 * as it does not reference any HTML elements or browser functionality. Just
 * operates on the page model.
 *
 * @author Jaroslav Tulach <jtulach@netbeans.org>
 */
public class AppTest {
    private Index model;
    

    @BeforeMethod
    public void initModel() {
        model = new Index().applyBindings();
    }

 
}
