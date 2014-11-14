package demos.nehe.lesson39;

import java.util.Iterator;

/*
  class MassConnectedWithSpring is derived from class Simulation
  It creates 1 mass with mass value 1 kg and binds the mass to an arbitrary constant point with a spring.
  This point is refered as the connectionPos and the spring has a springConstant value to represent its
  stiffness.
*/

class MassConnectedWithSpring extends Simulation {
    float springConstant;													//more the springConstant, stiffer the spring force
    Vector3D connectionPos;													//the arbitrary constant point that the mass is connected

    public MassConnectedWithSpring(float springConstant)                    //Constructor firstly constructs its super class with 1 mass and 1 kg
    {
        this.springConstant = springConstant;								//set the springConstant

        connectionPos = new Vector3D(0.0f, -5.0f, 0.0f);						//set the connectionPos

        Mass mass = new Mass(1.0f);
        mass.pos = new Vector3D(connectionPos).add(new Vector3D(10.0f, 0.0f, 0.0f));		//set the position of the mass 10 meters to the right side of the connectionPos
        mass.vel = new Vector3D(0.0f, 0.0f, 0.0f);						//set the velocity of the mass to zero
        addMass(mass);
    }

    public void solve()													//the spring force will be applied
    {
        Iterator masses = getMasses();                                      //we will apply force to all masses (actually we have 1 mass, but we can extend it in the future)
        while (masses.hasNext()) {
            Mass mass = (Mass) masses.next();
            Vector3D springVector = new Vector3D(mass.pos).sub(connectionPos);			//find a vector from the position of the mass to the connectionPos
            mass.applyForce(springVector.inverse().scale(springConstant));			//apply the force according to the famous spring force formulation
        }
    }
}
