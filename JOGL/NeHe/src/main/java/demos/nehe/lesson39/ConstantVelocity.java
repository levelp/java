package demos.nehe.lesson39;

/*
  class ConstantVelocity is derived from class Simulation
  It creates 1 mass with mass value 1 kg and sets its velocity to (1.0f, 0.0f, 0.0f)
  so that the mass moves in the x direction with 1 m/s velocity.
*/

class ConstantVelocity extends Simulation {
    public ConstantVelocity() 			//Constructor firstly constructs its super class with 1 mass and 1 kg
    {
        Mass mass = new Mass(1.0f);
        mass.pos = new Vector3D(0.0f, 0.0f, 0.0f);		//a mass was created and we set its position to the origin
        mass.vel = new Vector3D(1.0f, 0.0f, 0.0f);		//we set the mass's velocity to (1.0f, 0.0f, 0.0f)
        addMass(mass);
    }

}
