package demos.nehe.lesson39;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pepijnve
 * Date: Sep 21, 2003
 * Time: 1:12:19 PM
 * To change this template use Options | File Templates.
 */
class Simulation {
    private List masses = new ArrayList();									// masses

    public Simulation()			// Constructor creates some masses with mass values m
    {
    }

    public void addMass(Mass mass) {
        masses.add(mass);
    }

    public void removeMass(Mass mass) {
        masses.remove(mass);
    }

    public Iterator getMasses() {
        return masses.iterator();						// get the mass at the index
    }

    public void init()								// this method will call the init() method of every mass
    {
        for (int i = 0; i < masses.size(); i++) {
            Mass mass = (Mass) masses.get(i);       // We will init() every mass
            mass.init();                            // call init() method of the mass
        }
    }

    public void solve()							// no implementation because no forces are wanted in this basic container
    {
        // in advanced containers, this method will be overrided and some forces will act on masses
    }

    public void simulate(float dt)					// Iterate the masses by the change in time
    {
        for (int i = 0; i < masses.size(); i++) {   // We will iterate every mass
            Mass mass = (Mass) masses.get(i);
            mass.simulate(dt);                  // Iterate the mass and obtain new position and new velocity
        }
    }

    public void operate(float dt)					// The complete procedure of simulation
    {
        init();										// Step 1: reset forces to zero
        solve();									// Step 2: apply forces
        simulate(dt);								// Step 3: iterate the masses by the change in time
    }
}
