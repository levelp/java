#include <iostream>

using namespace std;

int main(){
  int a = 3, b = 10;

  a ^= b ^= a ^= b;

  cout << "a = " << a << "   b = " << b << endl;

  a = 3; b = 10;

  a ^= (b ^= (a ^= b));

  cout << "a = " << a << "   b = " << b << endl;

  return 0;
}