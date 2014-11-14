package demos.nehe.lesson39;

/**
 * Created by IntelliJ IDEA.
 * User: pepijnve
 * Date: Sep 21, 2003
 * Time: 1:06:04 PM
 * To change this template use Options | File Templates.
 */
class Mass {
    float m;									// The mass value
    Vector3D pos;								// Position in space
    Vector3D vel;								// Velocity
    Vector3D force;								// Force applied on this mass at an instance

    public Mass(float m)								// Constructor
    {
        this.m = m;
        pos = new Vector3D();
        vel = new Vector3D();
        force = new Vector3D();
    }

    /*
      void applyForce(Vector3D force) method is used to add external force to the mass.
      At an instance in time, several sources of force might affect the mass. The vector sum
      of these forces make up the net force applied to the mass at the instance.
    */
    public void applyForce(Vector3D force) {
        this.force.add(force);					// The external force is added to the force of the mass
    }

    /*
      void init() method sets the force values to zero
    */
    public void init() {
        force.x = 0;
        force.y = 0;
        force.z = 0;
    }

    /*
      void simulate(float dt) method calculates the new velocity and new position of
      the mass according to change in time (dt). Here, a simulation method called
      "The Euler Method" is used. The Euler Method is not always accurate, but it is
      simple. It is suitable for most of physical simulations that we know in common
      computer and video games.
    */
    public void simulate(float dt) {
        Vector3D forceScaled = new Vector3D(this.force);
        vel.add(forceScaled.scale(dt / m));				// Change in velocity is added to the velocity.
        // The change is proportinal with the acceleration (forceScaled / m) and change in time

        Vector3D velScaled = new Vector3D(this.vel);
        pos.add(velScaled.scale(dt));						// Change in position is added to the position.
        // Change in position is velocity times the change in time
    }

}
