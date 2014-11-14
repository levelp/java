package demos.nehe.lesson44;

/**
 * @author Abdul Bezrati
 */
class Tuple3f{
    float x, y, z;

    public Tuple3f(){
      x = y = z = 0;
    }

    public Tuple3f(Tuple3f p){
      x = p.x;
      y = p.y;
      z = p.z;
    }

    public Tuple3f(float x, float y, float z){
      this.x = x;
      this.y = y;
      this.z = z;
    }

    public void set(float x, float y, float z){
      this.x = x;
      this.y = y;
      this.z = z;
    }

    public void set(Tuple3f vec){
      x = vec.x;
      y = vec.y;
      z = vec.z;
    }

    public void cross(Tuple3f u,Tuple3f v){
      x =  (u.y*v.z) - (u.z*v.y);
      y =  (u.z*v.x) - (u.x*v.z);
      z =  (u.x*v.y) - (u.y*v.x);
    }

    public float Dot(Tuple3f u){
      return  u.x*this.x +
              u.y*this.y +
              u.z*this.z;
    }

    public void add(Tuple3f u,Tuple3f v){
      x =  u.x + v.x;
      y =  u.y + v.y;
      z =  u.z + v.z;
    }

    public void scaleAdd(float f,Tuple3f u,Tuple3f v){
      x = f*u.x + v.x;
      y = f*u.y + v.y;
      z = f*u.z + v.z;
    }

    public void add(Tuple3f u){
      x +=  u.x;
      y +=  u.y;
      z +=  u.z;
    }

    public void add(float a){
      x += a;
      y += a;
      z += a;
    }

    public void sub(Tuple3f u,Tuple3f v){
      x =  u.x - v.x;
      y =  u.y - v.y;
      z =  u.z - v.z;
    }

    public void sub(Tuple3f u){
      x -=  u.x;
      y -=  u.y;
      z -=  u.z;
    }

    public void scale(float mul){
      x*=   mul;
      y*=   mul;
      z*=   mul;
    }

    public void scale(float mul, Tuple3f v){
      x =   mul*v.x;
      y =   mul*v.y;
      z =   mul*v.z;
    }

    public void mul(Tuple3f v1,Tuple3f v2){
      x =   v1.x*v2.x;
      y =   v1.y*v2.y;
      z =   v1.z*v2.z;
    }

    public void normalize(){
      float length = (float)Math.sqrt(x*x + y*y + z*z);
      x/=length;
      y/=length;
      z/=length;
    }

    public float distance(Tuple3f u){
      return (float)Math.sqrt((this.x - u.x)*(this.x - u.x) +
                              (this.y - u.y)*(this.y - u.y) +
                              (this.z - u.z)*(this.z - u.z));
	}

    public float length(){
      return (float)Math.sqrt(x*x + y*y + z*z);
    }

    public void negate(){
      x = - x;
      y = - y;
      z = - z;
    }
  }
